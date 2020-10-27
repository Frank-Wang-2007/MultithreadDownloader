package com.frank;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadManager implements Runnable {

	private String savePath;// 保存路径

	private int threadNum;// 总的下载线程数

	private String urlFile;// 下载的链接地址

	private boolean isStarted;// 是否下载开始

	private List<DownloadThread> downloadList = new ArrayList<DownloadThread>();// 用于监视何时合并文件存放Thread的list

	public void PrintLog(String log){
		SimpleDateFormat formatter = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss]");
		GUI.jta.append(new StringBuilder().append(formatter.format(new Date())).append(log).append("\n").toString());

	}

	public DownloadManager(String savePath, int threadNum, String urlFile) {
		super();
		this.savePath = savePath;
		this.threadNum = threadNum;
		this.urlFile = urlFile;
	}

	// 最终调用线程下载。本线程中调用分线程。
	public void action() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		long t1 = System.currentTimeMillis();
		if (!isStarted) {// 如果没有下载 ， 就开始 ， 并且将已经下载的变量值设为true
			startDownload();
			isStarted = true;
		}
		while (true) {
			boolean finish = true;// 初始化认为所有线程下载完成，逐个检查
			for (DownloadThread thread : downloadList) {// 如果有任何一个没完成，说明下载没完成，不能合并文件
				if (!thread.isFinish()) {
					finish = false;
					break;
				}
			}
			if (finish) {// 全部下载完成才为true
				 mergeFiles();// 合并文件
				break;// 跳出循环 ， 下载结束
			}

			try {
				Thread.sleep(1000);// 休息一会 ， 减少cpu消耗
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long t2 = System.currentTimeMillis();
		PrintLog("[INFO]:Download Time Consuming：" + (t2 -t1)/1000 + " S");
	}

	public void startDownload() {
		int[][] posAndLength = getPosAndLength();// 得到每个线程开始值 ， 下载字节数大小
		for (int i = 0; i < posAndLength.length; i++) {// 根据下载信息创建每个下载线程，并且启动他们。
			int pos = posAndLength[i][0];
			int length = posAndLength[i][1];
			DownloadThread downloadThread = new DownloadThread(i + 1, length,
				pos, savePath, urlFile);
			new Thread(downloadThread).start();
			downloadList.add(downloadThread);
		}
	}

	public long getFileLength() {
		PrintLog("[INFO]:Getting File Size...");
		HttpURLConnection conn = null;
		long result = 0;
		try {
			URL url = new URL(urlFile);
			conn = (HttpURLConnection) url.openConnection();
			// 使用Content-Length头信息获得文件大小
			result = Long.parseLong(conn.getHeaderField("Content-Length"));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		PrintLog("[INFO]:Get The File Size Is " + result);
		return result;
	}

	// 具体细节求出每个线程的开始位置和文件下载大小
	public int[][] getPosAndLength() {
		int[][] result = new int[threadNum][2];
		int fileLength = (int) getFileLength();
		int every = fileLength % threadNum == 0 ? fileLength / threadNum
			: fileLength / threadNum + 1;
		for (int i = 0; i < result.length; i++) {
			int length = 0;
			if (i != result.length - 1) {
				length = every;
			} else {
				length = fileLength - i * every;
			}
			result[i][0] = i * every;
			result[i][1] = length;
		}
		return result;
	}

	// 合并文件
	public void mergeFiles() {
		PrintLog("[INFO]:Merge Files...");
		OutputStream out = null;
		try {
			out = new FileOutputStream(savePath);
			for (int i = 1; i <= threadNum; i++) {
				InputStream in = new FileInputStream(savePath + i);
				byte[] bytes = new byte[2048];
				int read = 0;
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
					out.flush();
				}
				if (in != null) {
					in.close();
					new File(savePath + i).delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		PrintLog("[INFO]:Merge Files Succeeded");
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public String getUrlFile() {
		return urlFile;
	}

	public void setUrlFile(String urlFile) {
		this.urlFile = urlFile;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public List<DownloadThread> getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(List<DownloadThread> downloadList) {
		this.downloadList = downloadList;
	}
}
