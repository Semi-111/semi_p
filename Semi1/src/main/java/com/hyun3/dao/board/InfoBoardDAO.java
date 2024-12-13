package com.hyun3.dao.board;

import com.hyun3.domain.MemberDTO;
import com.hyun3.domain.board.InfoBoardDTO;
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
    String sql;

    if (Integer.parseInt(role) >= 60) { // 관리자 권한
      sql = "DELETE FROM INFOBOARD WHERE CM_NUM = ?";
    } else {
      sql = "DELETE FROM INFOBOARD i " +
          " WHERE i.CM_NUM = ? " +
          " AND EXISTS ( " +
          "   SELECT 1 FROM MEMBER m " +
          "   WHERE m.MB_NUM = i.MB_NUM AND m.USERID = ? AND m.ROLE = ?)";
    }

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, cmNum);
      if (Integer.parseInt(role) < 60) {
        ps.setString(2, userId);
        ps.setString(3, role);
      }

      ps.executeUpdate();
    }
  }

  // 게시글 리스트 가져오기 (게시판 구분 포함)
  public List<InfoBoardDTO> listBoard(String division, int offset, int size) {
    List<InfoBoardDTO> list = new ArrayList<>();
    String sql = " SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, " +
        " TO_CHAR(i.CA_DATE, 'YYYY-MM-DD') AS CA_DATE, i.FILENAME, " +
        " m.USERID, m.NICKNAME " +
        " FROM INFOBOARD i " +
        " JOIN member m ON i.MB_NUM = m.MB_NUM " +
        " WHERE i.DIVISION = ? " +
        " ORDER BY i.CM_NUM DESC " +
        " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

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

    sql.append(" SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, ")
        .append(" TO_CHAR(i.CA_DATE, 'YYYY-MM-DD') AS CA_DATE, i.FILENAME, ")
        .append(" m.USERID, m.NICKNAME ")
        .append(" FROM INFOBOARD i ")
        .append(" JOIN member m ON i.MB_NUM = m.MB_NUM ")
        .append(" WHERE i.DIVISION = ? ");

    if ("all".equals(schType)) {
      sql.append(" AND (INSTR(i.TITLE, ?) >= 1 OR INSTR(i.CONTENT, ?) >= 1) ");
    } else {
      sql.append(" AND INSTR(").append(schType).append(", ?) >= 1 ");
    }

    sql.append(" ORDER BY i.CM_NUM DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      ps.setString(1, division);
      int paramIndex = 2;

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

  // 데이터 개수
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
    sql.append("SELECT COUNT(*) AS cnt FROM INFOBOARD WHERE DIVISION = ? ");

    if ("all".equals(schType)) {
      sql.append(" AND (INSTR(TITLE, ?) >= 1 OR INSTR(CONTENT, ?) >= 1)");
    } else {
      sql.append(" AND INSTR(").append(schType).append(", ?) >= 1");
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

}
