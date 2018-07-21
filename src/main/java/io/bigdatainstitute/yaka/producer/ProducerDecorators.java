package io.bigdatainstitute.yaka.producer;

import io.bigdatainstitute.yaka.producer.decorators.HighDurable;

public enum ProducerDecorators {
	HIGH_DURABLE(new HighDurable());

	ProducerDecorator decorator;
	
	private ProducerDecorators(ProducerDecorator decorator) {
		this.decorator = decorator;	
	}

	public ProducerDecorator getDecorator() {
		return decorator;
	}
}
