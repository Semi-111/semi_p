<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/rightSide.css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">

<div class="col-3 d-none d-lg-block" id="rightSide">
	
    <div class="sidebar">
        <!-- 검색창 -->
		<div class="search-bar">
            <input type="text" placeholder="전체 게시판의 글을 검색하세요!">
        </div>
        <!-- HOT 게시물 -->
        <div class="section">
            <div class="section-header">
                <span>HOT 게시물</span>
                <a href="#">더 보기</a>
            </div>
            <ul>
                <li><a href="#">📣크라임씬 다섯 번째 사건!</a><span>12/12 12:28</span></li>
                <li><a href="#">해명글 올립니다</a><span>12/14 11:34</span></li>
                <li><a href="#">새봄, 우리의 계절을 마무리하며🌱</a><span>12/15 13:00</span></li>
                <li><a href="#">탄핵은 잘 된 일이고</a><span>12/14 17:06</span></li>
            </ul>
        </div>

        <!-- BEST 게시판 -->
        <div class="section">
            <div class="section-header">
                <span>BEST 게시판</span>
                <a href="#">더 보기</a>
            </div>
            <ul>
	            <li><a href="#">📣크라임씬 다섯 번째 사건!</a><span>12/12 12:28</span></li>
	            <li><a href="#">해명글 올립니다</a><span>12/14 11:34</span></li>
	            <li><a href="#">새봄, 우리의 계절을 마무리하며🌱</a><span>12/15 13:00</span></li>
	            <li><a href="#">탄핵은 잘 된 일이고</a><span>12/14 17:06</span></li>
            </ul>
        </div>

        <!-- 최근 강의평 -->
        <div class="section">
            <div class="section-header">
                <span>최근 강의평</span>
                <a href="${pageContext.request.contextPath}/lectureReview/list">더 보기</a>
            </div>
            <c:forEach var="dto" items="${listReview}">
                <div class="review">
	            	<div class="star-rate">
	            		<c:forEach begin="1" end="5" var="i">          	                		
	                    	<i class="fa fa-star star ${i <= dto.rating ? 'active' : ''}" data-value="${i}"></i>
	                    </c:forEach>                	
                    </div>
                    <p class="title">
                    	<a href="${pageContext.request.contextPath}/lectureReview/article?review_num=${dto.review_Num}&page=${page}">${dto.sb_Name} : ${dto.pf_Name}</a>        
                    </p>
                    <p class="content">
                            ${dto.content}
                    </p>
                </div>
            </c:forEach>
        </div>
    </div>
	
</div>
