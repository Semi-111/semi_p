package com.hyun3.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hyun3.dao.academy.GradePointDAO;
import com.hyun3.domain.academy.GradePointDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class GradePointController {
	
	@RequestMapping(value = "/grade/list", method = RequestMethod.GET)
	public ModelAndView showGradePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자 ID 또는 회원 번호를 요청 파라미터에서 가져오기
        long mb_Num = Long.parseLong(req.getParameter("mb_Num")); // 요청에서 mb_Num 추출

        // 2. DAO 호출 및 데이터 조회
        GradePointDAO dao = new GradePointDAO();
        List<GradePointDTO> gradeList = dao.findByMemberId(mb_Num);
        
        // 3. 학점 통계 계산
		Map<String, Double> gradeStats = dao.calculateGrade(gradeList);
		double gpa = gradeStats.get("gpa");
		double totalCredits = gradeStats.get("TotalCredits");
		
		// 4. ModelAndView 생성
		ModelAndView mav = new ModelAndView("grade/grade");
		mav.addObject("gradeList", gradeList); // 조회된 학점 리스트
		mav.addObject("gpa", gpa); // 계산된 GPA
		mav.addObject("totalCredits", totalCredits); // 총 취득 학점
		
		return mav;
	}
}
