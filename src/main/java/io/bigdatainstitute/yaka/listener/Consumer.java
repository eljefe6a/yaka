package io.bigdatainstitute.yaka.listener;

import java.util.ArrayList;
import java.util.Properties;

public abstract class Consumer<K, V> implements AutoCloseable {
	public ArrayList<DataListener<K, V>> listeners;

	public ArrayList<ListenerDecorator<K, V>> preReceiveLoopListeners;
	public ArrayList<ListenerDecorator<K, V>> preReceiveListeners;
	public ArrayList<ListenerDecorator<K, V>> postReceiveListeners;
	public ArrayList<ListenerDecorator<K, V>> postReceiveLoopListeners;

	public String brokers;
	public String topic;
	public String consumerGroupName;

	ListenerDecorator<K, V>[] decorators;
	
	Class<K> keyClass;
	Class<V> valueClass;

	@SafeVarargs
	public Consumer(String brokers, String topic, String consumerGroupName, Class<K> keyClass, Class<V> valueClass, ListenerDecorator<K, V>... decorators) {
		this.brokers = brokers;
		this.topic = topic;
		this.consumerGroupName = consumerGroupName;

		this.decorators = decorators;
		
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public abstract void init();

	public void addListener(DataListener<K, V> listener) {
		listeners.add(listener);
	}

	public abstract void close();

	public void registerDecorators(Properties consumerProperties) {
		for (ListenerDecorator<K, V> decorator : decorators) {
			decorator.init(consumerProperties, keyClass, valueClass);
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

	public void preReceiveLoop() {
		for (ListenerDecorator<K, V> decorator : preReceiveLoopListeners) {
			decorator.preReceiveLoop();
		}
	}

	public void preReceive() {
		for (ListenerDecorator<K, V> decorator : preReceiveListeners) {
			decorator.preReceive();
		}
	}

	public void postReceive() {
		for (ListenerDecorator<K, V> decorator : postReceiveListeners) {
			decorator.postReceive();
		}
	}

	public void postReceiveLoop() {
		for (ListenerDecorator<K, V> decorator : postReceiveLoopListeners) {
			decorator.postReceiveLoop();
		}
	}
}
