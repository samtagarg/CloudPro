package com.cloudshare.useroperations.webservices;

import javax.jws.WebService;

import com.cloudshare.useroperations.dto.RegistrationDTO;
import com.cloudshare.useroperations.service.RegistrationService;
import com.cloudshare.useroperations.webservices.model.RegistrationResponse;


@WebService(endpointInterface = "com.cloudshare.useroperations.webservices.IUserOperations", serviceName = "UserOperationsService", portName = "UserOperationsPort")
public class UserOperations implements IUserOperations {

	public RegistrationResponse registerUser(RegistrationDTO request) throws Exception {
		// TODO Auto-generated method stub
		return (RegistrationResponse) RegistrationService.getInstance().processRequest(request);
	}

	public String authenticateUser(String request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
