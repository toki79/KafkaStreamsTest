package tki.bigdata.tier2;

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

@EnableBinding(T2CashflowService.CashflowSink.class)
public class T2CashflowService {

	@StreamListener(CashflowSink.T2_CASHFLOW_IN)
	public synchronized void receive2(Cashflow cashflow, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) byte[] key) {
		System.out.println("******************");
		System.out.println("Tier 2: At Cashflow Sink2");
		System.out.println("******************");
		System.out.println("Received cashflow " + cashflow + ", key:" + new String(key));
	}

	public interface CashflowSink {

		String T2_CASHFLOW_IN = "t2_cashflow_in";

		@Input(T2_CASHFLOW_IN)
		SubscribableChannel t2_cashflow_in();
	}
}
