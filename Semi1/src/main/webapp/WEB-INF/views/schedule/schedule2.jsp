<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/core.css" type="text/css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/util-jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/core.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/header.js"></script>




<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ScheduleCss.css" type="text/css">

</head>
<body>

	<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	
    <main>
    <form action="/SaveTimetableServlet" method="POST"></form>
        <div class="time-table-selector">
            <div class="select-box-container">
                <select id="semesterSelect" name="semester">
                    <option value="4학년 2학기" onclick="sendServer(4,2)">4학년 2학기</option>
                    <option value="4학년 1학기" onclick="sendServer(4,1)">4학년 1학기</option>
                    <option value="3학년 2학기" onclick="sendServer(3,2)">3학년 2학기</option>
                    <option value="3학년 1학기" onclick="sendServer(3,1)">3학년 1학기</option>
                    <option value="2학년 2학기" onclick="sendServer(2,2)">2학년 2학기</option>
                    <option value="2학년 1학기" onclick="sendServer(2,1)">2학년 1학기</option>
                    <option value="1학년 2학기" onclick="sendServer(1,2)">1학년 2학기</option>
                    <option value="1학년 1학기" onclick="sendServer(1,1)">1학년 1학기</option>
                </select>

		<nav class="timetable-btn-container" id="timetableBtnContainer">
		    <button class="save-timetable-btn" onclick="saveTimetable()"> SAVE!<br>TIMETABLE </button>
		</nav>

            </div>
		</div>

		 
		
        <!-- 시간표 그리드 -->
        <div class="timetable">
        	<div class="texttable"></div>
            <table id="timetable">
                <thead>
                    <tr>
                        <th class="time-slot" id="monday" style="background: #E8D9FF">월</th>
                        <th class="time-slot" id="tuesday" style="background: #E8D9FF">화</th>
                        <th class="time-slot" id="wednesday" style="background: #E8D9FF">수</th>
                        <th class="time-slot" id="thursday" style="background: #E8D9FF">목</th>
                        <th class="time-slot" id="friday" style="background: #E8D9FF">금</th>
                        <th class="time-slot" width="30px" style="background: #E8D9FF">시간</th>
                    </tr>
                </thead>
                <tbody>
					<!-- 시간표 내용 -->
                </tbody>
            </table>
        </div>

        <!-- 수업 검색 모달을 여는 버튼 -->
           <!-- 수업 검색 버튼을 시간표 안에 위치시키기 -->
            <div class="search-btn-container">
                <button id="searchModalBtn" class="search-button"><i class="fi fi-rr-search"></i>수업 검색</button>
            </div>
    </main>
    	
    <script>
    // 페이지 로드 후 이벤트 리스너 등록
    document.addEventListener('DOMContentLoaded', function() {
        const semesterSelect = document.getElementById('semesterSelect');  // 셀렉트 박스
        const textTable = document.querySelector('.texttable');  // '시간표' 텍스트가 있는 div

        // 셀렉트 박스 값이 변경될 때마다 실행되는 함수
        semesterSelect.addEventListener('change', function() {
            const selectedValue = semesterSelect.value;  // 선택된 값 가져오기
            textTable.textContent = selectedValue + " 시간표";  // '시간표' 텍스트에 반영
        });

        // 초기 셀렉트 박스 값에 맞게 설정 (페이지 로드 시)
        const initialValue = semesterSelect.value;
        textTable.textContent = initialValue + " 시간표";  // 초기 텍스트 설정
    });
    
    document.addEventListener("DOMContentLoaded", function() {
        console.log("문서가 로드되었습니다.");
        // 모든 .modal-line 요소에 클릭 이벤트를 추가
        const modalLines = document.querySelectorAll('.modal-line');
        modalLines.forEach(function(modalLine) {
            modalLine.addEventListener('click', function(event) {
                // 클릭된 요소가 td인지 확인
                if (event.target.tagName === 'TD') {
                    console.log('클릭된 td:', event.target); // 클릭된 td 요소
                    console.log('클릭되었음:', true); // 클릭 이벤트가 발생했는지 확인

                    // 'selected' 클래스 토글
                    event.target.classList.toggle('selected');
                    console.log('selected 클래스 여부:', event.target.classList.contains('selected'));
                    
                }
            });
        });
    }); 
    
	// 수업 저장 버튼 클릭 이벤트 처리
	function saveTimetable() {
	    const semester = document.getElementById('semesterSelect').value;  // 선택된 학기
	    const selectedSubjects = [];  // 선택된 수업들을 저장할 배열
	    
	    // 학년과 학기 분리
	    const semesterParts = semester.split(" ");
	    const grade = semesterParts[0];  // 예: "1학년"
	    const term = semesterParts[1];   // 예: "1학기"

	    // 시간표에서 선택된 수업을 배열에 추가
	    $('.modal-line td.selected').each(function() {
	        const subject = {
	            sbNum: $(this).closest('tr').data('sbnum'),  // 과목 번호
	        };
	        selectedSubjects.push(subject);  // 배열에 수업 추가
	    });
	    
	    console.log("선택된 학기:", semesterParts);
	    console.log("선택된 수업들:", selectedSubjects);

	    // 선택된 수업이 없으면 경고
	    if (selectedSubjects.length === 0) { 
	        alert('선택된 수업이 없습니다.');
	        return;  // 수업이 선택되지 않으면 함수 종료
	    }

	    // AJAX 요청으로 서버에 데이터 전송
	    $.ajax({
	        url: '/Semi1/SaveTimetableServlet',  // 서버 URL
	        type: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({ 
	            subjects: selectedSubjects.map(function(subject) {
	                // 여기서 sbNum을 String으로 변환
	                subject.sbNum = String(subject.sbNum);
	                return subject;
	            }),
	            semester: semester  // 선택된 학기 정보
	        }),  
	        success: function(response) {
	            if (response.status === 'success') {
	                alert('시간표 저장 성공!');
	                location.reload();  // 저장 후 페이지 새로고침
	            } else {
	                alert('시간표 저장 실패!');
	            }
	    },
	    error: function(xhr, status, error) {
	        console.log('AJAX 요청 실패:', status, error);  // 에러 상태 출력
	        alert('서버 오류가 발생했습니다.');
	        console.log('서버 응답 텍스트:', xhr.responseText);  // 응답 내용 출력
	        console.log('선택된 과목:', selectedSubjects);  // 실패 시 선택된 과목 출력
	    }
	});

	}
	
	function loadTimetable(timetableData) {
	    // 시간표 데이터 배열을 순회하면서 각 시간대와 요일에 맞게 과목을 추가
	    timetableData.forEach(item => {
	        const { dtSubNum, gradeYear, semester, mbNum } = item; // 과목 번호, 학년, 학기, 회원 번호

	        // 각 과목이 배치될 시간대와 요일은 클릭 이벤트로 받아오는 방식이므로
	        // 과목을 추가할 곳을 찾는 로직이 필요합니다
	        // 예시로, studyDay(요일)과 studyTime(교시)에 맞게 셀을 찾는 코드
	        const studyDay = getStudyDay();   // 사용자가 클릭한 요일을 얻어오는 함수
	        const studyTime = getStudyTime(); // 사용자가 클릭한 시간을 얻어오는 함수

	        // 셀을 찾아서 과목명과 색상으로 채움
	        const cell = $(`#timetable tr[data-time="${studyTime}"] td[data-day="${studyDay}"]`);
	        cell.text("과목 번호: " + dtSubNum);  // 과목 번호 표시 (예: 1001)
	        cell.css("background-color", "#FFD8D8");  // 과목 색상 채우기 (예: 붉은색)
	    });
	}
	
    
	</script>

    <!-- 수업 검색 모달 -->
    <div id="searchModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <div id="input-boxx">
           	 	<h3>&nbsp;&nbsp;&nbsp;수업</h3>&nbsp;&nbsp;&nbsp;
            	<input type="text" id="search" placeholder="검색...">
            </div>
            <table class="subject">
                <thead>
                    <tr>
                        <th>학년</th>
                        <th>학수번호</th>
                        <th>교과목명</th>
                        <th>학점</th>
                        <th>교시</th>
                    </tr>
                </thead>
                <tbody>
                
				<c:forEach var="vo" items="${viewSubject}">
				    <tr class="modal-line" data-day="${vo.studyDay}" data-start="${vo.studytime}" data-color="${vo.color}" data-sbname="${vo.sbName}" data-sbnum="${vo.sbNum}">
				        <td>${vo.stGrade}</td>
				        <td>${vo.sbNum}</td>
				        <td>${vo.sbName}</td>
				        <td>${vo.hakscore}</td>
				        <td>${vo.studytime}</td> <!-- studyTime은 이미 String -->
				    </tr>
				</c:forEach>
                	
                	<c:if test="${empty viewSubject}">
    					<p>수업 정보가 없습니다.</p>
					</c:if>
                	                   
                </tbody>
            </table>
        </div>
    </div>
    
<script src="${pageContext.request.contextPath}/resources/js/Schedule.js"></script>

</body>
</html>
