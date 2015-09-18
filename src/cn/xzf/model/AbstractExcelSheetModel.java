package cn.xzf.model;

import java.util.List;

public abstract class AbstractExcelSheetModel<T> {
	private List<String> columnNames;
	private List<T> modelData;

	protected AbstractExcelSheetModel(List<T> modelData,
			List<String> columnNames) {
		setDataAndColumnNames(modelData, columnNames);
	}

	public void setDataAndColumnNames(List<T> modelData,
			List<String> columnNames) {
		this.modelData = modelData;
		this.columnNames = columnNames;
	}
	
	public int getColumnCount() {
		return columnNames.size();
	}
	
	public int getRowCount() {
		return modelData.size();
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}
	
	public List<T> getModelData() {
		return modelData;
	}

}
