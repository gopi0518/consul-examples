package com.personal.consul_examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	@Autowired
	private DiscoveryClient discoveryClient;
	TreeMap<Date, HealthCheck> logHC = new TreeMap<Date, HealthCheck>();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Scheduled(fixedRate = 100000)
	public void cronJobSch() {
		//HealthCheck HC = new HealthCheck();
		List<ServiceInstance> service1List = discoveryClient.getInstances("primarykafka");
		ServiceInstance service1 = null;
		HealthCheck hc = new HealthCheck();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<HealthCheck> entity = new HttpEntity<>(hc, headers);
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplateBuilder builder = new RestTemplateBuilder();
		System.out.println("scheduler is working");
		Date now = new Date();
		
		for (ServiceInstance serviceInstance : service1List) {
			service1 = serviceInstance;

			System.out.println("Service output:"+service1.toString());

			break;
		}

		if(service1 == null || service1.toString().contains("status=CRITICAL")) {
			
			System.out.println("Healthcheck success");

			if(logHC.size()==0) {
				System.out.println("JVM started");
				logHC.put(now, hc);
				hc.setActive_cluster("Secondary");
				hc.setIs_restarted(false);
				hc.setRestart_time(now);
				/*String response = builder.build()
						.postForObject("http://localhost:8081/kafka/healthcheck/notify", entity, String.class);
				//System.out.println(response);*/
				//logHC.put(now, hc);
			}
			else {

			if(logHC.lastEntry().getValue().active_cluster!="Secondary") {
				System.out.println("Status change to Secondary");
				hc.setActive_cluster("Secondary");
				hc.setIs_restarted(true);
				hc.setRestart_time(now);

				
				
				String response = builder.build()
						.postForObject("http://localhost:8081/kafka/healthcheck/notify", entity, String.class);
				
				System.out.println(response);
			}
			else {
				System.out.println("Status change to Secondary,no restart");
				hc.setActive_cluster("Secondary");
				hc.setIs_restarted(false);
				hc.setRestart_time(logHC.lastEntry().getValue().getRestart_time());
			}
			logHC.put(now, hc);
			}
			
		}
		else {
			//System.out.println(logHC.lastEntry().getKey()+"->"+logHC.lastEntry().getValue());
			if(logHC.size()==0) {
				logHC.put(now, hc);
				System.out.println("JVM started");
				hc.setActive_cluster("Primary");
				hc.setIs_restarted(false);
				hc.setRestart_time(now);
				/*String response = builder.build()
						.postForObject("http://localhost:8081/kafka/healthcheck/notify", entity, String.class);
				//logHC.put(now, hc);*/
			}
			else {

			if(logHC.lastEntry().getValue().active_cluster!="Primary") {
			    System.out.println("Status change to Primary");
				hc.setIs_restarted(true);
				hc.setActive_cluster("Primary");
				hc.setRestart_time(now);
				headers.setContentType(MediaType.APPLICATION_JSON);
				String response = builder.build()
						.postForObject("http://localhost:8081/kafka/healthcheck/notify", entity, String.class);
				System.out.println(response);
			}

			else {
				System.out.println("Status change to Primary, no restart");
				hc.setActive_cluster("Primary");
				hc.setIs_restarted(false);
				hc.setRestart_time(logHC.lastEntry().getValue().getRestart_time());
			}
			logHC.put(now, hc);
			}

			
		}
		
		System.out.println(logHC.size());
		System.out.println(logHC.lastEntry().getValue().active_cluster);
	}
}