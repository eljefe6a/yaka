# YAKA (Yet Another Kafka API)

Why would would you want a new/different Kafka API? A few reasons:

* Would not be directly coupled to Kafka. You shouldn't write code directly coupled to Kafka's API.
* Would not make you think about threading. The `KafkaConsumer` is not thread safe and you have to write your own. This opens code up to multi-threading bugs.
* Would not make you think about code, configs, and properties. Often Kafka needs a property change and code changes. This is unintuitive to most programmers.
* Wouldn't force flatMaps and functional programming (but still could use it)
* Without limitations of Kafka streams. Kafka Streams is always from Kafka and to Kafka.
* Would improve the typing system. It's possible to have your types and serializer/deserializer be different.
* Would make it easier to add pre- and post- processing of message for encryption/decryption or adding metrics.

## API Concepts

```java
// Simple consumption would look like
void receive(Context context) {
	context.key()
}

// Initializing would look like
new SimpleKafka(this, "broker", "topic", COMMIT_AFTER ... vargs);

void receive(Context context) {
	context.key();
}

// Consumer producer would look like
void receive(Context context) {
	// With the right type a commit happens for you
	context.key();
	context.send(topic, key, value);

        // Commit happens automatically after this send
}
```

### Consumer Types

```java
// These classes register themselves at certain events. They will add functionality without having to add code and properties.
enum CONSUMER_TYPES {
	COMMIT_AFTER(includes a class that commits after)
  IoT(class that sets things to be as fast a possible)
  AUTO_RETRY (Have a type that puts all sends into a database. If things don't ack, it will automatically resend and database will maintain order)
}
```

TODO: This is very consumer-focused. How should producers have types?

Each consumer type registers for specific events. Pre-start of receive loop, pre-send to receiver, post-send to receiver, post-acknowledgement of message (producer), post-receive loop. Also need to pass in properties when configuring.

