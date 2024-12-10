<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trainee Admin - 회원 관리</title>
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

        /* 기본 컨테이너 스타일 */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* 검색 영역 */
        .search-area {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .search-row {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-bottom: 10px;
        }

        /* 필터 및 검색 요소 */
        select, input[type="text"] {
            padding: 8px 12px;
            border: 1px solid #e1e1e1;
            border-radius: 4px;
            font-size: 14px;
            min-width: 150px;
        }

        input[type="text"] {
            flex: 1;
            min-width: 200px;
        }

        /* 버튼 스타일 */
        .btn {
            padding: 8px 16px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            border: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .btn-primary {
            background: #a855f7;
            color: white;
        }

        .btn-primary:hover {
            background: #9333ea;
        }

        .btn-success {
            background: #22c55e;
            color: white;
        }

        .btn-danger {
            background: #ef4444;
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
            background: #f9fafb;
            border-bottom: 1px solid #e5e7eb;
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

        .status-active {
            background: #dcfce7;
            color: #16a34a;
        }

        .status-inactive {
            background: #fee2e2;
            color: #dc2626;
        }

        /* 페이지네이션 */
        .pagination {
            display: flex;
            justify-content: center;
            gap: 5px;
            padding: 20px;
        }

        .pagination button {
            padding: 8px 12px;
            border: 1px solid #e5e7eb;
            background: white;
            color: #374151;
            border-radius: 4px;
            cursor: pointer;
        }

        .pagination button.active {
            background: #a855f7;
            color: white;
            border-color: #a855f7;
        }

        /* 체크박스 스타일 */
        .custom-checkbox {
            width: 16px;
            height: 16px;
            cursor: pointer;
        }

        /* 액션 버튼 영역 */
        .action-buttons {
            padding: 15px 20px;
            border-bottom: 1px solid #e5e7eb;
            display: flex;
            gap: 10px;
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
    </style>
</head>
<body>
    <div class="container">
        <h1 style="margin: 20px 0; font-size: 24px; color: #374151;">회원 관리</h1>

        <div class="search-area">
            <div class="search-row">
                <select>
                    <option>전체 회원</option>
                    <option>활성 회원</option>
                    <option>정지된 회원</option>
                    <option>탈퇴 회원</option>
                </select>
                <input type="text" placeholder="이름, 이메일, 아이디 검색">
                <button class="btn btn-primary">검색</button>
                <button class="btn btn-success">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M7 10l5 5 5-5M12 15V3"/>
                    </svg>
                    엑셀 내보내기
                </button>
            </div>
        </div>

        <div class="table-container">
            <div class="action-buttons">
                <button class="btn btn-primary">선택 활성화</button>
                <button class="btn btn-primary">선택 정지</button>
                <button class="btn btn-danger">선택 삭제</button>
            </div>

            <table>
                <thead>
                    <tr>
                        <th><input type="checkbox" class="custom-checkbox"></th>
                        <th>회원ID</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>가입일</th>
                        <th>최근 접속일</th>
                        <th>상태</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><input type="checkbox" class="custom-checkbox"></td>
                        <td>#12345</td>
                        <td>김트레이니</td>
                        <td>kim@trainee.com</td>
                        <td>2024-12-01</td>
                        <td>2024-12-06</td>
                        <td><span class="status-badge status-active">활성</span></td>
                        <td>
                            <button class="btn btn-primary" onclick="openMemberModal()">상세</button>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" class="custom-checkbox"></td>
                        <td>#12344</td>
                        <td>이트레이니</td>
                        <td>lee@trainee.com</td>
                        <td>2024-12-01</td>
                        <td>2024-12-05</td>
                        <td><span class="status-badge status-inactive">정지</span></td>
                        <td>
                            <button class="btn btn-primary" onclick="openMemberModal()">상세</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="pagination">
                <button>이전</button>
                <button class="active">1</button>
                <button>2</button>
                <button>3</button>
                <button>다음</button>
            </div>
        </div>

        <!-- 모달 -->
        <div class="modal-backdrop" id="modalBackdrop"></div>
        <div class="modal" id="memberModal">
            <div class="modal-header">
                <h3 class="modal-title">회원 상세 정보</h3>
                <button class="modal-close" onclick="closeMemberModal()">&times;</button>
            </div>
            <div class="member-details" style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 20px;">
                <div class="detail-item">
                    <div style="color: #666; font-size: 12px; margin-bottom: 4px;">회원 ID</div>
                    <div style="font-size: 14px;">#12345</div>
                </div>
                <div class="detail-item">
                    <div style="color: #666; font-size: 12px; margin-bottom: 4px;">이름</div>
                    <div style="font-size: 14px;">김트레이니</div>
                </div>
                <div class="detail-item">
                    <div style="color: #666; font-size: 12px; margin-bottom: 4px;">이메일</div>
                    <div style="font-size: 14px;">kim@trainee.com</div>
                </div>
                <div class="detail-item">
                    <div style="color: #666; font-size: 12px; margin-bottom: 4px;">가입일</div>
                    <div style="font-size: 14px;">2024-12-01</div>
                </div>
            </div>
            <div style="text-align: right; padding-top: 15px; border-top: 1px solid #e5e7eb;">
                <button class="btn btn-primary">활성화</button>
                <button class="btn btn-danger">정지</button>
            </div>
        </div>
    </div>

    <script>
        function openMemberModal() {
            document.getElementById('modalBackdrop').style.display = 'block';
            document.getElementById('memberModal').style.display = 'block';
        }

        function closeMemberModal() {
            document.getElementById('modalBackdrop').style.display = 'none';
            document.getElementById('memberModal').style.display = 'none';
        }
    </script>
</body>
</html>