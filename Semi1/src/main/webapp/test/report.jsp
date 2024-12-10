<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trainee Admin - 신고 관리</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", sans-serif;
        }

        body {
            background-color: #f5f5f5;
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* 통계 카드 스타일 */
        .report-summary {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .summary-card {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .summary-title {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }

        .summary-value {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }

        .priority-high {
            color: #ef4444;
        }

        .priority-medium {
            color: #f97316;
        }

        .priority-low {
            color: #22c55e;
        }

        /* 탭 스타일 */
        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            border-bottom: 1px solid #e5e7eb;
            padding-bottom: 10px;
        }

        .tab {
            padding: 8px 16px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            background: none;
            border: none;
            color: #666;
            transition: all 0.2s;
        }

        .tab:hover {
            background: #f3f4f6;
        }

        .tab.active {
            background: #a855f7;
            color: white;
        }

        /* 테이블 스타일 */
        .table-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .table-header {
            padding: 15px 20px;
            border-bottom: 1px solid #e5e7eb;
            background: #f9fafb;
        }

        .table-title {
            font-size: 16px;
            font-weight: 600;
            color: #374151;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px 20px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
        }

        th {
            background: #f9fafb;
            font-weight: 500;
            color: #374151;
            font-size: 14px;
        }

        td {
            font-size: 14px;
            color: #4b5563;
        }

        tr:hover {
            background: #f9fafb;
        }

        /* 상태 뱃지 */
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
        }

        .status-pending {
            background: #fff7ed;
            color: #f97316;
        }

        .status-processing {
            background: #ecfdf5;
            color: #22c55e;
        }

        .status-completed {
            background: #f0f9ff;
            color: #0284c7;
        }

        /* 버튼 스타일 */
        .btn {
            padding: 8px 16px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            border: none;
            background: #a855f7;
            color: white;
            transition: background 0.2s;
        }

        .btn:hover {
            background: #9333ea;
        }

        /* 모달 스타일 */
        .modal-backdrop {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: none;
        }

        .modal {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: white;
            padding: 20px;
            border-radius: 8px;
            width: 90%;
            max-width: 600px;
            display: none;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #e5e7eb;
        }

        .modal-title {
            font-size: 18px;
            font-weight: bold;
            color: #374151;
        }

        .modal-close {
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: #9ca3af;
        }

        .report-content {
            background: #f9fafb;
            padding: 15px;
            border-radius: 4px;
            margin: 10px 0;
            font-size: 14px;
            color: #4b5563;
        }

        .report-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            justify-content: flex-end;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 style="margin: 20px 0; font-size: 24px; color: #374151;">신고 관리</h1>

        <div class="report-summary">
            <div class="summary-card">
                <div class="summary-title">처리 대기</div>
                <div class="summary-value priority-high">23</div>
            </div>
            <div class="summary-card">
                <div class="summary-title">오늘 접수</div>
                <div class="summary-value priority-medium">15</div>
            </div>
            <div class="summary-card">
                <div class="summary-title">처리 완료</div>
                <div class="summary-value priority-low">45</div>
            </div>
            <div class="summary-card">
                <div class="summary-title">처리율</div>
                <div class="summary-value">89%</div>
            </div>
        </div>

        <div class="tabs">
            <button class="tab active">전체</button>
            <button class="tab">게시글</button>
            <button class="tab">댓글</button>
            <button class="tab">사용자</button>
        </div>

        <div class="table-container">
            <div class="table-header">
                <h2 class="table-title">신고 목록</h2>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>신고 ID</th>
                        <th>유형</th>
                        <th>신고 대상</th>
                        <th>신고자</th>
                        <th>신고 사유</th>
                        <th>신고일</th>
                        <th>상태</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>#12345</td>
                        <td>게시글</td>
                        <td>부적절한 판매 게시글</td>
                        <td>user123</td>
                        <td>스팸/광고</td>
                        <td>2024-12-06</td>
                        <td><span class="status-badge status-pending">대기</span></td>
                        <td>
                            <button class="btn" onclick="openReportDetail()">처리</button>
                        </td>
                    </tr>
                    <tr>
                        <td>#12344</td>
                        <td>댓글</td>
                        <td>욕설 포함 댓글</td>
                        <td>user456</td>
                        <td>욕설/비방</td>
                        <td>2024-12-06</td>
                        <td><span class="status-badge status-processing">처리중</span></td>
                        <td>
                            <button class="btn" onclick="openReportDetail()">처리</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 신고 처리 모달 -->
        <div class="modal-backdrop" id="modalBackdrop"></div>
        <div class="modal" id="reportModal">
            <div class="modal-header">
                <h3 class="modal-title">신고 처리</h3>
                <button class="modal-close" onclick="closeReportDetail()">&times;</button>
            </div>
            <div>
                <h4 style="margin-bottom: 10px; color: #374151;">신고 내용</h4>
                <div class="report-content">
                    부적절한 판매 게시글입니다. 허위 상품 정보를 포함하고 있습니다.
                </div>
                <h4 style="margin: 15px 0 10px; color: #374151;">처리 방법</h4>
                <select style="width: 100%; padding: 8px; border: 1px solid #e5e7eb; border-radius: 4px; margin-bottom: 15px;">
                    <option>게시글 삭제</option>
                    <option>게시글 숨김</option>
                    <option>경고 조치</option>
                    <option>신고 반려</option>
                </select>
                <div class="report-actions">
                    <button class="btn" style="background: #6b7280;" onclick="closeReportDetail()">취소</button>
                    <button class="btn">처리 완료</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        function openReportDetail() {
            document.getElementById('modalBackdrop').style.display = 'block';
            document.getElementById('reportModal').style.display = 'block';
        }

        function closeReportDetail() {
            document.getElementById('modalBackdrop').style.display = 'none';
            document.getElementById('reportModal').style.display = 'none';
        }
    </script>
</body>
</html>