<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<div id="search">
    <form id="searchForm" onsubmit="return searchPlaces()">
        <div class="search-input-group">
            <input type="text" id="schTerm" placeholder="검색어를 입력하세요" class="form-control mb-2">
            <button class="btn btn-primary">검색</button>
        </div>
    </form>
    <div class="search-header">
        <button class="btn" onclick="toggleSearchSideBar()" id="left-btn">
            <img src="${pageContext.request.contextPath}/resources/images/map/left.png" alt="">
        </button>
    </div>
    <div>
        <c:if test="${schTerm != '' && schTerm != 'none' && schTerm != null}">
            <span>
                <span style="font-weight: 600; font-size: 19px">
                    ${schTerm}
                </span>
                에 대한 검색결과
            </span>
        </c:if>
        <p style="font-weight: 500; font-size: 16px">
            총 ${dataCount}개의 검색결과가 있습니다.
        </p>
    </div>

    <c:forEach var="marker" items="${markersData}">
        <hr>
        <div class="search-results" id="storeDetails-${marker.stId}" onclick="loadDetails(${marker.stId})">
            <a href="#">
                <c:if test="${marker.divisionCode != null}">
                    <img src="${marker.divisionCode}" alt="" id="storeImg-${marker.stId}">
                </c:if>
            </a>
            <a href="#">
                <h3 id="storeName-${marker.stId}">${marker.stName}</h3>
                <p id="storeAddress-${marker.stId}">${marker.address}</p>
                <p id="storePhone-${marker.stId}">${marker.tel == null ? "가게 전화번호가 없음" : marker.tel} </p>
            </a>
            <input type="hidden" id="storeID" value="${marker.stId}">
        </div>
    </c:forEach>
    <div class="page-navigation">
        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
    </div>
    <input type="hidden" id="page" value="${page}">
</div>