package com.ibm.wfm.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.JrsMapperResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.JrsMapperService;

import io.swagger.annotations.ApiOperation;

@RestController()
@RequestMapping("/api/v1")
public class JrsMapperController {
	
	@Autowired
	private FileStorageService fss;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	
	@ApiOperation(value = "Map operational demographic data to new organization structure")
	@PostMapping("/jrs-mapper")
	public JrsMapperResponse jrsMapper(
			  @RequestParam(value="mapFile")  MultipartFile mapFile
			, @RequestParam(value="serviceAreamapFile")  MultipartFile serviceAreaMapFile
			, @RequestParam(value="dataFile") MultipartFile dataFile
			, @RequestParam(defaultValue = ",") String delimiter
		    , @RequestParam("keyStr") String keyStr
		    , @RequestParam("returnOffsetStr") String returnOffsetStr
		    , @RequestParam(required=false) String ofn) {
		
		if (ofn==null) ofn="JRS_TRANSFORMATION.csv";
		
		String uploadDir = fileStorageProperties.getUploadDir();
		
		Date startTime = new Date();
		String uploadMapFile = fss.storeFile(mapFile);
		String uploadServiceAreaMapFile = fss.storeFile(serviceAreaMapFile);
		
		Date dataUploadStartTime = new Date();
		String uploadDataFile = fss.storeFile(dataFile);
		
		JrsMapperService jrsMapperService = new JrsMapperService();
		
		String mapFileName = uploadDir+"/"+uploadMapFile; 
		String serviceAreaMapFileName = uploadDir+"/"+uploadServiceAreaMapFile; 
		String dataFileName = uploadDir+"/"+uploadDataFile; 
		

		String outputFileName = uploadDir+"/"+ofn;
		
		int[] keyOffsets = JrsMapperService.parseIntegerList(keyStr);
		int[] returnOffsets = null;
		
		if (returnOffsetStr!=null) returnOffsets = JrsMapperService.parseIntegerList(returnOffsetStr);

		Date evaluationStartTime = new Date();
		
		JrsMapperResponse jrsMapperResponse = jrsMapperService.mapFromFile(mapFileName, serviceAreaMapFileName, delimiter, dataFileName, keyOffsets, returnOffsets,outputFileName);
		
		jrsMapperResponse.setStartTime(startTime);
		jrsMapperResponse.setMapUploadStartTime(startTime);
		jrsMapperResponse.setDataUploadStartTime(dataUploadStartTime);
		jrsMapperResponse.setEvaluationStartTime(evaluationStartTime);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(ofn)
                .toUriString();
		jrsMapperResponse.setOutputDatasetUrl(fileDownloadUri);
		jrsMapperResponse.setCompletionTime(new Date());
		
		return jrsMapperResponse;
	}

}
