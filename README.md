Spring XD s3 source module
------------------------

Description: The module will extract data from AWS S3 bucket, download and unzip the files in local directory.

Usage:

module upload --type processor --name aws --file s3-module-1.0.0.BUILD-SNAPSHOT.jar

stream create --name test --definition "trigger --fixedDelay=15 | aws --bucketName=inputbucketname --remoteDir=events --localDir=/Users/mbabbar/Desktop/local --noOfDays=5 --unzip=true --unzipDir=/Users/mbabbar/Desktop/local | log" --deploy

Requirements - 

AWS credentials file needs to be created in ~/.aws directory with following content:

[default]

aws_access_key_id=YOUR_ACCESS KEY
aws_secret_access_key=YOUR_SECRET_KEY
