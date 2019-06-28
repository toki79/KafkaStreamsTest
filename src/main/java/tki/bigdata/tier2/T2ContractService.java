package tki.bigdata.tier2;

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

@EnableBinding(T2ContractService.ContractSink.class)
public class T2ContractService {

	@StreamListener(ContractSink.T2_CONTRACT_IN)
	public synchronized void receive2(Contract contract, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) byte[] key) {
		System.out.println("******************");
		System.out.println("Tier 2: At Contract Sink2");
		System.out.println("******************");
		System.out.println("Received contract " + contract + ", key:" + new String(key));
	}

	public interface ContractSink {

		String T2_CONTRACT_IN = "t2_contract_in";

		@Input(T2_CONTRACT_IN)
		SubscribableChannel t2_contract_in();

	}
}
