package com.cloudshare.cloudoperations.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.cloudshare.cloudoperations.webservices.model.DownloadRequest;
import com.cloudshare.cloudoperations.webservices.model.DownloadResponse;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyRequest;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyResponse;
import com.cloudshare.cloudoperations.webservices.model.GetFilesRequest;
import com.cloudshare.cloudoperations.webservices.model.GetFilesResponse;
import com.cloudshare.cloudoperations.webservices.model.GetSharedFileRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadResponse;

@WebService(name = "CloudOperationsService")
public interface ICloudOperations {

	@WebMethod(operationName = "UploadFile")
	@WebResult(name = "UploadResponse")
	public UploadResponse uploadFile(
			@WebParam(name = "UploadRequest") UploadRequest request)
			throws Exception;

	@WebMethod(operationName = "DownloadFile")
	@WebResult(name = "DownloadFileResponse")
	public DownloadResponse downloadFiles(
			@WebParam(name = "DownloadFileRequest") DownloadRequest request);

	@WebMethod(operationName = "GetFiles")
	@WebResult(name = "GetFilesResponse")
	public GetFilesResponse getFiles(
			@WebParam(name = "GetFilesRequest") GetFilesRequest request);

	@WebMethod(operationName = "GetFileShareKey")
	@WebResult(name = "GetFileShareKeyResponse")
	public GenerateSharingKeyResponse getFileShareKey(
			@WebParam(name = "GetFileShareKeyRequest") GenerateSharingKeyRequest request);

	@WebMethod(operationName = "GetSharedFile")
	@WebResult(name = "GetFileShareKeyResponse")
	public DownloadResponse getSharedFile(
			@WebParam(name = "GetFileShareKeyRequest") GetSharedFileRequest request);

}
