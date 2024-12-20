<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의실</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lectureReview/list.css" type="text/css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<script type="text/javascript">

$(function() {
	// .lecture-list의 개수 구하기
    var lectureCount = $('.lecture-detail .lecture-list').length;
    
    // 하나당 높이가 60이므로 총 높이 계산
    var totalHeight = lectureCount * 60 + 200;

    // .sidebar의 높이를 동적으로 설정
    $('.sidebar').css("height", totalHeight + "px");	
});

function searchList() {
	const f = document.searchForm;
	f.submit();
}

</script>

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="list-main">

    <div class="container">
    	
		
			<!-- 왼쪽 메뉴 -->
	        <div class="sidebar">
	        	<div class="search-bar">
		        	<form name="searchForm" action="${pageContext.request.contextPath}/lectureReview/list" method="get">
			    		<input type="text" name="kwd" value="${kwd}" class="form-control" placeholder="과목명, 교수명으로 검색하세요">
			    		<button type="button" class="btn" onclick="searchList();" >검색</button>
			    	</form>
	        	</div>
	            <div class="lecture-info">
	            					
	                <div class="lecture-my">내 강의</div>
	                           
	                <div class="lecture-detail">
	                	<c:forEach var="dto" items="${myLlist}">
	                		<div class="lecture-list">
			                    <span class="lecture-title">
			                    	<c:choose>
			                    		<c:when test="${dto.isWritten == 1}">
			                    			<a href="${pageContext.request.contextPath}/lectureReview/update?reviewnum=${dto.review_Num}">
			                    				${dto.sb_Name} (수정/확인)
			                    			</a>		                  		
			                    		</c:when>
			                    			
			                    		<c:otherwise>
			                    			<a href="${pageContext.request.contextPath}/lectureReview/write?atNum=${dto.at_Num}">
			                    				${dto.sb_Name} (평가하기)
			                    			</a>
			                    		</c:otherwise>		                    	
			                    	</c:choose>
			                    		                    	               
			                    </span>
			                    <br>
			                    <span class="professor">${dto.pf_Name}</span>
		                    </div>
	                    </c:forEach>
	                </div>	                	                
	            </div>
	        </div>
		
		
		
        <!-- 오른쪽 강의 평가 리스트 -->
        <div class="main-content">
        	
            <c:forEach var="dto" items="${reviewList}" varStatus="status">
            	<div class="review-list">
	                <div class="lecture-title">
	                	<a href="${pageContext.request.contextPath}/lectureReview/article?review_num=${dto.review_Num}&page=${page}">
	                		${dto.sb_Name}
	                	</a>
	                </div>
	                <div class="professor">${dto.pf_Name}</div>
	                <div class="star-rate">
	                	<!-- 별 평가점수 -->
	                	<c:forEach begin="1" end="5" var="i">          	                		
	                    	<i class="fa fa-star star ${i <= dto.rating ? 'active' : ''}" data-value="${i}"></i>
	                    </c:forEach>              	
	                </div>
	                <div class="nickName">${dto.nickName}</div>
	                <div class="review-text">${dto.content}</div>
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