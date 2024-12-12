<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
   <meta charset="UTF-8">
   <title>${mode=="update"?"게시글 수정":"게시글 작성"}</title>
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
       .form-group select,
       .form-group textarea {
           width: 100%;
           padding: 10px;
           border: 1px solid #ddd;
           border-radius: 4px;
           font-size: 14px;
           background-color: #fff;
       }

       .form-group input[type="text"]:focus,
       .form-group select:focus,
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
           if(mode === 'update') {
               f.action = "${pageContext.request.contextPath}/market/update";
           } else {
               f.action = "${pageContext.request.contextPath}/market/write";
           }
           
           f.submit();
       }
   </script>
</head>
<body>
   <header>
       <nav>
           <div class="logo">
               <img src="${pageContext.request.contextPath}/resources/images/trainee.png" alt="트레이니 로고">
           </div>
           <div class="nav-links">
               <a href="#" class="active">게시판</a>
               <a href="#">시간표</a>
               <a href="#">학점계산기</a>
               <a href="#">친구</a>
               <a href="#">공지</a>
               <a href="#">마이페이지</a>
               <a href="#">맛집</a>
           </div>
       </nav>
   </header>

   <div class="container">
       <h1 class="board-title">${mode=="update"?"게시글 수정":"게시글 작성"}</h1>
       
       <div class="write-form">
           <form name="marketForm" method="post" enctype="multipart/form-data">
               <div class="form-group">
                   <label for="category">카테고리</label>
                   <select id="category" name="category" required>
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
                   <label for="title">제목</label>
                   <input type="text" id="title" name="title" required value="${dto.title}">
               </div>

               <div class="form-group">
                   <label for="image">상품 이미지</label>
                   <div class="file-upload">
                       <label for="image">파일 선택</label>
                       <input type="file" id="image" name="selectFile" accept="image/*" 
                              onchange="previewImage(this);">
                       <span id="fileName" class="file-name">
                           ${empty dto.fileName ? "선택된 파일 없음" : dto.fileName}
                       </span>
                   </div>
                   <c:if test="${not empty dto.fileName}">
                       <img id="preview" class="preview-image" 
                            src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" 
                            style="display: block;">
                   </c:if>
                   <c:if test="${empty dto.fileName}">
                       <img id="preview" class="preview-image" src="#" style="display: none;">
                   </c:if>
               </div>

               <div class="form-group">
                   <label for="content">내용</label>
                   <textarea id="content" name="content" required>${dto.content}</textarea>
               </div>

               <div class="button-group">
                   <button type="button" class="submit-button" onclick="submitForm();">
                       ${mode=="update"?"수정완료":"등록하기"}
                   </button>
                   <a href="${pageContext.request.contextPath}/market/list" class="cancel-button">취소</a>
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
               <a href="#">이용약관</a>
               <a href="#">개인정보처리방침</a>
               <a href="#">청소년보호정책</a>
               <a href="#">커뮤니티이용규칙</a>
               <a href="#">공지사항</a>
               <a href="#">문의하기</a>
               <a href="#">@TRAINNF</a>
           </div>
       </div>
   </footer>
</body>
</html>