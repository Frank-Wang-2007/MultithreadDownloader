# MultithreadDownloader

#####                                                                                                                                                        																					&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;——一个基于Java的简易https协议多线程下载器

---

### 简介

**开发平台**：Java

**支持操作系统**：Windows（exe或jar，exe安装包自带jre，jar需要安装jre环境），MacOS（jar，需要jre环境）），Linux（jar，需要jre环境）

**依赖库**：commons-lang，javax.Swing，java.awt等

**使用的工具**：jdk（开发环境），jre（运行环境），IntelliJ IDEA 2020.3 （集成开发环境IDE），exe4j（exe编译工具），NSIS（exe格式安装包制作工具），Typora（markdown编辑器），Sublime Text（文本编辑器），Git（版本管理工具）

**支持下载协议**：https，理论上也支持http/TCP/ftp但没有实测

**是否支持多线程**：是

**是否开源**：是

**开源协议**：BSD协议

**开源地址**：https://github.com/Frank-Wang-2007/MultithreadDownloader

**JDK版本**：1.8

***

ps：部分文件因为SSL证书的原因可能无法下载。另外，由于GUI使用的是Swing组件，全屏可能导致显示不正常，后续可能改用JavaFX，无GUI版本没有影响。

***

### 源码示例部分（以有图形界面的GUI版本为准）

**主类**

~~~Java
package com.frank;

import java.io.File;

public class main {
	public static void main(String[] args) {
		File file = new File("./Download/");
		if (!file.exists()){
			file.mkdirs();
		}
		GUI gui = new GUI();
	}
}

~~~

**覆盖的run()方法**

~~~Java
@Override
	public void run() {
		HttpURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			PrintLog("[INFO]:InitThread：" + whichThread);
			URL fileUrl = new URL(url);
			conn = (HttpURLConnection) fileUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Range", "bytes=" + startPosition + "-");
			conn.connect();
			in = conn.getInputStream();
			out = new FileOutputStream(savePath + whichThread);
			byte[] bytes = new byte[4096];
			int count = 0;
			int read = 0;
			while ((read = in.read(bytes)) != -1) {
				if (length - count < bytes.length) {
					
					read = length - count;
				}
				out.write(bytes, 0, read);
				count = count + read;
				
				if (count == length) {
					break;
				}
			}
			isFinish = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
~~~

---

### BB两句

本来是想要加入日志系统的，但是由于Java自带的Logger生成的log可读性太差，~~log4j我又不会用~~，时间原因也没法自己开发出一个简易的Log框架，就只能用System.out.println();凑合一下了，有时间会把GUI组件从Swing换成更美观的JavaFX，也会加上Log系统，也可能适配一些其他协议比如ed2k，BT等



