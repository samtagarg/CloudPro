package com.cloudshare.useroperations.dto;

import com.cloudshare.dto.CommonAttributeDTO;


public class ForgotCredentialDTO extends CommonAttributeDTO {

	private String userMobileNumber;

	private String verificationCode;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

}
