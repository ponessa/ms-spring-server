package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbInfo;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbInfo(beanName="BrandDim",baseTableName="REFT.BRAND")
public class BrandDim extends NaryTreeNode {
	@DbTable(columnName="BRAND_ID",isId=true)
	private int        brandId;
	@DbTable(columnName="BRAND_CD",keySeq=1)
	private String     brandCd;
	@DbTable(columnName="BRAND_NM")
	private String     brandNm;
	@DbTable(columnName="BRAND_DESC")
	private String     brandDesc;
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BrandDim () {
		this.level= 0;
	}
	
	// Define base constructor
	public BrandDim (
      String     brandCd
    , String     brandNm
    , String     brandDesc
	) {
		super(brandCd, brandNm);
		this.level = 0;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.brandDesc                      = brandDesc;
	}
    
	// Define full constructor
	public BrandDim (
		  int        brandId
		, String     brandCd
		, String     brandNm
		, String     brandDesc
	) {
		super(brandCd, brandNm);
		this.level = 0;
		this.brandId                        = brandId;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.brandDesc                      = brandDesc;
	}
	@Override
	public String getCode() {
		return this.brandCd;
	}
	public String getDescription() {
		return this.brandNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BrandDim other = (BrandDim) obj;
		if (
            this.brandCd.equals(other.getBrandCd())
         && this.brandNm.equals(other.getBrandNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.brandCd)
        + "," + Helpers.formatCsvField(this.brandNm)
        + "," + Helpers.formatCsvField(this.brandDesc)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("BRAND_CD")
        + "," + Helpers.formatCsvField("BRAND_NM")
        + "," + Helpers.formatCsvField("BRAND_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getBrandId() {
		return brandId;
	}
	
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd.trim();
	}
	public String getBrandNm() {
		return brandNm;
	}
	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm.trim();
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
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