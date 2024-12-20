<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<head>
<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">

<!-- Bootstrap JS -->
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script> -->
</head>

<div class="header-container">
	<!-- 로고 -->
	<div class="logo">
		<a href="${pageContext.request.contextPath}/">
			<img src="${pageContext.request.contextPath}/resources/images/logo.png">
		</a>
	</div>

	<!-- 가운데 -->
	<nav>
		<ul class="nav nav-underline">
		  <li class="nav-item dropdown">
		    <a class="nav-link dropdown-toggle" id="bbsDropdown" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">게시판</a>
		    <ul class="dropdown-menu" aria-labelledby="bbsDropdown">
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=free">자유 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/secretBoard/list">비밀 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=info">정보 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/noticeBoard/list">공지 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=student">새내기 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/lessonBoard/list">학과별 게시판</a></li>
		        <li><hr class="dropdown-divider"></li>
		        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=oldbie">졸업생 게시판</a></li>
		    </ul>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/schedule/schedule2">시간표</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/grade/list">학점계산기</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/lectureReview/list">강의평가</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/market/list">장터</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/map">맛집</a>
		  </li>
		</ul>
	</nav>
	
	<!-- 오른쪽 -->
	<div class="top-icons">
		<!-- 로그인 상태에 따른 버튼 표시 -->
		<c:if test="${empty sessionScope.member}">
			<i class="bi bi-unlock" title="로그인"
				onclick="location.href='${pageContext.request.contextPath}/member/login'"></i>
			<i class="bi bi-person-plus" title="회원가입"
				onclick="location.href='${pageContext.request.contextPath}/member/member'"></i>
		</c:if>
		<c:if test="${not empty sessionScope.member}">
			<i class="bi bi-lock-fill" title="로그아웃"
				onclick="location.href='${pageContext.request.contextPath}/member/logout'"></i>
			<i class="bi bi-person-circle" title="마이페이지"
				onclick="location.href='${pageContext.request.contextPath}/member/myPage'"></i>
			<c:if test="${sessionScope.member.role >= 60}">
				<i class="bi bi-person-gear" title="관리자 페이지"
					onclick="location.href='${pageContext.request.contextPath}/admin/home/main'"></i>
			</c:if>
		</c:if>
	</div>
</div>