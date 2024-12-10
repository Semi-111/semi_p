<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trainee</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", sans-serif;
        }

        body {
            background-color: #f5f5f5;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* 헤더 스타일 */
        header {
            background: white;
            border-bottom: 1px solid #e1e1e1;
        }

        nav {
            max-width: 1200px;
            margin: 0 auto;
            padding: 8px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo img {
            height: 100px;
            width: auto;
            padding: 5px 0;
        }

        .nav-links {
            display: flex;
            gap: 2rem;
        }

        .nav-links a {
            text-decoration: none;
            color: #666;
            font-size: 14px;
        }

        .nav-links a:hover {
            color: #a855f7;
        }

        .nav-links a.active {
            color: #a855f7;
            font-weight: bold;
        }

        /* 게시판 헤더 */
        .board-title {
            font-size: 24px;
            margin: 20px 0;
        }

        .board-nav {
            display: flex;
            gap: 1rem;
            border-bottom: 1px solid #e1e1e1;
            padding: 10px 0;
            margin-bottom: 20px;
        }

        .board-nav a {
            text-decoration: none;
            color: #666;
            padding: 5px 10px;
        }

        .board-nav a.active {
            color: #a855f7;
            font-weight: bold;
        }

        .board-nav a:hover {
            color: #a855f7;
        }

        /* 메인 콘텐츠 레이아웃 */
        .main-content {
            display: grid;
            grid-template-columns: 1fr 300px;
            gap: 20px;
        }

        /* 게시글 목록 */
        .post-list {
            background: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .post-item {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .post-item:last-child {
            border-bottom: none;
        }

        .post-title {
            font-size: 16px;
            color: #333;
            margin-bottom: 8px;
        }

        .post-info {
            font-size: 12px;
            color: #888;
        }

        .post-info span {
            margin-right: 10px;
        }

        /* 사이드바 */
        .sidebar {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .sidebar-title {
            color: #a855f7;
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .hot-posts {
            margin-bottom: 30px;
        }

        .hot-post-item {
            padding: 10px 0;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
        }

        .hot-post-item:last-child {
            border-bottom: none;
        }

        .post-date {
            color: #888;
            font-size: 12px;
        }

        /* 푸터 */
        footer {
            background-color: #f9fafb;
            padding: 2rem 1rem;
            margin-top: 40px;
            border-top: 1px solid #eee;
        }

        .footer-content {
            max-width: 1200px;
            margin: 0 auto;
            font-size: 12px;
            color: #666;
        }

        .footer-links {
            margin-top: 1rem;
            display: flex;
            gap: 1rem;
        }

        .footer-links a {
            text-decoration: none;
            color: #666;
        }

        .footer-links a:hover {
            color: #a855f7;
        }
    </style>
</head>
<body>
    <header>
        <nav>
            <div class="logo">
                <img src="${pageContext.request.contextPath}/test/images/trainee.png" alt="트레이니 로고">
            </div>
            <div class="nav-links">
                <a href="#">게시판</a>
                <a href="#">시간표</a>
                <a href="#">학점계산기</a>
                <a href="#">친구</a>
                <a href="#">공지</a>
                <a href="#">마이페이지</a>
                <a href="#">맛집</a>
            </div>
        </nav>
    </header>

    <div class="container">
        <h1 class="board-title">장터게시판</h1>
        <div class="board-nav">
            <a href="#" class="active">전체</a>
            <a href="#">삽니다</a>
            <a href="#">팝니다</a>
            <a href="#">나눔</a>
            <a href="#">완료</a>
        </div>

        <div class="main-content">
            <div class="post-list">
                <div class="post-item">
                    <h3 class="post-title">동행자 구 홍대역 자취방</h3>
                    <div class="post-info">
                        <span>작성자: 익명</span>
                        <span>조회수: 15</span>
                        <span>11:29</span>
                    </div>
                </div>
                <div class="post-item">
                    <h3 class="post-title">중고 전자제품 팔아요</h3>
                    <div class="post-info">
                        <span>작성자: 익명</span>
                        <span>조회수: 23</span>
                        <span>10:45</span>
                    </div>
                </div>
                <div class="post-item">
                    <h3 class="post-title">자전거/중고 필름카메라</h3>
                    <div class="post-info">
                        <span>작성자: 익명</span>
                        <span>조회수: 8</span>
                        <span>10:15</span>
                    </div>
                </div>
            </div>

            <div class="sidebar">
                <div class="hot-posts">
                    <h3 class="sidebar-title">HOT 게시물</h3>
                    <div class="hot-post-item">
                        <span>운동할 친구를 구하는 원대 솔로♂</span>
                        <span class="post-date">11/29 14:23</span>
                    </div>
                    <div class="hot-post-item">
                        <span>중고 핸드폰 공동 구매</span>
                        <span class="post-date">11/28 15:58</span>
                    </div>
                </div>

                <div class="hot-posts">
                    <h3 class="sidebar-title">BEST 게시물</h3>
                    <div class="hot-post-item">
                        <span>기숙사 양도합니다</span>
                        <span class="post-date">11/27 12:30</span>
                    </div>
                    <div class="hot-post-item">
                        <span>전공서적 무료나눔</span>
                        <span class="post-date">11/26 18:45</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <div class="footer-content">
            <p>씨유트레이니 주식회사</p>
            <p>서울특별시 광도달북로21 동성빌딩 2층 | 사업자등록번호 : 123456789</p>
            <div class="footer-links">
                <a href="#">이용약관</a>
                <a href="#">개인정보처리방침</a>
                <a href="#">청소년보호정책</a>
                <a href="#">커뮤니티이용규칙</a>
                <a href="#">공지사항</a>
                <a href="#">문의하기</a>
                <a href="#">@TRAINNF</a>
            </div>
        </div>
    </footer>
</body>
</html>