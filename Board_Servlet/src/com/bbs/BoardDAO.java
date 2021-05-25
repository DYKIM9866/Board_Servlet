package com.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;



public class BoardDAO {
	
	private Connection conn; //db연결할 것
	
	public BoardDAO(Connection conn) { //생성자를 이용한 의존성 주입
		this.conn = conn;
	}
	
	//num의 최대값 구하기
	public int getMaxNum() {
		
		int maxNum = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "select nvl(max(num),0) from bbs";
			
			pstmt = conn.prepareStatement(sql);
			maxNum = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxNum;
	}
	
	//데이터 삽입
	public void insertData(BoardDTO dto) {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into bbs (boardNum,name,pwd,email,subject,content,";
			sql+= "ipAddr,hitCount,created) ";
			sql+= "values(?,?,?,?,?,?,?,0,sysdate)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPwd());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getSubject());
			pstmt.setString(6, dto.getContent());
			pstmt.setString(7, dto.getIpAddr());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
