package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

public class PracticeDim extends NaryTreeNode {
	@DbTable(columnName="PRACTICE_ID",isId=true)
	private int        practiceId;
	@DbTable(columnName="PRACTICE_CD",keySeq=1)
	private String     practiceCd;
	@DbTable(columnName="PRACTICE_NM")
	private String     practiceNm;
	@DbTable(columnName="PRACTICE_DESC")
	private String     practiceDesc;
	@DbTable(columnName="DCC_CD")
	private String     dccCd;
	@DbTable(columnName="ACCEL_PRACTICE_IND")
	private String     accelPracticeInd;
	@DbTable(columnName="SERVICE_LINE_CD")
	private String     serviceLineCd;
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public PracticeDim () {}
	
	// Define base constructor
	public PracticeDim (
      String     practiceCd
    , String     practiceNm
    , String     practiceDesc
    , String     serviceLineCd
	) {
		super(practiceCd==null?"GBS00":practiceCd, practiceNm==null?"Null practice":practiceNm);
		if (practiceCd==null) practiceCd = "GBS00";
		if (practiceNm==null) practiceNm = "Null practice";
		this.setLevel(3);
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.practiceDesc                   = practiceDesc;
		this.serviceLineCd                  = serviceLineCd;
	}
    
	// Define full constructor
	public PracticeDim (
		  int        practiceId
		, String     practiceCd
		, String     practiceNm
		, String     practiceDesc
		, String     dccCd
		, String     accelPracticeInd
		, String     serviceLineCd
	) {
		super(practiceCd==null?"GBS00":practiceCd, practiceNm==null?"Null practice":practiceNm);
		this.setLevel(3);
		this.practiceId                     = practiceId;
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.practiceDesc                   = practiceDesc;
		this.dccCd                          = dccCd;
		this.accelPracticeInd               = accelPracticeInd;
		this.serviceLineCd                  = serviceLineCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		PracticeDim other = (PracticeDim) obj;
		if (this.practiceCd == null) {
			if (other.getPracticeCd()==null) {
				if (this.practiceNm == null) {
					if (this.getPracticeNm()==null) return true;
					else return false;
				}
				else return this.practiceNm.equals(other.getPracticeNm());
			}
			else return false;
		}
		else if (this.practiceNm == null) {
			if (other.getPracticeNm()==null) {
				return this.practiceCd.equals(other.getPracticeCd());
			}
			else return false;
		}
		else if (
            this.practiceCd.equals(other.getPracticeCd())
         && this.practiceNm.equals(other.getPracticeNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.practiceCd)
        + "," + Helpers.formatCsvField(this.practiceNm)
        + "," + Helpers.formatCsvField(this.practiceDesc)
        + "," + Helpers.formatCsvField(this.serviceLineCd)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("PRACTICE_CD")
        + "," + Helpers.formatCsvField("PRACTICE_NM")
        + "," + Helpers.formatCsvField("PRACTICE_DESC")
        + "," + Helpers.formatCsvField("SERVICE_LINE_CD")
		;
	}
    
	// Define Getters and Setters
	public int getPracticeId() {
		return practiceId;
	}
	public void setPracticeId(int practiceId) {
		this.practiceId = practiceId;
	}
	public String getPracticeCd() {
		return practiceCd;
	}
	public void setPracticeCd(String practiceCd) {
		this.practiceCd = practiceCd;
	}
	public String getPracticeNm() {
		return practiceNm;
	}
	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}
	public String getPracticeDesc() {
		return practiceDesc;
	}
	public void setPracticeDesc(String practiceDesc) {
		this.practiceDesc = practiceDesc;
	}
	public String getDccCd() {
		return dccCd;
	}
	public void setDccCd(String dccCd) {
		this.dccCd = dccCd;
	}
	public String getAccelPracticeInd() {
		return accelPracticeInd;
	}
	public void setAccelPracticeInd(String accelPracticeInd) {
		this.accelPracticeInd = accelPracticeInd;
	}
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
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