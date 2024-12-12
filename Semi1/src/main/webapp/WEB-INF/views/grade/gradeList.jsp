<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="vo" items="${gradeList}">
	<tr>
		<td class="sbname-input"> ${vo.sb_Name} </td>
		<td class="credit-input"> ${vo.hakscore} </td>
		<td class="grade-input"> ${vo.grade} </td>
	</tr>
</c:forEach>