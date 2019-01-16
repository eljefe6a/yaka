package io.bigdatainstitute.yaka.listener.decorators;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.bigdatainstitute.yaka.listener.Consumer;
import io.bigdatainstitute.yaka.listener.ListenerDecoratorImpl;
import io.bigdatainstitute.yaka.listener.kafkaconsumerimpl.KafkaConsumerImpl;

public class KafkaStartingOffsets<K, V> extends ListenerDecoratorImpl<K, V> {
	private static final Logger logger = LogManager.getLogger("KafkaStartingOffsets");
	
	/** Option for using last committed offset */
	public static final String LASTCOMMITTED = "lastcommitted";

	/** Option for starting at beginning offset */
	public static final String BEGINNING = "beginning";

	/** Option for starting at ending offset */
	public static final String END = "end";

	String offsetValue;
	
	public KafkaStartingOffsets(String offsetValue) {
		this.offsetValue = offsetValue;
	}
	
	@Override
	public void preRun(Consumer<K, V> consumer) {
		if (consumer instanceof KafkaConsumerImpl) {
			KafkaConsumer<K, V> kafkaConsumer = ((KafkaConsumerImpl<K, V>) consumer).getKafkaConsumer();
			
			if (offsetValue.equals(LASTCOMMITTED)) {
				logger.info("Starting where ever the consumer last committed.");
			} else if (offsetValue.equals(BEGINNING)) {
				// Seek to beginning of all assignments
				kafkaConsumer.poll(0);
				kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());

				logger.info("Seeking all to beginning.");
			} else if (offsetValue.equals(END)) {
				// Send to end of all assignments
				kafkaConsumer.poll(0);
				kafkaConsumer.seekToEnd(kafkaConsumer.assignment());

				logger.info("Seeking all to end.");
			} else {
				// Its the comma separated list, parse and assign
				setManualOffsets(offsetValue, kafkaConsumer);
			}

			// Output info about offsets
			Set<TopicPartition> partitions = kafkaConsumer.assignment();

			for (TopicPartition partition : partitions) {
				Long actualEndOffset = kafkaConsumer.endOffsets(partitions).get(partition);
				long actualPosition = kafkaConsumer.position(partition);
				logger.info("Starting offsets for partition:" + partition.partition() + " Current Offset:" + actualPosition
						+ " Current End:" + actualEndOffset + " Difference: " + (actualEndOffset - actualPosition));
			}
		} else {
			logger.warn("This decorator can only be used with Kafka. No settings were changed.");
		}
	}

	/**
	 * Processes the manually set offsets for a partitions. The list should be
	 * partitionnum=offset with a comma separated list. E.g. 0=1234,1=4321
	 * 
	 * @param offsetOptionValue
	 *            The values for the offsets specified in the command line
	 * @param consumer
	 *            The Kafka consumer
	 */
	private void setManualOffsets(String offsetOptionValue, KafkaConsumer<?, ?> consumer) {
		logger.info("Setting offsets to manually selected. Offsets are \"" + offsetOptionValue + "\".");

		// Get assignments
		consumer.poll(0);

		// Put assignments in a map for easy retrieval
		HashMap<Integer, TopicPartition> partitionNumToTopicPartition = new HashMap<>();

		for (TopicPartition partition : consumer.assignment()) {
			partitionNumToTopicPartition.put(partition.partition(), partition);
		}

		// Start parsing the input
		String[] offsetGroups = offsetOptionValue.split(",");

		Pattern p = Pattern.compile("(\\d+)=(\\d+)");

		for (String offsetGroup : offsetGroups) {
			Matcher m = p.matcher(offsetGroup);

			// Verify user input matches regex
			if (m.matches()) {
				// Matches. Now seek to offset.
				Integer partitionNumber = Integer.valueOf(m.group(1));
				Integer partitionOffsetNumber = Integer.valueOf(m.group(2));

				if (partitionNumToTopicPartition.get(partitionNumber) != null) {
					// Programatically seek to place
					consumer.seek(partitionNumToTopicPartition.get(partitionNumber), partitionOffsetNumber);

					logger.info("Set partition " + partitionNumber + " to offset " + partitionOffsetNumber);
				} else {
					logger.info("Partition number " + partitionNumber
							+ " wasn't found in map. May not be assigned or is an incorrect partition number. Offset was "
							+ partitionOffsetNumber + "\".");
				}
			} else {
				// Didn't match the expected regex. Log this as being skipped
				logger.info("Offset group did not match format. Offset should partitionnum=offset. Offset group was \""
						+ offsetGroup + "\".");
			}
		}
	}
	
	@Override
	public void initListener(Properties consumerProperties, Class<K> keyClass, Class<V> valueClass) {
			
	}

	@Override
	public void closeListener() {
		
	}

	@Override
	public boolean offerPreRun() {
		return true;
	}
}
