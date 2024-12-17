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
<script src="${pageContext.request.contextPath}/resources/js/Schedule.js"></script>

</head>
<body>

	<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	
    <main>
        <div class="time-table-selector">
            <div class="select-box-container">
                <select id="semesterSelect">
                    <option value="4학년 2학기">4학년 2학기</option>
                    <option value="4학년 1학기">4학년 1학기</option>
                    <option value="3학년 2학기">3학년 2학기</option>
                    <option value="3학년 1학기">3학년 1학기</option>
                    <option value="2학년 2학기">2학년 2학기</option>
                    <option value="2학년 1학기">2학년 1학기</option>
                    <option value="1학년 2학기">1학년 2학기</option>
                    <option value="1학년 1학기">1학년 1학기</option>
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
	</script>

    <!-- 수업 검색 모달 -->
    <div id="searchModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <div id="input-boxx">
           	 	<h3>&nbsp;&nbsp;&nbsp;수업</h3>&nbsp;&nbsp;&nbsp;
            	<input type="text" id="search" placeholder="검색...">
            </div>
            <table>
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
				    <tr class="modal-line" data-day="${vo.studyDay}" data-start="${vo.studytime}" data-color="${vo.color}" data-sbname="${vo.sbName}">
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
    

</body>
</html>
