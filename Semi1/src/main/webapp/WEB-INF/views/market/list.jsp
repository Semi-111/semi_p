<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trainee</title>
<style>

/* 페이징 처리 */
.page-navigation {
	margin-top: 20px;
	display: flex;
	justify-content: center;
	gap: 5px;
}

.page-link {
	text-decoration: none;
	color: #666;
	padding: 8px 12px;
	border: 1px solid #ddd;
	border-radius: 4px;
	transition: all 0.2s;
}

.page-link:hover {
	background-color: #f0f0f0;
	color: #a855f7;
}

.page-link.active {
	background-color: #a855f7;
	color: white;
	border-color: #a855f7;
}

.page-box {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	padding: 15px;
	margin-top: 20px;
}

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

/* 컨테이너 */
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
	height: 100px;
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

/* 게시판 헤더 */
.board-title {
	font-size: 24px;
	margin: 20px 0;
	color: #333;
}

.board-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.board-nav {
	display: flex;
	gap: 1rem;
	border-bottom: 1px solid #e1e1e1;
	padding: 10px 0;
}

.board-nav a {
	text-decoration: none;
	color: #666;
	padding: 5px 10px;
}

.board-nav a.active {
	color: #a855f7;
	font-weight: bold;
}

.board-nav a:hover {
	color: #a855f7;
}

.write-button {
	background-color: #a855f7;
	color: white;
	padding: 8px 20px;
	border-radius: 6px;
	text-decoration: none;
	font-size: 14px;
	transition: background-color 0.2s;
}

.write-button:hover {
	background-color: #9333ea;
}

/* 검색 박스 */
.search-box {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
}

.search-box .form-select, .search-box .form-control {
	padding: 8px;
	border: 1px solid #ddd;
	border-radius: 4px;
}

.search-box .form-select {
	width: 150px;
}

.search-box .form-control {
	flex: 1;
}

