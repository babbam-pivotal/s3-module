Spring XD s3 source module
------------------------

Usage:

module upload --type processor --name aws --file s3-module-1.0.0.BUILD-SNAPSHOT.jar

stream create --name test --definition "trigger --fixedDelay=15 | aws --bucketName=inputbucketname --remoteDir=events --localDir=/Users/mbabbar/Desktop/local --noOfDays=5 | log" --deploy

Requirements - 

AWS credentials file needs to be created in ~/.aws directory with following content:

[default]
aws_access_key_id=4859792487594729574239579435432543
aws_secret_access_key=FDLASJFLDJSLFJDSLAFJDSLJFLDSAJFLDSJLFSGA
