package io.bigdatainstitute.yaka.example;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.decorators.ExactlyOnce;
import io.bigdatainstitute.yaka.listener.decorators.ListenerAutoType;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;

public class ListenerOnlyLambda {
	public static void main(String[] args) {
		String brokers = "broker";
		String topic = "test";
		String consumerGroup = "consumergroup";

		try (Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, topic, consumerGroup, String.class,
				String.class, new ListenerAutoType<>(), new ExactlyOnce<>());) {
			consumer.init();
			consumer.addListener((String key, String value) -> key.length() /* Do something with key/value */);
			consumer.blockUntilClosed();
		}
	}
}
