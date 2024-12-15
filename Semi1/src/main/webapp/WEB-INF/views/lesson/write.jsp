<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${mode=='update' ? '글수정' : '글쓰기'}</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕",
		sans-serif;
}

body {
	background-color: #f5f5f5;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

/* 헤더 스타일 */
header {
	background: white;
	border-bottom: 1px solid #e1e1e1;
	margin-bottom: 30px;
}

nav {
	max-width: 1200px;
	margin: 0 auto;
	padding: 8px 20px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.logo img {
	height: 45px;
	width: auto;
	padding: 5px 0;
}

.nav-links {
	display: flex;
	gap: 2rem;
}

.nav-links a {
	text-decoration: none;
	color: #666;
	font-size: 14px;
}

.nav-links a:hover {
	color: #a855f7;
}

.nav-links a.active {
	color: #a855f7;
	font-weight: bold;
}

/* 글쓰기 폼 스타일 */
.write-container {
	max-width: 800px;
	margin: 0 auto;
	background: white;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	padding: 30px;
}

.write-title {
	margin-bottom: 20px;
	padding-bottom: 20px;
	border-bottom: 1px solid #e5e7eb;
}

.write-title h3 {
	font-size: 24px;
	color: #333;
}

.write-form {
	width: 100%;
	border-collapse: collapse;
}

.write-form tr {
	border-bottom: 1px solid #e5e7eb;
}

.write-form td {
	padding: 15px 10px;
}

.write-form td:first-child {
	width: 100px;
	color: #666;
	font-weight: 500;
}

.form-select {
	width: 100%;
	padding: 8px 12px;
	border: 1px solid #d1d5db;
	border-radius: 4px;
	font-size: 14px;
}

.form-input {
	width: 100%;
	padding: 8px 12px;
	border: 1px solid #d1d5db;
	border-radius: 4px;
	font-size: 14px;
}

.form-textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #d1d5db;
	border-radius: 4px;
	min-height: 300px;
	resize: vertical;
	font-size: 14px;
	line-height: 1.6;
}

.form-file {
	padding: 8px 0;
}

.img-box {
	margin-top: 10px;
}

.img-box img {
	max-width: 200px;
	border-radius: 4px;
}

.button-container {
	display: flex;
	justify-content: center;
	gap: 10px;
	margin-top: 30px;
	padding-top: 20px;
	border-top: 1px solid #e5e7eb;
}

.btn {
	padding: 8px 20px;
	border-radius: 4px;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
	border: 1px solid transparent;
}

.btn-primary {
	background-color: #a855f7;
	color: white;
}

.btn-primary:hover {
	background-color: #9333ea;
}

.btn-light {
	background-color: #f3f4f6;
	color: #666;
	border-color: #d1d5db;
}

.btn-light:hover {
	background-color: #e5e7eb;
}

/* 푸터 스타일 */
footer {
	background-color: #f9fafb;
	padding: 2rem 1rem;
	margin-top: 40px;
	border-top: 1px solid #eee;
}

.footer-content {
	max-width: 1200px;
	margin: 0 auto;
	font-size: 12px;
	color: #666;
}

.footer-links {
	margin-top: 1rem;
	display: flex;
	gap: 1rem;
}

.footer-links a {
	text-decoration: none;
	color: #666;
}

.footer-links a:hover {
	color: #a855f7;
}
</style>

<script type="text/javascript">
	function check() {
		const f = document.boardForm;
		const userLessonNum = $ { userLessonNum }
		; // 컨트롤러에서 전달받은 사용자의 학과 번호
		const selectedCategory = parseInt(f.category.value);

		if (!f.category.value) {
			alert("카테고리를 선택하세요.");
			f.category.focus();
			return false;
		}

		// 선택한 카테고리가 자신의 학과가 아닐 경우
		if (selectedCategory !== userLessonNum) {
			alert("본인 학과의 게시판에만 글을 작성할 수 있습니다.");
			f.category.focus();
			return false;
		}

		if (!f.title.value.trim()) {
			alert("제목을 입력하세요.");
			f.title.focus();
			return false;
		}

		if (!f.content.value.trim()) {
			alert("내용을 입력하세요.");
			f.content.focus();
			return false;
		}

		f.action = "${pageContext.request.contextPath}/lessonBoard/${mode=='update'?'update':'writeForm'}";
		return true;
	}
</script>
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

	<div class="container">
		<div class="write-container">
			<div class="write-title">
				<h3>${mode=='update' ? '글수정' : '새 글 쓰기'}</h3>
			</div>

			<form name="boardForm" method="post" enctype="multipart/form-data"
				onsubmit="return check();">
				<table class="write-form">
					<tr>
						<td>학과</td>
						<td><select name="category" class="form-select">
								<option value="">:: 학과 선택 ::</option>
								<option value="51" ${userLessonNum==51 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==51 ? "selected":""}>경영학과</option>
								<option value="52" ${userLessonNum==52 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==52 ? "selected":""}>경찰행정과</option>
								<option value="53" ${userLessonNum==53 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==53 ? "selected":""}>디자인학과</option>
								<option value="54" ${userLessonNum==54 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==54 ? "selected":""}>화학공학과</option>
								<option value="55" ${userLessonNum==55 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==55 ? "selected":""}>컴퓨터응용전자과</option>
								<option value="56" ${userLessonNum==56 ? "" : "disabled"}
									${mode=="update" && dto.lessonNum==56 ? "selected":""}>정보통신학부</option>
						</select></td>
					</tr>
					<tr>
						<td>제목</td>
						<td><input type="text" name="title" maxlength="100"
							class="form-input" value="${mode=='update' ? dto.title : ''}"
							placeholder="제목을 입력하세요"></td>
					</tr>
					<tr>
						<td>작성자</td>
						<td>
							<p>${sessionScope.member.name}</p>
						</td>
					</tr>
					<tr>
						<td>내용</td>
						<td><textarea name="content" class="form-textarea" rows="12">${mode=='update' ? dto.board_content : ''}</textarea>
						</td>
					</tr>
					<tr>
						<td>첨부</td>
						<td><input type="file" name="selectFile" accept="image/*"
							class="form-file"></td>
					</tr>
					<c:if test="${mode=='update'}">
						<tr>
							<td>첨부된 파일</td>
							<td><c:if test="${not empty dto.fileName}">
									<div class="img-box">
										<img
											src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}"
											alt="첨부된 이미지">
									</div>
								</c:if></td>
						</tr>
					</c:if>
				</table>

				<div class="button-container">
					<button type="submit" class="btn btn-primary">
						${mode=='update' ? '수정완료' : '등록하기'}</button>

					<button type="button" class="btn btn-light"
						onclick="location.href='${pageContext.request.contextPath}/lessonBoard/list';">
						${mode=='update' ? '수정취소' : '등록취소'}</button>
					<c:if test="${mode=='update'}">
						<input type="hidden" name="cm_num" value="${dto.cm_num}">
						<input type="hidden" name="page" value="${page}">
					</c:if>
				</div>
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