package cn.xzf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import jodd.io.FileNameUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xzf.model.ListExcelSheetModel;

public class WriteExcelService {
	private static final Logger logger = LoggerFactory
			.getLogger(WriteExcelService.class);
	private List<ListExcelSheetModel> datas;
	private List<String> sheetNames;
	private int sheetCount = 0;
	private File file;
	private String excelType;

	public WriteExcelService(String fileName) {
		this(fileName, null);
	}

	public WriteExcelService(String fileName, List<ListExcelSheetModel> datas) {
		this(fileName, datas, null);
	}

	public WriteExcelService(String fileName, List<ListExcelSheetModel> datas,
			List<String> sheetNames) {
		this.datas = datas;
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
		if (isXLSXFile()) {
			workbook = new SXSSFWorkbook(-1);
		} else if (isXLSile()) {
			workbook = new HSSFWorkbook();
		} else {
			new RuntimeException("当前使用的文件格式未支持！");
		}
		for (int i = 0; i < sheetCount; i++) {
			Sheet sheet = null;
			int rowCount = 0;
			int columnCount = 0;
			if (sheetNames != null && i < sheetNames.size()) {
				sheet = workbook.createSheet(sheetNames.get(i));
			} else {
				sheet = workbook.createSheet();
			}

			// sheet数据处理
			if (datas != null && datas.size() > i) {
				ListExcelSheetModel sheetData = datas.get(i);
				// 处理表头
				if (sheetData.getColumnCount() > 0) {
					List<String> columnName = sheetData.getColumnNames();
					Row row = sheet.createRow(rowCount);
					rowCount++;
					columnCount = columnName.size();
					for (int j = 0; j < columnCount; j++) {
						row.createCell(j).setCellValue(columnName.get(j));
					}
				}

				// 处理数据记录
				int count = sheetData.getRowCount();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						Row row = sheet.createRow(rowCount);
						rowCount++;
						List<Object> rowData = sheetData.getRow(j);
						if (columnCount < rowData.size()) {
							columnCount = rowData.size();
						}
						for (int k = 0; k < rowData.size(); k++) {
							setCellValue(row.createCell(k), rowData.get(k));
						}
						if (isXLSXFile()) {
							if (j % 2000 == 0) {
								try {
									((SXSSFSheet) sheet).flushRows(2000);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} else if (isXLSile()) {
							if (j >= 65534) {
								logger.error(
										"文件{}中的sheet名为【{}】中的数据记录为{}"
												+ "已经超过65535条记录，超过的数据记录已经丢失，请重新处理！",
										new Object[] { file,
												sheet.getSheetName(), count });
								j = count;
							}
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
			if (isXLSXFile()) {
				((SXSSFWorkbook) workbook).dispose();
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getPath();
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

	private boolean isXLSXFile() {
		return "xlsx".equalsIgnoreCase(excelType);
	}

	private boolean isXLSile() {
		return "xls".equalsIgnoreCase(excelType);
	}

}
