package com.frank;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadThread implements Runnable {
	private int whichThread;// 当前第几个线程 ， 用于给下载文件起名
	private boolean isFinish;// 监听单一线程下载是否完成
	private int length;// 本线程要下载的文件字节数
	private int startPosition;// 本线程要下载的文件字节数
	private String savePath;// 保存的路径
	private String url;// 要下载的文件,用于创建连接

	public void PrintLog(String log){
		SimpleDateFormat formatter = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss]");
		GUI.jta.append(new StringBuilder().append(formatter.format(new Date())).append(log).append("\n").toString());
	}

	@Override
	public void run() {
		HttpURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			PrintLog("[INFO]:InitThread：" + whichThread);
			main.logger.Info("InitThread：" + whichThread);
			URL fileUrl = new URL(url);
			conn = (HttpURLConnection) fileUrl.openConnection();// 与服务器创建连接
			conn.setRequestMethod("GET");// 下载使用get请求

			conn.setRequestProperty("Range", "bytes=" + startPosition + "-");// 这里是设置文件输入流的首位置
			conn.connect();// 与服务器创建连接
			in = conn.getInputStream();// 获得输入流
			out = new FileOutputStream(savePath + whichThread);// 在硬盘上创建文件 ， 准备写入
			byte[] bytes = new byte[4096];// 用于写入的字节数组
			int count = 0;// 一共下载了多少字节
			int read = 0;// 单次读取的字节数
			while ((read = in.read(bytes)) != -1) {
				if (length - count < bytes.length) {// 检查一下是不是下载到了本线程需要的长度
					/* 比如说本线程还需要900字节，但是已经读取1000
					字节，则用要本线程总下载长度减去
					已经下载的长度*/
					read = length - count;
				}
				out.write(bytes, 0, read);// 将准确的字节写入输出流
				count = count + read;// 已经下载的字节数加上本次循环字节数
				/* 如果下载字节达到本线程所需要字节数，消除循环，
				 停止下载*/
				if (count == length) {
					break;
				}
			}
			isFinish = true;// 将监视变量设置为true
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 最后进行输入、输出、连接的关闭
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWhichThread() {
		return whichThread;
	}

	public void setWhichThread(int whichThread) {
		this.whichThread = whichThread;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public DownloadThread(int whichThread, int length, int startPosition,
						  String savePath, String url) {
		super();
		this.whichThread = whichThread;
		this.length = length;
		this.startPosition = startPosition;
		this.savePath = savePath;
		this.url = url;
	}

	public DownloadThread() {
		super();
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
}
