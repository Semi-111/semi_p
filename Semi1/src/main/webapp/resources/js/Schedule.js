	

document.addEventListener('DOMContentLoaded', function () {
    const searchModalBtn = document.getElementById('searchModalBtn');
    const modal = document.getElementById('searchModal');
    const searchBtnContainer = document.querySelector('.search-btn-container');
    const closeModal = document.querySelector('.close'); // 모달을 닫는 버튼

	// 수업 검색 버튼 클릭 시 모달 열기/닫기 토글
	searchModalBtn.addEventListener('click', function() {
	    if (modal.classList.contains('show')) {
	        // 모달이 열려있을 때
	        modal.classList.remove('show'); // 모달 숨기기
	        searchBtnContainer.classList.remove('modal-open'); // 버튼 원위치로 돌아가기
	    } else {
	        // 모달이 닫혀있을 때
	        modal.classList.add('show'); // 모달 열기
	        searchBtnContainer.classList.add('modal-open'); // 버튼을 위로 이동
	    }
	});

    // 모달 닫기 버튼 클릭 시
    closeModal.addEventListener('click', function() {
        modal.classList.remove('show'); // 모달 숨기기
        searchBtnContainer.classList.remove('modal-open'); // 버튼 원위치로 돌아가기
    });

    // 모달 밖을 클릭해서 닫을 경우에도 버튼 원위치로 돌아가도록 처리
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.classList.remove('show'); // 모달 숨기기
            searchBtnContainer.classList.remove('modal-open'); // 버튼 원위치로 돌아가기
        }
    });
});

	document.addEventListener('DOMContentLoaded', function () {
	    const tableBody = document.querySelector('#timetable tbody');
	    
	    // 시간과 30분 간격을 설정 (09:00부터 19:00까지)
	    const startHour = 9; // 시작 시간 (09:00)
	    const endHour = 19;  // 종료 시간 (19:00)
		
	
	    // 30분 간격으로 시간 추가 함수
	    function createTimetable() {
			
	        let currentHour = startHour;
			
	        // 09:00 ~ 19:00까지 30분 단위로 시간표 추가
	        for (let i = 0; i < 22; i++) { // 총 22번 (09:00~19:00, 30분 간격)
	            const row = document.createElement('tr');
	            
	            // 월, 화, 수, 목 각 요일에 빈 셀 추가
	            for (let j = 0; j < 5; j++) {
	                const emptyCell = document.createElement('td');
	                row.appendChild(emptyCell);
	            }
	
	            // 시간 셀을 금요일 셀에 추가 (맨 끝에)
	            const timeCell = document.createElement('td');
	            let hour = currentHour;
	            let minute = (i % 2 === 0) ? "00" : "30"; // 30분 간격
	
	            // 시간 셀에 "09:00" 또는 "09:30" 같은 형식으로 출력
	            timeCell.textContent = (hour < 10 ? "0" + hour : hour) + ":" + minute;
	            row.appendChild(timeCell);
	            
	            tableBody.appendChild(row);
	
	            // 30분씩 증가
	            if (minute === "30") {
	                currentHour++;
	            }
	        }
	    }
	
    // 시간표 생성 함수 호출
    createTimetable();
});

// 모달 안에 시간표
document.addEventListener('DOMContentLoaded', function () {
    var modal = document.getElementById("searchModal");
    var btn = document.getElementById("searchModalBtn");
    var span = document.getElementsByClassName("close")[0];

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
});

// 이미 색상이 적용된 셀인지 확인하는 함수
function isCellColored(col, startTime, duration) {
    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

    const startIndex = times.indexOf(startTime);

    if (startIndex === -1) {
        console.error("Invalid startTime: " + startTime);
        return false;
    }

    const rows = document.querySelectorAll('#timetable tbody tr');

    // 색상이 이미 적용된 셀이 있는지 확인
    for (let i = 0; i < duration; i++) {
        const row = rows[startIndex + i];
        const targetCell = row.querySelectorAll('td')[col];
        
        // 셀이 색상이 있으면 true 반환
        if (targetCell.classList.contains('colored')) {
            return true;
        }
    }

    return false; // 색상이 없으면 false 반환
}

