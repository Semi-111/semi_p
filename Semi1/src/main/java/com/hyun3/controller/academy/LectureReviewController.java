package com.hyun3.controller.academy;

import java.io.IOException;
import java.util.List;

import com.hyun3.dao.academy.LectureReviewDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.academy.LectureReviewDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LectureReviewController {
	
	@RequestMapping(value = "/lectureReview/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("lectureReview/list");
		
		LectureReviewDAO dao = new LectureReviewDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String userId = info.getUserId();
			
			// 내 강의 리스트
			List<LectureReviewDTO> myLlist = null;
			myLlist = dao.listLecture(userId);
			
			mav.addObject("myLlist", myLlist);
			
			
			
			// 리뷰 리스트
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int dataCount = dao.dataCount();
			
			// 전체 페이지 수
			int size = 3;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			// 데이터 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
					
			List<LectureReviewDTO> reviewList = dao.listReview(offset, size);
			
			for(LectureReviewDTO dto : reviewList) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			// reviewList = dao.listReview(userId);
			
			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/lectureReview/list";
			String articleUrl = cp + "/lectureReview/article?page=" + current_page;
			
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("reviewList", reviewList);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			// mav.addObject("schType", schType);
			// mav.addObject("kwd", kwd);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	
	
	/* 무한 스크롤때 필요
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "lectureReview/reviewList", method = RequestMethod.GET)
	public Map<String, Object> listReview(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 리뷰 리스트
		Map<String, Object> model = new HashMap<String, Object>();
		
		LectureReviewDAO dao = new LectureReviewDAO();
		MyUtil util = new MyUtilBootstrap();
		
		 수정, 삭제 여부때문에 필요
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		
		try {
			// 넘어온 페이지 번호
			String page = req.getParameter("pageNo");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int dataCount = dao.dataCount();
			
			// 전체 페이지 수
			int size = 3;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			// 데이터 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<LectureReviewDTO> list = dao.listReview(offset, size);
			
			for(LectureReviewDTO dto : list) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				
				// 리뷰 삭제 가능 여부 - GuestController 참고
				
				if(info!=null && (info.getUserId().equals(dto.getUserId()) || info.getUserLevel() >= 51)) {
					dto.setDeletePermit(true); //게시글 삭제 가능 여부
				}
				
			}
			
			model.put("list", list);
			model.put("pageNo", current_page);
			model.put("total_page", total_page);
			model.put("dataCount", dataCount);
			model.put("state", "true");
				
		} catch (Exception e) {
			model.put("state", "false");
			e.printStackTrace();
		}
		
		return model;
	}
	*/
	
	
	@RequestMapping(value = "/lectureReview/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("lectureReview/write");
		
		try {
			LectureReviewDAO dao = new LectureReviewDAO();
			long sbNum = Long.parseLong(req.getParameter("sbNum"));
			
			// 과목번호로 과목명 찾기
			String sbName = dao.findSbNameById(sbNum);
			
			mav.addObject("sbName", sbName);
			mav.addObject("sbNum", sbNum);
			mav.addObject("mode", "write");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/lectureReview/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {	
		// 글등록하기 : 넘어온 파라미터 - content, rating, sbNum
		
		LectureReviewDAO dao = new LectureReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			LectureReviewDTO dto = new LectureReviewDTO();
			
			dto.setUserId(info.getUserId());
			
			// 파라미터
			dto.setContent(req.getParameter("content"));
			dto.setRating(Integer.parseInt(req.getParameter("rating")));
			
			int sbNum = Integer.parseInt(req.getParameter("sbNum"));
			
			dao.insertLectureReview(dto, info.getUserId(), sbNum);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/lectureReview/list");
	}
	
	// 글보기
	@RequestMapping(value = "/lectureReview/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("lectureReview/article");
		
		
		return mav;
	}
	
	
}
