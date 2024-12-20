<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이메일 변경</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/changeThing.css">
<script type="text/javascript">
	function sendOk() {
		const f = document.changeEmailForm;
		

		let str = f.email.value;
		if (!str) {
			alert("현재 이메일을 입력하세요.");
			f.email.focus();
			return;
		}

		str = f.confirmEmail.value;
		if (!str) {
			alert("새로 입력하신 이메일이 일치하지 않습니다. ");
			f.confirmEmail.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/member/changeEmail";
		f.submit();
	}
</script>
</head>
<body>

	<h3>이메일 변경</h3>

	<form name="changeEmailForm" action="" method="post">
		<label for="email">현재 이메일 </label> <input type="text" id="email"
			name="email" required><br> <br> <label
			for="newEmail">새 이메일 </label> <input type="text" id="newEmail"
			name="newEmail" required><br> <br> <label
			for="confirmEmail">새 이메일 확인 </label> <input type="text"
			id="confirmEmail" name="confirmEmail" required><br> <br>

		<button type="button" onclick="sendOk();">이메일 변경</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/member/myPage'">취소</button>
	</form>

</body>
</html>