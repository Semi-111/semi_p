package com.hyun3.controller.academy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyun3.dao.academy.GradePointDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.academy.GradePointDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class GradePointController {

	@RequestMapping(value = "/grade/list", method = RequestMethod.POST) 
	public ModelAndView showGradePage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("grade/grade");

		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String userId = info.getUserId();
			
			String gradeYear = req.getParameter("gradeYear");
			String semester = req.getParameter("semester");
			
			GradePointDAO dao = new GradePointDAO();
			
			// 학점 데이터 가져오기
			List<GradePointDTO> gradeList = dao.findById(userId, gradeYear, semester);
			
			
			List<Map<String, Object>> processedGradeList = new ArrayList<>();
			
			for (GradePointDTO dto : gradeList) {
			    // Map 객체 생성
			    Map<String, Object> map = new HashMap<>();
		
			    // Map에 데이터 추가
			    map.put("sb_Name", dto.getSb_Name()); 		// 과목명
			    map.put("hakscore", dto.getHakscore());     // 학점
			    map.put("grade", dto.getGrade());           // 성적
			    map.put("gradeOptions", Arrays.asList("A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "F"));
			    	// 성적 선택 옵션
		
			    // 새롭게 가공된 Map 객체를 리스트에 추가
			    processedGradeList.add(map);
			}
			
			
			// 취득 학점 계산
			// int totalCredits = gradeList.stream().mapToInt(GradePointDTO::getHakscore).sum();
			int totalCredits = 0;
			double totalPoints = 0.0;
			
			for (GradePointDTO dto : gradeList) {
				totalCredits += dto.getHakscore(); // 학점 누적
				double gradePoint = dao.convertGradeToPoint(dto.getGrade()); // 성적을 숫자로 변환
				totalPoints += gradePoint * dto.getHakscore(); // 총 점수 누적
			}
			
			// 전체 평점 계산
			double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
			
			mav.addObject("gradeList", gradeList);
			
			mav.addObject("totalCredits", totalCredits);
			mav.addObject("gpa", Math.round(gpa * 100.0) / 100.0); // GPA 소수점 2자리 반올림
			
		    mav.addObject("gradeYear", gradeYear);
		    mav.addObject("semester", semester);
			
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		
			return mav;
		}
	 
	
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
			List<GradePointDTO> gradeList = dao.findById(userId);
								
			// 취득 학점 계산
			// int totalCredits = gradeList.stream().mapToInt(GradePointDTO::getHakscore).sum();
			int totalCredits = 0;
			double totalPoints = 0.0;
			
			for (GradePointDTO dto : gradeList) {
				totalCredits += dto.getHakscore(); // 학점 누적
				double gradePoint = dao.convertGradeToPoint(dto.getGrade()); // 성적을 숫자로 변환
				totalPoints += gradePoint * dto.getHakscore(); // 총 점수 누적
			}
			
			// 전체 평점 계산
			double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
			
			mav.addObject("gradeList", gradeList);
			
			mav.addObject("totalCredits", totalCredits);
			mav.addObject("gpa", Math.round(gpa * 100.0) / 100.0); // GPA 소수점 2자리 반올림
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return mav;
	}
}
