package com.cloudshare.cloudoperations.webservices;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

import com.cloudshare.cloudoperations.service.CloudOperationsService;
import com.cloudshare.cloudoperations.webservices.model.DownloadRequest;
import com.cloudshare.cloudoperations.webservices.model.DownloadResponse;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyRequest;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyResponse;
import com.cloudshare.cloudoperations.webservices.model.GetFilesRequest;
import com.cloudshare.cloudoperations.webservices.model.GetFilesResponse;
import com.cloudshare.cloudoperations.webservices.model.GetSharedFileRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadResponse;
import com.cloudshare.security.EncryptDecryptByteArr;

@MTOM
@WebService(endpointInterface = "com.cloudshare.cloudoperations.webservices.ICloudOperations", serviceName = "CloudOperationsService", portName = "CloudOperationsPort")
public class CloudOperations implements ICloudOperations {

	@Override
	public UploadResponse uploadFile(UploadRequest request)
			throws Exception {
		UploadResponse response = null;
		try {
			request.setFile(EncryptDecryptByteArr.encrypt(request.getFile()));
			response = (UploadResponse) new CloudOperationsService()
					.processRequest(request);
		} catch (Exception e) {
			response = new UploadResponse();
			response.setErrorMessage("Encryption failed");
			response.setStatus("FAIL");
		}

		return response;
	}

	@Override
	public DownloadResponse downloadFiles(DownloadRequest request) {
		DownloadResponse response = null;
		response = (DownloadResponse) new CloudOperationsService()
				.processRequest(request);
		try {
			response.setFile(EncryptDecryptByteArr.decrypt(response.getFile()));
		} catch (Exception e) {
			response.setErrorMessage("Decryption failed");
			response.setStatus("FAIL");
		}
		return response;
	}

	@Override
	public GetFilesResponse getFiles(GetFilesRequest request) {
		GetFilesResponse response = null;
		response = (GetFilesResponse) new CloudOperationsService()
				.processRequest(request);
		return response;
	}

	@Override
	public GenerateSharingKeyResponse getFileShareKey(
			GenerateSharingKeyRequest request) {
		GenerateSharingKeyResponse response = null;
		response = (GenerateSharingKeyResponse) new CloudOperationsService()
				.processRequest(request);
		return response;
	}

	@Override
	public DownloadResponse getSharedFile(
			GetSharedFileRequest request) {
		DownloadResponse response = null;
		response = (DownloadResponse) new CloudOperationsService()
				.processRequest(request);

		try {
			response.setFile(EncryptDecryptByteArr.decrypt(response.getFile()));
		} catch (Exception e) {
			response.setErrorMessage("Decryption failed");
			response.setStatus("FAIL");
		}

		return response;
	}

}
