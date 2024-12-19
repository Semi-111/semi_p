<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="vo" items="${gradeList}">
	<tr data-atnum="${vo.at_Num}">
		<td class="sbname-input"> ${vo.sb_Name} </td>
		<td class="credit-input"> ${vo.hakscore} </td>
		<td class="grade-input"> 
			<select name="grade" class="grade-select" data-selected="${vo.grade}">
				<option value="A+" ${vo.grade == "A+" ? "selected" : ""}>A+</option>
				<option value="A0" ${vo.grade == "A0" ? "selected" : ""}>A0</option>
				<option value="B+" ${vo.grade == "B+" ? "selected" : ""}>B+</option>
				<option value="B0" ${vo.grade == "B0" ? "selected" : ""}>B0</option>
				<option value="C+" ${vo.grade == "C+" ? "selected" : ""}>C+</option>
				<option value="C0" ${vo.grade == "C0" ? "selected" : ""}>C0</option>
				<option value="D+" ${vo.grade == "D+" ? "selected" : ""}>D+</option>
				<option value="D0" ${vo.grade == "D0" ? "selected" : ""}>D0</option>
				<option value="F" ${vo.grade == "F" ? "selected" : ""}>F</option>
			</select>
		</td>
	</tr>
</c:forEach>