package com.hyun3.controller;

import java.io.IOException;
import java.util.List;

import com.hyun3.dao.LessonDAO;
import com.hyun3.dao.board.StudentBoardDAO;
import com.hyun3.domain.LessonDTO;
import com.hyun3.domain.board.StudentBoardDTO;
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

		/*
		 * LessonDAO dao = new LessonDAO(); List<LessonDTO> listBoard = dao.listBoard(0,
		 * 5);
		 * 
		 * StudentBoardDAO dao = new StudentBoardDAO(); List<StudentBoardDTO>
		 */
		
		/*
		 * StudentBoardDAO dao = new StudentBoardDAO(); List<StudentBoardDAO>
		 * listStudent = dao.listBoard(0, 5);
		 * 
		 * PhotoDAO photoDAO = new PhotoDAO(); List<PhotoDTO> listPhoto =
		 * photoDAO.listPhoto(0, 5);
		 * 
		 * NoticeDAO noticeDAO = new NoticeDAO(); List<NoticeDTO> listNotice =
		 * noticeDAO.listNotice(0, 5);
		 * 
		 * BoardDAO boardDAO = new BoardDAO(); List<BoardDTO> listBoard =
		 * boardDAO.listBoard(0, 5);
		 * 
		 * LectureDAO lectureDAO = new LectureDAO(); List<LectureDTO> listLecture =
		 * lectureDAO.listLecture();
		 * 
		 * mav.addObject("listPhoto", listPhoto); mav.addObject("listNotice",
		 * listNotice); mav.addObject("listBoard", listBoard);
		 * mav.addObject("listLecture", listLecture);
		 */
/*		mav.addObject("listBoard", listBoard);
*/		return mav;
	}

}