<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Naver Map Example</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/css/bootstrap.min.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/icon/bootstrap-icons.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/core2.css" type="text/css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/util-jquery.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/map/map.css">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>

<button id="toggleButton" class="btn btn-primary" onclick="toggleSearch()">☰</button>
<div id="search">
    <div class="search-header">
        <h2>검색</h2>
        <button class="btn btn-secondary" onclick="toggleSearch()">닫기</button>
    </div>
    <div class="search-input-group">
        <input type="text" id="searchInput" placeholder="검색어를 입력하세요" class="form-control mb-2">
        <button onclick="searchPlaces()" class="btn btn-primary">검색</button>
    </div>
    <div class="search-results">
        <ul id="results"></ul>
    </div>
</div>


<div id="map"></div>


<script type="text/javascript"
        src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${NaverClientID}"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/map/map.js"></script>

</body>
</html>