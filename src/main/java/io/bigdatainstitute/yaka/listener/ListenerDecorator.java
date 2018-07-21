package io.bigdatainstitute.yaka.listener;

public abstract class ListenerDecorator {
	public boolean offerPreReceiveLoop() {
		return false;
	}
	
	public boolean offerPreReceive() {
		return false;
	}
	
	public boolean offerPostReceive() {
		return false;
	}
	
	public boolean offerPostReceiveLoop() {
		return false;
	}
	
	public void preReceiveLoop() {
		
	}
	
	public void preReceive() {
		
	}
	
	public void postReceive() {
		
	}
	
	public void postReceiveLoop() {
		
	}
	
	public abstract void init();
	
	public abstract void close();
}
