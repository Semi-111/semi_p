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

public class BoardRankDAO {
	public Connection conn = DBConn.getConnection();
	
	// 최근 강의평
	public List<LectureReviewDTO> recentReview(int offset, int size) {
		List<LectureReviewDTO> reviewList = new ArrayList<LectureReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT lr.rating, s.sb_name, p.pf_name, lr.content ");
			sb.append(" FROM lectureReview lr ");
			sb.append(" JOIN at_subject at ON lr.at_num = at.at_num ");
			sb.append(" JOIN member m ON at.mb_num = m.mb_num ");
			sb.append(" JOIN dt_subject dt ON at.dt_sub_num = dt.dt_sub_num ");
			sb.append(" JOIN subject s ON dt.sb_num = s.sb_num ");
			sb.append(" JOIN pf_sb ps ON s.sb_num = ps.sb_num ");
			sb.append(" JOIN professor p ON ps.pf_num = p.pf_num ");
			sb.append(" WHERE m.block = 0 ");
			sb.append(" ORDER BY lr.reg_date DESC; ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureReviewDTO dto = new LectureReviewDTO();
				
				dto.setRating(rs.getInt("rating"));
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setPf_Name(rs.getString("pf_name"));
				dto.setContent(rs.getString("content"));
				
				reviewList.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return reviewList;
	}
	
}
