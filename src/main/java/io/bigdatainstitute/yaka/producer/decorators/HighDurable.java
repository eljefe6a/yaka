package io.bigdatainstitute.yaka.producer.decorators;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;

import io.bigdatainstitute.yaka.producer.ProducerDecoratorImpl;

public class HighDurable<K, V> extends ProducerDecoratorImpl<K, V> {

	@Override
	public void initProducer(Properties producerProperties, Class<K> keyClass, Class<V> valueClass) {
		producerProperties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
	}

	@Override
	public void closeProducer() {

	}

}
