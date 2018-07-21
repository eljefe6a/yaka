package io.bigdatainstitute.yaka.listener.kafkaconsumerimpl;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.listener.ListenerDecorator;

public class KafkaConsumerImpl<K, V> extends Consumer<K, V> {
	Logger logger = Logger.getLogger(KafkaConsumerImpl.class);

	Properties props = new Properties();

	KafkaConsumer<K, V> consumer;

	@SafeVarargs
	public KafkaConsumerImpl(String brokers, String topic, String consumerGroupName, Class<K> keyClass,
			Class<V> valueClass, ListenerDecorator<K, V>... decorators) {
		super(brokers, topic, consumerGroupName, keyClass, valueClass, decorators);
	}

	@Override
	public void init() {
		// Configure initial location bootstrap servers
		props.put(BOOTSTRAP_SERVERS_CONFIG, brokers);
		// Configure consumer group
		props.put(GROUP_ID_CONFIG, consumerGroupName);

		registerDecorators(props);

	}

	@Override
	public void close() {
		consumer.close();
	}

	@Override
	public void addListener(DataListener<K, V> listener) {
		super.addListener(listener);

		// TODO: Handle multiple listeners

		// TOOD: Add threading

		// Create the consumer and subscribe to the topic
		consumer = new KafkaConsumer<K, V>(props);
		consumer.subscribe(Arrays.asList(topic));

		while (true) {
			preReceiveLoop(this);

			ConsumerRecords<K, V> records = consumer.poll(Long.MAX_VALUE);

			for (ConsumerRecord<K, V> record : records) {
				for (DataListener<K, V> currentListener : listeners) {
					preReceive(this, record.key(), record.value());

					currentListener.dataReceived(record.key(), record.value());

					postReceive(this, record.key(), record.value());
				}
			}

			postReceiveLoop(this);
		}
	}

	public KafkaConsumer<K, V> getKafkaConsumer() {
		return consumer;
	}
}