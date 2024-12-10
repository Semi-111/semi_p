<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <meta charset="utf-8">
    <title>카테고리로 장소 검색하기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jquery/jquery-3.6.0.min.js">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js"></script>

</head>
<body>

<script type="text/javascript"
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a1a3d5e557866e58ead34c0df86f677b&libraries=services"></script>
<script>
    var places = new kakao.maps.services.Places();

    var callback = function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            for (var i = 0; i < result.length; i++) {
                console.log(result[i]);
            }
            sendDataToServer(result);
        }
    };

    // 음식점 코드 검색
    places.categorySearch('FD6', callback, {
        radius: 20000, // 20km 반경 내에서 검색
        location: new kakao.maps.LatLng(${lat}, ${lon}),  // 현재 학원 좌표
        page: ${page},
        sort: kakao.maps.services.SortBy.DISTANCE
    });

    function sendDataToServer(data) {
        $.ajax({
            type: "POST",
            url: "/map/db",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(data),
            success: function(response) {
                console.log("Data sent successfully");
                console.log(data)
            },
            error: function(xhr, status, error) {
                console.error("Error sending data: " + error);
            }
        });
    }


</script>

</body>
</html>