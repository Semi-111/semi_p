package com.hyun3.controller;

import com.hyun3.dao.board.StudentBoardDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.board.InfoBoardDTO;
import com.hyun3.domain.board.StudentBoardDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.FileManager;
import com.hyun3.util.MyMultipartFile;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.mvc.annotation.RequestMethod.POST;
import static java.nio.charset.StandardCharsets.*;

@Controller
public class StudentController {

  private StudentBoardDAO dao = new StudentBoardDAO();
  private MyUtilBootstrap util = new MyUtilBootstrap();

  // 게시판 리스트
  @RequestMapping("/bbs/studentBoard/list")
  public ModelAndView handleStudentBoardList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("type"); // 'student' 또는 'oldbie'
    String category = req.getParameter("category"); // 학번 카테고리

    ModelAndView mav = new ModelAndView("board/student/list");

    try {
      String pageParam = req.getParameter("page");
      int current_page = 1;

      if (pageParam != null) {
        current_page = Integer.parseInt(pageParam);
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
        if ("student".equals(boardType)) {
          if (category != null) {
            category = URLDecoder.decode(category, UTF_8);
          }
        }
      }

      // 데이터 개수
      int dataCount = dao.dataCount(boardType, category, schType, kwd);

      int size = 10;
      int total_page = util.pageCount(dataCount, size);

      if (current_page > total_page) {
        current_page = total_page;
      }

      // 데이터베이스에서 가져올 데이터의 시작 위치(offset) 계산
      int offset = (current_page - 1) * size;
      if (offset < 0) offset = 0;

      List<StudentBoardDTO> list = dao.listBoard(boardType, offset, size, category, schType, kwd);



      String query = "";
      if (!kwd.isEmpty()) {
        query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      if ("student".equals(boardType)) {
        if (category != null && !category.isEmpty()) {
          query += "&category=" + URLEncoder.encode(category, UTF_8);
        }
      }

      String cp = req.getContextPath();
      String listUrl = cp + "/bbs/studentBoard/list?type=" + boardType;
      String articleUrl = cp + "/bbs/studentBoard/article?type=" + boardType + "&page=" + current_page;

      if (!query.isEmpty()) {
        listUrl += "&" + query;
        articleUrl += "&" + query;
      }

      String paging = util.paging(current_page, total_page, listUrl);

      // 카테고리 목록 가져오기 (student 타입일 경우)
      List<StudentBoardDTO> categories = null;
      if ("student".equals(boardType)) {
        categories = dao.listCategories();
        mav.addObject("categories", categories);
      }

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
      mav.addObject("category", category);
      mav.addObject("boardType", boardType);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return mav;
  }

  // 게시글 상세보기
  @RequestMapping("/bbs/studentBoard/article")
  public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("type"); // 'student' 또는 'oldbie'
    String category = req.getParameter("category"); // 학번 카테고리

    ModelAndView mav = new ModelAndView("board/student/article");

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

      if(!kwd.isEmpty()) {
        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      kwd = URLDecoder.decode(kwd, UTF_8);

      if ("student".equals(boardType)) {
        if (category != null) {
          category = URLDecoder.decode(category, UTF_8);
        }
      }

      // 조회수 증가
      dao.updateViews(cmNum);

      // 게시물 가져오기
      StudentBoardDTO dto = dao.findByNum(cmNum);

      if (dto == null) {
        return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + boardType);
      }

      dto.setContent(util.htmlSymbols(dto.getContent()));

      StudentBoardDTO prevDto = dao.findByPrev(boardType, dto.getCmNum(), category, schType, kwd);
      StudentBoardDTO nextDto = dao.findByNext(boardType, dto.getCmNum(), category, schType, kwd);

      HttpSession session = req.getSession();
      SessionInfo info = (SessionInfo) session.getAttribute("member");

      // 좋아요 여부, 좋아요 개수 가져오기
      boolean isUserLike = false;
      int boardLikeCount = dao.countBoardLike(cmNum);

      if (info != null) {
        isUserLike = dao.isUserLiked(cmNum, info.getMb_Num());
      }

      dto.setBoardLikeCount(boardLikeCount);

      mav.addObject("dto", dto);
      mav.addObject("page", page);
      mav.addObject("query", query);
      mav.addObject("num", cmNum);
      mav.addObject("prevDto", prevDto);
      mav.addObject("nextDto", nextDto);
      mav.addObject("boardType", boardType);
      mav.addObject("category", category);
      mav.addObject("isUserLike", isUserLike);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return mav;
  }

  // 게시글 쓰기 폼
  @RequestMapping(value = "/bbs/studentBoard/write", method = GET)
  public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("type"); // 'student' 또는 'oldbie'

    ModelAndView mav = new ModelAndView("board/student/write");
    mav.addObject("type", boardType);

    // 카테고리 목록 가져오기 (student 타입일 경우)
    if ("student".equals(boardType)) {
      List<StudentBoardDTO> categories = dao.listCategories();
      mav.addObject("categories", categories);
    }

