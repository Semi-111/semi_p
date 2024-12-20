<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴 완료</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script>
    function showModal() {
        const modal = document.getElementById("deleteModal");
        modal.style.display = "block";

        setTimeout(() => {
            location.href = "${pageContext.request.contextPath}/";
        }, 1500);
    }

    window.onload = showModal;
</script>
<style>
#deleteModal {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	display: none;
	z-index: 9999;
}

.modal-content {
	background: white;
	padding: 20px;
	margin: 15% auto;
	width: 300px;
	text-align: center;
	border-radius: 10px;
}

.modal-content p {
	font-size: 18px;
	font-weight: bold;
}

.modal-content button {
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}
</style>

</head>
<body>
	<div id="deleteModal">
		<div class="modal-content">
			<p>회원탈퇴가 완료되었습니다.</p>
			<p>잠시 후 메인 페이지로 이동합니다...</p>
			<button
				onclick="location.href='${pageContext.request.contextPath}/';">메인
				페이지로 이동</button>
		</div>
	</div>
</body>
</html>