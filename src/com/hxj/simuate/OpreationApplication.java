package com.hxj.simuate;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.hxj.simuate.view.Main;

public class OpreationApplication {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Main();
	}
}