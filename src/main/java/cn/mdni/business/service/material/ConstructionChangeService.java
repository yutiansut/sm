package cn.mdni.business.service.material;

import cn.mdni.business.constants.ChangeTypeEnum;
import cn.mdni.business.constants.OutApiEnum;
import cn.mdni.business.constants.PropertyHolder;
import cn.mdni.business.constants.ResponseEnum;
import cn.mdni.business.dao.orderflow.ConstructionChangeDao;
import cn.mdni.business.dao.orderflow.ConstructionChangeDetailDao;
import cn.mdni.business.entity.material.ConstructionChange;
import cn.mdni.business.entity.material.ConstructionChangeDetail;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.service.finance.FinaPrintService;
import cn.mdni.business.service.finance.ProjectChangeMoneyService;
import cn.mdni.business.service.api.OutApiService;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.json.JsonUtils;
import cn.mdni.commons.pdf.PDFUtils;
import cn.mdni.commons.pdf.PdfDrawCell;
import cn.mdni.commons.pdf.PdfTablePrint;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 施工（基装）变更Service
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/22.
 */
@Service
public class ConstructionChangeService extends CrudService<ConstructionChangeDao,ConstructionChange> {

    @Autowired
    private ConstructionChangeDetailDao constructionChangeDetailDao;
    @Autowired
    private ConstructionChangeDao constructionChangeDao;
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProjectChangeMoneyService projectChangeMoneyService;
    @Autowired
    private FinaPrintService finaPrintService;
    @Autowired
    private OutApiService outApiService;

    private Logger logger = LoggerFactory.getLogger(ConstructionChangeService.class);

    //增项
    private static final Integer ADD_ITEM = 1;
    //减项
    private static final Integer REDUCE_ITEM = 2;

