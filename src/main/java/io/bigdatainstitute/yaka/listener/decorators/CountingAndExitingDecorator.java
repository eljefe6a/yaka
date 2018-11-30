package io.bigdatainstitute.yaka.listener.decorators;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listenerproducer.ListenerProducerDecorator;
import io.bigdatainstitute.yaka.producer.Producer;

public class CountingAndExitingDecorator<K, V, K2, V2> extends ListenerProducerDecorator<K, V, K2, V2> {
	private static final Logger logger = LogManager.getLogger("CountingAndExitDecorator");

	AtomicLong messagesProduced;
	AtomicLong messagesReceived;

	/** Timer to output logging information */
	Timer outputTimer;

	/**
	 * The amount of time in seconds between firings of the timer and output of
	 * logging
	 */
	long timeInterval;
		
	/**
	 * Constructor
	 * 
	 * @param timeInterval
	 *            The amount of time in seconds between firings of the timer and
	 *            output of logging
	 */
	public CountingAndExitingDecorator(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	@Override
	public void initListener(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
		// Do all inits in this init
		messagesReceived = new AtomicLong(0);
		messagesProduced = new AtomicLong(0);

		// Create the timer and schedule the logging task
		outputTimer = new Timer("Messages Timer");
		outputTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Get the current count and reset back to 0
				long messagesReceivedInInterval = messagesReceived.getAndSet(0);
				long messagesProducedInInterval = messagesProduced.getAndSet(0);

				// Log out the count
				logger.info(messagesReceivedInInterval + " messages received " + messagesProducedInInterval
						+ " messages produced in interval");

				if (messagesReceivedInInterval == 0 || messagesProducedInInterval == 0) {
					logger.fatal("Exiting - no messages received or sent: " + messagesReceivedInInterval
							+ " messages received " + messagesProducedInInterval + " messages produced in interval");
					System.exit(-1);
				}
			}
		}, timeInterval * 1000, timeInterval * 1000);
	}

	@Override
	public void closeListener() {
	}

	@Override
	public void initProducer(Properties producerProperties, Class<K2> keyClass, Class<V2> valueClass) {
		// Inited in listener
	}

	@Override
	public void closeProducer() {

	}

	@Override
	public void preReceive(Consumer<K, V> consumer, K key, V value) {
		messagesReceived.incrementAndGet();
	}

	@Override
	public void postProduce(Producer<K2, V2> producer, K2 key, V2 value) {
		messagesProduced.incrementAndGet();
	};

	@Override
	public boolean offerPostProduce() {
		return true;
	}

	@Override
	public boolean offerPreReceive() {
		return true;
	}
}
