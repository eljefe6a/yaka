package io.bigdatainstitute.yaka.listener;

import java.util.ArrayList;

public abstract class Consumer implements AutoCloseable {
	public ArrayList<DataListener> listeners;

	public ArrayList<ListenerDecorator> preReceiveLoopListeners;
	public ArrayList<ListenerDecorator> preReceiveListeners;
	public ArrayList<ListenerDecorator> postReceiveListeners;
	public ArrayList<ListenerDecorator> postReceiveLoopListeners;

	public String brokers;
	public String topic;
	public String consumerGroupName;

	ListenerDecorators[] decorators;

	public Consumer(String brokers, String topic, String consumerGroupName, ListenerDecorators... decorators) {
		this.brokers = brokers;
		this.topic = topic;
		this.consumerGroupName = consumerGroupName;

		this.decorators = decorators;
	}

	public abstract void init();

	public void addListener(DataListener listener) {
		listeners.add(listener);
	}

	public abstract void close();

	public void registerDecorators() {
		for (ListenerDecorators decorator : decorators) {
			decorator.getListenerDecorator().init();
		}

		for (ListenerDecorators decorator : decorators) {
			if (decorator.getListenerDecorator().offerPreReceiveLoop()) {
				preReceiveLoopListeners.add(decorator.getListenerDecorator());
			}
		}

		for (ListenerDecorators decorator : decorators) {
			if (decorator.getListenerDecorator().offerPreReceive()) {
				preReceiveListeners.add(decorator.getListenerDecorator());
			}
		}

		for (ListenerDecorators decorator : decorators) {
			if (decorator.getListenerDecorator().offerPostReceiveLoop()) {
				postReceiveListeners.add(decorator.getListenerDecorator());
			}
		}

		for (ListenerDecorators decorator : decorators) {
			if (decorator.getListenerDecorator().offerPostReceiveLoop()) {
				postReceiveLoopListeners.add(decorator.getListenerDecorator());
			}
		}
	}

	public void preReceiveLoop() {
		for (ListenerDecorator decorator : preReceiveLoopListeners) {
			decorator.preReceiveLoop();
		}
	}

	public void preReceive() {
		for (ListenerDecorator decorator : preReceiveListeners) {
			decorator.preReceive();
		}
	}

	public void postReceive() {
		for (ListenerDecorator decorator : postReceiveListeners) {
			decorator.postReceive();
		}
	}

	public void postReceiveLoop() {
		for (ListenerDecorator decorator : postReceiveLoopListeners) {
			decorator.postReceiveLoop();
		}
	}
}
