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
	
	public void insertLectureReview(LectureReviewDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();		
		
		try {
			sb.append(" INSERT INTO lectureReview(review_num, content, reg_date, rating, at_num) ");
			sb.append(" VALUES(seq_lecture_review.NEXTVAL, ?, SYSDATE, ?, ? ) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			// 파라미터 바인딩
			pstmt.setString(1, dto.getContent());
			pstmt.setInt(2, dto.getRating());
			pstmt.setLong(3, dto.getAt_Num());
						
			pstmt.executeUpdate();
				        			
		} catch (SQLException e) {
	        e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 수강목록 리스트
	public List<LectureReviewDTO> listLecture(String userId) {
		List<LectureReviewDTO> list = new ArrayList<LectureReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.sb_name, p.pf_name, at.at_num, lr.review_num ");
			sb.append(" FROM member m ");
			sb.append(" JOIN at_subject at on m.mb_num = at.mb_num ");
			sb.append(" LEFT OUTER JOIN lectureReview lr on at.at_num = lr.at_num ");
			sb.append(" JOIN dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" JOIN subject s on dt.sb_num = s.sb_num ");
			sb.append(" JOIN pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" JOIN professor p on ps.pf_num = p.pf_num ");
			sb.append(" WHERE m.userId = ? ");
			sb.append(" ORDER BY at.grade_year ASC, semester ASC ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureReviewDTO dto = new LectureReviewDTO();
				
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setPf_Name(rs.getString("pf_name"));
				dto.setAt_Num(rs.getLong("at_num"));
				dto.setReview_Num(rs.getLong("review_num"));
			
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
	
	// 수강번호로 과목명, 교수명 찾기
	public LectureReviewDTO findByAtNum(long atNum) throws SQLException {
		LectureReviewDTO dto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.sb_name, p.pf_name, at.at_num ");
			sb.append(" FROM at_subject at ");
			sb.append(" JOIN dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" JOIN subject s on dt.sb_num = s.sb_num ");
			sb.append(" JOIN pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" JOIN professor p on ps.pf_num = p.pf_num ");
			sb.append(" WHERE at.at_num = ? ");

	        pstmt = conn.prepareStatement(sb.toString());
			pstmt.setLong(1, atNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new LectureReviewDTO();
	            dto.setSb_Name(rs.getString("sb_name"));
	            dto.setPf_Name(rs.getString("pf_name"));
	            dto.setAt_Num(rs.getLong("at_num"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 강의평가 목록
	public List<LectureReviewDTO> listReview(int offset, int size) {
		List<LectureReviewDTO> list = new ArrayList<LectureReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT lr.review_num, at.at_num, s.sb_name, p.pf_name, lr.rating, ");
			sb.append("    m.nickName, lr.content from member m ");
			sb.append(" JOIN at_subject at on m.mb_num = at.mb_num ");
			sb.append(" JOIN lectureReview lr on at.at_num = lr.at_num ");
			sb.append(" JOIN dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" JOIN subject s on dt.sb_num = s.sb_num ");
			sb.append(" JOIN pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" JOIN professor p on ps.pf_num = p.pf_num ");
			sb.append(" ORDER BY lr.reg_date DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureReviewDTO dto = new LectureReviewDTO();
				
				dto.setReview_Num(rs.getLong("review_num"));
				dto.setAt_Num(rs.getLong("at_num"));
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
	
	// 해당 게시물 보기
	public LectureReviewDTO findByRvNum(long reviewNum) {
		LectureReviewDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT lr.review_num, s.sb_num, s.sb_name, p.pf_name, lr.rating, ");
			sb.append("   m.nickName, m.userId, lr.content ");
			sb.append(" FROM member m ");
			sb.append(" JOIN at_subject at on m.mb_num = at.mb_num ");
			sb.append(" JOIN lectureReview lr on at.at_num = lr.at_num ");
			sb.append(" JOIN dt_subject dt on at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" JOIN subject s on dt.sb_num = s.sb_num ");
			sb.append(" JOIN pf_sb ps on s.sb_num = ps.sb_num ");
			sb.append(" JOIN professor p on ps.pf_num = p.pf_num ");
			sb.append(" WHERE lr.review_num = ? ");
			
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, reviewNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new LectureReviewDTO();
				
				dto.setReview_Num(rs.getLong("review_num"));
				dto.setSb_Num(rs.getLong("sb_num"));
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setPf_Name(rs.getString("pf_name"));
				dto.setRating(rs.getInt("rating"));
				dto.setNickName(rs.getString("nickName"));
				dto.setUserId(rs.getString("userId"));
				dto.setContent(rs.getString("content"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public int reviewCount(long atNum) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT count(*) cnt FROM lectureReview lr "
				+ " JOIN at_subject at ON lr.at_num = at.at_num "
				+ " WHERE at.at_num= ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, atNum);
			
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
	
	public LectureReviewDTO findBySbNum(long sbNum) {
	    LectureReviewDTO dto = null; // 결과를 담을 DTO
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    StringBuilder sb = new StringBuilder();

	    try {
	        // SQL 쿼리 작성
	        sb.append(" SELECT s.sb_num, s.sb_name, p.pf_name ");
	        sb.append(" FROM subject s ");
	        sb.append(" JOIN pf_sb ps ON s.sb_num = ps.sb_num "); // 교수와 과목 연결
	        sb.append(" JOIN professor p ON ps.pf_num = p.pf_num "); // 교수 정보 조회
	        sb.append(" WHERE s.sb_num = ? "); // 과목 번호 조건

	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setLong(1, sbNum); // sbNum 값 설정

	        // 쿼리 실행
	        rs = pstmt.executeQuery();

	        // 결과 처리
	        if (rs.next()) {
	            dto = new LectureReviewDTO();
	            dto.setSb_Num(rs.getLong("sb_num"));   // 과목 번호
	            dto.setSb_Name(rs.getString("sb_name")); // 과목명
	            dto.setPf_Name(rs.getString("pf_name")); // 교수명
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // 리소스 정리
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return dto; // 조회된 DTO 반환
	}

	
	// 강의평 수정
	public void updateReview(LectureReviewDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE lectureReview SET content=?, rating=? "
					+ " WHERE review_num=? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
}
