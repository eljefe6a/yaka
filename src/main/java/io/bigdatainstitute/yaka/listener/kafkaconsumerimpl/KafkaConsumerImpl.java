package io.bigdatainstitute.yaka.listener.kafkaconsumerimpl;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.DataListener;
import io.bigdatainstitute.yaka.listener.ListenerDecorators;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class KafkaConsumerImpl extends Consumer {
	Logger logger = Logger.getLogger(KafkaConsumerImpl.class);
	
	Properties props = new Properties();
	
    public KafkaConsumerImpl(String brokers, String topic, String consumerGroupName, ListenerDecorators...decorators) {
		super(brokers, topic, consumerGroupName, decorators);
	}

	@Override
	public void init() {
        // Configure initial location bootstrap servers
        props.put(BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
        // Configure consumer group
        props.put(GROUP_ID_CONFIG, "avrocon");
        // Start at the beginning of the topic
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Configure to use Confluent's Avro deserializer
        props.put(KEY_DESERIALIZER_CLASS_CONFIG,
                  KafkaAvroDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG,
                  KafkaAvroDeserializer.class.getName());
        
        registerDecorators();

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(DataListener listener) {
		super.addListener(listener);
		
		// TODO: Handle multiple listeners
		
		// TOOD: Add threading
		

        // Create the consumer and subscribe to the topic
        try (KafkaConsumer<Object, Object> consumer = new
            KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
            	preReceiveLoop();
            	
                ConsumerRecords<Object, Object> records = consumer.poll(100);

                for (ConsumerRecord<Object, Object> record : records) {
                    for (DataListener currentListener : listeners) {
                    	preReceive();
                    	
                    	currentListener.dataReceived(record.key(), record.value());
                    	
                    	postReceive();
                    }
                }
                
                postReceiveLoop();
            }

            // Consumer will automatically be closed
        } catch (Exception e) {
            // This handling could cause a cascading failure
            logger.error("There was an error while consuming", e);
        }
	}
}