package com.acme;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.xd.tuple.Tuple;
import static org.springframework.xd.tuple.TupleBuilder.tuple;


@Configuration
@EnableIntegration
@Import({AWSConfiguration.class})
public class ModuleConfiguration {
	@Autowired
	GenericTransformer<String,Tuple> transformer;
	
	@Bean
	public MessageChannel input() {
		return new DirectChannel();
	}

	@Bean
	MessageChannel output() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow myFlow() {
		return IntegrationFlows.from(this.input())
				.transform(transformer)
				.channel(this.output())
				.get();
	}
}

@Configuration
@Profile({"use-both","default"})
class AWSConfiguration {
	
	@Value("${remoteDir}")
	private String remoteDir;

	@Value("${bucketName:}")
	private String bucketName;

	@Value("${localDir:}")
	private String localDir;
	
	@Value("${noOfDays:}")
	private long noOfDays;
	
	@Value("${unzip:}")
	private String unzip;
	
	@Value("${unzipDir:}")
	private String unzipDir;
	
	@Bean
	GenericTransformer<String, Tuple> transformer() {
		return new GenericTransformer<String, Tuple>() {
			@Override
			public Tuple transform(String payload) {
				//payload is input which is a trigger only
				
				System.out.println("Processing Delta: " + bucketName + ":" + remoteDir + " -> " + localDir);
				ArrayList<String> downloaded_list = new AWSDataCollector(bucketName, remoteDir, localDir, noOfDays, unzip, unzipDir).start();
				Tuple tuple = tuple().put("downloaded_files", downloaded_list).build();
				return tuple;
			}
		};
	}		
}

