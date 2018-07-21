package io.bigdatainstitute.yaka.producer;

public abstract class ProducerDecorator {
	public abstract void init();
	
	public void preProduce() {
		
	};
	
	public void postProduce() {
		
	};
	
	public void finishProduce() {
		
	};
	
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
}
