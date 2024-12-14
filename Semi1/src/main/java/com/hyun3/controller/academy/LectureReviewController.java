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
		
		try {
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String userId = info.getUserId();
			
			List<LectureReviewDTO> list = null;
			list = dao.listLecture(userId);
			
			mav.addObject("list", list);
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/lectureReview/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("lectureReview/write");
		
		try {
			LectureReviewDAO dao = new LectureReviewDAO();
			long sbNum = Long.parseLong(req.getParameter("sbNum"));
			
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
		// 글등록하기 : 넘어온 파라미터 - content, rating, sbName
		
		ModelAndView mav = new ModelAndView("lectureReview/write");
		
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
		
		return mav;
	}
	
}
