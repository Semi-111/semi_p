package com.hyun3.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// 학과게시판으로 이동
public class LessonController {
	@RequestMapping(value = "/lessonBoard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		ModelAndView mav = new ModelAndView("lesson/list");
		
		return mav;
	}
}
