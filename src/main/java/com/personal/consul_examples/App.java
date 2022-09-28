package com.personal.consul_examples;

import com.ipqualityscore.JavaIPQSDBReader.DBReader;
import com.ipqualityscore.JavaIPQSDBReader.FileReader;
import com.ipqualityscore.JavaIPQSDBReader.FraudScore;
import com.ipqualityscore.JavaIPQSDBReader.IPQSRecord;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {
    	    // Open a DB file reader.
    	    FileReader reader = DBReader.Open("/Users/gdappili/Downloads/IPQualityScore-IP-Reputation-Database-Sample.ipqs/IPQualityScore-IP-Reputation-Database-IPv4.ipqs");
    	    
    	    // Request data about a given IP address.
    	    String ip = "8.8.0.0";
    	    System.out.println(reader.getColumns().get(1).toString());
    	    IPQSRecord record = reader.Fetch(ip);
    	    int k= record.getFraudScore().forStrictness(1);
    	    System.out.println(k);
    	    
    	    
    	    // Use the IPQSRecord to print some data about this IP.
    	    if(record.isProxy()){
    	    	
    	        System.out.println(ip + " is a proxy.");
    	    } else {
    	        System.out.println(ip + " is not a proxy.");
    	    }
    	} catch (Exception e){
    	    System.out.println(e.getMessage());
    	}
    }
}
