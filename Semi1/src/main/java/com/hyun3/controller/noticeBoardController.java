package com.hyun3.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class noticeBoardController {

	// 공지게시판 이동
	@RequestMapping(value = "/noticeBoard/list")
		public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		ModelAndView mav = new ModelAndView("noticeBoard/list");
		
		return mav;
		
	}
}
