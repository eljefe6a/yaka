package io.bigdatainstitute.yaka.example;

import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorators;
import io.bigdatainstitute.yaka.producer.kafkaproducerimpl.KafkaProducerImpl;

public class ProducerOnly {
	private static Logger logger = Logger.getLogger(ProducerOnly.class);
	
	public static void main(String[] args) {
		String brokers = "brokers";
		String topic = "test";
		
		try (Producer producer = new KafkaProducerImpl(brokers, topic, ProducerDecorators.HIGH_DURABLE);) {
			producer.produce("key", "value");
		} catch (Exception e) {
			logger.error("Error producing", e);
		}
	}
}
