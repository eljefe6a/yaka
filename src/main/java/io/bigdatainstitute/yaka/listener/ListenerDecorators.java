package io.bigdatainstitute.yaka.listener;

import io.bigdatainstitute.yaka.listener.decorators.ExactlyOnce;

public enum ListenerDecorators {
	EXACTLY_ONCE(new ExactlyOnce());
	
	ListenerDecorator decorator;
	
	private ListenerDecorators(ListenerDecorator decorator) {
		this.decorator = decorator;
	}
	
	public ListenerDecorator getListenerDecorator() {
		return decorator;
	}
}
