package com.ibm.wfm.beans;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;

public class FbsFootballDim {
	
	@DbTable(columnName ="FBS_ID",isId=true)    
	private int  fsbId;
	@DbTable(columnName ="CONF_CD",keySeq=1)    
	private String  conferenceCd;
	@DbTable(columnName ="DIVISION_CD",keySeq=2)    
	private String  divisionCd;
	@DbTable(columnName ="TEAM_CD",keySeq=3)    
	private String  teamCd;
	@DbTable(columnName ="INSTITUTION_NM")    
	private String  institutionNm;
	@DbTable(columnName ="NICKNAME")    
	private String  nickNm;
	@DbTable(columnName ="LOCATION_NM")    
	private String  locationNm;
	@DbTable(columnName ="FOUNDED_YR")    
	private int  foundedYr;
	@DbTable(columnName ="JOINED_DT")    
	private Date  joinedDt;
	@DbTable(columnName ="ENROLLMENT_CT")    
	private int  enrollmentCnt;
	@DbTable(columnName ="ENDOWMENT_AMT")    
	private Double endorcementAmt;
	@DbTable(columnName ="AUDIT_TMS")    
	private Timestamp auditTms;
	@DbTable(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
	
	
	public FbsFootballDim() {}

	public FbsFootballDim(int fsbId, String conferenceCd, String divisionCd, String teamCd, String institutionNm,
			String nickNm, String locationNm, int foundedYr, Date joinedDt, int enrollmentCnt, Double endorcementAmt,
			Timestamp auditTms, Timestamp effTms, Timestamp expirTms, String rowStatusCd) {
		super();
		this.fsbId = fsbId;
		this.conferenceCd = conferenceCd;
		this.divisionCd = divisionCd;
		this.teamCd = teamCd;
		this.institutionNm = institutionNm;
		this.nickNm = nickNm;
		this.locationNm = locationNm;
		this.foundedYr = foundedYr;
		this.joinedDt = joinedDt;
		this.enrollmentCnt = enrollmentCnt;
		this.endorcementAmt = endorcementAmt;
		this.auditTms = auditTms;
		this.effTms = effTms;
		this.expirTms = expirTms;
		this.rowStatusCd = rowStatusCd;
	}

	public int getFsbId() {
		return fsbId;
	}

	public void setFsbId(int fsbId) {
		this.fsbId = fsbId;
	}

	public String getConferenceCd() {
		return conferenceCd;
	}

	public void setConferenceCd(String conferenceCd) {
		this.conferenceCd = conferenceCd;
	}

	public String getDivisionCd() {
		return divisionCd;
	}

	public void setDivisionCd(String divisionCd) {
		this.divisionCd = divisionCd;
	}

	public String getTeamCd() {
		return teamCd;
	}

	public void setTeamCd(String teamCd) {
		this.teamCd = teamCd;
	}

	public String getInstitutionNm() {
		return institutionNm;
	}

	public void setInstitutionNm(String institutionNm) {
		this.institutionNm = institutionNm;
	}

	public String getNickNm() {
		return nickNm;
	}

	public void setNickNm(String nickNm) {
		this.nickNm = nickNm;
	}

	public String getLocationNm() {
		return locationNm;
	}

	public void setLocationNm(String locationNm) {
		this.locationNm = locationNm;
	}

	public int getFoundedYr() {
		return foundedYr;
	}

	public void setFoundedYr(int foundedYr) {
		this.foundedYr = foundedYr;
	}

	public Date getJoinedDt() {
		return joinedDt;
	}

	public void setJoinedDt(Date joinedDt) {
		this.joinedDt = joinedDt;
	}

	public int getEnrollmentCnt() {
		return enrollmentCnt;
	}

	public void setEnrollmentCnt(int enrollmentCnt) {
		this.enrollmentCnt = enrollmentCnt;
	}

	public Double getEndorcementAmt() {
		return endorcementAmt;
	}

	public void setEndorcementAmt(Double endorcementAmt) {
		this.endorcementAmt = endorcementAmt;
	}

	public Timestamp getAuditTms() {
		return auditTms;
	}

	public void setAuditTms(Timestamp auditTms) {
		this.auditTms = auditTms;
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
