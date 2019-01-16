package io.bigdatainstitute.yaka.listener.kafkaconsumerimpl;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

	KafkaConsumer<K, V> kafkaConsumer;
	
	/** Thread pool to run consumer in */
    ExecutorService threadPool;

    ConsumerThread consumerThread;
    
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

		threadPool = Executors.newFixedThreadPool(1);
	}

	@Override
	public void close() {
		if (consumerThread != null) {
			consumerThread.close();
		}
	}

	@Override
	public void addListener(DataListener<K, V> listener) {
		super.addListener(listener);

		// Create the consumer and subscribe to the topic
		kafkaConsumer = new KafkaConsumer<K, V>(props);
		kafkaConsumer.subscribe(Arrays.asList(topic));

		consumerThread = new ConsumerThread(this);
		threadPool.submit(consumerThread);
	}

	public KafkaConsumer<K, V> getKafkaConsumer() {
		return kafkaConsumer;
	}
	
	public class ConsumerThread implements Runnable {
	    /** Atomic boolean to keep track of wanting to close connection */
	    private AtomicBoolean isClosed = new AtomicBoolean(false);
	    
		KafkaConsumerImpl<K, V> consumer;
		
		public ConsumerThread(KafkaConsumerImpl<K, V> consumer) {
			this.consumer = consumer;
		}

		@Override
		public void run() {
			preRun(consumer);
			
			while (isClosed.get() == false) {
				preReceiveLoop(consumer);

				ConsumerRecords<K, V> records = consumer.kafkaConsumer.poll(Long.MAX_VALUE);

				for (ConsumerRecord<K, V> record : records) {
					preReceive(consumer, record.key(), record.value());

					consumer.getListener().dataReceived(record.key(), record.value());

					postReceive(consumer, record.key(), record.value());
				}

				postReceiveLoop(consumer);
			}
		}
		
		public void close() {
	        // Set the atomic boolean because we want while loop to stop
	        isClosed.set(true);
	        // Wake up the consumer if it is in a poll waiting for data
	        consumer.kafkaConsumer.wakeup();
	        // Shutdown the thread pool to stop accepting new tasks
	        threadPool.shutdown();

	        try {
	            // Wait for the consumer thread to finish
	            threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);
	        } catch (InterruptedException e) {
	            logger.error("Error while awaiting termination", e);
	        }

	        consumer.close();
	    }
	}
}