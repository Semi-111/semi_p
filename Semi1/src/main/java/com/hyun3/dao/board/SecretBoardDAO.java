package com.hyun3.dao.board;

import com.hyun3.domain.board.ReplyDTO;
import com.hyun3.domain.board.SecretBoardDTO;
import com.hyun3.domain.member.MemberDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecretBoardDAO {
  private final Connection conn = DBConn.getConnection();

  // 게시글 추가
  public void insertBoard(SecretBoardDTO dto, long mbNum) throws SQLException {
    String sql = "INSERT INTO SECRETBOARD (CM_NUM, DIVISION, TITLE, CONTENT, CA_DATE, FILENAME, VIEWS, MB_NUM) " +
        " VALUES (SEQ_STUDENT_BOARD.nextval, ?, ?, ?, SYSDATE, ?, 0, ?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      conn.setAutoCommit(false);

      ps.setString(1, dto.getDivision());
      ps.setString(2, dto.getTitle());
      ps.setString(3, dto.getContent());
      ps.setString(4, dto.getFileName());
      ps.setLong(5, mbNum);

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
  public void updateBoard(SecretBoardDTO dto, long mbNum) throws SQLException {
    String sql = "UPDATE SECRETBOARD SET TITLE = ?, CONTENT = ?, FILENAME = ? WHERE CM_NUM = ? AND MB_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, dto.getTitle());
      ps.setString(2, dto.getContent());
      ps.setString(3, dto.getFileName());
      ps.setLong(4, dto.getCmNum());
      ps.setLong(5, mbNum);

      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 게시글 삭제
  public void deleteBoard(long cmNum, String userId, String role) throws SQLException {
    String deleteLikesSql = "DELETE FROM SECRET_LK WHERE CM_NUM = ?";
    String deleteBoardSql;

    if (Integer.parseInt(role) >= 60) { // 관리자 권한
      deleteBoardSql = "DELETE FROM SECRETBOARD WHERE CM_NUM = ?";
    } else {
      deleteBoardSql = "DELETE FROM SECRETBOARD S " +
          " WHERE S.CM_NUM = ? " +
          " AND EXISTS ( " +
          "   SELECT 1 FROM MEMBER m " +
          "   WHERE m.MB_NUM = S.MB_NUM AND m.USERID = ? AND m.ROLE = ?)";
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

  public List<SecretBoardDTO> listBoard(String division, int offset, int size) {
    List<SecretBoardDTO> list = new ArrayList<>();
    String sql = "SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, " +
        "i.CA_DATE, i.FILENAME, " +
        "m.USERID, m.NICKNAME, " +
        "COUNT(l.CM_NUM) AS likeCount " +
        "FROM SECRETBOARD i " +
        "JOIN member m ON i.MB_NUM = m.MB_NUM " +
        "LEFT JOIN SECRET_LK l ON i.CM_NUM = l.CM_NUM " +
        "WHERE i.DIVISION = ? " +
        "GROUP BY i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, i.CA_DATE, i.FILENAME, m.USERID, m.NICKNAME " +
        "ORDER BY i.CM_NUM DESC " +
        "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, division);
      ps.setInt(2, offset);
      ps.setInt(3, size);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          SecretBoardDTO dto = new SecretBoardDTO();
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

  // 게시글 리스트 가져오기
  public List<SecretBoardDTO> listBoard(String division, int offset, int size, String schType, String kwd) {
    List<SecretBoardDTO> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder();

    sql.append(" SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, ")
        .append(" s.CA_DATE, s.FILENAME, ")
        .append(" m.USERID, m.NICKNAME, ")
        .append(" COUNT(l.CM_NUM) AS likeCount ")
        .append(" FROM SECRETBOARD s ")
        .append(" JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append(" LEFT JOIN SECRET_LK l ON s.CM_NUM = l.CM_NUM ")
        .append(" WHERE s.DIVISION = ? ");

    // 검색 조건 추가
    if (kwd != null && !kwd.isEmpty()) {
      if ("all".equals(schType)) {
        sql.append(" AND (INSTR(s.TITLE, ?) > 0 OR INSTR(s.CONTENT, ?) > 0) ");
      } else if ("title".equals(schType)) {
        sql.append(" AND INSTR(s.TITLE, ?) > 0 ");
      } else if ("content".equals(schType)) {
        sql.append(" AND INSTR(s.CONTENT, ?) > 0 ");
      } else if ("name".equals(schType)) {
        sql.append(" AND INSTR(m.NICKNAME, ?) > 0 ");
      }
    }

    sql.append(" GROUP BY s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, ")
        .append(" m.USERID, m.NICKNAME ")
        .append(" ORDER BY s.CM_NUM DESC ")
        .append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      int paramIndex = 1;
      ps.setString(paramIndex++, division);


      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
          ps.setString(paramIndex++, kwd);
        } else {
          ps.setString(paramIndex++, kwd);
        }
      }

      ps.setInt(paramIndex++, offset);
      ps.setInt(paramIndex++, size);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          SecretBoardDTO dto = new SecretBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
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

  // 특정 게시글 가져오기
  public SecretBoardDTO findByNum(long cmNum) {
    String sql = "SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, s.MB_NUM," +
        " m.USERID, m.NICKNAME" +
        " FROM SECRETBOARD s " +
        " JOIN MEMBER m ON s.MB_NUM = m.MB_NUM " +
        " WHERE s.CM_NUM = ?";

    SecretBoardDTO dto = null;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          dto = new SecretBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setMbNum(rs.getLong("MB_NUM"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dto;
  }

  // 데이터 개수e
  public int dataCount(String division) {
    String sql = "SELECT COUNT(*) AS cnt FROM SECRETBOARD WHERE DIVISION = ?";

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
    sql.append("SELECT COUNT(*) AS cnt FROM SECRETBOARD S ");

    // 작성자 검색 시 MEMBER 테이블과 JOIN 추가
    if ("name".equals(schType)) {
      sql.append("JOIN MEMBER m ON S.MB_NUM = m.MB_NUM ");
    }

    sql.append("WHERE S.DIVISION = ? ");

    if ("all".equals(schType)) {
      sql.append("AND (INSTR(S.TITLE, ?) >= 1 OR INSTR(S.CONTENT, ?) >= 1)");
    } else if ("name".equals(schType)) {
      sql.append("AND INSTR(m.NICKNAME, ?) >= 1 "); // 작성자 검색
    } else {
      sql.append("AND INSTR(S.").append(schType).append(", ?) >= 1");
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


  // 이전글 찾기
  public SecretBoardDTO findByPrev(String division, long cmNum, String schType, String kwd) {
    SecretBoardDTO dto = null;
    StringBuilder sb = new StringBuilder();

    sb.append("SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, ")
        .append("m.USERID, m.NICKNAME ")
        .append("FROM SECRETBOARD s ")
        .append("JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append("WHERE s.CM_NUM > ? AND s.DIVISION = ? ");

    if (kwd != null && !kwd.isEmpty()) {
      if ("all".equals(schType)) {
        sb.append(" AND (INSTR(s.TITLE, ?) > 0 OR INSTR(s.CONTENT, ?) > 0) ");
      } else if ("title".equals(schType)) {
        sb.append(" AND INSTR(s.TITLE, ?) > 0 ");
      } else if ("content".equals(schType)) {
        sb.append(" AND INSTR(s.CONTENT, ?) > 0 ");
      } else if ("name".equals(schType)) {
        sb.append(" AND INSTR(m.NICKNAME, ?) > 0 ");
      }
    }

    sb.append("ORDER BY s.CM_NUM ASC FETCH FIRST 1 ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
      int paramIndex = 1;
      ps.setLong(paramIndex++, cmNum);
      ps.setString(paramIndex++, division);


      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
          ps.setString(paramIndex++, kwd);
        } else {
          ps.setString(paramIndex++, kwd);
        }
      }

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          dto = new SecretBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dto;
  }

  // 다음글 찾기
  public SecretBoardDTO findByNext(String division, long cmNum,  String schType, String kwd) {
    SecretBoardDTO dto = null;
    StringBuilder sb = new StringBuilder();

    sb.append("SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, ")
        .append(" m.USERID, m.NICKNAME")
        .append(" FROM SECRETBOARD s ")
        .append(" JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append(" WHERE s.CM_NUM < ? AND s.DIVISION = ? ");

    if (kwd != null && !kwd.isEmpty()) {
      if ("all".equals(schType)) {
        sb.append(" AND (INSTR(s.TITLE, ?) > 0 OR INSTR(s.CONTENT, ?) > 0) ");
      } else if ("title".equals(schType)) {
        sb.append(" AND INSTR(s.TITLE, ?) > 0 ");
      } else if ("content".equals(schType)) {
        sb.append(" AND INSTR(s.CONTENT, ?) > 0 ");
      } else if ("name".equals(schType)) {
        sb.append(" AND INSTR(m.NICKNAME, ?) > 0 ");
      }
    }

    sb.append(" ORDER BY s.CM_NUM DESC FETCH FIRST 1 ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
      int paramIndex = 1;
      ps.setLong(paramIndex++, cmNum);
      ps.setString(paramIndex++, division);

      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
          ps.setString(paramIndex++, kwd);
        } else {
          ps.setString(paramIndex++, kwd);
        }
      }

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          dto = new SecretBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));

          MemberDTO member = new MemberDTO();
          member.setUserId(rs.getString("USERID"));
          member.setNickName(rs.getString("NICKNAME"));
          dto.setMember(member);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dto;
  }

  // 조회수 증가
  public void updateViews(long cmNum) throws SQLException {
    String sql = "UPDATE SECRETBOARD SET VIEWS = VIEWS + 1 WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.executeUpdate();
    }
  }


  // 좋아요 여부 확인
  public boolean isUserLiked(long cmNum, long mbNum) throws SQLException {
    boolean result = false;
    String sql = "SELECT COUNT(*) FROM SECRET_LK WHERE CM_NUM = ? AND MB_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.setLong(2, mbNum);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          result = rs.getInt(1) > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  // 좋아요 추가
  public void insertBoardLike(long cmNum, long mbNum) throws SQLException {
    String sql = "INSERT INTO SECRET_LK (CM_NUM, MB_NUM, DATETIME) VALUES (?, ?, SYSDATE)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.setLong(2, mbNum);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // 좋아요 삭제
  public void deleteBoardLike(long cmNum, long mbNum) throws SQLException {
    String sql = "DELETE FROM SECRET_LK WHERE CM_NUM = ? AND MB_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.setLong(2, mbNum);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // 좋아요 개수 카운트
  public int countBoardLike(long cmNum) {
    int count = 0;
    String sql = "SELECT COUNT(*) AS cnt FROM SECRET_LK WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          count = rs.getInt("cnt");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return count;
  }

  // 댓글
  // 게시글의 댓글 및 답글 저장
  public void insertReply(ReplyDTO dto) throws SQLException {
    PreparedStatement ps = null;
    String sql;

    try {
      sql = "INSERT INTO SECRET_CO(CO_NUM, CONTENT, REG_DATE, MB_NUM, CM_NUM) VALUES " +
          " (SEQ_SECRET_CO.nextval, ?, SYSDATE, ?, ?)";

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
      sql = "SELECT COUNT(*) AS cnt FROM SECRET_CO WHERE CM_NUM = ? ";

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

  // 댓글의 답글 개수
  public int dataCountReplyAnswer(long cmNum) {
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) cnt FROM SECRET_CO WHERE CM_NUM = ?";

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
      sb.append(" SELECT CO_NUM, S.MB_NUM, CONTENT, REG_DATE, S.CM_NUM, M.nickName ");
      sb.append(" FROM SECRET_CO S ");
      sb.append(" JOIN MEMBER M ON S.MB_NUM = M.MB_NUM");
      sb.append(" WHERE S.CM_NUM = ? ");
      sb.append(" ORDER BY S.CO_NUM DESC ");
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

  // 댓글의 답글 리스트
  public List<ReplyDTO> listReplyAnswer(long cmNum) { // 부모
    List<ReplyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append("SELECT CO_NUM, S.MB_NUM, CONTENT, REG_DATE, S.CM_NUM, M.nickName")
          .append(" FROM SECRET_CO S")
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


  public ReplyDTO findByReplyId(long coNum) {
    ReplyDTO dto = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT CO_NUM, S.MB_NUM, CONTENT, REG_DATE, S.CM_NUM, M.nickName "
          + " FROM SECRET_CO S  "
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
      sql = "DELETE FROM SECRET_CO "
          + " WHERE CO_NUM IN  "
          + " (SELECT CO_NUM FROM SECRET_CO START WITH CO_NUM = ? "
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
