<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${dto.title} - Trainee</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>

.post-navigation {
   background: white;
   border-radius: 8px;
   box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
   margin-top: 20px;
}

.post-navigation-item {
   padding: 15px 20px;
   display: flex;
   align-items: center;
   border-bottom: 1px solid #e5e7eb;
}

.post-navigation-item:last-child {
   border-bottom: none;
}

.post-navigation-label {
   color: #666;
   font-size: 14px;
   min-width: 60px;
}

.post-navigation-link {
   text-decoration: none;
   color: #333;
   font-size: 14px;
   margin-left: 15px;
}

.post-navigation-link:hover {
   color: #a855f7;
}

.post-navigation-item .bi {
   color: #666;
   font-size: 12px;
   margin: 0 5px;
 }

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

/* 게시글 상세 스타일 */
.post-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

.post-detail {
    background: white;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
}

.post-header {
    padding: 20px;
    border-bottom: 1px solid #e5e7eb;
}

.post-title {
    font-size: 24px;
    color: #333;
    margin-bottom: 15px;
}

.post-meta {
    display: flex;
    justify-content: space-between;
    color: #666;
    font-size: 14px;
    flex-wrap: wrap;
    gap: 10px;
}

.post-info {
    display: flex;
    gap: 15px;
}

.post-content {
    padding: 30px 20px;
    line-height: 1.6;
    color: #333;
    font-size: 15px;
}

.post-actions {
    padding: 15px 20px;
    border-top: 1px solid #e5e7eb;
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}

.btn {
    padding: 8px 16px;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 6px;
    border: 1px solid #e5e7eb;
    background: white;
}

.btn-purple {
    background: #a855f7;
    color: white;
    border: none;
}

.btn-purple:hover {
    background: #9333ea;
}

.btn-gray {
    color: #666;
}

.btn-gray:hover {
    background: #f9fafb;
}

.btn-red {
    color: #ef4444;
    border-color: #ef4444;
}

.btn-red:hover {
    background: #fef2f2;
}

/* 이미지 CSS */
.img-box {
    max-width: 100%;
    margin: 20px 0;
}

.img-box img {
    max-width: 100%;
    height: auto;
}

/* 목록으로 버튼 */
.back-link {
    display: inline-block;
    margin-bottom: 20px;
    color: #666;
    text-decoration: none;
    font-size: 14px;
}

.back-link:hover {
    color: #a855f7;
}

/* 파일 다운로드 링크 */
.file-link {
    display: inline-block;
    margin: 10px 0;
    padding: 8px 12px;
    background-color: #f3f4f6;
    border-radius: 4px;
    color: #666;
    text-decoration: none;
    font-size: 14px;
}

