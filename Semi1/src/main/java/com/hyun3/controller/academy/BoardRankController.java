package com.hyun3.controller.academy;

import java.io.IOException;
import java.util.List;

import com.hyun3.dao.academy.LectureReviewDAO;
import com.hyun3.domain.academy.LectureReviewDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BoardRankController {
	
	@RequestMapping(value = "/main/rightReview" , method = RequestMethod.GET)
	public ModelAndView 메소드명(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("main/rightSide");
		
		LectureReviewDAO ReviewDAO = new LectureReviewDAO();
		List<LectureReviewDTO> listReview = ReviewDAO.listReview(0, 3);
		
		mav.addObject("listReview", listReview);
	
		
		return mav;
	}
	
}
