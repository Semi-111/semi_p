package com.hyun3.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.hyun3.dao.board.InfoBoardDAO;
import com.hyun3.dao.board.SecretBoardDAO;
import com.hyun3.dao.board.StudentBoardDAO;
import com.hyun3.dao.lesson.LessonDAO;
import com.hyun3.dao.notice.NoticeDAO;
import com.hyun3.domain.board.InfoBoardDTO;
import com.hyun3.domain.board.SecretBoardDTO;
import com.hyun3.domain.board.StudentBoardDTO;
import com.hyun3.domain.lesson.LessonDTO;
import com.hyun3.domain.notice.NoticeDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
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
		List<InfoBoardDTO> freeBoard = infodao.listBoard("free", 0, 5); // 자유
		List<InfoBoardDTO> infoBoard = infodao.listBoard("info", 0, 5); // 정보

		SecretBoardDAO secretdao = new SecretBoardDAO();
		List<SecretBoardDTO> secretBoard = secretdao.listBoard("SECRET", 0, 5); // 비밀

		// 공지
		NoticeDAO noticedao = new NoticeDAO();

		StudentBoardDAO studentdao = new StudentBoardDAO();
		List<StudentBoardDTO> studentBoard = studentdao.listBoard1("student", 0, 5); // 새내기
		List<StudentBoardDTO> oldbieBoard = studentdao.listBoard1("oldbie", 0, 5); // 졸업생

		// List<StudentBoardDTO> oldbieBoard = studentdao.listBoard("oldbie", 0, 5,
		// null, null, null); // 졸업생

		// 졸업

		LessonDAO lessondao = new LessonDAO(); // 학과별
		try {
			List<LessonDTO> lessonBoard = lessondao.listBoard(0, 5);

			List<NoticeDTO> noticeBoard = noticedao.listBoard(0, 5, "0");

			mav.addObject("lessonBoard", lessonBoard);
			mav.addObject("noticeBoard", noticeBoard);
			mav.addObject("freeBoard", freeBoard);
			mav.addObject("infoBoard", infoBoard);
			mav.addObject("secretBoard", secretBoard);
			mav.addObject("studentBoard", studentBoard);
			mav.addObject("oldbieBoard", oldbieBoard);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/main/lucky", method = RequestMethod.GET)
	public ModelAndView lucky(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("main/lucky");

	    // 이미지 파일명 목록
	    String[] images = {
	        "/resources/images/lucky/test1.jpg",
	        "/resources/images/lucky/test2.jpg",
	        "/resources/images/lucky/test3.jpg",
	        "/resources/images/lucky/test4.jpg",
	        "/resources/images/lucky/test5.jpg",
	        "/resources/images/lucky/test6.jpg",
	        "/resources/images/lucky/test7.jpg"
	    };

	    // 랜덤 인덱스 선택
	    int randomIndex = (int) (Math.random() * images.length);
	    String selectedImage = images[randomIndex];

	    // 모델에 랜덤 이미지 추가
	    mav.addObject("selectedImage", selectedImage);

	    return mav;
	}
}