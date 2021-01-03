package com.hxj.simuate.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class Test {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
//		System.out.println(Pattern.matches("x=.+;", "x=1;"));
//		System.out.println("x=1;".charAt(2)); 
//		try {
////			System.out.println(disk.readByte());
//			RandomAccessFile disk = new RandomAccessFile("src/resource/file/disk.dat", "rw");
//			disk.seek(2);
//			disk.writeByte(0);
//			
//		} catch (IOException e1) {  
//			e1.printStackTrace();
//		}
//		System.out.println(Character.SIZE); 
		try {
			FileWriter fileWriter = new FileWriter(new File("src/resource/process/p0.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
