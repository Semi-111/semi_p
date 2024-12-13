<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lectureReview/write.css" type="text/css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />


<main class="write-main">

	<div class="container">
		<h1 class="board-title">강의평가</h1>
		
		<div class="write-form">
			<form name="review-form">
				<div class="form-group">
					<label for="title">과목명</label>
					<div>프로그래밍언어</div>
				</div>
				
				<div class="form-group">
		    		<div class="star-rating">
				        <i class="fa-regular fa-star rate" data-value="1"></i>
				        <i class="fa-regular fa-star rate" data-value="2"></i>
				        <i class="fa-regular fa-star rate" data-value="3"></i>
				        <i class="fa-regular fa-star rate" data-value="4"></i>
				        <i class="fa-regular fa-star rate" data-value="5"></i>
				        <span class="rating-text">0 / 5</span>
			    	</div>
				</div>
				
				<div class="form-group">
					<label for="content">강의평</label>
					<textarea id="content" name="content" required>${dto.content}</textarea>
				</div>
				
				<div class="button-group">
	                   <button type="button" class="submit-button" onclick="submitForm();">
	                       등록하기
	                   </button>
	                   <a href="${pageContext.request.contextPath}/lectureReview/list" class="cancel-button">취소</a>
	               </div>
			
			</form>
		</div>
	</div>

</main>

</body>
</html>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />

</body>
</html>