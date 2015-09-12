package cn.xzf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lzyy.db.SqlHelper;
import cn.lzyy.utils.ExeclUtils;

public class ExportExcelProgress {
	private static final Logger logger = LoggerFactory
			.getLogger(ExportExcelProgress.class);
	private String file;
	private String sql;
	private String displayName;
	private String keyName;
	private String separator;
	private String dbtype;
	
	public ExportExcelProgress(String file,String sql,String displayName,String keyName,String separator,String dbtype){
		this.file = file;
		this.sql = sql;
		this.displayName = displayName;
		this.keyName = keyName;
		this.separator = separator;
		this.dbtype = dbtype;
	}
	
	public boolean export(){
		return export(file, sql, displayName, keyName, separator, dbtype);
	}
	
	private boolean export(String file,String sql,String displayName,String keyName,String separator,String dbtype) {
		boolean flag = true;
		SqlHelper sqlHelper = new SqlHelper(dbtype);
		Object[] params = null;
		List<Map<String,Object>> results = sqlHelper.getListResults(sql, params, null, null);
		//sqlHelper.getListResults(sql, params);
		List<Map<String, String>> header = null;
		if (displayName != null) {
			header = getExcelHeaders(displayName,keyName,separator);
		}
		logger.info("表头信息：{}",header);
		logger.info("开始导出文件{}，开始时间为：{}",new Object[]{file,new java.util.Date()});
		ExeclUtils.export(results, file, header);
		logger.info("结束导出文件{}，结束时间为：{}",new Object[]{file,new java.util.Date()});
		return flag;
	}
	
	public List<Map<String, String>> getExcelHeaders(String displayNames,String keyNames,String separator) {
    	if (displayNames == null || displayNames.length()==0) {
			displayNames = keyNames;
		}
    	if (displayNames == null || displayNames.length()==0) {
			return null;
		}
    	if (separator == null || separator.length()==0) {
			separator = ",";
		}
    	String[] displayName = displayNames.split(separator);
    	String[] keyName = keyNames.split(separator);
    	if (displayName.length != keyName.length) {
    		logger.error("传入的显示名称为：{} \n传入的key值为:{} \n显示的名称与key值不一致!",new Object[]{displayName,keyName});
    		throw new RuntimeException("显示名称与值的长度不一致!");
		} else {
			List<Map<String, String>> headers = new ArrayList<Map<String,String>>(displayName.length);
			for (int i = 0; i < displayName.length; i++) {
				Map<String,String> header = new HashMap<String, String>();
				header.put("dispayName", displayName[i]);
				header.put("name", keyName[i]);
				headers.add(header);
			}
			return headers;
		}
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
