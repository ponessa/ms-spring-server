package com.ibm.wfm.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.ibm.wfm.beans.DaoTemplate;
import com.ibm.wfm.beans.DbColumn;
import com.ibm.wfm.beans.ExcelColumn;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DaoArtifactGenerator {

	public static void main(String[] args) throws IOException, TemplateException  {
		DaoTemplate daoTemplate = new DaoTemplate();
		daoTemplate.setSchema("REFT");
		daoTemplate.setName("GBS_TALL_LIST");
		daoTemplate.setRemarks("GBS Short List of Skills");
		daoTemplate.setScd(true);
		//daoTemplate.setExtensionNm("GBS");
		/*
		 * 	public DbColumn(String name, String dataType, int length, int scale, boolean nullable, String defaultValue,
			int keySeq, boolean id, boolean scd, String remarks
			name, dataType, length, scale, nullable, defaultValue, keySeq, id, scd, remarks
		 */
		daoTemplate.addDbColumn(new DbColumn("JRS_ID","INTEGER",4,0,false,null,-1,true,false,null));
		daoTemplate.addDbColumn(new DbColumn("JRS_CD","CHAR",8,0,false,null,1,false,false,null));
		daoTemplate.addDbColumn(new DbColumn("JRS_2_CD","CHAR",8,0,false,null,2,false,false,null));
		daoTemplate.addDbColumn(new DbColumn("JRS_3_CD","CHAR",8,0,false,null,3,false,false,null));
		daoTemplate.addDbColumn(new DbColumn("JRS_NM","VARCHAR",256,0,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("F_1","VARCHAR",256,0,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("FI_2","DATE",10,0,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("FIL_3","TIMESTAMP",26,0,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("FILL_4","DECIMAL",20,2,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("FILLE_5","VARCHAR",256,0,false,null,-1,false,false,""));
		daoTemplate.addDbColumn(new DbColumn("FILLER_6","VARCHAR",256,0,false,null,-1,false,false,""));
		
		daoTemplate.addExcelColumn(new ExcelColumn(1,"A","Any thing","JRS_2_CD"));
		daoTemplate.addExcelColumn(new ExcelColumn(1,"A","Any thing","JRS_5_CD"));
		
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		//works
		//String templatePath = "/Users/steve/eclipse-workspace/ms-spring-server/src/main/resources/templates";
		//fails - templatePath = "./classes/templates";
		//templatePath = System.getProperty("user.dir")+"/target/classes/static/templates/"; //with ddl.ftlh
		//String templatePath = System.getProperty("user.dir")+"/target/classes/templates/";//with test.ftlh
		
		//String templatePath = System.getProperty("user.dir")+"/target/classes/templates/";//with test.ftlh
		String templatePath = System.getProperty("user.dir")+"/target/classes/static/templates/";//with ddl.ftlh
		
		String templateName = "bean.ftlh"; //"ddl.ftlh";
		
		Writer out = new OutputStreamWriter(System.out);
		DaoArtifactGenerator.generate(templatePath, templateName, daoTemplate, out);
		
	}
	
	public static void generate(String templatePath, String templateName, DaoTemplate daoTemplate, Writer out) throws IOException, TemplateException {
		
        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        
        //Works
		//cfg.setDirectoryForTemplateLoading(new File("/Users/steve/eclipse-workspace/ms-spring-server/src/main/resources/templates"));
		//fail 
        //cfg.setDirectoryForTemplateLoading(new File("."));
        //fail
        //cfg.setDirectoryForTemplateLoading(new File(DaoArtifactGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath()));

        cfg.setDirectoryForTemplateLoading(new File(templatePath));
        
        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
		
        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateName);
        
        Map root = new HashMap();
        root.put("daotemplate", daoTemplate);

        /* Merge data-model with template */
        //Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();
        out.close();
		return;
	}


}