    @Transactional
    public Object syncConstructionChange(String requestKey) {
        Object result = new Object();
        try{
            ConstructionChange constructionChange = JsonUtils.fromJson(requestKey,ConstructionChange.class);
            if( null == constructionChange){
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),ResponseEnum.PARAM_ERROR.getMessage());
            }
            //判断项目是否存在
            CustomerContract customerContract = customerContractService.getByCode(constructionChange.getOrderNo());
            if( null == customerContract){
                //项目信息不存在 则向老订单系统推送
                return outApiService.makeOldOrderProject(requestKey, OutApiEnum.SYNC_CONSTRUCTION_CHANGE);
            }
            constructionChange.setContractCode(constructionChange.getOrderNo());
            constructionChange.setChangeNo(constructionChange.getConstructionChangeNo());
            constructionChange.setCreateTime(new Date());
            //获取改变更单是否存在
            ConstructionChange isExist= this.entityDao.getByChangeNo(constructionChange.getChangeNo());
            if(isExist != null) {
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),ResponseEnum.DEFEAT.getMessage());
            }
            //插入基装变更记录
            this.entityDao.insert(constructionChange);
            //完善变更项明细
            List<ConstructionChangeDetail> detailList = constructionChange.getConstructionChangeInfo();
            detailList.forEach(d -> {
                d.setChangeNo(constructionChange.getChangeNo());
                d.setCreateTime(constructionChange.getCreateTime());
            });
            //插入变更明细
            constructionChangeDetailDao.batchInsertList(detailList);
            //插入变更款
            projectChangeMoneyService.insertNewChangeMoney(constructionChange.getContractCode(),constructionChange.getChangeNo(),constructionChange.getChangeListTotalPrice(), ChangeTypeEnum.BASIC);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),ResponseEnum.ERROR.getMessage());
        }
        return  result;
    }



    /**
     *  打印或查看
     * @param ids 变更单id
     * @param isPrint  是否是打印
     * @param res
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object viewOrPrint(Long[] ids, Boolean isPrint, HttpServletRequest res) {
        //创建pdf
        String fileFullPath = getPdfs(ids);
        String imgerFullFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/home.png";
        String addWater = this.finaPrintService.addWater(imgerFullFile, fileFullPath);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        //printCount 是打印,更新打印次数
        if(isPrint){
            updatePrintCount(ids);
        }
        ResponseEntity<byte[]> responseEntity = null;
        try{
            responseEntity = new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(new File(addWater)), httpHeaders,
                    HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("基装变更打印错误!" + e);
        }
        return responseEntity;
    }

    /**
     * 批量更新打印次数
     * @param ids id数组
     */
    public void updatePrintCount(Long[] ids) {
        constructionChangeDao.updatePrintCount(ids);
    }

    /**
     * 获取基装变更列表 隐藏手机号中间四位
     * @param paramMap
     * @return
     */
    public BootstrapPage<ConstructionChange> searchConstructionChangeScrollPage(Map<String ,Object> paramMap) {
        BootstrapPage<ConstructionChange> result=this.searchScrollPage(paramMap);
        result.setRows(this.replaceAllMobile(result.getRows()));
        return  result;
    }

    /**
     * 隐藏手机号中间四位
     * @param constructionChangeList
     * @return
     */
    private List<ConstructionChange> replaceAllMobile(List<ConstructionChange> constructionChangeList) {
        constructionChangeList.forEach(p->{
            String mobile = p.getCustomerMobile();
            String secondMobile = p.getSecondContractMobile();
            if( null != mobile && mobile.length() > 7 ){
                p.setCustomerMobile(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
            if( null != secondMobile && secondMobile.length() > 7 ){
                p.setSecondContractMobile(secondMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
        });
        return constructionChangeList;
    }

    /**
     * 创建多个pdf
     * @param ids
     * @return
     */
    private String getPdfs(Long[] ids) {
        //查询变更信息集合
        List<ConstructionChange> changes = constructionChangeDao.findMoreByIds(ids);
        List<String> filePath = new ArrayList<String>();
        try{
            if(changes != null && changes.size() > 0){
                changes.forEach(change -> {
                    String tempfilePath = FileUtils.saveFilePath(UploadCategory.PDF, PropertyHolder.getUploadBaseUrl(),
                            UUID.randomUUID().toString() + "." + UploadCategory.PDF.getPath());
                    File outFile = new File(tempfilePath);
                    float[] relativeWidths = {0.1F, 0.15F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.15F};
                    PdfTablePrint pdfTablePrint = new PdfTablePrint(9, outFile, relativeWidths);
                    //pdfTablePrint.drawTableRowNoboderFlag("基装变更详情");
                    pdfTablePrint.drawTableRow("基装变更详情", PdfDrawCell.getTitleFont(), false);
                    pdfTablePrint.drawTableRowEmpty();
                    //第二联系人姓名 及 电话, 没有的话就取第一联系人的
                    String[] rowTwo = {"客户姓名", StringUtils.isNotBlank(change.getSecondContractName()) ? change.getSecondContractName() : change.getCustomerName(),
                            "客户电话",  StringUtils.isNotBlank(change.getSecondContractMobile()) ? change.getSecondContractMobile() : change.getCustomerMobile()};
                    pdfTablePrint.drawTableRowCellFillEmpty(rowTwo);
                    String applyDate = "";
                    if(change.getChangeApplyDate() != null){
                        applyDate = DateUtils.parseStrYMD(change.getChangeApplyDate());
                    }
                    String[] rowThree = {"变更单号", change.getChangeNo(), "项目编号", change.getContractCode(),
                            "变更日期", applyDate, "变更原因", change.getChangeReason()};
                    pdfTablePrint.drawTableRowCellColSpanEmpty(rowThree);
                    pdfTablePrint.drawTableRow("基装变更增加项",true);
                    String[] rowFive = {"类型", "项目名称", "单位", "数量", "损耗", "人工费", "综合单价", "总价", "说明"};
                    pdfTablePrint.drawTableRow(rowFive);
                    //子集合
                    List<ConstructionChangeDetail> detailList = change.getDetailList();
                    List<ConstructionChangeDetail> reduceDetails = null;
                    if( detailList!= null && detailList.size() > 0) {
                        //根据changeType分组
                        Map<Integer, List<ConstructionChangeDetail>> changeTypeMap = detailList.stream()
                                .filter(detail -> detail.getChangeType() != null)
                                .collect(Collectors.groupingBy(ConstructionChangeDetail :: getChangeType));
                        //增项集合
                        List<ConstructionChangeDetail> addDetails = changeTypeMap.get(ADD_ITEM);
                        //减项集合
                        reduceDetails = changeTypeMap.get(REDUCE_ITEM);
                        if (addDetails != null && addDetails.size() > 0) {
                            //遍历增项集合,并填充数据
                            addDetails.forEach(detail -> {
                                String[] rowAdd = {"增项", detail.getChangeProjectName(), detail.getUnit(),
                                        String.valueOf(detail.getAmount()), String.valueOf(detail.getLoss()),
                                        String.valueOf(detail.getLaborCosts()), String.valueOf(detail.getTotalUnitPrice()),
                                        String.valueOf(detail.getUnitProjectTotalPrice()), detail.getExplain()};
                                pdfTablePrint.drawTableRowCellColSpanEmpty(rowAdd);
                            });
                        }
                    }
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[] { "合计", String.valueOf(change.getAddProjectTotalPrice())});
                    pdfTablePrint.drawTableRow("基装变更减少项",true);
                    String[] rowSeven = {"类型", "项目名称", "单位", "数量", "损耗", "人工费", "综合单价", "总价", "说明"};
                    pdfTablePrint.drawTableRow(rowSeven);
                    if( detailList!= null && detailList.size() > 0 && reduceDetails != null && reduceDetails.size() > 0){
                        //遍历减项集合,并填充数据
                        reduceDetails.forEach(detail -> {
                            String[] rowReduce = {"减项", detail.getChangeProjectName(), detail.getUnit(),
                                    String.valueOf(detail.getAmount()), String.valueOf(detail.getLoss()),
                                    String.valueOf(detail.getLaborCosts()), String.valueOf(detail.getTotalUnitPrice()),
                                    String.valueOf(detail.getUnitProjectTotalPrice()), detail.getExplain()};
                            pdfTablePrint.drawTableRowCellColSpanEmpty(rowReduce);
                        });
                    }
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[] { "合计", String.valueOf(change.getCutProjectTotalPrice())});
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[] { "变更合计金额", String.valueOf(change.getChangeListTotalPrice())});

                    pdfTablePrint.build();
                    filePath.add(tempfilePath);
                });

            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("创建pdf失败! " + e);
        }
        if(filePath.size() > 1){
            //合并
            String result = FileUtils.saveFilePath(UploadCategory.PDF, PropertyHolder.getUploadBaseUrl(),
                    UUID.randomUUID().toString() + "." + UploadCategory.PDF.getPath());
            PDFUtils.pdfMergeFiles(filePath.toArray(new String[]{}), result);
            return result;
        }else{
            return filePath.get(0) ;
        }
    }

}
