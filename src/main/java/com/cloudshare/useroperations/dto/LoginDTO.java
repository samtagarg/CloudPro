package com.cloudshare.useroperations.dto;

import com.cloudshare.dto.CommonAttributeDTO;


public class LoginDTO extends CommonAttributeDTO {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
