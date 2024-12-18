<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>공지사항</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/notice/article.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</head>
<body>

<div class="container">
    <div class="article-container">
        <div class="article-header">
            <h1 class="title">공지사항</h1>
            
            <div class="article-title">
                <c:if test="${dto.notice == 1}">
                    <span class="badge bg-danger">중요</span>
                </c:if>
                <span class="badge bg-primary">공지</span>
                ${dto.title}
            </div>
            
            <div class="article-info">
                <span class="writer"><i class="bi bi-person"></i> ${dto.nickName}</span>
                <span class="date"><i class="bi bi-calendar3"></i> ${dto.ca_date}</span>
                <span class="views"><i class="bi bi-eye"></i> ${dto.views}</span>
            </div>
        </div>

        <div class="article-content">
            <c:if test="${not empty dto.fileName}">
                <div class="image-container">
                    <img src="${pageContext.request.contextPath}/uploads/notice/${dto.fileName}">
                </div>
            </c:if>
            
            <div class="content">
                ${dto.content}
            </div>
        </div>

        <div class="article-footer">
            <table class="table table-borderless">
                <tr>
                    <td class="text-start">
                        <c:if test="${not empty prevDto}">
                            <a href="${pageContext.request.contextPath}/noticeBoard/article?${query}&noticeNum=${prevDto.cm_num}" class="text-decoration-none">
                                <i class="bi bi-chevron-up"></i> 이전글: ${prevDto.title}
                            </a>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <c:if test="${not empty nextDto}">
                            <a href="${pageContext.request.contextPath}/noticeBoard/article?${query}&noticeNum=${nextDto.cm_num}" class="text-decoration-none">
                                <i class="bi bi-chevron-down"></i> 다음글: ${nextDto.title}
                            </a>
                        </c:if>
                    </td>
                </tr>
            </table>

            <div class="buttons">
			    <button type="button" class="btn btn-list" onclick="location.href='${pageContext.request.contextPath}/noticeBoard/list?${query}';">목록</button>
			    <c:if test="${sessionScope.member.role >= 60}">
			        <button type="button" class="btn btn-update" onclick="location.href='${pageContext.request.contextPath}/noticeBoard/update?noticeNum=${dto.cm_num}&page=${page}';">수정</button>
			        <button type="button" class="btn btn-delete" onclick="if(confirm('게시글을 삭제하시겠습니까?')) { location.href='${pageContext.request.contextPath}/noticeBoard/delete?noticeNum=${dto.cm_num}&page=${page}'; }">삭제</button>
			    </c:if>
			</div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>