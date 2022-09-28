package com.personal.consul_examples;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

public class ProducerSecondaryExample {




@Autowired
@Qualifier("secondary")
private KafkaTemplate secondaryKafkaTemplate;

public void send() {
    secondaryKafkaTemplate.send("TestCloudEra","hey there.. cloudera");
    
}

}
