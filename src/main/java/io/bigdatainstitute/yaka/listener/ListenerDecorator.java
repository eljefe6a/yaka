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
	
	public void preReceiveLoop() {
		
	}
	
	public void preReceive() {
		
	}
	
	public void postReceive(Consumer<K, V> consumer) {
		
	}
	
	public void postReceiveLoop() {
		
	}
	
	public abstract void init(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public abstract void close();
}
