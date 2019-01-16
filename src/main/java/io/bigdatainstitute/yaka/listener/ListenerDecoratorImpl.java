package io.bigdatainstitute.yaka.listener;

public abstract class ListenerDecoratorImpl<K, V> implements ListenerDecorator<K, V> {
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
}
