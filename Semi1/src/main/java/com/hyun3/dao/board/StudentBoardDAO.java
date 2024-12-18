package com.hyun3.dao.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.board.StudentBoardDTO;
import com.hyun3.domain.member.MemberDTO;
import com.hyun3.util.DBConn;

public class StudentBoardDAO {
  private final Connection conn = DBConn.getConnection();

  // 카테고리 목록 가져오기
  public List<StudentBoardDTO> listCategories() {
    List<StudentBoardDTO> list = new ArrayList<>();
    String sql = "SELECT CT_NUM, CT_NAME FROM CATEGORY ORDER BY CT_NUM DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        StudentBoardDTO dto = new StudentBoardDTO();
        dto.setCategoryNum(rs.getInt("CT_NUM"));
        dto.setCategoryName(rs.getString("CT_NAME"));
        list.add(dto);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }

  // 게시글 추가
  public void insertBoard(StudentBoardDTO dto, String userId) throws SQLException {
    String sql = "INSERT INTO STUDENTBOARD (CM_NUM, DIVISION, TITLE, CONTENT, CA_DATE, FILENAME, VIEWS, MB_NUM, CT_NUM) " +
        "VALUES (SEQ_STUDENT_BOARD.nextval, ?, ?, ?, SYSDATE, ?, 0, (SELECT MB_NUM FROM MEMBER WHERE USERID = ?), ?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      conn.setAutoCommit(false);

      ps.setString(1, dto.getDivision());
      ps.setString(2, dto.getTitle());
      ps.setString(3, dto.getContent());
      ps.setString(4, dto.getFileName());
      ps.setString(5, userId);
      ps.setInt(6, dto.getCategoryNum());

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
  public void updateBoard(StudentBoardDTO dto) throws SQLException {
    String sql = "UPDATE STUDENTBOARD SET TITLE = ?, CONTENT = ?, FILENAME = ?, CT_NUM = ? WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, dto.getTitle());
      ps.setString(2, dto.getContent());
      ps.setString(3, dto.getFileName());
      ps.setInt(4, dto.getCategoryNum());
      ps.setLong(5, dto.getCmNum());

      ps.executeUpdate();
    }
  }

