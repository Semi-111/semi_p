package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import com.hyun3.dao.LessonDAO;
import com.hyun3.domain.LessonDTO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.FileManager;
import com.hyun3.util.MyMultipartFile;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


@Controller
public class LessonController {
	// 학과게시판으로 이동
	@RequestMapping(value = "/lessonBoard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) 
	       throws ServletException, IOException {
	   ModelAndView mav = new ModelAndView("lesson/list");
	       
	   LessonDAO dao = new LessonDAO();
	   MyUtil util = new MyUtilBootstrap();
	   
	   String page = req.getParameter("page");
	   int current_page = 1;
	   if(page != null) {
	       current_page = Integer.parseInt(page);
	   }
	   
	   String schType = req.getParameter("schType");
	   String kwd = req.getParameter("kwd");
	   if(schType == null) {
	       schType = "all";
	       kwd = "";
	   }
	   
	   int size = 10;
	   int total_page = 0;
	   int dataCount = 0;
	   
	   try {
	       // 검색어가 있는 경우와 없는 경우를 구분
	       if(kwd != null && kwd.length() != 0) {
	           dataCount = dao.dataCount(schType, kwd);
	       } else {
	           dataCount = dao.dataCount();
	       }
	       total_page = util.pageCount(dataCount, size);
	       
	       if(current_page > total_page) {
	           current_page = total_page;
	       }
	       
	       int offset = (current_page - 1) * size;
	       if(offset < 0) offset = 0;
	       
	       // 검색 여부에 따라 다른 메서드 호출
	       List<LessonDTO> list = null;
	       if(kwd != null && kwd.length() != 0) {
	           list = dao.listBoard(offset, size, schType, kwd);
	       } else {
	           list = dao.listBoard(offset, size);
	       }
	       
	       String query = "";
	       if(kwd != null && kwd.length() != 0) {
	           query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	       }
	       
	       String listUrl = req.getContextPath() + "/lessonBoard/list";
	       String paginationUrl = listUrl;
	       if(query.length() != 0) {
	           paginationUrl += "?" + query;
	       }

	       // 페이징 처리 추가
	       String paging = util.paging(current_page, total_page, listUrl);
	       
	       // 게시글 보기 URL
	       String articleUrl = req.getContextPath() + "/lessonBoard/article?page=" + current_page;
	       if(query.length() != 0) {
	           articleUrl += "&" + query;
	       }
	       
	       mav.addObject("list", list);
	       mav.addObject("page", current_page);
	       mav.addObject("total_page", total_page);
	       mav.addObject("dataCount", dataCount);
	       mav.addObject("size", size);
	       mav.addObject("paginationUrl", paginationUrl);
	       mav.addObject("paging", paging);
	       mav.addObject("articleUrl", articleUrl);
	       mav.addObject("query", query);
	       
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	   
	   return mav;
	}
	       

	       
	
	// 글쓰기 폼으로 이동
	@RequestMapping(value = "/lessonBoard/writeForm", method = RequestMethod.GET)
	public ModelAndView lessonWriteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		
		ModelAndView mav = new ModelAndView("lesson/write");
		
		return mav;
	}
	
	// 글 작성
	@RequestMapping(value = "/lessonBoard/writeForm", method = RequestMethod.POST)
	public ModelAndView lessonWriteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		LessonDAO dao = new LessonDAO();
		
		HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    FileManager fileManager = new FileManager();

	    // 파일 저장 경로
	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "photo";

	    try {
	        LessonDTO dto = new LessonDTO();

	        // DTO에 데이터 저장
	        dto.setTitle(req.getParameter("title"));
	        dto.setBoard_content(req.getParameter("content")); // 수정 // 글 내용
	        dto.setMb_num(info.getMb_Num());
	        dto.setDivision("lesson"); // 학과게시판이라는 것을 알려줌
	        // 학과 : 경영학(51) 경창행정과 (52) 디자인학과 (53) 화학공학 (54) 컴퓨터응용전자과 (55) 정보통신학부 (56)
	        dto.setLessonNum(Integer.parseInt(req.getParameter("category")));
	        
	        // 파일 업로드 처리
	        String filename = null;
	        Part p = req.getPart("selectFile");

	        // FileManager를 사용하여 파일 업로드
	        MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
	        if (multiPart != null) {
	            // 저장된 파일명
	            filename = multiPart.getSaveFilename();
	        }

	        // 파일명을 DTO에 저장
	        if (filename != null) {
	            dto.setFileName(filename);
	        }

	        // 데이터베이스에 게시물 정보 저장
	        dao.insertLesson(dto);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/lessonBoard/list");
	}
	
	@RequestMapping(value="/lessonBoard/article")
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("lesson/article");
	    
	    String page = req.getParameter("page");
	    String query = "page=" + page;
	    
	    String schType = req.getParameter("schType");
	    String kwd = req.getParameter("kwd");
	    if (schType == null) {
	        schType = "all";
	        kwd = "";
	    }
	    
	    if (kwd != null && kwd.length() != 0) {
	        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	    }
	    
	    LessonDAO dao = new LessonDAO();
	    
	    try {
	        long num = Long.parseLong(req.getParameter("cm_num"));
	        
	        // 조회수 증가
	        dao.updateHitCount(num);
	        
	        // 게시물 가져오기
	        LessonDTO dto = dao.findById(num);
	        if(dto == null) {
	            return new ModelAndView("redirect:/lessonBoard/list?" + query);
	        }
	        
	        // 이전글, 다음글
	        LessonDTO prevDto = dao.findByPrev(num, schType, kwd);
	        LessonDTO nextDto = dao.findByNext(num, schType, kwd);
	        
	        mav.addObject("dto", dto);
	        mav.addObject("page", page);
	        mav.addObject("query", query);
	        mav.addObject("prevDto", prevDto);
	        mav.addObject("nextDto", nextDto);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return mav;
	}
	
	@RequestMapping(value="/lessonBoard/delete")
	public ModelAndView lessonDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("redirect:/lessonBoard/list");
		
		return mav;
	}
	
}
