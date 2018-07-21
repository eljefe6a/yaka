package io.bigdatainstitute.yaka.producer.kafkaproducerimpl;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorator;

public class KafkaProducerImpl<K, V> extends Producer<K, V> {
	Logger logger = Logger.getLogger(KafkaProducerImpl.class);

	KafkaProducer<K, V> producer;

	@SafeVarargs
	public KafkaProducerImpl(String brokers, String topic, Class<K> keyClass, Class<V> valueClass, ProducerDecorator<K, V>... decorators) {
		super(brokers, topic, keyClass, valueClass, decorators);
	}

	@Override
	public void produce(K key, V value) {
		// TODO: Add rest of produce events
		
		// TODO: Add message acknowledgments event
		producer.send(new ProducerRecord<K, V>(topic, key, value));
	}

	@Override
	public void finish() {

	}

	@Override
	public void close() throws Exception {
		producer.close();
	}

	@Override
	public void init() {
		Properties propsProd = new Properties();
		propsProd.put(BOOTSTRAP_SERVERS_CONFIG, brokers);
		
		registerDecorators(propsProd);

		producer = new KafkaProducer<>(propsProd);
	}
}