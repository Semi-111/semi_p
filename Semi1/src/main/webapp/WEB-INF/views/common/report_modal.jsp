<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- common/report_modal.jsp -->
<div id="reportModal" class="modal" style="display: none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>신고하기</h3>
            <button type="button" class="close" onclick="closeReportModal()">&times;</button>
        </div>
        <div class="modal-body">
            <form name="reportForm">
                <input type="hidden" name="rpTable" id="rpTable">
                <input type="hidden" name="rpUrl" id="rpUrl">
                
                <div class="form-group">
                    <label>신고 사유</label>
                    <select name="rpReason" class="form-select">
                        <option value="음란물">음란물</option>
                        <option value="욕설">욕설</option>
                        <option value="광고">광고/스팸</option>
                        <option value="사기">사기 의심</option>
                        <option value="기타">기타</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>상세 내용</label>
                    <textarea name="rpContent" class="form-control" rows="4"></textarea>
                </div>
                <div class="text-right">
                    <button type="button" class="btn btn-light" onclick="closeReportModal()">취소</button>
                    <button type="button" class="btn btn-danger" onclick="sendReport()">신고하기</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>