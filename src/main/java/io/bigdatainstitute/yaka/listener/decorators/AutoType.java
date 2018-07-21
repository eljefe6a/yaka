package io.bigdatainstitute.yaka.listener.decorators;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.bigdatainstitute.yaka.listener.ListenerDecorator;

public class AutoType<K, V> extends ListenerDecorator<K, V> {

	@Override
	public void init(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
		setDeserializerForType(consumerProperties, keyClass, ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
		setDeserializerForType(consumerProperties, valueClass, ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
	}

	private void setDeserializerForType(Properties consumerProperties, Class<?> type, String setting) {
		String deserializer = null;

		if (type.toString().equals("String")) {
			deserializer = StringDeserializer.class.toString();
		}
		
		// TODO: Add more

		consumerProperties.setProperty(setting, deserializer);
	}

	@Override
	public void close() {

	}

}
