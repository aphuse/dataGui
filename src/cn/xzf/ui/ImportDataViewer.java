package cn.xzf.ui;

import javax.swing.ComboBoxModel;
import javax.swing.SpinnerModel;
import javax.swing.table.TableModel;

public interface ImportDataViewer {
	public void showExcelFileChooseDialog(String title);
	public void refreshTableData(TableModel dataModel);
	public String getTextFieldFileValue();
	public void showStatusMessage(String message);
	public void showStatusProcessBar();
	public void hideStatusProcessBar();
	public int getSpinnerSheetValue();
	public void refreshComboBoxColumnData(ComboBoxModel dataModel);
	public void enableImportButton(boolean enable);
	public void refreshSpinnerSheetModel(SpinnerModel model);
	public String getTextFieldSQLValue();
	public String getTextFieldAppenColumnValue();
	public int getComboBoxColumnSelectedIndex();
	public void enableQueryButton(boolean enable);
	public String getChooseFileName();
}
