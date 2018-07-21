package io.bigdatainstitute.yaka.producer;

import java.util.ArrayList;
import java.util.Properties;

public abstract class Producer<K, V> implements AutoCloseable {
	public ArrayList<ProducerDecorator<K, V>> preProduceListeners = new ArrayList<>();
	public ArrayList<ProducerDecorator<K, V>> postProduceListeners = new ArrayList<>();
	public ArrayList<ProducerDecorator<K, V>> messageAcknowledgedListeners = new ArrayList<>();
	
	public String brokers;
	public String topic;

	ProducerDecorator<K, V>[] decorators;
	
	Class<K> keyClass;
	Class<V> valueClass;

	@SafeVarargs
	public Producer(String brokers, String topic, Class<K> keyClass, Class<V> valueClass, ProducerDecorator<K, V>... decorators) {
		this.brokers = brokers;
		this.topic = topic;

		this.decorators = decorators;
		
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}
	
	public abstract void init();
	
	public abstract void produce(K key, V value);

	public void registerDecorators(Properties producerProperties) {
		for (ProducerDecorator<K, V> decorator : decorators) {
			decorator.init(producerProperties, keyClass, valueClass);
		}

		for (ProducerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPreProduce()) {
				preProduceListeners.add(decorator);
			}
		}

		for (ProducerDecorator<K, V> decorator : decorators) {
			if (decorator.offerPostProduce()) {
				postProduceListeners.add(decorator);
			}
		}
		
		for (ProducerDecorator<K, V> decorator : decorators) {
			if (decorator.offerMessageAcknowleged()) {
				messageAcknowledgedListeners.add(decorator);
			}
		}
	}
	
	public void preProduce(Producer<K, V> producer, K key, V value) {
		for (ProducerDecorator<K, V> decorator : preProduceListeners) {
			decorator.preProduce(producer, key, value);
		}
	}
	
	public void postProduce(Producer<K, V> producer, K key, V value) {
		for (ProducerDecorator<K, V> decorator : postProduceListeners) {
			decorator.postProduce(producer, key, value);
		}
	}
	
	public void messageAcknowledged(Producer<K, V> producer, K key, V value) {
		for (ProducerDecorator<K, V> decorator : messageAcknowledgedListeners) {
			decorator.messageAcknowleged(producer, key, value);
		}
	}
}
