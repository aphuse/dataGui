package cn.xzf;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import cn.xzf.ui.MainWindow;

public class Launch {

	public static void main(String[] args) throws IOException {
		File file = new File("dataGui.log");
		if (!file.exists()) {
			file.createNewFile();
		}
		PrintStream printStream = new PrintStream(file);
		System.setErr(printStream);
		System.setOut(printStream);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
