package com.hyun3.dao.board;

import com.hyun3.domain.board.InfoBoardDTO;
import com.hyun3.domain.board.ReplyDTO;
import com.hyun3.domain.member.MemberDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfoBoardDAO {

  private final Connection conn = DBConn.getConnection();

  // 게시글 추가
  public void insertBoard(InfoBoardDTO dto, String userId) throws SQLException {
    String sql = "INSERT INTO INFOBOARD (cm_num, DIVISION, title, content, ca_date, fileName, views, MB_NUM) " +
        " VALUES (SEQ_INFO_BOARD.nextval, ?, ?, ?, SYSDATE, ?, 0, " +
        "        (SELECT MB_NUM FROM member WHERE userId = ?))";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      conn.setAutoCommit(false);

      ps.setString(1, dto.getDivision());
      ps.setString(2, dto.getTitle());
      ps.setString(3, dto.getContent());
      ps.setString(4, dto.getFileName());
      ps.setString(5, userId);

      ps.executeUpdate();
      conn.commit();
    } catch (Exception e) {
      conn.rollback();
      throw e;
    } finally {
      conn.setAutoCommit(true);
    }
  }

  // 게시글 수정
  public void updateBoard(InfoBoardDTO dto, long mbNum) throws SQLException {
    String sql = "UPDATE INFOBOARD SET TITLE = ?, CONTENT = ?, FILENAME = ? WHERE CM_NUM = ? AND MB_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, dto.getTitle());
      ps.setString(2, dto.getContent());
      ps.setString(3, dto.getFileName());
      ps.setLong(4, dto.getCmNum());
      ps.setLong(5, mbNum);

      ps.executeUpdate();
    }
  }

  // 게시글 삭제
  public void deleteBoard(long cmNum, String userId, String role) throws SQLException {
    String deleteLikesSql = "DELETE FROM INFO_LK WHERE CM_NUM = ?";
    String deleteBoardSql;

    if (Integer.parseInt(role) >= 60) { // 관리자 권한
      deleteBoardSql = "DELETE FROM INFOBOARD WHERE CM_NUM = ?";
    } else {
      deleteBoardSql = "DELETE FROM INFOBOARD i " +
          " WHERE i.CM_NUM = ? " +
          " AND EXISTS ( " +
          "   SELECT 1 FROM MEMBER m " +
          "   WHERE m.MB_NUM = i.MB_NUM AND m.USERID = ? AND m.ROLE = ?)";
    }

    try {
      conn.setAutoCommit(false);
      try (PreparedStatement psLikes = conn.prepareStatement(deleteLikesSql)) {
        psLikes.setLong(1, cmNum);
        psLikes.executeUpdate();
      }

      try (PreparedStatement ps = conn.prepareStatement(deleteBoardSql)) {
        ps.setLong(1, cmNum);
        if (Integer.parseInt(role) < 60) {
          ps.setString(2, userId);
          ps.setString(3, role);
        }
        ps.executeUpdate();
      }
      conn.commit();
    } catch (SQLException e) {
      conn.rollback();
      throw e;
    } finally {
      conn.setAutoCommit(true);
    }
  }

  // 지금 localDate를 가지고 올거면 TO_CHAR로 가지고오면 안되고 자바에서 SUBSTRING으로 잘라야함
  // 게시글 리스트 가져오기
  public List<InfoBoardDTO> listBoard(String division, int offset, int size) {
    List<InfoBoardDTO> list = new ArrayList<>();
    String sql = "SELECT " +
        "i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, " +
        "i.CA_DATE, i.FILENAME, " +
        "m.USERID, m.NICKNAME, " +
        "NVL(l.likeCount, 0) AS likeCount, " +
        "NVL(r.replyCount, 0) AS replyCount " +
        "FROM INFOBOARD i " +
        "JOIN member m ON i.MB_NUM = m.MB_NUM " +
        "LEFT JOIN ( " +
        "    SELECT CM_NUM, COUNT(*) AS likeCount " +
        "    FROM INFO_LK " +
        "    GROUP BY CM_NUM " +
        ") l ON i.CM_NUM = l.CM_NUM " +
        "LEFT JOIN ( " +
        "    SELECT CM_NUM, COUNT(*) AS replyCount " +
        "    FROM INFO_CO " +
        "    GROUP BY CM_NUM " +
        ") r ON i.CM_NUM = r.CM_NUM " +
        "WHERE i.DIVISION = ? " +
        "ORDER BY i.CM_NUM DESC " +
        "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, division);
      ps.setInt(2, offset);
      ps.setInt(3, size);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          InfoBoardDTO dto = new InfoBoardDTO();
          dto.setCmNum(rs.getInt("CM_NUM"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setBoardLikeCount(rs.getInt("likeCount"));
          dto.setReplyCount(rs.getInt("replyCount"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);

          list.add(dto);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }

  public List<InfoBoardDTO> listReplyCount(String division) {
    List<InfoBoardDTO> list = new ArrayList<>();

    String sql = "SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, i.CA_DATE, i.FILENAME, " +
        " m.USERID, m.NICKNAME, " +
        " NVL(r.replyCount, 0) AS replyCount " +
        " FROM INFOBOARD i " +
        " JOIN member m ON i.MB_NUM = m.MB_NUM " +
        " LEFT JOIN ( " +
        "    SELECT CM_NUM, COUNT(*) AS replyCount " +
        "    FROM INFO_CO " +
        "    GROUP BY CM_NUM " +
        " ) r ON i.CM_NUM = r.CM_NUM " +
        " WHERE i.DIVISION = ? " +
        " ORDER BY i.CM_NUM DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, division);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          InfoBoardDTO dto = new InfoBoardDTO();
          dto.setCmNum(rs.getInt("CM_NUM"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setReplyCount(rs.getInt("replyCount"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);

          list.add(dto);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }


  // 게시글 검색 포함 리스트
  public List<InfoBoardDTO> listBoard(String division, int offset, int size, String schType, String kwd) {
    List<InfoBoardDTO> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder();

    sql.append("SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, ")
        .append("i.CA_DATE, i.FILENAME, ")
        .append("m.USERID, m.NICKNAME, ")
        .append("COUNT(l.CM_NUM) AS likeCount ")
        .append("FROM INFOBOARD i ")
        .append("JOIN member m ON i.MB_NUM = m.MB_NUM ")
        .append("LEFT JOIN INFO_LK l ON i.CM_NUM = l.CM_NUM ")
        .append("WHERE i.DIVISION = ? ");

    // 검색 조건 추가
    if ("all".equals(schType)) {
      sql.append(" AND (INSTR(i.TITLE, ?) > 0 OR INSTR(i.CONTENT, ?) > 0) ");
    } else if ("title".equals(schType)) {
      sql.append(" AND INSTR(i.TITLE, ?) > 0 ");
    } else if ("content".equals(schType)) {
      sql.append(" AND INSTR(i.CONTENT, ?) > 0 ");
    } else if ("name".equals(schType)) {
      sql.append(" AND INSTR(m.NICKNAME, ?) > 0 ");
    }

    sql.append("GROUP BY i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, i.CA_DATE, i.FILENAME, m.USERID, m.NICKNAME ")
        .append("ORDER BY i.CM_NUM DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      int paramIndex = 1;
      ps.setString(paramIndex++, division);

      if ("all".equals(schType)) {
        ps.setString(paramIndex++, kwd);
        ps.setString(paramIndex++, kwd);
      } else {
        ps.setString(paramIndex++, kwd);
      }

      ps.setInt(paramIndex++, offset);
      ps.setInt(paramIndex, size);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          InfoBoardDTO dto = new InfoBoardDTO();
          dto.setCmNum(rs.getInt("CM_NUM"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setBoardLikeCount(rs.getInt("likeCount"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);

          list.add(dto);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }


  // 조회수 증가
  public void updateViews(long cmNum) throws SQLException {
    String sql = "UPDATE INFOBOARD SET VIEWS = VIEWS + 1 WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.executeUpdate();
    }
  }

  // 데이터 개수e
  public int dataCount(String division) {
    String sql = "SELECT COUNT(*) AS cnt FROM INFOBOARD WHERE DIVISION = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, division);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("cnt");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0;
  }

  // 검색 포함 데이터 개수
  public int dataCount(String division, String schType, String kwd) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(*) AS cnt FROM INFOBOARD i ");

    // 작성자 검색 시 MEMBER 테이블과 JOIN 추가
    if ("name".equals(schType)) {
      sql.append("JOIN MEMBER m ON i.MB_NUM = m.MB_NUM ");
    }

    sql.append("WHERE i.DIVISION = ? ");

    if ("all".equals(schType)) {
      sql.append("AND (INSTR(i.TITLE, ?) >= 1 OR INSTR(i.CONTENT, ?) >= 1)");
    } else if ("name".equals(schType)) {
      sql.append("AND INSTR(m.NICKNAME, ?) >= 1 "); // 작성자 검색
    } else {
      sql.append("AND INSTR(i.").append(schType).append(", ?) >= 1");
    }

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      ps.setString(1, division);

      int paramIndex = 2;
      if ("all".equals(schType)) {
        ps.setString(paramIndex++, kwd);
        ps.setString(paramIndex, kwd);
      } else {
        ps.setString(paramIndex, kwd);
      }

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("cnt");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0;
  }

  // 특정 게시글 가져오기
  public InfoBoardDTO findByNum(long cmNum) {
    String sql = "SELECT i.CM_NUM, i.DIVISION, i.TITLE, i.CONTENT, i.CA_DATE, i.FILENAME, i.VIEWS,  I.MB_NUM, " +
        " m.USERID, m.NICKNAME " +
        " FROM INFOBOARD i " +
        " JOIN MEMBER m ON m.MB_NUM = i.MB_NUM " +
        " WHERE i.CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          InfoBoardDTO dto = new InfoBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setMbNum(rs.getLong("MB_NUM"));

          int boardLikeCount = countBoardLike(cmNum);
          dto.setBoardLikeCount(boardLikeCount);

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);

          return dto;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  // 이전글 찾기
  public InfoBoardDTO findByPrev(String division, long cmNum, String schType, String kwd) {
    InfoBoardDTO dto = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append(" SELECT I.CM_NUM, I.TITLE, I.CONTENT, I.CA_DATE, I.FILENAME, I.VIEWS, I.MB_NUM, M.USERID, M.NICKNAME, M.ROLE ")
          .append(" FROM INFOBOARD I ")
          .append(" JOIN MEMBER M ON M.MB_NUM = I.MB_NUM ")
          .append(" WHERE I.CM_NUM > ? AND I.DIVISION = ? AND M.ROLE = '40' ");

      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          sb.append(" AND (INSTR(I.TITLE, ?) >= 1 OR INSTR(I.CONTENT, ?) >= 1) ");
        } else if ("CA_DATE".equals(schType)) {
          kwd = kwd.replaceAll("[-./]", ""); // yyyyMMdd 형식으로 변환
          sb.append(" AND TO_CHAR(I.CA_DATE, 'YYYYMMDD') = ? ");
        } else {
          sb.append(" AND INSTR(").append(schType).append(", ?) >= 1 ");
        }
      }

      sb.append("ORDER BY I.CM_NUM ASC FETCH FIRST 1 ROWS ONLY");

      ps = conn.prepareStatement(sb.toString());

      ps.setLong(1, cmNum);
      ps.setString(2, division);

      if (kwd != null && !kwd.isEmpty()) {
        ps.setString(3, kwd);
        ps.setString(4, kwd);
        if ("all".equals(schType)) {
          ps.setString(3, kwd);
        }
      }

      rs = ps.executeQuery();

      if (rs.next()) {
        dto = new InfoBoardDTO();
        dto.setCmNum(rs.getLong("CM_NUM"));
        dto.setTitle(rs.getString("TITLE"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setViews(rs.getInt("VIEWS"));
        dto.setCaDate(rs.getString("CA_DATE"));
        dto.setFileName(rs.getString("FILENAME"));
        dto.setMbNum(rs.getLong("MB_NUM"));

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setNickName(rs.getString("NICKNAME"));
        member.setRole(rs.getString("ROLE"));
        dto.setMember(member);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return dto;
  }

  // 다음글 찾기
  public InfoBoardDTO findByNext(String division, long cmNum, String schType, String kwd) {
    InfoBoardDTO dto = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append("SELECT I.CM_NUM, I.TITLE, I.CONTENT, I.CA_DATE, I.FILENAME, I.VIEWS, I.MB_NUM, M.USERID, M.NICKNAME, M.ROLE ")
          .append("FROM INFOBOARD I ")
          .append("JOIN MEMBER M ON M.MB_NUM = I.MB_NUM ")
          .append(" WHERE I.CM_NUM < ? AND I.DIVISION = ? AND M.ROLE = '40' ");

      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          sb.append("AND (INSTR(I.TITLE, ?) >= 1 OR INSTR(I.CONTENT, ?) >= 1) ");
        } else if ("CA_DATE".equals(schType)) {
          kwd = kwd.replaceAll("[-./]", ""); // yyyyMMdd 형식으로 변환
          sb.append("AND TO_CHAR(I.CA_DATE, 'YYYYMMDD') = ? ");
        } else {
          sb.append("AND INSTR(").append(schType).append(", ?) >= 1 ");
        }
      }

      sb.append("ORDER BY I.CM_NUM DESC FETCH FIRST 1 ROWS ONLY");

      ps = conn.prepareStatement(sb.toString());

      ps.setLong(1, cmNum);
      ps.setString(2, division);

      if (kwd != null && !kwd.isEmpty()) {
        ps.setString(3, kwd);
        ps.setString(4, kwd);
        if ("all".equals(schType)) {
          ps.setString(3, kwd);
        }
      }

      rs = ps.executeQuery();

      if (rs.next()) {
        dto = new InfoBoardDTO();
        dto.setCmNum(rs.getLong("CM_NUM"));
        dto.setTitle(rs.getString("TITLE"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setViews(rs.getInt("VIEWS"));
        dto.setCaDate(rs.getString("CA_DATE"));
        dto.setFileName(rs.getString("FILENAME"));
        dto.setMbNum(rs.getLong("MB_NUM"));

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setNickName(rs.getString("NICKNAME"));
        member.setRole(rs.getString("ROLE"));
        dto.setMember(member);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return dto;
  }

  // 좋아요 여부 확인
  public boolean isUserLiked(long cmNum, long mbNum) throws SQLException {
    boolean result = false;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) FROM INFO_LK WHERE CM_NUM = ? AND MB_NUM = ?";
      ps = conn.prepareStatement(sql);
      ps.setLong(1, cmNum);
      ps.setLong(2, mbNum);
      rs = ps.executeQuery();

      if (rs.next()) {
        result = rs.getInt(1) > 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }
    return result;
  }


  // 게시글의 좋아요 추가
  public void insertBoardLike(long num, long mb_Num) throws SQLException {
    PreparedStatement ps = null;
    String sql;

    try {
      sql = "INSERT INTO INFO_LK (CM_NUM, MB_NUM, DATETIME) VALUES (?, ?, SYSDATE)";
      // cm_num : 게시글 mb_num : 회원번호

      ps = conn.prepareStatement(sql);

      ps.setLong(1, num);
      ps.setLong(2, mb_Num);

      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(ps);
    }
  }

  // 게시글의 좋아요 삭제
  public void deleteBoardLike(long num, long mb_Num) throws SQLException {
    PreparedStatement ps = null;
    String sql;

    try {
      sql = "DELETE FROM INFO_LK WHERE CM_NUM = ? AND MB_NUM = ?";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, num);
      ps.setLong(2, mb_Num);

      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(ps);
    }
  }

  // 게시글 좋아요 데이터 개수
  public int countBoardLike(long num) {
    int result = 0;

    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) cnt FROM INFO_LK WHERE CM_NUM = ?";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, num);

      rs = ps.executeQuery();

      if(rs.next()) {
        result = rs.getInt("cnt");
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }
    return result;
  }

  // 게시글 댓글

  // 게시글의 댓글 및 답글 저장
  public void insertReply(ReplyDTO dto) throws SQLException {
    PreparedStatement ps = null;
    String sql;

    try {
      sql = "INSERT INTO INFO_CO(CO_NUM, CONTENT, REG_DATE, MB_NUM, CM_NUM) VALUES " +
          " (SEQ_INFO_CO.nextval, ?, SYSDATE, ?, ?)";

      ps = conn.prepareStatement(sql);

      ps.setString(1, dto.getContent());
      ps.setLong(2, dto.getMbNum());
      ps.setLong(3, dto.getCmNum());

      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(ps);
    }
  }

  // 게시글의 댓글 개수
  public int dataCountReply(long cmNum) {
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) AS cnt FROM INFO_CO WHERE CM_NUM = ? ";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, cmNum);

      rs = ps.executeQuery();
      if(rs.next()) {
        result = rs.getInt("cnt");
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }


    return result;
  }

  // 게시글의 댓글
  public List<ReplyDTO> listReply(long cmNum, int offset, int size) {
    List<ReplyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append(" SELECT CO_NUM, I.MB_NUM, CONTENT, REG_DATE, I.CM_NUM, M.nickName ");
      sb.append(" FROM INFO_CO I ");
      sb.append(" JOIN MEMBER M ON I.MB_NUM = M.MB_NUM");
      sb.append(" WHERE I.CM_NUM = ? ");
      sb.append(" ORDER BY I.CO_NUM DESC ");
      sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

      ps = conn.prepareStatement(sb.toString());

      ps.setLong(1, cmNum);
      ps.setInt(2, offset);
      ps.setInt(3, size);

      rs = ps.executeQuery();

      while (rs.next()) {
        ReplyDTO dto = new ReplyDTO();
        dto.setCoNum(rs.getLong("CO_NUM"));
        dto.setMbNum(rs.getLong("MB_NUM"));
        dto.setNickName(rs.getString("nickName"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setReg_date(rs.getString("REG_DATE"));
        dto.setCmNum(rs.getLong("CM_NUM"));

        list.add(dto);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return list;
  }

  // -------------------------------------------
  // 댓글의 답글 리스트
  public List<ReplyDTO> listReplyAnswer(long cmNum) { // 부모
    List<ReplyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append("SELECT CO_NUM, I.MB_NUM, CONTENT, REG_DATE, I.CM_NUM, M.nickName")
          .append(" FROM INFO_CO I")
          .append(" JOIN MEMBER M ON I.MB_NUM = M.MB_NUM ")
          .append(" WHERE CM_NUM = ? ")
          .append(" ORDER BY CO_NUM DESC ");

      ps = conn.prepareStatement(sb.toString());

      ps.setLong(1, cmNum);
      rs = ps.executeQuery();

      while (rs.next()) {
        ReplyDTO dto = new ReplyDTO();

        dto.setCoNum(rs.getLong("CO_NUM"));
        dto.setMbNum(rs.getLong("MB_NUM"));
        dto.setNickName(rs.getString("nickName"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setReg_date(rs.getString("REG_DATE"));
        dto.setCmNum(rs.getLong("CM_NUM"));
        dto.setAnswerCount(rs.getInt("answerCount"));

        list.add(dto);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return list;
  }

  // 댓글의 답글 개수
  public int dataCountReplyAnswer(long cmNum) {
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) cnt FROM INFO_CO WHERE CM_NUM = ?";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, cmNum);

      rs = ps.executeQuery();

      if(rs.next()) {
        result = rs.getInt("cnt");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return result;
  }

  public ReplyDTO findByReplyId(long coNum) {
    ReplyDTO dto = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT CO_NUM, I.MB_NUM, CONTENT, REG_DATE, I.CM_NUM, M.nickName "
          + " FROM INFO_CO I  "
          + " JOIN MEMBER M ON I.MB_NUM = M.MB_NUM  "
          + " WHERE CO_NUM = ? ";
      pstmt = conn.prepareStatement(sql);

      pstmt.setLong(1, coNum);

      rs=pstmt.executeQuery();

      if(rs.next()) {
        dto = new ReplyDTO();

        dto.setCoNum(rs.getLong("CO_NUM"));
        dto.setMbNum(rs.getLong("MB_NUM"));
        dto.setContent(rs.getString("content"));
        dto.setReg_date(rs.getString("reg_date"));
        dto.setNickName(rs.getString("nickName"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(pstmt);
    }

    return dto;
  }

  // 게시물의 댓글 삭제
  public void deleteReply(long coNum, long mbNum, String role) throws SQLException {
    PreparedStatement pstmt = null;
    String sql;

    if (role == null || role.isEmpty()) {
      throw new IllegalArgumentException("Role 값이 올바르지 않습니다.");
    }

    int userLevel = Integer.parseInt(role);
    if(userLevel < 60) {
      ReplyDTO dto = findByReplyId(coNum);
      if(dto == null || (mbNum != dto.getMbNum())) {
        return;
      }
    }

    try {
      sql = "DELETE FROM INFO_CO "
          + " WHERE CO_NUM IN  "
          + " (SELECT CO_NUM FROM INFO_CO START WITH CO_NUM = ? "
          + "     CONNECT BY PRIOR CO_NUM = CM_NUM) ";
      pstmt = conn.prepareStatement(sql);

      pstmt.setLong(1, coNum);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(pstmt);
    }
  }


}