const subjectColors = {
	"프로그래밍기초": "#FFD8D8", "데이터통신": "#FAECC5", "모바일프로그래밍": "#E4F7BA", "데이터엔지니어링": "#D4F4FA", "컴퓨터전자과": "#DAD9FF",
	"스마트전자과": "#FFD9EC",
	"전자정보공학과": "#FAECC5",
	"AI전자과": "#E4F7BA",
	"ICT융합전자과": "#FFD8D8",
	"컴퓨터공학과": "#DAD9FF",
	"로봇전자과": "#FFD9EC",
	"임베디드전자과": "#FFD8D8",
	"스마트기기과": "#D4F4FA",
	"데이터전자과": "#FFD9EC",
	"유기화학": "#DAD9FF",
	"경영학원론": "#FAECC5",
	"경제학개론": "#E4F7BA",
	"마케팅원론": "#D4F4FA",
	"회계원리": "#DAD9FF",
	"재무관리": "#FFD9EC",
	"인사관리": "#DAD9FF",
	"경영전략": "#D4F4FA",
	"국제경영": "#E4F7BA",
	"경찰학개론": "#E4F7BA",
	"형법총론": "#FFD8D8",
	"형사소송법": "#FFD9EC",
	"범죄학": "#FAECC5",
	"경찰행정법": "#E4F7BA",
	"경찰윤리": "#FFD8D8",
	"경찰정보론": "#FFD9EC",
	"경찰조직관리": "#D4F4FA",
	"기초디자인": "#FFD9EC",
	"색채학": "#FAECC5",
	"디자인사": "#D4F4FA",
	"타이포그래피": "#FAECC5",
	"광고디자인": "#FAECC5",
	"패키지디자인": "#D4F4FA",
	"웹디자인" : "#FFD8D8",
	"UI/UX디자인": "#FAECC5",
	"일반화학": "#FFD9EC",
	"물리화학": "#DAD9FF",
	"화학공정": "#FAECC5",
	"화학반응공학": "#DAD9FF",
	"재료과한": "#FFD9EC",
	"환경화학": "#E4F7BA",
	"고분자화학": "#FFD9EC",
	"디지털논리회로": "#E4F7BA",
	"자료구조": "#D4F4FA",
	"컴퓨터구조": "#FFD9EC",
	"운영체제": "#E4F7BA",
	"마이크로프로세서": "#FFD8D8",
	"임베디드시스템": "#DAD9FF",
	"컴퓨터네트워크": "#D4F4FA",
	"정보통신개론": "#D4F4FA",
	"통신이론": "#E4F7BA",
	"데이터통신": "#FFD9EC",
	"네트워크보안": "#D4F4FA",
	"무선통신": "#FAECC5",
	"광통신": "#E4F7BA",
	"네트워크관리": "#FFD8D8",
	"정보보호": "#FFD9EC"
};     

// 마우스 오버 시 색을 미리보기로 적용하는 함수
function highlightPreviewTimeSlots(col, startTime, duration, subjectColor) {
    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

    const startIndex = times.indexOf(startTime);

    if (startIndex === -1) {
        console.error("Invalid startTime: " + startTime);
        return;
    }

    const rows = document.querySelectorAll('#timetable tbody tr'); 

    // 지속 시간에 맞게 여러 셀에 색을 미리보기
    for (let i = 0; i < duration; i++) {
        const row = rows[startIndex + i];
        const targetCell = row.querySelectorAll('td')[col];

        // 색을 미리보기 (배경색만 적용)
        targetCell.style.backgroundColor = subjectColor;
        targetCell.style.borderColor = subjectColor;
    }
}

// 마우스 아웃 시 색을 제거하는 함수
function removePreviewTimeSlots(col, startTime, duration) {
    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

    const startIndex = times.indexOf(startTime);

    if (startIndex === -1) {
        console.error("Invalid startTime: " + startTime);
        return;
    }

    const rows = document.querySelectorAll('#timetable tbody tr'); 

    // 색을 미리보기한 셀의 색상 삭제
    for (let i = 0; i < duration; i++) {
        const row = rows[startIndex + i];
        const targetCell = row.querySelectorAll('td')[col];

        targetCell.style.backgroundColor = '';
        targetCell.style.borderColor = '';
   	 	}
	}

