package io.bigdatainstitute.yaka.example;

import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.listener.ListenerDecorators;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;
import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorators;
import io.bigdatainstitute.yaka.producer.kafkaproducerimpl.KafkaProducerImpl;

public class ConsumerProducer {
	private static Logger logger = Logger.getLogger(ConsumerProducer.class);

	public static void main(String[] args) {
		String brokers = "broker";
		String inputTopic = "input";
		String outputTopic = "output";
		String consumerGroup = "consumergroup";

		try (Consumer consumer = new KafkaConsumerImpl(brokers, inputTopic, consumerGroup,
				ListenerDecorators.EXACTLY_ONCE);
				Producer producer = new KafkaProducerImpl(brokers, outputTopic, ProducerDecorators.HIGH_DURABLE);) {
			consumer.addListener(new DataListener() {

				@Override
				public void dataReceived(Object key, Object value) {
					producer.produce(key, value);

				}
			});
		} catch (Exception e) {
			logger.error("Error consuming and producing", e);
		}
	}
}
