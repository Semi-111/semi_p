package com.hyun3.dao;

import com.hyun3.domain.MemberDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
  private Connection conn = DBConn.getConnection();

  public MemberDTO loginMember(String userId, String pwd) {
    MemberDTO dto = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql;

    try {
      sql = "SELECT mb_Num, userId, NICKNAME, "
          + " role, ca_Day, modifyDay "
          + " FROM member "
          + " WHERE userId = ? AND pwd = ? AND block = 1";

      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, userId);
      pstmt.setString(2, pwd);

      rs = pstmt.executeQuery();

      if(rs.next()) {
        dto = new MemberDTO();

        dto.setMb_Num(rs.getLong("mb_Num"));
        dto.setUserId(rs.getString("userId"));
        dto.setName(rs.getString("name"));
        dto.setRole(rs.getInt("role"));
        dto.setCa_Day(rs.getString("ca_Day"));
        dto.setModifyDay(rs.getString("modifyDay"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(pstmt);
    }

    return dto;
  }

  public void insertMember(MemberDTO dto) throws SQLException {
    PreparedStatement pstmt = null;
    String sql;

    try {
      conn.setAutoCommit(false);

      sql = "INSERT INTO member(mb_Num, userId, pwd, NICKNAME, nickName, block, role, ca_Day, modifyDay) "
          + " VALUES (member_seq.NEXTVAL, ?, ?, ?, ?, 1, 1, SYSDATE, SYSDATE)";
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, dto.getUserId());
      pstmt.setString(2, dto.getPwd());
      pstmt.setString(3, dto.getName());
      pstmt.setString(4, dto.getNickName());

      pstmt.executeUpdate();

      pstmt.close();
      pstmt = null;

      sql = "INSERT INTO dt_member(userId, birth, email, tel) VALUES (?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?)";
      pstmt=conn.prepareStatement(sql);

      pstmt.setString(1, dto.getUserId());
      pstmt.setString(2, dto.getBirth());
      pstmt.setString(3, dto.getEmail());
      pstmt.setString(4, dto.getTel());

      pstmt.executeUpdate();

			/*
		    sql = "INSERT ALL "
		    	+ " INTO member1(memberIdx, userId, userPwd, userName, enabled, userLevel, register_date, modify_date) VALUES(member_seq.NEXTVAL, ?, ?, ?, 1, 1, SYSDATE, SYSDATE) "
		    	+ " INTO member2(userId, birth, email, tel, zip, addr1, addr2) VALUES (?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?) "
		    	+ " SELECT * FROM dual";

		    pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());

			pstmt.setString(4, dto.getUserId());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getEmail());
			pstmt.setString(7, dto.getTel());
			pstmt.setString(8, dto.getZip());
			pstmt.setString(9, dto.getAddr1());
			pstmt.setString(10, dto.getAddr2());

			pstmt.executeUpdate();
		*/
      conn.commit();

    } catch (SQLException e) {
      DBUtil.rollback(conn);

      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(pstmt);

      try {
        conn.setAutoCommit(true); // 테이블 2개 이상 넣을때 commit을 건드려야한다.
      } catch (SQLException e2) {
      }
    }

  }

  public MemberDTO findById(String userId) {
    MemberDTO dto = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    StringBuilder sb = new StringBuilder();

    try {
      sb.append("SELECT mb_Num, m1.userId, pwd, name, ");
      sb.append("      block, role, ca_Day, modifyDay,");
      sb.append("      TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
      sb.append("      email, tel");
      sb.append("  FROM member1 m1");
      sb.append("  LEFT OUTER JOIN dt_member m2 ON m1.userId=m2.userId ");
      sb.append("  WHERE m1.userId = ?");

      pstmt = conn.prepareStatement(sb.toString());

      pstmt.setString(1, userId);

      rs = pstmt.executeQuery();

      if(rs.next()) {
        dto = new MemberDTO();

        dto.setMb_Num(rs.getLong("mb_Num"));
        dto.setUserId(rs.getString("userId"));
        dto.setPwd(rs.getString("pwd"));
        dto.setName(rs.getString("name"));
        dto.setBlock(rs.getInt("block"));
        dto.setRole(rs.getInt("role"));
        dto.setCa_Day(rs.getString("ca_Day"));
        dto.setModifyDay(rs.getString("modifyDay"));
        dto.setBirth(rs.getString("birth"));
        dto.setTel(rs.getString("tel"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(rs);
      DBUtil.close(pstmt);
    }

    return dto;
  }

  public void updateMember(MemberDTO dto) throws SQLException {
    PreparedStatement pstmt = null;
    String sql;

    try {
      sql = "UPDATE member SET pwd=?, modifyDay=SYSDATE WHERE userId=?";
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, dto.getPwd());
      pstmt.setString(2, dto.getUserId());

      pstmt.executeUpdate();

      pstmt.close();
      pstmt = null;

      sql = "UPDATE dt_member SET birth=TO_DATE(?,'YYYY-MM-DD'), email=?, tel=?, WHERE userId=?";
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, dto.getBirth());
      pstmt.setString(2, dto.getEmail());
      pstmt.setString(3, dto.getTel());
      pstmt.setString(4, dto.getUserId());

      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(pstmt);
    }
  }

  public void updateMemberLevel(String userId, int role) throws SQLException {
    PreparedStatement pstmt = null;
    String sql;

    try {
      sql = "UPDATE member SET role=? WHERE userId=?";
      pstmt = conn.prepareStatement(sql);

      pstmt.setInt(1, role);
      pstmt.setString(2, userId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(pstmt);
    }
  }


  public void deleteMember(String userId) throws SQLException {
    PreparedStatement pstmt = null;
    String sql;

    try {
      sql = "UPDATE member SET block.=0, role=0 WHERE userId=?";
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, userId);

      pstmt.executeUpdate();

      pstmt.close();
      pstmt = null;

      sql = "DELETE FROM dt_member WHERE userId=?";
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1, userId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      DBUtil.close(pstmt);
    }
  }

}