// 수업 검색 버튼 클릭 시 모달 열기
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('searchModalBtn').addEventListener('click', function() {
        // 모달 열기
        const modal = document.getElementById('searchModal');
        modal.style.display = 'block'; // 모달 표시

        // 모달 창이 열린 후, .modal-line 요소들에 이벤트 리스너 추가
        modal.querySelectorAll('.modal-line').forEach(function(row) {
            // 클릭 시 색을 적용
            row.addEventListener('click', function() {
                let weeks = ['월', '화', '수', '목', '금'];
                const studyDay = row.dataset.day;  // 수업 요일
                const startTime = row.dataset.start;  // 수업 시작 시간 (예: '09:00' 또는 '09:30')
                const duration = 5;  // 2시간 수업이라 다섯칸 칠해져야함

                // 해당 요일에 맞는 컬럼 인덱스 찾기
                const col = weeks.indexOf(studyDay); // 월=0, 화=1, 수=2, 목=3, 금=4
                
                const subjectName = row.dataset.sbname;
                const subjectColor = subjectColors[subjectName] || "#A566FF";

                // 클릭된 셀에 대해서 색상과 텍스트 저장
                toggleCellColor(col, startTime, duration, subjectColor, subjectName, startTime); // 클릭된 영역에 색과 텍스트 적용
            });

            // 더블클릭 시 색상 삭제
            row.addEventListener('dblclick', function() {
                let weeks = ['월', '화', '수', '목', '금'];
                const studyDay = row.dataset.day;
                const startTime = row.dataset.start;
                const duration = 5;

                // 해당 요일에 맞는 컬럼 인덱스 찾기
                const col = weeks.indexOf(studyDay);
                
                // 색상 삭제
                removeTimeSlots(col, startTime, duration); // 기존 색상 삭제
            });

            // 마우스 오버 시 색을 미리보기 (하지만 클릭된 셀은 영향을 받지 않음)
            row.addEventListener('mouseover', function() {
                let weeks = ['월', '화', '수', '목', '금'];
                const studyDay = row.dataset.day;  
                const startTime = row.dataset.start;  
                const duration = 5;  

                // 해당 요일에 맞는 컬럼 인덱스 찾기
                const col = weeks.indexOf(studyDay); 
                
                const subjectName = row.dataset.sbname;
                const subjectColor = subjectColors[subjectName] || "#A566FF";

                // 색을 미리보기 (하지만 클릭된 셀은 영향을 받지 않도록)
                if (!isCellColored(col, startTime, duration)) {
                    highlightPreviewTimeSlots(col, startTime, duration, subjectColor);
                }
            });

            // 마우스 아웃 시 색상 제거 (마찬가지로 클릭된 셀은 영향을 받지 않음)
            row.addEventListener('mouseout', function() {
                let weeks = ['월', '화', '수', '목', '금'];
                const studyDay = row.dataset.day; 
                const startTime = row.dataset.start;  
                const duration = 5;  

                // 해당 요일에 맞는 컬럼 인덱스 찾기
                const col = weeks.indexOf(studyDay);

                // 색을 미리보기로 했던 색을 제거
                if (!isCellColored(col, startTime, duration)) {
                    removePreviewTimeSlots(col, startTime, duration);
                }
            });
        });
    });
});

