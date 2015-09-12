package cn.xzf.presenter;

import javax.swing.SwingWorker;

import cn.xzf.service.ExportExcelProgress;
import cn.xzf.ui.ExportDataViewer;
import cn.xzf.ui.listener.ExportDataViewListener;

public class ExportDataPresenter implements ExportDataViewListener {
	
	private ExportDataViewer view;
	
	public ExportDataPresenter(ExportDataViewer view) {
		this.view = view;
	}

	public void textAreaSqlFocus() {
		view.showMessage("������ȷ�Ĳ�ѯSQL���");
	}

	public void displayNameChckbxChange() {
		view.displayNameChckbxChange();
	}

	public void textFieldKeyFocus() {
		view.showMessage("����SQL���ص�������ʹ�ö��ŷָ�");
	}

	public void textFieldDisplayNameFocus() {
		view.showMessage("���õ���EXCEL���ݱ�ͷ����ʾ��������˳����SQL������ֵһһ��Ӧ��ʹ�ö��ŷָ�");
	}

	public void buttonFileAction() {
		view.showMessage("��ѡ�񵼳�������EXCEL�ļ���");
	}

	public void buttonFileMouseClicked() {
		view.showFileChoseDialog();
	}

	public void buttonResetMouseClicked() {
		view.clear();
	}

	public void buttonExcelMouseClicked() {
		//�����������ؼ����ò�����
		view.eventDisable();
		String sql = view.getSql();
		String file = view.getFile();
		String key = view.getKey();
		String displayName = view.getDisplayName();
		if (!view.IsCustomerSetDisplayName()) {
			displayName = null;
			key = null;
		}
		
		final ExportExcelProgress eep = new ExportExcelProgress(file, sql, displayName, key, ",", "lzmis");
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
						view.showMessage("���ݵ����Ѿ����");
						view.showMessageDialog("���ݵ����Ѿ����");
					}
				} catch (Exception e) {
					view.showMessage("���ݵ����쳣��������־");
					view.showMessageDialog("���ݵ����쳣��\n" + e.getMessage());
				} 
				
			}
		};
		view.showProgress();
		task.execute();
		view.showMessage("���ڵ������ݵ�EXCEL�ļ��������ĵȴ�...");
	}

	public void buttonOpenDirMouseClicked() {
		view.openDataDir();
	}

}
