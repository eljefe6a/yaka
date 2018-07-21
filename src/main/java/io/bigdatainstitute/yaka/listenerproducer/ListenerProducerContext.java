package io.bigdatainstitute.yaka.listenerproducer;

import io.bigdatainstitute.yaka.producer.Producer;

public class ListenerProducerContext<K, V> {
	Producer<K, V> producer;
	
	public ListenerProducerContext(Producer<K, V> producer) {
		this.producer = producer;
	}
	
	public void send(K key, V value) {
		producer.produce(key, value);
	}
}
