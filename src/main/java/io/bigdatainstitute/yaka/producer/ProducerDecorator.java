package io.bigdatainstitute.yaka.producer;

import java.util.Properties;

public interface ProducerDecorator<K, V> {
	public abstract void initProducer(Properties producerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public void preProduce(Producer<K, V> producer, K key, V value);
	
	public void postProduce(Producer<K, V> producer, K key, V value);
	
	public void messageAcknowleged(Producer<K, V> producer, K key, V value);
	
	public abstract void closeProducer();
	
	public boolean offerPreProduce();
	
	public boolean offerPostProduce();
	
	public boolean offerMessageAcknowleged();
}
