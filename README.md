# YAKA (Yet Another Kafka API)

Why would would you want a new/different Kafka API? A few reasons:

* Would not be directly coupled to Kafka. You shouldn't write code directly coupled to Kafka's API.
* Would not make you think about threading. The `KafkaConsumer` is not thread safe and you have to write your own. This opens code up to multi-threading bugs.
* Would not make you think about code, configs, and properties. Often Kafka needs a property change and code changes. This is unintuitive to most programmers.
* Wouldn't force flatMaps and functional programming (but you can still use it)
* Without limitations of Kafka Streams. Kafka Streams is always from Kafka and to Kafka. Yaka allows you get from other systems or output to other systems.
* Would improve the typing system. It's possible to have your types and serializer/deserializer be different.
* Would make it easier to add pre- and post- processing of message for encryption/decryption or adding metrics.
* Kafka API often forces you to mix property configurations and API calls. This makes it so that you can make API calls without have done the requisite property configuration. 

## API Concepts

### Listeners (Consumers)

Instead of direct consumption, YAKA uses listeners. These listeners are notified whenever a new message arrives. The
listener doesn't have to create its own thread.

```java
// Simple consumption. This includes automatically figuring out types and making the consumption exactly once
try (Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, topic, consumerGroup, String.class,
		String.class, new ListenerAutoType<>(), new ExactlyOnce<>());) {
	consumer.addListener(new DataListener<String, String>() {
		@Override
		public void dataReceived(String key, String value) {
			// Do something
		}
	});
}

// The same thing, but using lambdas
try (Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, topic, consumerGroup, String.class,
		String.class, new ListenerAutoType<>(), new ExactlyOnce<>());) {
	consumer.addListener((String key, String value) -> key.length() /* Do something with key/value */);
}
```

### Producers

```java
// Create a producer. Automatically figures out the types and makes the configuration changes to be highly durable.
try (Producer<String, String> producer = new KafkaProducerImpl<>(brokers, topic, String.class, String.class,
		new ProducerAutoType<>(), new HighDurable<>());) {
	producer.produce("key", "value");
} catch (Exception e) {
	logger.error("Error producing", e);
}
```

### Listeners and Producers

A very common pattern is to listen on a topic, do something, and then produce a new message. YAKA makes this easier
and more straightforward.

```java
// Create a listener and a producer. Then listen and produce data.
Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, inputTopic, consumerGroup, String.class,
		String.class, new ListenerAutoType<>(), new ExactlyOnce<>());
Producer<String, String> producer = new KafkaProducerImpl<String, String>(brokers, outputTopic, String.class,
		String.class, new ProducerAutoType<>(), new HighDurable<String, String>());

try (ListenerProducer<String, String, String, String> listenerProducer = new ListenerProducer<>(consumer,
		producer)) {
	consumer.addListener(new DataListener<String, String>() {

		@Override
		public void dataReceived(String key, String value) {
			producer.produce(key, value);

		}
	});
} catch (Exception e) {
	logger.error("Error consuming and producing", e);
}

// The same thing with lambdas		
Consumer<String, String> consumer = new KafkaConsumerImpl<>(brokers, inputTopic, consumerGroup, String.class,
		String.class, new ListenerAutoType<>(), new ExactlyOnce<>());
Producer<String, String> producer = new KafkaProducerImpl<String, String>(brokers, outputTopic, String.class,
		String.class, new ProducerAutoType<>(), new HighDurable<String, String>());

try (ListenerProducer<String, String, String, String> listenerProducer = new ListenerProducer<>(consumer,
		producer)) {
	listenerProducer.addListener((String key, String value,
			ListenerProducerContext<String, String> context) -> context.send(key, value));
} catch (Exception e) {
	logger.error("Error consuming and producing", e);
}
```

## Decorators

Most of the configuration of consumers and producers is done by decorators. These decorators make property changes
to the producer or consumer. They also register for events. These events allow a decorator to make changes or
call API functions on the producer or consumer.
