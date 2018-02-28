package cn.mdni.business.service.api;


import cn.mdni.business.constants.*;
import cn.mdni.business.dto.material.MaterialChangeItem;
import cn.mdni.business.dto.material.MaterialItem;
import cn.mdni.business.dto.material.PushMaterialChangeDto;
import cn.mdni.business.dto.material.PushMaterialDto;
import cn.mdni.business.entity.finance.ProjectPayplanStage;
import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.business.entity.material.ProjectChangeMaterial;
import cn.mdni.business.entity.material.ProjectMaterial;
import cn.mdni.business.entity.material.SmMaterialChangeAuditRecord;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.business.entity.operatelog.RequestOutapiLog;
import cn.mdni.business.service.finance.PayplanService;
import cn.mdni.business.service.finance.ProjectPayplanStageService;
import cn.mdni.business.service.material.SmChangeDetailService;
import cn.mdni.business.service.material.MongoCustomerContractService;
import cn.mdni.business.service.material.ProjectMaterialService;
import cn.mdni.business.service.material.SmMaterialChangeAuditRecordService;
import cn.mdni.business.service.operatelog.RequestOutapiLogService;
import cn.mdni.business.service.orderflow.ContractService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.commons.json.JsonUtils;
import cn.mdni.core.dto.StatusDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description: 调用外部接口Service
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/12/11.
 */
@Service
public class OutApiService  {

    @Autowired
    private ContractService contractService;
    @Autowired
    private PayplanService payplanService;
    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;
    @Autowired
    private ProjectMaterialService projectMaterialService;
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;
    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private RequestOutapiLogService requestOutapiLogService;
    @Autowired
    private SmMaterialChangeAuditRecordService smMaterialChangeAuditRecordService;


    /**
     * post 调用外部接口
     * @param map 调用参数
     * @param outapi 外部接口信息
     * @param needCreateKey 是否需要生成加密key
     * @return  返回true 调用成功
     */
    public boolean postOutApi(Map<String,Object> map,OutApiEnum outapi,boolean needCreateKey) {
        boolean result = false;
        RequestOutapiLog  requestOutapiLog = new RequestOutapiLog();
        requestOutapiLog.setPushType(outapi.toString());
        try {
            if(map != null){
                if(needCreateKey){
                    String signKey= SignatureUtils.getKey(map,"");
                    MapUtils.putNotNull(map,"key",signKey);
                }
                String response = HttpUtils.post(outapi.getApiurl(),map);
                requestOutapiLog.setPushContent(JsonUtils.pojoToJson(map));
                requestOutapiLog.setResponseContent(response);
                Map<String,Object> mapResponse = JsonUtils.fromJson(response,HashMap.class);
                if(null == mapResponse || !mapResponse.containsKey("code")){

                }else if(mapResponse.get("code").toString().equals("200") || mapResponse.get("code").toString().equals("1")) {
                    result = true;
                }
            }
        }catch (Exception e){
            result = false;
            e.printStackTrace();
        }
        requestOutapiLog.setNeedAgainPush(result ? 0 : 1);
        requestOutapiLogService.insertRequestLog(requestOutapiLog);
        return result;
    }
    /**
     * 签约完成，推送项目信息 到 产业工人系统
     * @param contractCode
     */
    public boolean pushProjectToMps(String contractCode){
        return contractService.pushProjectToMps(contractCode);
    }

