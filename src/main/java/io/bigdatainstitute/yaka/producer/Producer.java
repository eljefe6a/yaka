package io.bigdatainstitute.yaka.producer;

import java.util.ArrayList;

public abstract class Producer implements AutoCloseable {
	public ArrayList<ProducerDecorator> preProduceListeners;
	public ArrayList<ProducerDecorator> postProduceListeners;
	public ArrayList<ProducerDecorator> finishProduceListeners;
	
	public String brokers;
	public String topic;

	ProducerDecorators[] decorators;

	public Producer(String brokers, String topic, ProducerDecorators... decorators) {
		this.brokers = brokers;
		this.topic = topic;

		this.decorators = decorators;
	}
	
	public abstract void init();
	
	public abstract void produce(Object key, Object value);

	/**
	 * Optional. Do things like flush or commit a transaction
	 */
	public abstract void finish();
	
	public boolean offerPreProduce() {
		return false;
	};
	
	public boolean offerPostProduce() {
		return false;
	};
	
	public boolean offerFinishProduce() {
		return false;
	};
	
	public void registerDecorators() {
		for (ProducerDecorators decorator : decorators) {
			decorator.getDecorator().init();
		}

		for (ProducerDecorators decorator : decorators) {
			if (decorator.getDecorator().offerPreProduce()) {
				preProduceListeners.add(decorator.getDecorator());
			}
		}

		for (ProducerDecorators decorator : decorators) {
			if (decorator.getDecorator().offerPostProduce()) {
				postProduceListeners.add(decorator.getDecorator());
			}
		}

		for (ProducerDecorators decorator : decorators) {
			if (decorator.getDecorator().offerFinishProduce()) {
				finishProduceListeners.add(decorator.getDecorator());
			}
		}
	}
}
