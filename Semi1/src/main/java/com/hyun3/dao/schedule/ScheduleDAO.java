package com.hyun3.dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.schedule.ScheduleDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class ScheduleDAO {
	public Connection conn = DBConn.getConnection();

	public boolean insertSubject(ScheduleDTO dto) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean success = false;
		try {
			conn = DBConn.getConnection(); // DB 연결
			String sql = "INSERT INTO at_subject (at_num, grade_year, semester, dt_sub_num, mb_num) "
					+ "VALUES (seq_at_subject.NEXTVAL, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getGradee());
			ps.setString(2, dto.getStGradee());
			ps.setString(3, dto.getDt_sb_num());
			ps.setLong(4, dto.getMb_Num());

			int result = ps.executeUpdate();
			if (result > 0) {
				success = true; // 성공
			} else {
				System.out.println("DB insert failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(ps);

		}
		return success;
	}

	// 모달 창 안에 수업 과목 리스트 
	public List<ScheduleDTO> viewSubject() {
		List<ScheduleDTO> list = new ArrayList<ScheduleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "    SELECT s.st_grade, s.sb_num, s.sb_name, d.dt_sub_num, d.hakscore, TO_CHAR(t.studytime, 'HH24:MI') AS studytime, t.studyday "
					+ "    FROM subject s " + "    JOIN dt_subject d ON s.sb_num = d.sb_num "
					+ "    JOIN timetable t ON d.dt_sub_num = t.dt_sub_num " + "	   ORDER BY s.st_grade";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ScheduleDTO dto = new ScheduleDTO();
				dto.setStGrade(rs.getInt("st_grade"));
				dto.setSbNum(rs.getString("sb_num"));
				dto.setDt_sb_num(rs.getString("dt_sub_num"));
				dto.setSbName(rs.getString("sb_name"));
				dto.setHakscore(rs.getInt("hakscore"));
				dto.setStudytime(rs.getString("studytime"));
				dto.setStudyDay(rs.getString("studyday"));

				list.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	public List<ScheduleDTO> getTimetable(String gradeYear, String semester, Long mb_num) {
		List<ScheduleDTO> list = new ArrayList<ScheduleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	        // SQL 쿼리 준비
	        sql = " SELECT a.grade_year, a.semester, a.dt_sub_num, a.mb_num, TO_CHAR(t.studytime, 'HH24:MI') AS studytime, t.studyday, t.studyday, sb_name "
	        		+ " FROM at_subject a "
	        		+ " JOIN timetable t ON a.dt_sub_num = t.dt_sub_num "
	        		+ " JOIN dt_subject d ON t.dt_sub_num = d.dt_sub_num "
	        		+ " JOIN subject s ON d.sb_num = s.sb_num "
	        		+ " WHERE a.grade_year = ? AND a.semester = ? AND a.mb_num = ? ";
	        
	        // PreparedStatement 객체 생성
	        pstmt = conn.prepareStatement(sql);
	        
	        // 파라미터 바인딩
	        pstmt.setString(1, gradeYear); // grade_year 값 설정
	        pstmt.setString(2, semester);  // semester 값 설정
	        pstmt.setLong(3, mb_num);    // mb_num 값 설정 (회원 번호)

	        // 쿼리 실행
	        rs = pstmt.executeQuery();
	        
	        // 결과 처리
	        while (rs.next()) {
	            ScheduleDTO dto = new ScheduleDTO();
	            dto.setGradee(rs.getString("grade_year"));
	            dto.setStGradee(rs.getString("semester"));
	            dto.setDt_sb_num(rs.getString("dt_sub_num"));  // 과목 번호도 추가로 설정
	            dto.setMb_Num(rs.getLong("mb_num"));      // 회원 번호도 설정
                dto.setStudytime(rs.getString("studytime"));
                dto.setStudyDay(rs.getString("studyday"));	            
                dto.setSbName(rs.getString("sb_name"));	            
	            
	            // 리스트에 DTO 추가
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // 자원 해제
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }
	    
	    // 결과 반환
	    return list;
	}
}