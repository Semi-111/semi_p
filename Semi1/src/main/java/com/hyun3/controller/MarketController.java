package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.hyun3.dao.MarketDAO;
import com.hyun3.domain.sw.MarketDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
			mav.addObject("page", current_page);
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

	// 글 작성
	@RequestMapping(value = "/market/write", method = RequestMethod.POST)
	public ModelAndView marketWriteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MarketDTO dto = new MarketDTO();

		try {
			// 파일 업로드 처리를 위한 Part 객체
			Part filePart = req.getPart("selectFile");
			String fileName = null;

			if (filePart != null && filePart.getSize() > 0) {
				// 실제 파일 이름 가져오기
				fileName = filePart.getSubmittedFileName();
				
				// 파일을 서버에 저장
				String path = req.getServletContext().getRealPath("/uploads");
				File f = new File(path);
				if (!f.exists()) {
					f.mkdirs();
				}

				// 파일 저장
				filePart.write(path + File.separator + fileName);

			}

			// DTO에 데이터 저장
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setFileName(fileName);
			dto.setCt_num(Integer.parseInt(req.getParameter("category")));
			dto.setMb_num(5); // 5는 임시 / 진짜는 세션에서 가져온다

			MarketDAO dao = new MarketDAO();
			
			dao.insertMarket(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/market/list");
	}
}
