package io.bigdatainstitute.yaka.producer;

import java.util.Properties;

public abstract class ProducerDecorator<K, V> {
	public abstract void init(Properties producerProperties, Class<K> keyClass, Class<V> valueClass);
	
	public void preProduce() {
		
	};
	
	public void postProduce() {
		
	};
	
	public void finishProduce() {
		
	};
	
	public void messageAcknowleged() {
		
	}
	
	public abstract void close();
	
	public boolean offerPreProduce() {
		return false;
	};
	
	public boolean offerPostProduce() {
		return false;
	};
	
	public boolean offerFinishProduce() {
		return false;
	};
	
	public boolean offerMessageAcknowleged() {
		return false;
	};
}
