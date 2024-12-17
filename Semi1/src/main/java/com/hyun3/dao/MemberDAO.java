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
			sql = "SELECT mb_Num, userId, nickname, " + " role, ca_Day, modifyDay, lessonNum " + " FROM member "
					+ " WHERE userId = ? AND pwd = ? AND block = 0";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);
			pstmt.setString(2, pwd);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				dto.setMb_Num(rs.getLong("mb_Num"));
				dto.setUserId(rs.getString("userId"));
				dto.setName(rs.getString("nickname"));
				dto.setRole(rs.getString("role"));
				dto.setCa_Day(rs.getString("ca_Day"));
				dto.setModifyDay(rs.getString("modifyDay"));
				dto.setLessonNum(rs.getInt("lessonNum")); // lessonNum 설정 추가
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
		// 시퀀스 값을 가져오는 SQL
		sql = "SELECT member_seq.NEXTVAL FROM dual";
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		int seqValue = 0;
		if (rs.next()) {
			seqValue = rs.getInt(1);
		}

		try {
			conn.setAutoCommit(false);

			sql = "INSERT INTO member(mb_Num, userId, pwd, nickName, ca_Day, role, lessonNum, block) VALUES (?, ?, ?, ?, SYSDATE, 1, ?, 1)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, seqValue);
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getPwd());
			pstmt.setString(4, dto.getNickName());
			pstmt.setInt(5, dto.getLessonNum());

			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			sql = "INSERT INTO dt_member(mb_Num, name, email, birthday, tel, studentnum) VALUES (?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, seqValue);
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getBirth());
			pstmt.setString(5, dto.getTel());
			pstmt.setInt(6, dto.getStudentNum());

			pstmt.executeUpdate();

			/*
			 * sql = "INSERT ALL " +
			 * " INTO member(mb_Num, userId, pwd, nickName, ca_Day, modifyDay, role, lessonNum, block) VALUES (member_seq.NEXTVAL, ?, ?, ?, SYSDATE, SYSDATE, 1, ?, 1) "
			 * +
			 * " INTO dt_member(mb_Num, name, email, birthday, tel, studentnum) VALUES (member_seq.NEXTVAL, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?) "
			 * + " SELECT * FROM dual";
			 * 
			 * pstmt = conn.prepareStatement(sql);
			 * 
			 * ...
			 * 
			 * pstmt.executeUpdate();
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
			sb.append("SELECT m.mb_Num, m.userId, m.nickName, m.role, ");
			sb.append("       m.ca_Day, m.modifyDay, m.lessonNum, ");
			sb.append("       dt.name, dt.email, TO_CHAR(dt.birthday, 'YYYY-MM-DD') birthday, ");
			sb.append("       dt.tel, dt.studentnum ");
			sb.append("  FROM member m ");
			sb.append("  LEFT OUTER JOIN dt_member dt ON m.mb_Num = dt.mb_Num ");
			sb.append("  WHERE m.userId = ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				// member 테이블 데이터 설정
				dto.setMb_Num(rs.getLong("mb_Num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setRole(rs.getString("role"));
				dto.setCa_Day(rs.getString("ca_Day"));
				dto.setModifyDay(rs.getString("modifyDay"));
				dto.setLessonNum(rs.getInt("lessonNum"));

				// dt_member 테이블 데이터 설정
				dto.setName(rs.getString("name"));

				dto.setEmail(rs.getString("email"));
				/*
				 * if (dto.getEmail() != null) { String[] emailParts =
				 * dto.getEmail().split("@"); if (emailParts.length == 2) {
				 * dto.setEmail1(emailParts[0]); dto.setEmail2(emailParts[1]); } }
				 */

				dto.setBirth(rs.getString("birthday"));

				dto.setTel(rs.getString("tel"));
				/*
				 * if (dto.getTel() != null) { String[] telParts = dto.getTel().split("-"); if
				 * (telParts.length == 3) { dto.setTel1(telParts[0]); dto.setTel2(telParts[1]);
				 * dto.setTel3(telParts[2]); } }
				 */

				dto.setStudentNum(rs.getInt("studentnum"));
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
			/*
			 * sql = "UPDATE member SET pwd=?, modifyDay=SYSDATE WHERE userId = ?"; pstmt =
			 * conn.prepareStatement(sql);
			 * 
			 * pstmt.setString(1, dto.getPwd()); pstmt.setString(2, dto.getUserId());
			 * pstmt.executeUpdate();
			 * 
			 * pstmt.close(); pstmt = null;
			 * 
			 * sql =
			 * "UPDATE dt_member SET email=?, birthday=TO_DATE(?,'YYYY-MM-DD'), tel=? WHERE =?"
			 * ; pstmt = conn.prepareStatement(sql);
			 * 
			 * pstmt.setString(1, dto.getEmail()); pstmt.setString(2, dto.getBirth());
			 * pstmt.setString(3, dto.getTel()); pstmt.setLong(4, dto.getMb_Num());
			 * pstmt.executeUpdate();
			 * 
			 * pstmt.executeUpdate();
			 */

			sql = "SELECT mb_Num FROM member WHERE userId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				long mb_Num = rs.getLong("mb_Num");

				// 2. dt_member 테이블 업데이트
				sql = "UPDATE dt_member SET email = ?, birthday = TO_DATE(?, 'YYYY-MM-DD'), tel = ? WHERE mb_Num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEmail());
				pstmt.setString(2, dto.getBirth());
				pstmt.setString(3, dto.getTel());
				pstmt.setLong(4, mb_Num);
				pstmt.executeUpdate();
			}

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
		ResultSet rs = null;
		String sql;

		try {
			// 1. 먼저 member 테이블에서 userId에 해당하는 mb_Num을 조회
			sql = "SELECT mb_Num FROM member WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			long mb_Num = 0;
			if (rs.next()) {
				mb_Num = rs.getLong("mb_Num");
			}

			pstmt.close();
			pstmt = null;
			rs.close();

			// 2. member 테이블에서 해당 userId를 비활성화 처리 (block=1, role=0)
			sql = "UPDATE member SET block=1, role=0 WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			// 3. dt_member 테이블에서 mb_Num을 기준으로 데이터 삭제
			if (mb_Num > 0) {
				sql = "DELETE FROM dt_member WHERE mb_Num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, mb_Num);
				pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
	}

}
