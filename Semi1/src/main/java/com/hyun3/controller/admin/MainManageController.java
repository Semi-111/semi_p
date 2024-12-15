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
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainManageController {

	// 어드민 페이지로 이동
	@RequestMapping(value = "/admin/home/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/home/main");
		return mav;
	}

	// 신고페이지로 이동
	@RequestMapping(value = "/admin/home/report", method = RequestMethod.POST)
	public ModelAndView reportWrite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/home/report");

		// 이곳에서 누가 신고당했는지 목록이 떠야함.(article.jsp 에서 신고 가능)
		
		return mav;
	}
	
	    // 신고 대시보드
	    @RequestMapping(value = "/admin/report/list")
	    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) 
	            throws ServletException, IOException {
	        
	        ReportDAO dao = new ReportDAO();
	        ModelAndView mav = new ModelAndView("admin/report/list");
	        
	        try {
	            List<ReportDTO> list = dao.listReport();
	            
	            // 통계 데이터 계산
	            int pendingCount = 0;
	            int todayCount = 0;
	            int completedCount = 0;
	            
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            String today = sdf.format(new Date());
	            
	            for(ReportDTO dto : list) {
	                if("대기".equals(dto.getStatus())) {
	                    pendingCount++;
	                } else if("완료".equals(dto.getStatus())) {
	                    completedCount++;
	                }
	                if(today.equals(dto.getReportDate())) {
	                    todayCount++;
	                }
	            }
	            
	            // 처리율 계산
	            double processRate = list.isEmpty() ? 0 : 
	                (completedCount * 100.0) / list.size();
	            
	            mav.addObject("list", list);
	            mav.addObject("pendingCount", pendingCount);
	            mav.addObject("todayCount", todayCount);
	            mav.addObject("completedCount", completedCount);
	            mav.addObject("processRate", String.format("%.1f", processRate));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return mav;
	    }

	    // 신고 처리 상태 업데이트 (AJAX)
	    @RequestMapping(value = "/admin/report/updateStatus", method = RequestMethod.POST)
	    public void updateStatus(HttpServletRequest req, HttpServletResponse resp) 
	            throws ServletException, IOException {
	        
	        ReportDAO dao = new ReportDAO();
	        String state = "false";
	        
	        try {
	            long rpNum = Long.parseLong(req.getParameter("rpNum"));
	            String status = req.getParameter("status");
	            
	            //dao.updateReportStatus(rpNum, status);
	            state = "true";
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject job = new JSONObject();
	        job.put("state", state);
	        
	        resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out = resp.getWriter();
	        out.print(job.toString());
	    }
	}
