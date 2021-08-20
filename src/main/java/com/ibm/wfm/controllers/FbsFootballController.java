package com.ibm.wfm.controllers;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FbsFootballDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FbsFootballDao2Service;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.LadderComparatorService;
import com.ibm.wfm.utils.DataMarshaller;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1")
public class FbsFootballController {
	
	@Autowired
	private FbsFootballDao2Service fbsFootballDaoService;
	
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	
	@GetMapping(path="/sec-football/scd",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.FBS_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.FBS_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="/sec-football/pit",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllPitFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.FBS_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.FBS_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="/sec-football",produces = { "application/json", "application/xml"})
	public List<FbsFootballDim> retrieveAllCurrentFbsFootball(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) throws SQLException {
		fbsFootballDaoService.setTableNm("TEST.FBS_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.FBS_FOOTBALL_SCD_V");
		return fbsFootballDaoService.findAll(filters);
	}
	
	@DeleteMapping("sec-football")
	public int deleteAll() {
		fbsFootballDaoService.setTableNm("TEST.FBS_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.FBS_FOOTBALL_SCD_V");
		return fbsFootballDaoService.deleteAll();
	}
	
	@PostMapping("sec-football/etl")
	public EtlResponse etl2FbsFootball(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="fbsFootball.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		fbsFootballDaoService.setTableNm("TEST.FBS_FOOTBALL_DIM_V");
		fbsFootballDaoService.setScdTableNm("TEST.FBS_FOOTBALL_SCD_V");
		EtlResponse etlResponse = fbsFootballDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 3
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping("/etl/fbsFootball/deprecated")
	public EtlResponse etlFbsFootball(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="fbsFootball.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		EtlResponse etlResponse = new EtlResponse();
	    int totalCnt=0;
	    int insertCnt=0;
	    int updateCnt=0;
	    int deleteCnt=0;
	    
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		List<FbsFootballDim> fbsFootballTeamsInserts = null;
		List<FbsFootballDim> fbsFootballTeamsDeletes = null;
		//Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        //        .toAbsolutePath().normalize();
		if (LadderComparatorService.processCsv(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                             , fileStorageProperties.getUploadDir()+"/"+newFileName
				                             , 3
				                             , fileStorageProperties.getUploadDir()+"/"+outputFileNm)) {
			CSVReader csvReader = new CSVReader(new FileReader(fileStorageProperties.getUploadDir()+"/"+outputFileNm));
		    String[] values = null;
		    while ((values = csvReader.readNext()) != null) {
		    	totalCnt++;
		    	FbsFootballDim fbsFootball = (FbsFootballDim) DataMarshaller.buildObjectFromList(FbsFootballDim.class, values, 1);
		    	/*
		    	 * Updates are treated like inserts within SCD. 
		    	 * T2 Expire previous record for the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
		    	 */
		        if (values[0].equalsIgnoreCase("I") || values[0].equalsIgnoreCase("U")) {
		        	if (values[0].equalsIgnoreCase("I")) insertCnt++;
		        	else updateCnt++;
		        	if (fbsFootballTeamsInserts==null) fbsFootballTeamsInserts = new ArrayList<>();
		        	fbsFootballTeamsInserts.add(fbsFootball);
		        }
		        else if (values[0].equalsIgnoreCase("D")) {
		        	deleteCnt++;
		        	if (fbsFootballTeamsDeletes==null) fbsFootballTeamsDeletes = new ArrayList<>();
		        	fbsFootballTeamsDeletes.add(fbsFootball);
		        }
		        else {
		        	throw new EtlException("Unrecognized type encountered: "+ values[0]);
		        }
		    }		
		}
		else {
			throw new EtlException("Ladder comparison failed");
		}
		
		etlResponse.setTotalCnt(totalCnt);
		etlResponse.setInsertCnt(insertCnt);
		etlResponse.setUpdateCnt(updateCnt);
		etlResponse.setDeleteCnt(deleteCnt);
		
		if (fbsFootballTeamsInserts!=null)
			etlResponse.setInsertUpdateAppliedCnt(fbsFootballDaoService.insertAll(fbsFootballTeamsInserts));
		
		if (fbsFootballTeamsDeletes!=null)
			etlResponse.setDeleteAppliedCnt(fbsFootballDaoService.delete(fbsFootballTeamsDeletes));
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(outputFileNm)
                .toUriString();
		
		etlResponse.setDeltaFile(fileDownloadUri);
		
		return etlResponse;
	}

}
