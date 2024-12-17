package com.hyun3.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.hyun3.dao.ReportDAO;
import com.hyun3.domain.ReportDTO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {

	// 신고 등록
	@ResponseBody
	@RequestMapping(value = "/report/insert", method = RequestMethod.POST)
	public Map<String, Object> reportInsert(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info == null) {
				model.put("state", "false");
				model.put("message", "로그인이 필요합니다.");
				return model;
			}

			ReportDTO dto = new ReportDTO();
			dto.setRP_title(req.getParameter("rpTitle"));
			dto.setRP_content(req.getParameter("rpContent"));
			dto.setRP_reason(req.getParameter("rpReason"));
			dto.setRP_table(req.getParameter("rpTable"));
			dto.setMb_num(info.getMb_Num()); // 신고자 번호

			// 추가된 필드들
			dto.setTargetNum(Long.parseLong(req.getParameter("rpTargetNum"))); // 신고된 글번호
			dto.setTargetMbNum(Long.parseLong(req.getParameter("rpTargetMbNum"))); // 신고된 작성자번호

			ReportDAO dao = new ReportDAO();
			dao.insertReport(dto);

			model.put("state", "true");
			model.put("message", "신고가 접수되었습니다.");

		} catch (NumberFormatException e) {
	        model.put("state", "false");
	        model.put("message", "잘못된 데이터 형식입니다.");
	        e.printStackTrace();
	    } catch (Exception e) {
	        model.put("state", "false");
	        model.put("message", "신고 처리 중 오류가 발생했습니다.");
	        e.printStackTrace();
	    }

		return model;
	}
}
