package com.cloudshare.amazonoperations;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloudshare.util.WriteByteArray;

public class AmazonOperations {
	private AmazonS3 s3;

	public AmazonOperations() {
		BufferedReader b = null;
		String accessKey = null;
		String secretKey = null;
		try {
			b = new BufferedReader(new FileReader(new File(
					"C:\\Users\\Administrator\\Desktop\\AmazonKeys.txt")));
			accessKey = b.readLine();
			secretKey = b.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (b != null)
				try {
					b.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey,
				secretKey);
		s3 = new AmazonS3Client(awsCreds);

		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		((AmazonWebServiceClient) s3).setRegion(usWest2);
	}

	public static void main(String[] args) {
		InputStream is = new ByteArrayInputStream(
				WriteByteArray.getByteFromFile(new File(
						"C:\\Users\\Administrator\\Desktop\\Temp.txt")));
		AmazonOperations a = new AmazonOperations();
		System.out.println(a.isBucketPresent("samta123"));
		//a.s3.createBucket("samta123");
		a.uploadFile(is, "samta123", "samta123");
	}

	public List<String> getFiles(String userName) {
		List<String> list = null;
		ObjectListing bucketList = s3.listObjects(new ListObjectsRequest()
				.withBucketName(userName));
		if (bucketList != null) {
			list = new ArrayList<String>();
		}
		for (S3ObjectSummary objectSummary : bucketList.getObjectSummaries()) {
			list.add(objectSummary.getKey());
		}
		return list;
	}

	public void uploadFile(InputStream is, String fileName, String userName) {
		if (!isBucketPresent(userName)) {
			s3.createBucket(userName);
		}
		s3.putObject(userName, fileName, is, null);
	}

	private boolean isBucketPresent(String bucketName) {
		boolean present = false;
		for (Bucket bucket : s3.listBuckets()) {
			System.out.println(bucket.getName());
			if (bucket.getName() != null && bucket.getName().equals(bucketName)) {
				present = true;
				break;
			}
		}

		return present;
	}

	public byte[] downloadFile(String fileName, String userName) {
		S3Object object = s3
				.getObject(new GetObjectRequest(userName, fileName));
		byte[] fileBytes = null;
		try {
			IOUtils.copy(object.getObjectContent(), new FileOutputStream(
					userName + "" + fileName));

			File file = new File(userName + "" + fileName);

			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream inputStream = new BufferedInputStream(fis);
			fileBytes = new byte[(int) file.length()];
			inputStream.read(fileBytes);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileBytes;
	}

}
