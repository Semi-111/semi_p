<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/market/list.css">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trainee</title>

<script type="text/javascript">
	function searchList() {
		const f = document.searchForm;
		f.submit();
	}
</script>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
</head>
<body>
	<div class="container">
		<h1 class="board-title">장터게시판</h1>

		<!-- 상단 네비게이션과 글쓰기 버튼 -->
		<div class="board-header">
			<div class="board-nav">
				<a href="${pageContext.request.contextPath}/market/list" class="${empty param.category ? 'active' : ''}">전체</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=1" class="${param.category=='1' ? 'active' : ''}">삽니다</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=2" class="${param.category=='2' ? 'active' : ''}">팝니다</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=3" class="${param.category=='3' ? 'active' : ''}">룸</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=4" class="${param.category=='4' ? 'active' : ''}">책</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=5" class="${param.category=='5' ? 'active' : ''}">옷</a> 
				<a href="${pageContext.request.contextPath}/market/list?category=6" class="${param.category=='6' ? 'active' : ''}">기타</a>
			</div>
			<a href="${pageContext.request.contextPath}/market/write" class="write-button">글쓰기</a>
		</div>

		<!-- 검색 -->
		<div class="search-box">
			<form name="searchForm" action="${pageContext.request.contextPath}/market/list" method="get">
				<div style="display: flex; gap: 10px; justify-content: center;">
					<select name="schType" class="form-select">
						<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
						<option value="title" ${schType=="title"?"selected":""}>제목</option>
						<option value="content" ${schType=="content"?"selected":""}>내용</option>
					</select> 
					<input type="text" name="kwd" value="${kwd}" class="form-control" placeholder="검색어를 입력하세요">
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
							<span>작성자: ${dto.nickName}</span> 
							<span>작성일: ${dto.ca_date}</span>
						</div>
					</div>
				</c:forEach>

				<!-- 페이징 처리 -->
				<div class="page-box">
					<div class="page-navigation">
						<!-- 처음 페이지 -->
						<c:if test="${current_page > 1}">
							<a href="${paginationUrl}${empty query ? '?' : '&'}page=1"
								class="page-link" title="처음">⌈</a>
						</c:if>

						<!-- 이전 페이지 -->
						<c:if test="${current_page > 1}">
							<a href="${paginationUrl}${empty query ? '?' : '&'}page=${current_page-1}" class="page-link" title="이전">〈</a>
						</c:if>

						<!-- 페이지 -->
						<c:forEach var="page" begin="1" end="${total_page}">
							<c:if test="${current_page == page}">
								<span class="page-link active">${page}</span>
							</c:if>
							<c:if test="${current_page != page}">
								<a href="${paginationUrl}${empty query ? '?' : '&'}page=${page}" class="page-link">${page}</a>
							</c:if>
						</c:forEach>

						<!-- 다음 페이지 -->
						<c:if test="${current_page < total_page}">
							<a href="${paginationUrl}${empty query ? '?' : '&'}page=${current_page+1}" class="page-link" title="다음">〉</a>
						</c:if>

						<!-- 마지막 페이지 -->
						<c:if test="${current_page < total_page}">
							<a href="${paginationUrl}${empty query ? '?' : '&'}page=${total_page}" class="page-link" title="마지막">⌋</a>
						</c:if>
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
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>