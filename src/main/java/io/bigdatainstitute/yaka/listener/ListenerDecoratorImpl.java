package io.bigdatainstitute.yaka.listener;

public abstract class ListenerDecoratorImpl<K, V> implements ListenerDecorator<K, V> {
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
}
