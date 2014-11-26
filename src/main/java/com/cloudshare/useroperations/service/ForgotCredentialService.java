package com.cloudshare.useroperations.service;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudshare.dao.CommonDAO;
import com.cloudshare.security.SaltTextEncryption;
import com.cloudshare.useroperations.bean.CodeVerificationBean;
import com.cloudshare.useroperations.bean.UserBean;
import com.cloudshare.useroperations.dto.CommonAttributesDTO;
import com.cloudshare.useroperations.dto.ForgotCredentialDTO;
import com.cloudshare.useroperations.dto.UpdatePasswordDTO;
import com.cloudshare.useroperations.webservices.model.CommonResponseAttributes;
import com.cloudshare.util.CommonValidations;
import com.cloudshare.util.RandomCodeGenerator;
import com.cloudshare.util.SendMail;

public class ForgotCredentialService {

	private static ForgotCredentialService SINGLETON;
	private Logger logger = LoggerFactory
			.getLogger(ForgotCredentialService.class);

	private CommonDAO commonDao = null;

	public static ForgotCredentialService getInstance() throws Exception {
		if (SINGLETON == null)
			SINGLETON = new ForgotCredentialService();

		return SINGLETON;
	}

	private ForgotCredentialService() throws Exception {
		if (SINGLETON != null) {
			throw new Exception(ForgotCredentialService.class.getName());
		}
	}

	public boolean validate(Object obj) throws Exception {
		boolean result = false;

		if (obj instanceof UpdatePasswordDTO)
			result = validateUpdatePassword((UpdatePasswordDTO) obj);

		if (obj instanceof ForgotCredentialDTO)
			result = validateForgotPassword((ForgotCredentialDTO) obj);

		return result;
	}

	private boolean validateForgotPassword(ForgotCredentialDTO dto)
			throws Exception {
		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType().equals(
							"FORGOT_PASSWORD_REQUEST_TYPE")) {
				result = true;
			} else {
				throw new Exception("INVALID_REQUEST_TYPE");
			}
		} else {
			throw new Exception("INVALID_EMAIL_ADDRESS");
		}
		return result;
	}

	private boolean validateUpdatePassword(UpdatePasswordDTO dto)
			throws Exception {
		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getPassword())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType().equals(
							"UPDATE_PASSWORD_REQUEST_TYPE")) {
				if (CommonValidations.isValidEmailAddress(dto
						.getUserEmailAddress())) {
					if (!CommonValidations.isStringEmpty(dto
							.getVerificationCode())) {
						result = true;
					} else {
						throw new Exception("EMPTY_VERIFICATION_CODE");
					}
				} else {
					throw new Exception("INVALID_EMAIL_ADDRESS");
				}
			} else {
				throw new Exception("INVALID_REQUEST_TYPE");
			}
		} else {
			throw new Exception("INVALID_EMAIL_ADDRESS");
		}
		return result;
	}

	public CommonResponseAttributes processRequest(Object dto) {
		CommonResponseAttributes response = new CommonResponseAttributes();
		try {
			boolean validate = validate(dto);
			commonDao = CommonDAO.getInstance();

			if (validate) {

				if (((CommonAttributesDTO) dto).getRequestType().equals(
						"UPDATE_PASSWORD_REQUEST_TYPE")) {
					CodeVerificationBean verificationBean = commonDao
							.getVerificationCodeDetails(
									((UpdatePasswordDTO) dto)
											.getUserEmailAddress(),
									((UpdatePasswordDTO) dto)
											.getVerificationCode());

					if (verificationBean != null) {
						SaltTextEncryption saltImpl = SaltTextEncryption
								.getInstance();
						String encryptedPassword = saltImpl
								.createHash(((UpdatePasswordDTO) dto)
										.getPassword());
						((UpdatePasswordDTO) dto)
								.setPassword(encryptedPassword);

						commonDao
								.upatePasswordByEmailId(
										((UpdatePasswordDTO) dto)
												.getUserEmailAddress(),
										((UpdatePasswordDTO) dto).getPassword());
						response.setRequestStatus("SUCCESS");

					} else {
						throw new Exception("INVALID_REQUEST_TYPE");

					}
				} else if (((ForgotCredentialDTO) dto).getRequestType().equals(
						"FORGOT_PASSWORD_REQUEST_TYPE")) {
					UserBean user = commonDao
							.getVerifiedUserInfoByEmailId(((ForgotCredentialDTO) dto)
									.getUserEmailAddress());

					if (user != null) {
						String randomCode = RandomCodeGenerator.randomString(5);
						((ForgotCredentialDTO) dto)
								.setVerificationCode(randomCode);
						commonDao
								.insertVerificationCode((ForgotCredentialDTO) dto);

						response.setRequestStatus("SUCCESS");

						sendForgotPasswordMailNotification(
								((ForgotCredentialDTO) dto)
										.getUserEmailAddress(),
								user.getUserName(), randomCode);

					} else {
						throw new Exception("USER_DO_NOT_EXIST");
					}
				}
			}

		} catch (Exception e) {
			response.setErrorCode("500");
			response.setRequestStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	private void sendForgotPasswordMailNotification(String emailAddress,
			String fullName, String code) throws NoSuchAlgorithmException,
			NoSuchPaddingException, Exception {

		StringTemplateGroup emailTemplateGroup = new StringTemplateGroup(
				"FORGOT_PASSWORD_GROUP", System.getProperty("emailTemplates"));
		StringTemplate forgotEmail = emailTemplateGroup
				.getInstanceOf("forgotPasswordEmail");
		forgotEmail.setAttribute("fullName", fullName);
		forgotEmail.setAttribute("code", code);
		forgotEmail.setAttribute("from", "CloudShare Inc.");
		String message = forgotEmail.toString();

		logger.info(message);

		String to[] = { emailAddress };

		SendMail sm = new SendMail("Forgot Password", message, null, to);
		sm.send();
	}
}
