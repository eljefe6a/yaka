package io.bigdatainstitute.yaka.listener.decorators;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.bigdatainstitute.yaka.listener.ListenerDecoratorImpl;

public class ListenerAutoType<K, V> extends ListenerDecoratorImpl<K, V> {

	@Override
	public void initListener(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
		setDeserializerForType(consumerProperties, keyClass, ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
		setDeserializerForType(consumerProperties, valueClass, ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
	}

	private void setDeserializerForType(Properties consumerProperties, Class<?> type, String setting) {
		String deserializer = null;

		if (type.equals(String.class)) {
			deserializer = StringDeserializer.class.getName();
		} else {
			throw new RuntimeException("Type not found in list. Type was " + type.toString());
		}
		
		// TODO: Add more

		consumerProperties.setProperty(setting, deserializer);
	}

	@Override
	public void closeListener() {

	}

}
