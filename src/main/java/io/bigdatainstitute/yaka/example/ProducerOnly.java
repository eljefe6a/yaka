package io.bigdatainstitute.yaka.example;

import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.decorators.HighDurable;
import io.bigdatainstitute.yaka.producer.decorators.ProducerAutoType;
import io.bigdatainstitute.yaka.producer.kafkaproducerimpl.KafkaProducerImpl;

public class ProducerOnly {
	private static Logger logger = Logger.getLogger(ProducerOnly.class);

	public static void main(String[] args) {
		String brokers = "brokers";
		String topic = "test";

		try (Producer<String, String> producer = new KafkaProducerImpl<>(brokers, topic, String.class, String.class,
				new ProducerAutoType<>(), new HighDurable<>());) {
			producer.init();
			producer.produce("key", "value");
		} catch (Exception e) {
			logger.error("Error producing", e);
		}
	}
}
