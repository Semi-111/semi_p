<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TRAINEE</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.container {
	max-width: 300px;
	width: 100%;
	padding: 20px;
}
</style>
<script type="text/javascript">
	function sendOk() {
		const f = document.deleteForm;

		const pwd = f.pwd.value;
		const confirmPwd = f.confirmPwd.value;

		if (!pwd || !confirmPwd) {
			alert("비밀번호를 입력해주세요.");
			return;
		}

		if (pwd !== confirmPwd) {
			alert("비밀번호가 일치하지 않습니다.");
			return;
		}

		f.action = "${pageContext.request.contextPath}/member/delete";
		f.submit();
	}
</script>
</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<div class="container">

		<div class="d-flex justify-content-center align-items-center"
			style="min-height: 100vh;">
			<div class="row justify-content-center">
				<div class="col-md-7">
					<div class="border p-4">
						<form name="deleteForm" action="" method="post" class="row g-3">
							<h3 class="text-center fw-bold">회원 탈퇴</h3>
							<div class="d-grid">
								<p class="form-control-plaintext text-center">회원을 탈퇴하시려면
									비밀번호를 두 번 입력해주세요.</p>
							</div>
							<div class="d-grid">
								<input type="password" name="pwd"
									class="form-control form-control-lg" autocomplete="off"
									placeholder="비밀번호">
							</div>
							<div class="d-grid">
								<input type="password" name="confirmPwd"
									class="form-control form-control-lg" autocomplete="off"
									placeholder="비밀번호 확인">
							</div>
							<div class="d-grid">
								<button type="button" class="btn btn-lg btn-danger"
									onclick="sendOk();">
									확인 <i class="bi bi-check2"></i>
								</button>
								<input type="hidden" name="mode" value="delete">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</div>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

</body>
</html>