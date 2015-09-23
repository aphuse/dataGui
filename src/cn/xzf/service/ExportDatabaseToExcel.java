package cn.xzf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lzyy.db.SqlHelper;
import cn.xzf.model.ListExcelSheetModel;

public class ExportDatabaseToExcel {
	private static final Logger logger = LoggerFactory
			.getLogger(ExportDatabaseToExcel.class);
	private String file;
	private String sql;
	private String displayName;
	private String keyName;
	private String separator;
	private String dbtype;

	public ExportDatabaseToExcel(String file, String sql, String displayName,
			String keyName, String separator, String dbtype) {
		this.file = file;
		this.sql = sql;
		this.displayName = displayName;
		this.keyName = keyName;
		this.separator = separator;
		this.dbtype = dbtype;
	}

	public boolean export() {
		return export(file, sql, displayName, keyName, separator, dbtype);
	}

	private boolean export(String file, String sql, String displayName,
			String keyName, String separator, String dbtype) {
		boolean flag = true;
		SqlHelper sqlHelper = new SqlHelper(dbtype);
		Object[] params = null;
		List<Map<String, Object>> results = sqlHelper.getListResults(sql,
				params, null, null);
		List<Object> header = null;
		if (displayName != null) {
			header = getExcelHeaders(displayName, separator);
		} else {
			header = getExcelHeadersByData(results);
		}
		List<ListExcelSheetModel> datas = new ArrayList<ListExcelSheetModel>();
		ListExcelSheetModel data = new ListExcelSheetModel(getExcelDatas(
				results, keyName, separator), header);
		datas.add(data);
		WriteExcelService writeExcelService = new WriteExcelService(file, datas);
		logger.info("表头信息：{}", header);
		logger.info("开始导出文件{}，开始时间为：{}", new Object[] { file,
				new java.util.Date() });
		String fileName = writeExcelService.writeToFile();
		logger.info("结束导出文件{}，结束时间为：{}", new Object[] { fileName,
				new java.util.Date() });
		return flag;
	}

	private List<List<Object>> getExcelDatas(
			List<Map<String, Object>> sqlDatas, String keyNames,
			String separator) {
		if (separator == null || separator.length() == 0) {
			separator = ",";
		}
		List<List<Object>> results = null;
		if (sqlDatas != null) {
			results = new ArrayList<List<Object>>(sqlDatas.size());
			if (keyNames != null && keyNames.length() > 0) {
				String[] keyName = keyNames.split(separator);
				for (Map<String, Object> map : sqlDatas) {
					List<Object> result = new ArrayList<Object>(keyName.length);
					for (String string : keyName) {
						result.add(map.get(string));
					}
					results.add(result);
				}
			} else {
				for (Map<String, Object> map : sqlDatas) {
					List<Object> result = new ArrayList<Object>(map.size());
					for (Iterator<Entry<String, Object>> iterator = map
							.entrySet().iterator(); iterator.hasNext();) {
						Entry<String, Object> entry = iterator.next();
						result.add(entry.getValue());
					}
					results.add(result);
				}
			}
		}

		return results;
	}

	private List<Object> getExcelHeaders(String displayNames, String separator) {
		if (displayNames == null || displayNames.length() == 0) {
			return null;
		}
		if (separator == null || separator.length() == 0) {
			separator = ",";
		}
		Object[] displayName = displayNames.split(separator);
		List<Object> headers = Arrays.asList(displayName);
		return headers;
	}

	private List<Object> getExcelHeadersByData(
			List<Map<String, Object>> sqlDatas) {
		List<Object> headers = null;
		if (sqlDatas != null && sqlDatas.size() > 0) {
			headers = new ArrayList<Object>();
			Map<String, Object> map = sqlDatas.get(0);
			for (Iterator<Entry<String, Object>> iterator = map.entrySet()
					.iterator(); iterator.hasNext();) {
				Entry<String, Object> entry = iterator.next();
				headers.add(entry.getKey());
			}
		}
		return headers;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
}
