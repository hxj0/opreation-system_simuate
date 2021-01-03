package com.hxj.simuate.entity;

import java.util.Arrays;

public class PCB implements Comparable<PCB>{

	private Integer PSW;//可以用1表示时钟中断，2表示输入输出中断，4表示软中断，可以组合1+2,1+4,,2+4,1+2+4
	private Integer priority; //进程优先级 数值小 优先级高
	private String IR; //指令寄存器IR 存放4个字符即可
	private Integer PC; //指令位置
	private Integer DR; //数据缓冲寄存器DR,存放x的值
	private Integer state; // 0就绪，1执行，2阻塞1个时间片，3阻塞两个时间片，以此类推
	private Device device;
	private String name; //进程名字
	private Integer ID; //进程ID
	private Long[] time; //就绪时的等待时间、阻塞时阻塞了多少时间、执行了的总时间
	private Process process;
	private Long enterTime;
	
	public PCB() {
		
	}
	
	public PCB(Integer pSW, Integer priority, String iR, Integer pC, Integer dR, Integer state, String name, Integer iD,
			Long[] time, Process process, Long enterTime) {
		super();
		PSW = pSW;
		this.priority = priority;
		IR = iR;
		PC = pC;
		DR = dR;
		this.state = state;
		this.name = name;
		ID = iD;
		this.time = time;
		this.process = process;
		this.enterTime = enterTime;
	}

	

	public PCB(PCB pcb) {
		PSW = pcb.getPSW();
		this.priority = pcb.priority;
		IR = pcb.getIR();
		PC = pcb.getPC();
		DR = pcb.getDR();
		this.state = pcb.state;
		this.name = pcb.name;
		ID = pcb.getID();
		this.time = pcb.time;
		this.process = pcb.process;
	}

	
	
	
	public Long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Long enterTime) {
		this.enterTime = enterTime;
	}

	public Process getProcess() {
		return process;
	}



	public void setProcess(Process process) {
		this.process = process;
	}



	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Integer getPSW() {
		return PSW;
	}
	public void setPSW(Integer pSW) {
		PSW = pSW;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getIR() {
		return IR;
	}
	public void setIR(String iR) {
		IR = iR;
	}
	public Integer getDR() {
		return DR;
	}
	public void setDR(Integer dR) {
		DR = dR;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getPC() {
		return PC;
	}
	public void setPC(Integer pC) {
		PC = pC;
	}
	public Long[] getTime() {
		return time;
	}
	public void setTime(Long[] time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "PCB [PSW=" + PSW + ", priority=" + priority + ", IR=" + IR + ", PC=" + PC + ", DR=" + DR + ", state="
				+ state + ", device=" + device + ", name=" + name + ", ID=" + ID + ", time=" + Arrays.toString(time)
				+ ", process=" + process + "]";
	}

	@Override
	public int compareTo(PCB o) {
		return this.priority-o.priority;
	}
}
