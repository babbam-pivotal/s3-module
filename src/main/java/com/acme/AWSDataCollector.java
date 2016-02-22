package com.acme;

import java.io.BufferedWriter;

/**
 * S3 Reader
 * author : mbabbar@pivotal.io
 *
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSDataCollector {

	private String bName = ""; // any bucket name
	private String remoteDir = ""; // events | media | devices | others
	private String localDir = "";
	private long noOfDays = 7;
	private String unzip = "false";
	private String unzipDir = "";
	String keyName = "";
	ArrayList<String> s3list = new ArrayList<String>();
	ArrayList<File> localList = new ArrayList<File>();
	ArrayList<String> downloadList = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		AWSDataCollector ap = new AWSDataCollector("nineinputdire", "event", "/Users/mbabbar/Desktop/latest", 7, "true", "/Users/mbabbar/Desktop/latest");
		ap.start();
	}
	
	
	
	private void createDummyFiles() {
		for (int i=0; i<2500; i++)
		{
			File newFile = new File("/Users/mbabbar/Desktop/events/test_"+i+".txt");
			try {
				newFile.createNewFile();
				FileWriter fileWritter = new FileWriter(newFile,true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(UUID.randomUUID().toString());
				bufferWritter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}



	public AWSDataCollector(String bName, String remoteDir, String localDir, long noOfDays, String unzip, String unzipDir) {
		super();
		this.bName = bName;
		this.remoteDir = remoteDir;
		this.localDir = localDir;
		this.noOfDays = noOfDays;
		this.unzip = unzip;
		this.unzipDir = unzipDir;
		
	}
	
	public ArrayList<String> start() {
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		AmazonS3 s3 = new AmazonS3Client(credentials);

		String bucketName = bName;
		System.out.println("===========================================");
		System.out.println("Started to Read S3");
		System.out.println("===========================================\n");

		try {
			/*
			 * List the buckets in your account
			 */
			System.out.println("Listing buckets");
			for (Bucket bucket : s3.listBuckets()) {
				System.out.println(" - " + bucket.getName());
			}
			System.out.println();

			// Get list of files from the local directory
			File folder = new File(localDir);
			if(!folder.exists())
				if (folder.mkdir()) {
					System.out.println("Local directory is created - " + localDir);
				} else {
					System.out.println("Failed to create local directory - " + localDir);
				}
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					localList.add(listOfFiles[i]);
				}
			}

			/*
			 * List objects in your bucket by prefix - There are many options
			 * for listing the objects in your bucket. Keep in mind that buckets
			 * with many objects might truncate their results when listing their
			 * objects, so be sure to check if the returned object listing is
			 * truncated, and use the AmazonS3.listNextBatchOfObjects(...)
			 * operation to retrieve additional results.
			 */
			System.out.println("Listing objects");
			ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(remoteDir));
			
			//process returned objects from S3 - first lot
			processS3Objects(objectListing);
			//handle truncated list - get other batches of objects
			while(objectListing.isTruncated())
			{
				objectListing = s3.listNextBatchOfObjects(objectListing);
				//process other batches too
				processS3Objects(objectListing);
			}
			
			
			System.out.println("S3 File list size - " + s3list.size());
			System.out.println();
			System.out.println("Local File list size - " + localList.size());
			System.out.println();
			System.out.println("Download File List");
			System.out.println(downloadList.size() + " Files - " + downloadList + ", Unzip in - " + unzipDir + " -> " + unzip);
			System.out.println();

			// download files to localDir
			for (String fn : downloadList) {
				GetObjectRequest gor = new GetObjectRequest(bucketName, fn);
				File f = new File(localDir + "/" + getFileName(fn));
				s3.getObject(gor, f);
				if (unzip.equals("true") && !unzipDir.equals(""))
				{
					UnzipHelper.gunzipIt(f.getAbsolutePath(), unzipDir);
				}
			}

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		
		return downloadList;
	}

	private void processS3Objects(ObjectListing objectListing) {
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			keyName = objectSummary.getKey();
			if (isFile(keyName)) {
				s3list.add(keyName);
				//only download files which are not in local and are of same size
				if (!existInLocalAndSameSize(objectSummary)) {
					downloadList.add(objectSummary.getKey());
				}
			}
		}
	}

	private boolean existInLocalAndSameSize(S3ObjectSummary objectSummary) {
		String keyName = objectSummary.getKey();
		long diff = new Date().getTime() - objectSummary.getLastModified().getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffDays > noOfDays)
			return true; // ignore old files
		for (File f : localList) {
			if (f.getName().equals(getFileName(keyName)) && f.length() == objectSummary.getSize()) {
				return true; // file exist in local and has same size - ignore
			}
		}
		return false; // file not in local OR not of same size - add
	}

	private String getFileName(String kn) {
		return kn.substring(kn.lastIndexOf('/') + 1);
	}

	private static boolean isFile(String key) {
		if (key.endsWith("/"))
			return false;
		else
			return true;
	}

}
