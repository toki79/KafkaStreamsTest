package tki.bigdata.steams;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.integration.channel.AbstractMessageChannel;


import tki.bigdata.pojo.Cashflow;

@EnableBinding(CashflowService.CashflowSink.class)
public class CashflowService {
 
	@StreamListener(CashflowSink.T1_CASHFLOW_IN)
	@SendTo(CashflowSink.T2_CASHFLOW_OUT)
	public synchronized Cashflow receive1(String message) {
		System.out.println("******************");
		System.out.println("At Sink1");
		System.out.println("******************");
		System.out.println("Received message " + message);

		String arr[] = message.split(";");
		if (arr[0].equalsIgnoreCase("Cashflow")) {
			Cashflow cf = new Cashflow();
			cf.setContractId(Integer.parseInt(arr[1]));
			cf.setDate(arr[2]);
			cf.setAmount(Float.parseFloat(arr[3]));

			return cf;
		}

		return null;

	}

	/**
	 * Ein Intercepter, um den Key für die Kafka Message zu setzen
	 * vgl. https://stackoverflow.com/questions/56689257/how-to-set-the-key-of-the-message
	 * @param output
	 * @return
	 */
	@Bean 
	public ApplicationRunner runner(@Qualifier("t2_cashflow_out") MessageChannel output) {
		((AbstractMessageChannel) output).addInterceptor(0, new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				return MessageBuilder.fromMessage(message)
						.setHeader(KafkaHeaders.MESSAGE_KEY, (((Cashflow) message.getPayload()).getContractId()+"").getBytes()).build();						
			}

		});
		return args -> {
		};
	}

	@StreamListener(CashflowSink.T2_CASHFLOW_IN)
	public synchronized void receive2(Cashflow cashflow, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) byte[] key) {
		System.out.println("******************");		
		System.out.println("At Sink2");
		System.out.println("******************");
		System.out.println("Received cashflow " + cashflow + ", key:" + new String(key));		
	}

	public interface CashflowSink {
		String T1_CASHFLOW_IN = "t1_cashflow_in";
		String T2_CASHFLOW_IN = "t2_cashflow_in";
		String T1_CASHFLOW_OUT = "t1_cashflow_out";
		String T2_CASHFLOW_OUT = "t2_cashflow_out";

		@Input(T1_CASHFLOW_IN)
		SubscribableChannel t1_cashflow_in();

		@Input(T2_CASHFLOW_IN)
		SubscribableChannel t2_cashflow_in();

		@Output(T1_CASHFLOW_OUT)
		SubscribableChannel t1_cashflow_out();

		@Output(T2_CASHFLOW_OUT)
		SubscribableChannel t2_cashflow_out();
	}
}
