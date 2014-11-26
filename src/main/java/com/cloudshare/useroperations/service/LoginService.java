package com.cloudshare.useroperations.service;

import com.cloudshare.dao.CommonDAO;
import com.cloudshare.security.SaltTextEncryption;
import com.cloudshare.useroperations.bean.UserBean;
import com.cloudshare.useroperations.dto.LoginDTO;
import com.cloudshare.useroperations.webservices.model.LoginResponse;
import com.cloudshare.util.CommonValidations;

public class LoginService {

	private static LoginService SINGLETON;

	public static LoginService getInstance() throws Exception {
		if (SINGLETON == null)
			SINGLETON = new LoginService();

		return SINGLETON;
	}

	private LoginService() throws Exception {
		if (SINGLETON != null) {
			throw new Exception(LoginService.class.getName());
		}
	}

	public boolean validate(Object obj) throws Exception {
		LoginDTO dto = (LoginDTO) obj;

		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getUserEmailAddress())
				&& CommonValidations.isValidEmailAddress(dto
						.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType().trim().equals("LOGIN")) {
				if (!CommonValidations.isStringEmpty(dto.getPassword())) {
					result = true;
				} else {
					throw new Exception("EMPTY_PASSWORD");
				}
			} else {
				throw new Exception("INVALID_REQUEST_TYPE");
			}
		} else {
			throw new Exception("INVALID_EMAIL_ADDRESS");
		}

		return result;
	}

	public Object processRequest(Object dto) {
		CommonDAO dao = null;
		LoginResponse response = null;
		try {
			dao = CommonDAO.getInstance();
			boolean validate = validate(dto);

			if (validate) {

				UserBean user = dao.searchByEmailId(((LoginDTO) dto)
						.getUserEmailAddress());
				if (user != null) {
					boolean result = authenticateUser((LoginDTO) dto,
							user.getPassword());
					if (result) {
						response = new LoginResponse();
						response.setFullName(user.getUserName());
						response.setRequestStatus("SUCCESS");
					} else {
						throw new Exception("INCORRECT_PASSWORD");
					}
				} else
					throw new Exception("USER_DO_NOT_EXIST");

			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new LoginResponse();
			response.setErrorCode("500");
			response.setRequestStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	public boolean authenticateUser(LoginDTO dto, String actualPassword)
			throws Exception {
		String password = dto.getPassword();
		try {

			SaltTextEncryption salt = SaltTextEncryption.getInstance();

			boolean valid = salt.validateStrings(password, actualPassword);
			if (valid) {
				return true;
			} else {
				throw new Exception("INCORRECT_PASSWORD");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
