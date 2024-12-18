<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TRAINEE - MYPAGE</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />


</head>
<body>
	<div class="myPage">
		<form name="myPageForm" method="post">
			<div class="first-profile">
				<h3>내 정보</h3>
				<div class="myInfo">
					<p>아이디: ${memberInfo.userId}</p>
					<p>이름: ${memberInfo.name}</p>
					<p>닉네임: ${memberInfo.nickName}</p>
					<p>학과: ${department}</p>
				</div>
				<div class="logout">
					<button type="button" class="logoutBtn"
						onclick="location.href='${pageContext.request.contextPath}/member/logout'">로그아웃</button>
				</div>
			</div>

			<div class="second-profile">
				<h3>계정</h3>
				<div class="myAccount">
					<p>아이디 |</p>
					<p>
						학과 설정 | <select>
							<option value="dept1">경영학과</option>
							<option value="dept2">경찰행정과</option>
							<option value="dept3">디자인학과</option>
							<option value="dept4">화학공학과</option>
							<option value="dept5">컴퓨터응용전자과</option>
							<option value="dept6">정보통신학과</option>
						</select>
					</p>
					<p>
						학과 처리 내역 |
						<button type="button" onclick="openModal()">보기</button>
					</p>

					<!-- 모달 창 -->
					<div id="myModal" style="display: none;">
						<div class="modal-content">
							<span class="close" onclick="closeModal()">&times;</span>
							<%-- <p>${memberInfo.CA_day}</p> --%>
						</div>
					</div>
					<div>
						<!-- 비밀번호 변경 -->
						<a href="/member/changePassword">비밀번호 변경</a>
					</div>
					<div>
						<!-- 이메일 변경 -->
						<a href="/member/changeEmail">이메일 변경</a>
					</div>
				</div>
			</div>

			<div class="third-profile">
				<h3>일정</h3>

			</div>
		</form>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
	</div>
</body>
</html>