    /**
     * todo  只有北京推送，其他门店待定
     * 结束收款 推送中期款 尾款  到产业工人系统
     * @param projectPayplanStage
     * @return
     */
    public void pushAmountToMps(ProjectPayplanStage projectPayplanStage) {

        Map<String,Object> map = new HashedMap();
        String type = "";
        String paymentStatus = "";
        switch (projectPayplanStage.getStageTemplateCode()){
            case "NODE_MEDIUM":
                type = "401";break;
            case "NODE_FINAL":
                type = "402";break;
        }
        if(projectPayplanStage.getFinishMode().equals(OpreatModeEnum.MANUAL.toString())){
            //手动结束
            paymentStatus = "30";
        } else if(projectPayplanStageService.calculateStageSummaryExpectedReceived(projectPayplanStage).compareTo(BigDecimal.ZERO) == 1){
            //自动结束 未交满
            paymentStatus = "20";
        }else {
            //自动结束 交满
            paymentStatus = "10";
        }
        MapUtils.putNotNull(map,"type",type);
        MapUtils.putNotNull(map,"paymentStatus",paymentStatus);
        MapUtils.putNotNull(map,"orderId",projectPayplanStage.getContractCode());
        MapUtils.putNotNull(map,"time",DateUtils.parseStr(projectPayplanStage.getFinishTime(),DateUtils.DF_YMD_EN));
        MapUtils.putNotNull(map,"amount",projectPayplanStage.getActualTotalReceived());
        String signKey= SignatureUtils.getKey(map,"&");
        MapUtils.putNotNull(map,"key",signKey);
        if(payplanService.getNeedPushToMps(projectPayplanStage.getStageTemplateId().longValue())){
            this.postOutApi(map,OutApiEnum.PUSH_AMMOUNT,false);
        }
    }

    /**
     * 推送选材单 到产业工人系统
     * @param contractCode
     * @return
     */
    public void pushMaterialToMps(String contractCode) {
        try {
            //创建线程
            Thread thread = new Thread(() -> {
                Map<String,Object> map = new HashedMap();
                PushMaterialDto pushMaterialDto = this.getPushMaterial(contractCode);
                if(null != pushMaterialDto && null != pushMaterialDto.getMaterialInfo()) {
                    map = JsonUtils.fromJson(JsonUtils.pojoToJson(pushMaterialDto),HashMap.class);
                    String signKey= SignatureUtils.getKey(map,"");
                    MapUtils.putNotNull(map,"key",signKey);
                    String pushJson = JsonUtils.pojoToJson(map);
                    Map<String,Object> mapPush = new HashMap<String,Object>();
                    MapUtils.putNotNull(mapPush,"data",pushJson);
                    this.postOutApi(mapPush,OutApiEnum.PUSH_MATERIALS,false);
                }
            });
            //执行
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 推送主材变更单 到产业工人系统
     * @param contractCode
     * @param changVersionNo
     * @return
     */
    public void pushMaterialChangeToMps(String contractCode,String changVersionNo) {
        //创建线程
        Thread thread = new Thread(() -> {
            List<SmChangeDetail> smChangeDetails = smChangeDetailService.findChangeDetail(contractCode,changVersionNo);
            for (SmChangeDetail smChangeDetail:smChangeDetails) {
                this.pushMaterialChangeToMpsByChangeNo(smChangeDetail);
            }
        });
        //执行
        thread.start();
        this.pushMaterialToMps(contractCode);
    }

    /**
     * 处理老订单系统项目
     * @param requestKey 请求参数
     * @param outapi 请求接口
     * @return
     */
    public Object makeOldOrderProject(String requestKey,OutApiEnum outapi){
        Map<String,Object> map = new HashMap();
        map.put("requestKey",requestKey);
        boolean isOk = this.postOutApi(map,outapi,false);
        if(isOk){
            return  StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
        }else {
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),"该项目不存在");
        }
    }