  // 게시글 삭제
  public void deleteBoard(long cmNum) throws SQLException {
    String sql = "DELETE FROM STUDENTBOARD WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.executeUpdate();
    }
  }

  // 게시글 리스트 가져오기
  public List<StudentBoardDTO> listBoard(String division, int offset, int size, String category, String schType, String kwd) {
    List<StudentBoardDTO> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder();

    sql.append(" SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, ")
        .append(" s.CA_DATE, s.FILENAME, ")
        .append(" m.USERID, m.NICKNAME, ")
        .append(" c.CT_NUM, c.CT_NAME, ")
        .append(" COUNT(l.CM_NUM) AS likeCount ")
        .append(" FROM STUDENTBOARD s ")
        .append(" JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append(" LEFT JOIN STUDENT_LK l ON s.CM_NUM = l.CM_NUM ")
        .append(" LEFT JOIN CATEGORY c ON s.CT_NUM = c.CT_NUM ")
        .append(" WHERE s.DIVISION = ? ");

    // 학번 카테고리 추가 (student 타입일 경우)
    if ("student".equals(division) && category != null && !category.isEmpty()) {
      sql.append(" AND s.CT_NUM = ? ");
    }

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
        .append(" m.USERID, m.NICKNAME, c.CT_NUM, c.CT_NAME ")
        .append(" ORDER BY s.CM_NUM DESC ")
        .append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      int paramIndex = 1;
      ps.setString(paramIndex++, division);

      if ("student".equals(division) && category != null && !category.isEmpty()) {
        ps.setString(paramIndex++, category);
      }

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
          StudentBoardDTO dto = new StudentBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setBoardLikeCount(rs.getInt("likeCount"));

          dto.setCategoryNum(rs.getInt("CT_NUM"));
          dto.setCategoryName(rs.getString("CT_NAME"));

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
  public StudentBoardDTO findByNum(long cmNum) {
    String sql = "SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, s.MB_NUM," +
        "m.USERID, m.NICKNAME, c.CT_NUM, c.CT_NAME " +
        "FROM STUDENTBOARD s " +
        "JOIN MEMBER m ON s.MB_NUM = m.MB_NUM " +
        "LEFT JOIN CATEGORY c ON s.CT_NUM = c.CT_NUM " +
        "WHERE s.CM_NUM = ?";

    StudentBoardDTO dto = null;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          dto = new StudentBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));
          dto.setMbNum(rs.getLong("MB_NUM"));

          dto.setCategoryNum(rs.getInt("CT_NUM"));
          dto.setCategoryName(rs.getString("CT_NAME"));

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

  // 이전글 찾기
  public StudentBoardDTO findByPrev(String division, long cmNum, String category, String schType, String kwd) {
    StudentBoardDTO dto = null;
    StringBuilder sb = new StringBuilder();

    sb.append("SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, ")
        .append("m.USERID, m.NICKNAME, c.CT_NUM, c.CT_NAME ")
        .append("FROM STUDENTBOARD s ")
        .append("JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append("LEFT JOIN CATEGORY c ON s.CT_NUM = c.CT_NUM ")
        .append("WHERE s.CM_NUM > ? AND s.DIVISION = ? ");

    if ("student".equals(division) && category != null && !category.isEmpty()) {
      sb.append(" AND s.CT_NUM = ? ");
    }

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

      if ("student".equals(division) && category != null && !category.isEmpty()) {
        ps.setString(paramIndex++, category);
      }

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
          dto = new StudentBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));

          dto.setCategoryNum(rs.getInt("CT_NUM"));
          dto.setCategoryName(rs.getString("CT_NAME"));

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
  public StudentBoardDTO findByNext(String division, long cmNum, String category, String schType, String kwd) {
    StudentBoardDTO dto = null;
    StringBuilder sb = new StringBuilder();

    sb.append("SELECT s.CM_NUM, s.DIVISION, s.TITLE, s.CONTENT, s.VIEWS, s.CA_DATE, s.FILENAME, ")
        .append("m.USERID, m.NICKNAME, c.CT_NUM, c.CT_NAME ")
        .append("FROM STUDENTBOARD s ")
        .append("JOIN MEMBER m ON s.MB_NUM = m.MB_NUM ")
        .append("LEFT JOIN CATEGORY c ON s.CT_NUM = c.CT_NUM ")
        .append("WHERE s.CM_NUM < ? AND s.DIVISION = ? ");

    if ("student".equals(division) && category != null && !category.isEmpty()) {
      sb.append(" AND s.CT_NUM = ? ");
    }

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

    sb.append("ORDER BY s.CM_NUM DESC FETCH FIRST 1 ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
      int paramIndex = 1;
      ps.setLong(paramIndex++, cmNum);
      ps.setString(paramIndex++, division);

      if ("student".equals(division) && category != null && !category.isEmpty()) {
        ps.setString(paramIndex++, category);
      }

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
          dto = new StudentBoardDTO();
          dto.setCmNum(rs.getLong("CM_NUM"));
          dto.setDivision(rs.getString("DIVISION"));
          dto.setTitle(rs.getString("TITLE"));
          dto.setContent(rs.getString("CONTENT"));
          dto.setViews(rs.getInt("VIEWS"));
          dto.setCaDate(rs.getString("CA_DATE"));
          dto.setFileName(rs.getString("FILENAME"));

          dto.setCategoryNum(rs.getInt("CT_NUM"));
          dto.setCategoryName(rs.getString("CT_NAME"));

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
    String sql = "UPDATE STUDENTBOARD SET VIEWS = VIEWS + 1 WHERE CM_NUM = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      ps.executeUpdate();
    }
  }

  // 데이터 개수
  public int dataCount(String division, String category, String schType, String kwd) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(*) AS cnt FROM STUDENTBOARD s ")
        .append("WHERE s.DIVISION = ? ");

    if ("student".equals(division) && category != null && !category.isEmpty()) {
      sql.append(" AND s.CT_NUM = ? ");
    }

    if (kwd != null && !kwd.isEmpty()) {
      if ("all".equals(schType)) {
        sql.append(" AND (INSTR(s.TITLE, ?) >= 1 OR INSTR(s.CONTENT, ?) >= 1) ");
      } else if ("name".equals(schType)) {
        sql.append(" AND EXISTS (SELECT 1 FROM MEMBER m WHERE m.MB_NUM = s.MB_NUM AND INSTR(m.NICKNAME, ?) >= 1) ");
      } else {
        sql.append(" AND INSTR(s.").append(schType).append(", ?) >= 1 ");
      }
    }

    int count = 0;

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      int paramIndex = 1;
      ps.setString(paramIndex++, division);

      if ("student".equals(division) && category != null && !category.isEmpty()) {
        ps.setString(paramIndex++, category);
      }

      if (kwd != null && !kwd.isEmpty()) {
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
          ps.setString(paramIndex++, kwd);
        } else if ("name".equals(schType)) {
          ps.setString(paramIndex++, kwd);
        } else {
          ps.setString(paramIndex++, kwd);
        }
      }

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

  // 좋아요 여부 확인
  public boolean isUserLiked(long cmNum, long mbNum) throws SQLException {
    boolean result = false;
    String sql = "SELECT COUNT(*) FROM STUDENT_LK WHERE CM_NUM = ? AND MB_NUM = ?";

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
    String sql = "INSERT INTO STUDENT_LK (CM_NUM, MB_NUM, DATETIME) VALUES (?, ?, SYSDATE)";

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
    String sql = "DELETE FROM STUDENT_LK WHERE CM_NUM = ? AND MB_NUM = ?";

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
    String sql = "SELECT COUNT(*) AS cnt FROM STUDENT_LK WHERE CM_NUM = ?";

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
}
