function deleteStudentBoard() {
    if (!confirm('게시글을 삭제하시겠습니까?')) {
        return false;
    }
    location.href = contextPath + '/bbs/studentBoard/delete?type=' + boardType + '&cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}

function updateStudentBoard() {
    if(! confirm('게시글을 수정하시겠습니까?')) {
        return false;
    }
    location.href = contextPath + '/bbs/studentBoard/update?type=' + boardType + '&cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}

function deleteInfoBoard() {
    if (!confirm('게시글을 삭제하시겠습니까?')) {
        return false;
    }
    location.href = contextPath + '/bbs/infoBoard/delete?type=' + boardType + '&cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}

function updateInfoBoard() {
    if (!confirm('게시글을 수정하시겠습니까?')) {
        return false;
    }
    location.href = contextPath + '/bbs/infoBoard/update?type=' + boardType + '&cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}

function updateSecretBoard() {
    if (!confirm('게시글을 수정하시겠습니까?')) {
        return false;
    }

    location.href = contextPath + '/bbs/secretBoard/update?cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}

function deleteSecretBoard() {
    if (!confirm('게시글을 삭제하시겠습니까?')) {
        return false;
    }

    location.href = contextPath + '/bbs/secretBoard/delete?cmNum=' + encodeURIComponent(cmNum) + '&page=' + encodeURIComponent(page);
}


function ajaxFun(url, method, formData, dataType, fn, file = false) {
    const settings = {
        type: method,
        data: formData,
        dataType: dataType,
        success: function (data) {
            fn(data);
        },
        beforeSend: function (jqXHR) {
            jqXHR.setRequestHeader('AJAX', true);
        },
        error: function (jqXHR) {
            if (jqXHR.status === 403) {
                alert('로그인이 필요합니다.');
                return false;
            } else if (jqXHR.status === 406) {
                alert('요청 처리가 실패했습니다.');
                return false;
            }
            console.log(jqXHR.responseText);
        }
    };

    if (file) {
        settings.processData = false;
        settings.contentType = false;
    }

    $.ajax(url, settings);
}

