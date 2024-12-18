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

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10)
@Controller
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

	@RequestMapping(value = "/noticeBoard/writeForm", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		// 관리자가 아니면 리스트로 리다이렉트
		if (info == null || Integer.parseInt(info.getRole()) < 60) {
			return new ModelAndView("redirect:/noticeBoard/list");
		}

		NoticeDAO dao = new NoticeDAO();
		ModelAndView mav = new ModelAndView("notice/write");

		try {
			List<LessonDTO> lessonList = dao.getLessonList();
			mav.addObject("mode", "write");
			mav.addObject("lessonList", lessonList);
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

		// 관리자가 아니면 리스트로 리다이렉트
	    if (info == null || Integer.parseInt(info.getRole()) < 60) {
	        return new ModelAndView("redirect:/noticeBoard/list");
	    }

		// 파일을 저장할 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		// 폴더가 없으면 생성
		File dir = new File(pathname);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		FileManager fileManager = new FileManager();

		try {
			NoticeDTO dto = new NoticeDTO();

			String notice = req.getParameter("notice");

			dto.setNotice(notice != null && notice.equals("1") ? 1 : 0);
			dto.setDivision(req.getParameter("category"));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setMb_num(info.getMb_Num());

			// 중요공지 체크박스 처리
			System.out.println("notice value: " + notice); // 디버깅용
			dto.setNotice(notice != null && notice.equals("1") ? 1 : 0);

			// 파일 처리
			String filename = null;
			Part p = req.getPart("selectFile");

			if (p != null && p.getSize() > 0) {
				MyMultipartFile mf = fileManager.doFileUpload(p, pathname);
				if (mf != null) {
					filename = mf.getSaveFilename();
					dto.setFileName(filename);
				}
			}

			dao.insertNotice(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/noticeBoard/list");
	}

	// 글보기
	@RequestMapping(value = "/noticeBoard/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtilBootstrap();

		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long noticeNum = Long.parseLong(req.getParameter("noticeNum")); // cm_num임 (글번호)
			String division = req.getParameter("division");
			if (division == null) {
				division = "0";
			}

			// 조회수 증가
			dao.updateHitCount(noticeNum);

			// 게시물 가져오기
			NoticeDTO dto = dao.findById(noticeNum);
			if (dto == null) {
				return new ModelAndView("redirect:/noticeBoard/list?" + query);
			}

			// HTML 태그를 엔티티코드로 변환
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// Content에서 줄바꿈 처리
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 이전글, 다음글
			NoticeDTO prevDto = dao.findByPrev(noticeNum, division);
			NoticeDTO nextDto = dao.findByNext(noticeNum, division);

			ModelAndView mav = new ModelAndView("notice/article");

			mav.addObject("dto", dto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("page", page);
			mav.addObject("query", query);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/noticeBoard/list?" + query);
	}
	
	// 게시글 수정 폼
	@RequestMapping(value = "/noticeBoard/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    
	    // 관리자가 아니면 리스트로 리다이렉트
	    if (info == null || Integer.parseInt(info.getRole()) < 60) {
	        return new ModelAndView("redirect:/noticeBoard/list");
	    }
	    
	    NoticeDAO dao = new NoticeDAO();
	    String page = req.getParameter("page");
	    
	    try {
	        long noticeNum = Long.parseLong(req.getParameter("noticeNum"));
	        NoticeDTO dto = dao.findById(noticeNum);
	        
	        if (dto == null) {
	            return new ModelAndView("redirect:/noticeBoard/list?page=" + page);
	        }
	        
	        List<LessonDTO> lessonList = dao.getLessonList();
	        
	        ModelAndView mav = new ModelAndView("notice/write");
	        mav.addObject("dto", dto);
	        mav.addObject("lessonList", lessonList);
	        mav.addObject("page", page);
	        mav.addObject("mode", "update");
	        
	        return mav;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return new ModelAndView("redirect:/noticeBoard/list?page=" + page);
	}

	// 게시글 수정
	@RequestMapping(value = "/noticeBoard/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    
	    // 관리자가 아니면 리스트로 리다이렉트
	    if (info == null || Integer.parseInt(info.getRole()) < 60) {
	        return new ModelAndView("redirect:/noticeBoard/list");
	    }
	    
	    String page = req.getParameter("page");
	    NoticeDAO dao = new NoticeDAO();
	    
	    try {
	        NoticeDTO dto = new NoticeDTO();
	        
	        dto.setCm_num(Long.parseLong(req.getParameter("noticeNum")));
	        dto.setDivision(req.getParameter("category"));
	        dto.setTitle(req.getParameter("title"));
	        dto.setContent(req.getParameter("content"));
	        
	        String notice = req.getParameter("notice");
	        dto.setNotice(notice != null && notice.equals("1") ? 1 : 0);
	        
	        dao.updateNotice(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return new ModelAndView("redirect:/noticeBoard/list?page=" + page);
	}

	// 게시글 삭제
	@RequestMapping(value = "/noticeBoard/delete")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    
	    // 관리자가 아니면 리스트로 리다이렉트
	    if (info == null || Integer.parseInt(info.getRole()) < 60) {
	        return new ModelAndView("redirect:/noticeBoard/list");
	    }
	    
	    String page = req.getParameter("page");
	    NoticeDAO dao = new NoticeDAO();
	    
	    try {
	        long noticeNum = Long.parseLong(req.getParameter("noticeNum"));
	        dao.deleteNotice(noticeNum);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return new ModelAndView("redirect:/noticeBoard/list?page=" + page);
	}
}