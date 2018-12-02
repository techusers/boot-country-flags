package org.search.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseDto {
	
	private String addedby;
	
	private String modifiedby;
	
	private Date addeddate;
	
	private Date modifieddate;

	@JsonIgnore
	public String getAddedby() {
		return addedby;
	}

	public void setAddedby(String addedby) {
		this.addedby = addedby;
	}

	@JsonIgnore
	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	@JsonIgnore
	public Date getAddeddate() {
		return addeddate;
	}

	public void setAddeddate(Date addeddate) {
		this.addeddate = addeddate;
	}

	@JsonIgnore
	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	
}
