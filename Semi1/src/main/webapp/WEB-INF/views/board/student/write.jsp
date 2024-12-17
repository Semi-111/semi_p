<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title><c:choose>
    <c:when test="${mode == 'write'}">게시글 작성</c:when>
    <c:otherwise>게시글 수정</c:otherwise>
  </c:choose></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/write.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <script src="${pageContext.request.contextPath}/resources/js/board/list.js"></script>
  <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
<header>
  <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="container">
  <div class="write-form">


    <form action="${pageContext.request.contextPath}/bbs/studentBoard/${mode}" method="post" enctype="multipart/form-data">
      <input type="hidden" name="cmNum" value="${dto.cmNum}" />
      <input type="hidden" name="type" value="${type}" />
      <input type="hidden" name="page" value="${page}" />

<c:choose>
  <c:when test="${boardType == 'student'}">
        <div class="form-group">
          <label for="category">학번:</label>
          <select name="category" id="category" required class="form-control">
          <option value="">학번을 선택하세요</option>
          <!-- 학번 카테고리 추가 -->
          <option value="7" <c:if test="${7 == dto.categoryNum}">selected</c:if>>25학번</option>
          <option value="8" <c:if test="${8 == dto.categoryNum}">selected</c:if>>24학번</option>
          <option value="9" <c:if test="${9 == dto.categoryNum}">selected</c:if>>23학번</option>
          <option value="10" <c:if test="${10 == dto.categoryNum}">selected</c:if>>22학번</option>
          </select>
        </div>
  </c:when>
</c:choose>

      <div class="form-group">
        <label for="title">제목:</label>
        <input type="text" name="title" id="title" value="${dto.title}" required class="form-control" />
      </div>
      <div class="form-group">
        <label for="content">내용:</label>
        <textarea name="content" id="content" rows="10" required class="form-control">${dto.content}</textarea>
      </div>
      <div class="form-group">
        <label for="file">첨부 파일:</label>
        <input type="file" name="file" id="file" class="form-control" />
        <c:if test="${dto.fileName != null && !empty dto.fileName}">
          <p>현재 파일: <a href="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}">${dto.fileName}</a></p>
        </c:if>
      </div>
      <div class="button-group">
        <button type="submit" class="btn submit-button">
          <c:choose>
            <c:when test="${mode == 'write'}">작성</c:when>
            <c:otherwise>수정</c:otherwise>
          </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=${type}&page=${page}" class="btn cancel-button">취소</a>
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
