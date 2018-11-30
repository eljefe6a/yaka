package io.bigdatainstitute.yaka.listener;

import java.util.Properties;

public interface ListenerDecorator<K, V> {
	public boolean offerPreReceiveLoop();
	
	public boolean offerPreReceive();
	
	public boolean offerPostReceive();
	
	public boolean offerPostReceiveLoop();
	
	public void preReceiveLoop(Consumer<K, V> consumer);
	
	public void preReceive(Consumer<K, V> consumer, K key, V value);
	
	public void postReceive(Consumer<K, V> consumer, K key, V value);
	
	public void postReceiveLoop(Consumer<K, V> consumer);
	
	public abstract void initListener(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public abstract void closeListener();
}
