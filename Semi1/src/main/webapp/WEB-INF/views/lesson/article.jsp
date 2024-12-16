<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript">
window.contextPath = "${pageContext.request.contextPath}";
</script>
<title>${dto.title}-Trainee</title>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <!-- jQuery -->
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    
    <!-- 신고 관련 CSS/JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/report.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/report.js"></script>
    
    <!-- 외부 css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/lesson/article.css">
    
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
	<jsp:include page="/WEB-INF/views/layout/header.jsp" />
<script type="text/javascript">
    function deleteBoard() {
        if (confirm("게시글을 삭제하시겠습니까?")) {
            let query = "cm_num=${dto.cm_num}&${query}";
            let url = "${pageContext.request.contextPath}/lessonBoard/delete?" + query;
            location.href = url;
        }
    }
</script>


<script>
console.log("lessonNum:", "${dto.lessonNum}");
console.log("title:", "${dto.title}");
</script>

</head>
<body>
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
                        <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" alt="첨부 이미지">
                    </div>
                    <a href="${pageContext.request.contextPath}/lessonBoard/download?cm_num=${dto.cm_num}" class="file-link">
                        <i class="bi bi-download"></i> ${dto.fileName}
                    </a>
                </c:if>
            </div>

            <div class="like-container">
                <button class="btn ${dto.userLiked ? 'btn-purple' : 'btn-gray'} like-button"
                    onclick="toggleLike();" 
                    data-liked="${dto.userLiked}" 
                    data-num="${dto.cm_num}">
                    <i class="bi ${dto.userLiked ? 'bi-heart-fill' : 'bi-heart'}"></i> 좋아요
                    <span class="like-count">${dto.likeCount}</span>
                </button>
            </div>

            <div class="post-actions">
			    <c:if test="${sessionScope.member.mb_Num==dto.mb_num || sessionScope.member.role == 60}">
			        <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/update?cm_num=${dto.cm_num}&page=${page}';">수정</button>
			        <button class="btn btn-red" onclick="deleteBoard();">삭제</button>
			    </c:if>
                <!-- 신고 버튼 수정 -->
                <!-- table 자리에 divison이 들어갈까. -->
               	 <c:if test="${not empty sessionScope.member}">
				    <button type="button" class="btn btn-red btnReport" 
				        data-table="lessonBoard" 
				        data-lessonNum="${dto.lessonNum}"
				        data-url="${pageContext.request.contextPath}/lessonBoard/article?cm_num=${dto.cm_num}"
				        data-title="${dto.title}">
				        <i class="bi bi-exclamation-triangle"></i> 신고
				    </button>
				</c:if>
                <button class="btn btn-gray" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/list?${query}';">목록</button>
            </div>

            <jsp:include page="reply.jsp" />

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
    </div>
	
<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

<script type="text/javascript">
        function toggleLike() {
            let url = "${pageContext.request.contextPath}/lessonBoard/like";
            let query = "cm_num=${dto.cm_num}";

            const fn = function(data) {
                let state = data.state;
                if (state === "true") {
                    let $btn = $(".like-button");
                    let $icon = $btn.find("i");
                    let $count = $btn.find(".like-count");
                    let liked = $btn.data("liked") === true;

                    if (liked) {
                        $btn.removeClass("btn-purple").addClass("btn-gray");
                        $icon.removeClass("bi-heart-fill").addClass("bi-heart");
                    } else {
                        $btn.removeClass("btn-gray").addClass("btn-purple");
                        $icon.removeClass("bi-heart").addClass("bi-heart-fill");
                    }

                    $btn.data("liked", !liked);
                    $count.text(data.likeCount);
                    $btn.data("count", data.likeCount);
                } else {
                    alert("좋아요 처리가 실패했습니다.");
                }
            };

            ajaxFun(url, "post", query, "json", fn);
        }
    </script>
</body>
</html>