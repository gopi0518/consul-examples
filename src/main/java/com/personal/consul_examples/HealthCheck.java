package com.personal.consul_examples;

import java.util.Date;

public class HealthCheck {
	String active_cluster;
    boolean is_restarted;
	public boolean isIs_restarted() {
		return is_restarted;
	}

	public void setIs_restarted(boolean is_restarted) {
		this.is_restarted = is_restarted;
	}

	public Date getRestart_time() {
		return restart_time;
	}

	public void setRestart_time(Date restart_time) {
		this.restart_time = restart_time;
	}

	
	Date restart_time;

	public String getActive_cluster() {
		return active_cluster;
	}

	public void setActive_cluster(String active_cluster) {
		this.active_cluster = active_cluster;
	}

}
