package com.ibm.wfm.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.NaryTreeNodeEvaluation;
import com.ibm.wfm.beans.TaxonomyEvaluationResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.TaxonomyEvaluatorService;
import com.ibm.wfm.utils.Helpers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1")
public class TaxonomyEvaluatorContoller {
	
	@Autowired
	private FileStorageService fss;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private TaxonomyEvaluatorService te;
	
	@ApiOperation(value = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.", response = TaxonomyEvaluationResponse.class)
	@PostMapping("/taxonomy-evaluator-upload")
	public TaxonomyEvaluationResponse taxonomyEvaluatorUpload(@RequestParam(value="tax",required=true) @ApiParam(value="CSV containing standard taxonomy.") MultipartFile taxFile
			, @RequestParam(value="data",required=true) @ApiParam(value="CSV containing data to be evaluated agaist the taxonomy.") MultipartFile dataFile
			, @RequestParam(defaultValue = ",") @ApiParam(value="Data and taxonomy file delimiter.") String delimiter
			, @RequestParam("keyStr") @ApiParam(value="Offsets within the CSV that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr
			, @RequestParam(required=false, value="ofn", defaultValue="taxonomyEvaluationResults.csv") @ApiParam(value="Output file name.") String ofn
			, @RequestParam(required=false, defaultValue="true") @ApiParam(value="Include in output only rows for branches in error.") boolean outputErrorsOnly
			, @RequestParam(required=false, defaultValue="none") @ApiParam(value="Offsets of the data file columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {
		
		Date startTime = new Date();
		Date taxUploadStartTime;
		Date dataUploadStartTime;

		
		if (ofn==null) ofn="taxonomyEvaluationResults.csv";
		
		String uploadDir = fileStorageProperties.getUploadDir();
		
		taxUploadStartTime = new Date();
		System.out.println("taxUploadStartTime: "+taxUploadStartTime);
		String uploadTaxonomyFile = fss.storeFile(taxFile);
		dataUploadStartTime = new Date();
		System.out.println("dataUploadStartTime: "+dataUploadStartTime);
		String uploadDataFile = fss.storeFile(dataFile);
		System.out.println("uploads completed at: "+new Date());
		String taxFileName = uploadDir+"/"+uploadTaxonomyFile; //"/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv";
		String dataFileName = uploadDir+"/"+uploadDataFile; //"/Users/steve/$WFM/wf360/data/rah_people_data.csv";
		boolean useFullkey = true;
		
		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i=0; i<keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}
		
		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);
		
		TaxonomyEvaluationResponse ter = te.evaluateTaxonomy(taxFileName, dataFileName, delimiter, useFullkey, keyOffsets,ofn,outputErrorsOnly,dataFileOffsets);	
		
		ter.setStartTime(startTime);
		ter.setDataUploadStartTime(dataUploadStartTime);
		ter.setTaxUploadStartTime(taxUploadStartTime);
		
		return ter;
		
	}
	
	@ApiOperation(value = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.", response = TaxonomyEvaluationResponse.class)
	@PostMapping("/taxonomy-evaluator-file")
	public TaxonomyEvaluationResponse taxonomyEvaluatorFile(@RequestParam(value="tax",required=true) @ApiParam(value="Name of CSV containing standard taxonomy.") String taxFile
			, @RequestParam(value="data",required=true) @ApiParam(value="Name of CSV containing data to be evaluated agaist the taxonomy.") String dataFile
			, @RequestParam(defaultValue = ",") @ApiParam(value="Data and taxonomy file delimiter.") String delimiter
			, @RequestParam("keyStr") @ApiParam(value="Offsets within the CSV that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr
			, @RequestParam(required=false, value="ofn", defaultValue="taxonomyEvaluationResults.csv") @ApiParam(value="Output file name.") String ofn
			, @RequestParam(required=false, defaultValue="true") @ApiParam(value="Include in output only rows for branches in error.") boolean outputErrorsOnly
			, @RequestParam(required=false, defaultValue="none") @ApiParam(value="Offsets of the data file columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {
		
		Date startTime = new Date();
		Date taxUploadStartTime = new Date();;
		Date dataUploadStartTime = new Date();;

		
		if (ofn==null) ofn="taxonomyEvaluationResults.csv";
		
		String uploadDir = fileStorageProperties.getUploadDir();

		String taxFileName = uploadDir+"/"+taxFile; //"/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv";
		String dataFileName = uploadDir+"/"+dataFile; //"/Users/steve/$WFM/wf360/data/rah_people_data.csv";
		boolean useFullkey = true;
		
		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i=0; i<keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}
		
		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);
		
		TaxonomyEvaluationResponse ter = te.evaluateTaxonomy(taxFileName, dataFileName, delimiter, useFullkey, keyOffsets,ofn,outputErrorsOnly,dataFileOffsets);	
		
		ter.setStartTime(startTime);
		ter.setDataUploadStartTime(dataUploadStartTime);
		ter.setTaxUploadStartTime(taxUploadStartTime);
		
		return ter;
		
	}
	
	@ApiOperation(value = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.", response = TaxonomyEvaluationResponse.class)
	@PostMapping("/taxonomy-evaluator-api")
	public TaxonomyEvaluationResponse taxonomyEvaluatorApi(@RequestParam(value="tax",required=true) @ApiParam(value="API to retrieve standard taxonomy.") String taxApi
			, @RequestParam(value="data",required=true) @ApiParam(value="Name of CSV containing data to be evaluated agaist the taxonomy.") String dataApi
			, @RequestParam("keyStr") @ApiParam(value="Offsets within API data that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr
			, @RequestParam(required=false, value="ofn", defaultValue="taxonomyEvaluationResults.csv") @ApiParam(value="Output file name.") String ofn
			, @RequestParam(required=false, defaultValue="true") @ApiParam(value="Include in output only rows for branches in error.") boolean outputErrorsOnly
			, @RequestParam(required=false, defaultValue="none") @ApiParam(value="Offsets of the data within API data columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {

		
		TaxonomyEvaluationResponse ter = null;
		
		return ter;
		
	}


}
