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

public class ShowTimeTable {
	@WebServlet("/LoadTimetableServlet")
	public class LoadTimetableServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;

	    private ScheduleDAO dao = new ScheduleDAO();

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // 세션에서 로그인한 사용자의 memberId를 가져오기
	        HttpSession session = request.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");

	        // 사용자가 선택한 학년, 학기 정보 받기
	        String gradeYear = request.getParameter("grade_year");
	        String semester = request.getParameter("semester");
	        Long memberId = info.getMb_Num(); // 로그인한 회원 번호

	        // 데이터베이스에서 시간표 조회
	        List<ScheduleDTO> timetable = dao.getTimetable(gradeYear, semester, memberId);

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
	                timetableObject.put("grade_Year", dto.getGradee());
	                timetableObject.put("semester", dto.getStGradee());
	                timetableObject.put("dt_sub_num", dto.getSbNum());
	                timetableObject.put("mb_num", dto.getMb_Num());
	                
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
}