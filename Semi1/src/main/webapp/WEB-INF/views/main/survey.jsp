<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>survey</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style>
@font-face {
	font-family: 'YClover-Bold';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_231029@1.1/YClover-Bold.woff2')
		format('woff2');
	font-weight: 700;
	font-style: normal;
}

@font-face {
	font-family: 'omyu_pretty';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2304-01@1.0/omyu_pretty.woff2')
		format('woff2');
	font-weight: normal;
	font-style: normal;
}

body {
	font-family: 'omyu_pretty', sans-serif;
	margin: 20px;
	padding: 0;
}

h1 {
	text-align: center;
	color: #f7f7f7;
	font-size: 36px;
	font-family: 'YClover-Bold', sans-serif;
}

.container {
	width: 100%;
	height: 100%;
	text-align: center;
	position: relative;
	z-index: 1;
}

.container::after {
	width: 100%;
	height: 100%;
	content: "";
	background:
		url("https://github.com/Kwonhyunseung/project/blob/main/%EB%B0%A4%ED%95%98%EB%8A%98.jpg?raw=true")
		no-repeat center center fixed;
	background-size: cover;
	position: fixed;
	top: 0;
	left: 0;
	z-index: -1;
	opacity: 3;
}

.question-container {
	display: flex;
	justify-content: space-around;
	margin-bottom: 15px;
}

.question {
	background-color: rgba(255, 255, 255, 0.697);
	border: 2px solid #ffffff;
	border-radius: 10px;
	padding: 20px;
	text-align: left;
	width: 45%;
}

.question p {
	font-weight: bold;
	font-size: 21px;
	color: #0e0e0e;
}

.choice label {
	font-size: 20px;
}

.choice {
	margin-bottom: 10px;
}

input[type="radio"] {
	margin-right: 5px;
}

#resultBtn {
	display: block;
	margin: 0 auto;
	padding: 10px 20px;
	background-color: #c0c0c0ec;
	color: rgb(30, 30, 30);
	border: none;
	border-radius: 5px;
	cursor: pointer;
	font-family: 'YClover-Bold', sans-serif;
	font-size: 15px;
}

#result {
	display: none;
	text-align: center;
	margin-top: 20px;
	font-size: 25px;
}

/* 토스트 팝업 스타일 */
.toast {
	visibility: hidden;
	position: fixed;
	top: 30px;
	left: 50%;
	transform: translateX(-50%);
	background-color: #424242ec;
	color: #f9f9f9;
	text-align: center;
	border-radius: 5px;
	padding: 16px;
	z-index: 1;
	font-size: 18px;
}

/* 토스트 팝업 나타남 효과 */
.toast.show {
	visibility: visible;
	animation: fadeIn 0.5s, fadeOut 0.5s 2.5s; /* 2.5초 후에 자동으로 사라짐 */
}
/* 나타남 애니메이션 */
@
keyframes fadeIn {
	from {top: -60px;
	opacity: 0;
}

to {
	top: 30px;
	opacity: 1;
}

}
/* 사라짐 애니메이션 */
@
keyframes fadeOut {
	from {top: 30px;
	opacity: 1;
}

to {
	top: -60px;
	opacity: 0;
}

}
#main-home-button {
	font-family: 'YClover-Bold', sans-serif;
	background-color: #ffffffef; /* 배경색 변경 */
	color: #000000; /* 텍스트 색상 변경 */
	border: 2px solid #4f4f4fef; /* 테두리 스타일 변경 */
	border-radius: 100px; /* 테두리 둥글게 변경 */
	padding: 10px 20px; /* 안쪽 여백 조정 */
	font-size: 16px; /* 폰트 크기 변경 */
	cursor: pointer; /* 커서를 포인터로 변경하여 클릭 가능하도록 함 */
	transition: background-color 0.3s, color 0.3s, border-color 0.3s;
	/* 호버 시 부드러운 전환 효과 추가 */
}

