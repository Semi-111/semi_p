package com.hyun3.controller;

import com.hyun3.dao.board.SecretBoardDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.board.ReplyDTO;
import com.hyun3.domain.board.SecretBoardDTO;
import com.hyun3.domain.board.StudentBoardDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.ResponseBody;
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
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyun3.mvc.annotation.RequestMethod.*;
import static java.nio.charset.StandardCharsets.*;

@Controller
public class SecretBoardController {

  private static final String DIVISION_VALUE = "SECRET";

  @RequestMapping("/bbs/secretBoard/list")
  public ModelAndView handleBoardList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = DIVISION_VALUE;

    ModelAndView mav = new ModelAndView("board/secret/list"); // 폴더명 / 파일명

    SecretBoardDAO dao = new SecretBoardDAO();
    MyUtil util = new MyUtilBootstrap();

    try {
      String page = req.getParameter("page");
      int current_page = 1;

      if (page != null) {
        current_page = Integer.parseInt(page); // 페이지 번호가 있으면 변환
      }

      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");

      if (schType == null) {
        schType = "all";
        kwd = "";
      }

      // GET 방식일 경우 디코딩
      if (req.getMethod().equalsIgnoreCase("GET")) {
        kwd = URLDecoder.decode(kwd, UTF_8);
      }

      // 데이터 개수
      int dataCount;
      if (kwd.isEmpty()) {
        dataCount = dao.dataCount(boardType);
      } else {
        dataCount = dao.dataCount(boardType, schType, kwd);
      }

      int size = 10;
      int total_page = util.pageCount(dataCount, size);

      if (current_page > total_page) {
        current_page = total_page;
      }

      int offset = (current_page - 1) * size;
      if (offset < 0)
        offset = 0;

      List<SecretBoardDTO> list = null;
      if (kwd.isEmpty()) {
        list = dao.listBoard(boardType, offset, size);
      } else {
        list = dao.listBoard(boardType, offset, size, schType, kwd);
      }

      // 쿼리
      String query = "";
      if (!kwd.isEmpty()) {
        query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      String cp = req.getContextPath();
      String listUrl = cp + "/bbs/secretBoard/list";
      String articleUrl = cp + "/bbs/secretBoard/article?page=" + current_page;

      if (!query.isEmpty()) {
        listUrl += "?" + query;
        articleUrl += "&" + query;
      }

      String paging = util.paging(current_page, total_page, listUrl);

      formatPostDate(list);

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

  @RequestMapping(value = "/bbs/secretBoard/write", method = GET)
  public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    ModelAndView mav = new ModelAndView("board/secret/write");
    mav.addObject("boardType", DIVISION_VALUE);
    mav.addObject("mode", "write");
    return mav;
  }

  @RequestMapping(value = "/bbs/secretBoard/write", method = POST)
  // 글 작성
  // 넘어온 파라미터 : title, content [,imageFile]
  public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // boardType을 고정된 값으로 설정
    SecretBoardDAO dao = new SecretBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    try {
      SecretBoardDTO dto = new SecretBoardDTO();

      dto.setDivision(DIVISION_VALUE);

      dto.setTitle(req.getParameter("title"));
      dto.setContent(req.getParameter("content"));

      handleFileUpload(req, session, dto); // 이미지 처리

      dao.insertBoard(dto, info.getMb_Num());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/secretBoard/list");
  }

  @RequestMapping(value = "/bbs/secretBoard/article", method = GET)
  public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = DIVISION_VALUE;

    SecretBoardDAO dao = new SecretBoardDAO();
    MyUtil util = new MyUtilBootstrap();

    String page = req.getParameter("page");
    String query = "page=" + page;

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");
      if (schType == null) {
        schType = "all";
        kwd = "";
      }

      kwd = URLDecoder.decode(kwd, UTF_8);

      if (!kwd.isEmpty()) {
        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      dao.updateViews(cmNum);

      // 게시물 가져오기
      SecretBoardDTO dto = dao.findByNum(cmNum);

      if (dto == null) {
        return new ModelAndView("redirect:/bbs/secretBoard/list");
      }

      dto.setContent(util.htmlSymbols(dto.getContent()));

      SecretBoardDTO prevDto = dao.findByPrev(boardType, dto.getCmNum(), schType, kwd);
      SecretBoardDTO nextDto = dao.findByNext(boardType, dto.getCmNum(), schType, kwd);

      HttpSession session = req.getSession();
      SessionInfo info = (SessionInfo) session.getAttribute("member");

      // 좋아요 여부, 좋아요 개수 가져오기
      boolean isUserLike = false;
      int boardLikeCount = dao.countBoardLike(cmNum);

      if (info != null) {
        isUserLike = dao.isUserLiked(cmNum, info.getMb_Num());
      }

      dto.setBoardLikeCount(boardLikeCount);

      ModelAndView mav = new ModelAndView("board/secret/article");

      mav.addObject("dto", dto);
      mav.addObject("page", page);
      mav.addObject("query", query);
      mav.addObject("num", cmNum);
      mav.addObject("prevDto", prevDto);
      mav.addObject("nextDto", nextDto);
      mav.addObject("boardType", boardType);
      mav.addObject("isUserLike", isUserLike);

      // 포워딩
      return mav;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/secretBoard/list");
  }

  @RequestMapping(value = "/bbs/secretBoard/update", method = GET)
  public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionInfo info = getMember(req); // member 불러옴

    String page = req.getParameter("page");

    ModelAndView mav = new ModelAndView("board/secret/write");
    SecretBoardDAO dao = new SecretBoardDAO();

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));

      SecretBoardDTO dto = dao.findByNum(cmNum);

      if (dto == null) {
        return new ModelAndView("redirect:/bbs/secretBoard/list?page=" + page);
      }

      if (dto.getMbNum() != info.getMb_Num()) {
        return new ModelAndView("redirect:/bbs/secretBoard/list?page=" + page);
      }

      mav.addObject("boardType", DIVISION_VALUE);
      mav.addObject("dto", dto);
      mav.addObject("mode", "update");
      mav.addObject("page", page);

      return mav;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/secretBoard/list?page=" + page);
  }

  @RequestMapping(value = "/bbs/secretBoard/update", method = POST)
  public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 넘어온 파라미터 title, content [,imageFile]
    SecretBoardDAO dao = new SecretBoardDAO();
    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    try {
      SecretBoardDTO dto = new SecretBoardDTO();

      dto.setDivision(DIVISION_VALUE); // boardType
      dto.setTitle(req.getParameter("title"));
      dto.setContent(req.getParameter("content"));

      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      dto.setCmNum(cmNum);

      SecretBoardDTO oldDto = dao.findByNum(cmNum); // 기존 게시글
      String oldFile = oldDto.getFileName();
      dto.setFileName(oldFile);

      handleFileUpload(req, session, dto); // 이미지 처리

      dao.updateBoard(dto, info.getMb_Num());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/secretBoard/list");
  }

  @RequestMapping(value = "/bbs/secretBoard/delete", method = GET)
  public ModelAndView deleteBoard(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    SecretBoardDAO dao = new SecretBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    String page = req.getParameter("page");
    String query = "page=" + page;

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");

      if (schType == null) {
        schType = "all";
        kwd = "";
      }

      kwd = URLDecoder.decode(kwd, UTF_8);

      if (!kwd.isEmpty()) {
        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      dao.deleteBoard(cmNum, info.getUserId(), info.getRole());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/secretBoard/list?" + query);
  }

  @ResponseBody
  @RequestMapping(value = "/bbs/secretBoard/insertBoardLike")
  public Map<String, Object> insertBoardLike(HttpServletRequest req, HttpServletResponse resp)
      throws SQLException, IOException {
    Map<String, Object> model = new HashMap<>();

    SecretBoardDAO dao = new SecretBoardDAO();
    SessionInfo info = getMember(req); // member

    String state = "false";
    int boardLikeCount = 0;

    try {
      long cmNum = Long.parseLong(req.getParameter("cm_Num"));
      String userLiked = req.getParameter("userLiked");
      Long mbNum = info.getMb_Num();

      if (userLiked.equals("true")) {
        dao.deleteBoardLike(cmNum, mbNum);
      } else {
        dao.insertBoardLike(cmNum, mbNum);
      }

      boardLikeCount = dao.countBoardLike(cmNum);

      state = "true";

    } catch (SQLException e) {
      state = "liked";
    } catch (Exception e) {
      e.printStackTrace();
    }

    model.put("state", state);
    model.put("boardLikeCount", boardLikeCount);

    return model;
  }

  // 게시글 공감 저장 - AJAX : JSON
  @ResponseBody
  @RequestMapping(value = "/bbs/secretBoard/replyInsert", method = POST)
  public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, Object> model = new HashMap<>();
    String state = "false";

    SecretBoardDAO dao = new SecretBoardDAO();
    HttpSession session = req.getSession();

    try {
      SessionInfo info = (SessionInfo) session.getAttribute("member");

      ReplyDTO dto = new ReplyDTO();

      dto.setContent(req.getParameter("content"));
      dto.setCmNum(Long.parseLong(req.getParameter("cmNum"))); // 게시글번호
      dto.setMbNum(info.getMb_Num());

      dao.insertReply(dto);
      state = "true";
    } catch (Exception e) {
      e.printStackTrace();
    }

    model.put("state", state);
    return model;
  }


  // 댓글 리스트 - AJAX : Text
  @RequestMapping(value = "/bbs/secretBoard/listReply", method = GET)
  public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // 넘어오는 파라미터 : cmNum, pageNo

    SecretBoardDAO dao = new SecretBoardDAO();
    MyUtil util = new MyUtilBootstrap();

    HttpSession session = req.getSession();

    try {
      SessionInfo info = (SessionInfo) session.getAttribute("member");

      String cmNumStr = req.getParameter("cmNum");

      if (cmNumStr == null || cmNumStr.trim().isEmpty()) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "cmNum parameter is missing");
        return null;
      }

      long cmNum;
      try {
        cmNum = Long.parseLong(cmNumStr);
      } catch (NumberFormatException e) {
        System.err.println("Invalid cmNum parameter: " + cmNumStr);
        e.printStackTrace();
        return null;
      }

      String pageNo = req.getParameter("pageNo");
      int current_page = 1;
      if (pageNo != null) {
        try {
          current_page = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
          System.err.println("Invalid pageNo parameter: " + pageNo);
          e.printStackTrace();
          return null;
        }
      }

      int size = 5;
      int total_page = 0;
      int replyCount = 0;

      // 게시글에 달린 댓글 수를 세는 메서드
      replyCount = dao.dataCountReply(cmNum);
      total_page = util.pageCount(replyCount, size);
      if (current_page > total_page) {
        current_page = total_page;
      }

      int offset = (current_page - 1) * size;
      if (offset < 0) offset = 0;

      // 댓글 리스트 가져오기
      List<ReplyDTO> list = dao.listReply(cmNum, offset, size);

      // 엔터를 <br>로 변환
      for (ReplyDTO dto : list) {
        dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
      }

      String paging = util.pagingMethod(current_page, total_page, "listPage");

      ModelAndView mav = new ModelAndView("board/listReply");

      mav.addObject("listReply", list);
      mav.addObject("pageNo", current_page);
      mav.addObject("replyCount", replyCount);
      mav.addObject("total_page", total_page);
      mav.addObject("paging", paging);
      mav.addObject("cmNum", cmNum); // cmNum을 모델에 추가

      return mav;
    } catch (Exception e) {
      e.printStackTrace();
      resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid Parameters");
      throw e;
    }
  }


  @RequestMapping(value = "/bbs/secretBoard/listReplyAnswer", method = GET)
  public ModelAndView listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    SecretBoardDAO dao = new SecretBoardDAO();
    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      HttpSession session = req.getSession();
      SessionInfo info = (SessionInfo) session.getAttribute("member");

      List<ReplyDTO> list = dao.listReplyAnswer(cmNum);

      for (ReplyDTO dto : list) {
        dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
      }

      ModelAndView mav = new ModelAndView("bbs/listReplyAnswer");
      mav.addObject("listReplyAnswer", list);

      return mav;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @ResponseBody
  @RequestMapping(value = "/bbs/secretBoard/countReplyAnswer", method = POST)
  public Map<String, Object> countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Map<String, Object> model = new HashMap<>();

    SecretBoardDAO dao = new SecretBoardDAO();
    int count = 0;

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      count = dao.dataCountReplyAnswer(cmNum);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    model.put("count", count);
    return model;
  }


  @ResponseBody
  @RequestMapping(value = "/bbs/secretBoard/deleteReply", method = POST)
  public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Map<String, Object> model = new HashMap<>();
    SecretBoardDAO dao = new SecretBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");
    String state = "false";

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));

      dao.deleteReply(cmNum, info.getMb_Num(), info.getRole());

      state = "true";
    } catch (Exception e) {
      e.printStackTrace();
    }
    model.put("state", state);
    return model;
  }

  private static void handleFileUpload(HttpServletRequest req, HttpSession session, SecretBoardDTO dto) throws IOException, ServletException {
    String filename = null;

    FileManager fileManager = new FileManager();
    String root = session.getServletContext().getRealPath("/");
    String pathname = root + "uploads" + File.separator + "photo";

    Part p = req.getPart("file");

    // 새 파일이 있는 경우 처리
    if (p != null && p.getSize() > 0) {
      MyMultipartFile multiPart = fileManager.doFileUpload(p, pathname);
      if (multiPart != null) {
        filename = multiPart.getSaveFilename();
      }

      // 기존 파일 삭제
      if (dto.getFileName() != null && !dto.getFileName().isEmpty()) {
        File oldFile = new File(pathname + File.separator + dto.getFileName());
        if (oldFile.exists()) {
          oldFile.delete();
        }
      }

      // 파일 이름 설정
      if (filename != null && !filename.isEmpty()) {
        dto.setFileName(filename); // 새 파일 이름 설정
      }
    }
  }

  private static SessionInfo getMember(HttpServletRequest req) {
    HttpSession session = req.getSession();
    return (SessionInfo) session.getAttribute("member");
  }

  private static void formatPostDate(List<SecretBoardDTO> list) {
    LocalDateTime now = LocalDateTime.now(); // 현재 시간

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (SecretBoardDTO dto : list) {

      try {
        LocalDateTime postDate = LocalDateTime.parse(dto.getCaDate(), dateTimeFormatter); // yyyy-MM-dd HH:mm:ss로 가지고옴

        Duration duration = Duration.between(postDate, now);

        if (duration.toHours() < 12) {
          dto.setFormattedCaDate(postDate.format(timeFormatter));
        } else {
          dto.setFormattedCaDate(postDate.format(dateFormatter));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
