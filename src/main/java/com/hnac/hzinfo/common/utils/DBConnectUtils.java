package com.hnac.hzinfo.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnectUtils{
	private static boolean inMegaData(ResultSetMetaData m, String col){
		int length = 0;
		try {
			length =  m.getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i <= length; i++){
			try {
				if (m.getColumnName(i).compareTo(col) == 0){
					return true;
				}
			} catch (SQLException e) {
				break;
			}
		}
		return false;
	}
	
	private static <T> int reflectData(ResultSet rs, Class type, List<T> list){
		
		int count = 0;
		try{
			Field[] fs = type.getDeclaredFields();
			ResultSetMetaData m= rs.getMetaData();
			while (rs.next() == true){
				T k = (T)type.newInstance();
				for(int i = 0 ; i < fs.length; i++){  
					try{
			           Field f = fs[i];  
			           if (!inMegaData(m, f.getName()))
			        	   continue;
			           f.setAccessible(true); //设置些属性是可以访问的 
			           String strtype = f.getType().toString();//得到此属性的类型  
			           if (strtype.endsWith("String")) {  
			               f.set(k, rs.getString(f.getName())) ;        //给属性设值  
			           }else if(strtype.endsWith("int") || strtype.endsWith("Integer")){   
			              f.set(k, rs.getInt(f.getName())) ;       //给属性设值  
			           }else if (strtype.endsWith("Date")) {  
			               f.set(k, rs.getDate(f.getName())) ;        //给属性设值  
			           }
			           else if (strtype.endsWith("boolean")) {  
			               f.set(k, rs.getBoolean(f.getName())) ;        //给属性设值  
			           }
			           else if (strtype.endsWith("double")) {  
			               f.set(k, rs.getDouble(f.getName())) ;        //给属性设值  
			           }
			           else{
			        	   System.out.println("unhandled type of dbtype");
			           }
			        }
					catch (Exception e){
						
					}
				}
				count++;
				list.add(k);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/*
	 * 获取对象的方法
	 */
	public static <T> int Query(String connStr, String sql, Class type, List<T> list){
		int count = 0;
		Connection con = null;
		ResultSet rs = null;
        Statement ts = null;
		try {
			con = DriverManager.getConnection(connStr);
            ts = con.createStatement();
            rs = ts.executeQuery(sql);
            count = reflectData(rs, type, list);
            ts.close();
            ts = null;
            con.close();
            con = null;
        } catch (Exception e) {
        }
		try {
            if (ts != null) {
            	ts.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
            	con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
	}
	
	private static List<List<String>> parseData(ResultSet rs, List<String> colname){
		//获取列名
		if (colname == null)
			colname = new ArrayList<String>();
		List<List<String>> ret = new ArrayList<List<String>>();
		int length = 0;
		try {
			ResultSetMetaData m= rs.getMetaData();
			length =  m.getColumnCount();
			for (int i = 1; i <= length; i++){
				try {
					colname.add(m.getColumnName(i));
				} catch (SQLException e) {
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//组值
		int count = 0;
		try{
			while (rs.next() == true){
				List<String> row = new ArrayList<String>();
				for(int i = 0 ; i < colname.size(); i++){  
					try{
						row.add(rs.getString(colname.get(i)));
			        }
					catch (Exception e){
						row.add("");
					}
				}
				count++;
				ret.add(row);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	/*
	 * 获取数据的方法，colname可传null
	 */
	public static List<List<String>> QueryString(String connStr, String sql, List<String> colname){
		Connection con = null;
		ResultSet rs = null;
        Statement ts = null;
        List<List<String>> ret = new ArrayList<List<String>>();
		try {
			con = DriverManager.getConnection(connStr);
            ts = con.createStatement();
            rs = ts.executeQuery(sql);
            ret = parseData(rs, colname);
            ts.close();
            ts = null;
            con.close();
            con = null;
        } catch (Exception e) {
        }
		try {
            if (ts != null) {
            	ts.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
            	con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
	}
}
