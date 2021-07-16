package com.tweetapp.main.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TweetAppKafkaProducer {

	@Value("${application.tweet.kafka.topic}")
	private String topic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void publish(String message) {
		kafkaTemplate.send(topic, message);
	}
}