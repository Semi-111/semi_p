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

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="list-main">
    <div class="container">
        <!-- 왼쪽 메뉴 -->
        <div class="sidebar">
            <div class="search-bar">
                <input type="text" placeholder="과목명, 교수명으로 검색">
            </div>
            <div class="lecture-info">
                <div class="lecture-name">내 강의</div>
                <div class="lecture-detail">
                    <span class="badge">X2</span>
                    진로와상담<br>
                    <span class="professor">장종익</span>
                </div>
            </div>
            <div class="points">
                <div class="current-points">내 포인트 현황</div>
                <div class="points-value">6 포인트</div>
            </div>
            <div class="review-prompt">
                지난 학기에 수강하신<br>
                <strong>진로와상담</strong> 강의는 어땠나요?<br>
                다른 학우들을 위해 강의평을 남겨주세요!
                <button class="register-btn">강의평 등록하고 10 포인트 적립하기</button>
            </div>
        </div>

        <!-- 오른쪽 강의 평가 리스트 -->
        <div class="main-content">
            <div class="review">
                <div class="lecture-title">재미있는정신분석이야기</div>
                <div class="professor">종연영</div>
                <div class="star-rate">
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star-half-alt"></i>
                    <i class="fa fa-star"></i>
                </div>
                <div class="review-text">진짜 개인적으로 1점도 아까웠던 수업이었음...</div>
            </div>

            <div class="review">
                <div class="lecture-title">심리통계</div>
                <div class="professor">황창주</div>
                <div class="star-rate">
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star"></i>
                    <i class="fa fa-star"></i>
                </div>
                <div class="review-text">교수 수업태도 너무 불량하고...</div>
            </div>
            
            <div class="review">
                <div class="lecture-title">고급영어회화실습</div>
                <div class="professor">LUCIER PETER</div>
                <div class="star-rate">
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star active"></i>
                    <i class="fa fa-star"></i>
                    <i class="fa fa-star"></i>
                </div>
                <div class="review-text">나르시즘이 있는... 영어로 말도 많이 할 수 있어서 좋음.</div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />

</body>
</html>