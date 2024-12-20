
function mapMove() {

    let url = path + `/map/moveP`;
    let pageElement = document.getElementById('page');
    let page = pageElement ? pageElement.value : null;

    if (page === null) {
        console.error("Page element not found");
        return;
    }

    let query = 'page=' + page + "&lat1=" + lat1 + "&lat2=" + lat2 + "&lon1=" + lon1 + "&lon2=" + lon2;

    let schTerm = $("#schTerm").val();
    console.log(schTerm);

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

$(function () {
    loadMoreReviews();
});


function loadMoreReviews() {

    page++;
    let url = path + '/map/blog';
    let stId = document.getElementById('stId').value;
    let query = 'stId=' + stId + '&page=' + page;

    const fn = function(data) {
        addNewReviews(data);
    };

    ajaxFun(url, 'get', query, 'json', fn);
}

function addNewReviews(data) {
    let blogReviewsNode = document.querySelector('#blog-reviews .reviews-container');
    let dataCount = parseInt(data.dataCount);
    let pageNo = parseInt(data.pageNo);
    let total_page = parseInt(data.total_page);

    if (dataCount === 0) {
        return;
    }

    let htmlText;
    for (let review of data.list) {
        htmlText = '<a href='+review.blogUrl+'>';
        htmlText += '<div class="blog-review">';
        htmlText += '  <h3>' + review.blogTitle + '</h3>';
        htmlText += '  <p>' + review.blogContent + '</p>';
        htmlText += '  <p><strong>작성자:</strong> ' + review.blogName + '</p>';
        htmlText += '  <p><strong>작성일:</strong> ' + review.blogDate + '</p>';
        htmlText += '</div>';
        htmlText += '</a>';
        htmlText += '<br> <hr>';

        blogReviewsNode.insertAdjacentHTML('beforeend', htmlText);
    }

    if (pageNo >= total_page) {
        document.getElementById('loadMoreButton').style.display = 'none';
    }
}


function loadMoreImages() {

    page++;
    let url = path + '/map/images';
    let stId = document.getElementById('stId').value;
    let query = 'stId=' + stId + '&page=' + page;

    const fn = function(data) {
        addNewImages(data);
    };

    ajaxFun(url, 'get', query, 'json', fn);
}

function addNewImages(data) {
    let imagesContainer = document.querySelector('#images-container');
    if (!imagesContainer) {
        console.error("Images container not found");
        return;
    }

    let dataCount = parseInt(data.dataCount);
    let pageNo = parseInt(data.pageNo);
    let total_page = parseInt(data.total_page);

    if (dataCount === 0) {
        return;
    }
        let htmlText;
        for (let img of data.list) {
        htmlText = '<a href='+img.imgUrl+'>';
        htmlText += '<div class="detail-thumbnail-images">';
        htmlText += '  <img src="' + img.thumbnail + '" alt="' + img.imgAlt + '" class="img-1">';
        htmlText += '</div>';
        htmlText += '</a>';
        imagesContainer.insertAdjacentHTML('beforeend', htmlText);
    }

        if (pageNo >= total_page) {
        document.getElementById('loadMoreImagesButton').style.display = 'none';
    }
        }


