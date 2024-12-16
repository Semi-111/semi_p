package com.hyun3.controller.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.hyun3.dao.ReportDAO;
import com.hyun3.domain.ReportDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

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

	// 신고 목록 조회
	
	@RequestMapping(value = "/admin/home/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("admin/home/report");
	    
	    ReportDAO dao = new ReportDAO();
	    MyUtil util = new MyUtilBootstrap();

	    try {
	        String page = req.getParameter("page");
	        int current_page = 1;
	        if (page != null) {
	            current_page = Integer.parseInt(page);
	        }

	        // 검색
	        String schType = req.getParameter("schType");
	        String kwd = req.getParameter("kwd");
	        if (schType == null) {
	            schType = "all";
	            kwd = "";
	        }

	        // GET 방식이면 디코딩
	        if (req.getMethod().equalsIgnoreCase("GET")) {
	            kwd = URLDecoder.decode(kwd, "utf-8");
	        }

	        // 전체 데이터 개수
	        int dataCount;
	        if (kwd.length() == 0) {
	            dataCount = dao.dataCount();
	        } else {
	            dataCount = dao.dataCount(schType, kwd);
	        }

	        // 전체 페이지 수
	        int size = 10;
	        int total_page = util.pageCount(dataCount, size);
	        if (current_page > total_page) {
	            current_page = total_page;
	        }

	        // 게시물 가져오기
	        int offset = (current_page - 1) * size;
	        if (offset < 0) offset = 0;

	        List<ReportDTO> list = null;
	        if (kwd.length() == 0) {
	            list = dao.listReport(offset, size);
	        } else {
	            list = dao.listReport(offset, size, schType, kwd);
	        }

	        String query = "";
	        if (kwd.length() != 0) {
	            query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	        }

	        // 페이징 처리
	        String cp = req.getContextPath();
	        String listUrl = cp + "/admin/home/report";
	        
	        if (query.length() != 0) {
	            listUrl += "?" + query;
	        }

	        String paging = util.paging(current_page, total_page, listUrl);

	        // JSP에 전달할 속성
	        mav.addObject("list", list);
	        mav.addObject("page", current_page);
	        mav.addObject("total_page", total_page);
	        mav.addObject("dataCount", dataCount);
	        mav.addObject("size", size);
	        mav.addObject("paging", paging);
	        mav.addObject("schType", schType);
	        mav.addObject("kwd", kwd);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/admin/report/detail")
	public ReportDTO detail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	    ReportDTO dto = null;
	    
	    try {
	        long rpNum = Long.parseLong(req.getParameter("rpNum"));
	        
	        ReportDAO dao = new ReportDAO();
	        dto = dao.findByIdWithPostInfo(rpNum);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	    
	    return dto;
	}
}
