package io.bigdatainstitute.yaka.producer;

import java.util.Properties;

public abstract class ProducerDecorator<K, V> {
	public abstract void init(Properties producerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public void preProduce(Producer<K, V> producer, K key, V value) {
		
	};
	
	public void postProduce(Producer<K, V> producer, K key, V value) {
		
	};
	
	public void messageAcknowleged(Producer<K, V> producer, K key, V value) {
		
	}
	
	public abstract void close();
	
	public boolean offerPreProduce() {
		return false;
	};
	
	public boolean offerPostProduce() {
		return false;
	};
	
	public boolean offerMessageAcknowleged() {
		return false;
	};
}
