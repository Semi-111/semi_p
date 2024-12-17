<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TRAINEE - LOGIN</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<style type="text/css">
.container {
	max-width: 800px;
	display: flex;
	justify-content: center; /* 수평 가운데 */
	/* align-items: center; //수직 가운데 */
	margin-top: 100px;
	margin-bottom: 100px;
}

.border {
	border-radius: 20px !important;
	border: 2px solid #CE93E8 !important;
}

.logo-title>img {
	width: 300px;
	margin-left: 12px;
}

#loginBtn {
	color: #FFFFFF;
}

.bi {
	color: #FFFFFF;
}
</style>

<script type="text/javascript">
	function sendLogin() {
		const f = document.loginForm;
		let str;

		str = f.userId.value;
		if (!str) {
			f.userId.focus();
			return;
		}

		str = f.pwd.value;
		if (!str) {
			f.pwd.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/member/login";
		f.submit();
	}
</script>
</head>
<body>
	<main>
		<div class="container">
			<div class="row">
				<div class="col-md-6 offset-md-3">
					<div class="border mt-5 p-4">
						<form name="loginForm" action="" method="post" class="row g-3">
							<div class="logo-title">
								<img
									src="${pageContext.request.contextPath}/resources/images/logo.png">
							</div>
							<div class="col-12">
								<label class="mb-1">아이디</label> <input type="text" name="userId"
									class="form-control" placeholder="ID">
							</div>
							<div class="col-12">
								<label class="mb-1">패스워드</label> <input type="password"
									name="pwd" class="form-control" autocomplete="off"
									placeholder="PW">
							</div>
							<div class="col-12">
								<div class="form-check">
									<input class="form-check-input" type="checkbox" id="rememberMe">
									<label class="form-check-label" for="rememberMe"> 아이디
										저장</label>
								</div>
							</div>
							<div class="col-12">
								<button type="button" class="btn float-end" id="loginBtn"
									onclick="sendLogin();" style="background-color: #CE93E8;">
									&nbsp;Login&nbsp;<i class="bi bi-check2"></i>
								</button>
							</div>
						</form>
						<hr class="mt-4">
						<div class="col-12">
							<p class="text-center mb-0">
								<a href="#" class="text-decoration-none me-2">아이디 찾기</a> <a
									href="#" class="text-decoration-none me-2">패스워드 찾기</a> <a
									href="${pageContext.request.contextPath}/member/member"
									class="text-decoration-none">회원가입</a>
							</p>
						</div>
					</div>

					<div class="d-grid">
						<p class="form-control-plaintext text-center text-primary">${message}</p>
					</div>

				</div>
			</div>

		</div>
	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

</body>
</html>