

let markers = [];
let infoWindows = [];




function toggleSearchSideBar() {
    let searchDiv = document.getElementById('search');
    if (searchDiv.classList.contains('close')) {
        searchDiv.classList.remove('close');
    } else {
        searchDiv.classList.add('close');
    }
}


$(function (){
    mapPage2(1, schTerm);
})

function mapPage2(page,schTerm) {

    let url = path + `/map/moveP`;

    let query = 'page=' + page + "&lat1=" + lat1 + "&lat2=" + lat2 + "&lon1=" + lon1 + "&lon2=" + lon2;

    if (schTerm != null && schTerm !== "none") {
        schTerm = encodeURIComponent(schTerm);
        query += "&schTerm=" + schTerm;
    }

    let search = "#mapContainer";

    const fn = function (data) {
        $(search).html(data);
    }

    ajaxFun(url, 'GET', query, 'text', fn);
}


function mapPage(page) {

    let url = path + `/map/moveP`;

    let query = 'page=' + page + "&lat1=" + lat1 + "&lat2=" + lat2 + "&lon1=" + lon1 + "&lon2=" + lon2;

    if (schTerm != null && schTerm !== "none") {
        schTerm = encodeURIComponent(schTerm);
        query += "&schTerm=" + schTerm;
    }

    let search = "#mapContainer";

    const fn = function (data) {
        $(search).html(data);
    }

    ajaxFun(url, 'GET', query, 'text', fn);
}






function loadDetails(stId) {
    let url = path + '/map/details';
    let query = 'stId=' + stId;
    let page = document.getElementById('page').value;

    query += "&page=" + page;
    if (schTerm != null && schTerm !== "none") {
        schTerm = encodeURIComponent(schTerm);
        query += "&schTerm=" + schTerm;
    }

    let detail = "#mapContainer";

    const fn = function (data) {
        $(detail).html(data);
    }

    ajaxFun(url, 'GET', query, 'text', fn);

}


// TODO : 검색시 마커 초기화 후 새로운 마커 생성
function searchPlaces() {
    let schTerm = document.getElementById('schTerm').value;
    schTerm = encodeURIComponent(schTerm);
    if (schTerm.trim() === '') {
        alert('검색어를 입력하세요');
        return false;
    }


    const mainUrl = path + '/map';
    const query = '?schTerm=' + schTerm + "&lat1=" + lat1 + "&lat2=" + lat2 + "&lon1=" + lon1 + "&lon2=" + lon2;

    window.location.href = mainUrl+query;

    return false;
}




