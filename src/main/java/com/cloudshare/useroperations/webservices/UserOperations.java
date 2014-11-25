package com.cloudshare.useroperations.webservices;

import javax.jws.WebService;


@WebService(endpointInterface = "com.cloudshare.useroperations.webservices.IUserOperations", serviceName = "UserOperationsService", portName = "UserOperationsPort")
public class UserOperations implements IUserOperations {

	public String registerUser(String request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String authenticateUser(String request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
