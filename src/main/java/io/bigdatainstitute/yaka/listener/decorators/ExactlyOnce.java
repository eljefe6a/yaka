package io.bigdatainstitute.yaka.listener.decorators;

import java.util.Properties;

import io.bigdatainstitute.yaka.listener.ListenerDecorator;

public class ExactlyOnce<K, V> extends ListenerDecorator<K, V> {
	@Override
	public void init(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close() {
		
	}
	
}
