<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>2024학년도 1학기 수강신청 안내 - Trainee</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/base.css">
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕",
		sans-serif;
}

body {
	background-color: #f5f5f5;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

/* 헤더 스타일 */
header {
	background: white;
	border-bottom: 1px solid #e1e1e1;
}

nav {
	max-width: 1200px;
	margin: 0 auto;
	padding: 8px 20px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.logo img {
	height: 45px;
	width: auto;
	padding: 5px 0;
}

.nav-links {
	display: flex;
	gap: 2rem;
}

.nav-links a {
	text-decoration: none;
	color: #666;
	font-size: 14px;
}

.nav-links a:hover {
	color: #a855f7;
}

.nav-links a.active {
	color: #a855f7;
	font-weight: bold;
}

/* 푸터 스타일 */
footer {
	background-color: #f9fafb;
	padding: 2rem 1rem;
	margin-top: 40px;
	border-top: 1px solid #eee;
}

.footer-content {
	max-width: 1200px;
	margin: 0 auto;
	font-size: 12px;
	color: #666;
}

.footer-links {
	margin-top: 1rem;
	display: flex;
	gap: 1rem;
}

.footer-links a {
	text-decoration: none;
	color: #666;
}

.footer-links a:hover {
	color: #a855f7;
}

/* 게시글 상세 스타일 */
.post-container {
	max-width: 800px;
	margin: 0 auto;
	padding: 20px;
}

.post-detail {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
}

.post-header {
	padding: 20px;
	border-bottom: 1px solid #e5e7eb;
}

.post-title {
	font-size: 24px;
	color: #333;
	margin-bottom: 15px;
}

.post-meta {
	display: flex;
	justify-content: space-between;
	color: #666;
	font-size: 14px;
	flex-wrap: wrap;
	gap: 10px;
}

.post-info {
	display: flex;
	gap: 15px;
}

.post-content {
	padding: 30px 20px;
	line-height: 1.6;
	color: #333;
	font-size: 15px;
}

.post-actions {
	padding: 15px 20px;
	border-top: 1px solid #e5e7eb;
	display: flex;
	justify-content: space-between;
	gap: 10px;
}

.action-left {
	display: flex;
	gap: 10px;
}

.action-right {
	display: flex;
	gap: 10px;
}

.btn {
	padding: 8px 16px;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	display: flex;
	align-items: center;
	gap: 6px;
	border: 1px solid #e5e7eb;
	background: white;
}

.btn-purple {
	background: #a855f7;
	color: white;
	border: none;
}

.btn-purple:hover {
	background: #9333ea;
}

.btn-gray {
	color: #666;
}

.btn-gray:hover {
	background: #f9fafb;
}

.btn-red {
	color: #ef4444;
	border-color: #ef4444;
}

.btn-red:hover {
	background: #fef2f2;
}

/* 댓글 영역 */
.comments-section {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.comments-header {
	padding: 15px 20px;
	border-bottom: 1px solid #e5e7eb;
	font-weight: bold;
	color: #333;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.comments-count {
	color: #a855f7;
}

.comment-write {
	padding: 20px;
	border-bottom: 1px solid #e5e7eb;
}

.comment-textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #e5e7eb;
	border-radius: 4px;
	resize: none;
	margin-bottom: 10px;
	font-size: 14px;
	min-height: 100px;
}

.comment-list {
	padding: 0 20px;
}

.comment-item {
	padding: 15px 0;
	border-bottom: 1px solid #e5e7eb;
}

.comment-item:last-child {
	border-bottom: none;
}

.comment-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8px;
}

.comment-author {
	font-weight: 500;
	color: #333;
	font-size: 14px;
}

.comment-date {
	color: #666;
	font-size: 12px;
}

.comment-text {
	color: #4b5563;
	font-size: 14px;
	line-height: 1.5;
}

.comment-actions {
	margin-top: 8px;
	display: flex;
	gap: 15px;
}

.comment-action {
	color: #666;
	font-size: 12px;
	cursor: pointer;
}

.comment-action:hover {
	color: #a855f7;
}

/* 이미지 CSS */
.img-box {
    max-width: 100%;
    margin: 20px 0;
}

.img-box img {
    max-width: 100%;
    height: auto;
}

/* 목록으로 버튼 */
.back-link {
	display: inline-block;
	margin-bottom: 20px;
	color: #666;
	text-decoration: none;
	font-size: 14px;
}

.back-link:hover {
	color: #a855f7;
}
</style>
</head>
<head>
<title>게시글 상세 - Trainee</title>

</head>
<body>
	<header>
		<nav>
			<div class="logo">
				<img
					src="${pageContext.request.contextPath}/resources/images/logo.png" alt="트레이니 로고">
			</div>
			<div class="nav-links">
				<a href="#" class="active">게시판</a> 
				<a href="#">시간표</a> 
				<a href="#">학점계산기</a>
				<a href="#">친구</a> 
				<a href="#">공지</a> 
				<a href="#">마이페이지</a> 
				<a href="#">맛집</a>
			</div>
		</nav>
	</header>

	<div class="post-container">
		<!-- query는 내가 이전에 어떤 화면이었는지 확인하기 위함. 검색을 했는지 안 했는지. -->
		<a href="${pageContext.request.contextPath}/market/list?${query}" class="back-link">← 목록으로</a>

		<div class="post-detail">
			<div class="post-header">
				<h1 class="post-title">${dto.title}</h1>
				<div class="post-meta">
					<div class="post-info">
						<span>작성자: ${dto.nickName}</span> <span>작성일: ${dto.ca_date}</span>
					</div>
					<div class="post-info">
						<span>조회 ${dto.views}</span>
						<!-- <span>댓글 5</span> -->
						<!-- 댓글 기능 구현 시 추가 -->
					</div>
				</div>
			</div>
			<div class="post-content">
				<p>${dto.content}</p>
				<c:if test="${not empty dto.fileName}">
					<div class="img-box">
      					  <img src="${pageContext.request.contextPath}/uploads/${dto.fileName}" class="img-fluid">
    				</div>
				</c:if>
			</div>
			<div class="post-actions">
				<div class="action-left">
					<button class="btn btn-gray">좋아요 0</button>
					<button class="btn btn-gray">공유하기</button>
				</div>
				<div class="action-right">
						<c:if test="${sessionScope.member.mb_Num==dto.mb_num}">
						
						<button class="btn btn-purple"
							onclick="location.href='${pageContext.request.contextPath}/market/update?marketNum=${dto.marketNum}&page=${page}'">수정</button>
						<button class="btn btn-red" onclick="deletePost()">삭제</button>
						
						</c:if>
					<button class="btn btn-red">신고</button>
				</div>
			</div>
		</div>

		<!-- 이전글/다음글 -->
		<div class="post-navigation"
			style="margin-top: 20px; background: white; border-radius: 8px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); padding: 15px;">
			<c:if test="${not empty prevDto}">
				<div style="padding: 8px 0;">
					<span style="color: #666;">이전글:</span> 
					<a href="${pageContext.request.contextPath}/market/article?${query}&marketNum=${prevDto.marketNum}" 
					style="text-decoration: none; color: #333;">${prevDto.title}</a>
				</div>
			</c:if>
			<c:if test="${not empty nextDto}">
				<div style="padding: 8px 0;">
					<span style="color: #666;">다음글:</span> <a
						href="${pageContext.request.contextPath}/market/article?${query}&marketNum=${nextDto.marketNum}"
						style="text-decoration: none; color: #333;">${nextDto.title}</a>
				</div>
			</c:if>
		</div>

		<!-- 댓글 섹션은 나중에 구현 -->
	</div>

	<footer>
		<div class="footer-content">
			<p>씨유트레이니 주식회사</p>
			<p>서울특별시 광도달북로21 동성빌딩 2층 | 사업자등록번호 : 123456789</p>
			<div class="footer-links">
				<a href="#">이용약관</a> <a href="#">개인정보처리방침</a> <a href="#">청소년보호정책</a>
				<a href="#">커뮤니티이용규칙</a> <a href="#">공지사항</a> <a href="#">문의하기</a> 
				<a href="#">@TRAINNF</a>
			</div>
		</div>
	</footer>

	<script>
		function deletePost() {
			if (confirm('게시글을 삭제하시겠습니까?')) {
				location.href = '${pageContext.request.contextPath}/market/delete?marketNum=${dto.marketNum}&page=${page}';
			}
		}
	</script>
</body>
</html>