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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/board/article.js"></script>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<div class="post-container">
    <a href="${pageContext.request.contextPath}/bbs/studentBoard/list?type=${boardType}&page=${page}" class="back-link">← 목록으로</a>
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

        <c:if test="${dto.fileName != null && !empty dto.fileName}">
            <div class="post-image">
                <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="게시글 이미지">
            </div>
        </c:if>

        <div class="post-actions" style="display: flex; justify-content: space-between; align-items: center;">
            <div class="action-left" style="display: flex; align-items: center; gap: 10px;">
                <button type="button" class="btn btn-gray btnSendBoardLike" title="좋아요">
                    <i class="${isUserLike ? 'fa-solid fa-heart liked' : 'fa-regular fa-heart'} heart-icon"></i>
                    &nbsp;&nbsp;<span id="boardLikeCount">${(dto.boardLikeCount != null && dto.boardLikeCount > 0) ? dto.boardLikeCount : 0}</span>
                </button>
                <button class="btn btn-gray">공유하기</button>
            </div>

            <c:if test="${sessionScope.member.mb_Num == dto.mbNum || sessionScope.member.role eq '60'}">
                <div class="action-right" style="display: flex; align-items: center; gap: 10px;">
                    <button class="btn btn-purple" onclick="updateStudentBoard();">수정</button>
                    <button class="btn btn-red" onclick="deleteStudentBoard();">삭제</button>
                    <button class="btn btn-red">신고</button>
                </div>
            </c:if>
        </div>
    </div>
</div>

<div class="comments-section">
    <div class="comments-header">
        <span>댓글 <span class="comments-count">5</span></span>
        <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/bbs/studentBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'">새로고침</button>
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

<footer>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</footer>

<script>
    $(function () {
        $('.btnSendBoardLike').click(function () {
            const $i = $(this).find('i.heart-icon');
            let userLiked = $i.hasClass('fa-solid');
            let msg = userLiked ? '공감을 취소 하시겠습니까?' : '이 글에 공감하시겠습니까?';
            if (!confirm(msg)) return;

            let url = '${pageContext.request.contextPath}/bbs/studentBoard/insertBoardLike';
            let query = 'cm_Num=${dto.cmNum}&userLiked=' + userLiked;

            ajaxFun(url, 'post', query, 'json', function (data) {
                if (data.state === 'true') {
                    $('#boardLikeCount').text(data.boardLikeCount || 0);
                    if (userLiked) {
                        $i.removeClass('fa-solid fa-heart').addClass('fa-regular fa-heart').removeClass('liked');
                    } else {
                        $i.removeClass('fa-regular fa-heart').addClass('fa-solid fa-heart liked');
                    }
                } else if (data.state === 'liked') {
                    alert('게시글 공감은 한 번만 가능합니다.');
                } else {
                    alert('게시글 공감 처리에 실패했습니다.');
                }
            });
        });
    });
</script>

</body>
</html>
