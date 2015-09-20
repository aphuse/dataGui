package cn.xzf.presenter;

import javax.swing.SwingWorker;

import cn.xzf.service.ExportDatabaseToExcel;
import cn.xzf.ui.ExportDataViewer;
import cn.xzf.ui.listener.ExportDataViewListener;

public class ExportDataPresenter implements ExportDataViewListener {
	
	private ExportDataViewer view;
	
	public ExportDataPresenter(ExportDataViewer view) {
		this.view = view;
	}

	public void textAreaSqlFocus() {
		view.showMessage("设置正确的查询SQL语句");
	}

	public void displayNameChckbxChange() {
		view.displayNameChckbxChange();
	}

	public void textFieldKeyFocus() {
		view.showMessage("设置SQL返回的列名，使用逗号分隔");
	}

	public void textFieldDisplayNameFocus() {
		view.showMessage("设置导出EXCEL数据表头的显示列名，其顺序与SQL返回列值一一对应，使用逗号分隔");
	}

	public void buttonFileAction() {
		view.showMessage("请选择导出的数据EXCEL文件名");
	}

	public void buttonFileMouseClicked() {
		view.showFileChoseDialog();
	}

	public void buttonResetMouseClicked() {
		view.clear();
	}

	public void buttonExcelMouseClicked() {
		//将面板的其他控件设置不可用
		view.eventDisable();
		String sql = view.getSql();
		String file = view.getFile();
		String key = view.getKey();
		String displayName = view.getDisplayName();
		if (!view.IsCustomerSetDisplayName()) {
			displayName = null;
			key = null;
		}
		
		final ExportDatabaseToExcel eep = new ExportDatabaseToExcel(file, sql, displayName, key, ",", "lzmis");
		SwingWorker<Boolean, Void> task = new SwingWorker<Boolean, Void>(){
			@Override
			protected Boolean doInBackground() throws Exception {
				setProgress(0);
				return eep.export();
			}
			@Override
			protected void done() {
				super.done();
				view.eventEnsable();
				view.hideProgress();
				try {
					if (get()) {
						view.showMessage("数据导出已经完成");
						view.showMessageDialog("数据导出已经完成");
					}
				} catch (Exception e) {
					view.showMessage("数据导出异常，请检查日志");
					view.showMessageDialog("数据导出异常：\n" + e.getMessage());
				} 
				
			}
		};
		view.showProgress();
		task.execute();
		view.showMessage("正在导出数据到EXCEL文件，请耐心等待...");
	}

	public void buttonOpenDirMouseClicked() {
		view.openDataDir();
	}

}
