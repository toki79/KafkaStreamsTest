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
//	
//
//	
//	@StreamListener
//	public void process(@Input("topic2") KStream<String, Contract> contracts) {
//		System.out.println("Found contracts");
//	}
//
//	public interface ContractSink extends Sink {
//		@Input("topic2")
//	    KStream<?, ?> inputStream();
//    }
//	
//	/**
//	 * get sample data from topic, create objects and send them
//	 * @param s
//	 * @return
//	 */
//	@StreamListener(Processor.INPUT)
//	@SendTo(Processor.OUTPUT)
//	public Object processStg1(String s) {
//		String arr[] = s.split(";");
//		if (arr[0].equalsIgnoreCase("Contract")) {
//			Contract c = new Contract();
//			c.setId(Integer.parseInt(arr[1]));
//			c.setName(arr[2]);
//			return c;
//		}
//		else if (arr[0].equalsIgnoreCase("Cashflow")) {
//			Cashflow cf = new Cashflow();
//			cf.setContractId(Integer.parseInt(arr[1]));
//			cf.setDate(arr[2]);
//			cf.setAmount(Float.parseFloat(arr[3]));
//			return cf;
//		}
//		
//		return ("ERROR: could not parse type");
//	}
}

