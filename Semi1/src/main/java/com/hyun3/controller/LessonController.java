package com.hyun3.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;

import com.hyun3.dao.LessonDAO;
import com.hyun3.domain.LessonDTO;
import com.hyun3.domain.LessonLikeDTO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.FileManager;
import com.hyun3.util.MyMultipartFile;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class LessonController {
	// 카테고리별 학과게시판으로 이동
	@RequestMapping(value = "/lessonBoard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	   ModelAndView mav = new ModelAndView("lesson/list");
	   
	   // 세션에서 사용자 정보 가져오기
	   HttpSession session = req.getSession();
	   SessionInfo info = (SessionInfo)session.getAttribute("member");
	   
	   // 디버깅용 출력
	    System.out.println("===== 세션 디버깅 =====");
	    System.out.println("세션 ID: " + session.getId());
	    System.out.println("로그인 사용자 정보: " + info);
	    if(info != null) {
	        System.out.println("사용자 학과 번호: " + info.getLessonNum());
	    } else {
	        System.out.println("로그인 정보가 없습니다.");
	    }
	    System.out.println("===================");
	   
	   LessonDAO dao = new LessonDAO();
	   MyUtil util = new MyUtilBootstrap();
	   
	   String page = req.getParameter("page");
	   String categoryParam = req.getParameter("category");
	   
	   int current_page = 1;
	   if (page != null) {
	       current_page = Integer.parseInt(page);
	   }
	   
	   // 카테고리 파라미터 처리
	   int category = 0;
	   if (categoryParam != null && !categoryParam.isEmpty()) {
	       category = Integer.parseInt(categoryParam);
	       
	       // 선택된 카테고리가 있고, 그것이 자신의 학과가 아닌 경우
	       if (category != 0 && category != info.getLessonNum()) {
	           resp.setContentType("text/html;charset=utf-8");
	           PrintWriter out = resp.getWriter();
	           out.println("<script>");
	           out.println("alert('타 학과 학생은 이용이 불가능합니다.');");
	           out.println("location.href='" + req.getContextPath() + "/lessonBoard/list';");
	           out.println("</script>");
	           return null;
	       }
	   }

	   String schType = req.getParameter("schType");
	   String kwd = req.getParameter("kwd");
	   if (schType == null) {
	       schType = "all";
	       kwd = "";
	   }
	   
	   int size = 10;
	   int total_page = 0;
	   int dataCount = 0;
	   
	   try {
	       if (category > 0) {
	           // 카테고리별 조회
	           dataCount = dao.dataCountByCategory(category);
	       } else if (kwd != null && kwd.length() != 0) {
	           // 검색어 조회
	           dataCount = dao.dataCount(schType, kwd);
	       } else {
	           // 전체 조회
	           dataCount = dao.dataCount();
	       }
	       
	       total_page = util.pageCount(dataCount, size);
	       
	       if (current_page > total_page) {
	           current_page = total_page;
	       }
	       
	       int offset = (current_page - 1) * size;
	       if (offset < 0) offset = 0;
	       
	       List<LessonDTO> list = null;
	       
	       if (category > 0) {
	           // 카테고리별 리스트
	           list = dao.listBoardByCategory(offset, size, category);
	       } else if (kwd != null && kwd.length() != 0) {
	           // 검색 리스트
	           list = dao.listBoard(offset, size, schType, kwd);
	       } else {
	           // 전체 리스트
	           list = dao.listBoard(offset, size);
	       }
	       
	       String query = "";
	       if (category > 0) {
	           query = "category=" + category;
	       } else if (kwd != null && kwd.length() != 0) {
	           query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	       }
	       
	       String listUrl = req.getContextPath() + "/lessonBoard/list";
	       String paginationUrl = listUrl;
	       if (query.length() != 0) {
	           paginationUrl += "?" + query;
	       }
	       
	       String paging = util.paging(current_page, total_page, listUrl);
	       
	       String articleUrl = req.getContextPath() + "/lessonBoard/article?page=" + current_page;
	       if (query.length() != 0) {
	           articleUrl += "&" + query;
	       }
	       
	       mav.addObject("list", list);
	       mav.addObject("page", current_page);
	       mav.addObject("total_page", total_page);
	       mav.addObject("dataCount", dataCount);
	       mav.addObject("size", size);
	       mav.addObject("category", category);  // 현재 선택된 카테고리
	       mav.addObject("paginationUrl", paginationUrl);
	       mav.addObject("paging", paging);
	       mav.addObject("articleUrl", articleUrl);
	       mav.addObject("query", query);
	       mav.addObject("userLessonNum", info.getLessonNum());  // 사용자의 학과 번호
	       
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	   
	   return mav;
	}

	// 글쓰기 폼으로 이동
	@RequestMapping(value = "/lessonBoard/writeForm", method = RequestMethod.GET)
	public ModelAndView lessonWriteForm(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException, SQLException {
	    ModelAndView mav = new ModelAndView("lesson/write");
	    
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo)session.getAttribute("member");
	    
	    // 사용자의 학과 번호를 뷰로 전달
	    mav.addObject("userLessonNum", info.getLessonNum());
	    
	    return mav;
	}

	// 글 작성
	@RequestMapping(value = "/lessonBoard/writeForm", method = RequestMethod.POST)
	public ModelAndView lessonWriteSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		LessonDAO dao = new LessonDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		FileManager fileManager = new FileManager();

		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo";

		try {
			LessonDTO dto = new LessonDTO();

			// DTO에 데이터 저장
			dto.setTitle(req.getParameter("title"));
			dto.setBoard_content(req.getParameter("content")); // 수정 // 글 내용
			dto.setMb_num(info.getMb_Num());
			dto.setDivision("lesson"); // 학과게시판이라는 것을 알려줌
			// 학과 : 경영학(51) 경창행정과 (52) 디자인학과 (53) 화학공학 (54) 컴퓨터응용전자과 (55) 정보통신학부 (56)
			dto.setLessonNum(Integer.parseInt(req.getParameter("category")));

			// 파일 업로드 처리
			String filename = null;
			Part p = req.getPart("selectFile");

			// FileManager를 사용하여 파일 업로드
			MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
			if (multiPart != null) {
				// 저장된 파일명
				filename = multiPart.getSaveFilename();
			}

			// 파일명을 DTO에 저장
			if (filename != null) {
				dto.setFileName(filename);
			}

			// 데이터베이스에 게시물 정보 저장
			dao.insertLesson(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/lessonBoard/list");
	}

	@RequestMapping(value = "/lessonBoard/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("lesson/article");
		HttpSession session = req.getSession();

		String page = req.getParameter("page");
		String query = "page=" + page;

		String schType = req.getParameter("schType");
		String kwd = req.getParameter("kwd");
		if (schType == null) {
			schType = "all";
			kwd = "";
		}

		if (kwd != null && kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}

		LessonDAO dao = new LessonDAO();

		try {
			long num = Long.parseLong(req.getParameter("cm_num"));
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			LessonDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/lessonBoard/list?" + query);
			}

			// 좋아요 정보 설정
			dto.setUserLiked(dao.getUserLike(info.getMb_Num(), num));
			// 좋아요 개수
			dto.setLikeCount(dao.countLikes(num));

			// 이전글, 다음글
			LessonDTO prevDto = dao.findByPrev(num, schType, kwd);
			LessonDTO nextDto = dao.findByNext(num, schType, kwd);

			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/lessonBoard/delete", method = RequestMethod.GET)
	public ModelAndView lessonDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LessonDAO dao = new LessonDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		FileManager fileManager = new FileManager();

		String page = req.getParameter("page");

		// 파일 저장경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo";

		try {
			long cm_num = Long.parseLong(req.getParameter("cm_num")); // 글번호

			// 게시물 정보 가져오기
			LessonDTO dto = dao.findById(cm_num);

			// 게시글 정보가 없으면
			if (dto == null) {
				return new ModelAndView("redirect:/lessonBoard/list?page=" + page);
			}

			// 게시물을 올린 사용자나 관리자가 아니면
			// mb_num은 long 타입
			if (dto.getMb_num() != info.getMb_Num() && Integer.parseInt(info.getRole()) < 100) {
				fileManager.doFiledelete(pathname, dto.getFileName());
			}

			// 테이블 데이터 삭제
			dao.deleteLesson(cm_num);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/lessonBoard/list?page" + page);
	}

	// 글 수정
	@RequestMapping(value = "/lessonBoard/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LessonDAO dao = new LessonDAO();

		String page = req.getParameter("page");
		long cm_num = Long.parseLong(req.getParameter("cm_num"));

		ModelAndView mav = new ModelAndView("lesson/write");

		try {
			LessonDTO dto = dao.findById(cm_num);
			if (dto == null) {
				return new ModelAndView("redirect:/lessonBoard/list?page" + page);
			}

			// 게시물을 작성한 사용자가 아니면
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info.getMb_Num() != dto.getMb_num() && Integer.parseInt(info.getRole()) < 51) {
				return new ModelAndView("redirect:/lessonBoard/list?page=" + page);
			}

			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/lessonBoard/list?page=" + page);
	}

	// 수정한 글 전달받기
	@RequestMapping(value = "/lessonBoard/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LessonDAO dao = new LessonDAO();

		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();

		String page = req.getParameter("page");
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo";

		try {
			LessonDTO dto = new LessonDTO();

			dto.setCm_num(Long.parseLong(req.getParameter("cm_num")));
			dto.setTitle(req.getParameter("title"));
			dto.setBoard_content(req.getParameter("content"));
			dto.setLessonNum(Integer.parseInt(req.getParameter("category")));

			// 파일 업로드 처리
			String filename = null;
			Part p = req.getPart("selectFile");

			// FileManager를 사용하여 파일 업로드
			MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
			if (multiPart != null) {
				// 저장된 파일명
				filename = multiPart.getSaveFilename();
			}

			// 파일명을 DTO에 저장
			if (filename != null) {
				dto.setFileName(filename);
			}

			dao.updateLesson(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/lessonBoard/list?page=" + page);
	}

	// 좋아요
	@RequestMapping(value = "/lessonBoard/like")
	public void like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LessonDAO dao = new LessonDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "true";
		int likeCount = 0;

		try {
			long cm_num = Long.parseLong(req.getParameter("cm_num"));
			LessonLikeDTO dto = new LessonLikeDTO();
			dto.setCm_num(cm_num);
			dto.setMb_num(info.getMb_Num());

			boolean liked = dao.getUserLike(info.getMb_Num(), cm_num);

			if (liked) {
				dao.deleteLike(dto);
			} else {
				dao.insertLike(dto);
			}

			// 좋아요 개수 가져오기
			likeCount = dao.countLikes(cm_num);

		} catch (Exception e) {
			state = "false";
		}

		JSONObject job = new JSONObject();
		job.put("state", state);
		job.put("likeCount", likeCount);

		resp.setContentType("application/json; charset=utf-8");
		resp.getWriter().write(job.toString());
	}
}
