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
	                <div class="stat-value">${totalGpa}<span class="stat-max">/4.5</span></div>
	            </div>
	            <div class="stat-item">
	                <div class="stat-label">취득 학점</div>
	                <div class="stat-value">${totalCredits}<span class="stat-max">/150</span></div>
	            </div>
	        </div>
	        
	        <div class="graph-con">
		    	<div class="graph">
		            <canvas id="gradeGraph" ></canvas>
		        </div>
		        <div class="chart">
		        	<canvas id="gradeChart"></canvas>
		        </div>
		    </div>
	    </div>
	
		<div class="semester-nav">
		    <button class="semester-btn" data-year="1" data-semester="1">1학년 1학기</button>
		    <button class="semester-btn" data-year="1" data-semester="2">1학년 2학기</button>
		    <button class="semester-btn" data-year="2" data-semester="1">2학년 1학기</button>
		    <button class="semester-btn" data-year="2" data-semester="2">2학년 2학기</button>
		    <button class="semester-btn" data-year="3" data-semester="1">3학년 1학기</button>
		    <button class="semester-btn" data-year="3" data-semester="2">3학년 2학기</button>
		    <button class="semester-btn" data-year="4" data-semester="1">4학년 1학기</button>
		    <button class="semester-btn" data-year="4" data-semester="2">4학년 2학기</button>
		</div>

	    <div class="grade-table">
	        <div class="grade-header">
	            <div class="grade-title">학년 학기</div>
	            <button class="submit-btn">시간표 불러오기</button>
	        </div>
	        <div class="grade-sub">
	        	평점<span class="gpa highlight">0.0</span> 취득<span class="credits highlight">0</span>
	        </div>
	        <table>
	            <thead>
	                <tr>
	                    <th>교과명</th>
	                    <th width="80" style="text-align: center;">학점</th>
	                    <th width="120" style="text-align: center;">성적</th>
	                </tr>
	            </thead>
	            
	            <tbody id="semester-data">
					
	            </tbody>
	        </table>
	    </div>
	</div>
</main>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />


<script type="text/javascript">

function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

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
			complete:function() {
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


$(function () {
    $(".semester-btn").click(function () {
        // 모든 버튼에서 'active' 클래스 제거 후, 클릭된 버튼에 추가
        $(".semester-btn").removeClass("active");
        $(this).addClass("active");
		
		let gradeYear = $(this).attr('data-year');
        let semester = $(this).attr('data-semester');
        
     	// 제목 변경
     	$(".grade-title").text(gradeYear + "학년 " + semester + "학기");
		
        let jsonUrl = '${pageContext.request.contextPath}/grade/gradeList';
        let jsonQuery = 'gradeYear=' + gradeYear + '&semester=' + semester;
        
        ajaxFun(jsonUrl, 'POST', jsonQuery, 'json', function (data) {
            // GPA 및 학점 업데이트
            $('.gpa').text(data.gpa);
            $('.credits').text(data.credits);
        });
        
           
     	// HTML 데이터 요청 (교과목 목록 업데이트)
     	let htmlUrl = '${pageContext.request.contextPath}/grade/gradeList';
     	let htmlQuery = 'gradeYear=' + gradeYear + '&semester=' + semester;
		
     	ajaxFun(htmlUrl, 'GET', htmlQuery, 'text', function (data) {
     		let tbody = $("#semester-data");
     	    tbody.html(data); // 반환된 HTML을 테이블에 삽입
		});
        
	});
});




let gradeChart; // Chart.js 인스턴스

// 그래프 생성/업데이트 함수
function createOrUpdateChart(semesters, overallGrades) {
    const ctx = document.getElementById("gradeGraph").getContext("2d");
    
    if (gradeChart) {
        // 기존 그래프 업데이트
        gradeChart.data.labels = semesters;
        gradeChart.data.datasets[0].data = overallGrades;
        gradeChart.update();
    } else {
        // 새 그래프 생성
        gradeChart = new Chart(ctx, {
            type: "line",
            data: {
                labels: semesters, // X축 라벨 배열
                datasets: [
                    {
                        label: "평점 (GPA)",
                        data: overallGrades, // GPA 데이터
                        borderColor: "red",
                        backgroundColor: "white",
                        fill: false,
                    },
                ],
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: "top",
                    },
                },
                
                scales: {
                	x: {
                		
                		ticks: {
                            callback: function (value, index) {
                                const label = this.getLabelForValue(value);
                                if (Array.isArray(label)) {
                                    return label.join("\n");
                                }
                                return label;
                            },
                            maxRotation: 0, // 라벨 회전 비활성화
                            minRotation: 0,
                        },
                      
                        grid: {
                            drawBorder: true, // X축 경계선 표시
                            drawTicks: false, // X축 틱 제거
                        },
                    },
                    y: {
                        beginAtZero: true,
                        
                        max: 5.0, // Y축 최대값 4.0
                        ticks: {
                            stepSize: 1.0, // Y축 간격 1.0 단위로 설정
                            callback: function (value) {
                            	return value === 0 || value === 5 ? "" : value.toFixed(1);
								// 1.0, 2.0, 3.0, 4.0 형식으로 표시
                            },
                        },
                        
                        grid: {
                            drawBorder: false, // Y축 경계선 표시
                            drawTicks: false, // X축 틱 제거
                        },
                        
                    },
                },
            },
        });
    }
}


