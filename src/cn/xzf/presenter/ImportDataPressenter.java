package cn.xzf.presenter;

import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import cn.xzf.service.ReadExcelService;
import cn.xzf.ui.ImportDataViewer;
import cn.xzf.ui.listener.ImportDataViewListener;

public class ImportDataPressenter implements ImportDataViewListener {
	private List<List<Object>> tableModelDatas = null; //表格数据
	private List<List<Object>> databaseDatas = null; //数据库查询出的数据
	private ImportDataViewer viewer = null;
	private ReadExcelService readExcelService = null;
	
	public ImportDataPressenter(ImportDataViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void queryDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickOpenFileButton() {
		viewer.openFileDialog();
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
				Vector<Object> columnNames = new Vector<Object>(tableModelDatas.get(0));
				tableModelDatas.remove(0);
				Vector<Vector<Object>> datas = new Vector<Vector<Object>>();
				for (List<Object> list : tableModelDatas) {
					datas.add(new Vector<Object>(list));
				}
				TableModel dataModel = new DefaultTableModel(datas, columnNames);
				viewer.refreshTableData(dataModel);
				ComboBoxModel comboBoxModel = new DefaultComboBoxModel(columnNames);
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
						new Integer(1), new Integer(1), new Integer(
								numberSheet), new Integer(1)));
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

}
