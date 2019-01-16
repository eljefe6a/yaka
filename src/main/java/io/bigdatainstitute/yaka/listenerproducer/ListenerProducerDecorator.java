package io.bigdatainstitute.yaka.listenerproducer;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.ListenerDecorator;
import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorator;

public abstract class ListenerProducerDecorator<K, V, K2, V2> implements ListenerDecorator<K, V>, ProducerDecorator<K2, V2> {
	@Override
	public boolean offerPreRun() {
		return false;
	}
	
	@Override
	public boolean offerPreReceiveLoop() {
		return false;
	}
	
	@Override
	public boolean offerPreReceive() {
		return false;
	}
	
	@Override
	public boolean offerPostReceive() {
		return false;
	}
	
	@Override
	public boolean offerPostReceiveLoop() {
		return false;
	}
	
	@Override
	public void preRun(Consumer<K, V> consumer) {
		
	}
	
	@Override
	public void preReceiveLoop(Consumer<K, V> consumer) {
		
	}
	
	@Override
	public void preReceive(Consumer<K, V> consumer, K key, V value) {
		
	}
	
	@Override
	public void postReceive(Consumer<K, V> consumer, K key, V value) {
		
	}
	
	@Override
	public void postReceiveLoop(Consumer<K, V> consumer) {
		
	}
	
	@Override
	public void preProduce(Producer<K2, V2> producer, K2 key, V2 value) {
		
	};
	
	@Override
	public void postProduce(Producer<K2, V2> producer, K2 key, V2 value) {
		
	};
	
	@Override
	public void messageAcknowleged(Producer<K2, V2> producer, K2 key, V2 value) {
		
	}
	
	@Override
	public boolean offerPreProduce() {
		return false;
	};
	
	@Override
	public boolean offerPostProduce() {
		return false;
	};
	
	@Override
	public boolean offerMessageAcknowleged() {
		return false;
	};
}
