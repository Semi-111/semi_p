<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시글 작성</title>
  <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", sans-serif;
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
      height: 100px;
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

    .board-title {
      font-size: 24px;
      margin-bottom: 20px;
      color: #333;
    }

    /* 글쓰기 폼 스타일 */
    .write-form {
      background: white;
      border-radius: 8px;
      box-shadow: 0 1px 3px rgba(0,0,0,0.1);
      padding: 30px;
      max-width: 800px;
      margin: 0 auto;
    }

    .form-group {
      margin-bottom: 20px;
    }

    .form-group label {
      display: block;
      margin-bottom: 8px;
      font-weight: bold;
      color: #333;
      font-size: 14px;
    }

    .form-group input[type="text"],
    .form-group textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 14px;
      background-color: #fff;
    }

    .form-group input[type="text"]:focus,
    .form-group textarea:focus {
      outline: none;
      border-color: #a855f7;
    }

    .form-group textarea {
      height: 300px;
      resize: vertical;
      line-height: 1.6;
    }

    /* 파일 업로드 스타일 */
    .file-upload {
      margin-bottom: 20px;
    }

    .file-upload label {
      display: inline-block;
      padding: 10px 20px;
      background-color: #f0f0f0;
      border-radius: 4px;
      cursor: pointer;
      margin-right: 10px;
    }

    .file-upload input[type="file"] {
      display: none;
    }

    .file-name {
      display: inline-block;
      font-size: 14px;
      color: #666;
    }

    .preview-image {
      max-width: 200px;
      margin-top: 10px;
      display: none;
    }

    .button-group {
      display: flex;
      gap: 10px;
      justify-content: center;
      margin-top: 30px;
    }

    .submit-button {
      background-color: #a855f7;
      color: white;
      padding: 12px 30px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
    }

    .cancel-button {
      background-color: #666;
      color: white;
      padding: 12px 30px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      text-decoration: none;
      font-size: 14px;
      font-weight: bold;
    }

    .submit-button:hover,
    .cancel-button:hover {
      opacity: 0.9;
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
  </script>
</head>
<body>
<header>
  <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="container">
<%--  <h1 class="board-title">게시글 작성</h1>--%>

  <div class="write-form">
    <form name="writeForm" method="post" action="${pageContext.request.contextPath}/bbs/infoBoard/write" enctype="multipart/form-data">
      <!-- hidden 필드로 boardType 전달 -->
      <c:set var="boardType" value="${param.type}" />
      <input type="hidden" name="boardType" value="${boardType}">

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" required>
      </div>

      <div class="form-group">
        <label for="image">이미지</label>
        <div class="file-upload">
          <label for="image">파일 선택</label>
          <input type="file" id="image" name="file" accept="image/*" onchange="previewImage(this);">
          <span id="fileName" class="file-name">선택된 파일 없음</span>
        </div>
        <img id="preview" class="preview-image" src="#" alt="이미지 미리보기">
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" required></textarea>
      </div>

      <div class="button-group">
        <button type="submit" class="submit-button">등록</button>
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
