package com.ibm.wfm.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.DaoTemplate;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.DaoArtifactGeneratorService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.Helpers;
import com.opencsv.exceptions.CsvValidationException;

import freemarker.template.TemplateException;

@RestController
@RequestMapping("/api/v1")
public class DaoArtifactGeneratorController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Autowired
	private DaoArtifactGeneratorService daoArtifactGeneratorService;
	
	@PostMapping("dao-artifact-generator/java-bean")
	public ResponseEntity<Resource> daoGenerateJavaBean(@RequestParam("excelFile") MultipartFile excelFile
			                , @RequestParam(name="Excel Tab Name") String excelTabName
			                , HttpServletRequest request) throws CsvValidationException, IOException, EtlException, TemplateException, NoSuchMethodException, SecurityException {
		
		String excelFileName = fileStorageService.storeFile(excelFile);
		return daoGenerate(DaoTemplate.ARTIFACT_BEAN,excelFileName,excelTabName,request);
	}
	
	@PostMapping("dao-artifact-generator/ddl")
	public ResponseEntity<Resource> daoGenerateDdl(@RequestParam("excelFile") MultipartFile excelFile
			                , @RequestParam(name="Excel Tab Name") String excelTabName
			                , HttpServletRequest request) throws CsvValidationException, IOException, EtlException, TemplateException, NoSuchMethodException, SecurityException {
		
		String excelFileName = fileStorageService.storeFile(excelFile);
		return daoGenerate(DaoTemplate.ARTIFACT_DDL,excelFileName,excelTabName,request);
	}
	
	@PostMapping("dao-artifact-generator")
	public ResponseEntity<Resource> daoGenerateAll(@RequestParam("excelFile") MultipartFile excelFile
			                , @RequestParam(name="Excel Tab Name") String excelTabName
			                , HttpServletRequest request) throws CsvValidationException, IOException, EtlException, TemplateException, NoSuchMethodException, SecurityException {
		
		String excelFileName = fileStorageService.storeFile(excelFile);
		return daoGenerate(DaoTemplate.ARTIFACT_BEAN+DaoTemplate.ARTIFACT_DDL,excelFileName,excelTabName,request);
	}
	
	public ResponseEntity<Resource> daoGenerate(int artifactRequestValue
			                , String excelFileName
			                , String excelTabName
			                , HttpServletRequest request) throws CsvValidationException, IOException, EtlException, TemplateException, NoSuchMethodException, SecurityException {

		List<String> generatedDdl = daoArtifactGeneratorService.generateArtifcat(artifactRequestValue
											, fileStorageProperties.getUploadDir()+"/"+excelFileName
				                            , excelTabName
				                            , fileStorageProperties.getUploadDir());
		String returnFile = null;
		if (generatedDdl.size()>1) {
			//Zip the collection of files
			returnFile = "allArtifacts.zip";
			Helpers.zipMultipleFiles(fileStorageProperties.getUploadDir(), generatedDdl, returnFile);
		}
		else {
			returnFile = generatedDdl.get(0);
		}
		
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(returnFile);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());


        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
		
	}

}
