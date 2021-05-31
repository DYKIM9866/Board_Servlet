package com.util;

//페이징 처리
public class Paging {
	
	//전체 페이지 수 구하는 메소드
	public int getPageCount(int numPerPage, int dataCount) {
		int pageCount = 0;
		
		pageCount = dataCount/numPerPage;
		
		if(dataCount%numPerPage != 0) {
			pageCount += 1;
		}
		
		return pageCount;
	}
	
	
	//페이징 처리 메소드
	public String pageIndexList(int currentPage, int totalPage,String listUrl) {
		
		StringBuffer result = new StringBuffer();
		
		int numPerBlock = 5;
		int currentPageSetup;
		int page;
		
		if(currentPage == 0 || totalPage == 0) {
			return"";
		}
		if(listUrl.indexOf("?")!=-1) {
			listUrl += "&";
		}else {
			listUrl += "?";
		}
		
		currentPageSetup = (currentPage/numPerBlock) * numPerBlock;
		
		if(currentPage%numPerBlock == 0) {//아직 넘어가기 전이기 때문
			currentPageSetup = currentPageSetup - numPerBlock;
		}
		
		if(totalPage > numPerBlock && currentPageSetup > 0) {
			result.append("<a href=\""+listUrl + "pageNum="
					+currentPageSetup + "\"><이전</a>&nbsp;");
		}
		
		page = currentPageSetup+1;
		while(page<=totalPage && page <= (currentPageSetup + numPerBlock)) {
			
			if(page == currentPage) {
				result.append("<font color=\"Fuchsia\">" + page +"</font>&nbsp;");
			}else {
				result.append("<a href=\"" + listUrl +"pageNum=" + page + "\">" +
						page + "</a>&nbsp;");
			}
			page++;
		}
		
		if(totalPage - currentPageSetup > numPerBlock) {
			result.append("<a href=\""+listUrl + "pageNum" + page + 
					"\">다음></a>&nbsp;");
		}
		
		return result.toString();
	}
	
}
