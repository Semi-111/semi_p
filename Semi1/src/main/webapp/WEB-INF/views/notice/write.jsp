<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<jsp:include page="/WEB-INF/views/layout/header.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/notice/write.css">
<meta charset="UTF-8">
<title>${mode=="update"?"공지사항 수정":"공지사항 작성"}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        const f = document.noticeForm;
	
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

        let mode = '${mode}';
        if (mode === 'update') {
            f.action = "${pageContext.request.contextPath}/noticeBoard/update";
        } else {
            f.action = "${pageContext.request.contextPath}/noticeBoard/writeSubmit";
        }

        // 넘어가는 데이터 category / title/ content
        f.submit();
    }
</script>
</head>
<body>
    <div class="container">
        <h1 class="board-title">${mode=="update"?"공지사항 수정":"공지사항 작성"}</h1>

        <div class="write-form">
            <form name="noticeForm" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="category">카테고리</label>
                    <select id="category" name="category" required>
                        <option value="">경영</option>
                        <option value="">경찰행정</option>
                        <option value="">디자인</option>
                        <option value="">화학</option>
                        <option value="">컴퓨터응용전자</option>
                        <option value="">정보통신</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="notice">공지 구분</label>
                    <div class="notice-type">
                        <input type="checkbox" id="important" name="notice" value="1" ${dto.notice==1?"checked":""}>
                        <label for="important">중요 공지로 등록</label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" id="title" name="title" required value="${dto.title}">
                </div>

                <div class="form-group">
                    <label for="file">첨부 파일</label>
                    <div class="file-upload">
                        <label for="file">파일 선택</label>
                        <input type="file" id="file" name="selectFile" onchange="previewImage(this);">
                        <span id="fileName" class="file-name">
                            ${empty dto.fileName ? "선택된 파일 없음" : dto.fileName}
                        </span>
                    </div>
                    <c:if test="${not empty dto.fileName}">
                        <img id="preview" class="preview-image"
                            src="#/uploads/notice/${dto.fileName}"
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
                    <a href="#/notice/list" class="cancel-button">취소</a>
                </div>

                <c:if test="${mode=='update'}">
                    <input type="hidden" name="noticeNum" value="${dto.noticeNum}">
                    <input type="hidden" name="page" value="${page}">
                </c:if>
            </form>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>