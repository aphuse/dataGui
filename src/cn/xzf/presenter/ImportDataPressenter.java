package cn.xzf.presenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import cn.lzyy.db.SqlHelper;
import cn.xzf.model.ListExcelSheetModel;
import cn.xzf.service.ReadExcelService;
import cn.xzf.service.WriteExcelService;
import cn.xzf.ui.ImportDataViewer;
import cn.xzf.ui.listener.ImportDataViewListener;
import cn.xzf.ui.model.ListTableModel;

public class ImportDataPressenter implements ImportDataViewListener {
	private List<List<Object>> tableModelDatas = null; // 表格数据
	private List<Object> columnNames = null;
	private ImportDataViewer viewer = null;
	private ReadExcelService readExcelService = null;

	public ImportDataPressenter(ImportDataViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void clickOpenFileButton() {
		viewer.showExcelFileChooseDialog("选择导入的EXCEL文件");
	}

	@Override
	public void clickImportButton() {
		viewer.enableImportButton(false);
		viewer.showStatusMessage("正在导入EXCEL文件数据中...");
		viewer.showStatusProcessBar();
		SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				setProgress(0);
				importDataFromFile();
				return null;
			}

			@Override
			protected void done() {
				viewer.enableImportButton(true);
				viewer.hideStatusProcessBar();
				viewer.showStatusMessage("导入EXCEL文件数据完成");
				super.done();
			}
		};
		task.execute();
	}

	private void importDataFromFile() {
		String file = viewer.getTextFieldFileValue();
		int numberSheet = viewer.getSpinnerSheetValue();
		if (file != null && file.length() > 0) {
			tableModelDatas = readExcelService.excelToList(numberSheet - 1);
			if (tableModelDatas != null && tableModelDatas.size() > 0) {
				columnNames = tableModelDatas.get(0);
				tableModelDatas.remove(0);
				TableModel dataModel = new ListTableModel(tableModelDatas,
						columnNames);
				viewer.refreshTableData(dataModel);
				ComboBoxModel comboBoxModel = new DefaultComboBoxModel(
						new Vector<Object>(columnNames));
				viewer.refreshComboBoxColumnData(comboBoxModel);
			} else {
				viewer.refreshTableData(new DefaultTableModel());
				viewer.refreshComboBoxColumnData(new DefaultComboBoxModel());
			}
		}
	}

	public void setSpinnerSheetModel() {
		String fileName = viewer.getTextFieldFileValue();
		readExcelService = new ReadExcelService(fileName);
		viewer.showStatusMessage("正在加载EXCEL文件sheet数量...");
		viewer.showStatusProcessBar();
		SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				setProgress(0);
				int numberSheet = readExcelService.getExcelSheetCount();
				viewer.refreshSpinnerSheetModel(new SpinnerNumberModel(
						new Integer(1), new Integer(1),
						new Integer(numberSheet), new Integer(1)));
				return null;
			}

			@Override
			protected void done() {
				viewer.enableImportButton(true);
				viewer.hideStatusProcessBar();
				viewer.showStatusMessage("加载EXCEL文件sheet完成,现在可以导入EXCEL数据了");
				super.done();
			}
		};
		task.execute();
	}
	

	@Override
	public void clickAppenColumnButton() {
		String appenColumnNamesString = viewer.getTextFieldAppenColumnValue();
		String[] appStrings = appenColumnNamesString.split(",");
		if (columnNames == null) {
			columnNames = new ArrayList<Object>();
		}
		for (int i = 0; i < appStrings.length; i++) {
			columnNames.add(appStrings[i]);
		}
		TableModel dataModel = new ListTableModel(tableModelDatas, columnNames);
		viewer.refreshTableData(dataModel);
	}

	@Override
	public void clickQueryButton() {
		viewer.showStatusMessage("查询数据库数据中...");
		SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				setProgress(0);
				queryDatabase();
				return null;
			}

			@Override
			protected void done() {
				TableModel dataModel = new ListTableModel(tableModelDatas, columnNames);
				viewer.refreshTableData(dataModel);
				viewer.hideStatusProcessBar();
				viewer.showStatusMessage("查询数据库数据成功!");
				super.done();
			}
		};
		task.execute();
	}
	
	private void queryDatabase() {
		int columnIndex = viewer.getComboBoxColumnSelectedIndex();
		String SQL = viewer.getTextFieldSQLValue();
		SqlHelper sqlHelper = new SqlHelper("lzmis");
		int size = tableModelDatas.size();
		for ( int i = 0; i < size; i++) {
			List<Object> list = tableModelDatas.get(i);
			Map<String, Object> result = sqlHelper.getSingleResult(SQL,
					new Object[] { list.get(columnIndex) }, null, null);
			if (result != null) {
				list.addAll(mapToList(result));
			}
		}
	}

	private List<Object> mapToList(Map<String, Object> map) {
		List<Object> list = new ArrayList<Object>();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}

	@Override
	public void clickExportButton() {
		viewer.showExcelFileChooseDialog("选择导出的EXCEL文件");
	}

	@Override
	public void exportToExcelFile() {
		viewer.showStatusProcessBar();
		viewer.showStatusMessage("正在导出表格中的数据到EXCEL文件中...");
		final String fileName = viewer.getChooseFileName();
		ListExcelSheetModel model = new ListExcelSheetModel(tableModelDatas, columnNames);
		List<ListExcelSheetModel> datas = new ArrayList<ListExcelSheetModel>();
		datas.add(model);
		final WriteExcelService writeExcelService = new WriteExcelService(fileName, datas);
		SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				setProgress(0);
				writeExcelService.writeToFile();
				return null;
			}

			@Override
			protected void done() {
				viewer.hideStatusProcessBar();
				viewer.showStatusMessage("导出数据成功!数据文件为：" + fileName);
				super.done();
			}
		};
		task.execute();
		
	}

}
