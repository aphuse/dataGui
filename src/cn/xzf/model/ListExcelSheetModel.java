package cn.xzf.model;

import java.util.List;

public class ListExcelSheetModel extends AbstractExcelSheetModel<List<Object>> {

	public ListExcelSheetModel(List<List<Object>> modelData, List<String> columnNames) {
		super(modelData, columnNames);
	}

}
