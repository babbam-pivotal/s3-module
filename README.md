Spring XD - Project Carlos Cleanser Module
============================================

The project includes sample unit and integration tests, including the ability to test the module in an embedded single node container. 

## Requirements

In order to install the module and run it in your Spring XD installation, you will need to have installed:

* Spring XD version 1.1.x ([Instructions](http://docs.spring.io/spring-xd/docs/current/reference/html/#getting-started))

## Building with Maven

	$ mvn clean package

The project's [pom][] declares `spring-xd-module-parent` as its parent. This adds the dependencies needed to compile and test the module and also configures the [Spring Boot Maven Plugin][] to package the module as an uber-jar, packaging any dependencies that are not already provided by the Spring XD container.


## Using the Cleanser Module

The uber-jar will be in `[project build dir]/module-1.0.0.BUILD-SNAPSHOT.jar`. To install and register the module to your Spring XD distribution, use the `module upload` Spring XD shell command. Start Spring XD and the shell:

	$ module upload --file path-to/module-1.0.0.BUILD-SNAPSHOT.jar --name taxiCleanser --type processor


Now create and deploy a stream:

	xd:>stream create --name taxiStuff --definition "reactor-ip | taxiCleanser | log" --deploy

Post some data:

	$ cat readable_sample.csv | nc localhost 3000


You should see the stream output in the Spring XD log:


	2015-05-04 19:23:37,633 1.1.1.RELEASE  INFO ringBuffer-93 sink.taxiStuff - 07290D3599E7A0D62097A346EFCC1FB5,E7750A37CAB07D0DFF0AF7E3573AC141,01/01/13 00:00,01/01/13 00:02,120,0.44,-73.956528,40.716976,-73.96244,40.715008,CSH,3.5,0.5,0.5,0,0,4.5
2015-05-04 19:23:37,633 1.1.1.RELEASE  INFO ringBuffer-93 sink.taxiStuff - 0EC22AAF491A8BD91F279350C2B010FD,778C92B26AE78A9EBDF96B49C67E4007,01/01/13 00:01,01/01/13 00:03,120,0.71,-73.973145,40.752827,-73.965897,40.760445,CSH,4,0.5,0.5,0,0,5
2015-05-04 19:23:37,633 1.1.1.RELEASE  INFO ringBuffer-93 sink.taxiStuff - 3B4129883A1D05BE89F2C929DE136281,7077F9FD5AD649AEACA4746B2537E3FA,01/01/13 00:01,01/01/13 00:03,120,0.61,-73.987373,40.724861,-73.983772,40.730995,CRD,4,0.5,0.5,0,0,5
2015-05-04 19:23:37,633 1.1.1.RELEASE  INFO ringBuffer-93 sink.taxiStuff - DFBFA82ECA8F7059B89C3E8B93DAA377,CF8604E72D83840FBA1978C2D2FC9CDB,01/01/13 00:02,01/01/13 00:03,60,0.39,-73.981544,40.781475,-73.979439,40.784386,CRD,3,0.5,0.5,0.7,0,4.7
2015-05-04 19:23:37,633 1.1.1.RELEASE  INFO ringBuffer-93 sink.taxiStuff - 468244D1361B8A3EB8D206CC394BC9E9,BB899DFEA9CC964B50C540A1D685CCFB,01/01/13 00:00,01/01/13 00:04,240,1.71,-73.955383,40.779728,-73.967758,40.760326,CSH,6.5,0.5,0.5,0,0,7.5


[pom]: https://github.com/spring-projects/spring-xd-samples/blob/master/si-dsl-module/pom.xml
[build.gradle]: https://github.com/spring-projects/spring-xd-samples/blob/master/si-dsl-module/build.gradle
[Spring Integration Java DSL]: https://github.com/spring-projects/spring-integration-java-dsl
[Spring Boot Maven Plugin]: http://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html
[Spring Boot Gradle Plugin]: http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/build-tool-plugins-gradle-plugin.html
[propdeps plugin]: https://github.com/spring-projects/gradle-plugins/tree/master/propdeps-plugin
[Modules]: http://docs.spring.io/spring-xd/docs/current/reference/html/#modules
