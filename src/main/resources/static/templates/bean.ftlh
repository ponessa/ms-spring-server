package com.ibm.wfm.beans;

<#assign hasDate=false>
<#assign hasTms=false>
<#list daotemplate.dbColumns as dbcolumn>
	<#if dbcolumn.dataType=="DATE"><#assign hasDate=true></#if>
	<#if dbcolumn.dataType=="TIMESTAMP"><#assign hasTms=true></#if>
</#list>
<#if daotemplate.scd><#assign hasTms=true></#if>

<#if hasDate>
import java.sql.Date;
</#if>
<#if hasTms>
import java.sql.Timestamp;
</#if>
<#if hasDate || hasTms>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>

<#if daotemplate.dbColumns??>
import com.ibm.wfm.annotations.DbTable;
</#if>
<#if daotemplate.excelColumns??>
import com.ibm.wfm.annotations.ExcelSheet;
</#if>

import com.ibm.wfm.utils.Helpers;

public class ${daotemplate.nameProperCase}Dim {
<#list daotemplate.dbColumns as dbcolumn>
	@DbTable(columnName="${dbcolumn.name}"<#if dbcolumn.id>,isId=true</#if><#if dbcolumn.keySeq gt 0>,keySeq=${dbcolumn.keySeq}</#if>)
	<#if dbcolumn.dataType=="TIMESTAMP">
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	</#if>
	<#if dbcolumn.dataType=="DATE">
	@JsonFormat(pattern="yyyy-MM-dd")
	</#if>
	<#if daotemplate.excelColumns??>
		<#list daotemplate.excelColumns as excelcolumn>
			<#if excelcolumn.dbName == dbcolumn.name>
	@ExcelSheet(columnName="${excelcolumn.name}"<#if excelcolumn.position?has_content>,columnNum=${excelcolumn.position}</#if>)	
			</#if>
		</#list>
	</#if>
	private ${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase};
</#list>
<#if daotemplate.scd>
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
</#if>

	// Define null constructor
	public ${daotemplate.nameProperCase}Dim () {}
	
<#if daotemplate.etl>
	// Define base constructor
	public ${daotemplate.nameProperCase}Dim (
<#assign etlCnt = 0>
<#list daotemplate.dbColumns as dbcolumn>
		<#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>    , <#else>      </#if><#assign etlCnt = etlCnt+1>${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase}${'\n'}</#if><#t>
</#list>
	) {
<#list daotemplate.dbColumns as dbcolumn>
		<#if dbcolumn.etlSeq gt 0>		this.${dbcolumn.nameCamelCase?right_pad(30)} = ${dbcolumn.nameCamelCase};${'\n'}</#if><#t>
</#list>
	}
</#if>
    
<#assign keyCnt = 0>
	// Define full constructor
	public ${daotemplate.nameProperCase}Dim (
<#list daotemplate.dbColumns as dbcolumn>
		<#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase}
</#list>
	) {
<#list daotemplate.dbColumns as dbcolumn>
		this.${dbcolumn.nameCamelCase?right_pad(30)} = ${dbcolumn.nameCamelCase};
</#list>
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		${daotemplate.nameProperCase}Dim other = (${daotemplate.nameProperCase}Dim) obj;
		if (
<#assign etlCnt = 0>
<#list daotemplate.dbColumns as dbcolumn>
			<#if dbcolumn.etlSeq gt 0 && dbcolumn.etlSeq lt 3><#if etlCnt gt 0>         && <#else>            </#if><#assign etlCnt = etlCnt+1>this.${dbcolumn.nameCamelCase}.equals(other.get${dbcolumn.nameProperCase}())${'\n'}</#if><#t>
</#list>
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
		<#assign etlCnt = 0>
<#list daotemplate.dbColumns as dbcolumn>
    <#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>        + "," + <#else>                </#if><#assign etlCnt = etlCnt+1>Helpers.formatCsvField(this.${dbcolumn.nameCamelCase})${'\n'}</#if><#t>
</#list>
		;
	}
	
	public static String getEtlHeader() {
		return 
		<#assign etlCnt = 0>
<#list daotemplate.dbColumns as dbcolumn>
    <#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>        + "," + <#else>                </#if><#assign etlCnt = etlCnt+1>Helpers.formatCsvField("${dbcolumn.name}")${'\n'}</#if><#t>
</#list>
		;
	}
    
	// Define Getters and Setters
<#list daotemplate.dbColumns as dbcolumn>
	public ${dbcolumn.javaDataType} get${dbcolumn.nameProperCase}() {
		return ${dbcolumn.nameCamelCase};
	}
	public void set${dbcolumn.nameProperCase}(${dbcolumn.javaDataType} ${dbcolumn.nameCamelCase}) {
		this.${dbcolumn.nameCamelCase} = ${dbcolumn.nameCamelCase};
	}
</#list>
<#if daotemplate.scd>
	public Timestamp getEffTms() {
		return effTms;
	}
	public void setEffTms(Timestamp effTms) {
		this.effTms = effTms;
	}
	public Timestamp getExpirTms() {
		return expirTms;
	}
	public void setExpirTms(Timestamp expirTms) {
		this.expirTms = expirTms;
	}
	public String getRowStatusCd() {
		return rowStatusCd;
	}
	public void setRowStatusCd(String rowStatusCd) {
		this.rowStatusCd = rowStatusCd;
	}
</#if>
}