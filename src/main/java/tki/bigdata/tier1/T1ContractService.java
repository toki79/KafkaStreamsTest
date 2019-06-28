package tki.bigdata.tier1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import tki.bigdata.pojo.Contract;



@EnableBinding(T1ContractService.ContractSink.class)
public class T1ContractService {
	
	/**
	 * Ein Intercepter, um den Key für die Kafka Message zu setzen
	 * vgl. https://stackoverflow.com/questions/56689257/how-to-set-the-key-of-the-message
	 * @param output
	 * @return
	 */
	@Bean 
	public ApplicationRunner contractRunner(@Qualifier("t2_contract_out") MessageChannel output) {
		((AbstractMessageChannel) output).addInterceptor(0, new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				return MessageBuilder.fromMessage(message)
						.setHeader(KafkaHeaders.MESSAGE_KEY, (((Contract) message.getPayload()).getId()+"").getBytes()).build();						
			} 

		});
		return args -> {
		};
	}

	@StreamListener(ContractSink.T1_CONTRACT_IN)
	@SendTo(ContractSink.T2_CONTRACT_OUT)
	public synchronized Contract receive1(String message) {
		System.out.println("******************");
		System.out.println("Tier 1: At Contract Sink1");
		System.out.println("******************");
		System.out.println("Received message " + message);
		
		String arr[] = message.split(";");
		if (arr[0].equalsIgnoreCase("Contract")) {
			Contract c = new Contract();
			c.setId(Integer.parseInt(arr[1]));
			c.setName(arr[2]);
			return c;
		}
	
		return null;
	}

	

	public interface ContractSink {
		String T1_CONTRACT_IN = "t1_contract_in";
		
		String T1_CONTRACT_OUT = "t1_contract_out";
        String T2_CONTRACT_OUT = "t2_contract_out";
		
		@Input(T1_CONTRACT_IN)
		SubscribableChannel t1_contract_in();
		
		@Output(T1_CONTRACT_OUT)
		SubscribableChannel t1_contract_out();
		@Output(T2_CONTRACT_OUT)
		SubscribableChannel t2_contract_out();
}
}
