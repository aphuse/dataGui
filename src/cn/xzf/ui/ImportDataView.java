package cn.xzf.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import cn.xzf.presenter.ImportDataPressenter;
import cn.xzf.ui.listener.BottomStatusPanelListener;
import cn.xzf.ui.listener.ImportDataViewListener;

public class ImportDataView extends JPanel implements ImportDataViewer {
	private static final long serialVersionUID = 6477727595199191913L;
	private JTextField textFieldFile;
	private JTable table;
	private JButton buttonFile;
	private JPanel setPanel;
	private JButton buttonImport;
	private JSpinner spinnerSheet;
	private JLabel lblsheet;
	private JScrollPane tableScrollPane;
	private BottomStatusPanelListener statusBarListen;
	private JLabel lblcolum;
	private JComboBox comboBoxColumn;
	private JLabel lblSQL;
	private JTextField textFieldSQL;
	private JButton buttonQuery;
	private JPanel appenColumnPanel;
	private JLabel lblAppenColumn;
	private JTextField textFieldAppenColumn;
	
	private ImportDataViewListener listener = new ImportDataPressenter(this);
	private JButton buttonAppenColumn;
	private JButton buttonExport;
	private String fileName = null;

	public ImportDataView() {
		this(null);
	}

	public ImportDataView(BottomStatusPanelListener bottomStatusPanelListen) {
		statusBarListen = bottomStatusPanelListen;
		init();
		componentEventListent();
		setComponentInitStatus();
	}

