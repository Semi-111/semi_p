<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${dto.title} - Trainee</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", sans-serif;
        }

        body {
            background-color: #f5f5f5;
        }

        /* 게시글 컨테이너 */
        .post-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        /* 게시글 상세 */
        .post-detail {
            background: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .post-header {
            padding: 20px;
            border-bottom: 1px solid #e5e7eb;
        }

        .post-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 15px;
        }

        .post-meta {
            display: flex;
            justify-content: space-between;
            color: #666;
            font-size: 14px;
            flex-wrap: wrap;
            gap: 10px;
        }

        .post-info {
            display: flex;
            gap: 15px;
        }

        .post-content {
            padding: 30px 20px;
            line-height: 1.6;
            color: #333;
            font-size: 15px;
        }

        /* 버튼 스타일 */
        .post-actions {
            padding: 15px 20px;
            border-top: 1px solid #e5e7eb;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

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

        .btn-gray {
            color: #666;
        }

        .btn-gray:hover {
            background: #f9fafb;
        }

        .btn-red {
            color: #ef4444;
            border-color: #ef4444;
        }

        .btn-red:hover {
            background: #fef2f2;
        }

        /* 이전글/다음글 네비게이션 */
        .post-navigation {
            background: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-top: 20px;
        }

        .post-navigation-item {
            padding: 15px 20px;
            display: flex;
            align-items: center;
            border-bottom: 1px solid #e5e7eb;
        }

        .post-navigation-item:last-child {
            border-bottom: none;
        }

        .post-navigation-label {
            color: #666;
            font-size: 14px;
            min-width: 60px;
        }

        .post-navigation-link {
            text-decoration: none;
            color: #333;
            font-size: 14px;
            margin-left: 15px;
        }

        .post-navigation-link:hover {
            color: #a855f7;
        }

        /* 이미지 박스 */
        .img-box {
            max-width: 100%;
            margin: 20px 0;
        }

        .img-box img {
            max-width: 100%;
            height: auto;
        }

        /* 파일 다운로드 링크 */
        .file-link {
            display: inline-block;
            margin: 10px 0;
            padding: 8px 12px;
            background-color: #f3f4f6;
            border-radius: 4px;
            color: #666;
            text-decoration: none;
            font-size: 14px;
        }

        .file-link:hover {
            background-color: #e5e7eb;
            color: #a855f7;
        }

        /* 목록으로 링크 */
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #666;
            text-decoration: none;
            font-size: 14px;
        }

        .back-link:hover {
            color: #a855f7;
        }
    </style>
    
    <script type="text/javascript">
        function deleteBoard() {
            if(confirm("게시글을 삭제하시겠습니까?")) {
                let query = "cm_num=${dto.cm_num}&${query}";
                let url = "${pageContext.request.contextPath}/lessonBoard/delete?" + query;
                location.href = url;
            }
        }
    </script>
</head>
<body>
    <div class="post-container">
        <a href="${pageContext.request.contextPath}/lessonBoard/list?${query}" class="back-link">
            ← 목록으로
        </a>

        <div class="post-detail">
            <div class="post-header">
                <h1 class="post-title">${dto.title}</h1>
                <div class="post-meta">
                    <div class="post-info">
                        <span>작성자: ${dto.nickName}</span>
                        <span>학과: ${dto.lessonName}</span>
                        <span>작성일: ${dto.ca_date}</span>
                    </div>
                    <div class="post-info">
                        <span>조회 ${dto.views}</span>
                    </div>
                </div>
            </div>

            <div class="post-content">
                ${dto.board_content}
                
                <c:if test="${not empty dto.fileName}">
                    <div class="img-box">
                        <img src="${pageContext.request.contextPath}/uploads/photo/${dto.fileName}" 
                             alt="첨부 이미지">
                    </div>
                    <a href="${pageContext.request.contextPath}/lessonBoard/download?cm_num=${dto.cm_num}" 
                       class="file-link">
                        <i class="bi bi-download"></i> ${dto.fileName}
                    </a>
                </c:if>
            </div>

            <div class="post-actions">
                <c:if test="${sessionScope.member.mb_Num==dto.mb_num || sessionScope.member.role >= 51}">
                    <button class="btn btn-purple" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/update?cm_num=${dto.cm_num}&page=${page}';">
                        수정
                    </button>
                    <button class="btn btn-red" onclick="deleteBoard();">
                        삭제
                    </button>
                </c:if>
                <button class="btn btn-gray" onclick="location.href='${pageContext.request.contextPath}/lessonBoard/list?${query}';">
                    목록
                </button>
            </div>
        </div>

        <!-- 댓글 영역 include -->
        <jsp:include page="reply.jsp"/>

        <!-- 이전글/다음글 -->
        <div class="post-navigation">
            <c:if test="${not empty prevDto}">
                <div class="post-navigation-item">
                    <span class="post-navigation-label">이전글</span>
                    <i class="bi bi-chevron-up"></i>
                    <a class="post-navigation-link" href="${pageContext.request.contextPath}/lessonBoard/article?${query}&cm_num=${prevDto.cm_num}">
                        ${prevDto.title}
                    </a>
                </div>
            </c:if>
            <c:if test="${not empty nextDto}">
                <div class="post-navigation-item">
                    <span class="post-navigation-label">다음글</span>
                    <i class="bi bi-chevron-down"></i>
                    <a class="post-navigation-link" href="${pageContext.request.contextPath}/lessonBoard/article?${query}&cm_num=${nextDto.cm_num}">
                        ${nextDto.title}
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>