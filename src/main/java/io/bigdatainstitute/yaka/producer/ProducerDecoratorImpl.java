package io.bigdatainstitute.yaka.producer;

public abstract class ProducerDecoratorImpl<K, V> implements ProducerDecorator<K, V> {
	public void preProduce(Producer<K, V> producer, K key, V value) {
		
	};
	
	public void postProduce(Producer<K, V> producer, K key, V value) {
		
	};
	
	public void messageAcknowleged(Producer<K, V> producer, K key, V value) {
		
	}
	
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
