package tki.bigdata.steams;

import java.util.Date;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Contract;

@SpringBootApplication
public class StreamsApplication {
	@Autowired
	private KafkaTemplate<String, Object> template;
	
	public static void main(String[] args) {        
		SpringApplication.run(StreamsApplication.class, args);
	} 
}

