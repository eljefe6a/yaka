package io.bigdatainstitute.yaka.producer.testproducerimpl;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecoratorImpl;

public class TestProducerImpl<K, V> extends Producer<K, V> {
	Logger logger = Logger.getLogger(TestProducerImpl.class);

	LinkedMap<K, V> producedKeyValues;
	
	@SafeVarargs
	public TestProducerImpl(String topic, Class<K> keyClass, Class<V> valueClass,
			ProducerDecoratorImpl<K, V>... decorators) {
		super("", topic, keyClass, valueClass, decorators);
	}

	@Override
	public void produce(K key, V value) {
		preProduce(this, key, value);

		producedKeyValues.put(key, value);
		
		if (hasMessageAcknowledgedListeners == true) {
			producedKeyValues.put(key, value);
		}

		postProduce(this, key, value);

	}

	@Override
	public void close() throws Exception {
		
	}

	@Override
	public void init() {
		producedKeyValues  = new LinkedMap<>();
	}
	
	public K getKey(int index) {
		return producedKeyValues.get(index);
	}
	
	public V getValue(int index) {
		return producedKeyValues.getValue(index);
	}
}