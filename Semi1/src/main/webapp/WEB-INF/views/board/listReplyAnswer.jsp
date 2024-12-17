<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:forEach var="vo" items="${listReplyAnswer}">
	<div class="border-bottom m-1">
		<div class="row p-1">
			<div class="col-auto">
				<div class="reply-writer">
					<div class="name">${vo.nickName}</div>
				</div>
			</div>
			<div class="col align-self-center text-end">
				<span>${vo.reg_date}</span> |
				<c:choose>
					<c:when test="${vo.mbNum == sessionScope.member.mbNum || sessionScope.member.role eq '60' }">
						<span class="deleteReplyAnswer" data-replyNum="${vo.coNum}" data-parentNum="${vo.cmNum}">삭제</span>
					</c:when>
					<c:otherwise>
						<span class="notifyReplyAnswer" data-replyNum="${vo.coNum}" data-parentNum="${vo.cmNum}">신고</span>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div class="p-2">
			${vo.content}
		</div>
	</div>
</c:forEach>
