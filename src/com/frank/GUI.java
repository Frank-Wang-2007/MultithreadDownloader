package com.frank;

import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI extends JFrame {
	public static String DownloadURL,FileName;
	public static int ThreadAmount;
	static final JTextArea jta = new JTextArea("Log\n", 10000, 65);
	final JScrollPane jp = new JScrollPane(jta);
	public GUI() {
		setTitle("MultithreadDownloader");
		setSize(900, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container cp = getContentPane();
		getContentPane().setLayout(new FlowLayout());
		final JLabel jl = new JLabel("MultithreadDownloader");
		final JTextField jt1 = new JTextField("下载地址（https协议）", 65);
		final JTextField jt2 = new JTextField("文件名（带后缀）", 65);
		final JTextField jt3 = new JTextField("线程数", 65);
		final JButton jb = new JButton("确定");
		jta.setLocation(20,100);
		jt1.setLocation(20,20);
		jt2.setLocation(20,40);
		jt3.setLocation(20,60);
		jb.setLocation(20,80);
		jl.setLocation(20,0);
		cp.add(jl);
		cp.add(jt1);
		cp.add(jt2);
		cp.add(jt3);
		cp.add(jb);
		cp.add(jp);
		jta.setEditable(false);
		jb.setSize(5,5);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DownloadURL = jt1.getText();
				FileName = jt2.getText();
				String a = jt3.getText();
				if (NumberUtils.isNumber(a)) {
					ThreadAmount = Integer.parseInt(a);
					DownloadManager downloadManager = new DownloadManager("./Download/" + GUI.FileName, GUI.ThreadAmount, GUI.DownloadURL);
					downloadManager.action();
				}else{
					PrintLog("[ERROR]:线程数必须为正整数");
				}
			}
		});
		setVisible(true);
	}
	public void PrintLog(String log){
		SimpleDateFormat formatter = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss]");
		jta.append(new StringBuilder().append(formatter.format(new Date())).append(log).append("\n").toString());
	}
}
