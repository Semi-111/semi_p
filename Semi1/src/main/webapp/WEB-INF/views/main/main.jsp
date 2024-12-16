<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>트레니 에브리타임</title>
<%-- 
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/util-jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/main.js"></script>
 --%>
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/main.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/main.js"></script>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
</head>

<body>
	<form name="mainForm" method="get">
		<div class="container-fluid">
			<div class="row">
				<!-- 왼쪽 사이드 -->
				<div class="col-2 d-none d-lg-block" id="leftSide">
					<div class="myPage">프로필</div>
					<div class="row">
						<div class="col">
							<div class="weather">날씨 API</div>
						</div>
					</div>
					<div class="row slider-container">
						<div class="col">
							<div class="slider">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/1.jpg">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/2.jpg">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/3.jpg">
							</div>
						</div>
					</div>
					<div class="row slider-container">
						<div class="col">
							<div class="slider">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/4.jpg">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/5.jpg">
								<img
									src="${pageContext.request.contextPath}/resources/images/mainTest/6.jpg">
							</div>
						</div>
					</div>
				</div>

				<!-- 중앙 화면 -->
				<div class="col-7 col-lg-7" id="mainContent">
					<div class="survey">
						설문조사
					</div>
					<div class="row">
						<div class="col" id="bbs-container">
							<div class="container text-center">
								<div class="row row-cols-2 d-flex justify-content-center align-items-center">
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>자유 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>학과별 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>비밀 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>졸업생 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>정보 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>새내기 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
									<div class="col" id="bbs">
										<table class="board" style="border: 1px solid black">
											<tr>
												<th>이벤트 게시판</th>
											</tr>
											<tr>
												<td>게시글 1</td>
											</tr>
											<tr>
												<td>게시글 2</td>
											</tr>
											<tr>
												<td>게시글 3</td>
											</tr>
											<tr>
												<td>게시글 4</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- 오른쪽 사이드 -->
				<jsp:include page="/WEB-INF/views/main/rightSide.jsp" />

			</div>
		</div>
	</form>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>