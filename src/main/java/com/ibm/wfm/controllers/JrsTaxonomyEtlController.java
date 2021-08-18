package com.ibm.wfm.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GbsShortListDim;
import com.ibm.wfm.beans.GrowthPlatformDim;
import com.ibm.wfm.beans.PracticeDim;
import com.ibm.wfm.beans.ServiceLineDim;
import com.ibm.wfm.beans.UtNode;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.ShortListDaoService;
import com.ibm.wfm.utils.FileHelpers;

@RestController
@RequestMapping("/api/v1")
public class JrsTaxonomyEtlController {
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private ShortListDaoService shortListDaoService;
	
	//@Autowired
	//private EdsDaoService edsDaoService;
	
	@PostMapping(path="/jrs-taxonomy/etl",produces = { "application/json", "application/xml"})
	public List<EtlResponse> jrsTaxonomyEtl(@RequestParam("shortListFileName") MultipartFile shortListFileName
			, @RequestParam(value="tabName",defaultValue="GBS Short List",required=false) String tabName) throws IOException, NoSuchMethodException, SecurityException {
		
		/*
		 * Get and enrich the short list
		 *
		String shortListUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("gbs-short-list")
                .queryParam("MultipartFile", "shortListFileName")
                .queryParam("tabName", "tabName")
                .toUriString();
		
		ResponseEntity<GbsShortListDim[]> shortListResponseEntity = new RestTemplate().postForEntity(shortListUri, null,GbsShortListDim[].class); //uriVariables); 
		GbsShortListDim[] gbsShortLists = shortListResponseEntity.getBody();
		*/
		String excelFileName = fileStorageService.storeFile(shortListFileName);

		shortListDaoService.setT(GbsShortListDim.class);
		shortListDaoService.setTableNm("REFT.GBS_SHORT_LIST_DIM_V");
		shortListDaoService.setScdTableNm("REFT.GBS_SHORT_LIST_SCD_V");


		//deprecated
		//return shortListDaoService.getShortListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		List<GbsShortListDim> shortLists = shortListDaoService.getObjectListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		
		List<EtlResponse> etlResponses = new ArrayList<>();
		
		/*
		 * Retrieve the UT by calling the /api/v1/ut api using the default parameters of 
		 *   ocstatus: O (open offerings)
		 *   utlevel10: 10J00 (GBS)
		 */
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("ut-all")
                .toUriString();
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
			
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
			
			PracticeDim practice = new PracticeDim(utNode.getGbspracticecode()==null?"GBS000":utNode.getGbspracticecode()
					                              ,utNode.getGbspracticedescription()==null?"Null Practice":utNode.getGbspracticedescription()
					                              ,utNode.getGbspracticedescription(),utNode.getUtlevel17());
			if (!(practices.contains(practice))) practices.add(practice);
		}
		
		/*
		 * Write the lists for Brand, Growth Platform, Service Line, and Practice into files for ETL ladder processing.
		 */
		BufferedWriter brandWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/brandDim-new.csv"));
		boolean header = true;
		for (BrandDim brand: brands) {
			if (header) {
				brandWriter.write(BrandDim.getEtlHeader());
				header = false;
			}
			brandWriter.write(System.lineSeparator()+brand.toEtlString());
		}
		brandWriter.flush();
		brandWriter.close();
		
		BufferedWriter growthPlatformWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/growthPlatformDim-new.csv"));
		header = true;
		for (GrowthPlatformDim growthPlatform: growthPlatforms) {
			if (header) {
				growthPlatformWriter.write(growthPlatform.getEtlHeader());
				header = false;
			}
			growthPlatformWriter.write(System.lineSeparator()+growthPlatform.toEtlString());
		}
		growthPlatformWriter.flush();
		growthPlatformWriter.close();
		
		BufferedWriter serviceLineWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/serviceLineDim-new.csv"));
		header = true;
		for (ServiceLineDim serviceLine: serviceLines) {
			if (header) {
				serviceLineWriter.write(serviceLine.getEtlHeader());
				header = false;
			}
			serviceLineWriter.write(System.lineSeparator()+serviceLine.toEtlString());
		}
		serviceLineWriter.flush();
		serviceLineWriter.close();
		
