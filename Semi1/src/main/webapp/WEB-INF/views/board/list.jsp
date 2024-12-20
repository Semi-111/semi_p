<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${boardType == 'free' ? '자유게시판' : '정보게시판'}"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/list.css">
  <script src="${pageContext.request.contextPath}/resources/js/board/list.js"></script>

  <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
</head>
<body>

<header>
  <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="container">
  <h1 class="board-title">
    <c:out value="${boardType == 'free' ? '자유게시판' : '정보게시판'}"/>
  </h1>

  <form name="searchForm" action="${pageContext.request.contextPath}/bbs/infoBoard/list">
    <input type="hidden" name="type" value="${boardType}">
    <div class="board-controls">
      <div class="search-box">
        <select name="schType">
          <option value="all" ${schType == "all" ? 'selected' : ''}>전체</option>
          <option value="title" ${schType == "title" ? 'selected' : ''}>제목</option>
          <option value="content" ${schType == "content" ? 'selected' : ''}>내용</option>
          <option value="name" ${schType == "name" ? 'selected' : ''}>작성자</option>
        </select>
        <div class="search-input">
          <input type="text" name="kwd" value="${kwd}" placeholder="검색어를 입력하세요">
          <button type="button" onclick="searchList();">검색</button>
        </div>
      </div>
      <button type="button" class="write-button" onclick="location.href='${pageContext.request.contextPath}/bbs/infoBoard/write?type=${boardType}'">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
             stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 5v14M5 12h14"/>
        </svg>
        글쓰기
      </button>
    </div>
    </form>

  <div class="main-content">
    <!-- 게시글 목록 -->
    <div class="post-list">
      <c:forEach var="dto" items="${list}" varStatus="status">
        <div class="post-item">
          <div class="post-content">
            <h3 class="post-title">
              <a href="${pageContext.request.contextPath}/bbs/infoBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}">
                  ${dto.title}
              </a>
            </h3>
            <p>${fn:substring(dto.content, 0, 30)}...</p>
            <div class="post-meta">
              <span>${dto.member.nickName}</span>
              <div class="post-meta-icons">
                <span><i class="fa-solid fa-eye"></i> ${dto.views}</span>
                <c:if test="${dto.boardLikeCount > 0}">
                  <span><i class="${isUserLike ? 'fa-solid fa-heart liked' : 'fa-regular fa-heart'}"></i> ${dto.boardLikeCount}</span>
                </c:if>
                <c:if test="${dto.replyCount > 0}">
                  <span><i class="far fa-comment-dots"></i> ${dto.replyCount}</span>
                </c:if>
              </div>
              <span>${dto.formattedCaDate}</span>
            </div>
          </div>
          <c:if test="${dto.fileName != null && !empty dto.fileName}">
            <div class="post-image">
              <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="게시글 이미지">
            </div>
          </c:if>
        </div>
      </c:forEach>
    </div>

    <!-- 사이드바 -->
    <div class="sidebar">
      <div class="hot-posts">
        <h3 class="sidebar-title">HOT 게시물</h3>
        <div class="hot-post-item">
          <span>자유게시판 인기글</span>
          <span class="post-date">11/29</span>
        </div>
        <div class="hot-post-item">
          <span>학과 MT 후기</span>
          <span class="post-date">11/28</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>
      </div>

      <div class="weekly-posts">
        <h3 class="sidebar-title">주간 인기글</h3>
        <div class="hot-post-item">
          <span>2024 학과 행사</span>
          <span class="post-date">11/27</span>
        </div>
        <div class="hot-post-item">
          <span>스터디 모집</span>
          <span class="post-date">11/25</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>
      </div>

      <div class="recent-reviews">
        <h3 class="sidebar-title">최근 강의평</h3>
        <div class="hot-post-item">
          <span>알고리즘 수업 리뷰</span>
          <span class="post-date">11/26</span>
        </div>
        <div class="hot-post-item">
          <span>데이터베이스 수업 후기</span>
          <span class="post-date">11/24</span>
        </div>
        <a href="#" class="d-block text-end mt-2">더보기</a>
      </div>
    </div>
  </div>

  <div class="pagination">
    <ul>
<%--      ${paging}--%>
  ${dataCount == 0 ? "등록된 게시글이 없습니다" : paging}
    </ul>
  </div>
</div>


  <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
  <jsp:include page="/WEB-INF/views/layout/footer.jsp" />


</body>
</html>
