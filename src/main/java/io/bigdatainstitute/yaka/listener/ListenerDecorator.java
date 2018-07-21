package io.bigdatainstitute.yaka.listener;

import java.util.Properties;

public abstract class ListenerDecorator<K, V> {
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
	
	public abstract void init(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public abstract void close();
}
