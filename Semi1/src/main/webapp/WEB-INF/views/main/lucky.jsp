<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>LUCKY</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
img {
	width: 500px;
}
</style>
<script type="text/javascript">
	// 페이지 새로고침 시 랜덤 이미지가 계속 나오도록 설정
	function reloadPage() {
		window.location.reload(); // 페이지 새로고침
	}

	// 메인 페이지로 돌아가기
	function goToMain() {
		window.location.href = '/main'; // 메인 페이지로 이동
	}
</script>
</head>
<body>
	<h1>오늘의 운세는?</h1>
	<!-- 랜덤으로 선택된 이미지를 보여줌 -->
	<img src="${selectedImage}" alt="운세 이미지"
		style="width: 100%; max-width: 600px;">

	<br>
	<br>

	<!-- 버튼들 -->
	<button onclick="reloadPage()">운세 새로고침</button>
	<button onclick="goToMain()">나가기</button>

	<div>
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test1.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test2.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test3.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test4.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test5.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test6.jpg">
		<img
			src="${pageContext.request.contextPath}/resources/images/lucky/test7.jpg">
	</div>
</body>
</html>