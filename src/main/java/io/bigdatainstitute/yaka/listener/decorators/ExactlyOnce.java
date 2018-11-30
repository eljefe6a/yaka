package io.bigdatainstitute.yaka.listener.decorators;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.ListenerDecoratorImpl;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;

public class ExactlyOnce<K, V> extends ListenerDecoratorImpl<K, V> {
	Logger logger = Logger.getLogger(ExactlyOnce.class);
	
	@Override
	public void initListener(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
		consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		
		logger.info("Set to programmatically update offset");
	}
	
	@Override
	public void postReceive(Consumer<K, V> consumer, K key, V value) {
		// TODO: Figure out place to commit
		((KafkaConsumerImpl<K, V>) consumer).getKafkaConsumer().commitSync();
	}
	
	@Override
	public void closeListener() {
		
	}
	
}
