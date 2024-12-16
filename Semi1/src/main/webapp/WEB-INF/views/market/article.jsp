<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>2024학년도 1학기 수강신청 안내 - Trainee</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/market/article.css">
</head>
<head>
<title>게시글 상세 - Trainee</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
	<header>
		<nav>
			<div class="logo">
				<img
					src="${pageContext.request.contextPath}/resources/images/logo.png"
					alt="트레이니 로고">
			</div>
			<div class="nav-links">
				<a href="#" class="active">게시판</a> <a href="#">시간표</a> <a href="#">학점계산기</a>
				<a href="#">친구</a> <a href="#">공지</a> <a href="#">마이페이지</a> <a
					href="#">맛집</a>
			</div>
		</nav>
	</header>

	<div class="post-container">
		<!-- query는 내가 이전에 어떤 화면이었는지 확인하기 위함. 검색을 했는지 안 했는지. -->
		<a href="${pageContext.request.contextPath}/market/list?${query}"
			class="back-link">← 목록으로</a>

		<div class="post-detail">
			<div class="post-header">
				<h1 class="post-title">${dto.title}</h1>
				<div class="post-meta">
					<div class="post-info">
						<span>작성자: ${dto.nickName}</span>
						<hr>
						<span>작성일: ${dto.ca_date}</span>
					</div>
					<div class="post-info">
						<span> | 조회 ${dto.views}</span>
					</div>
				</div>
			</div>
			<div class="post-content">
				<p>${dto.content}</p>
				<c:if test="${not empty dto.fileName}">
					<div class="img-box">
						<img
							src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}"
							class="img-fluid">
					</div>
				</c:if>
			</div>
			<div class="post-actions">
				<div class="action-left">
					<button type="button" class="btn btn-gray" onclick="toggleLike();" title="관심">
						<i class="bi ${isLiked ? 'bi-heart-fill' : 'bi-heart'}"
							id="heartIcon"></i> 관심 <span id="countLikes">${countLikes}</span>
					</button>
					<button class="btn btn-gray">공유하기</button>
				</div>
				<div class="action-right">
					<c:if
						test="${sessionScope.member.mb_Num==dto.mb_num || sessionScope.member.role >= 51}">
						<button class="btn btn-purple"
							onclick="location.href='${pageContext.request.contextPath}/market/update?marketNum=${dto.marketNum}&page=${page}'">
							수정</button>
						<button class="btn btn-red" onclick="deletePost()">삭제</button>
					</c:if>
					<button class="btn btn-red">신고</button>
				</div>
			</div>
		</div>

		<!-- 이전글/다음글 -->
		<div class="post-navigation"
			style="margin-top: 20px; background: white; border-radius: 8px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); padding: 15px;">
			<c:if test="${not empty prevDto}">
				<div style="padding: 8px 0;">
					<span style="color: #666;">이전글:</span> <a
						href="${pageContext.request.contextPath}/market/article?${query}&marketNum=${prevDto.marketNum}"
						style="text-decoration: none; color: #333;">${prevDto.title}</a>
				</div>
			</c:if>
			<c:if test="${not empty nextDto}">
				<div style="padding: 8px 0;">
					<span style="color: #666;">다음글:</span> <a
						href="${pageContext.request.contextPath}/market/article?${query}&marketNum=${nextDto.marketNum}"
						style="text-decoration: none; color: #333;">${nextDto.title}</a>
				</div>
			</c:if>
		</div>

		<!-- 댓글 섹션은 나중에 구현 -->
	</div>

	<footer>
		<div class="footer-content">
			<p>씨유트레이니 주식회사</p>
			<p>서울특별시 광도달북로21 동성빌딩 2층 | 사업자등록번호 : 123456789</p>
			<div class="footer-links">
				<a href="#">이용약관</a> <a href="#">개인정보처리방침</a> <a href="#">청소년보호정책</a>
				<a href="#">커뮤니티이용규칙</a> <a href="#">공지사항</a> <a href="#">문의하기</a> <a
					href="#">@TRAINNF</a>
			</div>
		</div>
	</footer>

<script>
		function deletePost() {
			if (confirm('게시글을 삭제하시겠습니까?\n삭제한 게시글은 복구할 수 없습니다.')) {
				let url = '${pageContext.request.contextPath}/market/delete';
				url += '?marketNum=${dto.marketNum}&page=${page}';
				location.href = url;
			}
		}
</script>
	
<script type="text/javascript">
function ajaxFun(url, method, query, dataType, fn) {
	$.ajax({
		type: method,
		url: url,
		data: query,
		dataType: dataType,
		success: function(data) {
			fn(data);
		},
		beforeSend: function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error: function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 400) {
				alert("요청 처리가 실패했습니다.");
				return false;
			}
			console.log(jqXHR.responseText);
		}
	});
}
</script>	
	
<script type="text/javascript">
function toggleLike() {
    const $h = $(location).attr("href");
    const marketNum = "${dto.marketNum}";
    
    let url = "${pageContext.request.contextPath}/market/like";
    let query = "marketNum=" + marketNum;
    
    const fn = function(data) {
        let state = data.state;
        if(state === "loginFail") {
            alert("로그인 후 이용가능합니다.");
            return;
        }
        
        let $heartIcon = $("#heartIcon");
        if($heartIcon.hasClass("bi-heart")) {
            $heartIcon.removeClass("bi-heart").addClass("bi-heart-fill");
        } else {
            $heartIcon.removeClass("bi-heart-fill").addClass("bi-heart");
        }
        
        $("#countLikes").text(data.countLikes);
    };
    
    ajaxFun(url, "post", query, "json", fn);
}
</script>
</body>
</html>