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

import tki.bigdata.pojo.Category;



@EnableBinding(T1CategoryService.CategorySink.class)
public class T1CategoryService {
	
	/**
	 * Ein Intercepter, um den Key für die Kafka Message zu setzen
	 * vgl. https://stackoverflow.com/questions/56689257/how-to-set-the-key-of-the-message
	 * @param output
	 * @return
	 */
	@Bean 
	public ApplicationRunner categoryRunner(@Qualifier("t2_category_out") MessageChannel output) {
		((AbstractMessageChannel) output).addInterceptor(0, new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				return MessageBuilder.fromMessage(message)
						.setHeader(KafkaHeaders.MESSAGE_KEY, (((Category) message.getPayload()).getId()+"").getBytes()).build();						
			} 

		});
		return args -> {
		};
	}

	@StreamListener(CategorySink.T1_CATEGORY_IN)
	@SendTo(CategorySink.T2_CATEGORY_OUT)
	public synchronized Category receive1(String message) {
		System.out.println("******************");
		System.out.println("Tier 1: At Category Sink1");
		System.out.println("******************");
		System.out.println("Received message " + message);
		
		String arr[] = message.split(";");
		if (arr[0].equalsIgnoreCase("Category")) {
			Category c = new Category();
			c.setId(Integer.parseInt(arr[1]));
			c.setName(arr[2]);
			c.setDescription(arr[3]);
			c.setRegex(arr[4]);
			return c;
		}
	
		return null;
	}

	

	public interface CategorySink {
		String T1_CATEGORY_IN = "t1_category_in";
		
		String T1_CATEGORY_OUT = "t1_category_out";
        String T2_CATEGORY_OUT = "t2_category_out";
		
		@Input(T1_CATEGORY_IN)
		SubscribableChannel t1_category_in();
		
		@Output(T1_CATEGORY_OUT)
		SubscribableChannel t1_category_out();
		@Output(T2_CATEGORY_OUT)
		SubscribableChannel t2_category_out();
}
}
