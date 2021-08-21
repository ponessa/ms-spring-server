package com.ibm.wfm.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.wfm.annotations.DbInfo;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;

public class DataManagerType4 {

	/*
	 * A static block (runs at jre initialization) to ensure that the drivers are
	 * loaded. This is normal JDBC procedure.
	 */
	static {
		try {
			// register the driver with DriverManager
			// The newInstance() call is needed for the sample to work with
			// JDK 1.1.1 on OS/2, where the Class.forName() method does not
			// run the static initializer. For other JDKs, the newInstance
			// call can be omitted.
			// Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean connected = false;
	private String server = null;
	private int port = -1;
	private String database = null;
	private String url = null;
	private Connection con = null;
	private Map<String, String> jdbcParameters = null;

	public DataManagerType4(String server, int port, String database, Map<String, String> jdbcParameters) {
		this.server = server;
		this.port = port;
		this.database = database;
		this.url = "jdbc:db2://" + server + ":" + String.valueOf(port) + "/" + database;
		this.jdbcParameters = jdbcParameters;
	}

	public DataManagerType4(String server, int port, String database) {
		this(server, port, database, null);
	}

	public DataManagerType4(String dbUrl, Map<String, String> jdbcParameters) {
		this.url = dbUrl;
		this.jdbcParameters = jdbcParameters;
	}

	public DataManagerType4(String dbUrl) {
		this(dbUrl, null);
	}

	public static void main(String[] args) {

		String systemName = null;
		boolean validParams = true;

		String userid = "cmpce897";
		String password = "Unicorns@123456";
		String jdbcUrlName = "jdbc:db2://dashdb-super-cdo-dal13-07.services.dal.bluemix.net:50001/BLUDB:sslConnection=true;";
		Map<String, String> jdbcParameters = null;

		// systemName="bmsiw";

		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-system")) {
					systemName = args[++optind];
				} else {
					// validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			validParams = false;
		}

		if (systemName.equalsIgnoreCase("wf360")) {
			String jdbcUri = "jdbc:db2://db2whoc-flex-bknroqv.services.dal.bluemix.net:50001/BLUDB:apiKey=_1jY4ksIluBuJw1HaR2g3dXUYk-2tKoqS-y69RhJLqAR;securityMechanism=15;pluginName=IBMIAMauth;sslConnection=true;";
			// DataManagerType4 dm4 = new DataManagerType4(jdbcUri);
			System.out.println("Data manager created");

			try {
				Connection conn = DriverManager.getConnection(jdbcUri);
				System.out.println("Connection established");
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT *  from WF360_HR.FACT_TRAINTRACK_LK_SUMMARY where current_indicator=1 and effective_date>='2021-03-18' fetch first 10 rows only");
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print((i > 1 ? "," : "") + rsmd.getColumnName(i));
				}
				System.out.println("");

				while (resultSet.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						String columnValue = resultSet.getString(i);
						System.out.print((i > 1 ? "," : "") + columnValue + " (" + rsmd.getColumnTypeName(i) + ": "
								+ rsmd.getColumnDisplaySize(i) + ")");
					}
					System.out.println("");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;
		}
		
		if (systemName.equalsIgnoreCase("quest-hub-uat")) {
			jdbcUrlName = "jdbc:db2://4352d9dc-76ab-4441-b99f-143b862c8743.bv7e8rbf0shslbo0krsg.databases.appdomain.cloud:30224/bludb:user=ponessa;password=Welcome2PaaSCloud#;sslConnection=true;";
			jdbcParameters = new HashMap<String, String>();
			jdbcParameters.put("db2.jcc.sslConnection", "true");

			try {
				Connection conn = DriverManager.getConnection(jdbcUrlName);
				System.out.println("Connection established");
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT *  from SDMGPE.CASE_COMPONENT_V  fetch first 10 rows only");
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print((i > 1 ? "," : "") + rsmd.getColumnName(i));
				}
				System.out.println("");

				while (resultSet.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						String columnValue = resultSet.getString(i);
						System.out.print((i > 1 ? "," : "") + columnValue + " (" + rsmd.getColumnTypeName(i) + ": "
								+ rsmd.getColumnDisplaySize(i) + ")");
					}
					System.out.println("");
				}	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;
		}
		else if (systemName.equalsIgnoreCase("bmsiw")) {
			/*
			 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
			 * valid certification path to requested target. ERRORCODE=-4499, SQLSTATE=08001
			 * jdbcUrlName =
			 * "jdbc:db2://bldbmsa.boulder.ibm.com:5508/MWNCDSNB:sslConnection=true;";
			 */
			jdbcUrlName = "jdbc:db2://bldbmsa.boulder.ibm.com:5031/MWNCDSNB";
			userid = "BMSDAAS";
			password = "55five55";

		} else if (systemName.equalsIgnoreCase("rah")) {
			/*
			 * Appending the sslConnection and sslCertLocation infomation to the back of the
			 * JDBC URL it failed with a poorly formed JDBC URL. Resolved by adding JVM
			 * variables: -Ddb2.jcc.sslConnection=true
			 * -Ddb2.jcc.sslCertLocation=/Users/steve/certs/b01zvi23736591.crt
			 */
			// jdbcUrlName =
			// "jdbc:db2://b01zvi23736591.ahe.pok.ibm.com:60010/mtxgbst:sslConnection=true;sslCertLocation=/Users/steve/certs/b01zvi23736591.crt";
			// jdbcUrlName =
			// "jdbc:db2://b01zvi23736591.ahe.pok.ibm.com:60010/mtxgbst:retrieveMessagesFromServerOnGetMessage=true;sslConnection=true;";
			jdbcUrlName = "jdbc:db2://b01zvi23736591.ahe.pok.ibm.com:60010/mtxgbst";
			userid = "ponessa";
			password = "Vr=QdVZ]uTEjSly8";
			/*
			 * For properties you'd set via the -D flag on the command line, you can use the
			 * System.setProperty() method.
			 */
			jdbcParameters = new HashMap<String, String>();
			jdbcParameters.put("db2.jcc.sslConnection", "true");
			jdbcParameters.put("db2.jcc.sslCertLocation", "/Users/steve/certs/b01zvi23736591.crt");
		} else if (systemName.equalsIgnoreCase("prom")) {
			jdbcUrlName = "ppydalpdm01.sl.bluecloud.ibm.com:50001/PMPDMPRD";
			userid = "ponessa";
			password = "M4Q9r3ftcA.gGlN";
			jdbcParameters = new HashMap<String, String>();
			jdbcParameters.put("db2.jcc.sslConnection", "true");

			// error message
			// Error: No suitable driver found for
			// ppydalpdm01.sl.bluecloud.ibm.com:50001/PMPDMPRD
		} else if (systemName.equalsIgnoreCase("slam")) {
			jdbcUrlName = "b03acirdb014.ahe.boulder.ibm.com:6720/slamp";
			userid = "ponessa";
			password = "VLJB!Ey4oHZm8dHF";
			jdbcParameters = new HashMap<String, String>();
			jdbcParameters.put("db2.jcc.sslConnection", "true");
			jdbcParameters.put("db2.jcc.sslCertLocation", "/Users/steve/certs/b03acirdb014.crt");

			// error message
			// Error: No suitable driver found for
			// ppydalpdm01.sl.bluecloud.ibm.com:50001/PMPDMPRD
		} else if (systemName.equalsIgnoreCase("dif")) {
			jdbcUrlName = "jdbc:db2://582cb5cb-9113-4c8a-94ad-3a7b8a132de7.bu6tc4nd0urletsa6ufg.databases.appdomain.cloud:31614/BLUDB:user=igcl1tpa;password=CXiVyoiAFEiqQXVZ;sslConnection=true;";
			//userid = "igcl1tpa";
			//password = "CXiVyoiAFEiqQXVZ";
			jdbcParameters = new HashMap<String, String>();
			jdbcParameters.put("db2.jcc.sslConnection", "true");

			try {
				Connection conn = DriverManager.getConnection(jdbcUrlName);
				System.out.println("Connection established");
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT *  from REFT.USER");
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print((i > 1 ? "," : "") + rsmd.getColumnName(i));
				}
				System.out.println("");

				while (resultSet.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						String columnValue = resultSet.getString(i);
						System.out.print((i > 1 ? "," : "") + columnValue + " (" + rsmd.getColumnTypeName(i) + ": "
								+ rsmd.getColumnDisplaySize(i) + ")");
					}
					System.out.println("");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;

			// error message
			// Error: No suitable driver found for
			// ppydalpdm01.sl.bluecloud.ibm.com:50001/PMPDMPRD
		}

		DataManagerType4 dm4 = new DataManagerType4(jdbcUrlName, jdbcParameters);
		try {
			Connection con = dm4.connect(userid, password);
		} catch (SQLException se) {
			System.out.println("Error: " + se.getMessage());
			System.out.println(se);
			se.printStackTrace();
			return;
		}
		System.out.println("Connection established");
	}