// 클릭 시 색과 수업 정보를 적용하는 함수
function toggleCellColor(col, startTime, duration, subjectColor, subjectName, studyTime) {
    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

    const startIndex = times.indexOf(startTime);
    if (startIndex === -1) {
        console.error("Invalid startTime: " + startTime);
        return;
    }

    const rows = document.querySelectorAll('#timetable tbody tr');
    for (let i = 0; i < duration; i++) {
        const row = rows[startIndex + i];
        const targetCell = row.querySelectorAll('td')[col];

        // 이미 색상이 칠해지지 않은 셀에만 색을 칠함
        if (!targetCell.classList.contains('colored')) {
            targetCell.style.backgroundColor = subjectColor;
            targetCell.style.borderColor = subjectColor;
            targetCell.classList.add('colored'); // 색상 칠해진 상태 표시

            // 수업 이름과 시간을 표시할 텍스트 요소 생성 (세 번째 칸에만 표시)
            if (i === 2) {
                const subjectText = document.createElement('div');
                subjectText.classList.add('subject-text');
				
				// studyTime을 Date 객체로 변환
				 let [hours, minutes] = studyTime.split(':').map(num => parseInt(num));
				 let startDate = new Date(2024, 0, 1, hours, minutes); // 예시로 2024년 1월 1일로 설정

				 // 2시간을 더함
				 startDate.setHours(startDate.getHours() + 2);

				 // endTime 계산 (2시간 뒤)
				 let endTime = `${String(startDate.getHours()).padStart(2, '0')}:${String(startDate.getMinutes()).padStart(2, '0')}`;

                subjectText.innerHTML = `<strong>${subjectName}</strong><br>${studyTime} ~ ${endTime}`;
                
                // 텍스트 위치 조정 (셀 내에서 중앙에 위치하도록)
                targetCell.style.position = 'relative';
                subjectText.style.position = 'absolute';
                subjectText.style.top = '50%';
                subjectText.style.left = '50%';
                subjectText.style.transform = 'translate(-50%, -50%)';
                subjectText.style.fontSize = '16px';
                subjectText.style.textAlign = 'center';
                subjectText.style.color = '#747474';
                subjectText.style.whiteSpace = 'nowrap';

                // 텍스트를 셀에 추가
                targetCell.appendChild(subjectText);
            }
        }
    }
}

