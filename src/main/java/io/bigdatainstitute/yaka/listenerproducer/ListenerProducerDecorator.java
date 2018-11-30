package io.bigdatainstitute.yaka.listenerproducer;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.ListenerDecorator;
import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorator;

public abstract class ListenerProducerDecorator<K, V, K2, V2> implements ListenerDecorator<K, V>, ProducerDecorator<K2, V2> {
	public boolean offerPreReceiveLoop() {
		return false;
	}
	
	public boolean offerPreReceive() {
		return false;
	}
	
	public boolean offerPostReceive() {
		return false;
	}
	
	public boolean offerPostReceiveLoop() {
		return false;
	}
	
	public void preReceiveLoop(Consumer<K, V> consumer) {
		
	}
	
	public void preReceive(Consumer<K, V> consumer, K key, V value) {
		
	}
	
	public void postReceive(Consumer<K, V> consumer, K key, V value) {
		
	}
	
	public void postReceiveLoop(Consumer<K, V> consumer) {
		
	}
	
	public void preProduce(Producer<K2, V2> producer, K2 key, V2 value) {
		
	};
	
	public void postProduce(Producer<K2, V2> producer, K2 key, V2 value) {
		
	};
	
	public void messageAcknowleged(Producer<K2, V2> producer, K2 key, V2 value) {
		
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
