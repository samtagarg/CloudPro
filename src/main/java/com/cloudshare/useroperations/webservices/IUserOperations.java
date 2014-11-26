package com.cloudshare.useroperations.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.cloudshare.useroperations.dto.LoginDTO;
import com.cloudshare.useroperations.dto.RegistrationDTO;
import com.cloudshare.useroperations.webservices.model.CommonResponseAttributes;
import com.cloudshare.useroperations.webservices.model.LoginResponse;
import com.cloudshare.useroperations.webservices.model.RegistrationResponse;

@WebService(name = "UserOperationsService")
public interface IUserOperations {

	@WebMethod(operationName = "RegisterUser")
	@WebResult(name = "RegistrationResponse")
	public RegistrationResponse registerUser(
			@WebParam(name = "RegistrationRequest") RegistrationDTO request)
			throws Exception;

	@WebMethod(operationName = "AuthenticateUser")
	@WebResult(name = "AuthenticationResponse")
	public LoginResponse authenticateUser(
			@WebParam(name = "AuthenticationRequest") LoginDTO request)
			throws Exception;

	@WebMethod(operationName = "ForgotCredential")
	@WebResult(name = "ForgotCredentialResponse")
	public CommonResponseAttributes forgotCredential(
			@WebParam(name = "ForgotCredentialRequest") Object request)
			throws Exception;

}
