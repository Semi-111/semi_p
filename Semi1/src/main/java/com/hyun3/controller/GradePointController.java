package com.hyun3.controller;

import java.io.IOException;

import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class GradePointController {
	
	@RequestMapping(value = "/grade")
	public ModelAndView 메소드명(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("폴더명/파일명");
		
		return mav;
	}
}