    mav.addObject("mode", "write");
    return mav;
  }

  // 게시글 쓰기 제출
  @RequestMapping(value = "/bbs/studentBoard/write", method = POST)
  public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String type = req.getParameter("type"); // 'student' 또는 'oldbie'
    StudentBoardDAO dao = new StudentBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    try {
      StudentBoardDTO dto = new StudentBoardDTO();

      dto.setDivision(type); // 게시판 유형 설정
      dto.setTitle(req.getParameter("title")); // 제목
      dto.setContent(req.getParameter("content")); // 내용

      // student 타입일 경우 카테고리 설정
      if ("student".equals(type)) {
        String category = req.getParameter("category");
        if (category != null && !category.isEmpty()) {
          dto.setCategoryNum(Integer.parseInt(category));
        } else {
          dto.setCategoryNum(11); // 카테고리 없음
        }
      } else {
        dto.setCategoryNum(11); // oldbie 게시판은 카테고리 없음
      }

      handleFileUpload(req, session, dto); // 이미지 처리

      dao.insertBoard(dto, info.getUserId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + type);
  }

  // 게시글 수정 폼
  @RequestMapping(value = "/bbs/studentBoard/update", method = GET)
  public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("type"); // 'student' 또는 'oldbie'
    SessionInfo info = getMember(req);

    String page = req.getParameter("page");

    ModelAndView mav = new ModelAndView("board/student/write");
    StudentBoardDAO dao = new StudentBoardDAO();

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));

      StudentBoardDTO dto = dao.findByNum(cmNum);

      if (dto == null) {
        return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + boardType + "&page=" + page);
      }

      if (dto.getMbNum() != info.getMb_Num()) { // 작성한 사용자가 아니라면
        return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + boardType + "&page=" + page);
      }

      mav.addObject("type", boardType);
      mav.addObject("dto", dto);
      mav.addObject("mode", "update");
      mav.addObject("page", page);

      // 카테고리 목록 추가 (student 타입일 경우)
      if ("student".equals(boardType)) {
        List<StudentBoardDTO> categories = dao.listCategories();
        mav.addObject("categories", categories);
      }

      return mav;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + boardType + "&page=" + page);
  }

  // 게시글 수정 제출
  @RequestMapping(value = "/bbs/studentBoard/update", method = POST)
  public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String type = req.getParameter("type"); // 'student' 또는 'oldbie'
    StudentBoardDAO dao = new StudentBoardDAO();
    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    try {
      StudentBoardDTO dto = new StudentBoardDTO();

      dto.setDivision(type); // 게시판 유형
      dto.setTitle(req.getParameter("title"));
      dto.setContent(req.getParameter("content"));

      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      dto.setCmNum(cmNum);

      // 기존 게시글 정보 가져오기
      StudentBoardDTO oldDto = dao.findByNum(cmNum);
      dto.setFileName(oldDto.getFileName());

      // student 타입일 경우 카테고리 설정
      if ("student".equals(type)) {
        String category = req.getParameter("category");
        if (category != null && !category.isEmpty()) {
          dto.setCategoryNum(Integer.parseInt(category));
        } else {
          dto.setCategoryNum(0); // 카테고리 없음
        }
      } else {
        dto.setCategoryNum(0); // oldbie 게시판은 카테고리 없음
      }

      handleFileUpload(req, session, dto); // 이미지 처리

      dao.updateBoard(dto);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + type);
  }

  // 게시글 삭제
  @RequestMapping(value = "/bbs/studentBoard/delete", method = GET)
  public ModelAndView deleteBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String boardType = req.getParameter("type"); // 'student' 또는 'oldbie'
    StudentBoardDAO dao = new StudentBoardDAO();

    HttpSession session = req.getSession();
    SessionInfo info = (SessionInfo) session.getAttribute("member");

    String page = req.getParameter("page");
    String query = "page=" + page;

    try {
      long cmNum = Long.parseLong(req.getParameter("cmNum"));
      String schType = req.getParameter("schType");
      String kwd = req.getParameter("kwd");
      String category = req.getParameter("category"); // 학번 카테고리

      if (schType == null) {
        schType = "all";
        kwd = "";
      }

      kwd = URLDecoder.decode(kwd, UTF_8);
      if ("student".equals(boardType)) {
        if (category != null) {
          category = URLDecoder.decode(category, UTF_8);
        }
      }

      if (!kwd.isEmpty()) {
        query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, UTF_8);
      }

      // student 타입일 경우 카테고리 추가
      if ("student".equals(boardType)) {
        if (category != null && !category.isEmpty()) {
          query += "&category=" + URLEncoder.encode(category, UTF_8);
        }
      }

      dao.deleteBoard(cmNum);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView("redirect:/bbs/studentBoard/list?type=" + boardType + "&" + query);
  }

  // 좋아요 추가/삭제
  @ResponseBody
  @RequestMapping(value = "/bbs/studentBoard/insertBoardLike")
  public Map<String, Object> insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
    Map<String, Object> model = new HashMap<>();

    StudentBoardDAO dao = new StudentBoardDAO();
    SessionInfo info = getMember(req); // member

    String state = "false";
    int boardLikeCount = 0;

    try {
      long cmNum = Long.parseLong(req.getParameter("cm_Num"));
      String userLiked = req.getParameter("userLiked");
      Long mbNum = info.getMb_Num();

      if ("true".equals(userLiked)) {
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

  // 파일 업로드 처리
  private static void handleFileUpload(HttpServletRequest req, HttpSession session, StudentBoardDTO dto) throws IOException, ServletException {
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
    }

    // 파일이 없으면 null로 설정
    if (filename != null && !filename.isEmpty()) {
      dto.setFileName(filename); // 새 파일 이름 설정
    } else {
      dto.setFileName(null); // 파일이 없으면 null로 설정
    }
  }

  // 세션에서 멤버 정보 가져오기
  private static SessionInfo getMember(HttpServletRequest req) {
    HttpSession session = req.getSession();
    return (SessionInfo) session.getAttribute("member");
  }

  private static void formatPostDate(List<StudentBoardDTO> list) {
    LocalDateTime now = LocalDateTime.now(); // 현재 시간


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    for (StudentBoardDTO dto : list) {

      try {
        LocalDateTime postDate = LocalDateTime.parse(dto.getCaDate(), dateTimeFormatter); // yyyy-MM-dd HH:mm:ss로 가지고옴

        Duration duration = Duration.between(postDate, now);

        if(duration.toHours() < 12) {
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
