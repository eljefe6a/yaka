package io.bigdatainstitute.yaka.listener;

import java.util.ArrayList;
import java.util.Properties;

public abstract class Consumer<K, V> implements AutoCloseable {
	DataListener<K, V> listener;

	public ArrayList<ListenerDecorator<K, V>> preRunListeners = new ArrayList<>();
	public ArrayList<ListenerDecorator<K, V>> preReceiveLoopListeners = new ArrayList<>();
	public ArrayList<ListenerDecorator<K, V>> preReceiveListeners = new ArrayList<>();
	public ArrayList<ListenerDecorator<K, V>> postReceiveListeners = new ArrayList<>();
	public ArrayList<ListenerDecorator<K, V>> postReceiveLoopListeners = new ArrayList<>();

	public String brokers;
	public String topic;
	public String consumerGroupName;

	ListenerDecorator<K, V>[] decorators;

	Class<K> keyClass;
	Class<V> valueClass;

	@SafeVarargs
	public Consumer(String brokers, String topic, String consumerGroupName, Class<K> keyClass, Class<V> valueClass,
			ListenerDecorator<K, V>... decorators) {
		this.brokers = brokers;
		this.topic = topic;
		this.consumerGroupName = consumerGroupName;

		this.decorators = decorators;

		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public abstract void init();

	public void addListener(DataListener<K, V> listener) {
		if (this.listener != null) {
			throw new RuntimeException("Only one listener can be added and should only be called once.");
		} else {
			this.listener = listener;
		}
	}

	public DataListener<K, V> getListener() {
		return listener;
	}

	/**
	 * Closes the consumer and performs any other cleanup
	 */
	public abstract void close();

	/**
	 * Blocks the calling thread until the consumer thread closes
	 */
	public abstract void blockUntilClosed();

	/**
	 * Goes through all decorators and offers them the event to accept or not
	 * 
	 * @param consumerProperties
	 *            The consumer's properties object to add new settings
	 */
	public void registerDecorators(Properties consumerProperties) {
		for (ListenerDecorator<K, V> decorator : decorators) {
			decorator.initListener(consumerProperties, keyClass, valueClass);
		}

		for (ListenerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPreRun()) {
				preRunListeners.add(decorator);
			}
		}

		for (ListenerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPreReceiveLoop()) {
				preReceiveLoopListeners.add(decorator);
			}
		}

		for (ListenerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPreReceive()) {
				preReceiveListeners.add(decorator);
			}
		}

		for (ListenerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPostReceiveLoop()) {
				postReceiveListeners.add(decorator);
			}
		}

		for (ListenerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPostReceiveLoop()) {
				postReceiveLoopListeners.add(decorator);
			}
		}
	}

	public void preRun(Consumer<K, V> consumer) {
		for (ListenerDecorator<K, V> decorator : preRunListeners) {
			decorator.preRun(consumer);
		}
	}

	public void preReceiveLoop(Consumer<K, V> consumer) {
		for (ListenerDecorator<K, V> decorator : preReceiveLoopListeners) {
			decorator.preReceiveLoop(consumer);
		}
	}

	public void preReceive(Consumer<K, V> consumer, K key, V value) {
		for (ListenerDecorator<K, V> decorator : preReceiveListeners) {
			decorator.preReceive(consumer, key, value);
		}
	}

	public void postReceive(Consumer<K, V> consumer, K key, V value) {
		for (ListenerDecorator<K, V> decorator : postReceiveListeners) {
			decorator.postReceive(consumer, key, value);
		}
	}

	public void postReceiveLoop(Consumer<K, V> consumer) {
		for (ListenerDecorator<K, V> decorator : postReceiveLoopListeners) {
			decorator.postReceiveLoop(consumer);
		}
	}
}
