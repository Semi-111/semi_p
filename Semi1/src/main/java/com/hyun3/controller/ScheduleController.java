package com.hyun3.controller;

import java.io.IOException;
import java.util.List;

import com.hyun3.dao.schedule.ScheduleDAO;
import com.hyun3.domain.schedule.ScheduleDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ScheduleController {

	@RequestMapping(value = "/schedule/schedule2", method = RequestMethod.GET)
	public ModelAndView stClass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("schedule/schedule2");  // test/schedule2 JSP를 반환

	    ScheduleDAO dao = new ScheduleDAO();
	    try {
	        List<ScheduleDTO> viewSubject = dao.viewSubject();
	        
	        mav.addObject("viewSubject", viewSubject);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}
	
	
}
