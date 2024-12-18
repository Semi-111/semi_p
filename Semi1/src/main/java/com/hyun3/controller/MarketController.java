package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;

import com.hyun3.dao.market.MarketDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.market.MarketDTO;
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
	@RequestMapping(value = "/market/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
	    MarketDAO dao = new MarketDAO();
	    MyUtil util = new MyUtilBootstrap ();

	    String page = req.getParameter("page");
	    int current_page = 1;
	    if (page != null) {
	        current_page = Integer.parseInt(page);
	    }

	    // 카테고리 파라미터 처리
	    String categoryParam = req.getParameter("category");
	    int category = 0; // 0은 전체를 의미
	    if (categoryParam != null) {
	        category = Integer.parseInt(categoryParam);
	    }

	    String schType = req.getParameter("schType");
	    String kwd = req.getParameter("kwd");
	    if (schType == null) {
	        schType = "all";
	        kwd = "";
	    }

	    int size = 10;
	    int total_page = 0;
	    int dataCount = 0;

	    if (kwd != null && kwd.length() != 0) {
	        dataCount = dao.dataCount(schType, kwd);
	    } else if (category > 0) {
	        dataCount = dao.dataCount(category);
	    } else {
	        dataCount = dao.dataCount();
	    }

	    total_page = util.pageCount(dataCount, size);

	    if (current_page > total_page) {
	        current_page = total_page;
	    }

	    int offset = (current_page - 1) * size;
	    if(offset < 0) offset = 0;

	    List<MarketDTO> list = null;
	    if (kwd != null && kwd.length() != 0) {
	        list = dao.listBoard(offset, size, schType, kwd);
	    } else if (category > 0) {
	        list = dao.listBoard(offset, size, category);
	    } else {
	        list = dao.listBoard(offset, size);
	    }

	    String query = "";
	    if (kwd != null && kwd.length() != 0) {
	        query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	    } else if (category > 0) {
	        query = "category=" + category;
	    }

	    String listUrl = req.getContextPath() + "/market/list";
	    String paginationUrl = listUrl;
	    if (query.length() != 0) {
	        paginationUrl += "?" + query;
	    }

	    ModelAndView mav = new ModelAndView("market/list");

	    mav.addObject("list", list);
	    mav.addObject("category", category);
	    mav.addObject("page", current_page);
	    mav.addObject("dataCount", dataCount);
	    mav.addObject("size", size);
	    mav.addObject("total_page", total_page);
	    mav.addObject("listUrl", listUrl);
	    mav.addObject("query", query);           // query 추가
	    mav.addObject("paginationUrl", paginationUrl); // 페이징용 URL 추가

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

	// 글보기
	@RequestMapping(value = "/market/article", method = RequestMethod.GET)
	public ModelAndView marketArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MarketDAO dao = new MarketDAO();
		MyUtil util = new MyUtilBootstrap();

		// 현재 로그인 정보 가져오기
		HttpSession session = req.getSession();
		SessionInfo member = (SessionInfo)session.getAttribute("member");

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

			// 좋아요 여부
			boolean isLiked = false;
			if(member != null) {
			    isLiked = dao.isLikedMarket(marketNum, member.getMb_Num());
			}
			int countLikes = dao.countLikes(marketNum);

			// JSP로 전달할 속성
			ModelAndView mav = new ModelAndView("market/article");

			mav.addObject("isLiked", isLiked);
			mav.addObject("countLikes", countLikes);
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("member", member);

			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/market/list?" + query);
	}

	@RequestMapping(value = "/market/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    MarketDAO dao = new MarketDAO();

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    FileManager fileManager = new FileManager();

	    String page = req.getParameter("page");

	    // 파일 저장 경로
	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "photo";

	    try {
	        long marketNum = Long.parseLong(req.getParameter("marketNum"));

	        // 게시물 정보 가져오기
	        MarketDTO dto = dao.findById(marketNum);
	        if (dto == null) {
	            return new ModelAndView("redirect:/market/list?page=" + page);
	        }

	        // 게시물을 올린 사용자나 관리자가 아니면
	        // mb_num은 long 타입
	        if (dto.getMb_num() != info.getMb_Num() && Integer.parseInt(info.getRole())  < 100) {
	            return new ModelAndView("redirect:/market/list?page=" + page);
	        }

	        // 첨부 파일 삭제
	        if(dto.getFileName() != null && !dto.getFileName().isEmpty()) {
	            fileManager.doFiledelete(pathname, dto.getFileName());
	        }

	        // 테이블 데이터 삭제
	        dao.deleteMarket(marketNum);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/market/list?page=" + page);
	}

	@RequestMapping(value = "/market/like", method = RequestMethod.POST)
	public void like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    MarketDAO dao = new MarketDAO();
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    String state = "true";

	    try {
	        long marketNum = Long.parseLong(req.getParameter("marketNum"));

	        if(info == null) {
	            state = "loginFail";
	        } else {
	            boolean liked = dao.isLikedMarket(marketNum, info.getMb_Num());
	            if(liked) {
	                dao.deleteLike(marketNum, info.getMb_Num());
	            } else {
	                dao.insertLike(marketNum, info.getMb_Num());
	            }
	        }

	        // 좋아요 개수
	        int countLikes = dao.countLikes(marketNum);

	        JSONObject job = new JSONObject();
	        job.put("state", state);
	        job.put("countLikes", countLikes);

	        resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out = resp.getWriter();
	        out.print(job.toString());

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// 글 수정
	@RequestMapping(value="/market/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    MarketDAO dao = new MarketDAO();

	    String page = req.getParameter("page");
	    long marketNum = Long.parseLong(req.getParameter("marketNum"));

	    ModelAndView mav = new ModelAndView("market/write");

	    try {
	        MarketDTO dto = dao.findById(marketNum);
	        if(dto == null) {
	            return new ModelAndView("redirect:/market/list?page=" + page);
	        }

	        // 게시물을 작성한 사용자가 아니면
	        HttpSession session = req.getSession();
	        SessionInfo info = (SessionInfo)session.getAttribute("member");
	        if(info.getMb_Num() != dto.getMb_num() && Integer.parseInt(info.getRole()) < 51) {
	            return new ModelAndView("redirect:/market/list?page=" + page);
	        }

	        mav.addObject("dto", dto);
	        mav.addObject("page", page);
	        mav.addObject("mode", "update");

	        return mav;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/market/list?page=" + page);
	}

	@RequestMapping(value="/market/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    MarketDAO dao = new MarketDAO();

	    String page = req.getParameter("page");

	    try {
	        MarketDTO dto = new MarketDTO();

	        dto.setMarketNum(Integer.parseInt(req.getParameter("marketNum")));
	        dto.setTitle(req.getParameter("title"));
	        dto.setContent(req.getParameter("content"));
	        dto.setCt_num(Integer.parseInt(req.getParameter("category"))); // 카테고리가 있는 경우

	        // 파일 업로드 처리
	        String filename = req.getParameter("filename");
	        dto.setFileName(filename);

	        dao.updateMarket(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/market/list?page=" + page);
	}

}
