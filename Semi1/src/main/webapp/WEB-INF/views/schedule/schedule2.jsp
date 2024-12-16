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
                    <option value="2024-2">2024년 2학기</option>
                    <option value="2024-1">2024년 1학기</option>
                    <option value="2023-2">2023년 2학기</option>
                    <option value="2023-1">2023년 1학기</option>
                    <option value="2022-2">2022년 2학기</option>
                    <option value="2022-1">2022년 1학기</option>
                </select>
            </div>
		</div>
		
		<nav class="timetable-btn-container" id="timetableBtnContainer">
		    <button class="timetable-btn">시간표 1</button>
		    <button class="timetable-btn">시간표 2</button>
		    <button class="add-timetable-btn">새 시간표 만들기</button>
		</nav>
			
			  
        <!-- 시간표 그리드 -->
        <div class="timetable">
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

                </tbody>
            </table>
        </div>


        <!-- 수업 검색 모달을 여는 버튼 -->
           <!-- 수업 검색 버튼을 시간표 안에 위치시키기 -->
            <div class="search-btn-container">
                <button id="searchModalBtn" class="search-button"><i class="fi fi-rr-search"></i>수업 검색</button>
            </div>
    </main>

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