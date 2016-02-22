/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme;

import static org.springframework.util.ObjectUtils.nullSafeEquals;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;
/**
 * An example class to describe and validate module options. This Example
 * Illustrates how to bind an option to a Spring profile. This is not usually
 * required, but may be useful in some situations.
 *  
 * Note the @ModuleOption annotation on the setters and the javax.validation
 * annotations on the getters.
 *
 */
public class ModuleOptionsMetadata implements ProfileNamesProvider {
	private String bucketName;
	private String remoteDir;
	private String noOfDays;
	private String localDir;
	private String unzipDir;
	private String unzip;

	
	@NotNull
	public String getRemoteDir() {
		return remoteDir;
	}
	
	@NotNull
	public String getLocalDir() {
		return localDir;
	}
	
	public String getUnzipDir() {
		return unzipDir;
	}
	
	@NotNull
	public String getUnzip() {
		return unzip;
	}
	
	@NotNull
	public String getNoOfDays() {
		return noOfDays;
	}

	@NotNull
	public String getBucketName() {
		return bucketName;
	}
	
	@ModuleOption("S3 directory")
	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	
	@ModuleOption("s3 bucket name")
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	@ModuleOption("local directory")
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	
	@ModuleOption("unzip directory")
	public void setUnzipDir(String unzipDir) {
		this.unzipDir = unzipDir;
	}
	
	@ModuleOption("unzip")
	public void setUnzip(String unzip) {
		this.unzip = unzip;
	}
	
	@ModuleOption("No of days")
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	@Override
	public String[] profilesToActivate() {
		return new String[] {"use-both"};
	}

}