.search-box .btn {
	background-color: #a855f7;
	color: white;
	padding: 8px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.search-box select:focus, .search-box input:focus {
	outline: none;
	border-color: #a855f7;
}

.search-box button:hover {
	background-color: #9333ea;
}

/* 메인 콘텐츠 레이아웃 */
.main-content {
	display: grid;
	grid-template-columns: 1fr 300px;
	gap: 20px;
}

/* 게시글 목록 */
.post-list {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.post-item {
	padding: 20px;
	border-bottom: 1px solid #eee;
	transition: background-color 0.2s;
}

.post-item:hover {
	background-color: #f8f9fa;
}

.post-item:last-child {
	border-bottom: none;
}

.post-title {
	font-size: 16px;
	margin-bottom: 8px;
}

.post-title a {
	text-decoration: none;
	color: #333;
}

.post-title a:hover {
	color: #a855f7;
}

.post-info {
	font-size: 12px;
	color: #888;
}

.post-info span {
	margin-right: 10px;
}

/* 사이드바 */
.sidebar {
	background: white;
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.sidebar-title {
	color: #a855f7;
	font-size: 16px;
	font-weight: bold;
	margin-bottom: 15px;
}

.hot-posts {
	margin-bottom: 30px;
}

.hot-post-item {
	padding: 10px 0;
	border-bottom: 1px solid #eee;
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 13px;
}

.hot-post-item:last-child {
	border-bottom: none;
}

.post-date {
	color: #888;
	font-size: 12px;
}

/* 푸터 */
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

/* 반응형 디자인 */
@media ( max-width : 768px) {
	.main-content {
		grid-template-columns: 1fr;
	}
	.search-box div {
		flex-direction: column;
	}
	.search-box .form-select {
		width: 100%;
	}
}
</style>

<script type="text/javascript">
	function searchList() {
		const f = document.searchForm;
		f.submit();
	}
</script>
</head>
<body>
	<header>
		<nav>
			<div class="logo">
				<img
					src="${pageContext.request.contextPath}/resources/images/logo.png"
					alt="트레이니 로고">
			</div>
			<div class="nav-links">
				<a href="#">게시판</a> <a href="#">시간표</a> <a href="#">학점계산기</a> <a
					href="#">친구</a> <a href="#">공지</a> <a href="#">마이페이지</a> <a
					href="#">맛집</a>
			</div>
		</nav>
	</header>

	<div class="container">
		<h1 class="board-title">장터게시판</h1>

		<!-- 상단 네비게이션과 글쓰기 버튼 -->
		<div class="board-header">
			<div class="board-nav">
				<a href="${pageContext.request.contextPath}/market/list"
					class="active">전체</a> <a
					href="${pageContext.request.contextPath}/market/list?category=1">삽니다</a>
				<a href="${pageContext.request.contextPath}/market/list?category=2">팝니다</a>
				<a href="${pageContext.request.contextPath}/market/list?category=3">룸</a>
				<a href="${pageContext.request.contextPath}/market/list?category=4">완료</a>
			</div>
			<a href="${pageContext.request.contextPath}/market/write" class="write-button">글쓰기</a>
		</div>

		<!-- 검색 -->
		<div class="search-box">
			<form name="searchForm"
				action="${pageContext.request.contextPath}/market/list" method="get">
				<div style="display: flex; gap: 10px; justify-content: center;">
					<select name="schType" class="form-select">
						<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
						<option value="title" ${schType=="title"?"selected":""}>제목</option>
						<option value="content" ${schType=="content"?"selected":""}>내용</option>
					</select> <input type="text" name="kwd" value="${kwd}" class="form-control"
						placeholder="검색어를 입력하세요">
					<button type="button" class="btn" onclick="searchList();">검색</button>
				</div>
			</form>
		</div>

		<!-- 메인 콘텐츠 -->
		<div class="main-content">
			<!-- 게시글 목록 -->
			<div class="post-list">
				<c:forEach var="dto" items="${list}">
					<div class="post-item">
						<h3 class="post-title">
						 	<a href="${pageContext.request.contextPath}/market/article?marketNum=${dto.marketNum}&page=${page}">${dto.title}</a>
						</h3>
						<div class="post-info">
							<span>작성자: ${dto.nickName}</span> <span>작성일:
								${dto.ca_date}</span>
						</div>
					</div>
				</c:forEach>

				<!-- 페이징 처리 -->
				<div class="page-box">
					<div class="page-navigation">
						<!-- 처음 페이지 -->
						<c:if test="${current_page > 1}">
							<a href="${listUrl}?page=1" class="page-link" title="처음">⌈</a>
						</c:if>

						<!-- 이전 페이지 -->
						<c:if test="${current_page > 1}">
							<a href="${listUrl}?page=${current_page-1}" class="page-link"
								title="이전">〈</a>
						</c:if>

						<!-- 페이지 -->
						<c:forEach var="page" begin="1" end="${total_page}">
							<c:if test="${current_page == page}">
								<span class="page-link active">${page}</span>
							</c:if>
							<c:if test="${current_page != page}">
								<a href="${listUrl}?page=${page}" class="page-link">${page}</a>
							</c:if>
						</c:forEach>

						<!-- 다음 페이지 -->
						<c:if test="${current_page < total_page}">
							<a href="${listUrl}?page=${current_page+1}" class="page-link" title="다음">〉</a>
						</c:if>

						<!-- 마지막 페이지 -->
						<c:if test="${current_page < total_page}">
							<a href="${listUrl}?page=${total_page}" class="page-link" title="마지막">⌋</a>
						</c:if>
					</div>

					<!-- 데이터 수와 현재 페이지 -->
					<div style="text-align: center; margin-top: 10px;">
						<span style="color: #666;">${dataCount}개(${current_page}/${total_page}페이지)</span>
					</div>
				</div>
			</div>

			<!-- 사이드바 -->
			<div class="sidebar">
				<div class="hot-posts">
					<h3 class="sidebar-title">HOT 게시물</h3>
					<div class="hot-post-item">
						<span>운동할 친구를 구하는 원대 솔로♂</span> <span class="post-date">11/29
							14:23</span>
					</div>
					<div class="hot-post-item">
						<span>중고 핸드폰 공동 구매</span> <span class="post-date">11/28
							15:58</span>
					</div>
				</div>

				<div class="hot-posts">
					<h3 class="sidebar-title">BEST 게시물</h3>
					<div class="hot-post-item">
						<span>기숙사 양도합니다</span> <span class="post-date">11/27 12:30</span>
					</div>
					<div class="hot-post-item">
						<span>전공서적 무료나눔</span> <span class="post-date">11/26 18:45</span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<footer>
		<div class="footer-content">
			<p>씨유트레이니 주식회사</p>
			<p>서울특별시 광도달북로21 동성빌딩 2층 | 사업자등록번호 : 123456789</p>
			<div class="footer-links">
				<a href="#">이용약관</a> <a href="#">개인정보처리방침</a> <a href="#">청소년보호정책</a>
				<a href="#">커뮤니티이용규칙</a> <a href="#">공지사항</a> <a href="#">문의하기</a> <a
					href="#">@TRAINNF</a>
			</div>
		</div>
	</footer>
</body>
</html>