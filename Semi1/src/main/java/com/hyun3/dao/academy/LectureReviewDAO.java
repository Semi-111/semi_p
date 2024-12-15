package com.hyun3.dao.academy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.academy.LectureReviewDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class LectureReviewDAO {
	public Connection conn = DBConn.getConnection();
	
	public void insertLectureReview(LectureReviewDTO dto, String userId, int sbNum) throws SQLException{
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" INSERT INTO lectureReview(review_num, content, reg_date, rating, at_num) ");
			sb.append(" VALUES(seq_lecture_review.NEXTVAL, ?, SYSDATE, ?, ");
			sb.append("         (SELECT at.at_num FROM at_subject at ");
			sb.append("          JOIN member m ON at.mb_num = m.mb_num");
			sb.append("          JOIN dt_subject dt ON at.dt_sub_num = dt.dt_sub_num ");
			sb.append("          JOIN subject s ON dt.sb_num = s.sb_num ");
			sb.append("          WHERE m.userId = ? AND s.sb_num = ?)) ");
			
			conn.setAutoCommit(false); // 트랜잭션 시작
			
			pstmt = conn.prepareStatement(sb.toString());
			
			// 파라미터 바인딩
			pstmt.setString(1, dto.getContent());
			pstmt.setInt(2, dto.getRating());
			pstmt.setString(3, userId);
			pstmt.setInt(4, sbNum);
			
	        // 커밋
	        conn.commit();
			
		} catch (SQLException e) {
			// 오류 발생 시 롤백
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
	public List<LectureReviewDTO> listLecture(String userId) {
		List<LectureReviewDTO> list = new ArrayList<LectureReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" select s.sb_num, s.sb_name, p.pf_name ");
			sb.append(" from member m ");
			sb.append(" join at_subject at on m.mb_num = at.mb_num ");
			sb.append(" join dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" join subject s on dt.sb_num = s.sb_num ");
			sb.append(" join pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" join professor p on ps.pf_num = p.pf_num ");
			sb.append(" where m.userId = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureReviewDTO dto = new LectureReviewDTO();
				
				dto.setSb_Num(rs.getLong("sb_num"));
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setPf_Name(rs.getString("pf_name"));
			
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
	
	// 과목번호로 과목명 찾기
	public String findSbNameById(long sbNum) throws SQLException {
		String sbName = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT sb_name FROM subject WHERE sb_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sbNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				sbName = rs.getString("sb_name");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return sbName;
	}
	
	public List<LectureReviewDTO> listReview(int offset, int size) {
		List<LectureReviewDTO> list = new ArrayList<LectureReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" select lr.review_num, s.sb_name, p.pf_name, lr.rating, m.nickName, lr.content ");
			sb.append(" from member m ");
			sb.append(" join at_subject at on m.mb_num = at.mb_num ");
			sb.append(" join lectureReview lr on at.at_num = lr.at_num ");
			sb.append(" join dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" join subject s on dt.sb_num = s.sb_num ");
			sb.append(" join pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" join professor p on ps.pf_num = p.pf_num ");
			sb.append(" ORDER BY lr.review_num ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureReviewDTO dto = new LectureReviewDTO();
				
				dto.setReview_Num(rs.getLong("review_num"));				
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setPf_Name(rs.getString("pf_name"));
				dto.setRating(rs.getInt("rating"));
				dto.setNickName(rs.getString("nickName"));
				dto.setContent(rs.getString("content"));
			
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
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT COUNT(*) cnt FROM lectureReview ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
				
		return result;
	}
	
}
