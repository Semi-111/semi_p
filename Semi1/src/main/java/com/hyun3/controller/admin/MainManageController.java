package com.hyun3.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.hyun3.dao.ReportDAO;
import com.hyun3.domain.ReportDTO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainManageController {

	// 어드민 페이지로 이동
	@RequestMapping(value = "/admin/home/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/home/main");
		return mav;
	}

	// 기존의 두 메소드를 하나로 합침
	@RequestMapping(value = "/admin/home/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("admin/home/report");
	    
	    try {
	        ReportDAO dao = new ReportDAO();
	        List<ReportDTO> list = dao.listReport();
	        
	        // 목록을 모델에 추가
	        mav.addObject("list", list);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return mav;
	}
	
	@RequestMapping(value = "/admin/home/reportInsert", method = RequestMethod.POST)
	public ModelAndView reportInsert(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 신고하기 버튼을 누르면 이곳에서 report 테이블에 데이터를 인서트. 
		// 넘겨받는 파라미터 : 어디게시판인지(lessonNum) ? page, cm_num(게시물번호), 신고사유?, 신고내용?
		ReportDTO dto = new ReportDTO();
		ReportDAO dao = new ReportDAO();
		
		HttpSession session = req.getSession(); // 누가 신고했는지 알기위해서 세션에 저장
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		try {
			 // 신고 데이터 설정
	        dto.setRP_title(req.getParameter("rpTable") + " 게시판 신고");
	        dto.setRP_content(req.getParameter("rpContent"));
	        dto.setRP_reason(req.getParameter("rpReason"));
	        dto.setRP_table(req.getParameter("rpTable"));
	        dto.setRP_url(req.getParameter("rpUrl"));
	        dto.setMb_num(info.getMb_Num());
	        
	        dao.insertReport(dto);
	        state = "true";
			
		} catch (Exception e) {
			state = "false";
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("appliaction/json");
		resp.setCharacterEncoding("uth-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());

		return new ModelAndView("redirect:admin/home/main");
	}

}
