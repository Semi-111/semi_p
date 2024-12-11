<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학점 계산기</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/grade/grade.css" type="text/css">

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<body>


<jsp:include page="/WEB-INF/views/layout/header.jsp" />
<main class="grade-main">
	<div class="container">
	    <div class="stats-container">
	    	<!-- 학점 통계 -->
	        <div class="stats-grid">
	            <div class="stat-item">
	                <div class="stat-label">전체 평점</div>
<<<<<<< HEAD
	                <div class="stat-value">3.8<span class="stat-max">/4.5</span></div>
=======
	                <div class="stat-value">${gpa}<span class="stat-max">/4.5</span></div>
>>>>>>> branch 'master' of https://github.com/Semi-111/semi_p.git
	            </div>
	            <div class="stat-item">
	                <div class="stat-label">취득 학점</div>
<<<<<<< HEAD
	                <div class="stat-value">130<span class="stat-max">/150</span></div>
=======
	                <div class="stat-value">${totalCredits}<span class="stat-max">/150</span></div>
>>>>>>> branch 'master' of https://github.com/Semi-111/semi_p.git
	            </div>
	        </div>
	        
	    	<div class="graph">
	            <canvas id="gradeGraph" width="400" height="150"></canvas>
	        </div>
	    </div>
	
		<div class="semester-nav">
		    <form method="get" action="${pageContext.request.contextPath}/grade/list">
		        <button type="submit" class="semester-btn ${gradeYear == '1학년' && semester == '1학기' ? 'active' : ''}"
		                name="gradeYear" value="1학년" formaction="?semester=1학기">
		            1학년 1학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '1학년' && semester == '2학기' ? 'active' : ''}"
		                name="gradeYear" value="1학년" formaction="?semester=2학기">
		            1학년 2학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '2학년' && semester == '1학기' ? 'active' : ''}"
		                name="gradeYear" value="2학년" formaction="?semester=1학기">
		            2학년 1학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '2학년' && semester == '2학기' ? 'active' : ''}"
		                name="gradeYear" value="2학년" formaction="?semester=2학기">
		            2학년 2학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '3학년' && semester == '1학기' ? 'active' : ''}"
		                name="gradeYear" value="3학년" formaction="?semester=1학기">
		            3학년 1학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '3학년' && semester == '2학기' ? 'active' : ''}"
		                name="gradeYear" value="3학년" formaction="?semester=2학기">
		            3학년 2학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '4학년' && semester == '1학기' ? 'active' : ''}"
		                name="gradeYear" value="4학년" formaction="?semester=1학기">
		            4학년 1학기
		        </button>
		        <button type="submit" class="semester-btn ${gradeYear == '4학년' && semester == '2학기' ? 'active' : ''}"
		                name="gradeYear" value="4학년" formaction="?semester=2학기">
		            4학년 2학기
		        </button>
		    </form>
		</div>

		
	   <!--  <div class="semester-nav">
	        <button class="semester-btn">1학년 1학기</button>
	        <button class="semester-btn">1학년 2학기</button>
	        <button class="semester-btn">2학년 1학기</button>
	        <button class="semester-btn">2학년 2학기</button>
	        <button class="semester-btn">3학년 1학기</button>
	        <button class="semester-btn">3학년 2학기</button>
	        <button class="semester-btn active">4학년 1학기</button>
	        <button class="semester-btn">4학년 2학기</button>
	    </div> -->
	
	    <div class="grade-table">
	        <div class="grade-header">
	            <div class="grade-title">4학년 1학기</div>
	            <button class="submit-btn">시간표 불러오기</button>
	        </div>
	        <div class="grade-sub">
	        	평점<span class="highlight">${gpa}</span> 취득<span class="highlight">${totalCredits}</span>
	        </div>
	        <table>
	            <thead>
	                <tr>
	                    <th>교과명</th>
	                    <th width="80">학점</th>
	                    <th width="120">성적</th>
	                </tr>
	            </thead>
	            <tbody>
	            	<c:forEach var="dto" items="${gradeList}">
	            		<tr>
		            		<td>${dto.sb_Name}</td>
		            		<td><input type="text" class="credit-input" value="${dto.hakscore}"></td>
		            		<td>
		            			<select>
				            		<option value="A+" <c:if test="${dto.grade == 'A+'}">selected</c:if>>A+</option>
				                    <option value="A0" <c:if test="${dto.grade == 'A0'}">selected</c:if>>A0</option>
				                    <option value="B+" <c:if test="${dto.grade == 'B+'}">selected</c:if>>B+</option>
				                    <option value="B0" <c:if test="${dto.grade == 'B0'}">selected</c:if>>B0</option>
				                    <option value="C+" <c:if test="${dto.grade == 'C+'}">selected</c:if>>C+</option>
				                    <option value="C0" <c:if test="${dto.grade == 'C0'}">selected</c:if>>C0</option>
				                    <option value="D+" <c:if test="${dto.grade == 'D+'}">selected</c:if>>D+</option>
				                    <option value="D0" <c:if test="${dto.grade == 'D0'}">selected</c:if>>D0</option>
				                    <option value="F" <c:if test="${dto.grade == 'F'}">selected</c:if>>F</option>
	                			</select>
		            		</td>
		            	</tr>
	            	</c:forEach>
	            </tbody>
	        </table>
	    </div>
	</div>
</main>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />


<script type="text/javascript">
// 학기별 학점 데이터
const semesters = ["1학년 1학기", "1학년 2학기", "2학년 1학기", "2학년 2학기", "3학년 1학기", "3학년 2학기", "4학년 1학기", "4학년 2학기"];
const overallGrades = [3.5, 3.8, 3.6, 3.9, 3.7, 3.8, 4.0, 3.9]; // 전체 평균
// const majorGrades = [3.4, 3.7, 3.5, 3.8, 3.6, 3.7, 3.9, 3.8]; // 전공 평균

// 그래프 그리기
const ctx = document.getElementById('gradeGraph').getContext('2d');
new Chart(ctx, {
    type: 'line', // 라인 그래프
    data: {
        labels: semesters, // 학기 라벨
        datasets: [
            {
                label: '전체 평균',
                data: overallGrades,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                fill: false,
                tension: 0.1 // 곡선의 부드러움
            }
            /*
            ,{
                label: '전공 평균',
                data: majorGrades,
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                fill: false,
                tension: 0.1
            }
            */
        ]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'top'
            }
        },
        scales: {
            y: {
                beginAtZero: false,
                max: 4.5 // 최대 평점
            }
        }
    }
});
</script>

</body>
</html>