	/**
	 * 
	 * 初始化面板<br>
	 * <br>
	 */
	private void init() {
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] { 0, 0 };
		gbl_mainPanel.rowHeights = new int[] { 0, 30, 27, 25, 0 };
		gbl_mainPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_mainPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gbl_mainPanel);

		JPanel headPanel = new JPanel();
		GridBagConstraints gbc_headPanel = new GridBagConstraints();
		gbc_headPanel.anchor = GridBagConstraints.NORTH;
		gbc_headPanel.insets = new Insets(0, 0, 5, 0);
		gbc_headPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_headPanel.gridx = 0;
		gbc_headPanel.gridy = 0;
		add(headPanel, gbc_headPanel);
		GridBagLayout gbl_headPanel = new GridBagLayout();
		gbl_headPanel.columnWidths = new int[] { 10, 150, 20, 0 };
		gbl_headPanel.rowHeights = new int[] { 23, 0 };
		gbl_headPanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_headPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		headPanel.setLayout(gbl_headPanel);

		JLabel lblNewLabel = new JLabel("选择数据文件：");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		headPanel.add(lblNewLabel, gbc_lblNewLabel);

		textFieldFile = new JTextField();
		textFieldFile.setEditable(false);
		GridBagConstraints gbc_textFieldFile = new GridBagConstraints();
		gbc_textFieldFile.fill = GridBagConstraints.BOTH;
		gbc_textFieldFile.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldFile.gridx = 1;
		gbc_textFieldFile.gridy = 0;
		headPanel.add(textFieldFile, gbc_textFieldFile);
		textFieldFile.setColumns(10);

		buttonFile = new JButton("...");
		buttonFile.setToolTipText("选择导入的EXCEL文件");
		GridBagConstraints gbc_buttonFile = new GridBagConstraints();
		gbc_buttonFile.anchor = GridBagConstraints.EAST;
		gbc_buttonFile.fill = GridBagConstraints.VERTICAL;
		gbc_buttonFile.gridx = 2;
		gbc_buttonFile.gridy = 0;
		headPanel.add(buttonFile, gbc_buttonFile);

		setPanel = new JPanel();
		GridBagConstraints gbc_setPanel = new GridBagConstraints();
		gbc_setPanel.anchor = GridBagConstraints.NORTH;
		gbc_setPanel.insets = new Insets(0, 0, 5, 0);
		gbc_setPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_setPanel.gridx = 0;
		gbc_setPanel.gridy = 1;
		add(setPanel, gbc_setPanel);
		GridBagLayout gbl_setPanel = new GridBagLayout();
		gbl_setPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 60, 0 };
		gbl_setPanel.rowHeights = new int[] { 0, 0 };
		gbl_setPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_setPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setPanel.setLayout(gbl_setPanel);

		lblsheet = new JLabel("选择导入的sheet：");
		GridBagConstraints gbc_lblsheet = new GridBagConstraints();
		gbc_lblsheet.insets = new Insets(0, 0, 0, 5);
		gbc_lblsheet.gridx = 0;
		gbc_lblsheet.gridy = 0;
		setPanel.add(lblsheet, gbc_lblsheet);

		spinnerSheet = new JSpinner();
		spinnerSheet.setModel(new SpinnerNumberModel(new Integer(1),
				new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinnerSheet = new GridBagConstraints();
		gbc_spinnerSheet.anchor = GridBagConstraints.WEST;
		gbc_spinnerSheet.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerSheet.gridx = 1;
		gbc_spinnerSheet.gridy = 0;
		setPanel.add(spinnerSheet, gbc_spinnerSheet);

		buttonImport = new JButton("导入数据");
		buttonImport.setEnabled(false);
		GridBagConstraints gbc_buttonImport = new GridBagConstraints();
		gbc_buttonImport.insets = new Insets(0, 0, 0, 5);
		gbc_buttonImport.anchor = GridBagConstraints.WEST;
		gbc_buttonImport.gridx = 2;
		gbc_buttonImport.gridy = 0;
		setPanel.add(buttonImport, gbc_buttonImport);
		
		buttonExport = new JButton("表格数据导出EXCEL");
		GridBagConstraints gbc_buttonExport = new GridBagConstraints();
		gbc_buttonExport.insets = new Insets(0, 0, 0, 5);
		gbc_buttonExport.gridx = 3;
		gbc_buttonExport.gridy = 0;
		setPanel.add(buttonExport, gbc_buttonExport);

		lblcolum = new JLabel("选择关联的列：");
		GridBagConstraints gbc_lblcolum = new GridBagConstraints();
		gbc_lblcolum.insets = new Insets(0, 0, 0, 5);
		gbc_lblcolum.anchor = GridBagConstraints.EAST;
		gbc_lblcolum.gridx = 4;
		gbc_lblcolum.gridy = 0;
		setPanel.add(lblcolum, gbc_lblcolum);

		comboBoxColumn = new JComboBox();
		GridBagConstraints gbc_comboBoxColumn = new GridBagConstraints();
		gbc_comboBoxColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxColumn.gridx = 5;
		gbc_comboBoxColumn.gridy = 0;
		setPanel.add(comboBoxColumn, gbc_comboBoxColumn);
		
		appenColumnPanel = new JPanel();
		GridBagConstraints gbc_appenColumnPanel = new GridBagConstraints();
		gbc_appenColumnPanel.anchor = GridBagConstraints.NORTH;
		gbc_appenColumnPanel.insets = new Insets(0, 0, 5, 0);
		gbc_appenColumnPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_appenColumnPanel.gridx = 0;
		gbc_appenColumnPanel.gridy = 2;
		add(appenColumnPanel, gbc_appenColumnPanel);
		GridBagLayout gbl_appenColumnPanel = new GridBagLayout();
		gbl_appenColumnPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_appenColumnPanel.rowHeights = new int[]{0, 0};
		gbl_appenColumnPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_appenColumnPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		appenColumnPanel.setLayout(gbl_appenColumnPanel);
		
		lblAppenColumn = new JLabel("追加列名：");
		GridBagConstraints gbc_lblAppenColumn = new GridBagConstraints();
		gbc_lblAppenColumn.insets = new Insets(0, 0, 0, 5);
		gbc_lblAppenColumn.anchor = GridBagConstraints.EAST;
		gbc_lblAppenColumn.gridx = 0;
		gbc_lblAppenColumn.gridy = 0;
		appenColumnPanel.add(lblAppenColumn, gbc_lblAppenColumn);
		
		textFieldAppenColumn = new JTextField();
		textFieldAppenColumn.setToolTipText("输入添加的列名称，多个列名称使用逗号(,)分隔");
		GridBagConstraints gbc_textFieldAppenColumn = new GridBagConstraints();
		gbc_textFieldAppenColumn.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldAppenColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldAppenColumn.gridx = 1;
		gbc_textFieldAppenColumn.gridy = 0;
		appenColumnPanel.add(textFieldAppenColumn, gbc_textFieldAppenColumn);
		textFieldAppenColumn.setColumns(10);
		
		buttonAppenColumn = new JButton("追加列名");
		GridBagConstraints gbc_buttonAppenColumn = new GridBagConstraints();
		gbc_buttonAppenColumn.anchor = GridBagConstraints.EAST;
		gbc_buttonAppenColumn.gridx = 2;
		gbc_buttonAppenColumn.gridy = 0;
		appenColumnPanel.add(buttonAppenColumn, gbc_buttonAppenColumn);

		JPanel sqlPanel = new JPanel();
		GridBagConstraints gbc_sqlPanel = new GridBagConstraints();
		gbc_sqlPanel.anchor = GridBagConstraints.NORTH;
		gbc_sqlPanel.insets = new Insets(0, 0, 5, 0);
		gbc_sqlPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_sqlPanel.gridx = 0;
		gbc_sqlPanel.gridy = 3;
		add(sqlPanel, gbc_sqlPanel);
		GridBagLayout gbl_sqlPanel = new GridBagLayout();
		gbl_sqlPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_sqlPanel.rowHeights = new int[] { 0, 0 };
		gbl_sqlPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_sqlPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		sqlPanel.setLayout(gbl_sqlPanel);
		
		lblSQL = new JLabel("输入SQL：");
		GridBagConstraints gbc_lblSQL = new GridBagConstraints();
		gbc_lblSQL.anchor = GridBagConstraints.EAST;
		gbc_lblSQL.insets = new Insets(0, 0, 0, 5);
		gbc_lblSQL.gridx = 0;
		gbc_lblSQL.gridy = 0;
		sqlPanel.add(lblSQL, gbc_lblSQL);
		
		textFieldSQL = new JTextField();
		textFieldSQL.setToolTipText("输入SQL查询语句(示例：select name from goodsj where gcode=?)，作用使用选择表格中对应列的值作为参数值进行查询");
		GridBagConstraints gbc_textFieldSQL = new GridBagConstraints();
		gbc_textFieldSQL.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldSQL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSQL.gridx = 1;
		gbc_textFieldSQL.gridy = 0;
		sqlPanel.add(textFieldSQL, gbc_textFieldSQL);
		textFieldSQL.setColumns(10);
		
		buttonQuery = new JButton("查询数据");
		GridBagConstraints gbc_buttonQuery = new GridBagConstraints();
		gbc_buttonQuery.gridx = 2;
		gbc_buttonQuery.gridy = 0;
		sqlPanel.add(buttonQuery, gbc_buttonQuery);

		JPanel _table_panel = new JPanel();
		GridBagConstraints gbc__table_panel = new GridBagConstraints();
		gbc__table_panel.fill = GridBagConstraints.BOTH;
		gbc__table_panel.gridx = 0;
		gbc__table_panel.gridy = 4;
		add(_table_panel, gbc__table_panel);
		_table_panel.setLayout(new GridLayout(0, 1, 0, 0));

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tableScrollPane = new JScrollPane(table);
		JTable rowTable = new RowNumberTable(table);
		tableScrollPane.setRowHeaderView(rowTable);
		tableScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				rowTable.getTableHeader());
		_table_panel.add(tableScrollPane);

	}

	// 添加组件的监听事件
	private void componentEventListent() {

		buttonFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!buttonFile.isEnabled()) {
					return;
				}
				listener.clickOpenFileButton();
				if (fileName != null) {
					textFieldFile.setText(fileName);
					listener.setSpinnerSheetModel();
				}
			}
		});

		buttonImport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!buttonImport.isEnabled()) {
					return;
				}
				listener.clickImportButton();
			}
		});
		
		buttonAppenColumn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!buttonAppenColumn.isEnabled()) {
					return;
				}
				listener.clickAppenColumnButton();
			}
		});
		
		buttonQuery.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!buttonQuery.isEnabled()) {
					return;
				}
				listener.clickQueryButton();
			}
		});
		
		buttonExport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!buttonExport.isEnabled()) {
					return;
				}
				listener.clickExportButton();
				if (fileName != null) {
					listener.exportToExcelFile();
				}
			}
		});
		
		textFieldAppenColumn.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				listener.inputAppenColumnTextField();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				listener.inputAppenColumnTextField();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
		});
		
		textFieldSQL.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				listener.inputSQLTextField();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				listener.inputSQLTextField();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}
	
	/**
	 * 
	 * 设置组件的初始状态<br>
	 * <br>
	 */
	private void setComponentInitStatus() {
		enableImportButton(false);
		enableAppenColumnButton(false);
		enableExportButton(false);
		enableQueryButton(false);
		setEditableAppenColumnTextField(false);
		setEditableSQLTextField(false);
	}
	
	@Override
	public void showExcelFileChooseDialog(String title) {
		fileName = null;
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(title);
		FileFilter filter = new FileNameExtensionFilter("Excel 工作薄", "xlsx",
				"xls");
		jfc.setFileFilter(filter);
		jfc.setCurrentDirectory(new File("./data"));
		int i = jfc.showOpenDialog(this);
		if (i == 0) {
			fileName = jfc.getSelectedFile().getAbsolutePath();
		}
	}

	@Override
	public void refreshTableData(TableModel dataModel) {
		table.setModel(dataModel);
	}

	@Override
	public String getTextFieldFileValue() {
		return textFieldFile.getText();
	}

	@Override
	public void showStatusMessage(String message) {
		if (statusBarListen != null) {
			statusBarListen.showStatusMessage(message);
		}
	}

	@Override
	public void showStatusProcessBar() {
		if (statusBarListen != null) {
			statusBarListen.showStatusProcessBar();
		}
	}

	@Override
	public void hideStatusProcessBar() {
		if (statusBarListen != null) {
			statusBarListen.hideStatusProcessBar();
		}
	}

	@Override
	public int getSpinnerSheetValue() {
		return Integer.valueOf(spinnerSheet.getValue().toString());
	}

	@Override
	public void refreshComboBoxColumnData(ComboBoxModel dataModel) {
		comboBoxColumn.setModel(dataModel);
	}

	@Override
	public void enableImportButton(boolean enable) {
		buttonImport.setEnabled(enable);
	}

	@Override
	public void refreshSpinnerSheetModel(SpinnerModel model) {
		spinnerSheet.setModel(model);
	}

	@Override
	public String getTextFieldSQLValue() {
		return textFieldSQL.getText();
	}

	@Override
	public String getTextFieldAppenColumnValue() {
		return textFieldAppenColumn.getText();
	}

	@Override
	public int getComboBoxColumnSelectedIndex() {
		return comboBoxColumn.getSelectedIndex();
	}

	@Override
	public void enableQueryButton(boolean enable) {
		buttonQuery.setEnabled(enable);
	}

	@Override
	public String getChooseFileName() {
		String name = fileName;
		fileName = null;
		return name;
	}

	@Override
	public void enableExportButton(boolean enable) {
		buttonExport.setEnabled(enable);
	}

	@Override
	public void enableAppenColumnButton(boolean enable) {
		buttonAppenColumn.setEnabled(enable);
	}

	@Override
	public void setEditableAppenColumnTextField(boolean enable) {
		textFieldAppenColumn.setEditable(enable);
	}

	@Override
	public void setEditableSQLTextField(boolean enable) {
		textFieldSQL.setEditable(enable);
	}

}
