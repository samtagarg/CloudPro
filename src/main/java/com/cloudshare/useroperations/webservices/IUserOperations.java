package com.cloudshare.useroperations.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.cloudshare.useroperations.dto.RegistrationDTO;
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
	public String authenticateUser(
			@WebParam(name = "AuthenticationRequest") String request)
			throws Exception;

}
