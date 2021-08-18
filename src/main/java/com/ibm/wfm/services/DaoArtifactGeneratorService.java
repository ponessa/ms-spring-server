package com.ibm.wfm.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.DaoTemplate;
import com.ibm.wfm.beans.DbColumn;
import com.ibm.wfm.beans.ExcelColumn;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.utils.DaoArtifactGenerator;
import com.ibm.wfm.utils.DataMarshaller;

import freemarker.template.TemplateException;

@Component
public class DaoArtifactGeneratorService extends AbstractDaoService {
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	public List<String> generateArtifcat(int artifactRequestValue, String excelFileName, String excelTabName, String outputDirectory) throws IOException, TemplateException, NoSuchMethodException, SecurityException {
		//String templatePath = System.getProperty("user.dir")+fileStorageProperties.getTemplateDir();
		String templatePath = fileStorageProperties.getTemplateDir();
		System.out.println("templatePath orig="+templatePath);
		
		List<String> generatedFiles = new ArrayList<String>();
		
		List<DaoTemplate> daoTemplates = DataMarshaller.getObjectListFromExcel(DaoTemplate.class, excelFileName, "Tables");
		
		String[] tabs = excelTabName.split(",");

		for (DaoTemplate daoTemplate: daoTemplates) {
			if (excelTabName.equalsIgnoreCase("all") || Arrays.stream(tabs).anyMatch(daoTemplate.getName()::equals)) {
				List <DbColumn> dbColumns = DataMarshaller.getObjectListFromExcel(DbColumn.class, excelFileName, daoTemplate.getName());
				List <ExcelColumn> excelColumns = DataMarshaller.getObjectListFromExcel(ExcelColumn.class, excelFileName, daoTemplate.getName());

				//excelColumns.removeIf(element -> (element.getP("a")));
				Iterator<ExcelColumn> iter = excelColumns.iterator();
				while (iter.hasNext()) {
					ExcelColumn e = iter.next();
				  if (e.getPosition()==-1) iter.remove();
				}
				
				daoTemplate.setDbColumns(dbColumns);
				daoTemplate.setExcelColumns(excelColumns.size()==0?null:excelColumns);
				
				if (DaoTemplate.isDdlRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+daoTemplate.getName()+"_DIM.ddl");
					DaoArtifactGenerator.generate(templatePath, "ddl.ftlh", daoTemplate, out);
					generatedFiles.add(daoTemplate.getName()+"_DIM.ddl");
				}
				if (DaoTemplate.isBeanRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+daoTemplate.getNameProperCase()+"Dim.java");
					DaoArtifactGenerator.generate(templatePath, "bean.ftlh", daoTemplate, out);
					generatedFiles.add(daoTemplate.getNameProperCase()+"Dim.java");
				}
			}
		}
		
		return generatedFiles;
	}

	@Override
	public Connection getConnection() {
		// not required - Excel only
		return null;
	}
}
