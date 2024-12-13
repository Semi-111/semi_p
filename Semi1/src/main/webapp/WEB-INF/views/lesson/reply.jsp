<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">

/* 댓글 css */
.comment-edit {
    margin-top: 10px;
    padding: 10px;
    background: #f9fafb;
    border-radius: 4px;
}

.comment-edit .comment-textarea {
    min-height: 70px;
    margin-bottom: 10px;
}

.comment-edit .comment-actions {
    display: flex;
    gap: 10px;
}

.comments-section {
    background: white;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.comments-header {
    padding: 15px 20px;
    border-bottom: 1px solid #e5e7eb;
    font-weight: bold;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.comments-count {
    color: #a855f7;
}

.comment-write {
    padding: 20px;
    border-bottom: 1px solid #e5e7eb;
}

.comment-textarea {
    width: 100%;
    padding: 12px;
    border: 1px solid #e5e7eb;
    border-radius: 4px;
    resize: none;
    margin-bottom: 10px;
    font-size: 14px;
    min-height: 100px;
}

.comment-list {
    padding: 0 20px;
}

.comment-item {
    padding: 15px 0;
    border-bottom: 1px solid #e5e7eb;
}

.comment-item:last-child {
    border-bottom: none;
}

.comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.comment-author {
    font-weight: 500;
    color: #333;
    font-size: 14px;
}

.comment-date {
    color: #666;
    font-size: 12px;
}

.comment-text {
    color: #4b5563;
    font-size: 14px;
    line-height: 1.5;
}

.comment-actions {
    margin-top: 8px;
    display: flex;
    gap: 15px;
}

.comment-action {
    color: #666;
    font-size: 12px;
    cursor: pointer;
}

.comment-action:hover {
    color: #a855f7;
}

/* 버튼 스타일 - 댓글에서 사용되는 부분만 */
.btn {
    padding: 8px 16px;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 6px;
    border: 1px solid #e5e7eb;
    background: white;
}

.btn-purple {
    background: #a855f7;
    color: white;
    border: none;
}

.btn-purple:hover {
    background: #9333ea;
}
</style>
</head>
<body>
<div class="comments-section">
    <div class="comments-header">
        <span>댓글 <span class="comments-count">0</span></span>
        <button class="btn btn-purple" onclick="listReply(1);">새로고침</button>
    </div>
    <div class="comment-write">
        <textarea class="comment-textarea" placeholder="댓글을 입력하세요"></textarea>
        <button class="btn btn-purple btnSendReply" style="width: 100%">댓글 작성</button>
    </div>
    <div class="comment-list">
        <!-- 댓글은 Ajax로 로드됨 -->
    </div>
</div>
</body>
<script type="text/javascript">
$(function(){
    // 페이지 로딩 시 댓글 목록 불러오기
    listReply(1);
    
    // 댓글 등록
    $(".btnSendReply").click(function(){
        let cm_num = "${dto.cm_num}";
        const $tb = $(this).closest("div").find("textarea");
        let content = $tb.val().trim();
        
        if(! content) {
            alert("내용을 입력하세요!");
            $tb.focus();
            return false;
        }
        
        let url = "${pageContext.request.contextPath}/lessonBoard/insertReply";
        let query = "cm_num=" + cm_num + "&content=" + encodeURIComponent(content);
        
        const fn = function(data){
            if(data.state === "true") {
                $tb.val("");
                listReply(1);
            } else {
                alert("댓글을 추가하지 못했습니다.");
            }
        };
        
        ajaxFun(url, "post", query, "json", fn);
    });
});

// Ajax 공통 함수
function ajaxFun(url, method, query, dataType, fn) {
    $.ajax({
        type: method,
        url: url,
        data: query,
        dataType: dataType,
        success: function(data) {
            fn(data);
        },
        beforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("AJAX", true);
        },
        error: function(jqXHR) {
            if(jqXHR.status === 403) {
                alert("로그인 후 이용 가능합니다.");
                return false;
            } else if(jqXHR.status === 400) {
                alert("요청 처리가 실패했습니다.");
                return false;
            }
            console.log(jqXHR.responseText);
        }
    });
}

// 댓글 리스트 불러오기
function listReply(page) {
    let cm_num = "${dto.cm_num}";
    let url = "${pageContext.request.contextPath}/lessonBoard/listReply";
    let query = "cm_num=" + cm_num + "&pageNo=" + page;
    
    const fn = function(data){
        printReply(data);
    };
    ajaxFun(url, "get", query, "json", fn);
}

// 댓글 출력하기
function printReply(data) {
    let html = "";
    const list = data.list;
    let memberNum = "${sessionScope.member.mb_Num}";
    
    if(data.dataCount === 0) {
        html += "<div class='comment-item'>등록된 댓글이 없습니다.</div>";
    } else {
        for(let item of list) {
            html += '<div class="comment-item" data-num="' + item.co_num + '">';
            html += '    <div class="comment-header">';
            html += '        <span class="comment-author">' + item.nickName + '</span>';
            html += '        <span class="comment-date">' + item.reg_date + '</span>';
            html += '    </div>';
            html += '    <div class="comment-text">' + item.content + '</div>';
            
            if(parseInt(memberNum) === item.mb_num) {
                html += '    <div class="comment-actions">';
                html += '        <span class="comment-action" onclick="updateReplyForm(' + item.co_num + ', \'' + item.content + '\')">수정</span>';
                html += '        <span class="comment-action" onclick="deleteReply(' + item.co_num + ')">삭제</span>';
                html += '    </div>';
            }
            
            html += '</div>';
        }
    }
    
    $(".comment-list").html(html);
    // 댓글 갯수 업데이트
    $(".comments-count").text(data.dataCount);
}

//수정 폼으로 변경하는 함수
function updateReplyForm(co_num, content) {
    const $item = $(".comment-item[data-num='" + co_num + "']");
    
    let html = '<div class="comment-edit">';
    html += '  <textarea class="comment-textarea">' + content + '</textarea>';
    html += '  <div class="comment-actions">';
    html += '    <button class="btn btn-purple" onclick="updateReplySubmit(' + co_num + ')">수정완료</button>';
    html += '    <button class="btn btn-gray" onclick="listReply(1)">취소</button>';
    html += '  </div>';
    html += '</div>';
    
    $item.find(".comment-text, .comment-actions").hide();
    $item.append(html);
}

// 댓글 수정
function updateReplySubmit(co_num) {
    const $item = $(".comment-item[data-num='" + co_num + "']");
    const content = $item.find(".comment-textarea").val().trim();
    
    if(! content) {
        alert("내용을 입력하세요.");
        $item.find(".comment-textarea").focus();
        return false;
    }
    
    let url = "${pageContext.request.contextPath}/lessonBoard/updateReply";
    let query = "co_num=" + co_num + "&content=" + encodeURIComponent(content);
    
    const fn = function(data) {
        if(data.state === "true") {
            listReply(1);
        } else {
            alert("댓글을 수정하지 못했습니다.");
        }
    };
    
    ajaxFun(url, "post", query, "json", fn);
}

// 댓글 삭제
function deleteReply(co_num) {
    if(! confirm("댓글을 삭제하시겠습니까?")) {
        return;
    }
    
    let url = "${pageContext.request.contextPath}/lessonBoard/deleteReply";
    let query = "co_num=" + co_num;
    
    const fn = function(data){
        if(data.state === "true") {
            listReply(1);
        } else {
            alert("댓글을 삭제하지 못했습니다.");
        }
    };
    
    ajaxFun(url, "post", query, "json", fn);
}
</script>
</html>