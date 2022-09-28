package com.personal.consul_examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

public class ProducerPrimaryExample {

@Autowired
@Qualifier("primary")
private KafkaTemplate primaryKafkaTemplate;



public void send() {
    
    primaryKafkaTemplate.send("TestCloudEra","hey there.. cloudera");
}

}