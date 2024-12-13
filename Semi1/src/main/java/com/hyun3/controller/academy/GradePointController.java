package com.hyun3.controller.academy;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyun3.dao.academy.GradePointDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.academy.GradePointDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class GradePointController {
	
	@RequestMapping(value = "/grade/list", method = RequestMethod.GET)
	public ModelAndView showTotalGrade(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("grade/grade");
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String userId = info.getUserId();
			
			GradePointDAO dao = new GradePointDAO();
			
			// 학점 데이터 가져오기
			List<GradePointDTO> totalGradeList = dao.findById(userId);
			
			// 취득 학점 계산
			int totalCredits = 0;
			double totalPoints = 0.0;
			
			for (GradePointDTO dto : totalGradeList) {
				totalCredits += dto.getHakscore(); // 학점 누적
				double gradePoint = dao.convertGradeToPoint(dto.getGrade()); // 성적을 숫자로 변환
				totalPoints += gradePoint * dto.getHakscore(); // 총 점수 누적
			}
			
			// 전체 평점 계산
			double totalGpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
			
			mav.addObject("totalGradeList", totalGradeList);
			
			mav.addObject("totalCredits", totalCredits);
			mav.addObject("totalGpa", Math.round(totalGpa * 100.0) / 100.0); // GPA 소수점 2자리 반올림
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return mav;
	}
	
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/grade/list", method = RequestMethod.POST) 
	public Map<String, Object> showGrade(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		GradePointDAO dao = new GradePointDAO();

		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String userId = info.getUserId();
			
			int gradeYear = Integer.parseInt(req.getParameter("gradeYear"));
			int semester = Integer.parseInt(req.getParameter("semester"));
			
			// 학점 데이터 가져오기
			List<GradePointDTO> gradeList = dao.findById(userId, gradeYear, semester);
			
			// 취득 학점 계산
			int credits = 0;
			double points = 0.0;
			
			for (GradePointDTO dto : gradeList) {
				credits += dto.getHakscore(); // 학점 누적
				double gradePoint = dao.convertGradeToPoint(dto.getGrade()); // 성적을 숫자로 변환
				points += gradePoint * dto.getHakscore(); // 총 점수 누적
							
			}
			
			// 평점 계산
			double gpa = (credits > 0) ? points / credits : 0.0;
			
			
			model.put("credits", credits);
			model.put("gpa", Math.round(gpa * 100.0) / 100.0); // GPA 소수점 2자리 반올림
			
			model.put("gradeYear", gradeYear);
			model.put("semester", semester);
			
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		
			return model;
		}
	 
	// 수강 과목 리스트 - AJAX : Text
	@RequestMapping(value = "/grade/gradeList", method = RequestMethod.GET)
	public ModelAndView gradeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		GradePointDAO dao = new GradePointDAO();
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String userId = info.getUserId();
			
			int gradeYear = Integer.parseInt(req.getParameter("gradeYear"));
			int semester = Integer.parseInt(req.getParameter("semester"));
			
			List<GradePointDTO> gradeList = dao.findById(userId, gradeYear, semester);
			
			for(GradePointDTO dto : gradeList) {
				dto.setSb_Name(dto.getSb_Name());
				dto.setHakscore(dto.getHakscore());
				dto.setGrade(dto.getGrade());
			}
			
			ModelAndView mav = new ModelAndView("grade/gradeList");
			
			mav.addObject("gradeList", gradeList);
			
			return mav;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			resp.sendError(406);
			throw e;
		}
	}
}
