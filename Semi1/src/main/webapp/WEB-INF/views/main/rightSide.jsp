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
            <div class="review">
            	<span class="star">
            		⭐⭐⭐⭐⭐
            	</span>
            	<p class="title"> 
               		비드멘토 : 채수원
                </p>               
                <p>
                	솔직히 C+ 나올것 같긴 하지만 아직 수업이 진짜 포기하라고 F 받을까 생각도 했지만
                </p>
            </div>
            <div class="review">
            	<span class="star">
            		⭐⭐⭐⭐⭐
            	</span>
            	<p class="title"> 
               		비드멘토 : 채수원
                </p>               
                <p>
                	정말 좋아요! 준비 열심히 해주시고 학생들 서브에 챙겨주시고 친근하게 다가와 주셔서 한 학기 동안 재밌게 들었어요
                </p>               
            </div>
        </div>
    </div>
	
</div>
