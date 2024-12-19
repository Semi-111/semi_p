<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>트레니 에브리타임</title>
    <%--
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/css/bootstrap.min.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/util-jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/main.js"></script>
     --%>
    <link rel="stylesheet"
          href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/main.js"></script>

    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>

<body>
<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<form name="mainForm" method="get">
    <div class="container-fluid">
        <div class="row">
            <!-- 왼쪽 사이드 -->
            <div class="col-2 d-none d-lg-block" id="leftSide">
                <div class="profile-container">
                    <c:choose>
                        <c:when test="${sessionScope.member != null}">
                            <div class="profile">
                                <h5>나의 정보</h5>
                                <div class="profile-info">
                                    <div class="MyPicture">
                                        <img
                                                src="${pageContext.request.contextPath}/resources/images/indexUI/profile.jpg">
                                        <!-- 사진 바뀔 수 있도록.. 연동이 문제임 -->
                                    </div>
                                    <h6>닉네임: ${sessionScope.member.name}</h6>
                                </div>
                                <div class="profile-buttons">
                                    <button type="button"
                                            onclick="location.href='${pageContext.request.contextPath}/member/myPage'">
                                        내정보
                                    </button>
                                    <button type="button"
                                            onclick="location.href='${pageContext.request.contextPath}/member/logout'">
                                        로그아웃
                                    </button>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="login-prompt">
                                <h6>
                                    커뮤티니 이용을 위해<br>로그인이 필요합니다!
                                </h6>
                                <div class="login-buttons">
                                    <button type="button"
                                            onclick="location.href='${pageContext.request.contextPath}/member/login'">
                                        로그인
                                    </button>
                                    <button type="button"
                                            onclick="location.href='${pageContext.request.contextPath}/member/member'">
                                        회원가입
                                    </button>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="weather">

                        </div>
                    </div>
                </div>
                <div class="row slider-container">
                    <div class="col">
                        <div class="slider">
                            <img
                                    src="${pageContext.request.contextPath}/resources/images/indexUI/ad1.jpg">
                            <img
                                    src="${pageContext.request.contextPath}/resources/images/indexUI/ad2.jpg">
                        </div>
                    </div>
                </div>
                <div class="row slider-container">
                    <div class="col">
                        <div class="slider">
                            <img
                                    src="${pageContext.request.contextPath}/resources/images/indexUI/ad3.jpg">
                            <img
                                    src="${pageContext.request.contextPath}/resources/images/indexUI/ad4.jpg">
                        </div>
                    </div>
                </div>
            </div>

            <!-- 중앙 화면 -->
            <div class="col-7 col-lg-7" id="mainContent">
                <div class="survey">
                    <a href="/survey-page-url" target="_blank"> <img
                            src="${pageContext.request.contextPath}/resources/images/indexUI/survey1.png"
                            alt="설문조사 이미지" class="survey-image">
                    </a>
                </div>

                <div class="row">
                    <div class="col" id="bbs-container">
                        <div class="container text-center">
                            <div class="row row-cols-2">

                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 자유게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=free">더보기</a>
                                            </div>
                                        </div>
                                        <div class="board-contant">
                                            <c:forEach var="dto" items="${freeBoard}">
                                                <div class="text-truncate px-2 subject-list">
                                                    <!-- text-truncate : 말줄임표 -->
                                                    <a
                                                            href="${pageContext.request.contextPath}/bbs/infoBoard/article?type=free&cmNum=${dto.cmNum}&page=1">
                                                            ${dto.title} </a>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 학과별게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/lessonBoard/list">더보기</a>
                                            </div>
                                        </div>
                                        <div class="board-contant">
                                            <c:forEach var="dto" items="${lessonBoard}">
                                                <div class="text-truncate px-2 subject-list">
                                                    <!-- text-truncate : 말줄임표 -->
                                                    <a
                                                            href="${pageContext.request.contextPath}/lessonBoard/article?cm_num=${dto.cm_num}&page=1">
                                                            ${dto.title} </a>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 비밀게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/bbs/secretBoard/list">더보기</a>
                                            </div>
                                        </div>
                                        <!-- 기은 비밀게시판 빨리 만드렁 -->
                                        <%-- <div class="board-contant">
