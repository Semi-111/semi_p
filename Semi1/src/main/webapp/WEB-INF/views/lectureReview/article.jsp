<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lectureReview/article.css" type="text/css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="article-main">
	<div class="post-container">
		<!-- query는 내가 이전에 어떤 화면이었는지 확인하기 위함. 검색을 했는지 안 했는지. -->
		<a href="${pageContext.request.contextPath}/lectureReview/list"
			class="back-link">← 목록으로</a>

		<div class="post-detail">
			<div class="post-header">
				<h1 class="post-title">${dto.sb_Name}</h1>
				<div class="post-meta">
					<div class="post-info">
						<span>작성자: ${dto.nickName}</span>
						<hr>
						<span>작성일: </span>
					</div>
					<div class="post-info">
						<span> | 조회 </span>
					</div>
				</div>
			</div>
			<div class="post-content">
				<p>${dto.content}</p>
				<c:if test="${not empty dto.fileName}">
					<div class="img-box">
						<img
							src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}"
							class="img-fluid">
					</div>
				</c:if>
			</div>
			<div class="post-actions">
				<div class="action-left">
					<button type="button" class="btn btn-gray" onclick="toggleLike();" title="관심">
						<i class="bi ${isLiked ? 'bi-heart-fill' : 'bi-heart'}"
							id="heartIcon"></i> 관심 <span id="countLikes">${countLikes}</span>
					</button>
					<button class="btn btn-gray">공유하기</button>
				</div>
				<div class="action-right">
					<c:if
						test="${sessionScope.member.mb_Num==dto.mb_num || sessionScope.member.role >= 51}">
						<button class="btn btn-purple"
							onclick="location.href='${pageContext.request.contextPath}/market/update?marketNum=${dto.marketNum}&page=${page}'">
							수정</button>
						<button class="btn btn-red" onclick="deletePost()">삭제</button>
					</c:if>
					<button class="btn btn-red">신고</button>
				</div>
			</div>
		</div>
	</div>


</main>

<script type="text/javascript">

function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

</script>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />

</body>
</html>