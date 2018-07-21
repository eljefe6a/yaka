package io.bigdatainstitute.yaka.listenerproducer;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.producer.Producer;

public class ListenerProducer<CK, CV, PK, PV> implements AutoCloseable {
	Consumer<CK, CV> consumer;
	Producer<PK, PV> producer;

	public ListenerProducer(Consumer<CK, CV> consumer, Producer<PK, PV> producer) {
		this.consumer = consumer;
		this.producer = producer;
	}

	public void addListener(Listener<CK, CV, PK, PV> listener) {
		consumer.addListener(new DataListener<CK, CV>() {

			@Override
			public void dataReceived(CK key, CV value) {
				listener.dataReceived(key, value, new ListenerProducerContext<PK, PV>(producer));
			}
		});
	}

	@Override
	public void close() throws Exception {
		consumer.close();
		producer.close();
	}
}
