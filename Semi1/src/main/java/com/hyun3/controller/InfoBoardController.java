package com.hyun3.controller;

import com.hyun3.dao.board.InfoBoardDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.board.InfoBoardDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
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

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.hyun3.mvc.annotation.RequestMethod.*;
import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;

@Controller
public class InfoBoardController {

  @RequestMapping("/bbs/infoBoard/list")
  // http://localhost:9090/bbs/infoBoard/list?type=free - 자유게시판
  // http://localhost:9090/bbs/infoBoard/list?type=info - 정보게시판
  public ModelAndView handleBoardList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // 게시글 리스트 : 파라미터 : [page], [schType, kwd]
    String boardType = req.getParameter("type");

    httpError(resp, boardType);

    // ModelAndView 객체 생성
    ModelAndView mav = new ModelAndView("board/list"); // 폴더명 / 파일명

    InfoBoardDAO dao = new InfoBoardDAO();
    MyUtil util = new MyUtilBootstrap();

    try {
      String page = req.getParameter("page");
      int current_page = 1;

      if(page != null) {
        current_page = Integer.parseInt(page); // 페이지 번호가 있으면 변환
      }

      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");

      if(schType == null) {
        schType = "all";
        kwd = "";
      }

      // GET 방식일 경우 디코딩
      if(req.getMethod().equalsIgnoreCase("GET")) {
        kwd = URLDecoder.decode(kwd , UTF_8);
      }

      // 데이터 개수
      int dataCount;
      if(kwd.isEmpty()) {
        dataCount = dao.dataCount(); // 키워드가 없으면 전체 데이터 개수 반환
      } else {
        dataCount = dao.dataCount(schType, kwd); // 검색 조건에 따른 데이터 개수 반환
      }

      int size = 10;
      int total_page = util.pageCount(dataCount, size);

      if(current_page > total_page) {
        current_page = total_page;
      }

      // 데이터베이스에서 가져올 데이터의 시작 위치(offset) 계산
      int offset = (current_page - 1) * size;
      if(offset < 0) offset = 0;

      List<InfoBoardDTO> list = null;
      if(kwd.isEmpty()) {
        list = dao.listBoard(offset, size);
      } else {
        list = dao.listBoard(offset, size, schType, kwd);
      }

      // 쿼리
      String query = "";
      if(!kwd.isEmpty()) {
        query = "schType=" + schType +"&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      String cp = req.getContextPath();
      String listUrl = cp + "/bbs/infoBoard/list?type=" + boardType;
      String articleUrl = cp + "/bbs/infoBoard/article?type=" + boardType + "&page=" + current_page;

      if(!query.isEmpty()) {
        listUrl += "?" + query;
        articleUrl += "&" + query;
      }

      // /infoBoard/board/list?schType=subject&kwd=Java
      // /infoBoard/board/article?page=1&schType=subject&kwd=Java

      String paging = util.paging(current_page, total_page, listUrl);

      // 포워딩할 JSP에 전달할 속성
      mav.addObject("list", list);
      mav.addObject("page", current_page);
      mav.addObject("total_page", total_page);
      mav.addObject("dataCount", dataCount);
      mav.addObject("size", size);
      mav.addObject("articleUrl", articleUrl);
      mav.addObject("paging", paging);
      mav.addObject("schType", schType);
      mav.addObject("kwd", kwd);
      mav.addObject("boardType", boardType);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mav;
  }

  @RequestMapping(value = "/bbs/infoBoard/write", method = GET)
  public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // 글 쓰기 폼
    String boardType = req.getParameter("type");

    httpError(resp, boardType);


    ModelAndView mav = new ModelAndView("board/write");
    mav.addObject("boardType", boardType);
    mav.addObject("mode", "write");
    return mav;
  }

  @RequestMapping(value = "/bbs/infoBoard/write", method = POST)
  // 글 작성
  // 넘어온 파라미터 : title, content [,imageFile]
  public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("boardType");
    InfoBoardDAO dao = new InfoBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    FileManager fileManager = new FileManager();

    // 파일 저장 경로
    String root = session.getServletContext().getRealPath("/");
    String pathname = root + "uploads" + File.separator + "photo";

    httpError(resp, boardType);

    try {
      InfoBoardDTO dto = new InfoBoardDTO();

      dto.setDivision(boardType); // boardType 설정
      dto.setTitle(req.getParameter("title")); // 제목
      dto.setContent(req.getParameter("content")); // 내용

      String filename = null;

      // 파일 업로드
      Part p = req.getPart("file");
      MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
      if(multiPart != null) {
        filename = multiPart.getSaveFilename();
      }

      if(filename != null) {
        dto.setFileName(filename); // 이미지 파일명
      }

      System.out.println(pathname);


      dao.insertBoard(dto, info.getUserId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/infoBoard/list?type=" + boardType);
  }

  @RequestMapping(value = "/bbs/infoBoard/article", method = GET)
  public ModelAndView 메서드명(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // 넘어오는 파라미터: 글번호(num), 페이지(page), 제목(title), 내용(content) [, 이미지(imageFile), schType, kwd]
    String boardType = req.getParameter("type");

    InfoBoardDAO dao = new InfoBoardDAO();
    MyUtil util = new MyUtilBootstrap();

    String page = req.getParameter("page");
    String query = "page=" + page;

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");
      if(schType == null) {
        schType = "all";
        kwd = "";
      }

      kwd = URLDecoder.decode(kwd, UTF_8);

      if(!kwd.isEmpty()) {
        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      dao.updateViews(cmNum);

      // 게시물 가져오기
      InfoBoardDTO dto = dao.findByNum(cmNum);

      if(dto == null) {
        return new ModelAndView("redirect:/bbs/infoBoard/list?type=" + boardType);
      }

      dto.setContent(util.htmlSymbols(dto.getContent()));

      InfoBoardDTO prevDto = dao.findByPrev(dto.getCmNum(), schType, kwd);
      InfoBoardDTO nextDto = dao.findByNext(dto.getCmNum(), schType, kwd);

      ModelAndView mav = new ModelAndView("board/article");

      mav.addObject("dto", dto);
      mav.addObject("page", page);
      mav.addObject("query", query);
      mav.addObject("num", cmNum);
      mav.addObject("prevDto", prevDto);
      mav.addObject("nextDto", nextDto);
      mav.addObject("boardType", boardType);

      // 포워딩
      return mav;

    } catch (Exception e) {
     e.printStackTrace();
    }

//    return new ModelAndView("redirect:/bbs/infoBoARD/list?type=" + query);
    return new ModelAndView("redirect:/bbs/infoBoard/list?type=" + boardType + query);
  }


  private static void httpError(HttpServletResponse resp, String boardType) throws IOException {
    if (boardType == null || (!boardType.equals("free") && !boardType.equals("info"))) {
      resp.sendError(SC_FORBIDDEN); // 403
    }
  }
}
