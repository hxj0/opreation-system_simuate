package com.hxj.simuate.view;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.hxj.simuate.entity.PCB;
import com.hxj.simuate.entity.Process;
import com.hxj.simuate.handler.CPU;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class Main extends JFrame {
	
	public final int MEMORY_VALUE = 128; //内存大小

	public JButton[] fragments = new JButton[MEMORY_VALUE];
	public boolean[] allcation = new boolean[MEMORY_VALUE];
	
	public static Color freeColor = new Color(154,205,50); 
	
	public static Color usedColor = new Color(0xFF, 0x44, 0x00);

	public static JProgressBar progressBar;
	
	public static volatile Boolean run = false; 
	
	private JPanel contentPane;
	
	public static JTextField processField;
	public static JTextField instruField;
	public static JTextField middleResField;
	public static JTextField priorityField;
	public static JTextField toltalTimeField;
	public static JTextField clockField;
	
	private JTable readyTable;
	private JTable blockTable;
	private JTable deviceTable;
	private JTable resTable;

	private Thread thread;

	private static JList processList; 
 
	public static List<PCB> processes;

	public static DefaultListModel<String> model;

	private CPU cpu; 

	public static JComboBox speedComboBox; 

	public static JButton startBtn; 
	
	public static DefaultTableModel readyModel;
	public static DefaultTableModel blockModel;
	public static DefaultTableModel deviceModel;
	public static DefaultTableModel resModel;
	public static JTextField editTimeField;
	
	public int id = 0;
	

	/**
	 * Create the frame.
	 */
	public Main() {
		
		setTitle("操作系统-模拟文件与进程管理");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/statics/操作系统.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 765);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.CYAN);
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		setJMenuBar(menuBar);
		
		JMenu aboutMenu = new JMenu("关于");
		aboutMenu.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		menuBar.add(aboutMenu);
		
		JMenuItem aboutItem = new JMenuItem("关于");
		aboutItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		aboutMenu.add(aboutItem);
		aboutItem.addActionListener(e->{
			JOptionPane.showMessageDialog(null, "这是河北大学2020学年 18级候鑫杰操作系统课程设计");
		});
		
		JMenuItem exitItem = new JMenuItem("退出");
		exitItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		aboutMenu.add(exitItem);
		exitItem.addActionListener(e->{
			System.exit(0); 
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("当前进程");
		lblNewLabel.setIcon(new ImageIcon(Main.class.getResource("/statics/进程管理.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel.setBounds(22, 10, 85, 27);
		contentPane.add(lblNewLabel);
		
		processField = new JTextField();
		processField.setFont(new Font("宋体", Font.PLAIN, 16));
		processField.setEditable(false);
		processField.setHorizontalAlignment(SwingConstants.CENTER);
		processField.setBounds(32, 37, 66, 27);
		contentPane.add(processField);
		processField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("执行指令");
		lblNewLabel_1.setIcon(new ImageIcon(Main.class.getResource("/statics/调度指令.png")));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(117, 10, 85, 27);
		contentPane.add(lblNewLabel_1);
		
		instruField = new JTextField();
		instruField.setFont(new Font("宋体", Font.PLAIN, 16));
		instruField.setEditable(false);
		instruField.setHorizontalAlignment(SwingConstants.CENTER);
		instruField.setColumns(10);
		instruField.setBounds(116, 37, 86, 27);
		contentPane.add(instruField);
		
		JLabel lblNewLabel_1_1 = new JLabel("中间结果");
		lblNewLabel_1_1.setIcon(new ImageIcon(Main.class.getResource("/statics/结果.png")));
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(212, 10, 86, 27);
		contentPane.add(lblNewLabel_1_1);
		
		middleResField = new JTextField();
		middleResField.setFont(new Font("宋体", Font.PLAIN, 16));
		middleResField.setEditable(false);
		middleResField.setHorizontalAlignment(SwingConstants.CENTER);
		middleResField.setColumns(10);
		middleResField.setBounds(222, 37, 66, 27);
		contentPane.add(middleResField);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("优先级");
		lblNewLabel_1_1_1.setIcon(new ImageIcon(Main.class.getResource("/statics/优先级.png")));
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_1.setBounds(308, 10, 70, 27);
		contentPane.add(lblNewLabel_1_1_1);
		
		priorityField = new JTextField();
		priorityField.setFont(new Font("宋体", Font.PLAIN, 16));
		priorityField.setHorizontalAlignment(SwingConstants.CENTER);
		priorityField.setEditable(false);
		priorityField.setColumns(10);
		priorityField.setBounds(320, 37, 49, 27);
		contentPane.add(priorityField);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("执行时间");
		lblNewLabel_1_1_2.setIcon(new ImageIcon(Main.class.getResource("/statics/时间.png")));
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_2.setBounds(388, 10, 99, 27);
		contentPane.add(lblNewLabel_1_1_2);
		
		toltalTimeField = new JTextField();
		toltalTimeField.setFont(new Font("宋体", Font.PLAIN, 16));
		toltalTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		toltalTimeField.setEditable(false);
		toltalTimeField.setColumns(10);
		toltalTimeField.setBounds(417, 37, 49, 27);
		contentPane.add(toltalTimeField);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("时钟");
		lblNewLabel_1_1_1_1.setIcon(new ImageIcon(Main.class.getResource("/statics/时间 (1).png")));
		lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1.setBounds(490, 10, 70, 27);
		contentPane.add(lblNewLabel_1_1_1_1);
		
		clockField = new JTextField();
		clockField.setFont(new Font("宋体", Font.PLAIN, 16));
		clockField.setHorizontalAlignment(SwingConstants.CENTER);
		clockField.setEditable(false);
		clockField.setColumns(10);
		clockField.setBounds(500, 37, 49, 27);
		clockField.setText("0"); 
		contentPane.add(clockField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createTitledBorder("就绪队列")); 
		scrollPane.setToolTipText("");
		scrollPane.setBounds(22, 88, 266, 269);
		contentPane.add(scrollPane);
		
		readyTable = new JTable();
		readyModel = new DefaultTableModel(null, new String[] {"进程名","优先级","等待时间"});
		readyTable.setModel(readyModel); 
		readyTable.setEnabled(false);
		readyTable.setFont(new Font("宋体", Font.PLAIN, 16));
		scrollPane.setViewportView(readyTable);
		readyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setToolTipText("");
		scrollPane_1.setBorder(new TitledBorder(null, "阻塞队列", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_1.setBounds(298, 88, 272, 269);
		contentPane.add(scrollPane_1);
		
		blockTable = new JTable();
		blockTable.setEnabled(false);
		blockModel = new DefaultTableModel(null, new String[] {"进程名","优先级","等待时间","阻塞原因"});
		blockTable.setModel(blockModel);
		blockTable.setFont(new Font("宋体", Font.PLAIN, 16));
		blockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}});
		scrollPane_1.setViewportView(blockTable);
		

		for(int i = 0; i < MEMORY_VALUE; ++i) {
			fragments[i] = new JButton();
			fragments[i].setBackground(freeColor); 
//			fragments[i].setForeground(new Color(154,205,50));  
			fragments[i].setBounds(22+4*i, 604, 4, 54);
			fragments[i].setEnabled(false); 
			contentPane.add(fragments[i]);
		}
		
		progressBar = new JProgressBar();
		progressBar.setBounds(22, 604, 550, 54);
		contentPane.add(progressBar);
		progressBar.setBackground(new Color(154,205,50)); 
		progressBar.setForeground(new Color(0xFF, 0x44, 0x00));
		progressBar.setMaximum(MEMORY_VALUE); 
		progressBar.setValue(0); 
		progressBar.setVisible(false);
		
		
		JLabel lblNewLabel_2 = new JLabel("用户内存使用:");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(22, 576, 112, 27);
		contentPane.add(lblNewLabel_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new TitledBorder(null, "程序列表", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_2.setBounds(580, 312, 196, 346);
		contentPane.add(scrollPane_2);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setToolTipText("");
		scrollPane_3.setBorder(BorderFactory.createTitledBorder("设备占用"));
		scrollPane_3.setBounds(298, 366, 272, 200);
		contentPane.add(scrollPane_3);
		
		deviceTable = new JTable();
		deviceModel = new DefaultTableModel(null, new String[] {"设备名","状态","时间","占用进程"}); 
		deviceTable.setModel(deviceModel); 
		deviceTable.setFont(new Font("宋体", Font.PLAIN, 16));
		deviceTable.setEnabled(false);
		deviceTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}});
		
		scrollPane_3.setViewportView(deviceTable);
		
		JScrollPane scrollPane_3_2 = new JScrollPane();
		scrollPane_3_2.setToolTipText("");
		scrollPane_3_2.setBorder(BorderFactory.createTitledBorder("执行结果"));
		scrollPane_3_2.setBounds(22, 367, 266, 199);
		contentPane.add(scrollPane_3_2);
		
		resTable = new JTable();
		resModel = new DefaultTableModel(null, new String[] {"进程名", "最终结果", "执行耗时"});
		resTable.setModel(resModel);
		resTable.setFont(new Font("宋体", Font.PLAIN, 16)); 
		resTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}});
		resTable.setEnabled(false);
		
		scrollPane_3_2.setViewportView(resTable);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("启动");
		lblNewLabel_1_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_1.setFont(new Font("宋体", Font.BOLD, 16));
		lblNewLabel_1_1_1_1_1.setBounds(580, 10, 70, 27);
		contentPane.add(lblNewLabel_1_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_2 = new JLabel("暂停");
		lblNewLabel_1_1_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_2.setFont(new Font("宋体", Font.BOLD, 16));
		lblNewLabel_1_1_1_1_2.setBounds(731, 10, 66, 27);
		contentPane.add(lblNewLabel_1_1_1_1_2);
		
		JButton stopBtn = new JButton("");
		stopBtn.setBounds(731, 37, 66, 62);
		ImageIcon icon1 = new ImageIcon(Main.class.getResource("/statics/暂停.png"));
		icon1.setImage(icon1.getImage().getScaledInstance(66, 62, Image.SCALE_DEFAULT));
		stopBtn.setIcon(icon1);
		contentPane.add(stopBtn);
		stopBtn.addActionListener(e->{
			run = !run;
		});
		
		startBtn = new JButton("");
		ImageIcon icon = new ImageIcon(Main.class.getResource("/statics/开机.png"));
		icon.setImage(icon.getImage().getScaledInstance(66, 62, Image.SCALE_DEFAULT));
		startBtn.setIcon(icon);
		startBtn.setBounds(580, 37, 66, 62);
		contentPane.add(startBtn);
		
		ImageIcon icon2 = new ImageIcon(Main.class.getResource("/statics/重启.png"));
		icon2.setImage(icon2.getImage().getScaledInstance(66, 62, Image.SCALE_DEFAULT));
		JButton rebootBtn = new JButton(icon2);
		rebootBtn.setFont(new Font("宋体", Font.PLAIN, 16));
		rebootBtn.setBounds(660, 37, 66, 62);
		contentPane.add(rebootBtn);
		rebootBtn.addActionListener(e->{
			progressBar.setValue(0);
			if(thread != null)thread.stop();
			dispose();
			new Main();
		});
		
		JLabel lblNewLabel_1_1_1_1_3 = new JLabel("速度:");
		lblNewLabel_1_1_1_1_3.setIcon(new ImageIcon(Main.class.getResource("/statics/速度.png")));
		lblNewLabel_1_1_1_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_3.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1_3.setBounds(607, 138, 66, 27);
		contentPane.add(lblNewLabel_1_1_1_1_3);
		
		speedComboBox = new JComboBox();
		speedComboBox.setModel(new DefaultComboBoxModel(new String[] {"快", "中", "慢"}));
		speedComboBox.setFont(new Font("宋体", Font.PLAIN, 16));
		speedComboBox.setBounds(688, 138, 49, 27);
		contentPane.add(speedComboBox);
		speedComboBox.setSelectedIndex(1); 
		
		JLabel lblNewLabel_1_1_1_1_4 = new JLabel("时间片:");
		lblNewLabel_1_1_1_1_4.setIcon(new ImageIcon(Main.class.getResource("/statics/时间片.png")));
		lblNewLabel_1_1_1_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_4.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1_4.setBounds(589, 183, 84, 27);
		contentPane.add(lblNewLabel_1_1_1_1_4);
		
		editTimeField = new JTextField();
		editTimeField.setText("5");
		editTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		editTimeField.setFont(new Font("宋体", Font.PLAIN, 16));
		editTimeField.setColumns(10);
		editTimeField.setBounds(688, 183, 49, 27);
		contentPane.add(editTimeField);
		editTimeField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if(ch == KeyEvent.VK_BACK_SPACE)return;
				if(ch < '0' || ch > '9')e.consume();
			}
		});

		JLabel lblNewLabel_1_1_1_1_4_1 = new JLabel("调度算法:");
		lblNewLabel_1_1_1_1_4_1.setIcon(new ImageIcon(Main.class.getResource("/statics/算法包管理.png")));
		lblNewLabel_1_1_1_1_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_4_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1_4_1.setBounds(580, 224, 93, 27);
		contentPane.add(lblNewLabel_1_1_1_1_4_1);
		
		JComboBox algComboBox = new JComboBox();
		algComboBox.setModel(new DefaultComboBoxModel(new String[] {"时间片轮转", "优先级"}));
		algComboBox.setSelectedIndex(0);
		algComboBox.setFont(new Font("宋体", Font.PLAIN, 16));
		algComboBox.setBounds(688, 224, 99, 27);
		contentPane.add(algComboBox);
		
		startBtn.addActionListener(e->{
			run = true;
			startBtn.setEnabled(false); 
			editTimeField.setEditable(false);
			speedComboBox.setEnabled(false);
			processList.setEnabled(true); 
			algComboBox.setEnabled(false); 
			
			thread = new Thread(()->{
				int timeSlice = Integer.parseInt(editTimeField.getText());
				if(timeSlice <= 0)timeSlice = 5;
				int index = speedComboBox.getSelectedIndex();
				int algIndex = algComboBox.getSelectedIndex();
				CPU.priority = algIndex == 1;
				cpu = new CPU(this, timeSlice, index);
			}); 
			thread.start();
		});
		
		//声明菜单
		JPopupMenu popupMenu = new JPopupMenu();
		
		//运行
		JMenuItem privateChat = new JMenuItem("运行");
		privateChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startProcess();
			}
		});
		popupMenu.add(privateChat);
		
		//查看详情
		JMenuItem blackList = new JMenuItem("编辑");
		blackList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("编辑");
				for(PCB pcb : processes) {
					if(pcb.getName().equals(processList.getSelectedValue().toString().substring(0,2))) {
						new Edit(pcb);
					}
				}
			}
		});
		popupMenu.add(blackList);
		
		processList = new JList(); 
		processList.setFont(new Font("宋体", Font.PLAIN, 16));
		scrollPane_2.setViewportView(processList);		
		initProcessList();
		processList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) { //双击
					startProcess();
				}else if(e.isMetaDown() && processList.getSelectedIndex() >= 0) { //左键
					popupMenu.show(processList, e.getX(), e.getY());
				}
			}
		});
		processList.setEnabled(false); 
		
		JLabel lblNewLabel_1_1_1_1_2_1 = new JLabel("重启");
		lblNewLabel_1_1_1_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1_2_1.setFont(new Font("宋体", Font.BOLD, 16));
		lblNewLabel_1_1_1_1_2_1.setBounds(660, 10, 66, 27);
		contentPane.add(lblNewLabel_1_1_1_1_2_1);
		
		
		setResizable(false);
		setLocationRelativeTo(null); 
		setVisible(true);
	}


	public static void initProcessList() {
		processes = new ArrayList<>();
		File root = new File("src/resource/process"); 
 		File[] files = root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
 		});
 		for(int i = 0; i < files.length; ++i) {
 			BufferedReader reader = null;
 			try {
				reader = new BufferedReader(new FileReader(files[i]));
				String str = null;
				int memory = 0;
				List<String> codes = new ArrayList<>();
				
	 			while((str = reader.readLine()) != null) {
	 				memory++;
	 				codes.add(str);
 	 			}
//	 			System.out.println(codes); 
	 			if(codes.size() == 0)continue;
	 			Process process = new Process(codes, new Integer[] {0,0, memory});
	 			String name = files[i].getName(); 
				PCB pcb = new PCB(0, (int)(Math.random()*10+1), codes.get(0), 1, 0, 0, name.split("\\.")[0], i, new Long[]{0L,0L, 0L}, process, CPU.timeClock);
				processes.add(pcb);
			} catch (IOException e) { 
				e.printStackTrace();
			}finally {
				try { 
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
 		}
 		model = new DefaultListModel<>(); 
		processList.setModel(model);
		updateModel();
	}
	
	public void startProcess() {
		for(PCB pcb : processes) {
			if(pcb.getName().equals(processList.getSelectedValue().toString().substring(0,2))) {
				CPU.bufferList.add(new PCB(0, pcb.getPriority(), 
						pcb.getProcess().getCodes().get(0), 1, 0, 0, ++id+"__"+pcb.getName(), id, new Long[]{0L,0L, 0L}, new Process(pcb.getProcess()), CPU.timeClock));
				break;
			}
		}
	}
	
	public static void updateModel() {
		model.clear();
 		for(PCB pcb : processes) {
 			model.addElement(pcb.getName()+"  优先级:"+pcb.getPriority()); 
 		}
	}
}
