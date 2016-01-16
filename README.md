Spring XD s3 source module
------------------------

Usage:

module upload --type processor --name aws --file s3-module-1.0.0.BUILD-SNAPSHOT.jar

stream create --name test --definition "trigger --fixedDelay=15 | aws --bucketName=inputbucketname --remoteDir=events --localDir=/Users/mbabbar/Desktop/local --noOfDays=5 | log" --deploy
