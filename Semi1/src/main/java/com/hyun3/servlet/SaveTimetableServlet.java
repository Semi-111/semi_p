package com.hyun3.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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

	@WebServlet("/SaveTimetableServlet")
	public class SaveTimetableServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    
	    private ScheduleDAO dao = new ScheduleDAO();

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	 // 1. 세션에서 로그인한 사용자의 memberId를 가져오기   	 
	        HttpSession session = request.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");
	        
	        // 2. JSON 데이터 받아오기
	        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        StringBuilder jsonString = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonString.append(line);
	        }
	        
	     // jsonString이 비어 있지 않은지 확인
	        if (jsonString.length() == 0) {
	            System.out.println("No data received");
	        } else {
	            System.out.println("Received JSON: " + jsonString.toString());
	        }
	        
	        System.out.println("Received JSON: " + jsonString.toString());  // 받은 JSON 출력

	        // 3. JSON 파싱
	        JSONObject jsonObject = new JSONObject(jsonString.toString());
	        JSONArray subjectsArray = jsonObject.getJSONArray("subjects");  // 선택된 수업들
	        String semester = jsonObject.getString("semester");  // 학기 정보

	        boolean isAllInserted = true;

	        // 4. 수업들 DB에 삽입
	        for (int i = 0; i < subjectsArray.length(); i++) {
	            JSONObject subjectObj = subjectsArray.getJSONObject(i);
	            String sbNum = subjectObj.getString("sbNum");  // 과목 번호

	            // DTO 객체 생성 및 값 설정
	            ScheduleDTO dto = new ScheduleDTO();
	            dto.setGradee(semester.split(" ")[0].replaceAll("[^0-9]", ""));  // 학년
	            dto.setStGradee(semester.split(" ")[1].replaceAll("[^0-9]", ""));  // 학기
	            dto.setSbNum(sbNum);    // 수업 번호
	            dto.setMb_Num(info.getMb_Num()); // 로그인된 회원 ID

	            // DB에 저장
	            boolean isInserted = dao.insertSubject(dto);
	            if (!isInserted) {
	                isAllInserted = false;
	                break;
	            }
	        }

	        // 5. JSON 응답 생성
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        PrintWriter out = response.getWriter();
	        JSONObject jsonResponse = new JSONObject();

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
	
	


