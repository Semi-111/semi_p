<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/rightSide.css">

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
                <li>📣크라임씬 다섯 번째 사건! 탐정들 ...<span>12/12 12:28</span></li>
                <li>해명글 올립니다<span>12/14 11:34</span></li>
                <li>새봄, 우리의 계절을 마무리하며🌱<span>12/15 13:00</span></li>
                <li>탄핵은 잘 된 일이고<span>12/14 17:06</span></li>
            </ul>
        </div>

        <!-- BEST 게시판 -->
        <div class="section">
            <div class="section-header">
                <span>BEST 게시판</span>
                <a href="#">더 보기</a>
            </div>
            <ul>
	            <li>📣크라임씬 다섯 번째 사건! 탐정들 ...<span>12/12 12:28</span></li>
	            <li>해명글 올립니다<span>12/14 11:34</span></li>
	            <li>새봄, 우리의 계절을 마무리하며🌱<span>12/15 13:00</span></li>
	            <li>탄핵은 잘 된 일이고<span>12/14 17:06</span></li>
            </ul>
        </div>

        <!-- 최근 강의평 -->
        <div class="section">
            <div class="section-header">
                <span>최근 강의평</span>
                <a href="#">더 보기</a>
            </div>
            <c:forEach var="dto" items="${listReview}">
                <div class="review">
	            	<span class="star">
                            ${dto.rating}
                    </span>
                    <p class="title">
                            ${dto.sb_Name} : ${dto.pf_Name}
                    </p>
                    <p>
                            ${dto.content}
                    </p>
                </div>
            </c:forEach>
        </div>
    </div>
	
</div>
