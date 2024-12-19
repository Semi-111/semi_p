<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script type="text/javascript">
function sendOk() {
	const f = document.myPageForm;

	let str = f.pwd.value;
	if(!str) {
		alert("패스워드를 입력하세요. ");
		f.pwd.focus();
		return;
	}

	f.action = "${pageContext.request.contextPath}/member/changePwd";
	f.submit();
}
</script>

</head>
<body>
	<h3>비밀번호 변경</h3>
	
	<!-- 에러 메시지 표시 -->
    <c:if test="${not empty message}">
        <div style="color: red;">${message}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/member/changePwd" method="post">
        <label for="currentPwd">현재 비밀번호:</label>
        <input type="password" id="pwd" name="pwd" required><br><br>

        <label for="newPwd">새 비밀번호:</label>
        <input type="password" id="newPwd" name="newPwd" required><br><br>

        <label for="confirmPwd">새 비밀번호 확인:</label>
        <input type="password" id="confirmPwd" name="confirmPwd" required><br><br>

        <button type="button" onclick="sendOk();">비밀번호 변경</button>
    </form>

    <button onclick="location.href='${pageContext.request.contextPath}/member/myPage'">취소</button>
</body>
</html>