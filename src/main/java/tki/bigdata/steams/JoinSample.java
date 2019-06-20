package tki.bigdata.steams;

import java.io.InputStream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import tki.bigdata.pojo.MyValueContainer;
import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Contract;

@EnableBinding(JoinSample.KStreamKTableBinding.class)
public class JoinSample {

	@StreamListener
	@SendTo("t3_joined_out")
	public KStream<String, MyValueContainer> process(
			@Input("t2_cashflow_stream_in") KStream<String, Cashflow> cashflowStream,
			@Input("t2_contract_stream_in") KTable<String, Contract> contractTable) {

		// This one does not work at all
		return  cashflowStream
				.leftJoin(contractTable, (cashflow, contract) -> new MyValueContainer(cashflow, contract),				
						Joined.with(Serdes.String(), new JsonSerde(Cashflow.class), new JsonSerde(Contract.class)))
				;
		
//		// This one works half, but value2 of the the result is not right
		// {"value1":{"date":"20190601","amount":12.5,"contractId":1,"contract":null},"value2":"eyJpZCI6MSwibmFtZSI6ImFiYyJ9"}
//		return cashflowStream.leftJoin(contractTable, new ValueJoiner(){
//			@Override
//			public Object apply(Object value1, Object value2) {
//				System.out.println("we create a new MyValueContainer");
//				return new MyValueContainer(value1, value2);
//			}});
	}

	public interface KStreamKTableBinding {

		@Input("t2_cashflow_stream_in")
		KStream<?, ?> t2_cashflow_stream_in();

		@Input("t2_contract_stream_in")
		KTable<?, ?> t2_contract_stream_in();

		@Output("t3_joined_out")
		KStream<?, ?> t3_joined_out();
	}
}
