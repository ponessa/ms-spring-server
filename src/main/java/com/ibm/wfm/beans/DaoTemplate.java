package com.ibm.wfm.beans;

import java.util.ArrayList;
import java.util.List;

import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.utils.Helpers;

public class DaoTemplate {
	
	public static final int ARTIFACT_NONE=0;
	public static final int ARTIFACT_DDL = 1;
	public static final int ARTIFACT_BEAN = 2;
	
	@ExcelSheet(columnName = "Schema", columnNum = 0)
	private String schema;
	@ExcelSheet(columnName = "Name", columnNum = 1)
	private String name;
	@ExcelSheet(columnName = "Tablespace Name", columnNum = 2)
	private String tablespaceNm;                  
	@ExcelSheet(columnName = "Remarks", columnNum = 6)
	private String remarks;
	@ExcelSheet(columnName = "Extension Name", columnNum = 3)
	private String extensionNm;
	@ExcelSheet(columnName = "Is SCD", columnNum = 4)
	private boolean scd;
	@ExcelSheet(columnName = "ETL Seq", columnNum = 5)
	private boolean etl;
	private List<DbColumn> dbColumns;
	private List<ExcelColumn> excelColumns;

	public DaoTemplate() {
	}
	
	public String getNameProperCase() {
		return Helpers.toProperCase(name);
	}
	
	public String getNameCamelCase() {
		return Helpers.toCamelCase(name);
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTablespaceNm() {
		return tablespaceNm;
	}

	public void setTablespaceNm(String tablespaceNm) {
		this.tablespaceNm = tablespaceNm;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getExtensionNm() {
		return extensionNm;
	}

	public void setExtensionNm(String extensionNm) {
		this.extensionNm = extensionNm;
	}

	public List<DbColumn> getDbColumns() {
		return dbColumns;
	}

	public void setDbColumns(List<DbColumn> dbColumns) {
		this.dbColumns = dbColumns;
	}

	public List<ExcelColumn> getExcelColumns() {
		return excelColumns;
	}

	public void setExcelColumns(List<ExcelColumn> excelColumns) {
		this.excelColumns = excelColumns;
	}
	
	public boolean isScd() {
		return scd;
	}

	public void setScd(boolean scd) {
		this.scd = scd;
	}

	public void addExcelColumn(ExcelColumn excelColumn) {
		if (excelColumns == null)
			excelColumns = new ArrayList<ExcelColumn>();
		excelColumns.add(excelColumn);
	}
	
	public void addDbColumn(DbColumn dbColumn) {
		if (dbColumns == null)
			dbColumns = new ArrayList<DbColumn>();
		dbColumns.add(dbColumn);
	}
	
	public static boolean isDdlRequested(int requestValue) {
		return ((requestValue & ARTIFACT_DDL) == ARTIFACT_DDL);
	}
	
	public static boolean isBeanRequested(int requestValue) {
		return ((requestValue & ARTIFACT_BEAN) == ARTIFACT_BEAN);
	}

	public boolean isEtl() {
		return etl;
	}

	public void setEtl(boolean etl) {
		this.etl = etl;
	}

}
