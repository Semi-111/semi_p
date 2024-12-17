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
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">


</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<main class="write-main">

	<div class="container">
		<h1 class="board-title">강의평가</h1>
		
		<div class="write-form">
			<form name="reviewForm" method="post">
				<div class="form-group">
					<label for="title">과목명</label>
					<div class="sbTitle">${sbName}</div>
				</div>
				
				<div class="form-group">
		    		<div class="star-rate">
					    <i class="fa-solid fa-star star" data-value="1"></i>
				        <i class="fa-solid fa-star star" data-value="2"></i>
				        <i class="fa-solid fa-star star" data-value="3"></i>
				        <i class="fa-solid fa-star star" data-value="4"></i>
				        <i class="fa-solid fa-star star" data-value="5"></i>
				        <span class="rate-value"> 0 / 5 </span>				        
			    	</div>				    					
				</div>
				
				<div class="form-group">
					<label for="content">강의평</label>
					<textarea id="content" name="content" required placeholder="이 강의에 대한 총평을 작성해주세요.">${dto.content}</textarea>
				</div>
				
				<div class="button-group">
	            	<button type="button" class="submit-button" onclick="submitForm();">
                       ${mode=='update'?'수정완료':'등록하기'}
                   </button>
                   <button type="button" class="cancel-button" onclick="location.href='${pageContext.request.contextPath}/lectureReview/list'">
                   		취소
                   	</button>
                   	<input type="hidden" name="rating" id="ratingInput" value="0">
			    	<input type="hidden" name="atNum" value="${atNum}">
			    	<input type="hidden" name="reviewnum" value="${dto.review_Num}">                					
                   	<input type="hidden" name="page" value="${page}">
               </div>
               
			</form>
		</div>
	</div>

</main>

<script type="text/javascript">

function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

let rateValue = 0; // 초기 별점 값 (0)

$(function() {
	$('.star').click(function() {
		rateValue = $(this).attr('data-value');	
		
		$('.rate-value').text(rateValue + " / 5 ");
		
		// 모든 별의 active 클래스 초기화
        $('.star').removeClass('active');
		
		// 클릭된 별까지 active 클래스 추가
        for (let i = 1; i <= rateValue; i++) {
            $('.star[data-value="' + i + '"]').addClass('active');
        }
		
     	// hidden 필드 값 업데이트
        $('#ratingInput').val(rateValue);
	});
});



$(function() {
	// mode가 update일 때만 실행
	let mode = '${mode}';
	let rating = '${rating}';
	
	if(!rating) {
		rating = 0;
	}
	
	if(mode === 'update') {
		$('.star').each(function() {
			let starValue = $(this).data('value'); // data-value 값 가져오기
			
			if(starValue <= rating) {
				// rating 이하의 별에 활성화 클래스 추가
				$(this).addClass('active');
			} else {
				$(this).removeClass('active');
			}
		});	
	}
	
	// rate-value 영역에 현재 별점 표시
    $(".rate-value").text(rating + " / 5");		
});

function submitForm() {
	if(rateValue === 0) {
		// 별점을 선택하지 않았을 때 경고 메시지
        alert('별점을 선택해주세요.');
        return;
	}
	
	const f = document.reviewForm;
	let str;
	
	str = f.content.value.trim();
	if(!str) {
		alert('내용을 입력하세요. ');
        f.content.focus();
        return;
	}
	
	f.action = '${pageContext.request.contextPath}/lectureReview/${mode}';
	f.submit();
}

</script>


<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />

</body>
</html>

