# MultithreadDownloader

#####                                                                                                                                                        																					&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;����һ������Java�ļ���httpsЭ����߳�������

---

### ���

**����ƽ̨**��Java

**֧�ֲ���ϵͳ**��Windows��exe��jar��exe��װ���Դ�jre��jar��Ҫ��װjre��������MacOS��jar����Ҫjre����������Linux��jar����Ҫjre������

**������**��commons-lang��javax.Swing��java.awt��

**ʹ�õĹ���**��jdk��������������jre�����л�������IntelliJ IDEA 2020.3 �����ɿ�������IDE����exe4j��exe���빤�ߣ���NSIS��exe��ʽ��װ���������ߣ���Typora��markdown�༭������Sublime Text���ı��༭������Git���汾�������ߣ�

**֧������Э��**��https��������Ҳ֧��http/TCP/ftp��û��ʵ��

**�Ƿ�֧�ֶ��߳�**����

**�Ƿ�Դ**����

**��ԴЭ��**��BSDЭ��

**��Դ��ַ**��https://github.com/Frank-Wang-2007/MultithreadDownloader

**JDK�汾**��1.8

***

ps�������ļ���ΪSSL֤���ԭ������޷����ء����⣬����GUIʹ�õ���Swing�����ȫ�����ܵ�����ʾ���������������ܸ���JavaFX����GUI�汾û��Ӱ�졣

***

### Դ��ʾ�����֣�����ͼ�ν����GUI�汾Ϊ׼��

**����**

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

**���ǵ�run()����**

~~~Java
@Override
	public void run() {
		HttpURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			PrintLog("[INFO]:InitThread��" + whichThread);
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

### BB����

��������Ҫ������־ϵͳ�ģ���������Java�Դ���Logger���ɵ�log�ɶ���̫�~~log4j���ֲ�����~~��ʱ��ԭ��Ҳû���Լ�������һ�����׵�Log��ܣ���ֻ����System.out.println();�պ�һ���ˣ���ʱ����GUI�����Swing���ɸ����۵�JavaFX��Ҳ�����Logϵͳ��Ҳ��������һЩ����Э�����ed2k��BT��


