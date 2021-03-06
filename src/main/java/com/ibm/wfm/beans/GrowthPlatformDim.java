package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

public class GrowthPlatformDim extends NaryTreeNode {
	@DbTable(columnName="GROWTH_PLATFORM_ID",isId=true)
	private int        growthPlatformId;
	@DbTable(columnName="GROWTH_PLATFORM_CD",keySeq=1)
	private String     growthPlatformCd;
	@DbTable(columnName="GROWTH_PLATFORM_NM")
	private String     growthPlatformNm;
	@DbTable(columnName="GROWTH_PLATFORM_DESC")
	private String     growthPlatformDesc;
	@DbTable(columnName="BRAND_CD")
	private String     brandCd;
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GrowthPlatformDim () {}
	
	// Define base constructor
	public GrowthPlatformDim (
      String     growthPlatformCd
    , String     growthPlatformNm
    , String     growthPlatformDesc
    , String     brandCd
	) {
		super(growthPlatformCd, growthPlatformNm);
		this.setLevel(1);
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.growthPlatformDesc             = growthPlatformDesc;
		this.brandCd                        = brandCd;
	}
    
	// Define full constructor
	public GrowthPlatformDim (
		  int        growthPlatformId
		, String     growthPlatformCd
		, String     growthPlatformNm
		, String     growthPlatformDesc
		, String     brandCd
	) {
		super(growthPlatformCd, growthPlatformNm);
		this.setLevel(1);
		this.growthPlatformId               = growthPlatformId;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.growthPlatformDesc             = growthPlatformDesc;
		this.brandCd                        = brandCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GrowthPlatformDim other = (GrowthPlatformDim) obj;
		if (
            this.growthPlatformCd.equals(other.getGrowthPlatformCd())
         && this.growthPlatformNm.equals(other.getGrowthPlatformNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.growthPlatformCd)
        + "," + Helpers.formatCsvField(this.growthPlatformNm)
        + "," + Helpers.formatCsvField(this.growthPlatformDesc)
        + "," + Helpers.formatCsvField(this.brandCd)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GROWTH_PLATFORM_CD")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_NM")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_DESC")
        + "," + Helpers.formatCsvField("BRAND_CD")
		;
	}
    
	// Define Getters and Setters
	public int getGrowthPlatformId() {
		return growthPlatformId;
	}
	public void setGrowthPlatformId(int growthPlatformId) {
		this.growthPlatformId = growthPlatformId;
	}
	public String getGrowthPlatformCd() {
		return growthPlatformCd;
	}
	public void setGrowthPlatformCd(String growthPlatformCd) {
		this.growthPlatformCd = growthPlatformCd;
	}
	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}
	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}
	public String getGrowthPlatformDesc() {
		return growthPlatformDesc;
	}
	public void setGrowthPlatformDesc(String growthPlatformDesc) {
		this.growthPlatformDesc = growthPlatformDesc;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd;
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