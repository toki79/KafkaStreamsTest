package tki.bigdata.serde;

import org.springframework.kafka.support.serializer.JsonSerde;

import tki.bigdata.pojo.MyValueContainer;

public class MyValueContainerSerde extends JsonSerde<MyValueContainer> {
}
