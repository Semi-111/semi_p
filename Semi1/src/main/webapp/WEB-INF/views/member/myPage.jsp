<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>TRAINEE - MYPAGE</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/myPage.css">
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans&display=swap"
	rel="stylesheet">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
</head>
<body>
	<div class="container">
		<div class="myPage">
			<!-- 이미지 업로드 폼: action을 /member/image로, enctype 설정 필수 -->
			<form action="${pageContext.request.contextPath}/member/image"
				method="post" enctype="multipart/form-data">
				<section class="profile-section">
					<h2>
						<i class="bi bi-check-lg"></i> 프로필 이미지
					</h2>
					<div class="image-upload">
						<c:if test="${not empty sessionScope.member.image}">
							<%--<img src="${pageContext.request.contextPath}/uploads/photo/${sessionScope.member.image}" alt="프로필 이미지">--%>
							<%--<img src="${pageContext.request.contextPath}/resources/images/loading.gif	" alt="프로필 이미지">--%>
							<img
								src="${pageContext.request.contextPath}/uploads/photo/${sessionScope.member.image}"
								alt="프로필 이미지">
							<%--<img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="게시글 이미지">--%>

						</c:if>
						<c:if test="${empty sessionScope.member.image}">
							<img
								src="${pageContext.request.contextPath}/resources/images/indexUI/profile.jpg"
								alt="기본 프로필 이미지">
						</c:if>
						<input type="file" id="infoimage" name="infoimage"
							accept="image/*" class="form-control">
						<button type="submit" class="btn uploadBtn">이미지 업로드</button>
					</div>
				</section>
			</form>

			<!-- 내 정보 섹션 (단순 표시용이므로 form 불필요) -->
			<section class="profile-section">
				<h2>
					<i class="bi bi-check-lg"></i> 내 정보
				</h2>
				<div class="profile-info">
					<div class="info-item">
						<span class="label">이름</span> <span class="value">${memberInfo.name}</span>
					</div>
					<div class="info-item">
						<span class="label">닉네임</span> <span class="value">${memberInfo.nickName}</span>
					</div>
					<div class="info-item">
						<span class="label">학과</span>
						<c:choose>
							<c:when test="${memberInfo.lessonNum == 51}">경영학과</c:when>
							<c:when test="${memberInfo.lessonNum == 52}">경찰행정과</c:when>
							<c:when test="${memberInfo.lessonNum == 53}">디자인학과</c:when>
							<c:when test="${memberInfo.lessonNum == 54}">화학공학과</c:when>
							<c:when test="${memberInfo.lessonNum == 55}">컴퓨터응용전자과</c:when>
							<c:when test="${memberInfo.lessonNum == 56}">정보통신학부</c:when>
						</c:choose>
					</div>
				</div>
				<div class="logout">
					<button type="button" class="btn logoutBtn"
						onclick="location.href='${pageContext.request.contextPath}/member/logout'">로그아웃</button>
				</div>
			</section>

			<!-- 계정 섹션 -->
			<section class="profile-section">
				<h2>
					<i class="bi bi-check-lg"></i> 계정
				</h2>
				<div class="account-info">
					<div class="info-item">
						<span class="label">아이디</span> <span class="value">${memberInfo.userId}</span>
					</div>
					<br>
					<h5>
						<i class="bi bi-toggle2-on"></i> 정보 변경
					</h5>
					<div class="actions">
						<button type="button" class="btn changeBtn"
							onclick="location.href='${pageContext.request.contextPath}/member/changePwd'">비밀번호
							변경</button>
						<button type="button" class="btn changeBtn"
							onclick="location.href='${pageContext.request.contextPath}/member/changeEmail'">이메일
							변경</button>
						<button type="button" class="btn changeBtn"
							onclick="location.href='${pageContext.request.contextPath}/member/changeTel'">전화번호
							변경</button>
					</div>
				</div>
			</section>

			<!-- 일정 섹션 -->
			<!-- <section class="profile-section">
			<h2>일정</h2>
			<div class="schedule">
				<p>현재 일정이 없습니다.</p>
			</div>
		</section> -->

			<div>
				<button type="button" class="changeBtn"
					onclick="location.href='${pageContext.request.contextPath}/member/delete'">회원탈퇴</button>
			</div>

		</div>
	</div>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>