		BufferedWriter practiceWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/practiceDim-new.csv"));
		header = true;
		for (PracticeDim practice: practices) {
			if (header) {
				practiceWriter.write(practice.getEtlHeader());
				header = false;
			}
			practiceWriter.write(System.lineSeparator()+practice.toEtlString());
		}
		practiceWriter.flush();
		practiceWriter.close();
		
		/*
		 * Calling the Brand dimension's ETL processing /api/v1/eds/brand/etl api 
		 */
		// Build URI for the Brand ETL Service
		String brandEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                //.path("eds/brand/etl?newFileName={newFileName}&oldFileName={oldFileName}&outputFileName={outputFileName}")
                .path("eds/brand/etl")
                .queryParam("newFileName", "brandDim-new.csv")
                .queryParam("oldFileName", "brandDim-old.csv")
                .queryParam("outputFileName", "brandDim-etl.csv")
                .toUriString();
		
		// Set tye URI Parameter values
		// Not used but left in code because Map<K,V> urlParameters should work but isn't(parameter encoded in the URL)
		String oldFileName = fileStorageProperties.getUploadDir()+"/brandDim-old.csv";
		String newFileName = fileStorageProperties.getUploadDir()+"/brandDim-new.csv";
		String outputFileName = fileStorageProperties.getUploadDir()+"/brandDim-etl.csv";
		
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/brandDim-old.csv")) oldFileName=null;

		// Not used but left in code because Map<K,V> urlParameters should work but isn't
		Map<String,String> urlParameters = new HashMap<String,String>();
		urlParameters.put("oldFileName", oldFileName);
		urlParameters.put("newFileName", newFileName);
		urlParameters.put("outputFileName", outputFileName);
		
		//ResponseEntity<EtlResponse> brandEtlResponseEntity = new RestTemplate().postForEntity(brandEtlUri, null, EtlResponse.class, urlParameters); 
		//ResponseEntity<EtlResponse> brandEtlResponseEntity = new RestTemplate().postForEntity(brandEtlUri, null, EtlResponse.class, oldFileName,newFileName,outputFileName); 
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> brandEtlResponseEntity = new RestTemplate().postForEntity(brandEtlUri, null, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(brandEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/brandDim-new.csv", fileStorageProperties.getUploadDir()+"/brandDim-old.csv");
		
		/*
		 * Calling the Growth Platform dimension's ETL processing /api/v1/eds/growth-platform/etl api 
		 */
		String rootFileName = "growthPlatform";
		// Build URI for the Brand ETL Service
		String growthPlatformEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds/growth-platform/etl")
                .queryParam("newFileName", rootFileName+"Dim-new.csv")
                .queryParam("oldFileName", rootFileName+"Dim-old.csv")
                .queryParam("outputFileName", rootFileName+"Dim-etl.csv")
                .toUriString();

		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> growthPlatformEtlResponseEntity = new RestTemplate().postForEntity(growthPlatformEtlUri, null, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(growthPlatformEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");
		
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/service-line/etl api 
		 */
		rootFileName = "serviceLine";
		// Build URI for the Brand ETL Service
		String serviceLineEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds/service-line/etl")
                .queryParam("newFileName", rootFileName+"Dim-new.csv")
                .queryParam("oldFileName", rootFileName+"Dim-old.csv")
                .queryParam("outputFileName", rootFileName+"Dim-etl.csv")
                .toUriString();

		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> serviceLineEtlResponseEntity = new RestTemplate().postForEntity(serviceLineEtlUri, null, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(serviceLineEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");		
		
		/*
		 * Calling the Practice dimension's ETL processing /api/v1/eds/practice/etl api 
		 */
		rootFileName = "practice";
		// Build URI for the Brand ETL Service
		String practiceEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds/practice/etl")
                .queryParam("newFileName", rootFileName+"Dim-new.csv")
                .queryParam("oldFileName", rootFileName+"Dim-old.csv")
                .queryParam("outputFileName", rootFileName+"Dim-etl.csv")
                .toUriString();

		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> practiceEtlResponseEntity = new RestTemplate().postForEntity(practiceEtlUri, null, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(practiceEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");		
		
		
		
		return etlResponses;
	}

}
