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
		<a href="${pageContext.request.contextPath}/lectureReview/list?${query}"
			class="back-link">← 목록으로</a>

		<div class="post-detail">
			<div class="post-header">
				<h1 class="post-title">${dto.sb_Name}</h1>
				<div class="post-meta">
					<div class="post-info">
						<span>교수명: ${dto.pf_Name}</span>
					</div>
					<div class="post-info">
						<span>작성자: ${dto.nickName}</span>
					</div>					
				</div>
				<div class="star-rate">
	                	<!-- 별 평가점수 -->
	                	<c:forEach begin="1" end="5" var="i">          	                		
	                    	<i class="fa fa-star star ${i <= dto.rating ? 'active' : ''}" data-value="${i}"></i>
	                    </c:forEach>              	
	            </div>
			</div>
			<div class="post-content">
				${dto.content}
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