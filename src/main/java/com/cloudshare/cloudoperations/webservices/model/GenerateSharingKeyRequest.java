package com.cloudshare.cloudoperations.webservices.model;

public class GenerateSharingKeyRequest {

	private String emailAddress;
	private String password;
	private String sharedUserName;
	private String fileName;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSharedUserName() {
		return sharedUserName;
	}

	public void setSharedUserName(String sharedUserName) {
		this.sharedUserName = sharedUserName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
