package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FbsFootballDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FbsFootballDao2Service;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1")
public class AccFootballController {
	
	@Autowired
	private FbsFootballDao2Service fbsFootballDaoService;
	
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@GetMapping(path="acc-football/scd",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.ACC_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.ACC_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="acc-football/pit",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllPitFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.ACC_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.ACC_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="acc-football",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllCurrentFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.ACC_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.ACC_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters);
	}
	
	@DeleteMapping("acc-football")
	public int deleteAll() {
		fbsFootballDaoService.setTableNm("TEST.ACC_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.ACC_FOOTBALL_SCD_V");
		return fbsFootballDaoService.deleteAll();
	}
	
	@PostMapping("acc-football/etl")
	public EtlResponse etl2FbsFootball(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="fbsFootball.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		fbsFootballDaoService.setTableNm("TEST.ACC_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.ACC_FOOTBALL_SCD_V");
		EtlResponse etlResponse = fbsFootballDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 3
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}

}
