package com.ibm.wfm.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FutureSkillsDim;
import com.ibm.wfm.beans.GrowthPlatformDim;
import com.ibm.wfm.beans.PracticeDim;
import com.ibm.wfm.beans.ServiceLineDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.FileHelpers;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoController {
	
	@Autowired
	private EdsDaoService edsDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * EDS BRAND_DIM
	 */
	@GetMapping(path="/eds/brand/scd",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllBrands(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM_V");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		return edsDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="/eds/brand/pit",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllPitBrands(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) {
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM_V");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		return edsDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="/eds/brand",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllCurrentBrands(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM_V");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		return edsDaoService.findAll(filters);
	}
	
	@DeleteMapping("/eds/brand")
	public int deleteAllBrands() {
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM_V");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		return edsDaoService.deleteAll();
	}
	
	@PostMapping("/eds/brand/etl-upload")
	public EtlResponse etl2EdsBrandUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		
		EtlResponse etlResponse = edsDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping("/eds/brand/etl")
	public EtlResponse etl2EdsBrand(@RequestParam("oldFileName") String oldFileName
			                , @RequestParam("newFileName") String newFileName
			                , @RequestParam(name="outputFileName",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		edsDaoService.setT(BrandDim.class);
		edsDaoService.setTableNm("REFT.BRAND_DIM");
		edsDaoService.setScdTableNm("REFT.BRAND_SCD_V");
		
		String oldFn = fileStorageProperties.getUploadDir()+"/"+oldFileName;
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+oldFileName)) oldFn=null;
		
		EtlResponse etlResponse = edsDaoService.etl(oldFn
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	/*
	 * EDS GROWTH_PLATFORM_DIM
	 */
	@GetMapping(path="/eds/growth-platform/scd",produces = { "application/json", "application/xml"})
	public List<GrowthPlatformDim> retrieveAllGrowthPlatforms(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM_V");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		return edsDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="/eds/growth-platform/pit",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllPitGrowthPlatforms(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) {
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM_V");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		return edsDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="/eds/growth-platform",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllCurrentGrowthPlatforms(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM_V");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		return edsDaoService.findAll(filters);
	}
	
	@DeleteMapping("/eds/growth-platform")
	public int deleteAllGrowthPlatforms() {
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM_V");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		return edsDaoService.deleteAll();
	}
	
	@PostMapping("/eds/growth-platform/etl-upload")
	public EtlResponse etl2EdsGrowthPlatformUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		
		EtlResponse etlResponse = edsDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping("/eds/growth-platform/etl")
	public EtlResponse etl2EdsGrowthPlatform(@RequestParam("oldFileName") String oldFileName
			                , @RequestParam("newFileName") String newFileName
			                , @RequestParam(name="outputFileName",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		edsDaoService.setT(GrowthPlatformDim.class);
		edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM");
		edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
		
		String oldFn = fileStorageProperties.getUploadDir()+"/"+oldFileName;
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+oldFileName)) oldFn=null;
		
		EtlResponse etlResponse = edsDaoService.etl(oldFn
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/*
	 * EDS SERVICE_LINE_DIM
	 */
	@GetMapping(path="/eds/service-line/scd",produces = { "application/json", "application/xml"})
	public List<ServiceLineDim> retrieveAllServiceLines(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM_V");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		return edsDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="/eds/service-line/pit",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllPitServiceLines(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) {
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM_V");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		return edsDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="/eds/service-line",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllCurrentServiceLines(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM_V");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		return edsDaoService.findAll(filters);
	}
	
	@DeleteMapping("/eds/service-line")
	public int deleteAllServiceLines() {
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM_V");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		return edsDaoService.deleteAll();
	}
	
	@PostMapping("/eds/service-line/etl-upload")
	public EtlResponse etl2EdsServiceLineUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		
		EtlResponse etlResponse = edsDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping("/eds/service-line/etl")
	public EtlResponse etl2EdsServiceLine(@RequestParam("oldFileName") String oldFileName
			                , @RequestParam("newFileName") String newFileName
			                , @RequestParam(name="outputFileName",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		edsDaoService.setT(ServiceLineDim.class);
		edsDaoService.setTableNm("REFT.SERVICE_LINE_DIM");
		edsDaoService.setScdTableNm("REFT.SERVICE_LINE_SCD_V");
		
		String oldFn = fileStorageProperties.getUploadDir()+"/"+oldFileName;
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+oldFileName)) oldFn=null;
		
		EtlResponse etlResponse = edsDaoService.etl(oldFn
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/*
	 * EDS PRACTICE_DIM
	 */
	@GetMapping(path="/eds/practice/scd",produces = { "application/json", "application/xml"})
	public List<PracticeDim> retrieveAllPractices(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM_V");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		return edsDaoService.findAll(filters,"all");
	}
	
	@GetMapping(path="/eds/practice/pit",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllPitPractices(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters
			                                            , @RequestParam(name="pit",defaultValue = "CURRENT TIMESTAMP") @ApiParam(value = "Point in time, format: yyyy-MM-dd-hh.mm.ss.sssssss", example = "2021-06-28-00.00.00.0") String pit) {
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM_V");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		return edsDaoService.findAll(filters, pit);
	}
	
	@GetMapping(path="/eds/practice",produces = { "application/json", "application/xml"})
	public List<BrandDim> retrieveAllCurrentPractices(@RequestParam(defaultValue = "") @ApiParam(value = "Add filter in format of a SQL WHERE clause.") String filters) {
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM_V");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		return edsDaoService.findAll(filters);
	}
	
	@DeleteMapping("/eds/practice")
	public int deleteAllPractices() {
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM_V");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		return edsDaoService.deleteAll();
	}
	
	@PostMapping("/eds/practice/etl-upload")
	public EtlResponse etl2EdsPracticeUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		
		EtlResponse etlResponse = edsDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping("/eds/practice/etl")
	public EtlResponse etl2EdsPractice(@RequestParam("oldFileName") String oldFileName
			                , @RequestParam("newFileName") String newFileName
			                , @RequestParam(name="outputFileName",defaultValue="brand.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException {
		edsDaoService.setT(PracticeDim.class);
		edsDaoService.setTableNm("REFT.PRACTICE_DIM");
		edsDaoService.setScdTableNm("REFT.PRACTICE_SCD_V");
		
		String oldFn = fileStorageProperties.getUploadDir()+"/"+oldFileName;
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+oldFileName)) oldFn=null;
		
		EtlResponse etlResponse = edsDaoService.etl(oldFn
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
}