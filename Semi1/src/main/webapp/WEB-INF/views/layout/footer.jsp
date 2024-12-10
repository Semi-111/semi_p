<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
html, body {
    margin: 0;
    padding: 0;
    min-height: 100vh;
}

body {
    position: relative;
    padding-bottom: 160px; /* footer의 높이보다 크게 설정 */
}

/* 푸터 */
footer {
    background-color: #f9fafb;
    padding: 2rem 1rem;
    border-top: 1px solid #eee;
    position: absolute;
    bottom: 0;
    width: 100%;
    box-sizing: border-box;
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
</html>