package com.ibm.wfm.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;


public class DataMarshaller {
	
	public static void main(String[] args) {

		
	}

	public static void listPublicMethods(Class klass) {
		for (Method method : klass.getDeclaredMethods()) {
			int modifiers = method.getModifiers();
			if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
				System.out.println(method.getName());
			}
		}
	}
	
    public static <T> void writeCsv2PrintWriter(Class<T> type, PrintWriter writer, List<T> objects) {

        try {

            ColumnPositionMappingStrategy<T> mapStrategy
                    = new ColumnPositionMappingStrategy<>();

            mapStrategy.setType(type);

            String[] columns = new String[type.getDeclaredFields().length];
            int i=0;
            for (Field field : type.getDeclaredFields()) {
            	columns[i++]=field.getName();
            	writer.write((i>1?",":"")+field.getName());
            }
            writer.write(System.lineSeparator());
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<T> btcsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(objects);

        } catch (CsvException ex) {
            System.out.println("Error mapping Bean to CSV, "+ ex);
        }
    }
    
    public static <T> List<T> getObjectListFromExcel(Class<T> type,  String excelFileName, String excelTabName) throws IOException, NoSuchMethodException, SecurityException {
    	List<String[]> mySheet = ExcelReader.readExcelPage(excelFileName, excelTabName);
    	List<T> list = new ArrayList<T>(); //Return an empty array, instead of null, if the query has no rows;
    	int rowNum = 0;
    	String[] columns = null;
    	for (String[] row: mySheet) {
    		if (++rowNum==1) columns = row;
    		else {
	    		T myObject = type.cast(buildObjectFromList(type, row, columns, false, 0, false));
	    		list.add(myObject);
    		}
    	}
    	return list;
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values) {
    	return buildObjectFromList(type, values, null, true, 0, false);
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values, String[] columns) {
    	return buildObjectFromList(type, values, columns, true, 0, false);
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values, int iStart) {
    	return buildObjectFromList(type, values, null, true, iStart, false);
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values, boolean processSeq, int iStart) {
    	return buildObjectFromList(type, values, null, processSeq, iStart, false);
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values, int iStart, boolean includeScd) {
    	return buildObjectFromList(type, values, null, true, iStart, includeScd);
    }
    
    public static <T> Object buildObjectFromList(Class<T> type, String[] values, String[] columns, int iStart, boolean includeScd) {
    	return buildObjectFromList(type, values, columns, true, iStart, includeScd);
    }
	
	public static <T> Object buildObjectFromList(Class<T> type, String[] values, String[] columns, boolean processSeq, int iStart, boolean includeScd) {
		try {
			Constructor<T> constructor = type.getConstructor();
			T t = constructor.newInstance();
			int i = iStart;
			for (Field field : type.getDeclaredFields()) {
				DbTable dbColumn = field.getAnnotation(DbTable.class);
				ExcelSheet excelColumn = field.getAnnotation(ExcelSheet.class);
				if ((dbColumn!=null && !dbColumn.isId() && includeScd==dbColumn.isScd())
				||  (excelColumn!=null 
				//|| processSeq)
				)
				) {
					field.setAccessible(true);
					if (i<values.length) {
						String value = null;
						if (processSeq) value = values[i++];
						else {
							value = getValue4Field(field,values, columns);
						}
						if (value==null) {
							if (field.getType()==boolean.class) field.set(t,false);
							else if (field.getType()==int.class||field.getType()==short.class||field.getType()==float.class) field.set(t,0);
							else if (field.getType()==long.class) field.set(t,0L);
							else if (field.getType()==char.class) field.set(t,' ');
							else if (field.getType()==double.class) field.set(t,0.0);
							
							else field.set(t,value);
						}
						else if (field.getType()==String.class) field.set(t,value);
						else if (field.getType()==Integer.class) field.set(t, value.trim().length()==0?null:Integer.parseInt(value));
						else if (field.getType()==int.class) field.set(t, (value.trim().length()==0 || !Helpers.isNumeric(value))?-1:(int)Double.parseDouble(value));
						else if (field.getType()==Long.class || field.getType()==long.class) field.set(t, value.trim().length()==0?null:Long.parseLong(value));
						else if (field.getType()==Short.class || field.getType()==short.class) field.set(t, value.trim().length()==0?null:Short.parseShort(value));
						else if (field.getType()==Double.class) field.set(t, value.trim().length()==0?null:Double.parseDouble(value));
						else if (field.getType()==double.class) field.set(t, value.trim().length()==0.0?null:Double.parseDouble(value));
						else if (field.getType()==Boolean.class || field.getType()==boolean.class) {
							if (value.trim().length()==0 || value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("false")) field.set(t,false);
							else field.set(t, true);
						}
						else if (field.getType()==Float.class || field.getType()==float.class) field.set(t, value.trim().length()==0?null:Float.parseFloat(value));
						else if (field.getType()==java.sql.Timestamp.class) field.set(t, value.trim().length()==0?null:Timestamp.valueOf(value)); ////yyyy-mm-dd hh:mm:ss[.fffffffff]
						else if (field.getType()==java.sql.Date.class) field.set(t, value.trim().length()==0?null:java.sql.Date.valueOf(value));
						else {
							System.out.println("Unaccounted for field type: "+ field.getType()+", value="+value);
						}
					}
				}
			} //end - for (Field field : type.getDeclaredFields())
			return t;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getValue4Field(Field field, String[] values, String[] columns) {
		ExcelSheet column = field.getAnnotation(ExcelSheet.class);
		if (column!=null) {
			if (column.columnNum()>-1 && column.columnNum()<values.length) 
				return values[column.columnNum()];
			else if (column.columnName()!=null) {
				for (int i=0; i<columns.length; i++) {
					if (column.columnName().trim().equalsIgnoreCase(columns[i].trim()))
						return i<values.length?values[i]:null;
				}
			}
			
		}
		
		return null;		
	}
    
    public static ArrayList<Object> buildObjectListFromExcel(String className, String excelFileName) {
		return buildObjectListFromExcel(className, excelFileName, null);
	}
	
    public static <T> List<T> buildObjectListFromExcel(Class<T> type, String excelFileName, String excelTabName) {
    	return null;
    }
    
	public static ArrayList<Object> buildObjectListFromExcel(String className, String excelFileName, String excelTabName) {
		ArrayList<Object> objects = null;
		try {
			ArrayList<String[]> sheet = ExcelReader.readExcelPage(excelFileName, excelTabName);
			
			if (sheet==null) {
				System.out.println("Error DataMarshaller.buildObjectListFromExcel(). No sheet returned from ExcelReader.readExcelPage("
						+excelFileName+", "+excelTabName);
				return null;
			}
			
			String[] classAttributes = sheet.get(0);
			for (int i=1; i<sheet.size(); i++) {
				if (objects==null) objects = new ArrayList<Object>(sheet.size());
				objects.add(DataMarshaller.buildObjectFromList(className, classAttributes, sheet.get(i)));
			} //end - for (int i=1; i<ddlSheet.size(); i++) 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return objects;
	}
	
	public static ArrayList<String> buildStringListFromExcel(String excelFileName, String excelTabName, int cellNumber) {
		ArrayList<String> objects = null;
		try {
			ArrayList<String[]> sheet = ExcelReader.readExcelPage(excelFileName, excelTabName);
			
			if (sheet==null) {
				System.out.println("Error DataMarshaller.buildStringListFromExcel(). No sheet returned from ExcelReader.readExcelPage("
						+excelFileName+", "+excelTabName);
				return null;
			}
			
			for (int i=1; i<sheet.size(); i++) {
				if (sheet.get(i).length>cellNumber && sheet.get(i)[cellNumber].trim().length()>0) {
					if (objects==null) objects = new ArrayList<String>(sheet.size());
					objects.add(sheet.get(i)[cellNumber]);
				}
			} //end - for (int i=1; i<ddlSheet.size(); i++) 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return objects;
	}
	
	public static Object buildObjectFromList(String className, String[] classAttributes, String[] values) {
		Class klass = null;
		Object businessObject = null;
		try {
			klass = Class.forName(className);
			businessObject = klass.newInstance();
			
			Method[] methods = klass.getMethods();
			
			Class[] paramTypes = null;
			Object[] methodArgs = new Object[1];
			
			for (int i=0; i<classAttributes.length; i++) {
				Method setMethod = null;
				String methodName = "set" + classAttributes[i].substring(0, 1).toUpperCase() + classAttributes[i].substring(1);
				for (int m=0; m<methods.length; m++) {
					if (methods[m].getName().equals(methodName)) {
						setMethod = methods[m];
						m = methods.length;
					}
				} //end - for (int m=0; m<methods.length; m++)
				
				if (setMethod==null) {
					System.out.println("Error: Class Attribute "+ classAttributes[i] + " does not have a setter method.");
				}
				else {
					paramTypes = setMethod.getParameterTypes();
					if (paramTypes[0].getSimpleName().equals("String")) {
						if (i>=values.length)
							methodArgs[0]="";
						else
							methodArgs[0]=values[i];
						setMethod.invoke(businessObject, methodArgs);
					}
					else if (paramTypes[0].getSimpleName().equals("int") || paramTypes[0].getSimpleName().equals("Integer")) {
						if (i>=values.length || values[i].equals("") || values[i].equals(" ") || !Helpers.isNumeric(values[i]))
							methodArgs[0]=(new Integer(0));
						else
							methodArgs[0]=(new Integer(Helpers.parseInt(values[i]))); //.intValue();
						setMethod.invoke(businessObject, methodArgs);
					}
					else if (paramTypes[0].getSimpleName().equals("boolean") || paramTypes[0].getSimpleName().equals("Boolean")) {
						if (i>=values.length)
							methodArgs[0]=(new Boolean("False"));
						else if (values[i].equalsIgnoreCase("true")|| values[i].equalsIgnoreCase("t") || values[i].equalsIgnoreCase("yes") || values[i].equalsIgnoreCase("y") || values[i].equalsIgnoreCase("1"))
							methodArgs[0]=(new Boolean("True"));
						else if (values[i].equalsIgnoreCase("false")|| values[i].equalsIgnoreCase("f") || values[i].equalsIgnoreCase("no") || values[i].equalsIgnoreCase("n") || values[i].equalsIgnoreCase("0"))
							methodArgs[0]=(new Boolean("False"));
						else
							methodArgs[0]=(new Boolean(values[i])); //.booleanValue();
						setMethod.invoke(businessObject, methodArgs);
					}
					else if (paramTypes[0].getSimpleName().equals("double") || paramTypes[0].getSimpleName().equals("Double")) {
						if (i>=values.length ||values[i].equals("") || values[i].equals(" ") || !Helpers.isNumeric(values[i]))
							methodArgs[0]=(new Double("0.0"));
						else
							methodArgs[0]=(new Double(values[i]));
						setMethod.invoke(businessObject, methodArgs);
					}
					//Add char, Character, float, Float, byte, Byte, short, Short, long, Long
					else {
						System.out.println("Have not handled datatype: "+ paramTypes[0].getSimpleName());
					}
				}
				
			} //end - for (int i=0; i<classAttributes.length; i++)
			
			return businessObject;
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
			return null;
		}
	}	
	
	/*
	 * Old method
	 */
	public static Object getObjectFromArray(String[] columns, String[] values, String className) {
		// Create an instance of the named class using the default constructor
		Class klass = null;
		Object businessObject = null;
		try {
			klass = Class.forName(className);
			businessObject = klass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}

		//listPublicMethods( klass);
		
		Class[] paramTypes = new Class[1];
		Object[] methodArgs = new Object[1];
		Method setMethod = null;
		String methodName = null;
		boolean process=true;

		// Loop through columns and values and use the SETTER methods to set the
		// object's values
		for (int i = 0; i < columns.length; i++) {
			try {
				process = true;
				if (i<values.length) {
					methodName = "set" + columns[i].substring(0, 1).toUpperCase() + columns[i].substring(1);
	
					if (methodName.endsWith("Dt")) {
						//paramTypes[0] = Date.class;
						//methodArgs[0] = new Date(values[i]);
						process=false;
					} else if (methodName.endsWith("Amt")) {
						paramTypes[0] = Double.class;
						try {
							methodArgs[0] = new Double(values[i]);
						}
						catch (java.lang.NumberFormatException e) {
							process=false;
						}				
					} else if (methodName.endsWith("Id")) {
						paramTypes[0] = Integer.class;
						methodArgs[0] = (new Integer(values[i])).intValue();	
					} else {
						paramTypes[0] = String.class;
						methodArgs[0] = values[i];
					}
				} //end - if (i<values.length)
				else {
					process = false;
				}

				if (process) {
					setMethod = klass.getMethod(methodName, paramTypes);
					setMethod.invoke(businessObject, methodArgs);
				}
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				//return null;
			}
		}
		return businessObject;
	}
	
}
