package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wfm.beans.Certification;
import com.ibm.wfm.beans.FutureSkill;
import com.ibm.wfm.beans.FutureSkillsDim;
import com.ibm.wfm.services.CertificationDaoService;
import com.ibm.wfm.services.Wf360FutureSkillDaoService;
import com.ibm.wfm.utils.DataMarshaller;

@RestController
@RequestMapping("/api/v1")
public class Wf360Controller {
	
	@Autowired
	private Wf360FutureSkillDaoService futureSkillDaoService;
	
	@Autowired
	private CertificationDaoService certificationDaoService; 
	
	@GetMapping("/wf360/future-skills")
	public List<FutureSkillsDim> retrieveAllFutureSkills() throws SQLException {
		return futureSkillDaoService.findAll();
	}
	
	@GetMapping(value="/wf360/future-skills/csv",produces="text/csv")
	public void toCsvAllFutureSkills(HttpServletResponse response) throws IOException, SQLException {
		response.setContentType("text/csv; charset=utf-8");
		List<FutureSkill> futureSkills = futureSkillDaoService.findAll();
		DataMarshaller.writeCsv2PrintWriter(FutureSkill.class, response.getWriter(), futureSkills);
		return;
	}
	
	/*
	
	@GetMapping("/wf360/etl/future-skills")
	public int etlFutureSkills() {
		List<FutureSkill> futureSkills = futureSkillDaoService.findAll();
		return futureSkillDaoService.insertAll(futureSkills);
		//return null;
	}
	*/
	
	@GetMapping("/wf360/certifications")
	public List<Certification> retrieveAllCertifications() {
		return certificationDaoService.findAll();
	}
	
	/*
	@GetMapping("/wf360/idl/certifications")
	public int idlCertifications() {
		List<Certification> certifications = certificationDaoService.findAll();
		return certificationDaoService.insertAll(certifications);
	}
	*/
	
	@DeleteMapping("/wf360/certifications")
	public int deleteCertifications() {
		return certificationDaoService.deleteAll();
	}

}