<c:forEach var="dto" items="${secretBoard}">
<div class="text-truncate px-2 subject-list">
<!-- text-truncate : 말줄임표 -->
<a
href="${pageContext.request.contextPath}/bbs/secretBoard/article?type=secret&cmNum=${dto.cmNum}&page=1">
${dto.title} </a>
</div>
</c:forEach>
    </div> --%>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 공지게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/noticeBoard/list">더보기</a>
                                            </div>
                                        </div>
<%--                                        <div class="board-contant">--%>
<%--                                            <c:forEach var="dto" items="${listBoard1}">--%>
<%--                                                <div class="text-truncate px-2 subject-list">--%>
<%--                                                    <!-- text-truncate : 말줄임표 -->--%>
<%--                                                    <a--%>
<%--                                                            href="${pageContext.request.contextPath}/noticeBoard/article?CM_Num=${dto.CM_Num}&page=1">--%>
<%--                                                            ${dto.title} </a>--%>
<%--                                                </div>--%>
<%--                                            </c:forEach>--%>

<%--                                        </div>--%>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 새내기게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=student">더보기</a>
                                            </div>
                                        </div>
                                        <div class="board-contant">
                                            <c:forEach var="dto" items="${studentBoard}">
                                                <div class="text-truncate px-2 subject-list">
                                                    <!-- text-truncate : 말줄임표 -->
                                                    <a
                                                            href="${pageContext.request.contextPath}/bbs/studentBoard/article?type=student&cmNum=${dto.cmNum}&page=1">
                                                            ${dto.title} </a>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 정보게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=info">더보기</a>
                                            </div>
                                        </div>
                                        <div class="board-contant">
                                            <c:forEach var="dto" items="${infoBoard}">
                                                <div class="text-truncate px-2 subject-list">
                                                    <!-- text-truncate : 말줄임표 -->
                                                    <a
                                                            href="${pageContext.request.contextPath}/bbs/infoBoard/article?type=info&cmNum=${dto.cmNum}&page=1">
                                                            ${dto.title} </a>
                                                </div>
                                            </c:forEach>

                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="board-container">
                                        <div class="board-title subject-list" style="color: #A447AB;">
                                            <i class="bi bi-clipboard"></i> 졸업생게시판
                                            <div class="more-link">
                                                <a
                                                        href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=oldbie">더보기</a>
                                            </div>
                                        </div>
                                        <div class="board-contant">
                                            <c:forEach var="dto" items="${oldbieBoard}">
                                                <div class="text-truncate px-2 subject-list">
                                                    <!-- text-truncate : 말줄임표 -->
                                                    <a
                                                            href="${pageContext.request.contextPath}/bbs/studentBoard/article?type=oldbie&cmNum=${dto.cmNum}&page=1">
                                                            ${dto.title} </a>
                                                </div>
                                            </c:forEach>

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- 오른쪽 사이드 -->
            <jsp:include page="/WEB-INF/views/main/rightSide.jsp"/>

        </div>
    </div>
</form>

<script type="text/javascript">
    $(function () {
        const apiUrl = 'https://api.openweathermap.org/data/2.5/forecast?q=Seoul&appid=88ab27b7f970936f979a5be2c9dc8df1&units=metric';
        const weatherTransfer = {
            'Snow': '눈',
            'Rain': '비',
            'Clear': '맑음',
            'Clouds': '구름'
        };
        const now = new Date();
        const today = now.toISOString().split('T')[0];

        $.getJSON(apiUrl, function (data) {
            let found = false;

            $.each(data.list, function (i, item) {
                const date = item.dt_txt.split(' ')[0];

                if (date === today && !found) {
                    const temp = item.main.temp;
                    const humidity = item.main.humidity;
                    const speed = item.wind.speed;
                    const weather = weatherTransfer[item.weather[0].main] || '기타';
                    const icon = item.weather[0].icon;


                    $('<h4/>', {
                        text: date + ' (' + ['일', '월', '화', '수', '목', '금', '토'][new Date(date).getDay()] + ')',
                        css: {'font-size': '16px', 'padding-left': '13px'}
                    }).appendTo('.weather');

                    $('<ul/>', {
                        'class': 'weather-list',
                        html: '<li> 온도 : ' + temp
                            + '<br>습도 : ' + humidity + '<br>바람: ' + speed + '<br>날씨: ' + weather + ' <img src="http://openweathermap.org/img/w/' + icon + '.png"></li>',
                        css: {'font-size': '14px'}
                    }).appendTo('.weather');

                    found = true;
                }
            });
        });
    });
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>