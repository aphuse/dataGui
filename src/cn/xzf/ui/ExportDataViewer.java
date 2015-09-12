package cn.xzf.ui;

public interface ExportDataViewer {
	public void eventDisable();
	public void eventEnsable();
	public void showMessage(String message);
	public boolean enableExport();
	public void clear();
	public void openDataDir();
	//public void close();
	//public void showTime();
	public void showProgress();
	public void hideProgress();
	public void displayNameChckbxChange();
	public void showFileChoseDialog();
	public void showMessageDialog(String message);
	public String getSql();
	public String getFile();
	public String getKey();
	public String getDisplayName();
	public boolean IsCustomerSetDisplayName();
}