.file-link:hover {
    background-color: #e5e7eb;
    color: #a855f7;
}
</style>
<script type="text/javascript">
function deleteBoard() {
   if(confirm("게시글을 삭제하시겠습니까?")) {
       let query = "cm_num=${dto.cm_num}&${query}";
       let url = "${pageContext.request.contextPath}/lessonBoard/delete?" + query;
       location.href = url;
   }
}
</script></head>
<body>
   <header>
       <nav>
           <div class="logo">
               <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="트레이니 로고">
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

   <div class="post-container">
       <a href="${pageContext.request.contextPath}/lessonBoard/list?${query}" class="back-link">
           ← 목록으로
       </a>

       <div class="post-detail">
           <div class="post-header">
               <h1 class="post-title">${dto.title}</h1>
               <div class="post-meta">
                   <div class="post-info">
                       <span>작성자: ${dto.nickName}</span>
                       <span>학과: ${dto.lessonName}</span>
                       <span>작성일: ${dto.ca_date}</span>
                   </div>
                   <div class="post-info">
                       <span>조회 ${dto.views}</span>
                   </div>
               </div>
           </div>

           <div class="post-content">
               ${dto.board_content}
               
               <c:if test="${not empty dto.fileName}">
                   <div class="img-box">
                       <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" 
                            alt="첨부 이미지">
                   </div>
                   <a href="${pageContext.request.contextPath}/lessonBoard/download?cm_num=${dto.cm_num}" 
                      class="file-link">
                       <i class="bi bi-download"></i> ${dto.fileName}
                   </a>
               </c:if>
           </div>

           <div class="post-actions">
               <c:if test="${sessionScope.member.mb_Num==dto.mb_num || sessionScope.member.role >= 51}">
                   <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/update?cm_num=${dto.cm_num}&page=${page}';">
                       수정
                   </button>
                   <button class="btn btn-red" onclick="deleteBoard();">
                       삭제
                   </button>
               </c:if>
               <button class="btn btn-gray" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/list?${query}';">
                   목록
               </button>
           </div>
       </div>

		<jsp:include page="reply.jsp"/>

       <!-- 이전글/다음글 추가 -->
       <div class="post-navigation">
           <c:if test="${not empty prevDto}">
               <div class="post-navigation-item">
                   <span class="post-navigation-label">이전글</span>
                   <i class="bi bi-chevron-up"></i>
                   <a class="post-navigation-link" href="${pageContext.request.contextPath}/lessonBoard/article?${query}&cm_num=${prevDto.cm_num}">
                       ${prevDto.title}
                   </a>
               </div>
           </c:if>
           <c:if test="${not empty nextDto}">
               <div class="post-navigation-item">
                   <span class="post-navigation-label">다음글</span>
                   <i class="bi bi-chevron-down"></i>
                   <a class="post-navigation-link" href="${pageContext.request.contextPath}/lessonBoard/article?${query}&cm_num=${nextDto.cm_num}">
                       ${nextDto.title}
                   </a>
               </div>
           </c:if>
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
<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
    // 페이지 로딩 시 댓글 목록 불러오기
    listReply(1);
    
    // 댓글 등록
    $(".btn-purple").click(function(){
        let cm_num = "${dto.cm_num}";
        const $tb = $(this).closest("div").find("textarea");
        let content = $tb.val().trim();
        
        if(! content) {
            alert("내용을 입력하세요 !");
            $tb.focus();
            return false;
        }
        
        let url = "${pageContext.request.contextPath}/lessonBoard/insertReply";
        let query = "cm_num=" + cm_num + "&content=" + encodeURIComponent(content);
        
        const fn = function(data){
            $tb.val("");
            listReply(1);
        };
        
        ajaxFun(url, "post", query, "json", fn);
    });
});

// Ajax 공통 함수
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
            if(jqXHR.status === 400) {
                alert("요청 처리가 실패했습니다.");
                return false;
            }
            console.log(jqXHR.responseText);
        }
    });
}

// 댓글 리스트 불러오기
function listReply(page) {
    let cm_num = "${dto.cm_num}";
    let url = "${pageContext.request.contextPath}/lessonBoard/listReply";
    let query = "cm_num=" + cm_num + "&pageNo=" + page;
    
    const fn = function(data){
        printReply(data);
    };
    ajaxFun(url, "get", query, "json", fn);
}

// 댓글 출력하기
function printReply(data) {
    let html = "";
    const list = data.list;
    
    for(let item of list) {
        html += '<div class="comment-item">';
        html += '    <div class="comment-header">';
        html += '        <span class="comment-author">' + item.nickName + '</span>';
        html += '        <span class="comment-date">' + item.reg_date + '</span>';
        html += '    </div>';
        html += '    <div class="comment-text">' + item.content + '</div>';
        html += '    <div class="comment-actions">';
        
        if(${sessionScope.member.mb_Num} === item.mb_num) {
            html += '    <span class="comment-action" onclick="deleteReply(' + item.co_num + ')">삭제</span>';
        }
        
        html += '    </div>';
        html += '</div>';
    }
    
    $(".comment-list").html(html);
    
    // 댓글 갯수 업데이트
    $(".comments-count").text(data.dataCount);
}

// 댓글 삭제
function deleteReply(co_num) {
    if(! confirm("댓글을 삭제하시겠습니까 ? ")) {
        return;
    }
    
    let url = "${pageContext.request.contextPath}/lessonBoard/deleteReply";
    let query = "co_num=" + co_num;
    
    const fn = function(data){
        listReply(1);
    };
    
    ajaxFun(url, "post", query, "json", fn);
}
</script>
</html>