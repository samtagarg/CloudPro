package com.cloudshare.cloudoperations.webservices;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;


@MTOM
@WebService(endpointInterface = "com.cloudshare.cloudoperations.webservices.ICloudOperations", serviceName = "CloudOperationsService", portName = "CloudOperationsPort")
public class CloudOperations implements ICloudOperations {

	public String uploadFile(String request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String downloadFiles(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFiles(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileShareKey(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSharedFile(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