#main-home-button:hover {
	background-color: #d4d4d4; /* 호버 시 배경색 변경 */
	border-color: #ffbebe; /* 호버 시 테두리 색상 변경 */
}
</style>
</head>
<body>
	<div class="container">
		<br>
		<h1>나의 여행 캐릭터 고르기</h1>
		<br> <br>
		<form name="surveyForm" id="survey-form">
			<div class="question-container">
				<div class="question">
					<p>1. 나는 여행지를 선택할 때 주로?</p>
					<div class="choice">
						<input type="radio" name="q1" value="E"> <label for="a">a.
							사람과 볼거리가 많은 유명한 관광지로!</label><br> <input type="radio" name="q1"
							value="I"> <label for="b">b. 공기 좋고 조용한 푸르른 자연
							속으로!</label>
					</div>
				</div>

				<div class="question">
					<p>2. 식당에 도착한 당신! 주문할 음식은?</p>
					<div class="choice">
						<input type="radio" name="q2" value="P"> <label for="a">a.
							음식 비주얼만 보고 선택하기</label><br> <input type="radio" name="q2" value="J">
						<label for="b">b. 실패는 없다! 리뷰 보고 시키기</label>
					</div>
				</div>
			</div>

			<div class="question-container">
				<div class="question">
					<p>3. 유명한 맛집에 왔다. 새로운 메뉴가 있네?</p>
					<div class="choice">
						<input type="radio" name="q3" value="P"> <label for="a">a.
							새로운 거다! 이것도 시키자</label><br> <input type="radio" name="q3" value="J">
						<label for="b">b. 오! 새로운 메뉴네~ 근데 얼마야?</label>
					</div>
				</div>

				<div class="question">
					<p>4. 여행 일정이 추가되어 하루 더 머물게 되었다. 내일 일정을 정한다면?</p>
					<div class="choice">
						<input type="radio" name="q4" value="J"> <label for="a">a.
							아침에 브런치 카페에 갔다가 체크아웃 후 점심에...</label><br> <input type="radio"
							name="q4" value="P"> <label for="b">b. 맛집 갔다 재밌는
							거 하고 집</label>
					</div>
				</div>
			</div>

			<div class="question-container">
				<div class="question">
					<p>5. 여행지에 도착을 했다! 친구가 일정에 없는 장소에 잠깐 구경가자는데..</p>
					<div class="choice">
						<input type="radio" name="q5" value="J"> <label for="a">a.
							오! 에쁘다. 구경 가보자~</label><br> <input type="radio" name="q5" value="P">
						<label for="b">b. 다음 일정에 지정이 생기니 계획대로 움직이자!</label>
					</div>
				</div>

				<div class="question">
					<p>6. 비행시간이 긴 당신! 시간을 보내는 방법은?</p>
					<div class="choice">
						<input type="radio" name="q6" value="I"> <label for="a">a.
							밀린 드라마나 예능을 보며 시간 보내기</label><br> <input type="radio" name="q6"
							value="E"> <label for="b">b. 사진을 찍거나 수다를 떨며 시간
							보내기</label>
					</div>
				</div>
			</div>

			<div class="question-container">
				<div class="question">
					<p>7. 숙소를 선택할 때 나의 우선순위는?</p>
					<div class="choice">
						<input type="radio" name="q7" value="S"> <label for="a">a.
							가까운게 최고지! 교통편이 좋은 숙소</label><br> <input type="radio" name="q7"
							value="N"> <label for="b">b. 숙소뷰 깡패! 감성 추구 숙소</label>
					</div>
				</div>

				<div class="question">
					<p>8. 여행 중 가장 소중한 순간은?</p>
					<div class="choice">
						<input type="radio" name="q8" value="N"> <label for="a">a.
							일출 감상! 새로운 날을 기대하며</label><br> <input type="radio" name="q8"
							value="S"> <label for="b">b. 일몰 감상! 하루를 마무리하며</label>
					</div>
				</div>
			</div>

			<button type="button" id="resultBtn">결과 보기</button>
		</form>

		<!-- 결과 표시 -->
		<div id="result">
			<h3>나의 여행 캐릭터는?</h3>
			<p id="result-text"></p>
			<img src="" alt="result-image" id="result-image"
				style="max-width: 300px;">
		</div>
	</div>

	<div id="toast" class="toast"></div>

	<script>
		document
				.getElementById('survey-form')
				.addEventListener(
						'submit',
						function(e) {
							e.preventDefault(); // 기본 제출 동작 방지

							// 선택되지 않은 항목 확인
							const uncheckedQuestions = [];
							const questions = [ 'q1', 'q2', 'q3', 'q4', 'q5',
									'q6', 'q7', 'q8' ];
							questions.forEach(function(question) {
								if (!document.querySelector('input[name="'
										+ question + '"]:checked')) {
									uncheckedQuestions.push(question);
								}
							});

							if (uncheckedQuestions.length > 0) {
								// 선택되지 않은 항목이 있는 경우 토스트 팝업으로 안내
								const toast = document.getElementById('toast');
								toast.textContent = '선택되지 않은 항목이 있습니다. 모든 항목을 선택해주세요.';
								toast.classList.add('show');

								// 2.5초 후에 토스트 팝업 숨기기
								setTimeout(function() {
									toast.classList.remove('show');
								}, 2500);

								return; // 선택되지 않은 항목이 있으면 함수 종료
							}

							// 선택된 답변 가져오기
							const responses = {
								'1' : document
										.querySelector('input[name="q1"]:checked').value,
								'2' : document
										.querySelector('input[name="q2"]:checked').value,
								'3' : document
										.querySelector('input[name="q3"]:checked').value,
								'4' : document
										.querySelector('input[name="q4"]:checked').value,
								'5' : document
										.querySelector('input[name="q5"]:checked').value,
								'6' : document
										.querySelector('input[name="q6"]:checked').value,
								'7' : document
										.querySelector('input[name="q7"]:checked').value,
								'8' : document
										.querySelector('input[name="q8"]:checked').value
							};

							const result = calculateResult(responses);
							const resultText = document
									.getElementById('result-text');
							const resultImage = document
									.getElementById('result-image');

							// 결과 텍스트와 이미지 경로 설정
							switch (result) {
							case 'A':
								resultText.textContent = '여행 캐릭터 LIAM 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/art.jpg?raw=true';
								break;
							case 'B':
								resultText.textContent = '여행 캐릭터 JIN 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/history.jpg?raw=true';
								break;
							case 'C':
								resultText.textContent = '여행 캐릭터 SEUNG 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/extreme.jpg?raw=true';
								break;
							case 'D':
								resultText.textContent = '여행 캐릭터 SOPHIA 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/festival.jpg?raw=true';
								break;
							case 'E':
								resultText.textContent = '여행 캐릭터 WONDER 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/eco.jpg?raw=true';
								break;
							case 'F':
								resultText.textContent = '여행 캐릭터 LEXI 입니다.';
								resultText.style.color = '#f7f7f7';
								resultImage.src = 'https://github.com/Kwonhyunseung/project/blob/main/relax.jpg?raw=true';
								break;
							default:
								resultText.textContent = '...';
								resultImage.src = '';
							}

							// 결과 표시 영역 표시
							const resultDiv = document.getElementById('result');
							resultDiv.style.display = 'block';

							// 메인 홈으로 이동 버튼 생성
							if (!document.getElementById('main-home-button')) {
								const mainHomeButton = document
										.createElement('button');
								mainHomeButton.id = 'main-home-button';
								mainHomeButton.textContent = '메인 홈 이동';
								mainHomeButton.style.display = 'block';
								mainHomeButton.style.margin = '20px auto'; // 상단과 하단에 20px 마진, 좌우는 자동으로 중앙 배치
								mainHomeButton
										.addEventListener(
												'click',
												function() {
													window.location.href = 'index.html';
												});
								resultDiv.appendChild(mainHomeButton);
							}
						});

		function calculateResult(responses) {
			const characterScores = {
				'A' : 0,
				'B' : 0,
				'C' : 0,
				'D' : 0,
				'E' : 0,
				'F' : 0
			};

			// 각 질문에 대한 응답에 따라 특정 캐릭터에 점수를 부여
			if (responses['1'] === 'E' || responses['6'] === 'E') {
				characterScores['B']++;
				characterScores['C']++;
				characterScores['D']++;
			} else if (responses['1'] === 'I' || responses['6'] === 'I') {
				characterScores['A']++;
				characterScores['E']++;
				characterScores['F']++;
			}

			const keysToCheck = [ '2', '3', '4', '5' ];
			const randomKey = keysToCheck[Math.floor(Math.random()
					* keysToCheck.length)];

			if (responses[keysToCheck[0]] === 'P'
					|| responses[keysToCheck[1]] === 'P'
					|| responses[keysToCheck[2]] === 'P'
					|| responses[keysToCheck[3]] === 'P') {
				// 'P'를 선택한 경우
				const randomIndex = Math.floor(Math.random() * 3); // 0부터 2까지의 랜덤 인덱스 생성
				const characters = [ 'C', 'D', 'F' ];
				const selectedCharacter = characters[randomIndex];
				characterScores[selectedCharacter]++;
			} else if (responses[keysToCheck[0]] === 'J'
					|| responses[keysToCheck[1]] === 'J'
					|| responses[keysToCheck[2]] === 'J'
					|| responses[keysToCheck[3]] === 'J') {
				// 'J'를 선택한 경우
				const randomIndex = Math.floor(Math.random() * 3); // 0부터 2까지의 랜덤 인덱스 생성
				const characters = [ 'A', 'B', 'E' ];
				const selectedCharacter = characters[randomIndex];
				characterScores[selectedCharacter]++;
			}

			if (responses['7'] === 'N' || responses['8'] === 'N') {
				characterScores['A']++;
				characterScores['E']++;
				characterScores['F']++;
			} else if (responses['7'] === 'S' || responses['8'] === 'S') {
				characterScores['B']++;
				characterScores['C']++;
				characterScores['D']++;
			}

			// 가장 높은 점수를 가진 캐릭터를 반환합니다.
			let maxScore = 0;
			let resultCharacter = 'A'; // 기본값으로 A를 설정합니다.
			for ( const character in characterScores) {
				if (characterScores[character] > maxScore) {
					maxScore = characterScores[character];
					resultCharacter = character;
				}
			}

			return resultCharacter;
		}
	</script>
</body>
</html>