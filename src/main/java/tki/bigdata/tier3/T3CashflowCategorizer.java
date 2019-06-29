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
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;

import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Category;
import tki.bigdata.pojo.Contract;
import tki.bigdata.pojo.MyValueContainer;
import tki.bigdata.tier1.T1ContractService.ContractSink;
import tki.bigdata.tier2.T2CashflowService.CashflowSink;

@EnableBinding({ T3CashflowCategorizer.KStreamKTableBinding.class, Sink.class })
public class T3CashflowCategorizer {

	@StreamListener
	@SendTo("t3_cashflow_categorizer_categorized_cashflow_out")
	public KStream<Integer, Cashflow> processCategorizeCashflow (
			@Input("t2_cashflow_categorizer_cashflow_stream_in") KStream<Integer, Cashflow> cashflowStream,
			@Input("t2_cashflow_categorizer_category_stream_in") KTable<Integer, Category> categoryTable) {

		return cashflowStream.leftJoin(categoryTable, new ValueJoiner<Cashflow, Category, Cashflow>() {
			@Override
			public Cashflow apply(Cashflow cashflow, Category category) {
				cashflow.setCategory(category);
				return cashflow;
			}
		}, Joined.with(Serdes.Integer(), new JsonSerde<>(Cashflow.class), new JsonSerde<>(Category.class)));
	}


	
	@StreamListener("t3_cashflow_categorizer_categorized_cashflow_in")
	public synchronized void receive3(Cashflow cashflow, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) byte[] key) {
		System.out.println("******************");
		System.out.println("Tier3: At categorized cashflow Sink");
		System.out.println("******************");
		System.out.println("---------------------------------");
		System.out.println(cashflow);
		System.out.println("---------------------------------");
	}

	public interface KStreamKTableBinding {

		@Input("t2_cashflow_categorizer_cashflow_stream_in")
		KStream<?, ?> t2_cashflow_stream_in();

		@Input("t2_cashflow_categorizer_category_stream_in")
		KTable<?, ?> t2_category_stream_in();

		@Output("t3_cashflow_categorizer_categorized_cashflow_out")
		KStream<?, ?> t3_cashflow_categorizer_categorized_cashflow_out();

		@Input("t3_cashflow_categorizer_categorized_cashflow_in")
		SubscribableChannel t3_cashflow_categorizer_categorized_cashflow_in();
		
	}
	

}
