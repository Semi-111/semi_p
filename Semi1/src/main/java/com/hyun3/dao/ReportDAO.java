package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.ReportDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class ReportDAO {
	private Connection conn = DBConn.getConnection();

	// 신고 등록
	public void insertReport(ReportDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		System.out.println("DAO insert 시도:");
		System.out.println("제목: " + dto.getRP_title());
		System.out.println("내용: " + dto.getRP_content());
		try {
			sql = "INSERT INTO Report(RP_num, RP_title, RP_content, RP_reason, " + " RP_table, RP_url, MB_num) "
					+ " VALUES (seq_report.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getRP_title());
			pstmt.setString(2, dto.getRP_content());
			pstmt.setString(3, dto.getRP_reason());
			pstmt.setString(4, dto.getRP_table());
			pstmt.setString(5, dto.getRP_url());
			pstmt.setLong(6, dto.getMb_num());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 신고 목록
	public List<ReportDTO> listReport() {
		List<ReportDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
			sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
			sb.append("    r.MB_num, m.nickName ");
			sb.append(" FROM Report r ");
			sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
			sb.append(" ORDER BY r.RP_num DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportDTO dto = new ReportDTO();

				dto.setRP_num(rs.getLong("RP_num"));
				dto.setRP_title(rs.getString("RP_title"));
				dto.setRP_content(rs.getString("RP_content"));
				dto.setRP_reason(rs.getString("RP_reason"));
				dto.setRP_table(rs.getString("RP_table"));
				dto.setRP_url(rs.getString("RP_url"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setMemberName(rs.getString("nickName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 신고글 상세 정보
	public ReportDTO findById(long rpNum) {
		ReportDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
			sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
			sb.append("    r.MB_num, m.nickName ");
			sb.append(" FROM Report r ");
			sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
			sb.append(" WHERE r.RP_num = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setLong(1, rpNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReportDTO();

				dto.setRP_num(rs.getLong("RP_num"));
				dto.setRP_title(rs.getString("RP_title"));
				dto.setRP_content(rs.getString("RP_content"));
				dto.setRP_reason(rs.getString("RP_reason"));
				dto.setRP_table(rs.getString("RP_table"));
				dto.setRP_url(rs.getString("RP_url"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setMemberName(rs.getString("nickName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 신고글 삭제
	public void deleteReport(long rpNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM Report WHERE RP_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, rpNum);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 전체 데이터 개수
	public int dataCount() throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM Report";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 검색에서 전체 데이터 개수
	public int dataCount(String schType, String kwd) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM Report r " + " JOIN Member m ON r.MB_num = m.MB_num ";
			if (schType.equals("all")) {
				sql += " WHERE INSTR(RP_title, ?) >= 1 OR INSTR(RP_content, ?) >= 1 ";
			} else if (schType.equals("reportTitle")) {
				sql += " WHERE INSTR(RP_title, ?) >= 1 ";
			} else if (schType.equals("reportContent")) {
				sql += " WHERE INSTR(RP_content, ?) >= 1 ";
			} else if (schType.equals("nickName")) {
				sql += " WHERE INSTR(m.nickName, ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
			} else {
				pstmt.setString(1, kwd);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 페이징 처리한 데이터 리스트
	public List<ReportDTO> listReport(int offset, int size) {
		List<ReportDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
			sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
			sb.append("    r.MB_num, m.nickName ");
			sb.append(" FROM Report r ");
			sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
			sb.append(" ORDER BY r.RP_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportDTO dto = new ReportDTO();

				dto.setRP_num(rs.getLong("RP_num"));
				dto.setRP_title(rs.getString("RP_title"));
				dto.setRP_content(rs.getString("RP_content"));
				dto.setRP_reason(rs.getString("RP_reason"));
				dto.setRP_table(rs.getString("RP_table"));
				dto.setRP_url(rs.getString("RP_url"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setMemberName(rs.getString("nickName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 검색에서 리스트
	public List<ReportDTO> listReport(int offset, int size, String schType, String kwd) {
		List<ReportDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
			sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
			sb.append("    r.MB_num, m.nickName ");
			sb.append(" FROM Report r ");
			sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");

			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(RP_title, ?) >= 1 OR INSTR(RP_content, ?) >= 1 ");
			} else if (schType.equals("reportTitle")) {
				sb.append(" WHERE INSTR(RP_title, ?) >= 1 ");
			} else if (schType.equals("reportContent")) {
				sb.append(" WHERE INSTR(RP_content, ?) >= 1 ");
			} else if (schType.equals("nickName")) {
				sb.append(" WHERE INSTR(m.nickName, ?) >= 1 ");
			}

			sb.append(" ORDER BY r.RP_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportDTO dto = new ReportDTO();

				dto.setRP_num(rs.getLong("RP_num"));
				dto.setRP_title(rs.getString("RP_title"));
				dto.setRP_content(rs.getString("RP_content"));
				dto.setRP_reason(rs.getString("RP_reason"));
				dto.setRP_table(rs.getString("RP_table"));
				dto.setRP_url(rs.getString("RP_url"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setMemberName(rs.getString("nickName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public ReportDTO findByIdWithPostInfo(long rpNum) throws SQLException {
		ReportDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			// rp_url 컬럼 삭제
			sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, r.RP_reason, ");
	        sb.append("    r.RP_table, r.MB_num, r.target_mb_num, ");
	        sb.append("    m1.nickName memberName, m2.nickName postWriter ");
	        sb.append(" FROM Report r ");
	        sb.append(" JOIN Member m1 ON r.MB_num = m1.MB_num ");  // 신고자 정보
	        sb.append(" JOIN Member m2 ON r.target_mb_num = m2.MB_num ");  // 신고당한 사람 정보
	        sb.append(" WHERE r.RP_num = ?");
			
	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setLong(1, rpNum);
	        rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReportDTO();

				dto.setRP_num(rs.getLong("RP_num"));
	            dto.setRP_title(rs.getString("RP_title"));
	            dto.setRP_content(rs.getString("RP_content"));
	            dto.setRP_reason(rs.getString("RP_reason"));
	            dto.setRP_table(rs.getString("RP_table"));
	            dto.setMb_num(rs.getLong("MB_num"));
	            dto.setTargetMbNum(rs.getLong("target_mb_num"));
	            dto.setMemberName(rs.getString("memberName"));
	            dto.setPostWriter(rs.getString("postWriter"));

				// 게시글 정보
				dto.setPostTitle(rs.getString("postTitle"));
				dto.setPostContent(rs.getString("postContent"));
				dto.setPostWriter(rs.getString("postWriter"));
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 사용자 block 처리 차단
	public void userBlock(long mb_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE member SET block = 1 WHERE MB_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, mb_num);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 사용자 block 처리 해제
	public void userUnBlock(long mb_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE member SET block = 0 WHERE MB_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, mb_num);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

}