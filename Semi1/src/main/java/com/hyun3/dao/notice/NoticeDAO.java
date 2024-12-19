package com.hyun3.dao.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.lesson.LessonDTO;
import com.hyun3.domain.notice.NoticeDTO;
import com.hyun3.util.DBConn;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();

	public void insertNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO noticeBoard(CM_num, division, title, content, CA_date, fileName, views, MB_num, notice) "
					+ "VALUES (seq_notice_board.NEXTVAL, ?, ?, ?, SYSDATE, ?, 0, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getDivision());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getFileName());
			pstmt.setLong(5, dto.getMb_num());
			pstmt.setInt(6, dto.getNotice());

			pstmt.executeUpdate();

		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public int dataCount(String division) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM noticeBoard ";
			if (!division.equals("all")) {
				sql += " WHERE division = ?";
			}

			pstmt = conn.prepareStatement(sql);

			if (!division.equals("all")) {
				pstmt.setString(1, division);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	public int dataCount(String schType, String kwd, String division) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM noticeBoard n " + " JOIN Member m ON n.MB_num = m.MB_num ";

			if (!division.equals("all")) {
				sql += " WHERE division = ? AND ";
			} else {
				sql += " WHERE ";
			}

			if (schType.equals("all")) {
				sql += " (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ";
			} else if (schType.equals("title")) {
				sql += " INSTR(title, ?) >= 1 ";
			} else {
				sql += " INSTR(content, ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);

			int paramIndex = 1;
			if (!division.equals("all")) {
				pstmt.setString(paramIndex++, division);
			}

			if (schType.equals("all")) {
				pstmt.setString(paramIndex++, kwd);
				pstmt.setString(paramIndex, kwd);
			} else {
				pstmt.setString(paramIndex, kwd);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	public List<NoticeDTO> listBoard(int offset, int size, String division) throws SQLException {
		List<NoticeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT n.CM_num, n.division, title, content, ");
			sb.append(" TO_CHAR(CA_date, 'YYYY-MM-DD') ca_date, ");
			sb.append(" fileName, views, n.MB_num, m.nickName, n.notice ");
			sb.append(" FROM noticeBoard n ");
			sb.append(" JOIN Member m ON n.MB_num = m.MB_num ");

			if (!division.equals("0")) {
				sb.append(" WHERE n.division = ? ");
			}

			sb.append(" ORDER BY CM_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			int parameterIndex = 1;
			if (!division.equals("0")) {
				pstmt.setString(parameterIndex++, division);
			}
			pstmt.setInt(parameterIndex++, offset);
			pstmt.setInt(parameterIndex, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setCa_date(rs.getString("ca_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setViews(rs.getLong("views"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setNickName(rs.getString("nickName"));
				dto.setNotice(rs.getInt("notice")); // notice 값 설정 추가

				list.add(dto);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return list;
	}

	public List<NoticeDTO> listBoard(int offset, int size, String schType, String kwd, String division)
			throws SQLException {
		List<NoticeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT n.CM_num, n.division, title, content, ");
			sb.append(" TO_CHAR(CA_date, 'YYYY-MM-DD') ca_date, ");
			sb.append(" fileName, views, n.MB_num, m.nickName, n.notice ");
			sb.append(" FROM noticeBoard n ");
			sb.append(" JOIN Member m ON n.MB_num = m.MB_num ");
			sb.append(" WHERE ");

			if (!division.equals("0")) {
				sb.append(" n.division = ? AND ");
			}

			if (schType.equals("all")) {
				sb.append(" (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
			} else if (schType.equals("title")) {
				sb.append(" INSTR(title, ?) >= 1 ");
			} else {
				sb.append(" INSTR(content, ?) >= 1 ");
			}

			sb.append(" ORDER BY CM_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			int parameterIndex = 1;
			if (!division.equals("0")) {
				pstmt.setString(parameterIndex++, division);
			}

			if (schType.equals("all")) {
				pstmt.setString(parameterIndex++, kwd);
				pstmt.setString(parameterIndex++, kwd);
			} else {
				pstmt.setString(parameterIndex++, kwd);
			}

			pstmt.setInt(parameterIndex++, offset);
			pstmt.setInt(parameterIndex, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setCa_date(rs.getString("ca_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setViews(rs.getLong("views"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setNickName(rs.getString("nickName"));
				dto.setNotice(rs.getInt("notice")); // notice 값 설정 추가

				list.add(dto);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return list;
	}

	public void updateHitCount(long noticeNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE noticeBoard SET views=views+1 WHERE CM_num=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, noticeNum);
			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public NoticeDTO findById(long noticeNum) throws SQLException {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT n.CM_num, n.division, n.title, n.content, " + " TO_CHAR(n.CA_date, 'YYYY-MM-DD') ca_date, "
					+ " n.fileName, n.views, n.MB_num, m.nickName, n.notice " // notice 추가
					+ " FROM noticeBoard n " + " JOIN Member m ON n.MB_num = m.MB_num " + " WHERE n.CM_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, noticeNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setCa_date(rs.getString("ca_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setViews(rs.getLong("views"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setNickName(rs.getString("nickName"));
				dto.setNotice(rs.getInt("notice")); // notice 값도 설정
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	// 이전글
	public NoticeDTO findByPrev(long noticeNum, String division) throws SQLException {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (division.equals("all")) {
				sb.append("SELECT CM_num, title ");
				sb.append(" FROM noticeBoard ");
				sb.append(" WHERE CM_num > ? ");
				sb.append(" ORDER BY CM_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			} else {
				sb.append("SELECT CM_num, title ");
				sb.append(" FROM noticeBoard ");
				sb.append(" WHERE CM_num > ? AND division = ? ");
				sb.append(" ORDER BY CM_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			}

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, noticeNum);
			if (!division.equals("all")) {
				pstmt.setString(2, division);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setTitle(rs.getString("title"));
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	// 다음글
	public NoticeDTO findByNext(long noticeNum, String division) throws SQLException {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (division.equals("all")) {
				sb.append("SELECT CM_num, title ");
				sb.append(" FROM noticeBoard ");
				sb.append(" WHERE CM_num < ? ");
				sb.append(" ORDER BY CM_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			} else {
				sb.append("SELECT CM_num, title ");
				sb.append(" FROM noticeBoard ");
				sb.append(" WHERE CM_num < ? AND division = ? ");
				sb.append(" ORDER BY CM_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			}

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, noticeNum);
			if (!division.equals("all")) {
				pstmt.setString(2, division);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setTitle(rs.getString("title"));
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	// 학과 목록 가져오기
	public List<LessonDTO> getLessonList() throws SQLException {
		List<LessonDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT lessonNum, lessonName FROM lesson ORDER BY lessonNum";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LessonDTO dto = new LessonDTO();
				dto.setLessonNum(rs.getInt("lessonNum"));
				dto.setLessonName(rs.getString("lessonName"));
				list.add(dto);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return list;
	}

	public void updateNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE noticeBoard SET division=?, title=?, content=?, notice=? " + " WHERE CM_num=?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getDivision());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getNotice());
			pstmt.setLong(5, dto.getCm_num());

			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void deleteNotice(long noticeNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM noticeBoard WHERE CM_num=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, noticeNum);
			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
