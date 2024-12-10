<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<header>
	<div class="top-icons">
		<!-- 로그인 상태에 따른 버튼 표시 -->
		<c:if test="${empty sessionScope.member}">
			<!-- 로그인 버튼 -->
			<i class="bi bi-box-arrow-in-right" title="로그인" onclick="location.href='${pageContext.request.contextPath}/member/login'"></i>
			<!-- 회원가입 버튼 -->
			<i class="bi bi-person-plus" title="회원가입" onclick="location.href='${pageContext.request.contextPath}/member/member'"></i>
		</c:if>
		<c:if test="${not empty sessionScope.member}">
			<!-- 로그아웃 버튼 -->
			<i class="bi bi-box-arrow-right" title="로그아웃" onclick="location.href='${pageContext.request.contextPath}/member/logout'"></i>
			<!-- 알림 버튼 -->
			<i class="bi bi-bell" title="알림"></i>
			<c:if test="${sessionScope.member.role >= 51}">
				<!-- 관리자 버튼 -->
				<i class="bi bi-gear" title="관리자" onclick="location.href='${pageContext.request.contextPath}/admin'"></i>
			</c:if>
		</c:if>
	</div>
	<nav>
		<div class="logo">
			<img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="트레이니 로고">
		</div>
		<i class="bi bi-list hamburger"></i>
		<div class="nav-links">
			<a href="#">게시판</a>
			<a href="#">시간표</a>
			<a href="#">학점계산기</a>
			<a href="#">친구</a>
			<a href="#">장터</a>
			<a href="#">맛집</a>
		</div>
	</nav>
</header>
