// /resources/js/report.js
$(function(){
    // 신고 모달 HTML을 body에 추가
    const modalHtml = `
        <div id="reportModal" class="modal" style="display:none;">
            <div class="modal-content">
                <h3>신고하기</h3>
                <span class="close">&times;</span>
                <form name="reportForm">
                    <input type="hidden" name="rpTable" id="rpTable">
                    <input type="hidden" name="rpUrl" id="rpUrl">
                    <div class="form-group">
                        <label>신고 사유</label>
                        <select name="rpReason" class="form-select">
                            <option value="">신고 사유 선택</option>
                            <option value="음란물">음란물</option>
                            <option value="욕설">욕설</option>
                            <option value="광고">광고/스팸</option>
                            <option value="기타">기타</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>상세 내용</label>
                        <textarea name="rpContent" rows="4" class="form-control"></textarea>
                    </div>
                    <div>
                        <button type="button" class="btn btn-light" onclick="closeModal()">취소</button>
                        <button type="button" class="btn btn-danger btnSendReport">신고하기</button>
                    </div>
                </form>
            </div>
        </div>
    `;

    // 페이지 로드시 모달 HTML 추가
    if(!document.getElementById('reportModal')) {
        document.body.insertAdjacentHTML('beforeend', modalHtml);
    }

    // 신고 버튼 클릭 이벤트
    $("body").on("click", ".btnReport", function(){
        let table = $(this).data("table");
        let url = $(this).data("url");
        let title = $(this).data("title");
        
        $("#rpTable").val(table);
        $("#rpUrl").val(url);
        $("textarea[name='rpContent']").val("신고 게시글: " + title + "\n신고 사유: ");
        
        document.getElementById('reportModal').style.display = 'block';
    });

    // 신고하기 버튼 클릭
    $("body").on("click", ".btnSendReport", function(){
        const f = document.forms.reportForm;
        
        if(!f.rpReason.value.trim()) {
            alert("신고 사유를 선택하세요.");
            f.rpReason.focus();
            return;
        }
        
        if(!f.rpContent.value.trim()) {
            alert("신고 내용을 입력하세요.");
            f.rpContent.focus();
            return;
        }
        
        let url = "/report/submit";
        let query = $(f).serialize();
        
        const fn = function(data){
            if(data.state === "true") {
                alert("신고가 접수되었습니다.");
                closeModal();
            } else {
                alert("신고 접수에 실패했습니다.");
            }
        };
        
        ajaxFun(url, "POST", query, "json", fn);
    });
});

// 모달 닫기
function closeModal() {
    document.getElementById('reportModal').style.display = 'none';
}

// 모달 외부 클릭시 닫기
window.onclick = function(event) {
    let modal = document.getElementById('reportModal');
    if (event.target == modal) {
        closeModal();
    }
}