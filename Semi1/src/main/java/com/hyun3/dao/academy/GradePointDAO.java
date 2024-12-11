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
	
	public List<GradePointDTO> findByMemberId(long mb_Num) {
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
			sb.append(" WHERE m.mb_Num=? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, mb_Num);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GradePointDTO dto = new GradePointDTO();
				dto.setSb_Num(rs.getLong("sb_Num"));
				dto.setSb_Name(rs.getString("sb_Name"));
				dto.setHakscore(rs.getInt("hakscore"));
	            dto.setGrade(rs.getString("grade"));
	            dto.setGrade_year(rs.getString("grade_year"));
	            dto.setSemester(rs.getString("semester"));
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
	
	public Map<String, Double> calculateGrade(List<GradePointDTO> grades) {
	    double totalPoints = 0.0;
	    int totalCredits = 0;

	    for (GradePointDTO dto : grades) {
	        double gradePoint = convertGradeToPoint(dto.getGrade());
	        totalPoints += gradePoint * dto.getHakscore();
	        totalCredits += dto.getHakscore();
	    }

	    double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;

	    Map<String, Double> stats = new HashMap<>();
	    stats.put("GPA", gpa);
	    stats.put("TotalCredits", (double) totalCredits);
	    return stats;
	}
	
	
}
