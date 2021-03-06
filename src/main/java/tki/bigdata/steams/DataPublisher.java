package tki.bigdata.steams;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import tki.bigdata.pojo.Cashflow;
import tki.bigdata.pojo.Category;
import tki.bigdata.pojo.Contract;

public class DataPublisher {
	private static String BOOTSTRAP_SERVER = "tobi0179.westeurope.cloudapp.azure.com:9092";

	public static void main(String[] args) {
		Map<String, Object> props1 = new HashMap<>();
		props1.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		props1.put(ProducerConfig.RETRIES_CONFIG, 0);
		props1.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props1.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props1.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props1.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props1.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


		DefaultKafkaProducerFactory<Integer, Contract> pf1 = new DefaultKafkaProducerFactory<>(props1);
		KafkaTemplate<Integer, Contract> template1 = new KafkaTemplate<>(pf1, true);
		template1.setDefaultTopic("tier2.contract.d");


		Contract contract = new Contract();
		contract.setId(1);
		contract.setName("contract abc");

		template1.sendDefault(1, contract);
		
		// ----------------------------------------------------------------------

				Map<String, Object> props = new HashMap<>();
				props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
				props.put(ProducerConfig.RETRIES_CONFIG, 0);
				props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
				props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
				props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
				props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
				props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


				DefaultKafkaProducerFactory<Integer, Category> pfCategory = new DefaultKafkaProducerFactory<>(props);
				KafkaTemplate<Integer, Category> templateCategory = new KafkaTemplate<>(pfCategory, true);
				templateCategory.setDefaultTopic("tier2.category.d");

				Category category = new Category();
				category.setId(1);
				category.setName("Auto");
				category.setDescription("Alles rund ums Auto");
				category.setRegex(".*TANK.*");
			
				templateCategory.sendDefault(1, category);
				
				category = new Category();
				category.setId(2);
				category.setName("Immo");
				category.setDescription("Alles rund um Immobilien");
				category.setRegex(".*HAUSG.*");
			
				templateCategory.sendDefault(1, category);

				
		// ----------------------------------------------------------------------

		props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


		DefaultKafkaProducerFactory<Integer, Cashflow> pf = new DefaultKafkaProducerFactory<>(props);
		KafkaTemplate<Integer, Cashflow> template = new KafkaTemplate<>(pf, true);
		template.setDefaultTopic("tier2.cashflow.d");


		Cashflow cashflow = new Cashflow();
		cashflow.setDate("current date-time");
		cashflow.setAmount(100.50f);
		cashflow.setContractId(1);
		template.sendDefault(1, cashflow);
	}

}
