package com.hxj.simuate.entity;

import java.util.ArrayList;
import java.util.List;

public class Process{
	private List<String> codes; // 进程的指令集
	private Integer[] memory; // 进程占用的内存 开始位置  结束位置  大小
	
	public Process() {
		
	}
	
	public Process(List<String> codes, Integer[] memory) {
		this.codes = codes;
		this.memory = memory;
	}
	
	public Process(Process process) {
		this.memory = new Integer[] {0,0,process.getMemory()[2]};
		this.codes = new ArrayList<String>(process.codes);
	}
	
	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public Integer[] getMemory() {
		return memory;
	}

	public void setMemory(Integer[] memory) {
		this.memory = memory;
	}

}
