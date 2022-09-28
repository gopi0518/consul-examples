package com.personal.consul_examples;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {


    @Bean
    public ProducerFactory<String, String> primaryProducerFactory() {

        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "pkc-2396y.us-east-1.aws.confluent.cloud:9092");
        configProps.put("security.protocol","SASL_SSL");
        configProps.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='5JW4B4S7CPI65WMI' password='fLcw0MM8I/gp4e8hboYDZy3mwWeNZyiawOda7ek3Dr3OZT5j3m17AZbzLWKb8wUs';");
        configProps.put("sasl.mechanism","PLAIN");
        configProps.put("client.dns.lookup","use_all_dns_ips");
        configProps.put("session.timeout.ms",45000);
        configProps.put("retries",10);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        /*configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);*/
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public ProducerFactory<String, Object> localProducerFactory() {

        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "ec2-18-220-65-34.us-east-2.compute.amazonaws.com:9092");
		/*
		 * configProps.put("security.protocol","SASL_SSL"); configProps.put(
		 * "sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='5JW4B4S7CPI65WMI' password='fLcw0MM8I/gp4e8hboYDZy3mwWeNZyiawOda7ek3Dr3OZT5j3m17AZbzLWKb8wUs';"
		 * ); configProps.put("sasl.mechanism","PLAIN");
		 */
        configProps.put("client.dns.lookup","use_all_dns_ips");
        configProps.put("session.timeout.ms",45000);
        configProps.put("retries",10);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        /*configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);*/
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public ProducerFactory<String, Object> secondaryProducerFactory() {
    	IPLookupModel im;

        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "pkc-568p2.us-east-2.aws.confluent.cloud:9092");
        configProps.put("security.protocol","SASL_SSL");
        configProps.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='TBTY7Z4GJVITBKLL' password='yLQViReTdUJ692LX7JYx2jqTY6bAvQacQ2tTo8JR4sxx+Db737THfsNok7Ko+8X7';");
        configProps.put("sasl.mechanism","PLAIN");
        configProps.put("client.dns.lookup","use_all_dns_ips");
        configProps.put("session.timeout.ms",45000);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        /*configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);*/
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "primary")
    public KafkaTemplate<String, String> primaryKafkaTemplate() {
        return new KafkaTemplate<>(primaryProducerFactory());
    }

    @Bean(name = "secondary")
    public KafkaTemplate<String, Object> secondaryKafkaTemplate() {
        return new KafkaTemplate<String, Object>(secondaryProducerFactory());
    }
    @Bean(name = "local")
    public KafkaTemplate<String, Object> localKafkaTemplate() {
        return new KafkaTemplate<String, Object>(localProducerFactory());
    }
}
