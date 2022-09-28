package com.personal.consul_examples;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
@Configuration

public class KafkaConsumerConfig {

	 @Bean
	    public ConsumerFactory<String, String> consumerFactory() {
	        Map<String, Object> props = new HashMap<>();
	        props.put(
	          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"abc.us-east-2.aws.confluent.cloud:9092");
	        props.put(
	          ConsumerConfig.GROUP_ID_CONFIG, 
	          "test");
	        props.put("security.protocol","SASL_SSL");
	        props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='abc' password='abc';");
	        props.put("sasl.mechanism","PLAIN");
	        props.put("client.dns.lookup","use_all_dns_ips");
	        props.put("session.timeout.ms",45000);
	        props.put(
	                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	                StringSerializer.class);
	        props.put(
	                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	                StringSerializer.class);
	        props.put(
	          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
	          StringDeserializer.class);
	        props.put(
	          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
	          StringDeserializer.class);
	        return new DefaultKafkaConsumerFactory<>(props);
	    }

	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, String> 
	      kafkaListenerContainerFactory() {
	   
	        ConcurrentKafkaListenerContainerFactory<String, String> factory =
	          new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory());
	        return factory;
	    }
	
}
