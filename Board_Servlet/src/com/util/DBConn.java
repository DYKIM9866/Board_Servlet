package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
	
	private static Connection conn = null;
	
	public static Connection getConnection(){
		
		String url = "jdbc:oracle:thin:@localhost:1521:TestDB";
		String user = "DY_PERSONAL_PROJECT";
		String pwd = "a123";
		
		if(conn==null) {//conn이 비어있으면 한마디로 연결이 안되어있으면
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url,user,pwd);
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		
		}
		
		return conn;
	}
	
	public static void close() {
		if(conn==null) {//이미 연결 끊어졌으면 return
			return;
		}
		
		try {
			if(!conn.isClosed())//끊어져 있지않으면 끊어라
				conn.close();
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		conn = null; //비워주기
	}
	
	

}
