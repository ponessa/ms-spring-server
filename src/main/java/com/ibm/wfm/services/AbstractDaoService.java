package com.ibm.wfm.services;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.utils.DataManagerType4;
import com.ibm.wfm.utils.DataMarshaller;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public abstract class AbstractDaoService implements DaoInterface {
	private Class t = null;
	private Class parentT = null;
	private String baseTableNm = null;
	private String tableNm = null;
	private String scdTableNm = null;
	

	
	public <T> List<T> getListForSql(Class type, Connection conn, String sql) throws SQLException {
		return DataManagerType4.getSelectQuery(type, conn, sql);
	}
	
	public <T> List<T> findAll() throws SQLException {
		return findAll("", null, 0);
	}
	
	public <T> List<T> findAll(String filters) throws SQLException {
		return findAll(filters, null, 0);
	}
	
	public <T> List<T> findAll(String filters, String pit) throws SQLException {
		return findAll(filters, pit, 0);
	}
	
	public <T> List<T> findAll(String filters, int size)throws SQLException {
		return findAll(filters, null, size);
	}
	
	public <T> List<T> findAllTax(String filters, String pit, int size) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		System.out.println("Connection established");
		String sql = "SELECT * FROM xxx WHERE 1=1";
		return  DataManagerType4.getSelectTaxonomyQuery(t, conn, sql);
	}
	
	public <T> List<T> findAll(String filters, String pit, int size) throws SQLException {
		List<T> fbsFootballDims = new ArrayList<>();

		try {
			String xNm = tableNm;
			if (pit!=null) xNm = scdTableNm;

			Connection conn = getConnection();
			System.out.println("Connection established");
			String sql = "SELECT * FROM "+xNm + " WHERE 1=1";
			if (filters.length()>0) {
				sql+=" AND "+filters; //Bad - SQL Injection
			}
			if (pit!=null && !pit.equalsIgnoreCase("all")) {
				String delim = pit.equalsIgnoreCase("CURRENT TIMESTAMP")?"":"'";
				sql+=" AND "+delim+pit+delim+" BETWEEN EFF_TMS AND EXPIR_TMS";
			}
			//sql+=" ORDER BY CONF_CD, DIVISION_CD, TEAM_CD";
			StringBuffer orderBy = null;
			for (Field field : t.getDeclaredFields()) {
				DbTable column = field.getAnnotation(DbTable.class);
				if (column!=null && column.keySeq()>0) {
					if (orderBy==null) {
						orderBy = new StringBuffer();
						orderBy.append(" ORDER BY ").append(column.columnName());
					}
					else orderBy.append(", ").append(column.columnName());
				}
			}
			if (orderBy!=null) sql+= orderBy.toString();
			if (size>0) sql+= " FETCH FIRST "+size+" rows only";
			System.out.println(sql);
			fbsFootballDims = DataManagerType4.getSelectQuery(t, conn, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return fbsFootballDims;
	}
	
	public <T> int insertAll(List<T> fbsFootball) {
		
		int insertCnt = -1;
		try {
			Connection conn = getConnection();
			System.out.println("Connection established");
			insertCnt = DataManagerType4.insert2Connection(t, conn, tableNm, fbsFootball);
			conn.close();
			if (insertCnt>0) {
				System.out.println("Yah");
				return insertCnt;
			}
			else {
				System.out.println("boo");
				return insertCnt;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return insertCnt;
		}
	}
	
	public int deleteAll() {
		int deletedCnt = -1;
		Connection conn = getConnection();
		System.out.println("Connection established");
		deletedCnt = DataManagerType4.deleteAll2Connection(conn, tableNm);
		return deletedCnt;
	}
	
	public <T> int delete(List<T> fbsFootball) {
		
		int insertCnt = -1;

		Connection conn = getConnection();
		System.out.println("Connection established");
		insertCnt = DataManagerType4.delete2Connection(t, conn, tableNm+"_V", fbsFootball);
		if (insertCnt>0) {
			System.out.println("Yah");
			return insertCnt;
		}
		else {
			System.out.println("boo");
			return insertCnt;
		}

	}
	
	public <T> List<T> getObjectListFromExcel(String excelFileName, String excelTabName) throws NoSuchMethodException, SecurityException, IOException {
		List<T> objectList = DataMarshaller.getObjectListFromExcel(t,  excelFileName,  excelTabName);
		return objectList;
	}
	
	public <T> EtlResponse etl(String oldFileName
			                , String newFileName
			                , int keyLength
			                , String outputFileName) throws IOException, EtlException, CsvValidationException {
		EtlResponse etlResponse = new EtlResponse();
	    int totalCnt=0;
	    int insertCnt=0;
	    int updateCnt=0;
	    int deleteCnt=0;
	    
		List<T> fbsFootballTeamsInserts = null;
		List<T> fbsFootballTeamsDeletes = null;
		//Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        //        .toAbsolutePath().normalize();
		if (LadderComparatorService.processCsv(oldFileName
				                             , newFileName
				                             , keyLength
				                             , outputFileName)) {
			CSVReader csvReader = new CSVReader(new FileReader(outputFileName));
		    String[] values = null;
		    while ((values = csvReader.readNext()) != null) {
		    	totalCnt++;
		    	T fbsFootball = (T) DataMarshaller.buildObjectFromList(t, values, 1);
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
			etlResponse.setInsertUpdateAppliedCnt(this.insertAll(fbsFootballTeamsInserts));
		
		if (fbsFootballTeamsDeletes!=null)
			etlResponse.setDeleteAppliedCnt(this.delete(fbsFootballTeamsDeletes));
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(outputFileName)
                .toUriString();
		
		etlResponse.setDeltaFile(fileDownloadUri);
		
		return etlResponse;
	}
	

	public <T> Class<T> getT() {
		return t;
	}

	public void setT(Class t) {
		this.t = t;
	}

	public String getTableNm() {
		return tableNm;
	}

	public void setTableNm(String tableNm) {
		this.tableNm = tableNm;
	}

	public String getScdTableNm() {
		return scdTableNm;
	}

	public void setScdTableNm(String scdTableNm) {
		this.scdTableNm = scdTableNm;
	}

	public String getBaseTableNm() {
		return baseTableNm;
	}

	public void setBaseTableNm(String baseTableNm) {
		this.baseTableNm = baseTableNm;
	}

	public Class getParentT() {
		return parentT;
	}

	public void setParentT(Class parentT) {
		this.parentT = parentT;
	}
	


}
