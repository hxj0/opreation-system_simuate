package com.hxj.simuate.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.hxj.simuate.entity.Device;
import com.hxj.simuate.entity.PCB;
import com.hxj.simuate.entity.Result;
import com.hxj.simuate.view.Main;

public class CPU {
	public static List<PCB> bufferList;
	
	public List<PCB> readyList;

	public List<PCB> blockList;
	
	public List<Device> devices;
	
	public List<Result> results;
	

	private Main main;  
	
	public Integer totalMemory = 0;
	
	public int TIME_SLICE = 5;
	
	public Integer timeSlice ; //默认时间片为5
	
	public static volatile Long timeClock; //系统时钟
	
	public static boolean priority = false; //优先级 调度
	
	public PCB curPcb;
	
	private int sleepTime;
	
	private boolean end = false;  
	
	
	

 	public CPU(Main main, Integer timeSlice, int index) { 
		timeClock = 0L; //初始化时钟
 		TIME_SLICE = timeSlice;
 		this.timeSlice = TIME_SLICE; 
 		
 		if(index == 0) sleepTime = 100;
 		else if(index == 1)sleepTime = 500;
 		else sleepTime = 1000;
 		bufferList = new ArrayList<>(); 
  		readyList = new ArrayList<>();
 		blockList = new ArrayList<>();
 		devices = new ArrayList<>();
 		results = new ArrayList<>();
 		this.main = main;
// 		System.out.println(main); 
// 		while(!Main.run) {
// 			try {
//				Thread.sleep(500); //暂停时 睡眠
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
// 		}
// 		initProcess();
 		initDevice();
 		updateView();
 		run();
 	}
 	
 	
 	public void run() {
 		int delay = 0;
 		while(true) {
 			if(Main.run) {
 				if(bufferList.size() > 0) {
					int free = 0, start = -1, end = -1;
 					for(int i = 0; i < bufferList.size(); ++i) {
 						for(int j = 0; j < main.MEMORY_VALUE; ++j) {
 							if(!main.allcation[j]) {
 								if(start == -1)start = j;
 								free++;
 								if(free >= bufferList.get(i).getProcess().getMemory()[2]) {
 									end = j;
 	 								break;
 								}
 							}else {
 								free = 0;
 								start = -1;
 							}
 						}
 						
// 						totalMemory += bufferList.get(i).getProcess().getMemory();
 						if(end == -1) {
// 							totalMemory -= bufferList.get(i).getProcess().getMemory();
 							bufferList.clear();
 							JOptionPane.showMessageDialog(null, "内存不足！","提示", JOptionPane.WARNING_MESSAGE, 
 									new ImageIcon(CPU.class.getResource("/statics/错误.png")));
 							break;
 						}else {
 							for(int j = start; j < start+bufferList.get(i).getProcess().getMemory()[2]; ++j) {
 								main.allcation[j] = true;
 							}
 							bufferList.get(i).getProcess().getMemory()[0] = start;
 							bufferList.get(i).getProcess().getMemory()[1] = start+bufferList.get(i).getProcess().getMemory()[2]-1;
 						}
 						readyList.add(bufferList.get(i));
 						bufferList.remove(bufferList.get(i));
 					}
 				}
 				delay = 0;
 				dispatch(); //时间片轮转算法调度
 			}else {
 				try {
					Thread.sleep(sleepTime); //暂停时 睡眠
					delay++;
					if(delay > 1000 || end) {
						Main.startBtn.setEnabled(true);
						Main.speedComboBox.setEnabled(true);
						Main.editTimeField.setEditable(true); 
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
 			}
 		}
 	}
 	
 	//时间片轮转算法
	private void dispatch() {
		if(priority)Collections.sort(readyList); 
		boolean IO = false;
		try {
			Thread.sleep(sleepTime); //睡眠减速
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(curPcb == null && readyList.size() > 0) {
			curPcb = readyList.remove(0); 
			curPcb.getTime()[0] = 0L;
			curPcb.getTime()[1] = 0L;
			curPcb.setState(1); 
		}

		updateView();
		updateField();
		timeClock++; //时钟
		if(curPcb == null) {
			if(blockList.size() > 0) {
				dealIOBlock();
				return;
			}
//			Main.run = false;
//			end = true;
			return;
		}

		curPcb.getTime()[2]++;
		timeSlice--; 
		if(Pattern.matches("x=.+;", curPcb.getIR())) {
			curPcb.setDR(curPcb.getIR().charAt(2)-'0'); 
		}else if(Pattern.matches("x\\+\\+;", curPcb.getIR())) {
			curPcb.setDR(curPcb.getDR()+1); 
		}else if(Pattern.matches("x--;", curPcb.getIR())) {
			curPcb.setDR(curPcb.getDR()-1); 
		}else if(Pattern.matches("end\\.", curPcb.getIR())) {
			curPcb.setPSW(1); 
			results.add(new Result(curPcb.getName(), curPcb.getDR(), timeClock-curPcb.getEnterTime()));
			if(curPcb.getDevice() != null) {
				curPcb.getDevice().setPcb(null);  
				curPcb.getDevice().setState("空闲");
				curPcb.getDevice().setTime(0);
				curPcb.getDevice().setUsedTime(0);
			}
			timeSlice = TIME_SLICE;
//			totalMemory -= curPcb.getProcess().getMemory(); //释放内存

			freeMemory(curPcb);
			
			updateView();
			updateField();
			curPcb = null;
			return;
		}else if(Pattern.matches("!A\\d;", curPcb.getIR())) {
			waitDevice(curPcb, "A", curPcb.getIR().charAt(2)-'0');
			IO = true;
		}else if(Pattern.matches("!B\\d;", curPcb.getIR())) {
			waitDevice(curPcb, "B", curPcb.getIR().charAt(2)-'0');
			IO = true;
		}else if(Pattern.matches("!C\\d;", curPcb.getIR())) {
			waitDevice(curPcb, "C", curPcb.getIR().charAt(2)-'0');
			IO = true;
		}else {
			JOptionPane.showMessageDialog(null, "程序出错，指令错误！","提示", JOptionPane.WARNING_MESSAGE, 
						new ImageIcon(CPU.class.getResource("/statics/错误.png")));
//			totalMemory -= curPcb.getProcess().getMemory(); //释放内存
			freeMemory(curPcb);
			curPcb = null;
			return;
		}
		
		dealIOBlock();
		
		curPcb.setIR(curPcb.getProcess().getCodes().get(curPcb.getPC())); 
		curPcb.setPC(curPcb.getPC()+1);
		if(IO) {
			timeSlice = TIME_SLICE;
			curPcb = null;
			return;
		}
		if(timeSlice == 0) {
			timeSlice = TIME_SLICE;
			readyList.add(curPcb);
			curPcb = null;
		}
	}

	
	public void freeMemory(PCB pcb) {
		for(int i = pcb.getProcess().getMemory()[0]; i <= pcb.getProcess().getMemory()[1]; ++i) {
			main.allcation[i] = false;
		}
	}

	public void waitDevice(PCB pcb, String name, Integer time) {
		timeSlice = TIME_SLICE;
		pcb.setState(1+time); //设置阻塞
		pcb.setPSW(2); 
		for(Device device : devices) {
			if(device.getName().contains(name) && device.getPcb() == null) {
				device.setPcb(pcb);
				pcb.setDevice(device); 
				device.setState("占用");
				device.setTime(time);
				device.setUsedTime(0); 
				break;
			}
		}
		
		if(pcb.getDevice() == null) {
			pcb.setDevice(new Device(name, "", time, pcb, 0)); 
		}
		if(!blockList.contains(pcb)) blockList.add(pcb);
	}
	
	public void dealIOBlock() {
		ArrayList<PCB> list = new ArrayList<>();
		for(PCB pcb : readyList) { 
			pcb.getTime()[0]++;
		}
		for(PCB pcb : blockList) {
			pcb.getTime()[1]++;
			if(pcb.getDevice().getState().equals("")) {
				waitDevice(pcb, pcb.getDevice().getName(), pcb.getDevice().getTime()); 
				continue;
			}
			pcb.setState(pcb.getState()-1);
			if(pcb.getState() == 1) {
				pcb.setState(0);
				list.add(pcb);
			}
		}
		readyList.addAll(list);
		blockList.removeAll(list);
		for(PCB pcb : list) {
			pcb.getDevice().setPcb(null);  
			pcb.getDevice().setState("空闲");
			pcb.getDevice().setUsedTime(0);
			pcb.getDevice().setTime(0); 
			pcb.setDevice(null); 
		}
		for(Device device : devices) {
			if("占用".equals(device.getState())) {
				device.setUsedTime(device.getUsedTime()+1); 
			}
		}
	}
	
	public void updateField() {
		
		Main.processField.setText(curPcb == null? "idle" : curPcb.getName());
		Main.instruField.setText(curPcb == null? "nop":curPcb.getIR());
		Main.middleResField.setText(curPcb == null? "0":curPcb.getDR().toString());
		Main.priorityField.setText(curPcb == null? "10":curPcb.getPriority().toString()); 
		Main.toltalTimeField.setText(curPcb == null? "-":curPcb.getTime()[2].toString());  
		Main.clockField.setText(timeClock.toString()); 
	}
	
	public void updateView() {
		Main.readyModel.getDataVector().clear();
		Main.blockModel.getDataVector().clear();
		Main.deviceModel.getDataVector().clear();
		Main.resModel.getDataVector().clear();
		for(PCB pcb : readyList) {
			Main.readyModel.addRow(new Object[] {pcb.getName(), pcb.getPriority(), pcb.getTime()[0]});
		}
		for(Device device : devices) {
			Main.deviceModel.addRow(new Object[] {device.getName(), device.getState(), device.getUsedTime(), device.getPcb() == null?"":device.getPcb().getName()});
		}
		for(PCB pcb : blockList) {
			Main.blockModel.addRow(new Object[] {pcb.getName(), pcb.getPriority(), pcb.getTime()[1], "等待"+pcb.getDevice().getName()});
		}
		for(Result result : results) {
			Main.resModel.addRow(new Object[] {result.getName(), result.getRes(), result.getTime()}); 
		}
		for(int i = 0; i < main.MEMORY_VALUE; ++i) {
			if(main.allcation[i]) main.fragments[i].setBackground(Main.usedColor);
			else main.fragments[i].setBackground(Main.freeColor);
		}
		main.repaint();
	}

 	private void initDevice() {
 		devices.clear();
 		devices.add(new Device("A1", "空闲", 0, null, 0));
 		devices.add(new Device("A2", "空闲", 0, null, 0));
 		devices.add(new Device("A3", "空闲", 0, null, 0));
 		devices.add(new Device("B1", "空闲", 0, null, 0));
 		devices.add(new Device("B2", "空闲", 0, null, 0));
 		devices.add(new Device("C", "空闲", 0, null, 0));
	}
	
}
