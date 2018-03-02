package cn.damei.business.entity.finance;

public class FinaPaymethoStatus {
	
    public FinaPaymethoStatus() {
	}
    
	public FinaPaymethoStatus(Boolean opened, Boolean disabled) {
		this.opened = opened;
		this.disabled = disabled;
	}
	
	//是否展示第一个层级下面的node 
    private Boolean opened;
    //该节点不可点击
    private Boolean disabled;

    public Boolean getOpened() {
        return opened;
    }
    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
