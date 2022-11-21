package org.openmrs.module.ddpintegration;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;

public class DdpTask extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}
