<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trainee Admin</title>
    <style>
        /* 기존 스타일 유지 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", sans-serif;
        }

        body {
            background-color: #f5f5f5;
            display: flex;
        }

        /* 사이드바 스타일 */
        .admin-sidebar {
            width: 250px;
            background: #1a1a1a;
            color: white;
            height: 100vh;
            position: fixed;
            padding: 20px;
        }

        .admin-logo {
            padding: 20px 0;
            border-bottom: 1px solid #333;
            margin-bottom: 20px;
        }

        .admin-logo img {
            height: 40px;
            width: auto;
        }

        .admin-menu {
            list-style: none;
        }

        .admin-menu li {
            margin-bottom: 10px;
        }

        .admin-menu a {
            color: #888;
            text-decoration: none;
            display: flex;
            align-items: center;
            padding: 10px;
            border-radius: 4px;
            transition: all 0.3s;
        }

        .admin-menu a:hover {
            background: #333;
            color: #a855f7;
        }

        .admin-menu a.active {
            background: #a855f7;
            color: white;
        }

        /* 메인 콘텐츠 영역 */
        .admin-main {
            margin-left: 250px;
            padding: 20px;
            width: calc(100% - 250px);
        }

        /* 대시보드 카드 */
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .dashboard-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .card-title {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .card-value {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }

        .card-trend {
            font-size: 12px;
            color: #22c55e;
        }

        .card-trend.down {
            color: #ef4444;
        }

        /* 데이터 테이블 */
        .data-table {
            background: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .table-header {
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .table-title {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px 20px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            font-weight: 500;
            color: #666;
            font-size: 14px;
        }

        td {
            color: #333;
            font-size: 14px;
        }

        /* 상태 배지 */
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
        }

        .status-active {
            background: #dcfce7;
            color: #22c55e;
        }

        .status-pending {
            background: #fff7ed;
            color: #f97316;
        }

        .status-inactive {
            background: #fef2f2;
            color: #ef4444;
        }

        /* 액션 버튼 */
        .action-button {
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
            border: none;
            background: #a855f7;
            color: white;
        }

        .action-button:hover {
            background: #9333ea;
        }
    </style>
</head>
<body>
    <aside class="admin-sidebar">
        <div class="admin-logo">
            <img src="${pageContext.request.contextPath}/test/trainee/trainee.png" alt="트레이니 어드민">
        </div>
        <ul class="admin-menu">
            <li><a href="#" class="active">대시보드</a></li>
            <li><a href="#">회원 관리</a></li>
            <li><a href="#">게시판 관리</a></li>
            <li><a href="#">신고 관리</a></li>
            <li><a href="#">공지사항</a></li>
            <li><a href="#">통계</a></li>
            <li><a href="#">설정</a></li>
        </ul>
    </aside>

    <main class="admin-main">
        <h1 class="board-title">관리자 대시보드</h1>
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <div class="card-title">전체 회원수</div>
                <div class="card-value">12,345</div>
                <div class="card-trend">+2.4% from last week</div>
            </div>
            <div class="dashboard-card">
                <div class="card-title">오늘의 신규가입</div>
                <div class="card-value">89</div>
                <div class="card-trend">+12.3% from yesterday</div>
            </div>
            <div class="dashboard-card">
                <div class="card-title">게시글 수</div>
                <div class="card-value">3,456</div>
                <div class="card-trend">+5.7% from last week</div>
            </div>
            <div class="dashboard-card">
                <div class="card-title">신고 건수</div>
                <div class="card-value">23</div>
                <div class="card-trend down">+15.2% from last week</div>
            </div>
        </div>

        <div class="data-table">
            <div class="table-header">
                <h2 class="table-title">최근 신고 내역</h2>
                <button class="action-button">전체보기</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>신고 ID</th>
                        <th>신고 유형</th>
                        <th>신고된 게시글</th>
                        <th>신고자</th>
                        <th>신고 일시</th>
                        <th>상태</th>
                        <th>작업</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>#12345</td>
                        <td>스팸</td>
                        <td>불법 광고 게시글입니다.</td>
                        <td>user123</td>
                        <td>2024-12-06 11:30</td>
                        <td><span class="status-badge status-pending">처리중</span></td>
                        <td><button class="action-button">처리하기</button></td>
                    </tr>
                    <tr>
                        <td>#12344</td>
                        <td>욕설</td>
                        <td>부적절한 언어 사용</td>
                        <td>user456</td>
                        <td>2024-12-06 11:15</td>
                        <td><span class="status-badge status-active">완료</span></td>
                        <td><button class="action-button">상세보기</button></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="data-table">
            <div class="table-header">
                <h2 class="table-title">최근 가입 회원</h2>
                <button class="action-button">전체보기</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>회원 ID</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>가입일</th>
                        <th>상태</th>
                        <th>작업</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>#45678</td>
                        <td>김트레</td>
                        <td>kim@trainee.com</td>
                        <td>2024-12-06</td>
                        <td><span class="status-badge status-active">활성</span></td>
                        <td><button class="action-button">관리</button></td>
                    </tr>
                    <tr>
                        <td>#45677</td>
                        <td>이니니</td>
                        <td>lee@trainee.com</td>
                        <td>2024-12-06</td>
                        <td><span class="status-badge status-pending">대기</span></td>
                        <td><button class="action-button">관리</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>
