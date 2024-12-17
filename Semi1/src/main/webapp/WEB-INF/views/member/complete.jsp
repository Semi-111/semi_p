<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TARINEE - COMPLETE</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.body-container {
	max-width: 800px;
	margin: 30px auto;
	padding: 20px;
}

.custom-btn {
	display: block;
	width: 100%;
	height: 40px;
	background-color: #F6EFFA;
	color: #743394;
	font-size: 18px;
	border: 1px solid #8235B6;
	border-radius: 8px;
	cursor: pointer;
	transition: all 0.3s ease;
	background-color: #F6EFFA;
	margin-top: 2px;
}

.custom-btn:hover {
	background-color: #BB8BD2;
	color: #fff;
	border: none;
	transform: translateY(-2px);
	box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.body-container h4 {
	font-size: 24px;
	color: #4C1069;
	font-weight: bold;
	text-align: center;
}

.body-container p {
	font-size: 16px;
	color: #1A1A1A;
	text-align: center;
	margin: 10px 0;
}
</style>

</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<main>
		<div class="container">
			<div class="body-container">

				<div class="row justify-content-md-center mt-5">
					<div class="col-md-8">
						<div class="border rounded mt-5">
							<div class="border-bottom p-3">
								<h4 class="text-center fw-bold mb-0">${title}</h4>
							</div>
							<div class="pt-4 pb-4">
								<p class="text-center fs-6 mb-0">${message}</p>
							</div>
						</div>
					</div>
				</div>

				<div class="row justify-content-md-center mt-1">
					<div class="col-md-8 d-grid">
						<button type="button" class="btn btn-lg custom-btn"
							onclick="location.href='${pageContext.request.contextPath}/';">
							메인화면으로 이동 <i class="bi bi-arrow-counterclockwise"></i>
						</button>
					</div>
				</div>

			</div>
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

</body>
</html>