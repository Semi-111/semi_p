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
	
	public List<ScheduleDTO> viewSubject() {
		List<ScheduleDTO> list = new ArrayList<ScheduleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
	
	try {
		sql = "    SELECT s.st_grade, s.sb_num, s.sb_name, d.hakscore, TO_CHAR(t.studytime, 'HH24:MI') AS studytime, t.studyday "
				+ "    FROM subject s "
				+ "    JOIN dt_subject d ON s.sb_num = d.sb_num "
				+ "    LEFT JOIN timetable t ON d.dt_sub_num = t.dt_sub_num "
				+ "	   ORDER BY s.st_grade";
	
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			ScheduleDTO dto = new ScheduleDTO();
			dto.setStGrade(rs.getInt("st_grade"));
			dto.setSbNum(rs.getString("sb_num"));
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
	
	public boolean insertSubject(ScheduleDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;
        try {
            conn = DBConn.getConnection(); // DB 연결
            String sql = "INSERT INTO at_subject (at_num, grade_year, semester, dt_sb_num, mb_num) "
                       + "VALUES (seq_at_subject.NEXTVAL, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getGradee());
            ps.setString(2, dto.getStGradee());
            ps.setString(3, dto.getSbNum());
            ps.setString(4, dto.getMemberId());

            int result = ps.executeUpdate();
            if (result > 0) {
                success = true; // 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            DBUtil.close(ps);
            
        }
        return success;
    	}
	}

	
	
