package cn.xzf.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow {

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private BottomStatusPanel bottomStatusPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initialize();
	}

	public void run() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/images/ui.png")));
		frame.setTitle("小企鹅");
		frame.setBounds(100, 100, 946, 564);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 252, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		menuInit();
		statusBarInit();
		tabInit();
		componentEventListent();
	}

	private void tabInit() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		Component component = new ExportDataView(bottomStatusPanel);
		addTab("数据导出", component);
	}

	private void addTab(String title, Component component) {
		int i = tabbedPane.indexOfTab(title);
		if (i == -1) {
			tabbedPane.add(title, component);
			i = tabbedPane.getTabCount() - 1;
			tabbedPane.setTabComponentAt(i, new ClosedTabComponent(tabbedPane));
		}
		tabbedPane.setSelectedIndex(i);
		bottomStatusPanel.showStatusMessage("欢迎使用【" + tabbedPane.getTitleAt(i)
				+ "】功能");
	}

	private void menuInit() {
		// 菜单
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu optionMenu = new JMenu("操作");
		menuBar.add(optionMenu);

		JMenuItem dataLoadMenuItem = new JMenuItem("数据导出");
		dataLoadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component component = new ExportDataView(bottomStatusPanel);
				addTab("数据导出", component);
			}
		});
		optionMenu.add(dataLoadMenuItem);

		JMenuItem importMenuItem = new JMenuItem("数据导入");
		importMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component component = new ImportDataView(bottomStatusPanel);
				addTab("数据导入", component);
			}
		});
		optionMenu.add(importMenuItem);

		JMenuItem quitMenuItem = new JMenuItem("退出");
		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(frame, "是否关闭程序?",
						"提示", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		optionMenu.add(quitMenuItem);

	}

	private void statusBarInit() {
		bottomStatusPanel = new BottomStatusPanel();
		GridBagConstraints gbc_bottomStatusPanel = new GridBagConstraints();
		gbc_bottomStatusPanel.anchor = GridBagConstraints.PAGE_END;
		gbc_bottomStatusPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bottomStatusPanel.gridx = 0;
		gbc_bottomStatusPanel.gridy = 1;
		frame.getContentPane().add(bottomStatusPanel, gbc_bottomStatusPanel);
	}

	private void componentEventListent() {
		ChangeListener tabbedPaneChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				int selectIndex = sourceTabbedPane.getSelectedIndex();
				if (selectIndex != -1) {
					bottomStatusPanel.showStatusMessage("欢迎使用【"
							+ sourceTabbedPane.getTitleAt(selectIndex) + "】功能");
				} else {
					bottomStatusPanel.showStatusMessage(null);
				}
			}
		};
		tabbedPane.addChangeListener(tabbedPaneChangeListener);
	}

}
