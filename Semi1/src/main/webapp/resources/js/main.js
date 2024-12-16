/*
// 페이지가 메인 페이지인지 확인하고, 오른쪽 화면 숨기기
if (window.location.pathname === '/Semi1' || window.location.pathname === '/') {
	// 메인 페이지에서 오른쪽 화면 숨기기
	document.getElementById('rightScreen').classList.add('d-none');
} else {
	// 메인 페이지가 아닐 경우 오른쪽 화면을 보이게 설정
	document.getElementById('rightScreen').classList.remove('d-none');
}*/

$(document).ready(function() {
	// 슬라이더 초기화
	$('.slider').slick({
		dots: true,
		infinite: true,
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
		autoplay: true,
		autoplaySpeed: 3000
	});

	// 로그인 상태에 따라 요소 표시/숨김
	const isLoggedIn = false; // 로그인 상태 확인 (임시값)

	if (isLoggedIn) {
		$("#realTime ul").html('<li>인기글 1</li><li>인기글 2</li><li>인기글 3</li><li>인기글 4</li>');
		$("#weekly ul").html('<li>인기글 1</li><li>인기글 2</li><li>인기글 3</li><li>인기글 4</li>');
		$("#recentLectures ul").html('<li>별점: ★★★★☆ 강의명: 강의1 교수: 교수1 내용: 내용1</li><li>별점: ★★★☆☆ 강의명: 강의2 교수: 교수2 내용: 내용2</li><li>별점: ★★★★☆ 강의명: 강의3 교수: 교수3 내용: 내용3</li>');
	}
});