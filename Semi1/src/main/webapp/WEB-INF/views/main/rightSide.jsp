<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오른쪽 사이드 화면</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/main.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/rightSide.js"></script> <!-- js 링크 -->
</head>
<body>
	<div class="col-3 d-none d-lg-block" id="rightSide">
		<div class="search">
			<input type="text" placeholder="검색">
		</div>
		<div class="board" id="realTime">
			<h4>실시간 인기글</h4>
			<ul>
				<li>로그인 후 확인 가능합니다.</li>
			</ul>
		</div>
		<div class="board" id="weekly">
			<h4>주간 인기글</h4>
			<ul>
				<li>로그인 후 확인 가능합니다.</li>
			</ul>
		</div>
		<div class="board" id="recentLectures">
			<h4>최근 강의평</h4>
			<ul>
				<li>로그인 후 확인 가능합니다.</li>
			</ul>
		</div>
	</div>
</body>
</html>