    /**
     * 获取 推送选材单材料信息
     * @param contractCode
     * @return
     */
    private PushMaterialDto getPushMaterial(String contractCode){
        PushMaterialDto pushMaterialDto = new PushMaterialDto();
        Map<String,Object> map = new HashedMap();
        MapUtils.putNotNull(map,"contractCode",contractCode);
        List<MaterialItem> materialItemList = new ArrayList<>();
        List<ProjectMaterial> materialList = projectMaterialService.findWithSubListByMaterialParams(map);

        for (ProjectMaterial projectMaterial:materialList) {
            if(projectMaterial.getCategoryDetailCode().equals(SelectMaterialTypeEnmu.MAINMATERIAL.toString())
                    || projectMaterial.getCategoryDetailCode().equals(SelectMaterialTypeEnmu.UPGRADEITEM.toString())
                    || projectMaterial.getCategoryDetailCode().equals(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString())){
                MaterialItem materialItem = this.makeMaterialItem(projectMaterial);
                materialItemList.add(materialItem);
            }
        }
        pushMaterialDto.setOrderNumber(contractCode);
        pushMaterialDto.setMaterialInfo(materialItemList);
        return pushMaterialDto;
    }

    /**
     * 将sm 系统 材料信息 转换为 mps 系统所需信息
     * @param projectMaterial
     * @return
     */
    private MaterialItem makeMaterialItem(ProjectMaterial projectMaterial) {

        StringBuffer position = new StringBuffer();
        BigDecimal budgetNumber = BigDecimal.ZERO;
        BigDecimal includeLossNumber = BigDecimal.ZERO;
        BigDecimal budgetArea = BigDecimal.ZERO;

        for (SmSkuDosage smSkuDosage:projectMaterial.getSkuDosageList()) {
            if(position.indexOf(smSkuDosage.getDomainName())== -1)
            {
                position.append(smSkuDosage.getDomainName()).append(",");
            }
            //不为空说明是平米转片
            if(StringUtils.isNotBlank(smSkuDosage.getConvertUnit())){
                budgetArea = budgetArea.add(smSkuDosage.getBudgetDosage());
                //includeLossNumber = includeLossNumber.add(smSkuDosage.getBudgetDosage().multiply(smSkuDosage.getLossFactor()));
                projectMaterial.setMaterialUnit("㎡");
            }
            budgetNumber = budgetNumber.add(smSkuDosage.getNoLossDosage());
            includeLossNumber = includeLossNumber.add(smSkuDosage.getLossDosage());
        }
        MaterialItem materialItem = new MaterialItem();
        materialItem.setMaterialsChoiceType(SelectMaterialTypeEnmu.getEnumFromString(projectMaterial.getCategoryCode()).getLabel());
        materialItem.setMaterialsChoiceCategoryCode(projectMaterial.getProductCategoryUrl());
        materialItem.setSupplierCode(projectMaterial.getSupplierCode());
        materialItem.setSupplierName(projectMaterial.getSupplierName());
        materialItem.setBrand(projectMaterial.getBrand());
        materialItem.setAttribute(this.getAttrByMaterial(projectMaterial.getAttribute1(),projectMaterial.getAttribute2(),projectMaterial.getAttribute3()));

        //截取单位  元/个，元/㎡  截取后 取
        String[] unit = projectMaterial.getMaterialUnit().split("/");
        if(unit.length > 0) {
            materialItem.setUnit(unit[unit.length-1]);
        }else {
            materialItem.setUnit("");
        }
        materialItem.setSpec(projectMaterial.getSkuSqec() == null ? "" : projectMaterial.getSkuSqec());
        materialItem.setModel(projectMaterial.getSkuModel() == null ? "" : projectMaterial.getSkuModel());
        materialItem.setPosition(position.toString());
        materialItem.setBudgetNumber(budgetNumber.toString());
        materialItem.setArea(budgetArea.toString());
        materialItem.setIncludeLossNumber(includeLossNumber.toString());
        if( null != projectMaterial.getSkuDosageList()){
            BigDecimal lossRatio = projectMaterial.getSkuDosageList().get(0).getLossFactor();
            materialItem.setLossRatio(lossRatio == null ? "0" : lossRatio.toString());
        }
        return materialItem;
    }

