<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="detail-container" id="search">
    <!-- 이미지와 설명 영역 -->
    <button class="btn" onclick="mapMove()" id="back">
        <img src="${pageContext.request.contextPath}/resources/images/map/left.png" alt="">
    </button>
    <button class="btn" onclick="toggleSearchSideBar()" id="left-btn">
        <img src="${pageContext.request.contextPath}/resources/images/map/left.png" alt="">
    </button>
    <div class="detail-image-section">
        <c:forEach var="img" items="${img}">
            <div class="detail-thumbnail-images">
                <img src="${img.thumbnail}" alt="" class="img-1">
            </div>
        </c:forEach>
    </div>

    <!-- 상세 정보 -->
    <div class="detail-details-section">
        <h1>${st_name}</h1>
        <p>${category}</p>
        <p>블로그 리뷰 ${blog_cnt}</p>

        <!-- 주소 및 기타 정보 -->
        <div class="detail-info">
            <p><strong>주소:</strong> ${address}</p>
            <p><strong>전화:</strong> ${phone == null ? "가게 전화번호가 없음" : phone}</p>
        </div>
    </div>

    <!-- 탭 메뉴 -->
    <div class="tab-menu">
        <button  class="review-btn tab-link active" onclick="openTab(event, 'blog-reviews')">블로그 리뷰</button>
        <button class="review-btn tab-link" onclick="openTab(event, 'images')">이미지</button>
    </div>

    <!-- 블로그 리뷰 섹션 -->
    <div id="blog-reviews" class="tab-content" style="display: block;">
        <div class="reviews-container">

        </div>
        <div class="sentinel" data-loading="false"></div>
        <div class="button-container">
            <button id="loadMoreButton" class="btn btn-primary" onclick="loadMoreReviews()">더보기</button>
        </div>
    </div>

    <!-- 이미지 섹션 -->
    <div id="images" class="tab-content" style="display: none;">
        <div id="images-container" class="images-container">

        </div>
        <div class="sentinel" data-loading="false"></div>
        <div class="button-container">
            <button id="loadMoreImagesButton" class="btn btn-primary" onclick="loadMoreImages()">더보기</button>
        </div>
    </div>

    <input type="hidden" id="schTerm" value="${schTerm}">
    <input type="hidden" id="page" value="${page}">
<%--    <input type="hidden" id="reviewPage" value="${reviewPage}">--%>
    <input type="hidden" id="stId" value="${st_id}">
</div>

<script src="${pageContext.request.contextPath}/resources/js/map/mapDetail.js"></script>
<script>

    let page =0;

    function openTab(evt, tabName) {
        var i, tabcontent, tablinks;
        tabcontent = document.getElementsByClassName("tab-content");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }
        tablinks = document.getElementsByClassName("tab-link");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className += " active";

        // 데이터 초기화
        if (tabName === 'blog-reviews') {
            page = 1;
            document.querySelector('#images-container').innerHTML = '';
            loadMoreReviews();
        } else if (tabName === 'images') {
            page = 1;
            document.querySelector('#blog-reviews .reviews-container').innerHTML = '';
            loadMoreImages();
        }
    }
</script>