<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); 
   String cp = request.getContextPath();

%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게 시 판</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/bbs/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=cp%>/bbs/css/article.css"/>
</head>
<body>
	<div id="bbs">
		<div id="bbs_title">
		게 시 판
		</div>
		<div id="bbsArticle">
			<div id="bbsArticle_header">
				게시물 제목
			</div>
			<div class="bbsArticle_bottomLine">
				<dl>
					<dt>작성자</dt>
					<dd>유인나</dd>
					<dt>줄 수</dt>
					<dd>20</dd>
				</dl>
			</div>
			<div class="bbsArticle_bottomLine">
				<dl>
					<dt>등록일</dt>
					<dd>2021-04-07</dd>
					<dt>조회수</dt>
					<dd>10</dd>
				</dl>
			</div>
			<div id="bbsArticle_content">
				<table width="600">
					<tr>
						<td style="padding: 20px 80px 20px 62px;" valign="top" height="200">
						게시물내용
						</td>
					</tr>
				</table>
			</div>
			<div class="bbsArticle_noLine" style="text-align:right;">
				From : 127.0.0.1
			</div>
			<div id = "bbsArticle_footer">
				<div id = "leftFooter">
					<input type="button" value=" 수정 " class="btn2" onclick=""/>
					<input type="button" value=" 삭제 " class="btn2" onclick=""/>		
				</div>
				<div id = "rightFooter">
					<input type="button" value=" 리스트 " class="btn2" onclick=""/>
				</div>
			</div>
		</div>
	</div>














<br><br><br><br><br><br>
<br><br><br><br><br><br>
</body>
</html>