package com.ibm.wfm.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.GbsShortListDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.ShortListDaoService;

@RestController
@RequestMapping("/api/v1")
public class ShortListAccessController {
	
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	private ShortListDaoService shortListDaoService;
	
	@PostMapping("/gbs-short-list")
	public List<GbsShortListDim> retrieveAll(@RequestParam("shortListFileName") MultipartFile shortListFileName
			, @RequestParam("tabName") String tabName
			, @RequestParam(required=false) String ofn) throws NoSuchMethodException, SecurityException, IOException {
		String excelFileName = fileStorageService.storeFile(shortListFileName);
		shortListDaoService.setTableNm("REFT.GBS_SHORT_LIST_DIM_V");
		shortListDaoService.setScdTableNm("REFT.GBS_SHORT_LIST_SCD_V");

		//deprecated
		//return shortListDaoService.getShortListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		return shortListDaoService.getObjectListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
	}

	@PostMapping("/gbs-short-list/etl")
	public List<GbsShortListDim> etl(@RequestParam("shortListFileName") MultipartFile shortListFileName
			, @RequestParam("tabName") String tabName
			, @RequestParam(required=false) String ofn) throws NoSuchMethodException, SecurityException, IOException {
		String excelFileName = fileStorageService.storeFile(shortListFileName);
		shortListDaoService.setTableNm("REFT.GBS_SHORT_LIST_DIM_V");
		shortListDaoService.setScdTableNm("REFT.GBS_SHORT_LIST_SCD_V");

		//deprecated
		//return shortListDaoService.getShortListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		return shortListDaoService.getObjectListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
	}


}
