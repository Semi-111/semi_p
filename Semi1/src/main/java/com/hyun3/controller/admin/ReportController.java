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
			ReportDAO dao = new ReportDAO();

			dto.setRP_title(req.getParameter("rpTitle"));
			dto.setRP_content(req.getParameter("rpContent"));
			dto.setRP_reason(req.getParameter("rpReason"));
			dto.setRP_table(req.getParameter("rpTable"));
			dto.setRP_url(req.getParameter("rpUrl"));
			dto.setMb_num(info.getMb_Num());

			dao.insertReport(dto);

			model.put("state", "true");
			model.put("message", "신고가 접수되었습니다.");

		} catch (Exception e) {
			System.out.println("Controller Error: " + e.getMessage());
			e.printStackTrace();
			model.put("state", "false");
			model.put("message", "신고 처리 중 오류가 발생했습니다.");
		}

		return model;
	}
}
