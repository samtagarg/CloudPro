package com.cloudshare.useroperations.dto;

public class CommonAttributesDTO {

	private String userEmailAddress;
	private String requestType;
	private String userIPAddress;
	private int requestId;
	private int externalUserId;

	public int getExternalUserId() {
		return externalUserId;
	}

	public void setExternalUserId(int externalUserId) {
		this.externalUserId = externalUserId;
	}

	public String getUserEmailAddress() {
		return userEmailAddress;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getUserIPAddress() {
		return userIPAddress;
	}

	public void setUserIPAddress(String userIPAddress) {
		this.userIPAddress = userIPAddress;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

}
