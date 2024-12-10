<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>Trainee</title>
   <style>
   
  	 /* 모달 스타일 추가 */
       .modal-backdrop {
           display: none;
           position: fixed;
           top: 0;
           left: 0;
           width: 100%;
           height: 100%;
           background-color: rgba(0, 0, 0, 0.5);
           z-index: 1000;
       }
       
       .modal {
           display: none;
           position: fixed;
           top: 50%;
           left: 50%;
           transform: translate(-50%, -50%);
           background-color: white;
           padding: 20px;
           border-radius: 8px;
           box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
           z-index: 1001;
           width: 100%;
           max-width: 600px;
       }

       .modal-header {
           display: flex;
           justify-content: space-between;
           align-items: center;
           margin-bottom: 20px;
           padding-bottom: 10px;
           border-bottom: 1px solid #e1e1e1;
       }

       .modal-title {
           font-size: 18px;
           font-weight: bold;
           color: #333;
       }

       .modal-close {
           background: none;
           border: none;
           font-size: 20px;
           cursor: pointer;
           color: #666;
       }

       .modal-body {
           margin-bottom: 20px;
       }

       .form-group {
           margin-bottom: 15px;
       }

       .form-group label {
           display: block;
           margin-bottom: 5px;
           font-size: 14px;
           color: #666;
       }

       .form-control {
           width: 100%;
           padding: 8px 12px;
           border: 1px solid #e1e1e1;
           border-radius: 4px;
           font-size: 14px;
       }

       textarea.form-control {
           min-height: 200px;
           resize: vertical;
       }

       .modal-footer {
           display: flex;
           justify-content: flex-end;
           gap: 10px;
           padding-top: 15px;
           border-top: 1px solid #e1e1e1;
       }

       .btn {
           padding: 8px 16px;
           border-radius: 4px;
           font-size: 14px;
           cursor: pointer;
           border: none;
       }

       .btn-cancel {
           background-color: #e1e1e1;
           color: #666;
       }

       .btn-submit {
           background-color: #a855f7;
           color: white;
       }

       .btn-submit:hover {
           background-color: #9333ea;
       }

       .btn-cancel:hover {
           background-color: #d1d1d1;
       }
   
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

       /* 상단 컨트롤 영역 */
       .board-controls {
           display: flex;
           justify-content: space-between;
           align-items: center;
           margin: 20px 0;
           gap: 20px;
       }

       /* 검색 영역 */
       .search-box {
           display: flex;
           gap: 10px;
           flex-grow: 1;
           max-width: 600px;
       }

       .search-box select {
           padding: 8px;
           border: 1px solid #e1e1e1;
           border-radius: 4px;
           font-size: 14px;
           width: 120px;
       }

       .search-input {
           display: flex;
           flex-grow: 1;
       }

       .search-input input {
           flex-grow: 1;
           padding: 8px 12px;
           border: 1px solid #e1e1e1;
           border-radius: 4px 0 0 4px;
           font-size: 14px;
       }

       .search-input button {
           padding: 8px 16px;
           background: #a855f7;
           color: white;
           border: none;
           border-radius: 0 4px 4px 0;
           cursor: pointer;
           font-size: 14px;
       }

       .search-input button:hover {
           background: #9333ea;
       }

       /* 글쓰기 버튼 */
       .write-button {
           padding: 8px 20px;
           background: #a855f7;
           color: white;
           border: none;
           border-radius: 4px;
           cursor: pointer;
           font-size: 14px;
           font-weight: bold;
           display: flex;
           align-items: center;
           gap: 6px;
       }

       .write-button:hover {
           background: #9333ea;
       }

       /* 학과 선택 스타일 */
       .department-select {
           margin: 20px 0;
           padding: 10px;
           background: white;
           border: 1px solid #e1e1e1;
           border-radius: 8px;
       }

       .department-select select {
           width: 200px;
           padding: 8px;
           border: 1px solid #e1e1e1;
           border-radius: 4px;
           font-size: 14px;
       }

       /* 게시판 헤더 */
       .board-title {
           font-size: 24px;
           margin: 20px 0;
       }

       .board-nav {
           display: flex;
           gap: 1rem;
           border-bottom: 1px solid #e1e1e1;
           padding: 10px 0;
           margin-bottom: 20px;
       }

       .board-nav a {
           text-decoration: none;
           color: #666;
           padding: 5px 10px;
       }

       .board-nav a.active {
           color: #a855f7;
           font-weight: bold;
       }

       .board-nav a:hover {
           color: #a855f7;
       }

       /* 메인 콘텐츠 레이아웃 */
       .main-content {
           display: grid;
           grid-template-columns: 1fr 300px;
           gap: 20px;
       }

       /* 게시글 목록 */
       .post-list {
           background: white;
           border-radius: 8px;
           box-shadow: 0 1px 3px rgba(0,0,0,0.1);
       }

       .post-item {
           padding: 20px;
           border-bottom: 1px solid #eee;
       }

       .post-item:last-child {
           border-bottom: none;
       }

       .post-title {
           font-size: 16px;
           color: #333;
           margin-bottom: 8px;
       }

       .post-info {
           font-size: 12px;
           color: #888;
       }

       .post-info span {
           margin-right: 10px;
       }

       /* 사이드바 */
       .sidebar {
           background: white;
           border-radius: 8px;
           padding: 20px;
           box-shadow: 0 1px 3px rgba(0,0,0,0.1);
       }

       .sidebar-title {
           color: #a855f7;
           font-size: 16px;
           font-weight: bold;
           margin-bottom: 15px;
       }

       .hot-posts {
           margin-bottom: 30px;
       }

       .hot-post-item {
           padding: 10px 0;
           border-bottom: 1px solid #eee;
           display: flex;
           justify-content: space-between;
           align-items: center;
           font-size: 13px;
       }

       .hot-post-item:last-child {
           border-bottom: none;
       }

       .post-date {
           color: #888;
           font-size: 12px;
       }

       /* 푸터 */
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
</head>
<body>
   <header>
       <nav>
           <div class="logo">
               <img src="${pageContext.request.contextPath}/test/images/trainee.png" alt="트레이니 로고">
           </div>
           <div class="nav-links">
               <a href="#">게시판</a>
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
       <h1 class="board-title">학과별 게시판</h1>
       
       <!-- 학과 선택 추가 -->
       <div class="department-select">
           <select>
               <option>학과를 선택하세요</option>
               <option>컴퓨터공학과</option>
               <option>경영학과</option>
               <option>기계공학과</option>
               <option>전자공학과</option>
               <option>화학공학과</option>
               <option>건축학과</option>
           </select>
       </div>

       <div class="board-nav">
           <a href="#" class="active">전체</a>
           <a href="#">공지사항</a>
           <a href="#">학과행사</a>
           <a href="#">스터디</a>
           <a href="#">취업정보</a>
       </div>

       <!-- 검색 및 글쓰기 영역 추가 -->
       <div class="board-controls">
           <div class="search-box">
               <select>
                   <option>전체</option>
                   <option>제목</option>
                   <option>내용</option>
                   <option>작성자</option>
               </select>
               <div class="search-input">
                   <input type="text" placeholder="검색어를 입력하세요">
                   <button type="button">검색</button>
               </div>
           </div>
           <button class="write-button">
               <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                   <path d="M12 5v14M5 12h14"/>
               </svg>
               글쓰기
           </button>
       </div>

       <div class="main-content">
           <div class="post-list">
               <div class="post-item">
                   <h3 class="post-title">2024학년도 1학기 수강신청 안내</h3>
                   <div class="post-info">
                       <span>작성자: 학과사무실</span>
                       <span>조회수: 128</span>
                       <span>11:29</span>
                   </div>
               </div>
               <div class="post-item">
                   <h3 class="post-title">졸업프로젝트 발표회 일정 안내</h3>
                   <div class="post-info">
                       <span>작성자: 김교수</span>
                       <span>조회수: 95</span>
                       <span>10:45</span>
                   </div>
               </div>
               <div class="post-item">
                   <h3 class="post-title">알고리즘 스터디원 모집합니다</h3>
                   <div class="post-info">
                       <span>작성자: 익명</span>
                       <span>조회수: 45</span>
                       <span>10:15</span>
                   </div>
               </div>
           </div>

           <div class="sidebar">
               <div class="hot-posts">
                   <h3 class="sidebar-title">HOT 게시물</h3>
                   <div class="hot-post-item">
                       <span>2024 교과과정 개편 안내</span>
                       <span class="post-date">11/29 14:23</span>
                   </div>
                   <div class="hot-post-item">
                       <span>학과 MT 참가 신청</span>
                       <span class="post-date">11/28 15:58</span>
                   </div>
               </div>

               <div class="hot-posts">
                   <h3 class="sidebar-title">공지사항</h3>
                   <div class="hot-post-item">
                       <span>기사자격증 특강 안내</span>
                       <span class="post-date">11/27 12:30</span>
                   </div>
                   <div class="hot-post-item">
                       <span>학과 장학금 신청 안내</span>
                       <span class="post-date">11/26 18:45</span>
                   </div>
               </div>
           </div>
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
   
   <div class="modal-backdrop" id="modalBackdrop"></div>
   <div class="modal" id="writeModal">
       <div class="modal-header">
           <h2 class="modal-title">글쓰기</h2>
           <button type="button" class="modal-close" onclick="closeModal()">&times;</button>
       </div>
       <div class="modal-body">
           <form id="writeForm">
               <div class="form-group">
                   <label for="postTitle">제목</label>
                   <input type="text" id="postTitle" class="form-control" placeholder="제목을 입력하세요">
               </div>
               <div class="form-group">
                   <label for="postContent">내용</label>
                   <textarea id="postContent" class="form-control" placeholder="내용을 입력하세요"></textarea>
               </div>
           </form>
       </div>
       <div class="modal-footer">
           <button type="button" class="btn btn-cancel" onclick="closeModal()">취소</button>
           <button type="button" class="btn btn-submit">등록</button>
       </div>
   </div>

   <script>
       // 글쓰기 버튼 클릭 이벤트 핸들러
       document.querySelector('.write-button').addEventListener('click', function() {
           openModal();
       });

       // 모달 열기
       function openModal() {
           document.getElementById('modalBackdrop').style.display = 'block';
           document.getElementById('writeModal').style.display = 'block';
           document.body.style.overflow = 'hidden'; // 배경 스크롤 방지
       }

       // 모달 닫기
       function closeModal() {
           document.getElementById('modalBackdrop').style.display = 'none';
           document.getElementById('writeModal').style.display = 'none';
           document.body.style.overflow = 'auto'; // 배경 스크롤 복원
       }

       // 배경 클릭 시 모달 닫기
       document.getElementById('modalBackdrop').addEventListener('click', function() {
           closeModal();
       });
   </script>
</body>
</html>
