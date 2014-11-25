package com.cloudshare.useroperations.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "UserOperationsService")
public interface IUserOperations {

	@WebMethod(operationName = "RegisterUser")
	@WebResult(name = "RegistrationResponse")
	public String registerUser(
			@WebParam(name = "RegistrationRequest") String request)
			throws Exception;

	@WebMethod(operationName = "AuthenticateUser")
	@WebResult(name = "AuthenticationResponse")
	public String authenticateUser(
			@WebParam(name = "AuthenticationRequest") String request)
			throws Exception;

}
