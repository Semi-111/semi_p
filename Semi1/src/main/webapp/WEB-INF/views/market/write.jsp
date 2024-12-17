<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/market/write.css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
<meta charset="UTF-8">
<title>${mode=="update"?"게시글 수정":"게시글 작성"}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
</style>
<script>
	function previewImage(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				var preview = document.getElementById('preview');
				preview.src = e.target.result;
				preview.style.display = 'block';

				var fileName = document.getElementById('fileName');
				fileName.textContent = input.files[0].name;
			}
			reader.readAsDataURL(input.files[0]);
		}
	}

	function submitForm() {
		const f = document.marketForm;

		if (!f.category.value) {
			alert("카테고리를 선택하세요.");
			f.category.focus();
			return;
		}

		if (!f.title.value.trim()) {
			alert("제목을 입력하세요.");
			f.title.focus();
			return;
		}

		if (!f.content.value.trim()) {
			alert("내용을 입력하세요.");
			f.content.focus();
			return;
		}

		// mode가 update면 수정, 아니면 write로 설정
		let mode = '${mode}';
		if (mode === 'update') {
			f.action = "${pageContext.request.contextPath}/market/update";
		} else {
			f.action = "${pageContext.request.contextPath}/market/write";
		}

		f.submit();
	}
</script>
</head>
<body>
	<div class="container">
		<h1 class="board-title">${mode=="update"?"게시글 수정":"게시글 작성"}</h1>

		<div class="write-form">
			<form name="marketForm" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="category">카테고리</label> <select id="category"
						name="category" required>
						<option value="">카테고리 선택</option>
						<option value="1" ${dto.ct_num==1?"selected":""}>삽니다</option>
						<option value="2" ${dto.ct_num==2?"selected":""}>팝니다</option>
						<option value="3" ${dto.ct_num==3?"selected":""}>룸</option>
						<option value="4" ${dto.ct_num==4?"selected":""}>책</option>
						<option value="5" ${dto.ct_num==5?"selected":""}>옷</option>
						<option value="6" ${dto.ct_num==6?"selected":""}>기타</option>
					</select>
				</div>

				<div class="form-group">
					<label for="title">제목</label> <input type="text" id="title"
						name="title" required value="${dto.title}">
				</div>

				<div class="form-group">
					<label for="image">상품 이미지</label>
					<div class="file-upload">
						<label for="image">파일 선택</label> <input type="file" id="image"
							name="selectFile" accept="image/*" onchange="previewImage(this);">
						<span id="fileName" class="file-name"> ${empty dto.fileName ? "선택된 파일 없음" : dto.fileName}
						</span>
					</div>
					<c:if test="${not empty dto.fileName}">
						<img id="preview" class="preview-image"
							src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}"
							style="display: block;">
					</c:if>
					<c:if test="${empty dto.fileName}">
						<img id="preview" class="preview-image" src="#"
							style="display: none;">
					</c:if>
				</div>

				<div class="form-group">
					<label for="content">내용</label>
					<textarea id="content" name="content" required>${dto.content}</textarea>
				</div>

				<div class="button-group">
					<button type="button" class="submit-button" onclick="submitForm();">
						${mode=="update"?"수정완료":"등록하기"}</button>
					<a href="${pageContext.request.contextPath}/market/list"
						class="cancel-button">취소</a>
				</div>

				<c:if test="${mode=='update'}">
					<input type="hidden" name="marketNum" value="${dto.marketNum}">
					<input type="hidden" name="page" value="${page}">
				</c:if>
			</form>
		</div>
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
</body>
</html>