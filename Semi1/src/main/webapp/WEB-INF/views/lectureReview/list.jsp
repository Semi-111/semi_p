<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lectureReview/list.css" type="text/css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<script type="text/javascript">

$(function() {
	// .lecture-list의 개수 구하기
    var lectureCount = $('.lecture-detail .lecture-list').length;
    
    // 하나당 높이가 60이므로 총 높이 계산
    var totalHeight = lectureCount * 60 + 80;

    // .sidebar의 높이를 동적으로 설정
    $('.sidebar').css("height", totalHeight + "px");	
});



</script>

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="list-main">
    <div class="container">
        <!-- 왼쪽 메뉴 -->
        <div class="sidebar">
            <div class="lecture-info">
            	<!-- 검색 바 -->

                <div class="lecture-my">내 강의</div>
                           
                <div class="lecture-detail">
                	<c:forEach var="dto" items="${myLlist}">
                		<div class="lecture-list">
		                    <span class="lecture-title">		                    			                    		
		                    	<a href="${pageContext.request.contextPath}/lectureReview/write?sbNum=${dto.sb_Num}">
		                    		${dto.sb_Name} ${dto.isWritten==1 ? '(수정/확인)' : '(평가하기)'}
		                    	</a>
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
	                	<a href="#"></a>
	                	${dto.sb_Name}
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