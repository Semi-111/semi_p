package com.hyun3.dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.util.DBUtil;
import com.hyun3.domain.schedule.ScheduleDTO;
import com.hyun3.util.DBConn;

public class ScheduleDAO {
	public Connection conn = DBConn.getConnection();
	
	public List<ScheduleDTO> viewSubject() {
		List<ScheduleDTO> list = new ArrayList<ScheduleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
	
	try {
		sql = "    SELECT s.st_grade, s.sb_num, s.sb_name, d.hakscore, TO_CHAR(t.studytime, 'HH24:MI') AS studytime "
				+ "    FROM subject s "
				+ "    JOIN dt_subject d ON s.sb_num = d.sb_num "
				+ "    LEFT JOIN timetable t ON d.dt_sub_num = t.dt_sub_num ";
	
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			ScheduleDTO dto = new ScheduleDTO();
			dto.setStGrade(rs.getString("st_grade"));
			dto.setSbNum(rs.getString("sb_num"));
			dto.setSbName(rs.getString("sb_name"));
			dto.setHakscore(rs.getInt("hakscore"));
			dto.setStudytime(rs.getString("studytime"));
			
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
	
}
