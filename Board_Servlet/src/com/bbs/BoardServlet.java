package com.bbs;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.DBConn;
import com.util.Paging;

public class BoardServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	protected void doForward(HttpServletRequest req, HttpServletResponse resp
			,String url) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String cp = req.getContextPath();
		
		Connection conn =DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		Paging paging = new Paging();
		
		String uri = req.getRequestURI();
		
		String url;
		if(uri.indexOf("created.do")!=-1) {
			
			url = "/bbs/created.jsp";
			doForward(req, resp, url);
		}else if(uri.indexOf("created_ok.do")!=-1){
			
			BoardDTO dto = new BoardDTO();
			
			//최대 숫자구하기
			int maxNum = dao.getMaxNum();
			
			dto.setBoardNum(maxNum+1);
			dto.setName(req.getParameter("name"));
			dto.setSubject(req.getParameter("subject"));
			dto.setEmail(req.getParameter("email"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setContent(req.getParameter("content"));
			dto.setIpAddr(req.getRemoteAddr());
			
			dao.insertData(dto);
			
			url = cp + "/board/list.do";
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("list.do")!=-1){
			
			String pageNum = req.getParameter("pageNum");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			int numPerPage = 7;
			int currentPage = 1;
			
			if(pageNum != null) {
				currentPage = Integer.parseInt(pageNum);
			}
			if(searchValue == null) {
				searchKey = "subject";
				searchValue = "";
			}else {
				if(req.getMethod().equalsIgnoreCase("GET")) {
					searchValue  = URLDecoder.decode(searchValue,"utf-8");
				}
			}
			
			int dataCount = dao.getDataCount(searchKey,searchValue);
			int totalPage = paging.getPageCount(numPerPage, dataCount);
			
			if(currentPage>totalPage) {
				currentPage = totalPage;
			}
			
			int start = (currentPage-1)*numPerPage + 1; 
			int end = currentPage * numPerPage;
			//System.out.printf("%d %d %s %s",start,end,searchKey,searchValue);
			List<BoardDTO> lst = dao.getLists(start,end,searchKey,searchValue);
			
			String param = "";
			
			if(!searchValue.equals("")) {
				param += "searchKey="+searchKey+
						"&searchValue="+URLEncoder.encode(searchValue,"utf-8");
				
			}
			
			String listUrl = cp + "/board/list.do";
			
			if(!param.equals("")) {
				listUrl += "?" + param;
			}
			
			String pageIndexList = paging.pageIndexList(currentPage, totalPage, listUrl);
			
			String articleUrl = cp + "/board/article.do?pageNum=" + currentPage;
			if(!param.equals("")) {
				articleUrl+="&" +param;
			}
			
			req.setAttribute("lists", lst);
			req.setAttribute("pageIndexList", pageIndexList);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			
			url = "/bbs/list.jsp";
			doForward(req, resp, url);
		}else if(uri.indexOf("article.do")!=-1){
			
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			String pageNum = req.getParameter("pageNum");
			
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue!=null) {
				searchValue = URLDecoder.decode(searchValue,"utf-8");
			}
			dao.updateHitCount(boardNum);
			
			BoardDTO dto = dao.getReadData(boardNum);
			
			if(dto==null) {
				url = cp + "/board/list.do";
				resp.sendRedirect(url);
			}
			
			dto.setBoardNum(boardNum);
			
			int lineSu = dto.getContent().split("\n").length;
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			String param = "pageNum=" + pageNum;
			if(searchValue!=null) {
	   			param+="&searchKey="+searchKey;
	   			param+="&searchValue="+URLEncoder.encode(searchValue,"UTF-8");
	   		}
			req.setAttribute("dto", dto);
	   		req.setAttribute("params", param);
	   		req.setAttribute("lineSu", lineSu);
	   		req.setAttribute("pageNum", pageNum);
	   		
	   		url = "/bbs/article.jsp";
	   		doForward(req, resp, url);
	
		}else if(uri.indexOf("updated.do")!=-1){
			
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			String pageNum = req.getParameter("pageNum");
			
			String searchKey = req.getParameter("searchKey");
   		   	String searchValue = req.getParameter("searchValue");
   		   	
   		   	if(searchValue!=null){
   		   		
   		   		if(req.getMethod().equalsIgnoreCase("get")){
   		   			searchValue = URLDecoder.decode(searchValue,"UTF-8");
   		   		}
   		   	}else{
   		   		searchKey="subject";
   		   		searchValue="";
   		   	}
   		   	
   		   	BoardDTO dto = dao.getReadData(boardNum);
   		   	if(dto==null) {
		   		url = cp+"/sboard/list.do";
		   		resp.sendRedirect(url);	
		   	}
   		   	
   		   	String param = "pageNum=" + pageNum;
   		   	
   		   	if(!searchValue.equals("")){
		   		param+= "&searchKey=" + searchKey;
		   		param+= "&searchValue=" +URLEncoder.encode(searchValue,"UTF-8");
		   	}
   		   	
   		   	req.setAttribute("dto", dto);
		   	req.setAttribute("pageNum", pageNum);
		   	req.setAttribute("params", param);
		   	req.setAttribute("searchKey", searchKey);
		   	req.setAttribute("searchValue", searchValue);
			
		   	url = "/bbs/updated.jsp";
   			doForward(req,resp,url);
		}else if(uri.indexOf("updated_ok.do")!=-1) {
			
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
   			String pageNum = req.getParameter("pageNum");
   		   	String searchKey = req.getParameter("searchKey");
   		   	String searchValue = req.getParameter("searchValue");
   		   	
   		   	BoardDTO dto = new BoardDTO();
   		   	
   		   	dto.setBoardNum(Integer.parseInt(req.getParameter("boardNum")));
		   	dto.setSubject(req.getParameter("subject"));
		   	dto.setName(req.getParameter("name"));
		   	dto.setEmail(req.getParameter("email"));
		   	dto.setPwd(req.getParameter("pwd"));
		   	dto.setContent(req.getParameter("content"));
		   	
		   	dao.updateData(dto);
		   	
		   	String param = "pageNum"+pageNum;
		   	
		   	if(!searchValue.equals("")){
		   		param+= "&searchKey=" + searchKey;
		   		param+= "&searchValue=" +URLEncoder.encode(searchValue,"UTF-8");
		   	}
		   	url = cp+"/board/list.do?" + param;
		   	resp.sendRedirect(url);
			
		}else if(uri.indexOf("deleted.do")!=-1) {
			
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
   			String pageNum = req.getParameter("pageNum");
   			String searchKey = req.getParameter("searchKey");
   			String searchValue = req.getParameter("searchValue");
			
   			dao.deleteData(boardNum);
   			
   			String param = "pageNum=" +pageNum;
   			if(searchValue!=null) {
   				param += "&searchKey=" +searchKey;
   				param +="&searchValue=" + searchValue;
   			}
   			url = cp+"/board/list.do?" + param;
   			resp.sendRedirect(url);
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
