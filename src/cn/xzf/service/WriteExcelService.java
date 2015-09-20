package cn.xzf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jodd.io.FileNameUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class WriteExcelService {
	private List<List<List<Object>>> datas;
	private List<List<String>> columnNames;
	private List<String> sheetNames;
	private int sheetCount = 0;
	private File file;
	private String excelType;

	public WriteExcelService(String fileName) {
		this(fileName, null);
	}

	public WriteExcelService(String fileName, List<List<List<Object>>> datas) {
		this(fileName, datas, null);
	}

	public WriteExcelService(String fileName, List<List<List<Object>>> datas,
			List<List<String>> columnNames) {
		this(fileName, datas, columnNames, null);
	}

	public WriteExcelService(String fileName, List<List<List<Object>>> datas,
			List<List<String>> columnNames, List<String> sheetNames) {
		this.datas = datas;
		this.columnNames = columnNames;
		this.sheetNames = sheetNames;
		file = new File(fileName);
		excelType = FileNameUtil.getExtension(file.getName()).toLowerCase();
		if (sheetNames != null) {
			sheetCount = sheetNames.size();
		}
		if (datas != null && datas.size() > sheetCount) {
			sheetCount = datas.size();
		}
	}

	public String writeToFile() {
		Workbook workbook = null;
		int rowCount = 0;
		if ("xlsx".equalsIgnoreCase(excelType)) {
			workbook = new SXSSFWorkbook(-1);

		}
		for (int i = 0; i < sheetCount; i++) {
			Sheet sheet = null;
			int columnCount = 0;
			if (sheetNames != null && i < sheetNames.size()) {
				sheet = workbook.createSheet(sheetNames.get(i));
			} else {
				sheet = workbook.createSheet();
			}
			// 处理表头
			if (columnNames != null && i < columnNames.size()) {
				List<String> columnName = columnNames.get(i);
				if (columnName != null && columnName.size() > 0) {
					Row row = sheet.createRow(rowCount);
					rowCount++;
					columnCount = columnName.size();
					for (int j = 0; j < columnCount; j++) {
						row.createCell(j).setCellValue(columnName.get(j));
					}
				}
			}

			if (datas != null && datas.size() > i) {
				List<List<Object>> data = datas.get(i);
				int count = data.size();
				for (int j = 0; j < count; j++) {
					Row row = sheet.createRow(rowCount);
					rowCount++;
					List<Object> rowData = data.get(j);
					if (columnCount < rowData.size()) {
						columnCount = rowData.size();
					}
					for (int k = 0; k < rowData.size(); k++) {
						setCellValue(row.createCell(k), rowData.get(k));
					}
					if (j % 2000 == 0) {
						try {
							((SXSSFSheet) sheet).flushRows(2000);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// 生成数据内容结束

			for (int j = 0; j <= columnCount; j++) {
				sheet.autoSizeColumn(j);
			}

		}

		// 生成excel文件
		try {
			OutputStream os = new FileOutputStream(file);
			workbook.write(os);
			os.close();
			((SXSSFWorkbook) workbook).dispose();
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getPath();
	}

	/**
	 * 导出excel表格
	 * 
	 * @param datas
	 *            导出的数据
	 * @param file
	 *            文件名
	 * @param header
	 *            表头信息[{"dispayName":"显示名称1","name":"字段KEY1"},{"dispayName":
	 *            "显示名称1","name":"字段KEY1"}...]
	 * @return 导出的文件
	 */
	public String export(List<Map<String, Object>> datas, String file,
			List<Map<String, String>> header) {
		if ((datas == null || datas.size() == 0 || datas.get(0) == null)
				&& (header == null || header.isEmpty())) {
			return null;
		}
		Workbook workbook = new SXSSFWorkbook(-1);
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		// 生成表头开始
		if (header == null || header.isEmpty()) {
			if (header == null) {
				header = new ArrayList<Map<String, String>>();
			}
			Map<String, Object> map = datas.get(0);
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				String key = entry.getKey() + "";
				Map<String, String> hd = new HashMap<String, String>();
				hd.put("dispayName", key);
				hd.put("name", key);
				header.add(hd);
			}
		}

		for (int i = 0; i < header.size(); i++) {
			row.createCell(i).setCellValue(header.get(i).get("dispayName"));
		}
		// 生成表头结束
		// 生成数据内容开始
		int j = 1;
		for (Iterator<Map<String, Object>> iterator = datas.iterator(); iterator
				.hasNext();) {
			row = sheet.createRow(j++);
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			for (int i = 0; i < header.size(); i++) {
				// row.createCell(i).setCellValue(map.get(header[i]) + "");
				setCellValue(row.createCell(i),
						map.get(header.get(i).get("name")));
			}
			if (j % 2000 == 0) {
				try {
					((SXSSFSheet) sheet).flushRows(2000);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 生成数据内容结束

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		// 生成excel文件
		try {
			OutputStream os = new FileOutputStream(new File(file));
			workbook.write(os);
			os.close();
			((SXSSFWorkbook) workbook).dispose();
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			return;
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellValue(value + "");
		}
	}

}
