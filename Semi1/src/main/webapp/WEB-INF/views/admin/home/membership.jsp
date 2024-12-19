<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<!-- jQuery 추가 -->
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Bootstrap JS -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/admin/main.css">

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trainee Admin - 회원 관리</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/admin/main.css">
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

/* 기본 컨테이너 스타일 */
.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

/* 검색 영역 */
.search-area {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
}

.search-row {
	display: flex;
	gap: 10px;
	align-items: center;
	margin-bottom: 10px;
}

/* 필터 및 검색 요소 */
select, input[type="text"] {
	padding: 8px 12px;
	border: 1px solid #e1e1e1;
	border-radius: 4px;
	font-size: 14px;
	min-width: 150px;
}

input[type="text"] {
	flex: 1;
	min-width: 200px;
}

/* 버튼 스타일 */
.btn {
	padding: 8px 16px;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	border: none;
	display: inline-flex;
	align-items: center;
	gap: 6px;
}

.btn-primary {
	background: #a855f7;
	color: white;
}

.btn-primary:hover {
	background: #9333ea;
}

.btn-success {
	background: #22c55e;
	color: white;
}

.btn-danger {
	background: #ef4444;
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
	background: #f9fafb;
	border-bottom: 1px solid #e5e7eb;
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

.status-active {
	background: #dcfce7;
	color: #16a34a;
}

.status-inactive {
	background: #fee2e2;
	color: #dc2626;
}

/* 페이지네이션 */
.pagination {
	display: flex;
	justify-content: center;
	gap: 5px;
	padding: 20px;
}

.pagination button {
	padding: 8px 12px;
	border: 1px solid #e5e7eb;
	background: white;
	color: #374151;
	border-radius: 4px;
	cursor: pointer;
}

.pagination button.active {
	background: #a855f7;
	color: white;
	border-color: #a855f7;
}

/* 체크박스 스타일 */
.custom-checkbox {
	width: 16px;
	height: 16px;
	cursor: pointer;
}

/* 액션 버튼 영역 */
.action-buttons {
	padding: 15px 20px;
	border-bottom: 1px solid #e5e7eb;
	display: flex;
	gap: 10px;
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
</style>
<script type="text/javascript">
let currentMemberNum = 0;
let currentLessonNum = 0;
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/adminSide.jsp" />
	<div class="container">
		<h1 style="margin: 20px 0; font-size: 24px; color: #374151;">회원
			관리</h1>

		<form name="searchForm"
			action="${pageContext.request.contextPath}/admin/home/membership"
			method="get" class="search-area">
			<div class="search-row">
				<select name="schType">
					<option value="all" ${schType=="all"?"selected":""}>전체</option>
					<option value="userId" ${schType=="userId"?"selected":""}>아이디</option>
					<option value="nickName" ${schType=="nickName"?"selected":""}>닉네임</option>
				</select> <input type="text" name="kwd" value="${kwd}"
					placeholder="검색어를 입력하세요">
				<button type="button" class="btn btn-primary" onclick="searchList()">검색</button>
				<button type="button" class="btn btn-success"
					onclick="exportToExcel()">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
						viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2">
                        <path
							d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M7 10l5 5 5-5M12 15V3" />
                    </svg>
					엑셀 내보내기
				</button>
			</div>
		</form>

		<div class="table-container">
			<div class="action-buttons">
				<button class="btn btn-primary"
					onclick="updateSelectedStatus('active')">선택 활성화</button>
				<button class="btn btn-primary"
					onclick="updateSelectedStatus('block')">선택 정지</button>
			</div>

			<table>
				<thead>
					<tr>
						<th><input type="checkbox" class="custom-checkbox"
							onclick="checkAll(this)"></th>
						<th>회원ID</th>
						<th>아이디</th>
						<th>닉네임</th>
						<th>가입일</th>
						<th>소속학과</th>
						<th>권한</th>
						<th>상태</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${list}">
						<tr>
							<td><input type="checkbox" class="custom-checkbox"
								name="members" value="${dto.mb_Num}"></td>
							<td>${dto.mb_Num}</td>
							<td>${dto.userId}</td>
							<td>${dto.nickName}</td>
							<td>${dto.ca_Day}</td>
							<td>${dto.lessonName}</td>
							<td><c:choose>
									<c:when test="${dto.role == '60'}">관리자</c:when>
									<c:when test="${dto.role >= '51' && dto.role <= '56'}">과대표</c:when>
									<c:otherwise>일반회원</c:otherwise>
								</c:choose></td>
							<td><span
								class="status-badge ${dto.block == 0 ? 'status-active' : 'status-inactive'}">
									${dto.block == 0 ? '활성' : '정지'} </span></td>
							<td>
								<button class="btn btn-primary"
									onclick="openMemberModal(${dto.mb_Num})">권한조정</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="page-navigation">${paging}</div>
		</div>

		<!-- 모달 -->
		<div class="modal fade" id="memberModal" tabindex="-1" role="dialog"
			aria-labelledby="memberModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h3 class="modal-title">권한 조정</h3>
						<button type="button" class="modal-close"
							onclick="closeMemberModal()">&times;</button>
					</div>
					<div class="modal-body">
						<div class="form-group mb-3">
							<label class="fw-bold mb-2">현재 권한</label>
							<div id="currentRole" class="form-control-plaintext"></div>
						</div>
						<div class="form-group mb-3">
							<label class="fw-bold mb-2">변경할 권한</label> <select id="newRole"
								class="form-control">
								<option value="40">일반 학생</option>
								<option value="51">경영학과 과대표</option>
								<option value="52">경찰행정학과 과대표</option>
								<option value="53">디자인학과 과대표</option>
								<option value="54">음악공연학과 과대표</option>
								<option value="55">컴퓨터공학전자과 과대표</option>
								<option value="56">정보통신학과 과대표</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							onclick="updateRole()">변경하기</button>
						<button type="button" class="btn btn-secondary"
							onclick="closeMemberModal()">닫기</button>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript">
        // Ajax 함수 정의
        function ajaxFun(url, method, formData, dataType, fn, file=false) {
            const settings = {
                type: method,
                data: formData,
                dataType: dataType,
                success: function(data) {
                    fn(data);
                },
                beforeSend: function(jqXHR) {
                    jqXHR.setRequestHeader('AJAX', true);
                },
                error: function(jqXHR) {
                    if(jqXHR.status === 403) {
                        login();
                        return false;
                    } else if(jqXHR.status === 406) {
                        alert('요청 처리가 실패했습니다.');
                        return false;
                    }
                    console.log(jqXHR.responseText);
                }
            };
            
            if(file) {
                settings.processData = false;
                settings.contentType = false;
            }
            
            $.ajax(url, settings);
        }

        
        // 회원 상태 변경 함수
        function updateSelectedStatus(status) {
            const selectedMembers = getSelectedMembers();
            if(selectedMembers.length === 0) {
                alert('회원을 선택하세요.');
                return;
            }

            const message = status === 'active' ? '선택한 회원을 활성화하시겠습니까?' : '선택한 회원을 정지하시겠습니까?';
            if(!confirm(message)) {
                return;
            }

            const url = '${pageContext.request.contextPath}/admin/member/updateStatus';
            const query = 'memberIds=' + selectedMembers.join(',') + '&status=' + status;
            
            const fn = function(data) {
                if(data.state === 'success') {
                    alert(status === 'active' ? '활성화되었습니다.' : '정지되었습니다.');
                    location.reload();
                } else {
                    alert('처리 중 오류가 발생했습니다.');
                }
            };

            ajaxFun(url, 'post', query, 'json', fn);
        }

        // 선택된 회원 목록 가져오기
        function getSelectedMembers() {
            const checkboxes = document.getElementsByName('members');
            const selectedMembers = [];
            for(let i=0; i<checkboxes.length; i++) {
                if(checkboxes[i].checked) {
                    selectedMembers.push(checkboxes[i].value);
                }
            }
            return selectedMembers;
        }

        // 체크박스 전체 선택/해제
        function checkAll(source) {
            const checkboxes = document.getElementsByName('members');
            for(let i=0; i<checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
        }

        // 검색 함수
        function searchList() {
            const f = document.searchForm;
            f.submit();
        }

        function openMemberModal(memberNum) {
            currentMemberNum = memberNum;
            const myModal = new bootstrap.Modal(document.getElementById('memberModal'));
            
            const url = '${pageContext.request.contextPath}/admin/member/detail';
            const query = 'memberNum=' + memberNum;
            
            const fn = function(data) {
                if(data.state === "success") {
                    const currentRoleElement = document.getElementById('currentRole');
                    if (currentRoleElement) {
                        currentRoleElement.textContent = data.role === "40" ? "일반 학생" : `${data.lessonName} 과대표`;
                    }
                        
                    currentLessonNum = data.lessonNum;
                    
                    // 소속학과의 과대표 옵션만 선택 가능하도록 설정
                    const roleSelect = document.getElementById('newRole');
                    if (roleSelect) {
                        for(let option of roleSelect.options) {
                            if(option.value === "40") {
                                option.disabled = false;  // 일반 학생은 항상 선택 가능
                                continue;
                            }
                            // lessonNum이 이미 51~56 사이의 값이므로, 추가로 51을 더하지 않음
                            option.disabled = (parseInt(option.value) !== currentLessonNum);
                        }
                        
                        // 현재 권한 선택
                        roleSelect.value = data.role;
                    }
                    
                    myModal.show();
                } else {
                    alert('데이터를 가져오는데 실패했습니다.');
                }
            };
            
            ajaxFun(url, "get", query, "json", fn);
        }


        function closeMemberModal() {
            const myModal = bootstrap.Modal.getInstance(document.getElementById('memberModal'));
            if (myModal) {
                myModal.hide();
            }
        }


        function login() {
            location.href = '${pageContext.request.contextPath}/member/login';
        }
        
        // 역할 변경
        function updateRole() {
		    const newRole = document.getElementById('newRole').value;
		    if(!confirm('권한을 변경하시겠습니까?')) {
		        return;
		    }
		    
		    const url = '${pageContext.request.contextPath}/admin/member/updateRole';
		    const query = 'memberNum=' + currentMemberNum + '&role=' + newRole;
		    
		    const fn = function(data) {
		        if(data.state === "success") {
		            alert('권한이 변경되었습니다.');
		            location.reload();
		        } else {
		            alert('권한 변경에 실패했습니다.' + (data.message ? '\n' + data.message : ''));
		        }
		    };
		    
		    ajaxFun(url, "post", query, "json", fn);
		}
        
        
    </script>
		</head>