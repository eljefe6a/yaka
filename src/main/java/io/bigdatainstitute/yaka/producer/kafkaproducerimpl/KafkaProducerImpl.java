package io.bigdatainstitute.yaka.producer.kafkaproducerimpl;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.producer.Producer;
import io.bigdatainstitute.yaka.producer.ProducerDecorators;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaProducerImpl extends Producer {
	Logger logger = Logger.getLogger(KafkaProducerImpl.class);

	KafkaProducer<Object, Object> producer;

	public KafkaProducerImpl(String brokers, String topic, ProducerDecorators... decorators) {
		super(brokers, topic, decorators);
	}

	@Override
	public void produce(Object key, Object value) {
		producer.send(new ProducerRecord<Object, Object>(topic, key, value));
	}

	@Override
	public void finish() {

	}

	@Override
	public void close() throws Exception {
		producer.close();
	}

	@Override
	public void init() {
		Properties propsProd = new Properties();
		propsProd.put(BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
		// Configure Confluent's Avro serializer
		propsProd.put(KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		propsProd.put(VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

		producer = new KafkaProducer<>(propsProd);
	}
}