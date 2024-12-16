package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.LessonDTO;
import com.hyun3.domain.LessonLikeDTO;
import com.hyun3.domain.LessonReplyDTO;
import com.hyun3.domain.sw.ReplyDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class LessonDAO {
	public Connection conn = DBConn.getConnection();

	public void insertLesson(LessonDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO lessonBoard(CM_num, division, title, content, "
					+ " CA_date, fileName, views, MB_num, lessonNum) "
					+ " VALUES (seq_lesson_board.NEXTVAL, ?, ?, ?, SYSDATE, ?, 0, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getDivision());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getBoard_content());
			pstmt.setString(4, dto.getFileName());
			pstmt.setLong(5, dto.getMb_num());
			pstmt.setInt(6, dto.getLessonNum());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public int dataCount() throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM lessonBoard";
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

	public int dataCount(String schType, String kwd) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM lessonBoard b " + " JOIN Member m ON b.MB_num = m.MB_num ";
			if (schType.equals("all")) {
				sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 "; // board_content가 아닌 content
			} else if (schType.equals("title")) {
				sql += " WHERE INSTR(title, ?) >= 1 ";
			} else {
				sql += " WHERE INSTR(content, ?) >= 1 "; // board_content가 아닌 content
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

	public List<LessonDTO> listBoard(int offset, int size) throws SQLException {
		List<LessonDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT CM_num, division, title, content, ");
			sb.append("    TO_CHAR(CA_date, 'YYYY-MM-DD') CA_date, fileName, views, ");
			sb.append("    m.nickName, l.lessonName ");
			sb.append(" FROM lessonBoard b ");
			sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
			sb.append(" JOIN lesson l ON b.lessonNum = l.lessonNum ");
			sb.append(" ORDER BY CM_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LessonDTO dto = new LessonDTO();

				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setBoard_content(rs.getString("content"));
				dto.setCa_date(rs.getString("CA_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setViews(rs.getLong("views"));
				dto.setNickName(rs.getString("nickName"));
				dto.setLessonName(rs.getString("lessonName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public List<LessonDTO> listBoard(int offset, int size, String schType, String kwd) throws SQLException {
		List<LessonDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT CM_num, division, title, content, ");
			sb.append("    TO_CHAR(CA_date, 'YYYY-MM-DD') CA_date, fileName, views, ");
			sb.append("    m.nickName, l.lessonName ");
			sb.append(" FROM lessonBoard b ");
			sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
			sb.append(" JOIN lesson l ON b.lessonNum = l.lessonNum ");

			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("title")) {
				sb.append(" WHERE INSTR(title, ?) >= 1 ");
			} else if (schType.equals("content")) {
				sb.append(" WHERE INSTR(content, ?) >= 1 ");
			} else if (schType.equals("nickName")) {
				sb.append(" WHERE INSTR(m.nickName, ?) >= 1 ");
			}

			sb.append(" ORDER BY CM_num DESC ");
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
				LessonDTO dto = new LessonDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setBoard_content(rs.getString("content"));
				dto.setCa_date(rs.getString("CA_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setViews(rs.getLong("views"));
				dto.setNickName(rs.getString("nickName"));
				dto.setLessonName(rs.getString("lessonName"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 게시글 보기
	public LessonDTO findById(long num) throws SQLException {
		LessonDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.CM_num, b.division, b.title, b.content, " + " b.views, b.CA_date, b.fileName, "
					+ " b.MB_num, m.nickName, " + " b.lessonNum, l.lessonName " + " FROM lessonBoard b "
					+ " JOIN Member m ON b.MB_num = m.MB_num " + " JOIN lesson l ON b.lessonNum = l.lessonNum "
					+ " WHERE b.CM_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new LessonDTO();

				dto.setCm_num(rs.getLong("CM_num"));
				dto.setDivision(rs.getString("division"));
				dto.setTitle(rs.getString("title"));
				dto.setBoard_content(rs.getString("content"));
				dto.setViews(rs.getLong("views"));
				dto.setCa_date(rs.getString("CA_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setMb_num(rs.getLong("MB_num"));
				dto.setNickName(rs.getString("nickName"));
				dto.setLessonNum(rs.getInt("lessonNum"));
				dto.setLessonName(rs.getString("lessonName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 조회수 증가
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE lessonBoard SET views=views+1 WHERE CM_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 이전글
	public LessonDTO findByPrev(long num, String schType, String kwd) throws SQLException {
		LessonDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT CM_num, title ");
				sb.append(" FROM lessonBoard b ");
				sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
				sb.append(" WHERE CM_num > ? ");
				if (schType.equals("all")) {
					sb.append("   AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else if (schType.equals("title")) {
					sb.append("   AND INSTR(title, ?) >= 1 ");
				} else if (schType.equals("content")) {
					sb.append("   AND INSTR(content, ?) >= 1 ");
				} else if (schType.equals("nickName")) {
					sb.append("   AND INSTR(m.nickName, ?) >= 1 ");
				}
				sb.append(" ORDER BY CM_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT CM_num, title ");
				sb.append(" FROM lessonBoard ");
				sb.append(" WHERE CM_num > ? ");
				sb.append(" ORDER BY CM_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new LessonDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public LessonDTO findByNext(long num, String schType, String kwd) throws SQLException {
		LessonDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT CM_num, title ");
				sb.append(" FROM lessonBoard b ");
				sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
				sb.append(" WHERE CM_num < ? ");
				if (schType.equals("all")) {
					sb.append("   AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else if (schType.equals("title")) {
					sb.append("   AND INSTR(title, ?) >= 1 ");
				} else if (schType.equals("content")) {
					sb.append("   AND INSTR(content, ?) >= 1 ");
				} else if (schType.equals("nickName")) {
					sb.append("   AND INSTR(m.nickName, ?) >= 1 ");
				}
				sb.append(" ORDER BY CM_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT CM_num, title ");
				sb.append(" FROM lessonBoard ");
				sb.append(" WHERE CM_num < ? ");
				sb.append(" ORDER BY CM_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new LessonDTO();
				dto.setCm_num(rs.getLong("CM_num"));
				dto.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 게시글 삭제
	public void deleteLesson(long cm_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM lessonBoard WHERE cm_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, cm_num);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 글수정
	public void updateLesson(LessonDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE lessonBoard SET title=?, content=?, filename=?, " + " LESSONNUM = ? " + " WHERE cm_num=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getBoard_content());
			pstmt.setString(3, dto.getFileName());
			pstmt.setInt(4, dto.getLessonNum());
			pstmt.setLong(5, dto.getCm_num());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 게시글의 댓글 리스트
	public List<LessonReplyDTO> listReply(long cm_num) throws SQLException {
		List<LessonReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.reply_num, r.cm_num, r.mb_num, r.co_content, "
					+ " TO_CHAR(r.reg_date, 'YYYY-MM-DD HH24:MI:SS') reg_date, " + " m.nickName " + " FROM reply r "
					+ " JOIN Member m ON r.mb_num = m.mb_num " + " WHERE r.cm_num = ? " + " ORDER BY r.reply_num DESC ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, cm_num);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LessonReplyDTO dto = new LessonReplyDTO();

				// dto.setReply_num(rs.getLong("reply_num"));
				dto.setCm_num(rs.getLong("cm_num"));
				dto.setMb_num(rs.getLong("mb_num"));
				dto.setCo_content(rs.getString("co_content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setNickName(rs.getString("nickName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 댓글 삭제
	public void deleteReply(long reply_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM reply WHERE reply_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, reply_num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 댓글 추가
	public void insertReply(ReplyDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO lesson_CO(CO_num, content, reg_date, MB_num, CM_num) "
					+ " VALUES (seq_lesson_CO.NEXTVAL, ?, SYSDATE, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getContent());
			pstmt.setLong(2, dto.getMb_num());
			pstmt.setInt(3, dto.getCm_num());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 댓글 개수
	public int dataCount(int cm_num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM lesson_CO WHERE CM_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cm_num);

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

	// 댓글 목록
	public List<ReplyDTO> listReply(int cm_num, int offset, int size) throws SQLException {
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT r.CO_num, r.MB_num, m.nickName, ");
			sb.append("    r.content, TO_CHAR(r.reg_date, 'YYYY-MM-DD HH24:MI:SS') reg_date ");
			sb.append(" FROM lesson_CO r ");
			sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
			sb.append(" WHERE r.CM_num = ? ");
			sb.append(" ORDER BY r.CO_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, cm_num);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReplyDTO dto = new ReplyDTO();

				dto.setCo_num(rs.getInt("CO_num"));
				dto.setMb_num(rs.getInt("MB_num"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setNickName(rs.getString("nickName"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 댓글 삭제
	public void deleteReply(int co_num, long mb_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM lesson_CO WHERE CO_num = ? AND MB_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, co_num);
			pstmt.setLong(2, mb_num);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 댓글 수정
	// 댓글 수정
	public void updateReply(ReplyDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE lesson_CO SET content = ? WHERE CO_num = ? AND MB_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getContent());
			pstmt.setInt(2, dto.getCo_num());
			pstmt.setLong(3, dto.getMb_num());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 좋아요 추가
	public void insertLike(LessonLikeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO lesson_LK(MB_num, CM_num, dateTime) VALUES (?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getMb_num());
			pstmt.setLong(2, dto.getCm_num());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 좋아요 삭제
	public void deleteLike(LessonLikeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM lesson_LK WHERE MB_num = ? AND CM_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getMb_num());
			pstmt.setLong(2, dto.getCm_num());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 좋아요 개수
	public int countLikes(long cm_num) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int result = 0;

		try {
			sql = "SELECT COUNT(*) FROM lesson_LK WHERE CM_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, cm_num);

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

	// 사용자 좋아요 여부 확인
	public boolean getUserLike(long mb_num, long cm_num) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		boolean result = false;

		try {
			sql = "SELECT COUNT(*) FROM lesson_LK WHERE MB_num = ? AND CM_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, mb_num);
			pstmt.setLong(2, cm_num);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1) > 0;
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
}