	public Connection connect(String userid, String password) throws SQLException {
		if (this.jdbcParameters != null) {
			for (Map.Entry<String, String> jdbcParameter : this.jdbcParameters.entrySet()) {
				System.setProperty(jdbcParameter.getKey(), jdbcParameter.getValue());
			}
		}
		con = DriverManager.getConnection(url, userid, password);
		return con;
	}

	public Connection getConnection() {
		return con;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Map<String, String> getJdbcParameters() {
		return jdbcParameters;
	}

	public void setJdbcParameters(Map<String, String> jdbcParameters) {
		this.jdbcParameters = jdbcParameters;
	}
	
	public static int deleteAll2Connection(Connection conn, String targetTableName) {
		try {
			String sql = "DELETE FROM "+targetTableName;
			System.out.println(sql);
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/*

	 */
	public static <T> int delete2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects) {
		StringBuffer pos = new StringBuffer();
		PreparedStatement ps = null;
		String prepStmt = "DELETE "+targetTableName+" WHERE ";
		String valuesStr = "";
		int i=0;
		int deleteCnt=0;
		for (Field field : type.getDeclaredFields()) {
			DbTable column = field.getAnnotation(DbTable.class);
			if (column!=null && !column.isId() && column.keySeq()>0) {
				if(++i>1) {
					valuesStr+=" AND ";
				}
				valuesStr+=column.columnName()+"=?";
			}
		} //end - for (Field field : type.getDeclaredFields())

		try {
			conn.setAutoCommit(false);
			System.out.println(prepStmt+valuesStr);
			ps = conn.prepareStatement(prepStmt+valuesStr);
			for (Object object: objects) {
				i=0;
				Class<?> zclass = object.getClass();
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbTable column = field.getAnnotation(DbTable.class);
					if (column!=null && !column.isId() && column.keySeq()>0) {
						Class<?> fieldType = field.getType();
						if (fieldType==String.class) {
							ps.setString(++i, (String)field.get(object));
							pos.append((i>1?",":"")).append("'").append((String)field.get(object)).append("'");
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {
							ps.setInt(++i,field.get(object)==null?-1:(Integer)field.get(object));
							pos.append((i>1?",":"")).append((Integer)field.get(object));
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							ps.setDouble(++i,(Double) field.get(object));
							pos.append((i>1?",":"")).append((Double)field.get(object));
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							ps.setDate(++i,(Date)field.get(object));
							pos.append((i>1?",":"")).append("'").append((Date)field.get(object)).append("'");
						}
						else if (fieldType==Timestamp.class) {
							ps.setTimestamp(++i,(Timestamp)field.get(object));
							pos.append((i>1?",":"")).append("'").append((Timestamp)field.get(object)).append("'");
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				
				//Temp instead of batching
				//ps.execute();
				pos.append(")");
				System.out.println(pos.toString());
				
				ps.addBatch();
				if (++deleteCnt%100 == 0) {
					ps.executeBatch();
					conn.commit();
					System.out.println(deleteCnt);
				}
				else if(deleteCnt%50 == 0) System.out.print("*");
				else if(deleteCnt%10 == 0) System.out.print(".");
				
			} //end - for (Object object: objects)
			
			ps.executeBatch();
			
			conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		}
		
		return deleteCnt;
	}

	/*

	 */
	public static <T> int insert2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects) {
		boolean autocommit = false; //sp, setting to true for debugging only. Setting to true (1) generates an insert statement (but quotes nulls and does not
		                            // format timestamp correctly) and bypassed 
		boolean buildSql = true;
		StringBuffer sql = new StringBuffer();
		
		PreparedStatement ps = null;
		String prepStmt = "INSERT INTO "+targetTableName+" (";
		String valuesStr = ") VALUES(";
		
		int i=0;
		int insertCnt=0;
		for (Field field : type.getDeclaredFields()) {
			DbTable column = field.getAnnotation(DbTable.class);
			if (column!=null && !column.isId() && !column.isScd()) {
				if(++i>1) {
					prepStmt+=",";
					valuesStr+=",";
				}
				prepStmt+=column.columnName();
				valuesStr+="?";
			}
		} //end - for (Field field : type.getDeclaredFields())
		try {
			if (buildSql) sql.append(prepStmt).append(") VALUES(");
			
			conn.setAutoCommit(autocommit);
			System.out.println(prepStmt+valuesStr+")");
			ps = conn.prepareStatement(prepStmt+valuesStr+")");
			for (Object object: objects) {
				i=0;
				Class<?> zclass = object.getClass();
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbTable column = field.getAnnotation(DbTable.class);
					if (column!=null && !column.isId() && !column.isScd()) {
						Class<?> fieldType = field.getType();
						
						if (autocommit && i>0) sql.append(",");
						
						if (fieldType==String.class) {
							if (autocommit) sql.append("'").append((String)field.get(object)).append("'");
							ps.setString(++i, (String)field.get(object));
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {
							if (autocommit) sql.append(field.get(object)==null?-1:(Integer)field.get(object));
							ps.setInt(++i,field.get(object)==null?-1:(Integer)field.get(object));
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(Double)field.get(object));
							ps.setDouble(++i,field.get(object)==null?0.0:(Double) field.get(object));
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							if (autocommit) sql.append("'").append((Date)field.get(object)).append("'");
							ps.setDate(++i,(Date)field.get(object));
						}
						else if (fieldType==Timestamp.class) {
							if (autocommit) sql.append("'").append((Timestamp)field.get(object)).append("'");
							ps.setTimestamp(++i,(Timestamp)field.get(object));
						}
						else if (fieldType==BigDecimal.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(BigDecimal)field.get(object));
							ps.setBigDecimal(++i,(BigDecimal)field.get(object));
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				
				//Temp instead of batching for debugging
				if (autocommit) {
					System.out.println(sql.toString());
					ps.execute();
				}
				
				//for debugging only .. autocommit should be false
				if (!autocommit) {
					ps.addBatch();
					if (++insertCnt%100 == 0) {
						ps.executeBatch();
						conn.commit();
						System.out.println(insertCnt);
					}
					else if(insertCnt%50 == 0) System.out.print("*");
					else if(insertCnt%10 == 0) System.out.print(".");
				}
				
			} //end - for (Object object: objects)
			
			if (!autocommit) ps.executeBatch();
			
			conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		}
		
		return insertCnt;
	}
	
	public static <T> String generateSqlForTaxonomy(Class<T> type, String tableSuffix) throws ClassNotFoundException {
		StringBuffer sql = new StringBuffer();
	    List<String> selectColumns = new ArrayList<>();
	    List<String> tables = new ArrayList<>();
	    List<String> keys = new ArrayList<>();
	    List<String> foreignKeys = new ArrayList<>();
	    List<String> orderBy = new ArrayList<>();
	    int tableCnt = 0;

    	Class<T> root = type;
    	System.out.println(type.getCanonicalName());
    	for (Field field : type.getDeclaredFields()) {
			DbTable column = field.getAnnotation(DbTable.class);
			if (column!=null) {
				if (!column.isScd() && column.foreignKey()==-1)
					selectColumns.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
				if (column.foreignKey()>0) foreignKeys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
				if (column.keySeq()>0) orderBy.add(0,"T"+String.valueOf(tableCnt)+"."+column.columnName());
			}
	   }

	   DbInfo dbInfo = type.getAnnotation(DbInfo.class);
	   tables.add(dbInfo.baseTableName()+tableSuffix+" T"+String.valueOf(tableCnt));
       Class zParentType = null;
       try {
    	   zParentType = Class.forName("com.ibm.wfm.beans."+dbInfo.parentBeanName());
    	   root = zParentType;
       }
       catch (ClassNotFoundException cnfe) {}
       
       
       while (zParentType!=null) {
	       System.out.println(zParentType.getCanonicalName());
	       tableCnt++;
	       List<String> columns = new ArrayList<>();
	       dbInfo = (DbInfo) zParentType.getAnnotation(DbInfo.class);
		   tables.add(dbInfo.baseTableName()+tableSuffix+" T"+String.valueOf(tableCnt));
	       for (Field field : zParentType.getDeclaredFields()) {
				DbTable column = field.getAnnotation(DbTable.class);
				if (column!=null) {
					if (!column.isScd() && column.foreignKey()==-1)
						columns.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
					if (column.keySeq()>0) {
						keys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
						orderBy.add(0,"T"+String.valueOf(tableCnt)+"."+column.columnName());
					}
					if (column.foreignKey()>0) foreignKeys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
				}
	       }
	       selectColumns.addAll(0, columns);
	       dbInfo = (DbInfo) zParentType.getAnnotation(DbInfo.class);
	       zParentType = null;
	       if (dbInfo!=null) {
		       try {
		    	   zParentType = Class.forName("com.ibm.wfm.beans."+dbInfo.parentBeanName());
		    	   root = zParentType;
		       }
		       catch (ClassNotFoundException cnfe) {}
	       }
       }
       
       sql.append("SELECT ");
       int i=0;
       for (String column: selectColumns) {
    	   sql.append((i++ == 0?"":",")+column);
       }
       i=0;
       sql.append(" FROM ");
       for (String table: tables) {
    	   if (i==0) System.out.println(table);
    	   else {
    		   sql.append("INNER JOIN "+table);
    		   if (i<=keys.size())
    			   sql.append("ON "+keys.get(i-1)+" = "+foreignKeys.get(i-1));
    	   }
    	   i++;
       }
       i=0;
       System.out.println("ORDER BY ");
       for (String column: orderBy) {
    	   System.out.println((i++ == 0?"":",")+column);
       }
		return sql.toString();
	}
	
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, String query) throws SQLException, ClassNotFoundException {
		
	    List<T> list = new ArrayList<T>(); //Return an empty array, instead of null, if the query has no rows;
	    
	    String tableSuffix = "_DIM_V";
	    
	    List<String> selectColumns = new ArrayList<>();
	    List<String> tables = new ArrayList<>();
	    List<String> keys = new ArrayList<>();
	    List<String> foreignKeys = new ArrayList<>();
	    List<String> orderBy = new ArrayList<>();
	    List<Class>  zclassHierarchy = new ArrayList<>();
	    zclassHierarchy.add(type);
	    int tableCnt = 0;
	    try {
	    	Class<T> root = type;
	    	System.out.println(type.getCanonicalName());
	    	for (Field field : type.getDeclaredFields()) {
				DbTable column = field.getAnnotation(DbTable.class);
				if (column!=null) {
					if (!column.isScd() && column.foreignKey()==-1)
						selectColumns.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
					if (column.foreignKey()>0) foreignKeys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
					if (column.keySeq()>0) orderBy.add(0,"T"+String.valueOf(tableCnt)+"."+column.columnName());
				}
		   }

		   DbInfo dbInfo = type.getAnnotation(DbInfo.class);
		   tables.add(dbInfo.baseTableName()+tableSuffix+" T"+String.valueOf(tableCnt));
	       Class zParentType = null;
	       try {
	    	   zParentType = Class.forName("com.ibm.wfm.beans."+dbInfo.parentBeanName());
	    	   root = zParentType;
	    	   zclassHierarchy.add(0,zParentType);
	       }
	       catch (ClassNotFoundException cnfe) {}
	       
	       
	       while (zParentType!=null) {
		       System.out.println(zParentType.getCanonicalName());
		       tableCnt++;
		       List<String> columns = new ArrayList<>();
		       dbInfo = (DbInfo) zParentType.getAnnotation(DbInfo.class);
			   tables.add(dbInfo.baseTableName()+tableSuffix+" T"+String.valueOf(tableCnt));
		       for (Field field : zParentType.getDeclaredFields()) {
					DbTable column = field.getAnnotation(DbTable.class);
					if (column!=null) {
						if (!column.isScd() && column.foreignKey()==-1)
							columns.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
						if (column.keySeq()>0) {
							keys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
							orderBy.add(0,"T"+String.valueOf(tableCnt)+"."+column.columnName());
						}
						if (column.foreignKey()>0) foreignKeys.add("T"+String.valueOf(tableCnt)+"."+column.columnName());
					}
		       }
		       selectColumns.addAll(0, columns);
		       dbInfo = (DbInfo) zParentType.getAnnotation(DbInfo.class);
		       zParentType = null;
		       if (dbInfo!=null) {
			       try {
			    	   zParentType = Class.forName("com.ibm.wfm.beans."+dbInfo.parentBeanName());
			    	   root = zParentType;
			    	   zclassHierarchy.add(0,zParentType);
			       }
			       catch (ClassNotFoundException cnfe) {}
		       }
	       }
	       
	       StringBuffer sql = new StringBuffer();
	       sql.append("SELECT ");
	       int i=0;
	       for (String column: selectColumns) {
	    	   sql.append((i++ == 0?"":",")+column);
	       }
	       i=0;
	       sql.append(" FROM ");
	       for (String table: tables) {
	    	   if (i==0) sql.append(table);
	    	   else {
	    		   sql.append(" INNER JOIN "+table);
	    		   if (i<=keys.size())
	    			   sql.append(" ON "+keys.get(i-1)+" = "+foreignKeys.get(i-1));
	    	   }
	    	   i++;
	       }
	       i=0;
	       sql.append(" ORDER BY ");
	       for (String column: orderBy) {
	    	   sql.append((i++ == 0?"":",")+column);
	       }
	       
	       System.out.println(sql.toString());
	       for (Class c: zclassHierarchy) {
	    	   System.out.println(c.getName()+" ("+c.getCanonicalName()+", "+c.getTypeName()+", "+c.getSimpleName()+")");
	       }

	       Statement stmt = conn.createStatement();
	       ResultSet rst = stmt.executeQuery(sql.toString());

	       T lastObjectFound = null;
	       T lastRoot = null;
	       while (rst.next()) {
	    	   List<T> objectList = new ArrayList<>();
	    	   for (Class zclass: zclassHierarchy) {
	    	       Constructor<T> constructor = zclass.getConstructor();
		    	   T t = constructor.newInstance();
		    	   loadResultSet2Object(rst, t);
		    	   objectList.add(t);
	    	   }
	    	   i=0;
	    	   for (T object: objectList) {
	    		   if (list.size()==0) {
	    			   list.add(object);
	    			   lastObjectFound = object;
	    			   lastRoot = object;
	    		   }
	    		   else {
	    			   NaryTreeNode tempObject = NaryTree.findStatic((NaryTreeNode)object, (NaryTreeNode)list.get(0), false);
	    			   if (tempObject==null) {
	    				   if (i == 0) {
	    					   list.add(object);
	    					   lastRoot = object;
	    				   }
	    				   else ((NaryTreeNode)lastObjectFound).addChild((NaryTreeNode)object);
	    				   if (i<objectList.size()-1) lastObjectFound = object;
	    			   }
	    			   else {
	    				   if (i<objectList.size()-1) lastObjectFound = (T)tempObject;
	    			   }
	    		   }
	    		   i++;
	    	   }
	    	   lastObjectFound = lastRoot;
	       }
	    } 
	    catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
	    	throw new RuntimeException("Unable to construct "+type.getName()+ " object: " + e.getMessage(), e);
		} 
	    return list;
	}
	
	public static <T> List<T> getSelectQuery(Class<T> type, Connection conn, String query) throws SQLException {
	    List<T> list = new ArrayList<T>(); //Return an empty array, instead of null, if the query has no rows;
	    try {
	       Statement stmt = conn.createStatement();
	       ResultSet rst = stmt.executeQuery(query);
	       Constructor<T> constructor = type.getConstructor();
	       while (rst.next()) {
	    	   //if (list==null) list = new ArrayList<T>();
	    	   T t = constructor.newInstance();
	    	   loadResultSet2Object(rst, t);
	    	   list.add(t);
	       }
	       conn.close();
	    } 
	    catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
	    	throw new RuntimeException("Unable to construct "+type.getName()+ " object: " + e.getMessage(), e);
		} 
	    return list;
	}
	
	public static void loadResultSet2Object(ResultSet rst, Object object)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		Class<?> zclass = object.getClass();
		for (Field field : zclass.getDeclaredFields()) {
			DbTable column = field.getAnnotation(DbTable.class);
			if (column!=null) {
				if (hasColumn(rst,column.columnName())) {
					field.setAccessible(true);
					Object value = rst.getObject(column.columnName());
					Class<?> type = field.getType();
					if (isPrimitive(type)) {// check primitive type
						Class<?> boxed = boxPrimitiveClass(type);// box if primitive
						if (value==null && (type==int.class || type==short.class || type==long.class || type==double.class || type==float.class)) value=0;
						if (value==null && (type==boolean.class)) value=false;
						value = boxed.cast(value);
					}
					field.set(object, value);
				}
			}
		}
	}
	
	public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.equals(rsmd.getColumnName(x))) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean isPrimitive(Class<?> type) {
		return (type == int.class || type == long.class || type == double.class || type == float.class
				|| type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}
	
	public static Class<?> boxPrimitiveClass(Class<?> type) {
		if (type == int.class) {
			return Integer.class;
		} else if (type == long.class) {
			return Long.class;
		} else if (type == double.class) {
			return Double.class;
		} else if (type == float.class) {
			return Float.class;
		} else if (type == boolean.class) {
			return Boolean.class;
		} else if (type == byte.class) {
			return Byte.class;
		} else if (type == char.class) {
			return Character.class;
		} else if (type == short.class) {
			return Short.class;
		} else {
			String string = "class '" + type.getName() + "' is not a primitive";
			throw new IllegalArgumentException(string);
		}
	}

	public static void executeSqlBatch(Connection conn, String directoryName, String filePattern) throws SQLException, IOException {
		Statement stmt = conn.createStatement();
		String[] filePatterns = filePattern.split(",");
		for (String fp: filePatterns) {
			List<String> ddlFiles = FileHelpers.getFileList(directoryName+"/"+fp);
			for (String file: ddlFiles) {
				List<String> sqlStatements = FileHelpers.fileToSqlList(file);
				for (String sqlStatement: sqlStatements) {
					//stmt.addBatch(sqlStatement);
					stmt.execute(sqlStatement);
				}
			}
			//stmt.executeBatch();
		}
		stmt.close();
		conn.close();
	}
}