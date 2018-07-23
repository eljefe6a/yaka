package io.bigdatainstitute.yaka.listener;

public interface DataListener<K, V> {
	public void dataReceived(K key, V value);
}
