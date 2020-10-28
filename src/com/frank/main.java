package com.frank;

import java.io.File;

public class main {
	public static Logger logger = new Logger("./Logs");
	public static void main(String[] args) {
		File file = new File("./Download/");
		if (!file.exists()){
			file.mkdirs();
		}
		GUI gui = new GUI();
	}
}
