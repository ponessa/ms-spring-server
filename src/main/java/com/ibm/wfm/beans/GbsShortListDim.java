package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.ExcelSheet;

public class GbsShortListDim {
	private int jrsId;
	private String jrsCd;
	@ExcelSheet(columnName = "JR/S", columnNum = 0)
	private String jrsNm;
	@ExcelSheet(columnName = "JR Id", columnNum = 2)
	private String jobRoleCd;
	@ExcelSheet(columnName = "Job Role Name  ", columnNum = 1)
	private String jobRoleNm;
	@ExcelSheet(columnName = "SS ID", columnNum = 4)
	private String specialtyCd;
	@ExcelSheet(columnName = "Specialty Name", columnNum = 3)
	private String specialtyNm;
	@ExcelSheet(columnName = "Description", columnNum = 5)
	private String specialtyDesc;
	private String growthPlatformCd;
	@ExcelSheet(columnName = "JR/S Growth Platform", columnNum = 6)
	private String growthPlatformNm;
	private String serviceLineCd;
	@ExcelSheet(columnName = "JR/S Service Line", columnNum = 7)
	private String serviceLineNm;
	private String practiceCd;
	@ExcelSheet(columnName = "JR/S Practice", columnNum = 8)
	private String practiceNm;
	private String serviceAreaCd;
	@ExcelSheet(columnName = "JR/S Service Area", columnNum = 9)
	private String serviceAreaNm;
	@ExcelSheet(columnName = "Capacity Group", columnNum = 10)
	private String capacityGroupNm;
	@ExcelSheet(columnName = "ISV Package", columnNum = 11)
	private String isvPackageNm;
	@ExcelSheet(columnName = "Market Value", columnNum = 12)
	private String marketValueNm;
	@ExcelSheet(columnName = "Premium Type", columnNum = 13)
	private String premiumTypeNm;
	@ExcelSheet(columnName = "Primary Skill Grouping", columnNum = 14)
	private String primarySkillGroupingNm;
	@ExcelSheet(columnName = "Primary JR/S Count", columnNum = 15)
	private int primaryJrsCnt;
	@ExcelSheet(columnName = "Secondary JR/S Count", columnNum = 16)
	private int secondaryJrsCnt;
	@ExcelSheet(columnName = "JR/S Primary Job Category", columnNum = 17)
	private String jrsPrimaryJobCategoryNm;
	@ExcelSheet(columnName = "JR/S Community", columnNum = 18)
	private String jrsCommunityNm;
	@ExcelSheet(columnName = "JR/S CAMSS", columnNum = 19)
	private String jrsCamssNm;
	@ExcelSheet(columnName = "SVF Group", columnNum = 20)
	private String svfGroupNm;
	@ExcelSheet(columnName = "Calculated", columnNum = 21)
	private Double calculatedValue;
	
	public GbsShortListDim() {}

	public int getJrsId() {
		return jrsId;
	}

	public void setJrsId(int jrsId) {
		this.jrsId = jrsId;
	}

	public String getJrsCd() {
		return jrsCd;
	}

	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}

	public String getJrsNm() {
		return jrsNm;
	}

	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}

	public String getJobRoleCd() {
		return jobRoleCd;
	}

	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
	}

	public String getJobRoleNm() {
		return jobRoleNm;
	}

	public void setJobRoleNm(String jobRoleNm) {
		this.jobRoleNm = jobRoleNm;
	}

	public String getSpecialtyCd() {
		return specialtyCd;
	}

	public void setSpecialtyCd(String specialtyCd) {
		this.specialtyCd = specialtyCd;
	}

	public String getSpecialtyNm() {
		return specialtyNm;
	}

	public void setSpecialtyNm(String specialtyNm) {
		this.specialtyNm = specialtyNm;
	}

	public String getSpecialtyDesc() {
		return specialtyDesc;
	}

	public void setSpecialtyDesc(String specialtyDesc) {
		this.specialtyDesc = specialtyDesc;
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

	public String getServiceAreaCd() {
		return serviceAreaCd;
	}

	public void setServiceAreaCd(String serviceAreaCd) {
		this.serviceAreaCd = serviceAreaCd;
	}

	public String getServiceAreaNm() {
		return serviceAreaNm;
	}

	public void setServiceAreaNm(String serviceAreaNm) {
		this.serviceAreaNm = serviceAreaNm;
	}

	public String getCapacityGroupNm() {
		return capacityGroupNm;
	}

	public void setCapacityGroupNm(String capacityGroupNm) {
		this.capacityGroupNm = capacityGroupNm;
	}

	public String getIsvPackageNm() {
		return isvPackageNm;
	}

	public void setIsvPackageNm(String isvPackageNm) {
		this.isvPackageNm = isvPackageNm;
	}

	public String getMarketValueNm() {
		return marketValueNm;
	}

	public void setMarketValueNm(String marketValueNm) {
		this.marketValueNm = marketValueNm;
	}

	public String getPremiumTypeNm() {
		return premiumTypeNm;
	}

	public void setPremiumTypeNm(String premiumTypeNm) {
		this.premiumTypeNm = premiumTypeNm;
	}

	public String getPrimarySkillGroupingNm() {
		return primarySkillGroupingNm;
	}

	public void setPrimarySkillGroupingNm(String primarySkillGroupingNm) {
		this.primarySkillGroupingNm = primarySkillGroupingNm;
	}

	public int getPrimaryJrsCnt() {
		return primaryJrsCnt;
	}

	public void setPrimaryJrsCnt(int primaryJrsCnt) {
		this.primaryJrsCnt = primaryJrsCnt;
	}

	public int getSecondaryJrsCnt() {
		return secondaryJrsCnt;
	}

	public void setSecondaryJrsCnt(int secondaryJrsCnt) {
		this.secondaryJrsCnt = secondaryJrsCnt;
	}

	public String getJrsPrimaryJobCategoryNm() {
		return jrsPrimaryJobCategoryNm;
	}

	public void setJrsPrimaryJobCategoryNm(String jrsPrimaryJobCategoryNm) {
		this.jrsPrimaryJobCategoryNm = jrsPrimaryJobCategoryNm;
	}

	public String getJrsCommunityNm() {
		return jrsCommunityNm;
	}

	public void setJrsCommunityNm(String jrsCommunityNm) {
		this.jrsCommunityNm = jrsCommunityNm;
	}

	public String getJrsCamssNm() {
		return jrsCamssNm;
	}

	public void setJrsCamssNm(String jrsCamssNm) {
		this.jrsCamssNm = jrsCamssNm;
	}

	public String getSvfGroupNm() {
		return svfGroupNm;
	}

	public void setSvfGroupNm(String svfGroupNm) {
		this.svfGroupNm = svfGroupNm;
	}

	public Double getCalculatedValue() {
		return calculatedValue;
	}

	public void setCalculatedValue(Double calculatedValue) {
		this.calculatedValue = calculatedValue;
	}
	

}
