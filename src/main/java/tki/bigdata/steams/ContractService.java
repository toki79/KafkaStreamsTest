package tki.bigdata.steams;

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

import kafka.network.Processor;
import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Contract;

@EnableBinding(ContractService.ContractSink.class)
public class ContractService {
	
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
		System.out.println("At Sink1");
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

	@StreamListener(ContractSink.T2_CONTRACT_IN)
	public synchronized void receive2(Contract contract, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) byte[] key) {
		System.out.println("******************");
		System.out.println("At Sink2");
		System.out.println("******************");
		System.out.println("Received contract " + contract + ", key:" + new String(key));
	}

	public interface ContractSink {
		String T1_CONTRACT_IN = "t1_contract_in";
		String T2_CONTRACT_IN = "t2_contract_in";
		String T1_CONTRACT_OUT = "t1_contract_out";
        String T2_CONTRACT_OUT = "t2_contract_out";
		
		@Input(T1_CONTRACT_IN)
		SubscribableChannel t1_contract_in();
		@Input(T2_CONTRACT_IN)
		SubscribableChannel t2_contract_in();
		
		@Output(T1_CONTRACT_OUT)
		SubscribableChannel t1_contract_out();
		@Output(T2_CONTRACT_OUT)
		SubscribableChannel t2_contract_out();
}
}
