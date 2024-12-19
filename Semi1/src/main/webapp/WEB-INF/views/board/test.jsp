<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
        }

        .main-container {
            display: flex;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            gap: 10px;
        }

        .sidebar-left {
            width: 20%;
            background-color: #ffffff;
            padding: 15px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .user-info {
            text-align: center;
            margin-bottom: 20px;
        }

         .user-avatar-icon {
             font-size: 80px; /* 아이콘 크기 */
             color: #ccc; /* 아이콘 색상 */
             margin-bottom: 10px;
         }

        .user-avatar {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                margin-bottom: 10px;
            }

        .user-name {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .user-details {
            font-size: 14px;
            color: #666;
            margin-bottom: 15px;
        }

        .user-buttons button {
            width: 100%;
            padding: 10px;
            margin: 5px 0;
            background-color: #a855f7;
            color: #fff;
            border: none;
            cursor: pointer;
            border-radius: 8px;
            transition: background-color 0.3s ease, transform 0.3s ease; /* 애니메이션 추가 */
        }

        .user-buttons button:hover {
            background-color: #9333ea;
            transform: translateY(-2px);
        }

        .content {
            width: 60%;
            background-color: #ffffff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .board-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }

        .board-section {
            width: 48%;
            margin-bottom: 15px;
            background-color: #f5f5ff;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }

        .board-section h2 {
            font-size: 18px;
            margin-bottom: 10px;
            color: #333;
        }

        .board-section ul {
            list-style: none;
            padding: 0;
        }

        .board-section ul li {
            margin-bottom: 10px;
        }

        .sidebar-right {
            width: 20%;
            background-color: #ffffff;
            padding: 15px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .sidebar-right h3 {
            font-size: 18px;
            color: #6633cc;
            margin-bottom: 15px;
        }

        .hot-post-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }

        .hot-post-item span {
            display: inline-block;
        }

        .hot-post-item .post-date {
            color: #777;
        }

        @media (max-width: 1200px) {
            .main-container {
                flex-direction: column;
            }

            .sidebar-left, .sidebar-right {
                width: 100%;
                margin: 0 0 15px 0;
            }

            .content {
                width: 100%;
            }
        }

        @media (max-width: 768px) {
            .board-section h2 {
                font-size: 16px;
            }
        }

        @media (max-width: 480px) {
            body {
                font-size: 14px;
            }

            .sidebar-left, .sidebar-right {
                padding: 10px;
            }

            .content {
                padding: 10px;
            }

            .board-section {
                padding: 10px;
            }
        }
    </style>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="main-container">
    <!-- 왼쪽 사이드바 -->
    <div class="sidebar-left">
        <div class="user-info">
            <c:choose>
                <c:when test="${not empty sessionScope.member.nickName}">
                    <!-- 유저 이미지가 있는 경우 -->
                    <img src="${pageContext.request.contextPath}/uploads/${sessionScope.member.profileImage}" alt="유저 아바타" class="user-avatar"/>
                </c:when>
                <c:otherwise>
                    <!-- 기본 유저 아이콘 -->
                    <i class="bi bi-person-circle user-avatar-icon"></i>
                </c:otherwise>
            </c:choose>
            <div class="user-name">화석현승</div>
            <div class="user-details">권현승<br>trainee</div>
        </div>
        <div class="user-buttons">
            <button onclick="location.href='${pageContext.request.contextPath}/member/info'">내 정보</button>
            <button onclick="location.href='${pageContext.request.contextPath}/member/logout'">로그아웃</button>
        </div>
        <div>날씨 (오늘의 날씨)</div>
        <div>광고 사진</div>
        <div>광고 사진</div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="content">
        <h1>설문조사 (이벤트식)</h1>
        <div class="board-container">
            <div class="board-section">
                <h2>자유 게시판</h2>
                <ul>
                    <li>게시글 1</li>
                    <li>게시글 2</li>
                    <li>게시글 3</li>
                </ul>
            </div>
            <div class="board-section">
                <h2>학과별 게시판</h2>
                <ul>
                    <li>학과 게시글 1</li>
                    <li>학과 게시글 2</li>
                    <li>학과 게시글 3</li>
                </ul>
            </div>
            <div class="board-section">
                <h2>비밀 게시판</h2>
                <p>로그인한 학생만 이용 가능합니다.</p>
            </div>
            <div class="board-section">
                <h2>졸업생 게시판</h2>
                <ul>
                    <li>졸업생 게시글 1</li>
                    <li>졸업생 게시글 2</li>
                </ul>
            </div>
            <div class="board-section">
                <h2>정보 게시판</h2>
                <ul>
                    <li>정보글 1</li>
                    <li>정보글 2</li>
                    <li>정보글 3</li>
                </ul>
            </div>
            <div class="board-section">
                <h2>새내기 게시판</h2>
                <ul>
                    <li>새내기 글 1</li>
                    <li>새내기 글 2</li>
                </ul>
            </div>
            <div class="board-section">
                <h2>이벤트 게시판</h2>
                <ul>
                    <li>이벤트 1</li>
                    <li>이벤트 2</li>
                </ul>
            </div>
        </div>
    </div>

    <!-- 오른쪽 사이드바 -->
    <div class="sidebar-right">
        <h3>HOT 게시물</h3>
        <div class="hot-post-item">
            <span>자유게시판 인기글</span>
            <span class="post-date">11/29</span>
        </div>
        <div class="hot-post-item">
            <span>학과 MT 후기</span>
            <span class="post-date">11/28</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>

        <h3>주간 인기글</h3>
        <div class="hot-post-item">
            <span>2024 학과 행사</span>
            <span class="post-date">11/27</span>
        </div>
        <div class="hot-post-item">
            <span>스터디 모집</span>
            <span class="post-date">11/25</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>

        <h3>최근 강의평</h3>
        <div class="hot-post-item">
            <span>알고리즘 수업 리뷰</span>
            <span class="post-date">11/26</span>
        </div>
        <div class="hot-post-item">
            <span>데이터베이스 수업 후기</span>
            <span class="post-date">11/24</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>
    </div>
</div>

<footer>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</footer>

</body>
</html>