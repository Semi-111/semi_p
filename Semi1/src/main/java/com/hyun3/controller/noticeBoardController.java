package com.hyun3.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class noticeBoardController {

	// 공지게시판 이동
	@RequestMapping(value = "/noticeBoard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {

		ModelAndView mav = new ModelAndView("notice/list");

		return mav;
	}

	// 공지작성(글쓰기폼) 이동
	@RequestMapping(value = "/noticeBoard/writeForm", method = RequestMethod.GET)
	public ModelAndView noticeWriteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {

		ModelAndView mav = new ModelAndView("notice/write");

		return mav;
	}

	// 공지작성(insert)
	@RequestMapping(value = "/noticeBoard/writeSubmit", method = RequestMethod.POST)
	public ModelAndView noticeWriteSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		// 넘어오는 파라미터 글
		
		return new ModelAndView("redirect/noticeBoard/list"); 
	}
}
