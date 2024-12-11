package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.hyun3.dao.MarketDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.sw.MarketDTO;
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

public class MarketController {

	// 장터게시판으로 이동
	@RequestMapping(value = "/market/list", method = RequestMethod.GET)
	public ModelAndView marketList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 이곳에 글 리스트가 와야함. 페이징 처리 등등....
		// 게시글 리스트 : 파라미터 - [page], [schType, kwd]
		ModelAndView mav = new ModelAndView("market/list");

		MarketDAO dao = new MarketDAO();
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

			// 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(); // 검색이 아닌경우
			} else {
				dataCount = dao.dataCount(schType, kwd); // 검색인 경우
			}

			// 전체 페이지 수
			int size = 7;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<MarketDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listBoard(offset, size);
			} else {
				list = dao.listBoard(offset, size, schType, kwd);
			}

			// 쿼리
			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/market/list";

			// 글리스트 주소
			String articleUrl = cp + "/market/article?page=" + current_page;

			// 글보기 주소
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("current_page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 장터 글쓰기폼
	@RequestMapping(value = "/market/write", method = RequestMethod.GET)
	public ModelAndView marketWriteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("market/write");
		return mav;
	}

	@RequestMapping(value = "/market/write", method = RequestMethod.POST)
	public ModelAndView marketWriteSubmit(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    MarketDAO dao = new MarketDAO();
	    
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    
	    FileManager fileManager = new FileManager();
	    
	    // 파일 저장 경로
	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "photo";

	    try {
	        MarketDTO dto = new MarketDTO();
	        
	        // DTO에 데이터 저장
	        dto.setTitle(req.getParameter("title"));
	        dto.setContent(req.getParameter("content"));
	        dto.setCt_num(Integer.parseInt(req.getParameter("category")));
	        dto.setMb_num(info.getMb_Num());

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
	        dao.insertMarket(dto);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/market/list");
	}

	@RequestMapping(value = "/market/article", method = RequestMethod.GET)
	public ModelAndView marketArticle(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MarketDAO dao = new MarketDAO();
		MyUtil util = new MyUtilBootstrap();
		
		// 현재 로그인 정보 가져오기
		HttpSession info = req.getSession();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			
			long marketNum = Long.parseLong(req.getParameter("marketNum"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");

			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 조회수 증가
			dao.updateHitCount(marketNum);

			// 게시물 가져오기
			MarketDTO dto = dao.findById(marketNum);
			if (dto == null) {
				return new ModelAndView("redirect:/market/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글, 다음글
			MarketDTO prevDto = dao.findByPrev(marketNum, schType, kwd);
			MarketDTO nextDto = dao.findByNext(marketNum, schType, kwd);

			// JSP로 전달할 속성
			ModelAndView mav = new ModelAndView("market/article");

			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("member", info);
			 
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/market/list?" + query);
	}

}
