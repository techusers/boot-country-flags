package org.search.dto;

public class EtlRequestDto extends BaseDto {

	private boolean fullRefresh;

	public boolean isFullRefresh() {
		return fullRefresh;
	}

	public void setFullRefresh(boolean fullRefresh) {
		this.fullRefresh = fullRefresh;
	}
	
}
