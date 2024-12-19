<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전화번호 변경</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script type="text/javascript">
function sendOk() {
	const f = document.changeTelForm;

	let str = f.tel.value;
	if(!str){
		alert("현재 전화번호를 입력하세요.");
		f.tel.focus();
		return;
	}
	
	str = f.confirmTel.value;
	if(!str) {
		alert("새로 입력하신 전화번호가 일치하지 않습니다.");
		f.confirmTel.focus();
		return;
	}

	f.action = "${pageContext.request.contextPath}/member/changeTel";
	f.submit();
}
</script>

</head>
<body>
	<h3>전화번호 변경</h3>

    <form name="changeTelForm" action="" method="post">
        <label for="tel">현재 전화번호:</label>
        <input type="text" id="tel" name="tel" required><br><br>

        <label for="newTel">새 전화번호:</label>
        <input type="text" id="newTel" name="newTel" required><br><br>

        <label for="confirmTel">새 전화번호 확인:</label>
        <input type="text" id="confirmTel" name="confirmTel" required><br><br>

        <button type="button" onclick="sendOk();">전화번호 변경</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/member/myPage'">취소</button>
	</form>

</body>
</html>