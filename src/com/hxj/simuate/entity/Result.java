package com.hxj.simuate.entity;

public class Result {
	private String name;
	private Integer res;
	private Long time;
	
	
	public Result(String name, Integer res, Long time) {
		super();
		this.name = name;
		this.res = res;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRes() {
		return res;
	}
	public void setRes(Integer res) {
		this.res = res;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
}
