package io.bigdatainstitute.yaka.producer;

import java.util.ArrayList;
import java.util.Properties;

public abstract class Producer<K, V> implements AutoCloseable {
	public ArrayList<ProducerDecorator<K, V>> preProduceListeners = new ArrayList<>();
	public ArrayList<ProducerDecorator<K, V>> postProduceListeners = new ArrayList<>();
	public ArrayList<ProducerDecorator<K, V>> finishProduceListeners = new ArrayList<>();
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

	/**
	 * Optional. Do things like flush or commit a transaction
	 */
	public abstract void finish();
	
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
			if (decorator.offerFinishProduce()) {
				finishProduceListeners.add(decorator);
			}
		}
		
		for (ProducerDecorator<K, V> decorator : decorators) {
			if (decorator.offerMessageAcknowleged()) {
				messageAcknowledgedListeners.add(decorator);
			}
		}
	}
}
