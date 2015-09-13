package cn.xzf.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import cn.xzf.presenter.ExportDataPresenter;
import cn.xzf.ui.listener.BottomStatusPanelListener;
import cn.xzf.ui.listener.ExportDataViewListener;

public class ExportDataView extends JPanel implements ExportDataViewer {
	private static final long serialVersionUID = -6084035631913966310L;
	private JTextField textField_key;
	private JTextField textField_displayName;
	private JTextField textField_file;
	private JCheckBox chckbx_displayName;
	private JTextArea textArea_sql;
	private JButton button_opendir;
	private JButton btnexcel;
	private JButton button_reset;
	private JButton button_file;
	private boolean isCustomerSetDisplayName = true;
	private JPanel panel_1;

	private ExportDataViewListener viewListen = new ExportDataPresenter(this);
	private BottomStatusPanelListener bottomStatusPanelListen;
	

	public ExportDataView() {
		this(null);
	}
	
	public ExportDataView(BottomStatusPanelListener listen) {
		bottomStatusPanelListen = listen;
		init();
	}
	
	public void setBottomStatusPanelListen(BottomStatusPanelListener listen) {
		bottomStatusPanelListen = listen;
	}

	// 初始化窗体
	private void init() {
		GridBagLayout gbl_frame = new GridBagLayout();
		gbl_frame.columnWidths = new int[] { 335, 62, 93, 71, 101, 60, 205, 0 };
		gbl_frame.rowHeights = new int[] { 202, 5, 138, 50, 32, 0 };
		gbl_frame.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		gbl_frame.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gbl_frame);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "设置查询SQL语句",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 7;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);

		textArea_sql = new JTextArea();
		textArea_sql.setToolTipText("输入正确的查询SQL语句");
		textArea_sql.setWrapStyleWord(true);
		textArea_sql.setLineWrap(true);
		panel.add(textArea_sql);

		JScrollPane scrollPane = new JScrollPane(textArea_sql);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addGap(4)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								837, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addGap(4)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								166, Short.MAX_VALUE).addGap(3)));
		panel.setLayout(gl_panel);

		JPanel _data_head_panel = new JPanel();
		_data_head_panel.setBorder(new TitledBorder(null, "设置导出数据表头属性",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc__data_head_panel = new GridBagConstraints();
		gbc__data_head_panel.fill = GridBagConstraints.BOTH;
		gbc__data_head_panel.insets = new Insets(0, 0, 5, 0);
		gbc__data_head_panel.gridwidth = 7;
		gbc__data_head_panel.gridx = 0;
		gbc__data_head_panel.gridy = 2;
		add(_data_head_panel, gbc__data_head_panel);
		GridBagLayout gbl__data_head_panel = new GridBagLayout();
		gbl__data_head_panel.columnWidths = new int[] { 89, 740, 42, 0 };
		gbl__data_head_panel.rowHeights = new int[] { 23, 0, 23, 21, 0 };
		gbl__data_head_panel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl__data_head_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		_data_head_panel.setLayout(gbl__data_head_panel);

		chckbx_displayName = new JCheckBox("设置导出表显示列名默认为SQL语句返回的列名");
		GridBagConstraints gbc_chckbx_displayName = new GridBagConstraints();
		gbc_chckbx_displayName.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbx_displayName.anchor = GridBagConstraints.NORTH;
		gbc_chckbx_displayName.insets = new Insets(0, 0, 5, 5);
		gbc_chckbx_displayName.gridwidth = 2;
		gbc_chckbx_displayName.gridx = 0;
		gbc_chckbx_displayName.gridy = 0;
		_data_head_panel.add(chckbx_displayName, gbc_chckbx_displayName);

		JLabel _key_label = new JLabel(
				"显示列KEY：");
		_key_label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc__key_label = new GridBagConstraints();
		gbc__key_label.fill = GridBagConstraints.BOTH;
		gbc__key_label.insets = new Insets(0, 0, 5, 5);
		gbc__key_label.gridx = 0;
		gbc__key_label.gridy = 1;
		_data_head_panel.add(_key_label, gbc__key_label);

		textField_key = new JTextField();
		textField_key.setToolTipText("输入SQL语句返回的列名，使用逗号分隔");
		GridBagConstraints gbc_textField_key = new GridBagConstraints();
		gbc_textField_key.anchor = GridBagConstraints.NORTH;
		gbc_textField_key.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_key.insets = new Insets(0, 0, 5, 0);
		gbc_textField_key.gridwidth = 2;
		gbc_textField_key.gridx = 1;
		gbc_textField_key.gridy = 1;
		_data_head_panel.add(textField_key, gbc_textField_key);
		textField_key.setColumns(10);

		JLabel _displayname_label = new JLabel(
				"显示列名称：");
		_displayname_label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc__displayname_label = new GridBagConstraints();
		gbc__displayname_label.fill = GridBagConstraints.HORIZONTAL;
		gbc__displayname_label.insets = new Insets(0, 0, 5, 5);
		gbc__displayname_label.gridx = 0;
		gbc__displayname_label.gridy = 2;
		_data_head_panel.add(_displayname_label, gbc__displayname_label);

		textField_displayName = new JTextField();
		textField_displayName
				.setToolTipText("输入EXCEL表格显示列名称，顺序与返回列一一对应，使用逗号分隔");
		GridBagConstraints gbc_textField_displayName = new GridBagConstraints();
		gbc_textField_displayName.anchor = GridBagConstraints.NORTH;
		gbc_textField_displayName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_displayName.insets = new Insets(0, 0, 5, 0);
		gbc_textField_displayName.gridwidth = 2;
		gbc_textField_displayName.gridx = 1;
		gbc_textField_displayName.gridy = 2;
		_data_head_panel.add(textField_displayName, gbc_textField_displayName);
		textField_displayName.setColumns(10);

		JLabel _export_file_name_label = new JLabel(
				"导出文件名：");
		_export_file_name_label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc__export_file_name_label = new GridBagConstraints();
		gbc__export_file_name_label.fill = GridBagConstraints.HORIZONTAL;
		gbc__export_file_name_label.insets = new Insets(0, 0, 0, 5);
		gbc__export_file_name_label.gridx = 0;
		gbc__export_file_name_label.gridy = 3;
		_data_head_panel.add(_export_file_name_label,
				gbc__export_file_name_label);

		textField_file = new JTextField();
		textField_file.setToolTipText("导出EXCEL文件名");
		textField_file.setEditable(false);
		GridBagConstraints gbc_textField_file = new GridBagConstraints();
		gbc_textField_file.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_file.insets = new Insets(0, 0, 0, 5);
		gbc_textField_file.gridx = 1;
		gbc_textField_file.gridy = 3;
		_data_head_panel.add(textField_file, gbc_textField_file);
		textField_file.setColumns(10);

		button_file = new JButton("...");
		button_file.setToolTipText("选择导出的EXCEL文件");
		GridBagConstraints gbc_button_file = new GridBagConstraints();
		gbc_button_file.anchor = GridBagConstraints.NORTH;
		gbc_button_file.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_file.gridx = 2;
		gbc_button_file.gridy = 3;
		_data_head_panel.add(button_file, gbc_button_file);

		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 7;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 227, 159, 0, 0, 20, 0 };
		gbl_panel_1.rowHeights = new int[] { 45, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		button_opendir = new JButton("打开导出EXCEL文件目录");
		GridBagConstraints gbc_button_opendir = new GridBagConstraints();
		gbc_button_opendir.insets = new Insets(0, 0, 0, 5);
		gbc_button_opendir.gridx = 1;
		gbc_button_opendir.gridy = 0;
		panel_1.add(button_opendir, gbc_button_opendir);

		button_reset = new JButton("重置参数");
		GridBagConstraints gbc_button_reset = new GridBagConstraints();
		gbc_button_reset.insets = new Insets(0, 0, 0, 5);
		gbc_button_reset.gridx = 2;
		gbc_button_reset.gridy = 0;
		panel_1.add(button_reset, gbc_button_reset);
		button_reset.setToolTipText("清空所有的参数值");

		btnexcel = new JButton("导出EXCEL");
		GridBagConstraints gbc_btnexcel = new GridBagConstraints();
		gbc_btnexcel.insets = new Insets(0, 0, 0, 5);
		gbc_btnexcel.gridx = 3;
		gbc_btnexcel.gridy = 0;
		panel_1.add(btnexcel, gbc_btnexcel);
		btnexcel.setToolTipText("导出EXCEL文件");

		componentEventListent();
	}

	/**
	 * 禁用事件响应
	 */
	public void eventDisable() {
		textArea_sql.setEnabled(false);
		textArea_sql.setFocusable(false);
		textField_displayName.setEnabled(false);
		textField_displayName.setFocusable(false);
		textField_key.setEnabled(false);
		textField_key.setFocusable(false);
		chckbx_displayName.setEnabled(false);
		button_reset.setEnabled(false);
		button_file.setEnabled(false);
		btnexcel.setEnabled(false);
		button_opendir.setEnabled(false);
		textField_file.setEnabled(false);
	}

	public void eventEnsable() {
		btnexcel.setEnabled(true);
		textArea_sql.setEnabled(true);
		textArea_sql.setFocusable(true);
		textField_displayName.setEnabled(true);
		textField_displayName.setFocusable(true);
		textField_key.setEnabled(true);
		textField_key.setFocusable(true);
		chckbx_displayName.setEnabled(true);
		button_reset.setEnabled(true);
		button_file.setEnabled(true);
		button_opendir.setEnabled(true);
		textField_file.setEnabled(true);
	}

	/**
	 * 显示提示信息
	 */
	public void showMessage(String message) {
		if (bottomStatusPanelListen != null) {
			bottomStatusPanelListen.showStatusMessage(message);
		}
	}

	public void displayNameChckbxChange() {
		if (chckbx_displayName.isSelected()) {
			textField_key.setEditable(false);
			textField_displayName.setEditable(false);
			textField_key.setFocusable(false);
			textField_displayName.setFocusable(false);
			isCustomerSetDisplayName = false;
		} else {
			textField_key.setEditable(true);
			textField_displayName.setEditable(true);
			textField_key.setFocusable(true);
			textField_displayName.setFocusable(true);
			isCustomerSetDisplayName = true;
		}
	}

	public boolean enableExport() {
		boolean flag = chckbx_displayName.isSelected();
		String sql = textArea_sql.getText();
		String file = textField_file.getText();
		String key = textField_key.getText();
		String displayName = textField_displayName.getText();

		if (sql == null || sql.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "请设置运行的SQL语句");
			showMessage("请设置运行的SQL语句");
			return false;
		}
		if (flag) {
			displayName = "";
		} else {
			if (key == null || key.trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "请设置显示列KEY值");
				showMessage("请设置显示列KEY值");
				return false;
			}
			if (displayName == null || displayName.trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "请设置显示列名称");
				showMessage("请设置显示列名称");
				return false;
			}
			if (key.split(",").length != displayName.split(",").length) {
				JOptionPane
						.showMessageDialog(this, "显示列的名称与显示列的KEY值不一致，请重新设置");
				showMessage("显示列的名称与显示列的KEY值不一致，请重新设置");
				return false;
			}
		}
		if (file == null || file.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "请选择保存导出的EXCEL文件");
			showMessage("请选择保存导出的EXCEL文件");
			return false;
		}
		return true;
	}

	public void clear() {
		chckbx_displayName.setSelected(false);
		textArea_sql.setText("");
		textField_key.setText("");
		textField_key.setEditable(true);
		textField_displayName.setText("");
		textField_displayName.setEditable(true);
		textField_file.setText("");
		showMessage("参数已经清空，请重新设置");
	}

	public void openDataDir() {
		if (!button_opendir.isEnabled()) {
			return;
		}
		String filePath = textField_file.getText();
		if (filePath == null || filePath.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "未选择导出文件!");
			return;
		}
		File file = new File(textField_file.getText());
		try {
			java.awt.Desktop.getDesktop().open(file.getParentFile());
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "打开目录异常，请检查选择导出文件名是否正确");
		}
	}

	public void showFileChoseDialog() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("请选择导出EXCEL文件");
		jfc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Excel 工作薄(*.xlsx)";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory() || f.getName().endsWith(".xlsx")) {
					return true;
				}
				return false;
			}
		});
		jfc.setCurrentDirectory(new File("./data"));
		int i = jfc.showSaveDialog(this);
		if (i == 0) {
			String file = jfc.getSelectedFile().getAbsolutePath();
			if (!file.endsWith(".xlsx")) {
				file = file + ".xlsx";
			}
			textField_file.setText(file);
		}
	}

	public void showProgress() {
		if (bottomStatusPanelListen != null) {
			bottomStatusPanelListen.showStatusProcessBar();
		}
	}

	public void hideProgress() {
		if (bottomStatusPanelListen != null) {
			bottomStatusPanelListen.hideStatusProcessBar();
		}
	}

	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public String getSql() {
		return textArea_sql.getText();
	}

	public String getFile() {
		return textField_file.getText();
	}

	public String getKey() {
		return textField_key.getText();
	}

	public String getDisplayName() {
		return textField_displayName.getText();
	}
	
	public boolean IsCustomerSetDisplayName() {
		return isCustomerSetDisplayName;
	}

	// 添加组件的监听事件
	private void componentEventListent() {
		textArea_sql.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				viewListen.textAreaSqlFocus();
			}
		});

		chckbx_displayName.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				viewListen.displayNameChckbxChange();
			}
		});

		textField_key.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				viewListen.textFieldKeyFocus();
			}
		});

		textField_displayName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				viewListen.textFieldDisplayNameFocus();
			}
		});

		button_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewListen.buttonFileAction();
			}
		});

		button_file.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!button_file.isEnabled()) {
					return;
				}
				viewListen.buttonFileMouseClicked();
			}
		});

		button_reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!button_reset.isEnabled()) {
					return;
				}
				int option = JOptionPane.showConfirmDialog(getParent(), "是否清空设置的参数?",
						"提示", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					viewListen.buttonResetMouseClicked();
				}
			}
		});

		btnexcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!btnexcel.isEnabled() || !enableExport()) {
					return;
				}
				//label_statusMessage.setFocusable(true);
				viewListen.buttonExcelMouseClicked();
			}
		});

		button_opendir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!button_opendir.isEnabled()) {
					return;
				}
				openDataDir();
			}
		});

	}

}
