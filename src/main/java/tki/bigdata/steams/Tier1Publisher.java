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

public class Tier1Publisher {
	private static String BOOTSTRAP_SERVER = "tobi0179.westeurope.cloudapp.azure.com:9092";

	public static void main(String[] args) {
		Map<String, Object> props1 = new HashMap<>();
		props1.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		props1.put(ProducerConfig.RETRIES_CONFIG, 0);
		props1.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props1.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props1.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props1.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props1.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);


		DefaultKafkaProducerFactory<Integer, String> pf1 = new DefaultKafkaProducerFactory<>(props1);
		KafkaTemplate<Integer, String> template1 = new KafkaTemplate<>(pf1, true);
		template1.setDefaultTopic("tier1.contract.d");


		template1.sendDefault(1, "Contract;1;abc");
		
		// ----------------------------------------------------------------------

				Map<String, Object> props = new HashMap<>();
				props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
				props.put(ProducerConfig.RETRIES_CONFIG, 0);
				props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
				props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
				props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
				props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
				props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);


				DefaultKafkaProducerFactory<Integer, String> pfCategory = new DefaultKafkaProducerFactory<>(props);
				KafkaTemplate<Integer, String> templateCategory = new KafkaTemplate<>(pfCategory, true);
				templateCategory.setDefaultTopic("tier1.category.d");

				
			
				templateCategory.sendDefault(1, "Category;1;Auto;Bla;.*TANK.*");
				templateCategory.sendDefault(1, "Category;1;Immo;Bla;.*HAUSG.*");

				
		// ----------------------------------------------------------------------

		props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


		DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(props);
		KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
		template.setDefaultTopic("tier1.cashflow.d");


		template.sendDefault(1, "Cashflow;1;20190602;2.5");
	}

}