    /**
     * 推送主材变更单 到产业工人系统
     * @param smChangeDetail 变更单明细
     * @return
     */
    private void pushMaterialChangeToMpsByChangeNo(SmChangeDetail smChangeDetail){
        Map<String,Object> map = new HashedMap();
        try {
            PushMaterialChangeDto pushMaterialChangeDto = this.makeMaterialChange(smChangeDetail);
            if( null != pushMaterialChangeDto && null != pushMaterialChangeDto.getChangeItemInfo()){
                map = JsonUtils.fromJson(JsonUtils.pojoToJson(pushMaterialChangeDto),HashMap.class);
                String signKey= SignatureUtils.getKey(map,"");
                MapUtils.putNotNull(map,"key",signKey);
                String pushJson = JsonUtils.pojoToJson(map);
                Map<String,Object> mapPush = new HashMap<String,Object>();
                MapUtils.putNotNull(mapPush,"data",pushJson);
                this.postOutApi(mapPush,OutApiEnum.PUSH_CHANGE_MATERIAL,false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 处理变更单信息
     * @param smChangeDetail 变更单明细
     * @return
     */
    private PushMaterialChangeDto makeMaterialChange(SmChangeDetail smChangeDetail) {
        PushMaterialChangeDto pushMaterialChangeDto = new PushMaterialChangeDto();
        SmMaterialChangeAuditRecord smMaterialChangeAuditRecord = smMaterialChangeAuditRecordService.getRecordByChangeNo(smChangeDetail.getChangeNo());
        List<ProjectChangeMaterial> proMatrlByContrCodeList = new ArrayList<>();
        List<SmSkuChangeDosage> dosageByContrCodeList = new ArrayList<>();
        proMatrlByContrCodeList = mongoCustomerContractService.findAllMaterial(smChangeDetail.getContractCode(), smChangeDetail.getChangeNo());
        dosageByContrCodeList = mongoCustomerContractService.findAllDosage(smChangeDetail.getContractCode(), smChangeDetail.getChangeNo());
        pushMaterialChangeDto.setOrderNumber(smChangeDetail.getContractCode());
        pushMaterialChangeDto.setChangeBillCode(smChangeDetail.getChangeNo());
        pushMaterialChangeDto.setChangeApplyDate(DateUtils.parseStr(smChangeDetail.getCreateTime(),DateUtils.DF_YMD_EN));
        pushMaterialChangeDto.setDesignerName(smChangeDetail.getCreateUser());
        pushMaterialChangeDto.setChangeReason("");
        if(null != smMaterialChangeAuditRecord){
            pushMaterialChangeDto.setChangeCheckedDate(DateUtils.parseStr(smMaterialChangeAuditRecord.getAuditTime(),DateUtils.DF_YMD_EN));
            pushMaterialChangeDto.setCheckerName(smMaterialChangeAuditRecord.getAuditUser());
        }else{
            pushMaterialChangeDto.setChangeCheckedDate(DateUtils.parseStr(DateUtils.currentDate(),DateUtils.DF_YMD_EN));
            pushMaterialChangeDto.setCheckerName("");
        }
        if(StringUtils.isNotBlank(smChangeDetail.getChangeCategoryUrl())){
            pushMaterialChangeDto.setChangeItemInfo(this.makeMaterialChangeItemList(proMatrlByContrCodeList,dosageByContrCodeList));
        }
        return pushMaterialChangeDto;
    }

    /**
     * 处理产业工人所需 变更单子项
     * @param projectChangeMaterials 主材信息
     * @param smSkuChangeDosages 用量信息
     * @return
     */
    private List<MaterialChangeItem> makeMaterialChangeItemList(List<ProjectChangeMaterial> projectChangeMaterials,List<SmSkuChangeDosage> smSkuChangeDosages) {
        List<MaterialChangeItem> materialChangeItemList = new ArrayList<>();
        //把用量根据材料id分组对应
        Map<String, List<SmSkuChangeDosage>> collect = smSkuChangeDosages.stream().collect(Collectors.groupingBy(SmSkuChangeDosage::getProjectMaterialId));
        for (ProjectChangeMaterial projectChangeMaterial:projectChangeMaterials) {
            MaterialChangeItem materialChangeItem = new MaterialChangeItem();
            List<SmSkuChangeDosage> smSkuChangeDosageList = collect.get(projectChangeMaterial.getId());
            BigDecimal oldDosage = smSkuChangeDosageList.stream().filter(b -> b.getOriginalDosage() != null).map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal nowDosage = smSkuChangeDosageList.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal lastDosage = nowDosage.subtract(oldDosage);
            //原用量 大于现用量
            if(oldDosage.compareTo(nowDosage) == 1){
                materialChangeItem.setChangeType("2");
            }else if (oldDosage.compareTo(nowDosage) == -1) {
                materialChangeItem.setChangeType("1");
            }else {
                continue;
            }
            // 平米转片的   将片再转换为平米 推送给产业工人
            if(StringUtils.isNotBlank(smSkuChangeDosageList.get(0).getConvertUnit())){
                if (StringUtils.isNotBlank(projectChangeMaterial.getSkuSqec())) {//如果规格不为空，则把规格进行处理
                    String skuSqec = projectChangeMaterial.getSkuSqec();
                    //去掉空格
                    skuSqec = skuSqec.replace(" ", "");
                    if (skuSqec.indexOf("X") > 0 || skuSqec.indexOf("x") > 0) {
                        skuSqec = skuSqec.replaceAll("X", "*");
                        skuSqec = skuSqec.replaceAll("x", "*");
                    }
                    String[] split = skuSqec.split("\\*");
                    //规格
                    BigDecimal sqec = BigDecimal.ONE;
                    if (split != null && split.length > 0) {
                        for (String a : split) {
                            sqec = new BigDecimal(a);
                            sqec = sqec.multiply(sqec);
                        }
                        sqec = sqec.divide(new BigDecimal(1000000));
                    }
                    BigDecimal dosage = BigDecimal.ZERO;
                    dosage = lastDosage.multiply(sqec);
                    lastDosage = dosage;
                    materialChangeItem.setUnit("㎡");
                }
            }else {
                materialChangeItem.setUnit(projectChangeMaterial.getMaterialUnit());
            }

            materialChangeItem.setChangeNumber(lastDosage.toString());
            materialChangeItem.setMaterialsChoiceCategoryCode(projectChangeMaterial.getProductCategoryUrl());
            materialChangeItem.setBrand(projectChangeMaterial.getBrand());
            materialChangeItem.setAttribute(getAttrByMaterial(projectChangeMaterial.getAttribute1(),projectChangeMaterial.getAttribute2(),projectChangeMaterial.getAttribute3()));
            materialChangeItem.setSpec(projectChangeMaterial.getSkuSqec());
            materialChangeItem.setModel(projectChangeMaterial.getSkuModel());
            materialChangeItem.setMaterialsChoiceType(SelectMaterialTypeEnmu.getEnumFromString(projectChangeMaterial.getCategoryCode()).getLabel());
            materialChangeItemList.add(materialChangeItem);
        }
        return  materialChangeItemList;
    }

    /**
     * 获取材料属性
     */
    private String getAttrByMaterial(String attribute1,String attribute2,String attribute3){
        StringBuffer attr = new StringBuffer();
        if(StringUtils.isNotBlank(attribute1)){
            attr.append(attribute1).append(",");
        }
        if(StringUtils.isNotBlank(attribute2)){
            attr.append(attribute2).append(",");
        }
        if(StringUtils.isNotBlank(attribute3)){
            attr.append(attribute3).append(",");
        }
        String strResult = attr.toString();
        if(strResult.length() > 0){
            strResult = strResult.substring(0,strResult.length()-2);
        }
        return  strResult;
    }


}
