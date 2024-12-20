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
	
	
	// 총 취득 학점 계산
	public int totalHakscore(String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb = sb.append(" SELECT sum(dt.hakscore) totalhackscore ");
			sb = sb.append(" FROM dt_subject dt ");
			sb = sb.append(" JOIN at_subject at ON dt.dt_sub_num = at.dt_sub_num ");
			sb = sb.append(" JOIN member m ON at.mb_num = m.mb_num ");
			sb = sb.append(" WHERE m.userId = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getObject("totalhackscore") != null ? rs.getInt("totalhackscore") : 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
				
		return result;
	}
	
	
	// 전체 평점
	public double calculateTotalGPA(String userId) {
	    double totalPoints = 0.0;
	    int totalHakscore = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    StringBuilder sb = new StringBuilder();

	    try {
	        sb.append(" SELECT at.grade, dt.hakscore ");
	        sb.append(" FROM dt_subject dt ");
	        sb.append(" JOIN at_subject at ON dt.dt_sub_num = at.dt_sub_num ");
	        sb.append(" JOIN member m ON at.mb_num = m.mb_num ");
	        sb.append(" WHERE m.userId = ? ");

	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setString(1, userId);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String grade = rs.getString("grade");
	            int hakscore = rs.getInt("hakscore");

	            // 학점과 평점을 이용해 총 평점 계산
	            totalPoints += convertGradeToPoint(grade) * hakscore;
	            totalHakscore += hakscore;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    // 총 평점을 계산
	    return totalHakscore > 0 ? totalPoints / totalHakscore : 0.0;
	}
	
	
	public double convertGradeToPoint(String grade) {
		if (grade == null || grade.trim().isEmpty()) {
	        return 0.0; // null이나 공백일 때 0.0 반환
	    }
		
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
	
	
	// 학년 학기별 점수
	public double semesterPoints(String userId) {
	    double semesterPoints = 0.0;

	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        StringBuilder sb = new StringBuilder();
	        sb.append("SELECT dt.hakscore, at.grade ");
	        sb.append("FROM dt_subject dt ");
	        sb.append("JOIN at_subject at ON dt.dt_sub_num = at.dt_sub_num ");
	        sb.append("JOIN member m ON at.mb_num = m.mb_num ");
	        sb.append("WHERE m.userId = ? AND at.grade_year = ? AND at.semester = ?");

	        pstmt = conn.prepareStatement(sb.toString());
	        
	        GradePointDTO dto = new GradePointDTO();
	        pstmt.setString(1, userId);
	        pstmt.setInt(2, dto.getGrade_year());
	        pstmt.setInt(3, dto.getSemester());

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int hakscore = rs.getInt("hakscore");
	            String grade = rs.getString("grade");

	            double gradePoint = convertGradeToPoint(grade);
	            semesterPoints += gradePoint * hakscore; // 성적 * 학점 누적
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return semesterPoints;
	}
	
	
	// 학기별 취득 학점
	public int semesterCredits(String userId) {
	    int semesterCredits = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        StringBuilder sb = new StringBuilder();
	        sb.append("SELECT dt.hakscore ");
	        sb.append("FROM dt_subject dt ");
	        sb.append("JOIN at_subject at ON dt.dt_sub_num = at.dt_sub_num ");
	        sb.append("JOIN member m ON at.mb_num = m.mb_num ");
	        sb.append("WHERE m.userId = ? AND at.grade_year = ? AND at.semester = ? ");

	        pstmt = conn.prepareStatement(sb.toString());
	        
	        GradePointDTO dto = new GradePointDTO();
	        pstmt.setString(1, userId);
	        pstmt.setInt(2, dto.getGrade_year());
	        pstmt.setInt(3, dto.getSemester());
	        
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	semesterCredits += rs.getInt("hakscore"); // 학점 누적
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return semesterCredits;
	}
	
	
	// grade(성적) update
	public void updateGrade(GradePointDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE at_subject SET grade=? "
					+ " WHERE at_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getGrade());
			pstmt.setLong(2, dto.getAt_Num());
						
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}		
	}
	


	
	public List<GradePointDTO> findById(String userId) {
		List<GradePointDTO> list = new ArrayList<GradePointDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT at.at_num ,s.sb_num, s.sb_name, ");
			sb.append(" dt.hakscore,  ");
			sb.append(" grade, grade_year, at.semester, ");
			sb.append(" m.mb_num, m.userId ");
			sb.append(" FROM subject s ");
			sb.append(" JOIN dt_subject dt ON s.sb_Num = dt.sb_Num ");
			sb.append(" JOIN at_subject at ON dt.dt_sub_Num = at.dt_sub_Num ");
			sb.append(" JOIN member m ON at.mb_Num = m.mb_Num ");
			sb.append(" WHERE m.userId=? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GradePointDTO dto = new GradePointDTO();
				dto.setAt_Num(rs.getLong("at_num"));
				dto.setSb_Num(rs.getLong("sb_num"));
				dto.setSb_Name(rs.getString("sb_name"));
				dto.setHakscore(rs.getInt("hakscore"));
	            dto.setGrade(rs.getString("grade"));
	            dto.setGrade_year(rs.getInt("grade_year"));
	            dto.setSemester(rs.getInt("semester"));
	            dto.setMb_Num(rs.getLong("mb_num"));
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
			sb.append(" SELECT at.at_Num, s.sb_Name, dt.hakscore, at.grade, ");
			sb.append(" at.grade_year, at.semester, m.userId ");
			sb.append(" FROM subject s ");
			sb.append(" JOIN dt_subject dt ON s.sb_Num = dt.sb_Num ");
			sb.append(" JOIN at_subject at ON dt.dt_sub_Num = at.dt_sub_Num ");
			sb.append(" JOIN member m ON at.mb_Num = m.mb_Num ");
			sb.append(" WHERE m.userId=? AND at.grade_year = ? AND at.semester = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			pstmt.setInt(2, gradeYear);
			pstmt.setInt(3, semester);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GradePointDTO dto = new GradePointDTO();
				dto.setAt_Num(rs.getLong("at_Num"));
				dto.setSb_Name(rs.getString("sb_Name"));
				dto.setHakscore(rs.getInt("hakscore"));
				dto.setGrade(rs.getString("grade"));
	            
	            dto.setGrade_year(rs.getInt("grade_year"));
	            dto.setSemester(rs.getInt("semester"));	            
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
	        sb.append("SELECT at.grade, COUNT(*) AS gradeCount ");
	        sb.append("FROM at_subject at ");
	        sb.append("JOIN member m ON at.mb_Num = m.mb_Num ");
	        sb.append("WHERE m.userId = ? ");
	        sb.append("GROUP BY at.grade");

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


	
}
