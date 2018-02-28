package cn.mdni.business.entity.finance;

import cn.mdni.business.dto.finance.FinaPaymethodDto;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Paul 2017年6月8日
 */
public class FinaPaymethodTree extends IdEntity {


	// 编号
	private String code;
	// 名称
	private String name;
	// 父类Id
	private Long parentId;
	//父类名称
	private String parentName;
	//是否删除  0:未删除,1:已删除
	private String deleted;
	
	//排序值:越小越靠前
	private Integer sort;


	//所属支付方式ID
	private Integer methodId;

	//所属ID的全部路径
	private String attrAath;
	//是否启用
	private String attrStatus;
	//手续费率
	private String costRate;
	//最低手续费金额
	private String minCostfee;
	//封顶手续费金额
	private String maxCostFee;
	//创建时间
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
	private Date createTime;

	//操作人
	private String creator;

	//备注
	private String remark;
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}



	public Integer getMethodId() {
		return methodId;
	}

	public void setMethodId(Integer methodId) {
		this.methodId = methodId;
	}

	public String getAttrAath() {
		return attrAath;
	}

	public void setAttrAath(String attrAath) {
		this.attrAath = attrAath;
	}

	public String getAttrStatus() {
		return attrStatus;
	}

	public void setAttrStatus(String attrStatus) {
		this.attrStatus = attrStatus;
	}

	public String getCostRate() {
		return costRate;
	}

	public void setCostRate(String costRate) {
		this.costRate = costRate;
	}

	public String getMinCostfee() {
		return minCostfee;
	}

	public void setMinCostfee(String minCostfee) {
		this.minCostfee = minCostfee;
	}

	public String getMaxCostFee() {
		return maxCostFee;
	}

	public void setMaxCostFee(String maxCostFee) {
		this.maxCostFee = maxCostFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}



	/**
     * 构建目录树,递归操作
     *
     * @return 目录树
     */
    public static FinaPaymethodDto buildDictTree(FinaPaymethodDto curCode, List<FinaPaymethodTree> dictList) {
        if (CollectionUtils.isEmpty(dictList) || curCode == null) {
            return curCode;
        }
        //子节点数据集合
        List<FinaPaymethodDto> childNodeList = new ArrayList<>();

        // 构造根节点
        FinaPaymethodTree dict = null;
        for (int i = 0; i < dictList.size(); i++) {
        	dict = dictList.get(i);
            if (dict == null) {
                continue;
            }
            //当前的id等于遍历出的对象的父id
            if (curCode.getId().equals(dict.getParentId())) {
                FinaPaymethodDto childNode = new FinaPaymethodDto ();
                childNode.setId(dict.getId());
                childNode.setDeleted(dict.getDeleted());
                //判断当前状态为1(已删除)时,给text添加红色显示
                if("1".equals(childNode.getDeleted())){
                	childNode.setText("<font color=\'red\'>" + dict.getName() + "(已删除)</font>");
                }else{
                	childNode.setText(dict.getName());
                }
                childNode.setCode(dict.getCode());
                //排序值  暂时没用那先注释了
               // childNode.setSort(dict.getSort());
                //页面展示状态
                childNode.setState(new FinaPaymethoStatus (false, false));
                childNodeList.add(childNode);
            }
        }
        if (CollectionUtils.isNotEmpty(childNodeList)) {
            // 设置子节点
            curCode.setChildren(childNodeList);
        }
        // 递归构造子节点
        for (FinaPaymethodDto dto : childNodeList) {
        	buildDictTree(dto, dictList);
        }
        return curCode;
    }




}