<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/main.css">

<style type="text/css">

</style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trainee Admin</title>
</head>
<body>
    <aside class="admin-sidebar">
        <div class="admin-logo">
            <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="트레이니 어드민">
        </div>
        <ul class="admin-menu">
            <li><a href="#" class="active">대시보드</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/home/membership">회원 관리</a></li>
            <li><a href="#">게시판 관리</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/home/report">신고 관리</a></li>
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
                <div class="card-value">1,230</div>
                <div class="card-trend">+2.4% from last week</div>
            </div>
            <div class="dashboard-card">
                <div class="card-title">오늘의 신규가입</div>
                <div class="card-value">89</div>
                <div class="card-trend">+12.3% from yesterday</div>
            </div>
            <div class="dashboard-card">
                <div class="card-title">게시글 수</div>
                <div class="card-value">4,200</div>
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
        <button class="action-button" onclick="location.href='${pageContext.request.contextPath}/admin/home/report'">전체보기</button>
    </div>
		    <table>
		        <thead>
		            <tr>
		                <th>신고 번호</th>
		                <th>신고 제목</th>
		                <th>신고 내용</th>
		                <th>신고 사유</th>
		                <th>게시판 분류</th>
		                <th>신고자</th>
		                <th>작업</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="dto" items="${recentReports}">
		                <tr>
		                    <td>${dto.RP_num}</td>
		                    <td>${dto.RP_title}</td>
		                    <td>${dto.RP_content}</td>
		                    <td>${dto.RP_reason}</td>
		                    <td>${dto.RP_table}</td>
		                    <td>${dto.memberName}</td>
		                    <td>
		                        <button class="action-button" onclick="viewReport(${dto.RP_num})">상세보기</button>
		                    </td>
		                </tr>
		            </c:forEach>
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

<script type="text/javascript">
let currentReportId;

function viewReport(rpNum) {
    currentReportId = rpNum;
    
    fetch('${pageContext.request.contextPath}/admin/report/detail?rpNum=' + rpNum)
        .then(response => response.json())
        .then(data => {
            // 기존 필드 설정
            document.getElementById('modalTitle').textContent = data.RP_title;
            document.getElementById('modalReporter').textContent = data.memberName;
            document.getElementById('modalReason').textContent = data.RP_reason;
            document.getElementById('modalTable').textContent = data.RP_table;
            document.getElementById('modalContent').textContent = data.RP_content;
            
            // 새로운 필드 설정
            document.getElementById('postTitle').textContent = data.postTitle;
            document.getElementById('postWriter').textContent = data.postWriter;
            document.getElementById('postContent').textContent = data.postContent;
            
            // 부트스트랩 모달 표시
            new bootstrap.Modal(document.getElementById('reportModal')).show();
        });
}

</script>

</html>
