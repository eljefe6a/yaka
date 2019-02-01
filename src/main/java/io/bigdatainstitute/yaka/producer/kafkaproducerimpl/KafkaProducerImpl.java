package io.bigdatainstitute.yaka.producer.kafkaproducerimpl;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorator;

public class KafkaProducerImpl<K, V> extends Producer<K, V> {
	Logger logger = Logger.getLogger(KafkaProducerImpl.class);

	KafkaProducer<K, V> producer;

	@SafeVarargs
	public KafkaProducerImpl(String brokers, String topic, Class<K> keyClass, Class<V> valueClass,
			ProducerDecorator<K, V>... decorators) {
		super(brokers, topic, keyClass, valueClass, decorators);
	}

	@Override
	public void produce(K key, V value) {
		preProduce(this, key, value);

		if (hasMessageAcknowledgedListeners == true) {
			// Create callback and acknowledge message
			producer.send(new ProducerRecord<K, V>(topic, key, value),
					((RecordMetadata metadata, Exception exception) -> this.messageAcknowledged(this, key, value)));
		} else {
			producer.send(new ProducerRecord<K, V>(topic, key, value));
		}

		postProduce(this, key, value);

	}

	@Override
	public void close() throws Exception {
		producer.close();
	}

	@Override
	public void init() {
		Properties propsProd = new Properties();
		propsProd.put(BOOTSTRAP_SERVERS_CONFIG, brokers);

		// Initialize all decorators
		for (ProducerDecorator<K, V> producerDecorator : decorators) {
			producerDecorator.initProducer(propsProd, keyClass, valueClass);
		}

		registerDecorators(propsProd);

		producer = new KafkaProducer<>(propsProd);
	}

	public KafkaProducer<K, V> getProducer() {
		return producer;
	}
}