// AJAX로 데이터 가져오기
function fetchGradeData() {
    const jsonUrl = "${pageContext.request.contextPath}/grade/list";

    $.ajax({
        url: jsonUrl,
        type: "POST",
        dataType: "json",
        success: function (data) {
        	const semesters = data.semesters; // 학기 데이터
            const overallGrades = data.overallGrades; // GPA 데이터

            // Chart.js 그래프 생성/업데이트
            createOrUpdateChart(semesters, overallGrades);
        },
        error: function (xhr, status, error) {
            console.error("데이터 로드 실패:", error);
        },
    });
}




let gradeDistributionChart; // Chart.js 인스턴스

function createOrUpdateGradeChart(gradeLabels, gradePercentages) {
    const ctx = document.getElementById("gradeChart").getContext("2d");

    // 1. 원하는 순서 정의
    const customOrder = ["A+", "A0", "B+", "B0", "C+", "C0", "P", "F"];

    // 2. 라벨과 데이터를 customOrder 기준으로 정렬
    const sortedData = gradeLabels
        .map((label, index) => ({
            label, // 성적 라벨 (예: A+, B+)
            percentage: gradePercentages[index], // 해당 라벨의 비율 데이터
        }))
        .sort((a, b) => customOrder.indexOf(a.label) - customOrder.indexOf(b.label)); // customOrder 기준으로 정렬

    // 정렬된 데이터에서 라벨과 비율을 분리
    const sortedLabels = sortedData.map((item) => item.label);
    const sortedPercentages = sortedData.map((item) => item.percentage);

    if (gradeDistributionChart) {
        // 기존 차트 업데이트
        gradeDistributionChart.data.labels = sortedLabels; // 정렬된 라벨
        gradeDistributionChart.data.datasets[0].data = sortedPercentages; // 정렬된 데이터
        gradeDistributionChart.update();
    } else {
        // 새 차트 생성
        gradeDistributionChart = new Chart(ctx, {
            type: "bar", // 막대형 차트
            data: {
                labels: sortedLabels, // 정렬된 Y축 라벨 (성적: A+, A0, ...)
                datasets: [
                    {
                        label: "성적 비율 (%)", // 범례 라벨
                        data: sortedPercentages, // 정렬된 X축 데이터
                        backgroundColor: [
                            "rgba(255, 99, 132, 0.5)", // A+
                            "rgba(255, 159, 64, 0.5)", // A0
                            "rgba(75, 192, 192, 0.5)", // B+
                            "rgba(54, 162, 235, 0.5)", // B0
                            "rgba(153, 102, 255, 0.5)", // C+
                            "rgba(201, 203, 207, 0.5)", // C0
                            "rgba(100, 149, 237, 0.5)", // P
                            "rgba(128, 128, 128, 0.5)", // F
                        ],
                        borderColor: [
                            "rgba(255, 99, 132, 1)", // A+
                            "rgba(255, 159, 64, 1)", // A0
                            "rgba(75, 192, 192, 1)", // B+
                            "rgba(54, 162, 235, 1)", // B0
                            "rgba(153, 102, 255, 1)", // C+
                            "rgba(201, 203, 207, 1)", // C0
                            "rgba(100, 149, 237, 1)", // P
                            "rgba(128, 128, 128, 1)", // F
                        ],
                        borderWidth: 1, // 막대 테두리 두께
                    },
                ],
            },
            options: {
                indexAxis: "y", // 가로 막대형 차트
                responsive: true,
                plugins: {
                    legend: {
                        display: false, // 범례 숨김
                    },
                    tooltip: {
                        callbacks: {
                            label: function (tooltipItem) {
                                return tooltipItem.raw + "%"; // 툴팁에 % 추가
                            },
                        },
                    },
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        max: 100, // X축 최대값 설정 (비율 기준 100%)
                        ticks: {
                            stepSize: 20, // X축 간격 20%
                            callback: function (value) {
                                return value + "%"; // X축에 % 표시
                            },
                        },
                        grid: {
                            color: "rgba(200, 200, 200, 0.3)", // X축 그리드 색상
                        },
                    },
                    y: {
                        ticks: {
                            color: "black", // Y축 텍스트 색상
                        },
                        grid: {
                            display: false, // Y축 그리드 숨기기
                        },
                    },
                },
            },
        });
    }
}


//AJAX로 데이터 가져오기
function fetchGradeDistribution() {
 const jsonUrl = "${pageContext.request.contextPath}/grade/distribution";

 $.ajax({
     url: jsonUrl,
     type: "GET",
     dataType: "json",
     success: function (data) {
         const gradePercentages = data.gradePercentages; // 서버에서 받은 성적 비율 데이터
         const labels = Object.keys(gradePercentages); // 성적 라벨 (A+, B+, ...)
         const percentages = Object.values(gradePercentages); // 각 성적의 비율

         // Chart.js 차트 생성/업데이트
         createOrUpdateGradeChart(labels, percentages);
     },
     error: function (xhr, status, error) {
         console.error("성적 분포 데이터 로드 실패:", error);
     },
 });
}


//페이지 로드 시 데이터 가져오기
$(document).ready(function () {
    fetchGradeData();
    
    fetchGradeDistribution();
});



</script>

</body>
</html>