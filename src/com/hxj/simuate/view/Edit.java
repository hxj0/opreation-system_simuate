package com.hxj.simuate.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.hxj.simuate.entity.PCB;
import com.hxj.simuate.entity.Process;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class Edit extends JFrame {

	private JPanel contentPane;

	public Edit(PCB pcb) { 
		setTitle("编辑进程"+pcb.getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(Edit.class.getResource("/statics/编辑.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 462, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 57, 381, 243);
		contentPane.add(scrollPane);
		
		JTextPane instruText = new JTextPane();
		instruText.setFont(new Font("宋体", Font.PLAIN, 16));
		List<String> codes = pcb.getProcess().getCodes();
		scrollPane.setViewportView(instruText);
		Document document = instruText.getDocument();
		for(String code : codes) {
			try {
				document.insertString(document.getLength(), code+'\n', null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		
		JLabel lblNewLabel = new JLabel("优先级:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Edit.class.getResource("/statics/优先级.png")));
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel.setBounds(253, 16, 88, 31);
		contentPane.add(lblNewLabel);
		
		JComboBox<String> comboBox = new JComboBox<>();
		for(int i = 1; i <= 10; ++i) {
			comboBox.addItem(i+"");
		}
		comboBox.setSelectedItem(pcb.getPriority().toString()); 
		comboBox.setFont(new Font("宋体", Font.PLAIN, 16));
		comboBox.setBounds(351, 17, 70, 28);
		contentPane.add(comboBox);
		
		JLabel lblNewLabel_1 = new JLabel("指令序列:");
		lblNewLabel_1.setIcon(new ImageIcon(Edit.class.getResource("/statics/调度指令.png")));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(43, 16, 99, 31);
		contentPane.add(lblNewLabel_1);
		
		JButton backBtn = new JButton("退出");
		backBtn.setIcon(new ImageIcon(Edit.class.getResource("/statics/退出.png")));
		backBtn.setFont(new Font("宋体", Font.PLAIN, 16));
		backBtn.setBounds(50, 310, 92, 30);
		contentPane.add(backBtn);
		backBtn.addActionListener(e->{
			dispose();
		});
		
		JButton saveBtn = new JButton("保存");
		saveBtn.setIcon(new ImageIcon(Edit.class.getResource("/statics/保存.png")));
		saveBtn.setFont(new Font("宋体", Font.PLAIN, 16));
		saveBtn.setBounds(320, 310, 92, 30);
		saveBtn.addActionListener(e -> {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < document.getLength(); ++i) {
				try {
					sb.append(document.getText(i, 1));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			String string = sb.toString();
			String[] strings = string.split("\\n");
			try {
				FileWriter fileWriter = new FileWriter(new File("src/resource/process/"+pcb.getName()+".txt"));
				for(String s : strings) {
					if(!s.contains("end"))s+="\n";
					fileWriter.write(s);
				}
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace(); 
			}
			List<String> codes1 = Arrays.asList(strings); 
			Process process = new Process(codes1, new Integer[] {0,0,codes1.size()});
			pcb.setProcess(process); 
			pcb.setPriority(Integer.parseInt(comboBox.getSelectedItem().toString()));
			Main.updateModel();
			dispose();
		});
		
		contentPane.add(saveBtn);
		setLocationRelativeTo(null); 
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setVisible(true); 
	}
}
