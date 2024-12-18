package com.hyun3.controller.academy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String userId = info.getUserId();
		
		try {
			/*
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
			*/
			
			GradePointDAO dao = new GradePointDAO();
			
			// 전체 취득 학점
			int totalHakscore = dao.totalHakscore(userId);
			
			mav.addObject("totalHakscore", totalHakscore);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return mav;
	}
	
	
	// 성적 업데이트
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "grade/updateGrade", method = RequestMethod.POST)
	public Map<String, Object> updateGrade(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		
		Map<String, Object> model = new HashMap<>();
		GradePointDAO dao = new GradePointDAO();
		
		try {
			HttpSession session = req.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");
	    	        
	        String[] atNums = req.getParameterValues("atNum");
	        String[] grades = req.getParameterValues("grade");
	        
	        if (atNums != null && grades != null && atNums.length == grades.length) {
	            for (int i = 0; i < atNums.length; i++) {
	                // 각 값으로 DTO 생성
	                GradePointDTO dto = new GradePointDTO();
	                dto.setAt_Num(Long.parseLong(atNums[i])); // atNum은 숫자(Long)로 변환
	                dto.setGrade(grades[i]);                // grade는 문자열로 그대로 저장
	                
	                dto.setUserId(info.getUserId());
	                
	                // DAO 메서드 호출
	                dao.updateGrade(dto);
	            } 
	            model.put("status", "success");
	        } else {
	            model.put("status", "error");
	            model.put("message", "Invalid parameters.");
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	
	// 그래프
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/grade/list", method = RequestMethod.POST)
	public Map<String, Object> graphGrade(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

	    Map<String, Object> model = new HashMap<>();
	    GradePointDAO dao = new GradePointDAO();

	    try {
	        HttpSession session = req.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");
	        String userId = info.getUserId();

	        // 전체 학년/학기 데이터를 가져오기
	        List<GradePointDTO> gradeList = dao.findById(userId);

	        // 학기와 성적 데이터를 저장할 리스트
	        List<String> semesterLabels = new ArrayList<>();
	        List<Double> gpaList = new ArrayList<>();

	        // 학기별 GPA 계산
	        Map<String, List<GradePointDTO>> groupedData = gradeList.stream()
	            .collect(Collectors.groupingBy(dto -> dto.getGrade_year() + "학년 " + dto.getSemester() + "학기",
	            		TreeMap::new, Collectors.toList())); // 트리맵 사용하여 자동 정렬

	        for (String semester : groupedData.keySet()) {
	            List<GradePointDTO> semesterGrades = groupedData.get(semester);

	            int credits = 0;
	            double points = 0.0;

	            for (GradePointDTO dto : semesterGrades) {
	                credits += dto.getHakscore();
	                points += dao.convertGradeToPoint(dto.getGrade()) * dto.getHakscore();
	            }

	            double gpa = (credits > 0) ? points / credits : 0.0;

	            semesterLabels.add(semester); // 학기 이름 추가
	            gpaList.add(Math.round(gpa * 100.0) / 100.0); // GPA 계산
	        }


	        // 데이터를 모델에 추가
	        model.put("semesters", semesterLabels); // 학기 정보
	        model.put("overallGrades", gpaList);    // GPA 데이터

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return model;
	}
	
	
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/grade/gradeList", method = RequestMethod.POST) 
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
	
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/grade/distribution", method = RequestMethod.GET)	
	public Map<String, Object> getGradeDistribution(HttpServletRequest req, HttpServletResponse resp) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        HttpSession session = req.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");
	        String userId = info.getUserId();

	        // DAO를 사용해 성적 데이터를 가져옴
	        GradePointDAO dao = new GradePointDAO();
	        Map<String, Integer> gradeDistribution = dao.getGradeDistribution(userId);

	        // 총 성적 개수 계산
	        int totalGrades = gradeDistribution.values().stream().mapToInt(Integer::intValue).sum();
	        
	        // 각 성적의 비율 계산
	        Map<String, Double> gradePercentages = new HashMap<>();
	        for (Map.Entry<String, Integer> entry : gradeDistribution.entrySet()) {
	            double percentage = (double) entry.getValue() / totalGrades * 100;
	            gradePercentages.put(entry.getKey(), Math.round(percentage * 100.0) / 100.0); // 소수점 2자리 반올림
	        }

	        // 결과를 JSON 형식으로 반환
	        result.put("totalGrades", totalGrades); // 총 성적 수
	        result.put("gradePercentages", gradePercentages); // 성적별 비율
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	
}
