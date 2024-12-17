// 페이지 로드시 contextPath 전역변수 설정
window.addEventListener('DOMContentLoaded', function() {
   // article.jsp에서 설정한 contextPath 사용 
   console.log("=== Context Path Debug ===");
   console.log("초기 contextPath:", window.contextPath);
   
   if(!window.contextPath) {
       window.contextPath = '';
       console.log("기본값으로 설정된 contextPath:", window.contextPath);
   }

   // 신고 모달 HTML을 body에 추가
   const modalHtml = `
       <div id="reportModal" class="modal" style="display:none;">
           <div class="modal-content">
               <h3>신고하기</h3>
               <span class="close">&times;</span>
               <form name="reportForm">
			  	   <input type="hidden" name="rpTable" id="rpTable">
			       <input type="hidden" name="rpTitle" id="rpTitle">
			       <input type="hidden" name="rpTargetNum" id="rpTargetNum">     <!-- 추가 -->
			       <input type="hidden" name="rpTargetMbNum" id="rpTargetMbNum"> <!-- 추가 -->
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
});

// 신고 버튼 클릭 이벤트 
$(document).on("click", ".btnReport", function(){
	let table = $(this).data("table");
	let targetNum = $(this).data("num");      // 게시글 번호
	let targetMbNum = $(this).data("mb-num"); // 작성자 번호  
	let title = $(this).data("title");

   $("#rpTable").val(table);
   $("#rpTitle").val(title);
   $("#rpTargetNum").val(targetNum);        
   $("#rpTargetMbNum").val(targetMbNum);     

   document.getElementById('reportModal').style.display = 'block';
});

// 신고하기 버튼 클릭
$(document).on("click", ".btnSendReport", function() {
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

   let query = {
       rpTitle:  f.rpTitle.value,
       rpTable: f.rpTable.value,
       rpReason: f.rpReason.value,
       rpContent: f.rpContent.value,
	   rpTargetNum: f.rpTargetNum.value,     // 추가
	   rpTargetMbNum: f.rpTargetMbNum.value  // 추가
   };

   console.log("=== Report Send Debug ===");
   console.log("전송되는 데이터:", query);  // 디버깅용
   console.log("form 데이터:", query);

   $.ajax({
       type: "POST",
       url: window.contextPath + "/report/insert",
       data: query,
       dataType: "json",
       beforeSend: function(jqXHR) {
           jqXHR.setRequestHeader("AJAX", true);
       },
       success: function(data) {
           console.log("응답 데이터:", data);
           if(data.state === "true") {
               alert(data.message);
               closeModal();
           } else {
               alert(data.message || "신고 처리 중 오류가 발생했습니다.");
           }
       },
       error: function(jqXHR) {
           console.log("에러 상태:", jqXHR.status);
           console.log("에러 응답:", jqXHR.responseText);
           if(jqXHR.status === 403) {
               location.href = window.contextPath + "/member/login";
           } else {
               alert("신고 처리 중 오류가 발생했습니다.");
           }
       }
   });
}); 

// 모달 닫기
function closeModal() {
   document.getElementById('reportModal').style.display = 'none';
   document.forms.reportForm.reset();
}

// 모달 외부 클릭시 닫기
window.onclick = function(event) {
   let modal = document.getElementById('reportModal');
   if (event.target == modal) {
       closeModal();
   }
}

// 모달 닫기 버튼 클릭
$(document).on("click", ".close", function(){
   closeModal();
});

// 모달 ESC 키 눌렀을 때 닫기 
$(document).keydown(function(e) {
   if (e.keyCode == 27) { // ESC 키
       closeModal();
   }
});