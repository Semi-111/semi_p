<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<header>
	<div class="header-container">
		<!-- 로고 -->
		<div class="logo">

			<a href="${pageContext.request.contextPath}/">
				<img src="${pageContext.request.contextPath}/resources/images/logo.png">
			</a>
		</div>

		<!-- 가운데 -->
		<nav>
			<div class="nav-links">
				<ul class="nav nav-underline">
					<li class="nav-item">
						<a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">게시판</a>
						<ul class="dropdown-menu">
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=free">자유 게시판</a></li>
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/secretBoard/list">비밀 게시판</a></li>
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=info">정보게시판</a></li>
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/eventBoard/list">이벤트/공지</a></li>
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=student">새내기게시판</a></li>
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/lessonBoard/list">학과별 게시판</a></li>
					        	<!-- 학과별 게시판 들어가면 6개의 학과가 나오도록. 6개 중 하나 선택시 해당 학과 게시판으로 이동 ..이 나을듯요 -->
					        <li><hr class="dropdown-divider"></li>
					        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=oldbie">졸업생 게시판</a></li>
					        <li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lectureReview/list">강의 평가</a></li>

				      	</ul>
					</li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/schedule/list">시간표</a></li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/grade/list">학점계산기</a></li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/friends/list">친구</a></li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/market/list">장터</a></li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/food/list">맛집</a></li>
					<li class="nav-item1"><a class="nav-link">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
				</ul>
			</div>
		</nav>

		<!-- 오른쪽 -->
		<div class="top-icons">
			<!-- 로그인 상태에 따른 버튼 표시 -->
			<c:if test="${empty sessionScope.member}">
				<!-- 로그인 버튼 -->
				<i class="bi bi-unlock" title="로그인"
					onclick="location.href='${pageContext.request.contextPath}/member/login'"></i>
				<!-- 회원가입 버튼 -->
				<i class="bi bi-person-plus" title="회원가입"
					onclick="location.href='${pageContext.request.contextPath}/member/member'"></i>
			</c:if>
			<c:if test="${not empty sessionScope.member}">
				<!-- 로그아웃 버튼 -->
				<i class="bi bi-lock-fill" title="로그아웃"
					onclick="location.href='${pageContext.request.contextPath}/member/logout'"></i>
				<!-- 알림 버튼 -->
				<i class="bi bi-person-circle" title="마이페이지"></i>
				<c:if test="${sessionScope.member.role >= 51}">
					<!-- 관리자 버튼 -->
					<i class="bi bi-person-gear" title="관리자 페이지"
						onclick="location.href='${pageContext.request.contextPath}/admin/home/main'"></i>
				</c:if>
			</c:if>
		</div>
	</div>
</header>