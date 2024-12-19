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
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/report.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/main.js"></script>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trainee Admin - 신고 관리</title>
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
