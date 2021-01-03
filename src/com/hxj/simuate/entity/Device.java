package com.hxj.simuate.entity;

public class Device {
	private String name; //设备名
	private String state; //设备状态 
	private Integer time; //等待时间
	private PCB pcb; //使用设备的进程控制块
	private Integer usedTime; //使用时间
	
	public Device() {
		
	}
	
	public Device(String name, String state, Integer time, PCB pcb, Integer usedTime) {
		super();
		this.name = name;
		this.state = state;
		this.pcb = pcb;
		this.time = time;
		this.usedTime = usedTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public PCB getPcb() {
		return pcb;
	}
	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}

	public Integer getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Integer usedTime) {
		this.usedTime = usedTime;
	}
}
