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

<script>
    function reloadPage() {
        location.reload(); // 페이지 새로고침
    }

    function goToMain() {
        window.location.href = "${pageContext.request.contextPath}/main"; // 메인 페이지로 이동
    }
</script>

</head>
<body>
    <h1>오늘의 운세는?</h1>
    
    <!-- 랜덤 이미지 출력 -->
    <img src="${luckyImage}" alt="Lucky Image" class="lucky-image">

    <!-- 버튼들 -->
    <button onclick="reloadPage()">운세 새로고침</button>
    <button onclick="goToMain()">나가기</button>
</body>
</html>