// 더블 클릭 시 색상 삭제하는 함수
function removeTimeSlots(col, startTime, duration) {
    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

    const startIndex = times.indexOf(startTime);

    if (startIndex === -1) {
        console.error("Invalid startTime: " + startTime);
        return;
    }

	// 확인을 위한 알림 창 띄우기
	if (confirm("추가된 수업을 삭제하시겠습니까?")) {
	    const rows = document.querySelectorAll('#timetable tbody tr'); 
	    for (let i = 0; i < duration; i++) {
	        const row = rows[startIndex + i];
	        const targetCell = row.querySelectorAll('td')[col];

        // 색상 삭제
        targetCell.style.backgroundColor = '';
        targetCell.style.borderColor = '';
        targetCell.classList.remove('colored'); // 색상 칠해진 상태 제거
        
        // 수업 이름과 시간 텍스트 삭제
        const subjectText = targetCell.querySelector('.subject-text');
        if (subjectText) {
            targetCell.removeChild(subjectText);
        	}
    	}
	}
}

	// 학년과 학기 선택 시 시간표 가져오기
	document.getElementById('semesterSelect').addEventListener('change', function() {
	    const semester = this.value;
		const cleanedSemester = semester.replaceAll('학기', '').replaceAll('학년', '').trim();
	    const [gradeYear, semesterCode] = cleanedSemester.split(" ");  // 학년, 학기 분리
		console.log(gradeYear, semesterCode);

	    $.ajax({
	        url: '/Semi1/LoadTimetableServlet',  // 시간표를 가져오는 서버 URL
	        type: 'POST',
	        data: {
	            gradeYear: gradeYear,  // 학년
	            semesterCode: semesterCode  // 학기
	        },
	        success: function(response) {
	            // 성공적으로 데이터를 받으면 시간표 그리기
	            loadTimetable(response.timetable);
	        },
	        error: function(xhr, status, error) {
	            console.error("시간표 데이터를 가져오는 데 실패했습니다:", status, error);
	        }
	    });
		
	});
	
	function loadTimetable(timetableData) {
		// timetableData가 undefined 또는 빈 배열인 경우 처리
		 if (!timetableData || timetableData.length === 0) {
		     // 확인창 띄우기
		     const userConfirmed = confirm("등록된 수업이 없습니다.");
		     if (userConfirmed) {
		         console.log("사용자가 확인 버튼을 클릭했습니다.");
		     } else {
		         console.log("사용자가 취소 버튼을 클릭했습니다.");
		     }
		     return; // 함수 종료
		 }
		
		console.log("받아온 시간표 데이터:", timetableData);
	    // 시간표 데이터 배열을 순회하면서 각 시간대와 요일에 맞게 과목을 추가
	    timetableData.forEach(item => {
	        const { dt_sub_num, grade_year, semester, mb_num, studytime, studyday, sb_name} = item;
			
	        // 요일 및 시간에 맞는 셀을 찾기
	        const studyDayIndex = getStudyDayIndex(studyday); // '월', '화', '수', '목', '금'에 맞는 인덱스를 찾는 함수
	        const studyTimeIndex = getStudyTimeIndex(studytime); // 시간대에 맞는 인덱스를 찾는 함수

			// subjectName에 맞는 색상 가져오기
			const subjectColor = subjectColors[sb_name] || "#A566FF"; // subjectColors 객체에서 색상 가져오기, 기본값 "#A566FF"
			// 해당 셀을 찾고 색을 적용하는 함수 호출
			toggleCellColor(studyDayIndex, studyTimeIndex, 1, subjectColor, sb_name, studytime);  // studytime: 수업 시간
			   });
			}
			
	// studyDay(요일)에 맞는 인덱스 값 반환
	function getStudyDayIndex(studyday) {
	    const days = ['월', '화', '수', '목', '금'];
	    return days.indexOf(studyday); // '월' = 0, '화' = 1, ... , '금' = 4
	}

	// studyTime에 맞는 인덱스 값 반환
	function getStudyTimeIndex(studytime) {
	    const times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];
	    return times.indexOf(studytime);  // 09:00 = 0, 09:30 = 1, ... , 19:00 = 19
	}
	
	// 색상과 텍스트를 적용하는 함수
	function toggleCellColor(col, studyTime, duration, subjectColor, subjectName, studyTime) {
	    let times = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00'];

	    const startIndex = times.indexOf(studyTime);
		
	    if (startIndex === -1) {
	        console.error("Invalid startTime: " + studyTime);
	        return;
	    }

		    const rows = document.querySelectorAll('#timetable tbody tr');
			duration = 5;
			
		    for (let i = 0; i < duration; i++) {
		        const row = rows[startIndex + i];
		        const targetCell = row.querySelectorAll('td')[col];

		        // 이미 색상이 칠해지지 않은 셀에만 색을 칠함
		        if (!targetCell.classList.contains('colored')) {
		            targetCell.style.backgroundColor = subjectColor;
		            targetCell.style.borderColor = subjectColor;
		            targetCell.classList.add('colored'); // 색상 칠해진 상태 표시

		            // 수업 이름과 시간을 표시할 텍스트 요소 생성 (세 번째 칸에만 표시)
		            if (i === 2) {
		                const subjectText = document.createElement('div');
		                subjectText.classList.add('subject-text');

		                // studyTime을 Date 객체로 변환
		                let [hours, minutes] = studyTime.split(':').map(num => parseInt(num));
		                let startDate = new Date(2024, 0, 1, hours, minutes); // 예시로 2024년 1월 1일로 설정

		                // 2시간을 더함
		                startDate.setHours(startDate.getHours() + 2);

		                // endTime 계산 (2시간 뒤)
		                let endTime = `${String(startDate.getHours()).padStart(2, '0')}:${String(startDate.getMinutes()).padStart(2, '0')}`;

						
		                subjectText.innerHTML = `<strong>${subjectName}</strong><br>${studyTime} ~ ${endTime}`;

		                // 텍스트 위치 조정 (셀 내에서 중앙에 위치하도록)
		                targetCell.style.position = 'relative';
		                subjectText.style.position = 'absolute';
		                subjectText.style.top = '50%';
		                subjectText.style.left = '50%';
		                subjectText.style.transform = 'translate(-50%, -50%)';
		                subjectText.style.fontSize = '16px';
		                subjectText.style.textAlign = 'center';
		                subjectText.style.color = '#747474';
		                subjectText.style.whiteSpace = 'nowrap';

		                // 텍스트를 셀에 추가
		                targetCell.appendChild(subjectText);
		            }
		        }
		    }
		}
		
	
