package com.personal.consul_examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.json.*;
@Service

public class KafkaConsumerExample {
	@Qualifier("secondary")
    @Autowired
    private KafkaTemplate<String, Object> secondaryKafkaTemplate;
	private String[] topics;
	RestTemplate restTemplate = new RestTemplate();
	String ip;
	String query;
	int fraud_score=0;
	IPLookupModel im = new IPLookupModel();
	SplunkEventGen SG = new SplunkEventGen();
	DNSRaw dr= new DNSRaw();
	
	@KafkaListener(topics  = {"dns"}, groupId = "dnslookup1", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupFoo(String message) {
	    
	    //JSONArray array = new JSONArray(message);
	    JSONObject obj = new JSONObject(message);
	    long millis = System.currentTimeMillis();
	    ip=obj.getString("id.orig_h");
	    query=obj.getString("query");
	    dr.setAA(obj.getBoolean("AA"));
	    dr.setDnsquery(query);
	    dr.setResp_host(obj.getString("id.resp_h"));
	    dr.setIp(ip);
	    dr.setTs(millis);
	    System.out.println("IP"+ ip);
	    secondaryKafkaTemplate.send("dnsraw"
       			, ip,dr);
	    
		
		  String response=getPostsPlainJSON(); JSONObject resobj = new
		  JSONObject(response);
		  
		  if(resobj.getBoolean("success")) {
		  System.out.println(resobj.getBoolean("success"));
		  fraud_score=resobj.getInt("fraud_score"); }
		  System.out.println("fraud_score"+fraud_score);
		  System.out.println(resobj.getBoolean("success"));
		  im.setFraud_score(fraud_score); im.setIp(ip); im.setDnsquery(query);
		  
		  
		  secondaryKafkaTemplate.send("ipscore-detection" , ip,im);
		 
	    
	}
	
	public String getPostsPlainJSON() {
        String url = "https://ipqualityscore.com/api/json/ip/ceBdc2LEyNh5vxM2z8J2jjii8xyX6Q44/"+ip;
        return this.restTemplate.getForObject(url, String.class);
    }
	public String sendSplunkEvents() {
		for (long i = 0L; i < 100; i++) {
		long millis = System.currentTimeMillis();
		String event= "%ASA-4-106023: Deny udp src inside:192.168.9.20/38524 dst outside:192.168.10.106/514 by access-group";
		String source="udp:514";
		String host="boundary-fw-1";
		String sourceType="cisco:asa";
		String k=sourceType+millis;
        SG.setTime(millis);
        SG.setEvent(event);
        SG.setSource(source);
        SG.setSourcetype(sourceType);
        SG.setHost(host);
        secondaryKafkaTemplate.send("splunk-s2s-events",k,SG);
		
	}
		
		return "hello";
}
}
