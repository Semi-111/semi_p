<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${mode=='update' ? '글수정' : '글쓰기'}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lesson/write.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
<script type="text/javascript">
function check() {
    const f = document.boardForm;
    const userLessonNum = ${userLessonNum};
    const userRole = ${sessionScope.member.role};
    const selectedCategory = parseInt(f.category.value);

    if (!f.category.value) {
        alert("카테고리를 선택하세요.");
        f.category.focus();
        return false;
    }

    // 관리자가 아닌 경우에만 학과 체크
    if (userRole != 99 && selectedCategory !== userLessonNum) {
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
						<td><c:if test="${mode=='update'}">
								<input type="hidden" name="category" value="${dto.lessonNum}">
					            ${dto.lessonName} <!-- 수정 모드일 때는 학과명만 보여줌 -->
							</c:if> <c:if test="${mode!='update'}">
								<select name="category" class="form-select">
									<option value="51"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==51 ? "" : "disabled")}
										${dto.lessonNum==51 ? "selected":""}>경영학과</option>
									<option value="52"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==52 ? "" : "disabled")}
										${dto.lessonNum==52 ? "selected":""}>경찰행정과</option>
									<option value="53"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==53 ? "" : "disabled")}
										${dto.lessonNum==53 ? "selected":""}>디자인학과</option>
									<option value="54"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==54 ? "" : "disabled")}
										${dto.lessonNum==54 ? "selected":""}>화학공학과</option>
									<option value="55"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==55 ? "" : "disabled")}
										${dto.lessonNum==55 ? "selected":""}>컴퓨터응용전자과</option>
									<option value="56"
										${sessionScope.member.role == 60 ? "" : (userLessonNum==56 ? "" : "disabled")}
										${dto.lessonNum==56 ? "selected":""}>정보통신학부</option>
								</select>
							</c:if></td>
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
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>