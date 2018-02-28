package cn.mdni.business.dto.finance;



import cn.mdni.business.entity.finance.FinaPaymethoStatus;

import java.util.List;

/**
 * 特殊配置数据的 树形结构dto
 */
public class FinaPaymethodDto {

    public FinaPaymethodDto() {
        super();
    }
    public FinaPaymethodDto(Long id) {
		super();
		this.id = id;
	}
	public FinaPaymethodDto(Long id, String text) {
        super();
        this.id = id;
        this.text = text;
    }

    //节点id--存数据id
    private Long id;
    //子节点
    private List<FinaPaymethodDto> children;
    //文本信息
    private String text;
    //删除/未删除状态
    private String deleted;
    //展开状态
    private FinaPaymethoStatus state;
    //数据编号
    private String code;
    //排序值
    private Integer sort;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<FinaPaymethodDto> getChildren() {
        return children;
    }
    public void setChildren(List<FinaPaymethodDto> children) {
        this.children = children;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }
    public FinaPaymethoStatus getState() {
        return state;
    }
    public void setState(FinaPaymethoStatus state) {
        this.state = state;
    }
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
