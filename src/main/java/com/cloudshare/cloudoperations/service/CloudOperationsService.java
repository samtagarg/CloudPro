package com.cloudshare.cloudoperations.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.amazonaws.services.elasticmapreduce.model.InvalidRequestException;
import com.cloudshare.amazonoperations.AmazonOperations;
import com.cloudshare.cloudoperations.webservices.model.DownloadRequest;
import com.cloudshare.cloudoperations.webservices.model.DownloadResponse;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyRequest;
import com.cloudshare.cloudoperations.webservices.model.GenerateSharingKeyResponse;
import com.cloudshare.cloudoperations.webservices.model.GetFilesRequest;
import com.cloudshare.cloudoperations.webservices.model.GetFilesResponse1;
import com.cloudshare.cloudoperations.webservices.model.GetSharedFileRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadRequest;
import com.cloudshare.cloudoperations.webservices.model.UploadResponse;
import com.cloudshare.dao.CommonDAO;
import com.cloudshare.security.CustomEncryptionImpl;
import com.cloudshare.useroperations.bean.UserBean;
import com.cloudshare.useroperations.service.LoginService;
import com.cloudshare.util.SendMail;

public class CloudOperationsService {
	public Object processRequest(Object dto) {
		Object response = null;
		if (dto instanceof UploadRequest)
			response = uploadFile((UploadRequest) dto);
		else if (dto instanceof DownloadRequest)
			response = downloadFiles((DownloadRequest) dto);
		else if (dto instanceof GetFilesRequest)
			response = getFiles((GetFilesRequest) dto);
		else if (dto instanceof GenerateSharingKeyRequest)
			response = getSharedKey((GenerateSharingKeyRequest) dto);
		else if (dto instanceof GetSharedFileRequest)
			response = getSharedFile((GetSharedFileRequest) dto);
		return response;
	}

	private Object getSharedFile(GetSharedFileRequest request) {
		DownloadResponse response = null;

		try {
			CommonDAO dao = CommonDAO.getInstance();
			UserBean user = dao.searchByEmailId(request.getEmailAddress());
			if (user != null) {
				boolean result = LoginService.getInstance().authenticateUser(
						request.getPassword(), user.getPassword());
				if (result) {
					CustomEncryptionImpl impl = CustomEncryptionImpl
							.getInstance();

					String key = impl.decryptNum(request.getKey());
					StringTokenizer token = new StringTokenizer(key, ":");
					String password = token.nextToken();
					int userId = Integer.parseInt(token.nextToken());
					String sharedUserName = token.nextToken();
					String fileName = token.nextToken();

					if (sharedUserName.equals(request.getEmailAddress())) {
						UserBean bean = dao.searchByExternalUserId(userId);
						DownloadRequest req = new DownloadRequest();
						req.setFileName(fileName);
						req.setPassword(password);
						req.setEmailAddres(bean.getEmailAddress());

						response = (DownloadResponse) downloadFiles(req);
						response.setFileName(fileName);
					} else {
						response = new DownloadResponse();
						response.setErrorMessage("Shared User Name is mismatching");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new DownloadResponse();
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

	private Object getSharedKey(GenerateSharingKeyRequest request) {
		GenerateSharingKeyResponse response = null;

		try {
			CommonDAO dao = CommonDAO.getInstance();
			UserBean user = dao.searchByEmailId(request.getEmailAddress());
			if (user != null) {
				boolean result = LoginService.getInstance().authenticateUser(
						request.getPassword(), user.getPassword());

				if (result) {
					CustomEncryptionImpl impl = CustomEncryptionImpl
							.getInstance();

					String key = impl.encryptNum(user.getUserId(),
							request.getPassword(), request.getSharedUserName(),
							request.getFileName());

					response = new GenerateSharingKeyResponse();
					response.setKey(key);

					String[] to = { request.getSharedUserName() };
					String[] at = null;
					SendMail sm = new SendMail("Hi "
							+ request.getSharedUserName() + "\n "
							+ request.getEmailAddress() + " shared a file \n"
							+ request.getFileName()
							+ " with you.Please note the key "
							+ response.getKey(), request.getEmailAddress()
							+ " shared " + request.getFileName(), at, to);
					sm.send();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = new GenerateSharingKeyResponse();
			response.setKey(null);
		}
		return response;
	}

	private Object getFiles(GetFilesRequest request) {
		GetFilesResponse1 response = null;
		AmazonOperations aco = new AmazonOperations();
		response = new GetFilesResponse1();
		response.setFilesName(aco.getFiles(request.getUserName().replaceAll(
				"@", "-")));
		return response;
	}

	private Object downloadFiles(DownloadRequest request) {
		DownloadResponse response = null;
		response = new DownloadResponse();

		try {
			CommonDAO dao = CommonDAO.getInstance();
			validateRequest(request);
			UserBean user = dao.searchByEmailId(request.getEmailAddres());
			if (user != null) {
				boolean result = LoginService.getInstance().authenticateUser(
						request.getPassword(), user.getPassword());

				if (result) {
					response.setStatus("FAILURE");
					response.setErrorMessage("Either User doesn't exist or the credentials are incorrect");
				} else {
					AmazonOperations aco = new AmazonOperations();

					response.setStatus("SUCCESS");
					response.setFile(aco.downloadFile(request.getFileName(),
							request.getEmailAddres().replaceAll("@", "-")));
				}
			}
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			response.setStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	private Object uploadFile(UploadRequest request) {
		UploadResponse response = null;

		try {
			CommonDAO dao = CommonDAO.getInstance();
			validateRequest(request);
			response = new UploadResponse();
			UserBean user = dao.searchByEmailId(request.getEmailAddress());
			if (user != null) {
				boolean result = LoginService.getInstance().authenticateUser(
						request.getPassword(), user.getPassword());

				if (result) {
					response.setStatus("FAILURE");
					response.setErrorMessage("Either User doesnt exists or the credentials are incorrect");
				} else {
					InputStream is = getFileObject(request.getFile());
					AmazonOperations aco = new AmazonOperations();
					aco.uploadFile(is, request.getFileName(), request
							.getEmailAddress().replaceAll("@", "-"));
					response.setStatus("SUCCESS");
				}
			}
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			response.setStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("FAILURE");
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	public InputStream getFileObject(byte[] bytes) {
		InputStream stream = new ByteArrayInputStream(bytes);
		return stream;
	}

	public void validateRequest(Object dto) throws Exception {
		if (dto instanceof UploadRequest) {
			UploadRequest request = (UploadRequest) dto;

			if (request.getFile() == null)
				throw new InvalidRequestException("Empty File sent");

			if (request.getPassword() == null
					|| request.getEmailAddress() == null)
				throw new InvalidRequestException(
						"Empty username or password sent");
		} else if (dto instanceof DownloadRequest) {
			DownloadRequest request = (DownloadRequest) dto;
			if (request.getFileName() == null)
				throw new InvalidRequestException("Empty File sent");

			if (request.getPassword() == null
					|| request.getEmailAddres() == null)
				throw new InvalidRequestException(
						"Empty username or password sent");
		}
	}

}
