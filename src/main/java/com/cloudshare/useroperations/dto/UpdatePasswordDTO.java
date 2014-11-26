package com.cloudshare.useroperations.dto;

import com.cloudshare.dto.CommonAttributeDTO;


public class UpdatePasswordDTO extends CommonAttributeDTO {

	private String password;
	private String verificationCode;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
