<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/main.js"></script>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trainee Admin - 신고 관리</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕",
		sans-serif;
}

body {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

/* 테이블 스타일 */
.table-container {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	overflow: hidden;
}

.table-header {
	padding: 15px 20px;
	border-bottom: 1px solid #e5e7eb;
	background: #f9fafb;
}

.table-title {
	font-size: 16px;
	font-weight: 600;
	color: #374151;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 12px 20px;
	text-align: left;
	border-bottom: 1px solid #e5e7eb;
}

th {
	background: #f9fafb;
	font-weight: 500;
	color: #374151;
	font-size: 14px;
}

td {
	font-size: 14px;
	color: #4b5563;
}

tr:hover {
	background: #f9fafb;
}

/* 버튼 스타일 */
.btn {
	padding: 8px 16px;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	border: none;
	background: #a855f7;
	color: white;
	transition: background 0.2s;
}

.btn:hover {
	background: #9333ea;
}

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕",
		sans-serif;
}

body {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

/* 통계 카드 스타일 */
.report-summary {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
	gap: 20px;
	margin-bottom: 30px;
}

.summary-card {
	background: white;
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.summary-title {
	font-size: 14px;
	color: #666;
	margin-bottom: 10px;
}

.summary-value {
	font-size: 24px;
	font-weight: bold;
	color: #333;
}

.priority-high {
	color: #ef4444;
}

.priority-medium {
	color: #f97316;
}

.priority-low {
	color: #22c55e;
}

/* 탭 스타일 */
.tabs {
	display: flex;
	gap: 10px;
	margin-bottom: 20px;
	border-bottom: 1px solid #e5e7eb;
	padding-bottom: 10px;
}

.tab {
	padding: 8px 16px;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	background: none;
	border: none;
	color: #666;
	transition: all 0.2s;
}

.tab:hover {
	background: #f3f4f6;
}

.tab.active {
	background: #a855f7;
	color: white;
}

/* 테이블 스타일 */
.table-container {
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	overflow: hidden;
}

.table-header {
	padding: 15px 20px;
	border-bottom: 1px solid #e5e7eb;
	background: #f9fafb;
}

.table-title {
	font-size: 16px;
	font-weight: 600;
	color: #374151;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 12px 20px;
	text-align: left;
	border-bottom: 1px solid #e5e7eb;
}

th {
	background: #f9fafb;
	font-weight: 500;
	color: #374151;
	font-size: 14px;
}

td {
	font-size: 14px;
	color: #4b5563;
}

tr:hover {
	background: #f9fafb;
}

/* 상태 뱃지 */
.status-badge {
	padding: 4px 8px;
	border-radius: 12px;
	font-size: 12px;
	font-weight: 500;
	display: inline-block;
}

.status-pending {
	background: #fff7ed;
	color: #f97316;
}

.status-processing {
	background: #ecfdf5;
	color: #22c55e;
}

.status-completed {
	background: #f0f9ff;
	color: #0284c7;
}

/* 버튼 스타일 */
.btn {
	padding: 8px 16px;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	border: none;
	background: #a855f7;
	color: white;
	transition: background 0.2s;
}

.btn:hover {
	background: #9333ea;
}

/* 모달 스타일 */
.modal-backdrop {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	display: none;
}

.modal {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background: white;
	padding: 20px;
	border-radius: 8px;
	width: 90%;
	max-width: 600px;
	display: none;
}

.modal-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
	padding-bottom: 10px;
	border-bottom: 1px solid #e5e7eb;
}

.modal-title {
	font-size: 18px;
	font-weight: bold;
	color: #374151;
}

.modal-close {
	background: none;
	border: none;
	font-size: 24px;
	cursor: pointer;
	color: #9ca3af;
}

.report-content {
	background: #f9fafb;
	padding: 15px;
	border-radius: 4px;
	margin: 10px 0;
	font-size: 14px;
	color: #4b5563;
}

.report-actions {
	display: flex;
	gap: 10px;
	margin-top: 15px;
	justify-content: flex-end;
}

.page-navigation {
	margin-top: 20px;
	display: flex;
	justify-content: center;
	gap: 5px;
}

.page-navigation a {
	padding: 8px 16px;
	border: 1px solid #e5e7eb;
	border-radius: 4px;
	color: #374151;
	text-decoration: none;
	cursor: pointer;
	background-color: white;
}

.page-navigation a:hover {
	background: #f3f4f6;
}

.page-navigation span {
	padding: 8px 16px;
	border: 1px solid #a855f7;
	border-radius: 4px;
	background-color: #a855f7;
	color: white;
}

.page-navigation i {
	font-style: normal;
}

/* 비활성화된 페이지 링크 스타일 */
.page-navigation .disabled {
	color: #9ca3af;
	cursor: not-allowed;
	background-color: #f9fafb;
	border-color: #e5e7eb;
}

</style>
</head>
<body>
	<div class="container">
		<h1 style="margin: 20px 0; font-size: 24px; color: #374151;">신고 관리</h1>

		<form name="searchForm"
			action="${pageContext.request.contextPath}/admin/home/report"
			method="post" class="row g-3 bg-white p-3 mb-4 rounded shadow-sm">
			<input type="hidden" name="page" value="1">
			<div class="col-md-2">
				<select name="schType" class="form-select">
					<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
					<option value="reportTitle" ${schType=="reportTitle"?"selected":""}>신고
						제목</option>
					<option value="reportContent"
						${schType=="reportContent"?"selected":""}>신고 내용</option>
					<option value="nickName" ${schType=="nickName"?"selected":""}>신고자</option>
				</select>
			</div>
			<div class="col-md-8">
				<input type="text" name="kwd" value="${kwd}" class="form-control"
					placeholder="검색어를 입력하세요">
			</div>
			<div class="col-md-2">
				<button type="button" class="btn btn-primary w-100"
					onclick="searchList()">검색</button>
			</div>
		</form>

		<div class="table-container">
			<div class="table-header">
				<h2 class="table-title">신고 목록</h2>
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
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${list}">
						<tr>
							<td>${dto.RP_num}</td>
							<td>${dto.RP_title}</td>
							<td>${dto.RP_content}</td>
							<td>${dto.RP_reason}</td>
							<td>${dto.RP_table}</td>
							<td>${dto.memberName}</td>
							<td>
								<button class="btn" onclick="viewReport(${dto.RP_num})">상세보기</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="page-navigation">${paging}</div>
	</div>


	<div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="reportModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportModalLabel">신고 상세 내용</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- 신고 정보 섹션 -->
                <div class="border-bottom pb-3 mb-3">
                    <h6 class="text-muted mb-3">신고 정보</h6>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>신고 제목:</strong> <span id="modalTitle"></span></p>
                            <p><strong>신고자:</strong> <span id="modalReporter"></span></p>
                            <p><strong>신고 사유:</strong> <span id="modalReason"></span></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>게시판:</strong> <span id="modalTable"></span></p>
                            <p><strong>신고 내용:</strong> <span id="modalContent"></span></p>
                        </div>
                    </div>
                </div>
                
                <!-- 신고된 게시글 섹션 -->
                <div>
                    <h6 class="text-muted mb-3">신고된 게시글</h6>
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title" id="postTitle"></h5>
                            <h6 class="card-subtitle mb-2 text-muted" id="postWriter"></h6>
                            <p class="card-text" id="postContent"></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-danger" onclick="blockUser()">차단</button>
                <button type="button" class="btn btn-warning" onclick="rejectReport()">거부</button>
            </div>
        </div>
    </div>
</div>
<script>
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

// 차단 기능
function blockUser() {
    if(confirm('해당 사용자를 차단하시겠습니까?')) {
        fetch('${pageContext.request.contextPath}/admin/report/block?rpNum=' + currentReportId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if(data.state === 'success') {
                alert('사용자가 차단되었습니다.');
                location.reload();
            } else {
                alert('차단 처리 중 오류가 발생했습니다.');
            }
        });
    }
}

// 신고 거부 기능
function rejectReport() {
    if(confirm('이 신고를 거부하시겠습니까?')) {
        fetch('${pageContext.request.contextPath}/admin/report/reject?rpNum=' + currentReportId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if(data.state === 'success') {
                alert('신고가 거부되었습니다.');
                location.reload();
            }
        });
    }
}

</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
