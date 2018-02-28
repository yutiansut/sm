package cn.mdni.core.base.entity;

import java.io.Serializable;

public class UUIDEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4948114136781306377L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
