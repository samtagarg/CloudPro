package com.cloudshare.useroperations.dto;

import com.cloudshare.dto.CommonAttributeDTO;


public class RegistrationDTO extends CommonAttributeDTO {

	private String userFullName;
	private String userPassword;

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
