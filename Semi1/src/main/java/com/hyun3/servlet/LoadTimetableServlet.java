package com.hyun3.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hyun3.dao.schedule.ScheduleDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.schedule.ScheduleDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

	@WebServlet("/LoadTimetableServlet")
	public class LoadTimetableServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;

	    private ScheduleDAO dao = new ScheduleDAO();

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	System.out.println("서블릿 doPost 호출됨");
	        // 세션에서 로그인한 사용자의 memberId를 가져오기
	        HttpSession session = request.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");
	        if (info == null) {
	            System.out.println("세션 정보가 없습니다.");
	        } else {
	            System.out.println("세션 정보가 정상입니다: " + info.getMb_Num());
	        } 

	        // 사용자가 선택한 학년, 학기 정보 받기
	        String gradeYear = request.getParameter("gradeYear");
	        String semester = request.getParameter("semesterCode");
	        
	        // 데이터베이스에서 시간표 조회
	        List<ScheduleDTO> timetable = dao.getTimetable(gradeYear, semester, info.getMb_Num());
	        System.out.println("시간표 조회 결과: " + (timetable == null ? "null" : timetable.isEmpty() ? "빈 리스트" : timetable.size() + "개 항목"));
	        if (timetable == null || timetable.isEmpty()) {
	            System.out.println("시간표 정보가 없습니다.");
	        } else {
	            System.out.println("시간표가 존재합니다.");
	        }
	        
	        // JSON 응답 생성
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        PrintWriter out = response.getWriter();
	        JSONObject jsonResponse = new JSONObject();
	        
	        // 시간표가 있을 경우
	        if (timetable != null && !timetable.isEmpty()) {
	            JSONArray timetableArray = new JSONArray();
	            for (ScheduleDTO dto : timetable) {
	            	JSONObject timetableObject = new JSONObject();
	                timetableObject.put("grade_year", dto.getGradee());
	                timetableObject.put("semester", dto.getStGradee());
	                timetableObject.put("dt_sub_num", dto.getSbNum());
	                timetableObject.put("mb_num", dto.getMb_Num());
	                timetableObject.put("studytime", dto.getStudytime());
	                timetableObject.put("studyday", dto.getStudyDay());
	                timetableObject.put("sb_name", dto.getSbName());
	               
	                timetableArray.put(timetableObject);
	            }
	            jsonResponse.put("status", "success");
	            jsonResponse.put("timetable", timetableArray);
	        } else {
	            jsonResponse.put("status", "error");
	            jsonResponse.put("message", "No timetable found.");
	        }

	        out.print(jsonResponse.toString());
	        out.flush();
	    }
	}
	
