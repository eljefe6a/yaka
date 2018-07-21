package io.bigdatainstitute.yaka.example;

import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.listener.decorators.ExactlyOnce;
import io.bigdatainstitute.yaka.listener.decorators.ListenerAutoType;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;
import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.decorators.HighDurable;
import io.bigdatainstitute.yaka.producer.kafkaproducerimpl.KafkaProducerImpl;

public class ListenerProducerLong<K, V> {
	private static Logger logger = Logger.getLogger(ListenerProducerLong.class);

	public static void main(String[] args) {
		String brokers = "broker";
		String inputTopic = "input";
		String outputTopic = "output";
		String consumerGroup = "consumergroup";

		try (Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, inputTopic, consumerGroup,
				String.class, String.class, new ListenerAutoType<>(), new ExactlyOnce<>());
				Producer<String, String> producer = new KafkaProducerImpl<String, String>(brokers, outputTopic,
						String.class, String.class, new HighDurable<String, String>());) {
			consumer.addListener(new DataListener<String, String>() {

				@Override
				public void dataReceived(String key, String value) {
					producer.produce(key, value);

				}
			});
		} catch (Exception e) {
			logger.error("Error consuming and producing", e);
		}
	}
}
