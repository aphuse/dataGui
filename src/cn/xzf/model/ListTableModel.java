package cn.xzf.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -7030058013254307109L;
	private List<List<Object>> data;
	private List<String> columnNames;
	private int columnCount = 0;

	public ListTableModel(List<List<Object>> data) {
		this(data,null);
	}
	
	public ListTableModel(List<List<Object>> data, List<String> columns) {
		if (data == null) {
			this.data = new ArrayList<List<Object>>();
		} else {
			this.data = data;
		}
		columnNames = columns;
		if (columnNames == null) {
			if (data.size()>0) {
				columnCount = data.get(0).size();
			}
		} else {
			columnCount = columnNames.size();
		}
	}

	@Override
	public String getColumnName(int column) {
		if (columnNames != null) {
			return columnNames.get(column);
		} else {
			return super.getColumnName(column);
		}
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

}
