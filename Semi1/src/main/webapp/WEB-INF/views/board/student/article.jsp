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
    <script>
        const contextPath = '<c:out value="${pageContext.request.contextPath}" />';
        const boardType = '<c:out value="${boardType}" />';
        const cmNum = '<c:out value="${dto.cmNum}" />';
        const page = '<c:out value="${page}" />';
    </script>
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

<%--<button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/bbs/studentBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'">새로고침</button>--%>


<div class="reply">
    <form name="replyForm" method="post">
        <div class='form-header'>
            <!-- 텍스트와 새로고침 버튼 배치를 위해 flex 사용 -->
            <div style="flex: 1;">
                <span class="bold">댓글</span>
                <span class="instructions"> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
            </div>
            <!-- 새로고침 아이콘 -->
            <button type="button" class="btn-refresh"
                    onclick="location.href='${pageContext.request.contextPath}/bbs/studentBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'"
                    title="새로고침">
                <i class="fas fa-sync-alt"></i>
            </button>
        </div>

        <table class="table table-borderless reply-form">
            <tr>
                <td>
                    <textarea class="form-control" name="content" placeholder="댓글을 작성해주세요."></textarea>
                </td>
            </tr>
            <tr>
                <td align="right">
                    <button type="button" class="btn btn-light btnSendReply">댓글 등록</button>
                </td>
            </tr>
        </table>
    </form>

    <div id="listReply"></div>
</div>
<footer>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</footer>

<script>
    $(function () {
        listPage(1);

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

    /*댓글*/
    // 댓글 등록
    $(function () {
        $('.btnSendReply').click(function (){
            const $tb = $(this).closest('table'); // 답글로 인해서 closest를 찾는다
            let content = $tb.find('textarea').val().trim();

            if(! content) {
                $tb.find('textarea').focus();
                return false;
            }

            let cmNum = '${dto.cmNum}';
            let url = '${pageContext.request.contextPath}/bbs/studentBoard/replyInsert';

            let query = {cmNum : cmNum, content : content}; // 객체로 처리하면 인코딩 x
            // formData를 객체로 처리하면 content를 인코딩하면 안된다.

            const fn = function (data) {
                alert(data.state);
                if(data.state === 'true') {
                    $tb.find('textarea').val('');
                    listPage(1);
                } else {
                    alert('댓글 등록이 실패 했습니다.');
                }
            };
            ajaxFun(url, 'post', query, 'json', fn);
        });
    });


    function listPage(page) {
        let cmNum = '${dto.cmNum}'; // cmNum 가져오기
        let url = '${pageContext.request.contextPath}/bbs/studentBoard/listReply';
        let query = 'cmNum=' + cmNum + '&pageNo=' + page; // 쌍따옴표 없이 설정
        let selector = '#listReply';

        const fn = function (data) {
            $(selector).html(data);
        };

        ajaxFun(url, 'get', query, 'text', fn);
    }

    /* --------------------- */
    // 답글 버튼
    $(function () {
        $('#listReply').on('click', '.btnReplyAnswerLayout', function (){
            let replyNum = $(this).attr('data-replyNum'); // 동적으로 추가 된건 자식을 찾아서 부모를 찾아야한다.
            const $trAnswer = $(this).closest('tr').next();
            let isVisible = $trAnswer.is(':visible');

            if(isVisible) {
                $trAnswer.hide();
            } else {
                $trAnswer.show();

                // 답글 리스트
                listReplyAnswer(replyNum);

                // 답글 개수
                countReplyAnswer(replyNum);
            }
        });
    });

    // 답글 등록 버튼
    $(function () {
        $('#listReply').on('click', '.btnSendReplyAnswer', function () {
            let cmNum = '${dto.cmNum}';
            let replyNum = $(this).attr('data-replyNum');
            const $td = $(this).closest('td');

            let content = $td.find('textarea').val().trim();
            if( ! content) {
                $td.find('textarea').focus();
                return false;
            }

            let url = '${pageContext.request.contextPath}/bbs/studentBoard/replyInsert';
            let formData = {cmNum : cmNum, content : content};

            const fn = function (data) {
                $td.find('textarea').val('');

                if(data.state === 'true') {
                    listReplyAnswer(replyNum);
                    countReplyAnswer(replyNum);
                }
            };

            ajaxFun(url, 'post', formData, 'json', fn);
        });
    });

    // 댓글별 답글 리스트
    function listReplyAnswer(parentNum) {
        let url = '${pageContext.request.contextPath}/bbs/listReplyAnswer'
        let query = 'parentNum=' + parentNum;
        let selector = '#listReplyAnswer' + parentNum;

        const fn = function (data) {
            $(selector).html(data);
        };

        ajaxFun(url, 'get', query, 'text', fn);
    }

    // 댓글별 답글 개수
    function countReplyAnswer(parentNum) {
        let url = '${pageContext.request.contextPath}/bbs/countReplyAnswer'
        let query = 'parentNum=' + parentNum;

        const fn = function (data) {
            let count = data.count;
            let selector = '#answerCount' + parentNum;
            $(selector).html(count);
        };

        ajaxFun(url, 'post', query, 'json', fn);
    }

    // 댓글 삭제
    $(function () {
        $('#listReply').on('click', '.deleteReply', function () {
            if( ! confirm('게시글을 삭제 하시겠습니까')) {
                return false;
            }

            let cmNum = $(this).attr('data-replyNum');
            let page = $(this).attr('data-pageNo');

            let url = '${pageContext.request.contextPath}/bbs/studentBoard/deleteReply';
            let query = 'cmNum=' + cmNum;

            const fn = function (data) {
                listPage(page);
            }

            ajaxFun(url, 'post', query, 'json', fn);
        });
    });

    // 댓글의 답글 삭제
    // 댓글 삭제
    $(function () {
        $('#listReply').on('click', '.deleteReplyAnswer', function () {
            if( ! confirm('댓글을 삭제 하시겠습니까')) {
                return false;
            }

            let cmNum = $(this).attr('data-replyNum');
            let parentNum = $(this).attr('data-parentNum');

            let url = '${pageContext.request.contextPath}/bbs/studentBoard/deleteReply';
            let query = 'cmNum=' + cmNum;

            const fn = function (data) {
                listReplyAnswer(parentNum);
            }

            ajaxFun(url, 'post', query, 'json', fn);
        });
    });
</script>

</body>
</html>
