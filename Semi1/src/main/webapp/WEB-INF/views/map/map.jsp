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

    <style>
        body {
            display: flex;
            flex-direction: column;
            height: 100vh;
            margin: 0;
        }

        #search {
            width: 400px;
            padding: 20px;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
            position: absolute;
            display: block;
            top: 56px; /* Adjusted for navbar height */
            height: calc(100% - 56px); /* Adjusted for navbar height */
            background: white;
            z-index: 1002;
            transition: display 0.3s ease;
        }

        #search.closed {
            display: none;
        }

        #map {
            flex: 1;
            z-index: 1000;
        }

        #toggleButton {
            width: 60px;
            height: 60px;
            position: absolute;
            left: 10px;
            top: 66px;
            z-index: 1001;
            font-size: 1.5rem;
            background-color: #6f42c1;
            color: white;
        }

        .search-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .search-header h2 {
            margin: 0;
        }

        .search-results {
            margin-top: 20px;
        }

        .search-results ul {
            list-style: none;
            padding: 0;
        }

        .search-results li {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">상단바</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="#">게시판</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">시간표</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">학점계산기</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">장터</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

    <button id="toggleButton" class="btn btn-primary" onclick="toggleSearch()">☰</button>
    <div id="search">
        <div class="search-header">
            <h2>검색</h2>
            <button class="btn btn-secondary" onclick="toggleSearch()">닫기</button>
        </div>
        <input type="text" id="searchInput" placeholder="검색어를 입력하세요" class="form-control mb-2">
        <button onclick="searchPlaces()" class="btn btn-primary">검색</button>
        <div class="search-results">
            <ul id="results"></ul>
        </div>
    </div>


<div id="map"></div>


<script type="text/javascript"
        src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${NaverClientID}"></script>
<script>
    let map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.556601, 126.919494),
        zoom: 18
    });
    let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(37.556601, 126.919494),
        map: map,
        icon: {
            url: '${pageContext.request.contextPath}/resources/images/map/marker.png',
            size: new naver.maps.Size(48, 48),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 25)
        }
    });

    function toggleSearch() {
        let searchDiv = document.getElementById('search');
        if (searchDiv.classList.contains('closed')) {
            searchDiv.classList.remove('closed');
        } else {
            searchDiv.classList.add('closed');
        }
    }

    function searchPlaces() {

    }


</script>

</body>
</html>