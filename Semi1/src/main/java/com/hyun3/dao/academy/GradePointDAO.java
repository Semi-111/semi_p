package com.hyun3.dao.academy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyun3.domain.academy.GradePointDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class GradePointDAO {
	private Connection conn = DBConn.getConnection();
	
	
	public List<GradePointDTO> findById(String userId) {
		List<GradePointDTO> list = new ArrayList<GradePointDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.sb_Num, s.sb_Name, ");
			sb.append(" d.hakscore,  ");
			sb.append(" grade, grade_year, a.semester, ");
			sb.append(" m.mb_Num, m.userId ");
			sb.append(" FROM subject s ");
			sb.append(" JOIN dt_subject d ON s.sb_Num = d.sb_Num ");
			sb.append(" JOIN at_subject a ON d.dt_sub_Num = a.dt_sub_Num ");
			sb.append(" JOIN member m ON a.mb_Num = m.mb_Num ");
			sb.append(" WHERE m.userId=? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GradePointDTO dto = new GradePointDTO();
				dto.setSb_Num(rs.getLong("sb_Num"));
				dto.setSb_Name(rs.getString("sb_Name"));
				dto.setHakscore(rs.getInt("hakscore"));
	            dto.setGrade(rs.getString("grade"));
	            dto.setGrade_year(rs.getInt("grade_year"));
	            dto.setSemester(rs.getInt("semester"));
	            dto.setMb_Num(rs.getLong("mb_Num"));
	            dto.setUserId(rs.getString("userId"));
	            
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
	
	
	public List<GradePointDTO> findById(String userId, int gradeYear, int semester) {
		List<GradePointDTO> list = new ArrayList<GradePointDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.sb_Num, s.sb_Name, ");
			sb.append(" d.hakscore,  ");
			sb.append(" grade, grade_year, a.semester, ");
			sb.append(" m.mb_Num, m.userId ");
			sb.append(" FROM subject s ");
			sb.append(" JOIN dt_subject d ON s.sb_Num = d.sb_Num ");
			sb.append(" JOIN at_subject a ON d.dt_sub_Num = a.dt_sub_Num ");
			sb.append(" JOIN member m ON a.mb_Num = m.mb_Num ");
			sb.append(" WHERE m.userId=? AND grade_year = ? AND a.semester = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			pstmt.setInt(2, gradeYear);
			pstmt.setInt(3, semester);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GradePointDTO dto = new GradePointDTO();
				dto.setSb_Num(rs.getLong("sb_Num"));
				dto.setSb_Name(rs.getString("sb_Name"));
				dto.setHakscore(rs.getInt("hakscore"));
	            dto.setGrade(rs.getString("grade"));
	            dto.setGrade_year(rs.getInt("grade_year"));
	            dto.setSemester(rs.getInt("semester"));
	            dto.setMb_Num(rs.getLong("mb_Num"));
	            dto.setUserId(rs.getString("userId"));
	            
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
	
	public Map<String, Integer> getGradeDistribution(String userId) {
	    Map<String, Integer> gradeDistribution = new HashMap<>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        StringBuilder sb = new StringBuilder();
	        sb.append("SELECT grade, COUNT(*) AS gradeCount "); // 'gradeCount'라는 별칭 사용
	        sb.append("FROM subject s ");
	        sb.append("JOIN dt_subject d ON s.sb_Num = d.sb_Num ");
	        sb.append("JOIN at_subject a ON d.dt_sub_Num = a.dt_sub_Num ");
	        sb.append("JOIN member m ON a.mb_Num = m.mb_Num ");
	        sb.append("WHERE m.userId = ? ");
	        sb.append("GROUP BY grade");

	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setString(1, userId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	String grade = rs.getString("grade"); // 성적(A+, B+, ...) 데이터
	            int count = rs.getInt("gradeCount"); // 각 성적의 개수
	            gradeDistribution.put(grade, count); // 성적별 데이터를 맵에 추가
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return gradeDistribution;
	}

		
	
	public double convertGradeToPoint(String grade) {
		switch(grade) {
		case "A+": return 4.5;
        case "A0": return 4.0;
        case "B+": return 3.5;
        case "B0": return 3.0;
        case "C+": return 2.5;
        case "C0": return 2.0;
        case "D+": return 1.5;
        case "D0": return 1.0;
        case "F": return 0.0;
        default: return 0.0;
		}
	}


}
