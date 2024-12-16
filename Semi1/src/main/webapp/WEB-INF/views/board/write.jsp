<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/write.css">

  <title>게시글 작성</title>
  <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

  <script>
    document.addEventListener("DOMContentLoaded", function () {
      window.previewImage = function (input) {
        var preview = document.getElementById('preview');
        var fileName = document.getElementById('fileName');

        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            if (preview) {
              preview.src = e.target.result;
              preview.style.display = 'block';
            }
            if (fileName) {
              fileName.textContent = input.files[0].name;
            }
          };
          reader.readAsDataURL(input.files[0]);
        } else {
          if (preview) {
            preview.style.display = 'none';
            preview.src = '';
          }
          if (fileName) {
            fileName.textContent = '선택된 파일 없음';
          }
        }
      };
    });


    function updateBoard() {
      const f = document.writeForm;
      let str;

      str = f.title.value.trim();
      if(!str) {
        alert('제목을 입력하세요.');
        f.title.focus();
        return false;
      }

      str = f.content.value.trim();
      if(!str) {
        alert('내용을 입력하세요.');
        f.content.focus();
        return false;
      }

      // mode가 update일 때만 action 재설정
      <c:if test="${mode == 'update'}">
      f.action = '${pageContext.request.contextPath}/bbs/infoBoard/update?type=${boardType}&cmNum=${dto.cmNum}&page=${page}';
      </c:if>

      f.submit();
    }
  </script>
</head>
<body>
<header>
  <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="container">
  <c:set var="boardType" value="${param.type}" />

  <div class="write-form">
    <form name="writeForm" method="post"
          action="${pageContext.request.contextPath}/bbs/infoBoard/${mode == 'update' ? 'update' : 'write'}"
          enctype="multipart/form-data">

      <input type="hidden" name="type" value="${boardType}">

      <c:if test="${mode == 'update'}">
        <input type="hidden" name="cmNum" value="${dto.cmNum}">
        <input type="hidden" name="page" value="${page}">
      </c:if>

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" value="${dto.title}" required>
      </div>

      <div class="form-group">
        <label for="image">이미지</label>
        <div class="file-upload">
          <label for="image">파일 선택</label>
          <input type="file" id="image" name="file" accept="image/*" onchange="previewImage(this);">
          <span id="fileName" class="file-name">선택된 파일 없음</span>
        </div>
        <c:if test="${dto.fileName != null}">
<%--          <img id="preview" class="preview-image" src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="이미지 미리보기" style="display:block;">--%>
          <img id="preview" class="preview-image" src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="이미지 미리보기">
        </c:if>
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" required>${dto.content}</textarea>
      </div>

      <div class="button-group">
        <c:choose>
          <c:when test="${mode == 'update'}">
            <button type="button" class="submit-button" onclick="updateBoard();">수정완료</button>
          </c:when>
          <c:otherwise>
            <button type="submit" class="submit-button">등록하기</button>
          </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=${boardType}" class="cancel-button">취소</a>
      </div>
    </form>
  </div>
</div>

<footer>
  <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
  <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</footer>

</body>
</html>
