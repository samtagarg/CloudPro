package com.cloudshare.useroperations.webservices;

import javax.jws.WebService;

import com.cloudshare.useroperations.dto.LoginDTO;
import com.cloudshare.useroperations.dto.RegistrationDTO;
import com.cloudshare.useroperations.service.ForgotCredentialService;
import com.cloudshare.useroperations.service.LoginService;
import com.cloudshare.useroperations.service.RegistrationService;
import com.cloudshare.useroperations.webservices.model.CommonResponseAttributes;
import com.cloudshare.useroperations.webservices.model.LoginResponse;
import com.cloudshare.useroperations.webservices.model.RegistrationResponse;

@WebService(endpointInterface = "com.cloudshare.useroperations.webservices.IUserOperations", serviceName = "UserOperationsService", portName = "UserOperationsPort")
public class UserOperations implements IUserOperations {

	public RegistrationResponse registerUser(RegistrationDTO request)
			throws Exception {
		return (RegistrationResponse) RegistrationService.getInstance()
				.processRequest(request);
	}

	public LoginResponse authenticateUser(LoginDTO request) throws Exception {
		return (LoginResponse) LoginService.getInstance().processRequest(
				request);
	}

	@Override
	public CommonResponseAttributes forgotCredential(Object request) throws Exception {
		return ForgotCredentialService.getInstance().processRequest(request);
	}

}
