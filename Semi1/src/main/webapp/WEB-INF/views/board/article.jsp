<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${boardType}게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/article1.css">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

    <script>
        function deleteBoard() {
            if(! confirm('게시글을 삭제하시겠습니까?')) {
                return false;
            }
            location.href = '${pageContext.request.contextPath}/bbs/infoBoard/delete?type=${boardType}&cmNum=${dto.cmNum}&page=${page}';
        }

    </script>
</head>
<body>

    <header>
        <jsp:include page="/WEB-INF/views/layout/header.jsp" />
    </header>


    <div class="post-container">
        <a href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=${boardType}&num=${dto.cmNum}&page=${page}" class="back-link">← 목록으로</a>

        <div class="post-detail">
            <div class="post-header">
                <h1 class="post-title">${dto.title}</h1>
                <div class="post-meta">
                    <div class="post-info">
                        <span>작성자: ${dto.member.nickName}</span>
                        <span>작성일: ${dto.caDate}</span>
                    </div>
                    <div class="post-info">
                        <span>조회 ${dto.views}</span>
                        <span>댓글 5</span>
                    </div>
                </div>
            </div>

            <div class="post-content">
                <span>${dto.content}</span>
            </div>

<%--                <div class="post-image">--%>
<%--                    <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="게시글 이미지">--%>
<%--                </div>--%>
            <c:if test="${dto.fileName != null && !empty dto.fileName}">
                <div class="post-image">
                    <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="게시글 이미지">
                </div>
            </c:if>

            <div class="post-actions">
                <div class="action-left">
                    <button class="btn btn-gray">
                        좋아요 0
                    </button>
                    <button class="btn btn-gray">
                        공유하기
                    </button>
                </div>
                <div class="action-right">
                    <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/bbs/infoBoard/update?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'">수정</button>
                    <button class="btn btn-red" onclick = deleteBoard();>삭제</button>
                    <button class="btn btn-red">신고</button>
                </div>
            </div>
        </div>

        <div class="comments-section">
            <div class="comments-header">
                <span>댓글 <span class="comments-count">5</span></span>
                <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/bbs/infoBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'">새로고침</button>
            </div>
            <div class="comment-write">
                <textarea class="comment-textarea" placeholder="댓글을 입력하세요"></textarea>
                <button class="btn btn-purple" style="width: 100%">댓글 작성</button>
            </div>
            <div class="comment-list">
                <div class="comment-item">
                    <div class="comment-header">
                        <span class="comment-author">익명1</span>
                        <span class="comment-date">2024.12.06 12:30</span>
                    </div>
                    <div class="comment-text">
                        <span>댓글입니다.</span>
                    </div>
                    <div class="comment-actions">
                        <span class="comment-action">답글</span>
                        <span class="comment-action">신고</span>
                    </div>
                </div>
                <div class="comment-item">
                    <div class="comment-header">
                        <span class="comment-author">학과사무실</span>
                        <span class="comment-date">2024.12.06 13:15</span>
                    </div>
                    <div class="comment-text">
                        <span>댓글입니다.</span>
                    </div>
                    <div class="comment-actions">
                        <span class="comment-action">답글</span>
                        <span class="comment-action">신고</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
        <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
    </footer>

</body>
</html>