package com.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class BoardDAO {
	
	private Connection conn; //db연결할 것
	
	public BoardDAO(Connection conn) { //생성자를 이용한 의존성 주입
		this.conn = conn;
	}
	
	//num의 최대값 구하기
	public int getMaxNum() {
		
		int maxNum = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select nvl(max(boardNum),0) from bbs";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				maxNum = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println("여기2?");
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
			
			int result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("여기3?");
			System.out.println(e.toString());
		}
		
	}
	
	public int getDataCount(String searchKey, String searchValue) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			searchValue = "%" + searchValue + "%";
			
			sql = "select nvl(count(*),0) from bbs ";
			sql+= "where " + searchKey + " like ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("여기4?");
			System.out.println(e.toString());
		}
		return result;
	}
	
	public List<BoardDTO> getLists(int start,int end,
			String searchKey, String searchValue){
		
		List<BoardDTO> lists = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			searchValue = "%" + searchValue + "%";
			
			sql = "select * from( ";
			sql+= "select rownum rnum,data.* from( ";
			sql+= "select boardNum,name,subject,hitCount,to_char(created,'yyyy-mm-dd') created ";
			sql+= "from bbs where "+ searchKey + " like ? order by boardNum desc) data) ";
			sql+= "where rnum>=? and rnum<=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO vo = new BoardDTO();
				
				vo.setBoardNum(rs.getInt("boardNum"));
				vo.setName(rs.getString("name"));
				vo.setSubject(rs.getString("subject"));
				vo.setHitCount(rs.getInt("hitCount"));
				vo.setCreated(rs.getString("created"));
				
				lists.add(vo);
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("여기1?");
			System.out.println(e.toString());
		}
		return lists;
	}
	
	public void updateHitCount(int boardNum) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update bbs set hitCount=hitCount+1 where boardNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("힛카운트 에러");
			System.out.println(e.toString());
		}
	}
	
	public BoardDTO getReadData(int boardNum) {
		
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select boardNum,name,email,subject,pwd,content,hitCount,";
			sql+= "ipAddr,to_char(created,'yyyy-mm-dd') created ";
			sql+= "from bbs where boardNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setEmail(rs.getString("email"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setIpAddr(rs.getString("ipAddr"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
	}
	
	public void updateData(BoardDTO dto) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update bbs set name=?,pwd=?,email=?,subject=?,";
			sql+= "content=? where boardNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getBoardNum());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	
	public void deleteData(int boardNum) {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete bbs where boardNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
