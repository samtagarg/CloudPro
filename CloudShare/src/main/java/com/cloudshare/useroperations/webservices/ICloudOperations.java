package com.cloudshare.useroperations.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "CloudOperationsService")
public interface ICloudOperations {
	@WebMethod(operationName = "UploadFile")
	@WebResult(name = "UploadResponse")
	public String uploadFile(
			@WebParam(name = "UploadRequest") String request)
			throws Exception;

	@WebMethod(operationName = "DownloadFile")
	@WebResult(name = "DownloadFileResponse")
	public String downloadFiles(
			@WebParam(name = "DownloadFileRequest") String request);

	@WebMethod(operationName = "GetFiles")
	@WebResult(name = "GetFilesResponse")
	public String getFiles(
			@WebParam(name = "GetFilesRequest") String request);

	@WebMethod(operationName = "GetFileShareKey")
	@WebResult(name = "GetFileShareKeyResponse")
	public String getFileShareKey(
			@WebParam(name = "GetFileShareKeyRequest") String request);

	@WebMethod(operationName = "GetSharedFile")
	@WebResult(name = "GetFileShareKeyResponse")
	public String getSharedFile(
			@WebParam(name = "GetFileShareKeyRequest") String request);
}
