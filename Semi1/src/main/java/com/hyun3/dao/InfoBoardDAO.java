package com.hyun3.dao;

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

  public void insertBoard(InfoBoardDTO dto, String userId) throws SQLException {
    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // 트랜잭션 시작

      String sql = "INSERT INTO INFOBOARD (cm_num, DIVISION, title, content, ca_date, fileName, views, MB_NUM) " +
          " VALUES (SEQ_INFO_BOARD.nextval, ?, ?, ?, SYSDATE, ?, 0, " +
          "        (SELECT MB_NUM FROM member WHERE userId = ?))";

      // 회원번호(mb_num)은 userId를 통해 삽입

      ps = conn.prepareStatement(sql);

      ps.setString(1, dto.getDivision());
      ps.setString(2, dto.getTitle());
      ps.setString(3, dto.getContent());
      ps.setString(4, dto.getFileName());
      ps.setString(5, userId);

      ps.executeUpdate();

      conn.commit();
    } catch (Exception e) {
      conn.rollback();
      e.printStackTrace();
    } finally {
      DBUtil.close(ps);
      conn.setAutoCommit(true);
    }
  }

  public List<InfoBoardDTO> listBoard(int offset, int size) {
    List<InfoBoardDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, " +
          " TO_CHAR(i.CA_DATE, 'YYYY-MM-DD') AS CA_DATE, " +
          " m.USERID, m.NICKNAME" +
          // , m.NAME
          " FROM INFOBOARD i " +
          " JOIN member m ON i.MB_NUM = m.MB_NUM " +
          " WHERE m.ROLE = 0 " +
          " ORDER BY i.CM_NUM DESC " +
          " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

      ps = conn.prepareStatement(sql);
      ps.setInt(1, offset);
      ps.setInt(2, size); // 페이징 크키 설정

      rs = ps.executeQuery();

      while (rs.next()) {
        InfoBoardDTO dto = new InfoBoardDTO();
        dto.setCmNum(rs.getInt("CM_NUM"));
        dto.setTitle(rs.getString("TITLE"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setViews(rs.getInt("VIEWS"));
        dto.setCaDate(rs.getString("CA_DATE"));

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setNickName(rs.getString("NICKNAME"));
//        member.setName(rs.getString("NAME"));

        dto.setMember(member);
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

  public List<InfoBoardDTO> listBoard(int offset, int size, String schType, String kwd) {
    List<InfoBoardDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sql = new StringBuilder();

    try {
      sql.append(" SELECT i.CM_NUM, i.TITLE, i.CONTENT, i.VIEWS, ")
          .append(" TO_CHAR(i.CA_DATE, 'YYYY-MM-DD') AS CA_DATE, ")
          .append(" m.USERID, m.NICKNAME ")
          .append(" FROM INFOBOARD i ")
          .append(" JOIN member m ON i.MB_NUM = m.MB_NUM ")
          .append(" WHERE m.ROLE = 40 ");

      // INSTR - DB에서 문자열 내의 특정 문자열의 위치를 반환, 존재하지 않으면 0을 반환
      if ("all".equals(schType)) {
        sql.append(" AND (INSTR(i.TITLE, ?) >= 1 OR INSTR(i.CONTENT, ?) >= 1) ");
      } else if (" CA_DATE".equals(schType)) {
        kwd = kwd.replaceAll("[-./]", ""); // yyyyMMdd 형식으로 변환
        sql.append(" AND TO_CHAR(i.CA_DATE, 'YYYYMMDD') = ? ");
      } else {
        sql.append(" AND INSTR(").append(schType).append(", ?) >= 1 ");
      }

      sql.append(" ORDER BY i.CM_NUM DESC ")
          .append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

      ps = conn.prepareStatement(sql.toString());

      if(schType.equals("all")) {
        ps.setString(1, kwd);
        ps.setString(2, kwd);
        ps.setInt(3, offset);
        ps.setInt(4, size);
      } else {
        ps.setString(1, kwd);
        ps.setInt(1, offset);
        ps.setInt(1, size);
      }

      rs = ps.executeQuery();

      while (rs.next()) {
        InfoBoardDTO dto = new InfoBoardDTO();
        dto.setCmNum(rs.getInt("CM_NUM"));
        dto.setTitle(rs.getString("TITLE"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setViews(rs.getInt("VIEWS"));
        dto.setCaDate(rs.getString("CA_DATE"));

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setName(rs.getString("NICKNAME"));

        dto.setMember(member);
        list.add(dto);
      }

    } catch (SQLException e) {
      e.printStackTrace(); // 적절한 로깅 시스템으로 대체 권장
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return list;
  }

  public void updateViews(long cmNum) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "UPDATE INFOBOARD SET VIEWS = VIEWS + 1 WHERE cmNum = ?";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, cmNum); // 게시글 번호

      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(ps);
    }
  }

  public int dataCount(){
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "select count(*) cnt from INFOBOARD";

      ps = conn.prepareStatement(sql);

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

  public int dataCount(String schType, String kwd) {
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT COUNT(*) cnt "
          + " FROM INFOBOARD i  "
          + " JOIN member m ON i.userId = m.userId "
          + " WHERE block = 0 ";

      if(schType.equals("all")) { // subject 또는 content
        sql += " AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )"; // 문자열에서 특정 문자열의 위치를 찾는 함수 위치는 1부터 시작
      } else if(schType.equals("reg_date")) {
        kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
        sql += " AND TO_CHAR(CA_DATE, 'YYYYMMDD') = ? ";
      } else { // userName, subject, content
        sql += " AND INSTR(" + schType + ", ?) >= 1";
      }

      ps = conn.prepareStatement(sql);

      ps.setString(1, kwd);
      if(schType.equals("all")) {
        ps.setString(2, kwd);
      }

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

  // 글 찾기
  public InfoBoardDTO findByNum(long cmNum) {
    InfoBoardDTO dto = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT I.CM_NUM, I.DIVISION, I.TITLE, I.CONTENT, I.CA_DATE, I.FILENAME, I.VIEWS, I.MB_NUM, " +
          " M.USERID, M.NICKNAME, M.ROLE " +
          " FROM INFOBOARD I " +
          " JOIN MEMBER M ON M.MB_NUM = I.MB_NUM " +
          " WHERE I.CM_NUM = ? AND M.ROLE = 40";

      ps = conn.prepareStatement(sql);

      ps.setLong(1, cmNum);

      rs = ps.executeQuery();

      if(rs.next()) {
        dto = new InfoBoardDTO();

        dto.setCmNum(rs.getLong("CM_NUM"));
        dto.setTitle(rs.getString("TITLE"));
        dto.setContent(rs.getString("content"));
        dto.setViews(rs.getInt("VIEWS"));
        dto.setCaDate(rs.getString("CA_DATE"));

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setName(rs.getString("NICKNAME"));
        member.setRole(rs.getInt("ROLE"));
        dto.setMember(member);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(ps);
    }

    return dto;
  }

  // 이전글 찾기
  public InfoBoardDTO findByPrev(long cmNum, String schType, String kwd) {
    InfoBoardDTO dto = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append(" SELECT I.CM_NUM, I.TITLE, I.CONTENT, I.CA_DATE, I.FILENAME, I.VIEWS, M.USERID, M.NICKNAME, M.ROLE ")
          .append(" FROM INFOBOARD I ")
          .append(" JOIN MEMBER M ON M.MB_NUM = I.MB_NUM ")
          .append(" WHERE I.CM_NUM > ? AND M.ROLE = 40 ");

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
      int paramIndex = 1;

      ps.setLong(paramIndex++, cmNum);
      if (kwd != null && !kwd.isEmpty()) {
        ps.setString(paramIndex++, kwd);
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
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

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setName(rs.getString("NICKNAME"));
        member.setRole(rs.getInt("ROLE"));
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
  public InfoBoardDTO findByNext(long cmNum, String schType, String kwd) {
    InfoBoardDTO dto = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append("SELECT I.CM_NUM, I.TITLE, I.CONTENT, I.CA_DATE, I.FILENAME, I.VIEWS, M.USERID, M.NICKNAME, M.ROLE ")
          .append("FROM INFOBOARD I ")
          .append("JOIN MEMBER M ON M.MB_NUM = I.MB_NUM ")
          .append("WHERE I.CM_NUM < ? AND M.ROLE = 40 ");

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
      int paramIndex = 1;

      ps.setLong(paramIndex++, cmNum);
      if (kwd != null && !kwd.isEmpty()) {
        ps.setString(paramIndex++, kwd);
        if ("all".equals(schType)) {
          ps.setString(paramIndex++, kwd);
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

        MemberDTO member = new MemberDTO();
        member.setUserId(rs.getString("USERID"));
        member.setName(rs.getString("NICKNAME"));
        member.setRole(rs.getInt("ROLE"));
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
