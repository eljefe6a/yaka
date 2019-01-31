package io.bigdatainstitute.yaka.producer.testproducerimpl;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecoratorImpl;

public class TestProducerImpl<K, V> extends Producer<K, V> {
	Logger logger = Logger.getLogger(TestProducerImpl.class);

	/** The list of key/values produced in the order they were produced */
	LinkedList<AbstractMap.SimpleEntry<K, V>> producedKeyValues;

	@SafeVarargs
	public TestProducerImpl(String topic, Class<K> keyClass, Class<V> valueClass,
			ProducerDecoratorImpl<K, V>... decorators) {
		super("", topic, keyClass, valueClass, decorators);
	}

	@Override
	public void produce(K key, V value) {
		preProduce(this, key, value);

		producedKeyValues.add(new AbstractMap.SimpleEntry<K, V>(key, value));

		if (hasMessageAcknowledgedListeners == true) {
			messageAcknowledged(this, key, value);
		}

		postProduce(this, key, value);

	}

	@Override
	public void close() throws Exception {

	}

	@Override
	public void init() {
		producedKeyValues = new LinkedList<AbstractMap.SimpleEntry<K, V>>();
	}

	/**
	 * Gets the key at the index
	 * 
	 * @param index
	 *            The index to retrieve
	 * @return The key at the index
	 */
	public K getKey(int index) {
		return producedKeyValues.get(index).getKey();
	}

	/**
	 * Gets the value at the index
	 * 
	 * @param index
	 *            The index to retrieve
	 * @return The value at the index
	 */
	public V getValue(int index) {
		return producedKeyValues.get(index).getValue();
	}

	/**
	 * Gets the total number of messages produced
	 * 
	 * @return The total number of messages produced
	 */
	public int getSize() {
		return producedKeyValues.size();
	}

	/**
	 * Clears all previously produced messages
	 */
	public void clear() {
		producedKeyValues.clear();
	}

	/**
	 * Gets all produced values as a list
	 * 
	 * @return All produced values as a list
	 */
	public ArrayList<V> valuesAsList() {
		ArrayList<V> list = new ArrayList<>();

		for (SimpleEntry<K, V> simpleEntry : producedKeyValues) {
			list.add(simpleEntry.getValue());
		}

		return list;
	}
	
	/**
	 * Gets all produced keys as a list
	 * 
	 * @return All produced keys as a list
	 */
	public ArrayList<K> keysAsList() {
		ArrayList<K> list = new ArrayList<>();

		for (SimpleEntry<K, V> simpleEntry : producedKeyValues) {
			list.add(simpleEntry.getKey());
		}

		return list;
	}
}