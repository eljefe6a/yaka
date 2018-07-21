package io.bigdatainstitute.yaka.example;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.listener.ListenerDecorators;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;

public class ListenerOnly {
	public static void main(String[] args) {
		String brokers = "broker";
		String topic = "test";
		String consumerGroup = "consumergroup";

		try (Consumer consumer = new KafkaConsumerImpl(brokers, topic, consumerGroup,
				ListenerDecorators.EXACTLY_ONCE);) {
			consumer.addListener(new DataListener() {
				@Override
				public void dataReceived(Object key, Object value) {
					// Do something
				}
			});
		}
	}
}
