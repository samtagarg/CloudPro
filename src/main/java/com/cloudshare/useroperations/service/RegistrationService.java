package com.cloudshare.useroperations.service;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudshare.dao.CommonDAO;
import com.cloudshare.security.SaltTextEncryption;
import com.cloudshare.useroperations.bean.UserBean;
import com.cloudshare.useroperations.dto.RegistrationDTO;
import com.cloudshare.useroperations.webservices.model.RegistrationResponse;
import com.cloudshare.util.CommonValidations;
import com.cloudshare.util.SendMail;

public class RegistrationService {
	private static RegistrationService SINGLETON;

	private Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	public static RegistrationService getInstance() throws Exception {
		if (SINGLETON == null)
			SINGLETON = new RegistrationService();

		return SINGLETON;
	}

	private RegistrationService() throws Exception {
		if (SINGLETON != null) {
			throw new Exception(RegistrationService.class.getName());
		}
	}

	public boolean validate(RegistrationDTO registrationDTO) throws Exception {

		boolean result = false;

		if (!CommonValidations.isStringEmpty(registrationDTO
				.getUserEmailAddress())
				&& CommonValidations.isValidEmailAddress(registrationDTO
						.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(registrationDTO
					.getRequestType())
					&& registrationDTO.getRequestType().trim()
							.equals("REGISTRATION")) {
				if (!CommonValidations.isStringEmpty(registrationDTO
						.getUserFullName())) {

					if (!CommonValidations.isStringEmpty(registrationDTO
							.getUserPassword())) {
						try {
							CommonDAO dao = CommonDAO.getInstance();
							UserBean user = dao.searchByEmailId(registrationDTO
									.getUserEmailAddress());
							if (user == null)
								result = true;
							else
								throw new Exception(CommonDAO.USER_DONOT_EXISTS);
						} catch (Exception e) {
							if (e.getMessage().equals(
									CommonDAO.USER_DONOT_EXISTS)) {
								result = true;
							} else {
								throw e;
							}
						}

					} else {
						throw new Exception("Empty Password");
					}

				} else {
					throw new Exception("EMPTY_FULL_NAME");
				}
			} else {
				throw new Exception("INVALID_REQUEST_TYPE");
			}

		} else {
			throw new Exception("INVALID_EMAIL_ADDRESS");
		}

		return result;
	}

	public Object processRequest(Object obj) {
		RegistrationResponse response = null;
		boolean validate = false;
		RegistrationDTO dto = (RegistrationDTO) obj;
		CommonDAO commonDao = null;
		try {
			validate = validate(dto);

			if (validate) {
				commonDao = CommonDAO.getInstance();
				SaltTextEncryption saltImpl = new SaltTextEncryption();
				dto.setUserPassword(saltImpl.createHash(dto.getUserPassword()));
				int externalId = commonDao.registerUser(dto);

				response = new RegistrationResponse();
				response.setRequestStatus("SUCCESS");

				sendMailNotification(dto.getUserEmailAddress(), externalId,
						dto.getUserFullName());

			} else {
				throw new Exception("REQUEST_NOT_GENERATED");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = new RegistrationResponse();
			response.setErrorCode("500");
			response.setRequestStatus("FAILURE");
			response.setErrorMessage(e.getMessage());

		}

		return response;
	}

	private String generateVerificationLink(int externalID)
			throws NoSuchAlgorithmException, NoSuchPaddingException, Exception {
		String url = null;
		url = "http://54.148.120.222:8080/CloudShare/EmailAddressVerification?emailAddressVerificationCode="
				+ externalID;

		return url;
	}

	private void sendMailNotification(String emailAddress, int externalId,
			String fullName) throws NoSuchAlgorithmException,
			NoSuchPaddingException, Exception {
		String url = generateVerificationLink(externalId);

		StringTemplateGroup emailTemplateGroup = new StringTemplateGroup(
				"EMAIL_VERIFICATION_GROUP",
				System.getProperty("emailTemplates"));
		StringTemplate loginEmail = emailTemplateGroup
				.getInstanceOf("welcomeMail");
		loginEmail.setAttribute("fullName",
				fullName);
		loginEmail.setAttribute("URL", url);
		loginEmail.setAttribute("from",
				"Cloud Share");
		String message = loginEmail.toString();

		logger.info(message);

		String to[] = { emailAddress };

		SendMail sm = new SendMail("EMAIL_VERIFICATION_SUBJECT", message, null,
				to);
		sm.send();
	}

}
