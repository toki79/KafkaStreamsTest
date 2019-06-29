package tki.bigdata.tier3;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;



import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Contract;
import tki.bigdata.pojo.MyValueContainer;
import tki.bigdata.tier1.T1ContractService.ContractSink;


@EnableBinding({ T3JoinSample.JoinSampleBinding.class, Sink.class })
public class T3JoinSample  {

	@StreamListener
	@SendTo("t3_join_sample_joined_out")
	public KStream<Integer, MyValueContainer> processJoinSample(
			@Input("t2_join_sample_cashflow_stream_in") KStream<Integer, Cashflow> cashflowStream,
			@Input("t2_join_sample_contract_stream_in") KTable<Integer, Contract> contractTable) {

		return  cashflowStream
				.leftJoin(contractTable,						
						MyValueContainer::new,
						Joined.with(Serdes.Integer(), new JsonSerde<>(Cashflow.class), new JsonSerde<>(Contract.class)));
	}
	
	
	@StreamListener("t3_join_sample_joined_in")
	public void readFromKStreamProcessorOutput(MyValueContainer myValueContainer) {
		System.out.println("******************");
		System.out.println("Tier3: At JoinSample Sink");
		System.out.println("******************");
		System.out.println("Joined Cashflow/Contract details");
		System.out.println("---------------------------------");
		System.out.println(myValueContainer.getValue1());
		System.out.println(myValueContainer.getValue2());		
		System.out.println("---------------------------------");
	}
	


	public interface JoinSampleBinding {

		@Input("t2_join_sample_cashflow_stream_in")
		KStream<?, ?> t2_join_sample_cashflow_stream_in();

		@Input("t2_join_sample_contract_stream_in")
		KTable<?, ?> t2_join_sample_contract_stream_in();

		@Output("t3_join_sample_joined_out")
		KStream<?, ?> t3_join_sample_joined_out();
		
		@Input("t3_join_sample_joined_in")
		SubscribableChannel t3_join_sample_joined_in();
		


	}
}
