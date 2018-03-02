package cn.damei.business.service.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.constants.PrepareOrderStatusEnum;
import cn.damei.business.constants.SelectMaterialTypeEnmu;
import cn.damei.business.dao.material.ProdSkuDao;
import cn.damei.business.dao.material.IndentPrepareOrderDao;
import cn.damei.business.dao.material.IndentPrepareOrderItemDao;
import cn.damei.business.dao.material.PrepareOrderAbnormalLogDao;
import cn.damei.business.dao.material.ProjectMaterialDao;
import cn.damei.business.entity.commodity.prodsku.ProdSku;
import cn.damei.business.entity.material.IndentPrepareOrder;
import cn.damei.business.entity.material.IndentPrepareOrderItem;
import cn.damei.business.entity.material.PrepareOrderAbnormalLog;
import cn.damei.business.entity.material.ProjectMaterial;
import cn.damei.business.entity.material.SmSkuDosage;
import cn.damei.core.base.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndentPrepareOrderService extends CrudService<IndentPrepareOrderDao, IndentPrepareOrder> {

    private Logger logger = LoggerFactory.getLogger(IndentPrepareOrderService.class);
    @Autowired
    private PrepareOrderAbnormalLogDao prepareOrderAbnormalLogDao;
    @Autowired
    private ProjectMaterialDao projectMaterialDao;
    @Autowired
    private IndentPrepareOrderDao indentPrepareOrderDao;
    @Autowired
    private IndentPrepareOrderItemDao indentPrepareOrderItemDao;
    @Autowired
    private ProdSkuDao prodSkuDao;
    @Autowired
    private DataSourceTransactionManager txManager;


    /**
     * 异步生成 预备单 :
     * 1.通过合同code,查询到所有主材及用量集合;
     * 2.将结果按品牌拆分,并生成对应的预备单;一个品牌id,一个预备单
     * 3.对每个主材对skuCode进行分组,生成对应的预备单item;一个skuCode,一个预备单,对其用量其他数据合同并!
     * 4.创建出异常后,记录到 选材数据转换预备订单异常日志表中,并回滚之前所有插入操作
     * 5.为了避免for循环中频繁插入预备单,故先遍历一次,批量插入后,将branId作为键,预备单对象为值,做map,
     *      后遍历中匹配brandId, 从而是预备单与预备单item相对应,进而item中保存主表外键 prepareOrderId
     * 6.新线程spring事务监听不到, 需要手动开始事务!
     * @param contractCode 合同编码
     * @param dataSource   数据来源(选材/变更)
     */
    public void asynCreatePrepareOrder(String contractCode, String dataSource){
        if (StringUtils.isAnyBlank(contractCode, dataSource)) {
            logger.error("生成预备单失败,原因: 合同编码或者数据来源为空!");
            throw new IllegalArgumentException("生成预备单失败,原因: 合同编码或者数据来源为空!");
        }
        //创建线程
        Thread thread = new Thread(() -> {
            LocalDateTime now = LocalDateTime.now();
            /*===手动开启事务===*/
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            // 事物隔离级别，开启新事务
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            // 获得事务状态
            TransactionStatus status = txManager.getTransaction(def);

            try {

                //一.查询到所有主材及用量集合
                Map<String, Object> materialParams = new HashMap<String, Object>();
                materialParams.put("contractCode", contractCode);
                List<ProjectMaterial> materialList = projectMaterialDao.findWithSubListByMaterialParams(materialParams);
                //二.将结果按品牌id拆分(过滤掉为减项的)
                Map<Long, List<ProjectMaterial>> brandMap = materialList.stream()
                        .filter(material -> material.getBrandId() != null
                                && !SelectMaterialTypeEnmu.REDUCEITEM.toString().equals(material.getCategoryCode()))
                        .collect(Collectors.groupingBy(material -> material.getBrandId()));


                if (brandMap != null && brandMap.size() > 0) {
                    //获取到 skuCode 集合,去查询skuId和skuName(过滤掉为减项的)
                    List<String> skuCodeList = materialList.stream()
                            .filter(material -> material.getSkuCode() != null
                                && !SelectMaterialTypeEnmu.REDUCEITEM.toString().equals(material.getCategoryCode()))
                            .map(ProjectMaterial::getSkuCode)
                            .distinct()
                            .collect(Collectors.toList());

                    //查询
                    List<ProdSku> skuListByCodeList = new ArrayList<ProdSku>();
                    Map<String, ProdSku> skuMapTemp = new HashMap<String, ProdSku>();

                    if (skuCodeList != null && skuCodeList.size() > 0) {
                        skuListByCodeList = prodSkuDao.findSkuListByCodeList(skuCodeList);
                    }

                    if (skuListByCodeList.size() > 0) {
                        //skuCode为键, sku为对象,做映射
                        skuMapTemp = skuListByCodeList.stream().filter(sku -> StringUtils.isNotBlank(sku.getCode()))
                                .collect(Collectors.toMap(sku -> sku.getCode(), sku -> sku));
                    }

                    //skuCode为键, sku为对象
                    Map<String, ProdSku> skuMap = skuMapTemp;

                    //预备单待保存集合
                    List<IndentPrepareOrder> toSavePrepareOrderList = new ArrayList<IndentPrepareOrder>();
                    //预备单item待保存集合
                    List<IndentPrepareOrderItem> toSavePrepareOrderItemList = new ArrayList<IndentPrepareOrderItem>();

                    //一,保存 预备单,并返回主键
                    brandMap.forEach((brandId, brandMaterialList) -> {
                        if (brandMaterialList != null && brandMaterialList.size() > 0) {
                            //三.生成预备单, 一个品牌id 对应一个预备单
                            IndentPrepareOrder prepareOrder = new IndentPrepareOrder();
                            prepareOrder.setContractCode(contractCode);
                            prepareOrder.setDataSource(dataSource);
                            prepareOrder.setStatus(PrepareOrderStatusEnum.WAITING_TRANSFERRED.toString());
                            //品牌id
                            prepareOrder.setBrandId(brandId);
                            prepareOrder.setBrandName(brandMaterialList.get(0).getBrand());
                            prepareOrder.setCreateTime(now);
                            //保存预备单集合中
                            toSavePrepareOrderList.add(prepareOrder);
                        }
                    });

                    if (toSavePrepareOrderList.size() > 0) {
                        //批量保存预备单
                        indentPrepareOrderDao.batchInsertList(toSavePrepareOrderList);

                        //品牌Id,预备单映射
                        Map<Long, IndentPrepareOrder> prepareOrderWithBrandId = toSavePrepareOrderList.stream()
                                .collect(Collectors.toMap(k -> k.getBrandId(), v -> v));

                        //二.再次遍历brandMap,去生成预备单item
                        brandMap.forEach((Long brandId, List<ProjectMaterial> brandMaterialList) -> {
                            if (brandMaterialList != null && brandMaterialList.size() > 0) {
                                //对主材skuCode分组,
                                Map<String, List<ProjectMaterial>> skuCodeMap = brandMaterialList.stream().filter(m -> StringUtils.isNotBlank(m.getSkuCode()))
                                        .collect(Collectors.groupingBy(m -> m.getSkuCode()));

                                if(skuCodeMap != null && skuCodeMap.size() > 0){
                                    skuCodeMap.forEach((skuCode, mListBySkuCode) -> {
                                        if(mListBySkuCode != null && mListBySkuCode.size() > 0){
                                            //'订货数量',--预算用量之和
                                            BigDecimal quantity = BigDecimal.ZERO;
                                            //'安装位置',--每个用量功能区,拼接,去重
                                            Set<String> instLocationSet = new HashSet<String>();
                                            StringBuilder installationLocation = new StringBuilder();
                                            //'片数'(如果有平米转片存),	--loss_dosage之和
                                            BigDecimal tabletNum = BigDecimal.ZERO;

                                            //遍历skuCode所对应的集合, 计算预算用量之和/拼接安装位置/片数
                                            for(ProjectMaterial materialBySkuCode : mListBySkuCode){
                                                //获取当前主材对应的用量集合
                                                List<SmSkuDosage> skuDosagesBySkuCode = materialBySkuCode.getSkuDosageList();
                                                for (SmSkuDosage skuDosage : skuDosagesBySkuCode) {
                                                    //数量--含损用量之和
                                                    quantity = quantity.add(skuDosage.getBudgetDosage().multiply(skuDosage.getLossFactor()));
                                                    instLocationSet.add(skuDosage.getDomainName());
                                                    if (Constants.SQUARE_METER_TURN.equals(skuDosage.getConvertUnit())) {
                                                        tabletNum = tabletNum.add(skuDosage.getLossDosage());
                                                    }
                                                }
                                            }
                                            if(instLocationSet.size() > 0){
                                                instLocationSet.forEach(str -> {
                                                    installationLocation.append(str + ",");
                                                });
                                                //取出最后一个逗号
                                                if(installationLocation.lastIndexOf(",") == installationLocation.length() - 1){
                                                    installationLocation.deleteCharAt(installationLocation.length() - 1);
                                                }
                                            }
                                            /*==============一个skuCode 对应一个 预备单item================*/
                                            ProjectMaterial materialBySkuCode = mListBySkuCode.get(0);
                                            //五.生成预备单item
                                            IndentPrepareOrderItem prepareOrderItem = new IndentPrepareOrderItem();
                                            //通过预备单品牌id,拿到对应的预备单Id(主表id)
                                            prepareOrderItem.setPrepareOrderId(prepareOrderWithBrandId.get(brandId).getId());
                                            //通过skuCode在 skuMap 中获取skuId
                                            if (skuMap != null && skuMap.size() > 0) {
                                                ProdSku sku = skuMap.get(skuCode);
                                                prepareOrderItem.setSkuId(sku != null ? sku.getId() : null);
                                                prepareOrderItem.setSkuName(sku != null ? sku.getName() : null);
                                            }

                                            prepareOrderItem.setModel(materialBySkuCode.getSkuModel());
                                            prepareOrderItem.setSpec(materialBySkuCode.getSkuSqec());
                                            prepareOrderItem.setAttribute1(materialBySkuCode.getAttribute1());
                                            prepareOrderItem.setAttribute2(materialBySkuCode.getAttribute2());
                                            prepareOrderItem.setAttribute3(materialBySkuCode.getAttribute3());
                                            //商品单位
                                            prepareOrderItem.setSpecUnit(materialBySkuCode.getMaterialUnit());

                                            //订货数量
                                            prepareOrderItem.setQuantity(quantity);
                                            //安装位置
                                            prepareOrderItem.setInstallationLocation(installationLocation.toString());
                                            //片数
                                            prepareOrderItem.setTabletNum(tabletNum.longValue());

                                            //保存预备单item集合中
                                            toSavePrepareOrderItemList.add(prepareOrderItem);
                                        }
                                    });
                                }
                            }
                        });

                        if (toSavePrepareOrderItemList.size() > 0) {
                            //批量保存预备单item
                            indentPrepareOrderItemDao.batchInsertList(toSavePrepareOrderItemList);
                        }
                    }
                }
                //提交事务
                txManager.commit(status);
            } catch (Exception e) {
                e.printStackTrace();
                //事务回滚
                txManager.rollback(status);

                //异常保存到日志中
                PrepareOrderAbnormalLog abnormalLog = new PrepareOrderAbnormalLog();
                abnormalLog.setContractCode(contractCode);
                abnormalLog.setDataSource(dataSource);
                abnormalLog.setCreateTime(now);
                abnormalLog.setAbnormalContent(e.getMessage());
                prepareOrderAbnormalLogDao.insert(abnormalLog);

                logger.error("转预备单失败!已经存入预备单异常数据表中,异常原因: " + e.getMessage());
            }
        });
        //执行
        thread.start();
    }
}
