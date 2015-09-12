package cn.xzf;

import java.awt.EventQueue;

import cn.xzf.ui.MainWindow;

public class Launch {

	public static void main(String[] args) {
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
