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
<main class="">
	<div class="container">
	    <div class="stats-container">
	        <div class="stats-grid">
	            <div class="stat-item">
	                <div class="stat-label">전체 평점</div>
	                <div class="stat-value">3.85<span class="stat-max">/4.5</span></div>
	            </div>
	            <div class="stat-item">
	                <div class="stat-label">전공 평점</div>
	                <div class="stat-value">3.74<span class="stat-max">/4.5</span></div>
	            </div>
	            <div class="stat-item">
	                <div class="stat-label">취득 학점</div>
	                <div class="stat-value">128<span class="stat-max">/150</span></div>
	            </div>
	        </div>
	        
	    	<div class="graph">
	            <canvas id="gradeGraph" width="400" height="150"></canvas>
	        </div>
	    </div>
	
	    <div class="semester-nav">
	        <button class="semester-btn">1학년 1학기</button>
	        <button class="semester-btn">1학년 2학기</button>
	        <button class="semester-btn">2학년 1학기</button>
	        <button class="semester-btn">2학년 2학기</button>
	        <button class="semester-btn">3학년 1학기</button>
	        <button class="semester-btn">3학년 2학기</button>
	        <button class="semester-btn active">4학년 1학기</button>
	        <button class="semester-btn">4학년 2학기</button>
	    </div>
	
	    <div class="grade-table">
	        <div class="grade-header">
	            <div class="grade-title">4학년 1학기</div>
	            <button class="submit-btn">시간표 불러오기</button>
	        </div>
	        <div class="grade-sub">
	        	평점<span class="highlight">3.9</span> 전공<span class="highlight">0</span> 취득<span class="highlight">10</span>
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
	                <tr>
	                    <td>실무프로젝트</td>
	                    <td><input type="text" class="credit-input" value="4"></td>
	                    <td>
	                        <select>
								<option>A+</option>
	                            <option>A0</option>
	                            <option>B+</option>
	                            <option>B0</option>
	                            <option>C+</option>
	                            <option>C0</option>
	                            <option>D+</option>
	                            <option>D0</option>
	                            <option>F</option>
	                        </select>
	                    </td>
	                </tr>
	                <tr>
	                    <td>모바일프로그래밍</td>
	                    <td><input type="text" class="credit-input" value="3"></td>
	                    <td>
	                        <select>
								<option>A+</option>
	                            <option>A0</option>
	                            <option>B+</option>
	                            <option>B0</option>
	                            <option>C+</option>
	                            <option>C0</option>
	                            <option>D+</option>
	                            <option>D0</option>
	                            <option>F</option>
	                        </select>
	                    </td>
	                </tr>
	                <!-- 추가 행들 -->
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