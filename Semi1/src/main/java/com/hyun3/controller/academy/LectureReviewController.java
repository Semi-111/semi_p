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
			
			// 왼쪽 : 내 강의 목록
			List<LectureReviewDTO> myLlist = null;
			myLlist = dao.listLecture(userId);
			
			mav.addObject("myLlist", myLlist);
			
			for(LectureReviewDTO dto : myLlist) {
				long atNum = dto.getAt_Num();
				int reviewCount = dao.reviewCount(atNum);
				dto.setIsWritten(reviewCount > 0 ? 1 : 0); // 리뷰가 작성된 경우 1, 아니면 0
			}
			
			
			
			// 오른쪽 : 모든 강의평가 목록
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int dataCount = dao.dataCount();
			
			// 전체 페이지 수
			int size = 5;
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
			String listUrl = cp + "/lectureReview/list?=page=" + current_page;
			
		
			
			String paging = util.paging(current_page, total_page, listUrl);
			
						
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("reviewList", reviewList);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			
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
	
	
	// 글쓰기폼
	@RequestMapping(value = "/lectureReview/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("lectureReview/write");
		
		try {
			LectureReviewDAO dao = new LectureReviewDAO();
			long atNum = Long.parseLong(req.getParameter("atNum")); // 파라미터
			
			// 수강번호로 과목명,교수명 조회
			LectureReviewDTO dto = dao.findByAtNum(atNum);
			
			mav.addObject("sbName", dto.getSb_Name()); // 과목명
			mav.addObject("pfName", dto.getPf_Name()); // 교수명
			mav.addObject("atNum", dto.getAt_Num()); // 수강번호
			mav.addObject("mode", "write"); // 글쓰기 모드
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	// 글등록
	@RequestMapping(value = "/lectureReview/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {	
		// 글등록하기 : 넘어온 파라미터 - content, rating, atNum
		
		LectureReviewDAO dao = new LectureReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			LectureReviewDTO dto = new LectureReviewDTO();
			
			dto.setUserId(info.getUserId());
			
			// 파라미터
			dto.setContent(req.getParameter("content"));
			dto.setRating(Integer.parseInt(req.getParameter("rating")));
			
			long atNum = Long.parseLong(req.getParameter("atNum"));
			dto.setAt_Num(atNum);
			dao.insertLectureReview(dto);
			
			
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
		
		LectureReviewDAO dao = new LectureReviewDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			// review_num과 page 파라미터 받기
	        long reviewNum = Long.parseLong(req.getParameter("review_num"));
	        String page = req.getParameter("page");
	        String query = "page=" + page;
	        
	        
	        // 리뷰 상세 정보 가져오기
	        LectureReviewDTO dto = dao.findByRvNum(reviewNum);
	        
	        // 리뷰가 없는 경우 처리
	        if (dto == null) {
	            return new ModelAndView("redirect:/lectureReview/list?" + query);
	        }
	        
	        // 줄바꿈 처리
	        dto.setContent(util.htmlSymbols(dto.getContent()));
	        
			
	        // JSP로 데이터 전달
	        mav.addObject("dto", dto); // 리뷰 상세 정보
	        mav.addObject("page", page); // 현재 페이지 번호
	        mav.addObject("query", query); // 페이징 쿼리 문자열
						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return mav;
	}

	
	// 수정폼
	@RequestMapping(value = "/lectureReview/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("lectureReview/write");
		LectureReviewDAO dao = new LectureReviewDAO();
			
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String page = req.getParameter("page");
			
			// System.out.println(page);
			
			long reviewNum = Long.parseLong(req.getParameter("reviewnum"));
			LectureReviewDTO dto = dao.findByRvNum(reviewNum);
			
			
	        if(dto == null) {
	        	return new ModelAndView("redirect:/lectureReview/list?page=" + page);
	        }
	        
	        if(! dto.getUserId().equals(info.getUserId())) {
	        	return new ModelAndView("redirect:/lectureReview/list?page=" + page);
	        }
	        
	        
	        // sbName, rating 을 넘기지 않으면 수정페이지에서 나오지 않는다
	        mav.addObject("sbName", dto.getSb_Name());
	        mav.addObject("rating", dto.getRating());
	        
	        mav.addObject("page", page);
	        mav.addObject("dto", dto);       // 리뷰 상세 정보 전달
            mav.addObject("mode", "update"); // 수정 모드 설정
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return mav;
	}
	
	// 수정완료
	@RequestMapping(value = "/lectureReview/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		LectureReviewDAO dao = new LectureReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		if (page == null || page.isEmpty()) {
	        page = "1"; // 기본값 설정
	    }
		
		try {
			LectureReviewDTO dto = new LectureReviewDTO();
			
			dto.setReview_Num(Long.parseLong(req.getParameter("reviewnum")));
			dto.setRating(Integer.parseInt(req.getParameter("rating")));
			dto.setContent(req.getParameter("content"));
			
			dto.setUserId(info.getUserId());
			
			dao.updateReview(dto);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/lectureReview/list?page=" + page);
	}
	
	@RequestMapping(value = "/lectureReview/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("lectureReview/write");
		LectureReviewDAO dao = new LectureReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		

		
		try {
			long reviewNum = Long.parseLong(req.getParameter("reviewNum"));
			
			dao.deleteReview(reviewNum, info.getUserId());
			
			mav.addObject("mode", "update");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return new ModelAndView("redirect:/lectureReview/list");
	}
	
}
