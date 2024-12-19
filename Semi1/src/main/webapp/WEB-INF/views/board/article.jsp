<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${boardType == "free" ? "자유" : "정보"}게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/article1.css">
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

<div class="post-container"><!-- post-container 시작 -->
    <a href="${pageContext.request.contextPath}/bbs/infoBoard/list?type=${boardType}&cmNum=${dto.cmNum}&page=${page}" class="back-link">← 목록으로</a>

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

        <div class="post-actions">
            <div class="action-left">
                <button type="button" class="btn btn-gray btnSendBoardLike" title="좋아요">
                    <!-- isUserLike가 true면 채워진 하트, 아니면 빈 하트 -->
                    <i class="${isUserLike ? 'fa-solid fa-heart liked' : 'fa-regular fa-heart'} heart-icon"></i>
                    &nbsp;&nbsp;<span id="boardLikeCount">${(dto.boardLikeCount != null && dto.boardLikeCount > 0) ? dto.boardLikeCount : 0}</span>
                </button>
                <button class="btn btn-gray">
                <span class="reply-count">
                    <i class="far fa-comment-dots" style="margin-right:5px;"></i>${replyCount}
                </span>
                </button>
            </div>

            <c:if test="${sessionScope.member.mb_Num == dto.mbNum || sessionScope.member.role eq '60'}">
                <div class="action-right">
                    <button class="btn btn-purple" onclick="updateInfoBoard()">수정</button>
                    <button class="btn btn-red" onclick="deleteInfoBoard();">삭제</button>
                    <button class="btn btn-red">신고</button>
                </div>
            </c:if>
        </div>
    </div>

    <!-- reply 영역을 post-container 내부로 이동 -->
    <div class="reply">
        <form name="replyForm" method="post">
            <div class='form-header'>
                <div style="flex: 1;">
                    <span class="bold">댓글</span>
                    <span class="instructions"> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
                </div>
                <!-- 새로고침 아이콘 -->
                <button type="button" class="btn-refresh"
                        onclick="location.href='${pageContext.request.contextPath}/bbs/infoBoard/article?type=${boardType}&cmNum=${dto.cmNum}&page=${page}'"
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
    <!-- reply 영역 종료 -->

</div><!-- post-container 종료 -->


<footer>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</footer>

<script>
    $(function () {
        listPage(1);

        $('.btnSendBoardLike').click(function () {
            const $i = $(this).find('i.heart-icon');
            let userLiked = $i.hasClass('fa-solid'); // fa-solid fa-heart 라면 좋아요 상태
            let msg = userLiked ? '공감을 취소 하시겠습니까?' : '이 글에 공감하시겠습니까?';
            if (!confirm(msg)) return false;

            let url = '${pageContext.request.contextPath}/bbs/infoBoard/insertBoardLike';
            let query = 'cm_Num=${dto.cmNum}&userLiked=' + userLiked; // userLiked는 true/false 문자열로 전달

            const fn = function (data) {
                let state = data.state;
                if (state === 'true') {
                    let count = data.boardLikeCount || 0;
                    $('#boardLikeCount').text(count);

                    if (userLiked) {
                        // 좋아요 취소: 채워진 하트 → 빈 하트
                        $i.removeClass('fa-solid fa-heart').addClass('fa-regular fa-heart').removeClass('liked');
                    } else {
                        // 좋아요 추가: 빈 하트 → 채워진 하트
                        $i.removeClass('fa-regular fa-heart').addClass('fa-solid fa-heart liked');
                    }
                } else if (state === 'liked') {
                    alert('게시글 공감은 한 번만 가능합니다.');
                } else {
                    alert('게시글 공감 여부 처리가 실패했습니다.');
                }
            };

            ajaxFun(url, 'post', query, 'json', fn);
        });
    });

    // 댓글 등록
    $(function () {
        $('.btnSendReply').click(function (){
            const $tb = $(this).closest('table');
            let content = $tb.find('textarea').val().trim();

            if(! content) {
                $tb.find('textarea').focus();
                return false;
            }

            let cmNum = '${dto.cmNum}';
            let url = '${pageContext.request.contextPath}/bbs/infoBoard/replyInsert';

            let query = {cmNum : cmNum, content : content};

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
        let cmNum = '${dto.cmNum}';
        let url = '${pageContext.request.contextPath}/bbs/infoBoard/listReply';
        let query = 'cmNum=' + cmNum + '&pageNo=' + page;
        let selector = '#listReply';

        const fn = function (data) {
            $(selector).html(data);
        };

        ajaxFun(url, 'get', query, 'text', fn);
    }

    $(function () {
        // 답글 버튼
        $('#listReply').on('click', '.btnReplyAnswerLayout', function (){
            let replyNum = $(this).attr('data-replyNum');
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

            let url = '${pageContext.request.contextPath}/bbs/infoBoard/replyInsert';
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
            if( ! confirm('댓글을 삭제 하시겠습니까')) {
                return false;
            }

            let cmNum = $(this).attr('data-replyNum');
            let page = $(this).attr('data-pageNo');

            let url = '${pageContext.request.contextPath}/bbs/infoBoard/deleteReply';
            let query = 'cmNum=' + cmNum;

            const fn = function (data) {
                listPage(page);
            }

            ajaxFun(url, 'post', query, 'json', fn);
        });
    });

    // 댓글의 답글 삭제
    $(function () {
        $('#listReply').on('click', '.deleteReplyAnswer', function () {
            if( ! confirm('댓글을 삭제 하시겠습니까')) {
                return false;
            }

            let cmNum = $(this).attr('data-replyNum');
            let parentNum = $(this).attr('data-parentNum');

            let url = '${pageContext.request.contextPath}/bbs/infoBoard/deleteReply';
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
