	

document.addEventListener('DOMContentLoaded', function () {
    var modal = document.getElementById("searchModal");
    var btn = document.getElementById("searchModalBtn");
    var span = document.getElementsByClassName("close")[0];
    var searchBtnContainer = document.querySelector('.search-btn-container'); // 버튼 컨테이너

    // 수업 검색 버튼 클릭 시 모달 열기
    btn.onclick = function() {
        modal.classList.add('show'); // show 클래스를 추가하여 모달을 표시
        searchBtnContainer.classList.add('modal-open'); // 버튼도 모달과 함께 이동
    }

    // 모달 닫기 버튼 클릭 시 모달 닫기
    span.onclick = function() {
        modal.classList.remove('show'); // show 클래스를 제거하여 모달 숨기기
        searchBtnContainer.classList.remove('modal-open'); // 버튼 원위치
    }

    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.classList.remove('show'); // 모달 숨기기
            searchBtnContainer.classList.remove('modal-open'); // 버튼 원위치
        }
    }
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
	
	document.addEventListener('DOMContentLoaded', function () {
	    // 시간표 생성 관련 변수
	    const timetableList = document.querySelector('.timetable-list');
	    const addTimetableBtn = document.querySelector('.add-timetable');
	    
	    let timetableCount = 2; // 기존 시간표 개수 (시간표 1, 2)
	    
	    // 시간표 목록에 새 시간표 추가하는 함수
	    function addNewTimetable() {
	        timetableCount++; // 시간표 개수 증가
	        const newTimetableBtn = document.createElement('button');
	        newTimetableBtn.textContent = `시간표 ${timetableCount}`;
	        timetableList.appendChild(newTimetableBtn);
	    }

	    // 새 시간표 만들기 버튼 클릭 시 시간표 추가
	    addTimetableBtn.addEventListener('click', addNewTimetable);
	});

    // 시간표 생성 함수 호출
    createTimetable();
});

document.addEventListener('DOMContentLoaded', function () {
    const timetableBtnContainer = document.querySelector('#timetableBtnContainer');
    const addTimetableBtn = document.querySelector('.add-timetable-btn');
    
    // 로컬 스토리지에서 기존 시간표 리스트 가져오기
    let timetableBtnsData = JSON.parse(localStorage.getItem('timetableBtns')) || [];
    
    // 시간표 버튼 클릭 시 해당 시간표를 보여주는 함수
    function showTimetable(event) {
        const timetableName = event.target.textContent.replace(' ×', '');  // ' ×' 제거
        alert(`${timetableName}을(를) 보여줍니다.`);  // 실제로는 시간표 내용을 보여주는 코드로 변경 필요
    }

    // 시간표 버튼을 로컬 스토리지에 저장하고, 화면에 표시
    function renderTimetableButtons() {
        // 기존 버튼들을 모두 삭제하고 새로 렌더링
        const buttons = timetableBtnContainer.querySelectorAll('.timetable-btn');
        buttons.forEach(button => button.remove());

        // 로컬 스토리지에 저장된 시간표 버튼을 추가
        timetableBtnsData.forEach((timetableName, index) => {
            const newTimetableBtn = document.createElement('button');
            newTimetableBtn.textContent = timetableName;
            newTimetableBtn.classList.add('timetable-btn');
            
            // 엑스 버튼 추가
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = '×';
            deleteBtn.classList.add('delete-btn');
            deleteBtn.addEventListener('click', function(event) {
                deleteTimetable(index);
                event.stopPropagation();  // 엑스 버튼 클릭 시 버튼 클릭 이벤트 전파 막기
            });

            newTimetableBtn.appendChild(deleteBtn);
            newTimetableBtn.addEventListener('click', showTimetable);
            timetableBtnContainer.insertBefore(newTimetableBtn, addTimetableBtn); // "새 시간표 만들기" 버튼 앞에 추가
        });
    }

    // 시간표 삭제 함수
    function deleteTimetable(index) {
        timetableBtnsData.splice(index, 1);  // 해당 시간표를 배열에서 삭제
        localStorage.setItem('timetableBtns', JSON.stringify(timetableBtnsData));  // 로컬 스토리지에 저장
        renderTimetableButtons();  // 삭제 후 버튼 다시 렌더링
    }

    // 새 시간표 만들기 클릭 시 새로운 시간표 버튼 추가
    function addNewTimetable() {
        const newTimetableName = `시간표 ${timetableBtnsData.length + 1}`;
        timetableBtnsData.push(newTimetableName); // 새로운 시간표 이름을 리스트에 추가
        localStorage.setItem('timetableBtns', JSON.stringify(timetableBtnsData)); // 로컬 스토리지에 저장

        renderTimetableButtons(); // 버튼을 새로 렌더링
    }

    // 새 시간표 만들기 버튼 클릭 이벤트
    addTimetableBtn.addEventListener('click', addNewTimetable);

    // 페이지 로드 시 기존 시간표 버튼들을 렌더링
    renderTimetableButtons();
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