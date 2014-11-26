package com.cloudshare.useroperations.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * This hibernate bean class represents the table where the verification code will be stored.
 * The verification code can be for forgot password, verification of mobile numbers.
 * The columns that the table consists of are external user id, request type and the verification code.
 * The external user id and the request type creates a composite primary key.
 * For each user and request type only one record will be stored.
 */

@Entity
@Table(name = "CODE_VERIFICATION")
public class CodeVerificationBean implements Serializable {

	private static final long serialVersionUID = 8220636283439922734L;
	@Id
	private int id;
	private int userId;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
