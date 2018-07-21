package io.bigdatainstitute.yaka.listener;

public abstract class DataListener {
	public abstract void dataReceived(Object key, Object value);
}
