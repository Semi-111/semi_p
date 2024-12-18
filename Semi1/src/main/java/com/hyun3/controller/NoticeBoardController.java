package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import com.hyun3.dao.notice.NoticeDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.lesson.LessonDTO;
import com.hyun3.domain.notice.NoticeDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.FileManager;
import com.hyun3.util.MyMultipartFile;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10)
public class NoticeBoardController {

	@RequestMapping(value = "/noticeBoard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtilBootstrap();

		// 페이징 처리
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}

		// 학과 구분(lessonNum) 파라미터 처리
		String division = req.getParameter("division");
		if (division == null) {
			division = "0"; // 관리자(전체)
		}

		// 검색 파라미터 처리
		String schType = req.getParameter("schType");
		String kwd = req.getParameter("kwd");
		if (schType == null) {
			schType = "all";
			kwd = "";
		}

		// 페이징 처리
		int size = 10;
		int total_page = 0;
		int dataCount = 0;

		if (kwd != null && kwd.length() != 0) {
			dataCount = dao.dataCount(schType, kwd, division);
		} else {
			dataCount = dao.dataCount(division);
		}

		total_page = util.pageCount(dataCount, size);
		if (current_page > total_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * size;
		if (offset < 0)
			offset = 0;

		// 게시글 리스트 가져오기
		List<NoticeDTO> list = null;
		if (kwd != null && kwd.length() != 0) {
			list = dao.listBoard(offset, size, schType, kwd, division);
		} else {
			list = dao.listBoard(offset, size, division);
		}

		// 페이징 처리를 위한 URL 생성
		String query = "";
		if (kwd != null && kwd.length() != 0) {
			query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}
		if (!division.equals("0")) {
			query += (query.length() > 0 ? "&" : "") + "division=" + division;
		}

		String listUrl = req.getContextPath() + "/noticeBoard/list";
		String paginationUrl = listUrl;
		if (query.length() != 0) {
			paginationUrl += "?" + query;
		}

		ModelAndView mav = new ModelAndView("notice/list");

		mav.addObject("list", list);
		mav.addObject("division", division);
		mav.addObject("page", current_page);
		mav.addObject("dataCount", dataCount);
		mav.addObject("size", size);
		mav.addObject("total_page", total_page);
		mav.addObject("listUrl", listUrl);
		mav.addObject("query", query);
		mav.addObject("paginationUrl", paginationUrl);

		return mav;
	}

	@RequestMapping(value = "/noticeBoard/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();

		ModelAndView mav = new ModelAndView("notice/write");

		try {
			List<LessonDTO> lessonList = dao.getLessonList();

			mav.addObject("mode", "write");
			mav.addObject("lessonList", lessonList);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;

	}

	@RequestMapping(value = "/noticeBoard/writeSubmit", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    NoticeDAO dao = new NoticeDAO();

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    if (info == null) {
	        return new ModelAndView("redirect:/member/login");
	    }

	    // FileManager 객체 생성
	    FileManager fileManager = new FileManager();
	    
	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "notice";

	    try {
	        NoticeDTO dto = new NoticeDTO();

	        String division = req.getParameter("category"); // form의 category 값이 lessonNum
	        dto.setDivision(division);
	        dto.setTitle(req.getParameter("title"));
	        dto.setContent(req.getParameter("content"));
	        dto.setMb_num(info.getMb_Num());

	        String filename = null;
	        Part p = req.getPart("selectFile");

	        // fileManager 인스턴스의 메서드로 호출
	        MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
	        if (multiPart != null) {
	            filename = multiPart.getSaveFilename();
	            dto.setFileName(filename);
	        }

	        dao.insertNotice(dto);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/noticeBoard/list");
	}
}