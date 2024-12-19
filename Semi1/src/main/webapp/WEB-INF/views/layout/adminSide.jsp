<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
<aside class="admin-sidebar">
    <div class="sidebar-top">
        <div class="admin-logo">
            <a href="${pageContext.request.contextPath}/admin/home/main">
                <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="트레이니 어드민">
            </a>
        </div>

        <ul class="admin-menu">
            <li>
                <a href="${pageContext.request.contextPath}/admin/home/membership"
                   class="${requestScope['javax.servlet.forward.servlet_path'].endsWith('/membership') ? 'active' : ''}">
                    회원 관리
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/home/report"
                   class="${requestScope['javax.servlet.forward.servlet_path'].endsWith('/report') ? 'active' : ''}">
                    신고 관리
                </a>
            </li>
        </ul>
    </div>

    <div class="back-button">
        <a href="${pageContext.request.contextPath}/main" class="back-link">
            <i class="bi bi-arrow-left"></i> 메인으로 돌아가기
        </a>
    </div>
</aside>
</body>
</html>