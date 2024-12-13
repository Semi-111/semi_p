<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="list-main">
	<div class="container">
		<div class="articles">
			<div class="articlename"> 채플 </div>
			<div class="professor"> 진형섭,김광연,한경미 </div>
			<div class="rate">
				<span class="star"></span>
			</div>
			<div class="info">
				<span class="nickName">닉네임</span>
			</div>
			<div class="text"> 평가 내용 </div>
		</div>
	</div>
</main>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />

</body>
</html>