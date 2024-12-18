package com.hyun3.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hyun3.dao.schedule.ScheduleDAO;
import com.hyun3.domain.schedule.ScheduleDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ScheduleController {

	@RequestMapping(value = "/schedule/schedule2", method = RequestMethod.GET)
	public ModelAndView stClass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("schedule/schedule2");  // test/schedule2 JSP를 반환

	    ScheduleDAO dao = new ScheduleDAO();
	    try {
	        List<ScheduleDTO> viewSubject = dao.viewSubject();
	        
	        mav.addObject("viewSubject", viewSubject);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}
	
	@WebServlet("/SaveTimetableServlet")
	public class SaveTimetableServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;

	    // ScheduleDAO 객체 생성
	    private ScheduleDAO dao = new ScheduleDAO();

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // 1. 세션에서 로그인한 사용자의 memberId를 가져오기
	        HttpSession session = request.getSession();
	        String memberId = (String) session.getAttribute("memberId");

	        if (memberId == null) {
	            // 로그인되지 않은 상태라면 로그인 페이지로 리다이렉트
	            response.sendRedirect("login.jsp");
	            return;
	        }

	        // 2. JSON 데이터 받아오기
	        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        StringBuilder jsonString = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonString.append(line);
	        }

	        // 3. JSON 파싱
	        JSONObject jsonObject = new JSONObject(jsonString.toString());
	        JSONArray subjectsArray = jsonObject.getJSONArray("subjects");  // 선택된 수업들

	        String semester = jsonObject.getString("semester");  // 학기 정보 (1학년 1학기 등)

	        // 학기 정보에서 학년과 학기를 분리
	        String[] semesterParts = semester.split(" ");
	        String grade = semesterParts[0];  // 예: "1학년"
	        String term = semesterParts[1];   // 예: "1학기"

	        boolean isAllInserted = true;

	        // 4. 선택된 수업들 DB에 삽입
	        for (int i = 0; i < subjectsArray.length(); i++) {
	            JSONObject subjectObj = subjectsArray.getJSONObject(i);
	            String sbNum = subjectObj.getString("dt_sub_num");  // 과목 번호

	            // DTO 객체 생성 및 값 설정
	            ScheduleDTO dto = new ScheduleDTO();
	            dto.setGradee(grade);  // 학년
	            dto.setStGradee(term);  // 학기
	            dto.setSbNum(sbNum);  // 수업 번호
	            dto.setMemberId(memberId);  // 로그인된 회원 ID

	            // DB에 저장
	            boolean isInserted = dao.insertSubject(dto);
	            if (!isInserted) {
	                isAllInserted = false;  // 하나라도 실패하면 전체 실패
	                break;
	            }
	            
	            System.out.println("Received JSON: " + jsonString.toString());
	            System.out.println("Semester: " + semester);
	            System.out.println("Number of subjects: " + subjectsArray.length());
	        }

	        // 5. JSON 응답 생성
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        PrintWriter out = response.getWriter();
	        JSONObject jsonResponse = new JSONObject();

	        // 6. 응답 상태와 메시지 반환
	        if (isAllInserted) {
	            jsonResponse.put("status", "success");
	            jsonResponse.put("message", "시간표 저장 성공!");
	        } else {
	            jsonResponse.put("status", "error");
	            jsonResponse.put("message", "시간표 저장 실패!");
	        }

	        out.print(jsonResponse.toString());  // JSON 응답 전송
	        out.flush();
	    }
	    
	    
	}
	}