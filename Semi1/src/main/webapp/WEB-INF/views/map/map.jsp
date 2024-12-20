<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Naver Map Example</title>
    <link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/css/bootstrap.min.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap5/icon/bootstrap-icons.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/core2.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/map/layout.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/paginate.css" type="text/css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.6.0/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/map/map.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/map/mapDetail.css">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
    <script type="text/javascript"
            src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${NaverClientID}"></script>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>

<div id="mapContainer">


</div>

<button id="toggleButton" class="btn btn-primary" onclick="toggleSearchSideBar()">☰</button>


<div id="map" style="width:100%;height:400px;"></div>

<button id="refreshButton" class="btn btn-primary button1" onclick="refreshMap()">
    <img src="${pageContext.request.contextPath}/resources/images/map/refresh.png" alt="">
    현 위치에서 검색
</button>

<script>

    function ajaxFun(url, method, query, dataType, fn) {
        $.ajax({
            type: method,
            url: url,
            data: query,
            dataType: dataType,
            success: function (data) {
                fn(data);
            },
            beforeSend: function (jqXHR) {
                jqXHR.setRequestHeader("AJAX", true);
            },
            error: function (jqXHR) {
                if (jqXHR.status === 403) {
                    login();
                    return false;
                } else if (jqXHR.status === 400) {
                    alert("요청 처리가 실패했습니다.");
                    return false;
                }
                console.log(jqXHR.responseText);
            }
        });
    }

    let lat1 = ${lat1};
    let lat2 = ${lat2};
    let lon1 = ${lon1};
    let lon2 = ${lon2};

    let centerLat = (lat1 + lat2) / 2;
    let centerLon = (lon1 + lon2) / 2;

    let map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(centerLat, centerLon),
        zoom: 19
    });

    let markersData = '${markersDataJS}';
    let path = '${pageContext.request.contextPath}';
    if (markersData) {
        markersData = JSON.parse(markersData);
    }

    let schTerm = '${schTerm}';
    let pathContext  = '${pageContext.request.contextPath}';

</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/map/map.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/map/mapMarker.js"></script>


</body>
</html>