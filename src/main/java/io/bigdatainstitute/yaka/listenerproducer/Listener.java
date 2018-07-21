package io.bigdatainstitute.yaka.listenerproducer;

public interface Listener<CK, CV, PK, PV> {
	public void dataReceived(CK key, CV value, ListenerProducerContext<PK, PV> context);
}
