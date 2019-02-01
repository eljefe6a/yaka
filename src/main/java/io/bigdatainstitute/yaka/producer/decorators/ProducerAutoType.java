package io.bigdatainstitute.yaka.producer.decorators;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import io.bigdatainstitute.yaka.producer.ProducerDecoratorImpl;

public class ProducerAutoType<K, V> extends ProducerDecoratorImpl<K, V> {
	@Override
	public void initProducer(Properties producerProperties, Class<K> keyClass, Class<V> valueClass) {
		setDeserializerForType(producerProperties, keyClass, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG);
		setDeserializerForType(producerProperties, valueClass, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);
	}

	private void setDeserializerForType(Properties consumerProperties, Class<?> type, String setting) {
		String deserializer = null;

		if (type.equals(String.class)) {
			deserializer = StringSerializer.class.getName();
		} else {
			throw new RuntimeException("Type not found in list. Type was " + type.toString());
		}
		
		// TODO: Add more

		consumerProperties.setProperty(setting, deserializer);
	}

	@Override
	public void closeProducer() {
		// TODO Auto-generated method stub
		
	}
	
}
