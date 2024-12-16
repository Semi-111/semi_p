package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.MyCalendarDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class MyCalendarDAO {
	private Connection conn = DBConn.getConnection();

	public void insertMyCalendar(MyCalendarDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO MyCalendar(num, memberIdx, subject, color, sday, eday, "
					+ " stime, etime, repeat, repeat_cycle, memo, reg_date) "
					+ " VALUES(MyCalendar_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getMemberIdx());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getColor());
			pstmt.setString(4, dto.getSday());
			pstmt.setString(5, dto.getEday());
			pstmt.setString(6, dto.getStime());
			pstmt.setString(7, dto.getEtime());
			pstmt.setInt(8, dto.getRepeat());
			pstmt.setInt(9, dto.getRepeat_cycle());
			pstmt.setString(10, dto.getMemo());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	public List<MyCalendarDTO> listMonth(String startDay, String endDay, long memberIdx) {
		List<MyCalendarDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT num, subject, sday, eday, stime, etime, ");
			sb.append("               color, repeat, repeat_cycle ");
			sb.append(" FROM MyCalendar");
			sb.append(" WHERE memberIdx = ? AND ");
			sb.append(" ( ");
			sb.append("      ( ");
			sb.append("         ( TO_DATE(sday, 'YYYYMMDD') >= TO_DATE(?, 'YYYYMMDD') ");
			sb.append("             AND TO_DATE(sday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("          )  OR ( TO_DATE(eday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("             AND TO_DATE(eday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("          )");
			sb.append("       ) OR ("); // 반복일정
			sb.append("         repeat = 1 AND repeat_cycle != 0 ");
			sb.append(
					"            AND TO_CHAR(ADD_MONTHS(sday, 12 * repeat_cycle * TRUNC(((SUBSTR(?,1,4) - SUBSTR(sday,1,4)) / repeat_cycle))), 'YYYYMMDD') >= ? ");
			sb.append(
					"            AND TO_CHAR(ADD_MONTHS(sday, 12 * repeat_cycle * TRUNC(((SUBSTR(?,1,4) - SUBSTR(sday,1,4)) / repeat_cycle))), 'YYYYMMDD') <= ? ");
			sb.append("       )");
			sb.append(" ) ");
			sb.append(" ORDER BY sday ASC, num DESC ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, memberIdx);
			pstmt.setString(2, startDay);
			pstmt.setString(3, endDay);
			pstmt.setString(4, startDay);
			pstmt.setString(5, endDay);

			pstmt.setString(6, startDay);
			pstmt.setString(7, startDay);
			pstmt.setString(8, startDay);
			pstmt.setString(9, endDay);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MyCalendarDTO dto = new MyCalendarDTO();

				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				dto.setEday(rs.getString("eday"));
				dto.setStime(rs.getString("stime"));
				dto.setEtime(rs.getString("etime"));
				dto.setColor(rs.getString("color"));
				dto.setRepeat(rs.getInt("repeat"));
				dto.setRepeat_cycle(rs.getInt("repeat_cycle"));

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

	public List<MyCalendarDTO> listDay(String date, long memberIdx) {
		List<MyCalendarDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT num, subject, sday, eday, color,");
			sb.append("               TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM MyCalendar");
			sb.append(" WHERE memberIdx = ? AND ");
			sb.append(" ( ");
			sb.append("      ( ");
			sb.append("         TO_DATE(sday, 'YYYYMMDD') = TO_DATE(?, 'YYYYMMDD') ");
			sb.append(
					"         OR (eday IS NOT NULL AND TO_DATE(sday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD') AND TO_DATE(eday, 'YYYYMMDD') >= TO_DATE(?, 'YYYYMMDD')) ");
			sb.append("      ) OR ( "); // 반복일정
			sb.append(
					"           repeat=1 AND MOD(MONTHS_BETWEEN(TO_DATE(sday, 'YYYYMMDD'), TO_DATE(?, 'YYYYMMDD'))/12, repeat_cycle) = 0  ");
			sb.append("      ) ");
			sb.append(" ) ");
			sb.append(" ORDER BY num DESC ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, memberIdx);
			pstmt.setString(2, date);
			pstmt.setString(3, date);
			pstmt.setString(4, date);

			pstmt.setString(5, date);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MyCalendarDTO dto = new MyCalendarDTO();

				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				dto.setEday(rs.getString("eday"));
				dto.setColor(rs.getString("color"));
				dto.setReg_date(rs.getString("reg_date"));

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

	public MyCalendarDTO readMyCalendar(long num) {
		MyCalendarDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		String period, s;

		try {
			sql = "SELECT num, subject, sday, eday, stime, etime, "
					+ "      color, repeat, repeat_cycle, memo, reg_date " + "  FROM MyCalendar" + "  WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MyCalendarDTO();

				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				s = dto.getSday().substring(0, 4) + "-" + dto.getSday().substring(4, 6) + "-"
						+ dto.getSday().substring(6);
				dto.setSday(s);
				dto.setEday(rs.getString("eday"));
				if (dto.getEday() != null && dto.getEday().length() == 8) {
					s = dto.getEday().substring(0, 4) + "-" + dto.getEday().substring(4, 6) + "-"
							+ dto.getEday().substring(6);
					dto.setEday(s);
				}
				dto.setStime(rs.getString("stime"));
				if (dto.getStime() != null && dto.getStime().length() == 4) {
					s = dto.getStime().substring(0, 2) + ":" + dto.getStime().substring(2);
					dto.setStime(s);
				}
				dto.setEtime(rs.getString("etime"));
				if (dto.getEtime() != null && dto.getEtime().length() == 4) {
					s = dto.getEtime().substring(0, 2) + ":" + dto.getEtime().substring(2);
					dto.setEtime(s);
				}

				period = dto.getSday();
				if (dto.getStime() != null && dto.getStime().length() != 0) {
					period += " " + dto.getStime();
				}
				if (dto.getEday() != null && dto.getEday().length() != 0) {
					period += " ~ " + dto.getEday();
				}
				if (dto.getEtime() != null && dto.getEtime().length() != 0) {
					period += " " + dto.getEtime();
				}
				dto.setPeriod(period);

				dto.setColor(rs.getString("color"));
				dto.setRepeat(rs.getInt("repeat"));
				dto.setRepeat_cycle(rs.getInt("repeat_cycle"));
				dto.setMemo(rs.getString("memo"));
				dto.setReg_date(rs.getString("reg_date"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	public void updateMyCalendar(MyCalendarDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("UPDATE MyCalendar SET ");
			sb.append("  subject=?, color=?, sday=?, eday=?, stime=?, ");
			sb.append("  etime=?, repeat=?, repeat_cycle=?, memo=? ");
			sb.append("  WHERE num=? AND memberIdx=?");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getColor());
			pstmt.setString(3, dto.getSday());
			pstmt.setString(4, dto.getEday());
			pstmt.setString(5, dto.getStime());
			pstmt.setString(6, dto.getEtime());
			pstmt.setInt(7, dto.getRepeat());
			pstmt.setInt(8, dto.getRepeat_cycle());
			pstmt.setString(9, dto.getMemo());
			pstmt.setLong(10, dto.getNum());
			pstmt.setLong(11, dto.getMemberIdx());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

	}

	public void deleteMyCalendar(long num, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM MyCalendar WHERE num=? AND memberIdx=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);
			pstmt.setLong(2, memberIdx);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

	}
}
