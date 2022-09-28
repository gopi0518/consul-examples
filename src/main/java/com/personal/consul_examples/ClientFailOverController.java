package com.personal.consul_examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.health.DiscoveryClientHealthIndicator;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
public class ClientFailOverController {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Qualifier("primary")
    @Autowired
    
    private KafkaTemplate primaryKafkaTemplate;
    
    @Qualifier("secondary")
    @Autowired
    
    private KafkaTemplate secondaryKafkaTemplate;

    @GetMapping("/get")
    public String get() {
    	List<ServiceInstance> service1List = discoveryClient.getInstances("primarykafka");
        ServiceInstance service1 = null;
        SplunkEventGen SG = new SplunkEventGen();
        List<String> services = discoveryClient.getServices();
        System.out.println(services.get(0));
        
        for (ServiceInstance serviceInstance : service1List) {
            service1 = serviceInstance;
       
            System.out.println("Service output:"+service1.toString());
           
            break;
        }
        
        if(service1 == null || service1.toString().contains("status=CRITICAL")){
        	
           
           secondaryKafkaTemplate.send("acme.orders.secondary"
       			, "hello orders secondary");
       	
       	
       	System.out.println("Sending to secondary kafka");
       	for (long i = 0L; i < 1000; i++) {
    		long millis = System.currentTimeMillis();
    		String event= "%ASA-4-106023: Deny udp src inside:192.168.9.20/38524 dst outside:192.168.10.106/514 by access-group";
    		String source="udp:514";
    		String host="boundary-fw-1";
    		String sourceType="cisco:asa";
    		String index="main";
    		String k=sourceType+millis;
            SG.setTime(millis);
            SG.setEvent(event);
            SG.setSource(source);
            SG.setSourcetype(sourceType);
            SG.setHost(host);
            SG.setIndex(index);
            secondaryKafkaTemplate.send("splunk-s2s-events",k,SG);
    		
    	}
       	
       	for (long i = 0L; i < 10; i++) {
    		long millis = System.currentTimeMillis();
    		String event= "%ASA-2-106001: Inbound TCP connection denied from 34.215.24.225/9997 to 192.168.10.18/37477 flags RST  on interface outside";
    		String source="udp:514";
    		String host="boundary-fw-1";
    		String sourceType="cisco:asa";
    		String index="main";
    		String k=sourceType+millis;
            SG.setTime(millis);
            SG.setEvent(event);
            SG.setSource(source);
            SG.setSourcetype(sourceType);
            SG.setHost(host);
            SG.setIndex(index);
            secondaryKafkaTemplate.send("splunk-s2s-events",k,SG);
    		
    	}
       	
       	
           return "send to secondary";
        	
        }
        
        else {
        	
        	System.out.println("Service output:"+service1.toString());
            
          	 primaryKafkaTemplate.send("acme.orders.primary"
            			, "hello orders primary");
              return "send to primary";
            
        }
        
        
        
    }
    
    @GetMapping("/stop")
    public String restart() {
    	
    	System.out.println();
    	return "stoped";
    	
    }
    @GetMapping("/publish")
    public String pub() {
    	
    	System.out.println();
    	return "stoped";
    	
    }
}