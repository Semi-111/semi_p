package com.hyun3.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.hyun3.dao.LessonDAO;
import com.hyun3.dao.board.InfoBoardDAO;
import com.hyun3.domain.LessonDTO;
import com.hyun3.domain.board.InfoBoardDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("main/main");

		InfoBoardDAO infodao = new InfoBoardDAO();
		String boardType = req.getParameter("type");
		List<InfoBoardDTO> listBoard = infodao.listBoard(boardType, 0, 5);

		/* SecretBoardDAO secretdao = new SecretBoardDAO(); */

		LessonDAO lessondao = new LessonDAO();
		try {
			List<LessonDTO> lessonBoard = lessondao.listBoard(0, 5);
			mav.addObject("lessonBoard", lessonBoard);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		 * StudentBoardDAO studentdao = new StudentBoardDAO(); String boardType =
		 * req.getParameter("type"); List<StudentBoardDTO> listBoard =
		 * studentdao.listBoard(boardType, 0, 5, , );
		 */
		

		mav.addObject("listBoard", listBoard);
		return mav;
	}

}