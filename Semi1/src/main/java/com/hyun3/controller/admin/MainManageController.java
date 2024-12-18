package com.hyun3.controller.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyun3.dao.report.ReportDAO;
import com.hyun3.domain.report.ReportDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
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
			if (offset < 0)
				offset = 0;

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
	public Map<String, Object> detail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		try {
			long rpNum = Long.parseLong(req.getParameter("rpNum"));

			ReportDAO dao = new ReportDAO();
			ReportDTO dto = dao.findByIdWithPostInfo(rpNum);

			if (dto != null) {
				model.put("state", "success");
				model.put("RP_num", dto.getRP_num());
				model.put("RP_title", dto.getRP_title());
				model.put("RP_content", dto.getRP_content());
				model.put("RP_reason", dto.getRP_reason());
				model.put("RP_table", dto.getRP_table());
				model.put("RP_url", dto.getRP_url());
				model.put("memberName", dto.getMemberName());
				model.put("postTitle", dto.getPostTitle());
				model.put("postContent", dto.getPostContent());
				model.put("postWriter", dto.getPostWriter());
			} else {
				model.put("state", "false");
			}

		} catch (Exception e) {
			model.put("state", "false");
			e.printStackTrace();
		}

		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/admin/report/block", method = RequestMethod.POST)
	public Map<String, Object> block(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<>();

		try {
	        long rpNum = Long.parseLong(req.getParameter("rpNum"));
	        ReportDAO dao = new ReportDAO();
	        
	        // 신고글 정보 조회
	        ReportDTO dto = dao.findByIdWithPostInfo(rpNum);
	        
	        if (dto != null) {
	            // 신고된 사용자의 targetMbNum을 이용해 차단
	            dao.userBlock(dto.getTargetMbNum());
	            model.put("state", "success");
	        } else {
	            model.put("state", "false");
	        }
	        
	    } catch (Exception e) {
	        model.put("state", "false");
	        e.printStackTrace();
	    }
	    
	    return model;
	}
}
