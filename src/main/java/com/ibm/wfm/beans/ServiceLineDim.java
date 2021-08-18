package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

public class ServiceLineDim  extends NaryTreeNode {
	@DbTable(columnName="SERVICE_LINE_ID",isId=true)
	private int        serviceLineId;
	@DbTable(columnName="SERVICE_LINE_CD",keySeq=1)
	private String     serviceLineCd;
	@DbTable(columnName="SERVICE_LINE_NM")
	private String     serviceLineNm;
	@DbTable(columnName="SERVICE_LINE_DESC")
	private String     serviceLineDesc;
	@DbTable(columnName="GROWTH_PLATFORM_CD")
	private String     growthPlatformCd;
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public ServiceLineDim () {}
	
	// Define base constructor
	public ServiceLineDim (
      String     serviceLineCd
    , String     serviceLineNm
    , String     serviceLineDesc
    , String     growthPlatformCd
	) {
		super(serviceLineCd, serviceLineNm);
		this.setLevel(2);
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.serviceLineDesc                = serviceLineDesc;
		this.growthPlatformCd               = growthPlatformCd;
	}
    
	// Define full constructor
	public ServiceLineDim (
		  int        serviceLineId
		, String     serviceLineCd
		, String     serviceLineNm
		, String     serviceLineDesc
		, String     growthPlatformCd
	) {
		super(serviceLineCd, serviceLineNm);
		this.setLevel(2);
		this.serviceLineId                  = serviceLineId;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.serviceLineDesc                = serviceLineDesc;
		this.growthPlatformCd               = growthPlatformCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		ServiceLineDim other = (ServiceLineDim) obj;
		if (
            this.serviceLineCd.equals(other.getServiceLineCd())
         && this.serviceLineNm.equals(other.getServiceLineNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.serviceLineCd)
        + "," + Helpers.formatCsvField(this.serviceLineNm)
        + "," + Helpers.formatCsvField(this.serviceLineDesc)
        + "," + Helpers.formatCsvField(this.growthPlatformCd)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SERVICE_LINE_CD")
        + "," + Helpers.formatCsvField("SERVICE_LINE_NM")
        + "," + Helpers.formatCsvField("SERVICE_LINE_DESC")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_CD")
		;
	}
    
	// Define Getters and Setters
	public int getServiceLineId() {
		return serviceLineId;
	}
	public void setServiceLineId(int serviceLineId) {
		this.serviceLineId = serviceLineId;
	}
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getServiceLineDesc() {
		return serviceLineDesc;
	}
	public void setServiceLineDesc(String serviceLineDesc) {
		this.serviceLineDesc = serviceLineDesc;
	}
	public String getGrowthPlatformCd() {
		return growthPlatformCd;
	}
	public void setGrowthPlatformCd(String growthPlatformCd) {
		this.growthPlatformCd = growthPlatformCd;
	}
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
}