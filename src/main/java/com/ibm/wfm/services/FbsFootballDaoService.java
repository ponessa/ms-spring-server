package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.FbsFootballDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;
import com.ibm.wfm.utils.DataManagerType4;

@Component
public class FbsFootballDaoService {
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public List<FbsFootballDim> findAll(String filters) {
		return findAll(filters, null);
	}
	
	public List<FbsFootballDim> findAll(String filters, String pit) {
		List<FbsFootballDim> fbsFootballDims = null;
		System.out.println("Data manager created");
		try {
			String tableNm ="TEST.FBS_FOOTBALL_DIM_V";
			if (pit!=null) tableNm ="TEST.FBS_FOOTBALL_SCD_V";
			System.out.println("Before Connection");	
			String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
			Connection conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			String sql = "SELECT * FROM "+tableNm + " WHERE 1=1";
			if (filters.length()>0) {
				sql+=" AND "+filters; //Bad - SQL Injection
			}
			if (pit!=null && !pit.equalsIgnoreCase("all")) {
				String delim = pit.equalsIgnoreCase("CURRENT TIMESTAMP")?"":"'";
				sql+=" AND "+delim+pit+delim+" BETWEEN EFF_TMS AND EXPIR_TMS";
			}
			sql+=" ORDER BY CONF_CD, DIVISION_CD, TEAM_CD";
			System.out.println(sql);
			fbsFootballDims = DataManagerType4.getSelectQuery(FbsFootballDim.class, conn, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fbsFootballDims;
	}
	
	public int insertAll(List<FbsFootballDim> fbsFootball) {
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		//String jdbcUrlName = difProp.getUrl().replace("{userid}", System.getenv("dif-dao-userid")).replace("{password}", System.getenv("dif-dao-password"));
		
		int insertCnt = -1;
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			insertCnt = DataManagerType4.insert2Connection(FbsFootballDim.class, conn, "TEST.FBS_FOOTBALL_DIM_V", fbsFootball);
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
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		//String jdbcUrlName = difProp.getUrl().replace("{userid}", System.getenv("dif-dao-userid")).replace("{password}", System.getenv("dif-dao-password"));
		
		int deletedCnt = -1;
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			deletedCnt = DataManagerType4.deleteAll2Connection(conn, "TEST.FBS_FOOTBALL_DIM");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return deletedCnt;
		}
		return deletedCnt;
	}
	
	public int delete(List<FbsFootballDim> fbsFootball) {
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		//String jdbcUrlName = difProp.getUrl().replace("{userid}", System.getenv("dif-dao-userid")).replace("{password}", System.getenv("dif-dao-password"));
		
		int insertCnt = -1;
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			insertCnt = DataManagerType4.delete2Connection(FbsFootballDim.class, conn, "TEST.FBS_FOOTBALL_DIM_V", fbsFootball);
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

}
