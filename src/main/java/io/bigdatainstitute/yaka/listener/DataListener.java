package io.bigdatainstitute.yaka.listener;

public abstract class DataListener<K, V> {
	public abstract void dataReceived(K key, V value);
}
