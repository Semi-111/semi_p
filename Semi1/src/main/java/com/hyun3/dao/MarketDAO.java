package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.sw.MarketDTO;
import com.hyun3.util.DBUtil;

public class MarketDAO {
	public Connection conn = com.hyun3.util.DBConn.getConnection();

	public void insertMarket(com.hyun3.domain.sw.MarketDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO MARKETPLACE(marketNum, title, content, CA_date, filename, MB_num, CT_num) "
					+ " VALUES(SEQ_MARKETPLACE.NEXTVAL, ?, ?, SYSDATE, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getFileName());
			pstmt.setLong(4, dto.getMb_num());
			pstmt.setInt(5, dto.getCt_num());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			com.hyun3.util.DBUtil.close(pstmt);
		}
	}

	// 검색에서 사용하는 listBoard 메서드
	public List<MarketDTO> listBoard(int offset, int size, String schType, String kwd) throws SQLException {
		List<MarketDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT m.marketNum, m.title, m.content, m.CA_date, "
					+ " m.fileName, m.MB_num, m.CT_num, mb.nickName " + " FROM marketplace m "
					+ " JOIN Member mb ON m.MB_num = mb.MB_num ";
			if (schType.equals("all")) {
				sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("title")) {
				sql += " WHERE INSTR(title, ?) >= 1 ";
			} else {
				sql += " WHERE INSTR(content, ?) >= 1 ";
			}
			sql += " ORDER BY marketNum DESC " + " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";

			pstmt = conn.prepareStatement(sql);

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MarketDTO dto = new MarketDTO();

				dto.setMarketNum(rs.getInt("marketNum"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setCa_date(rs.getString("CA_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setMb_num(rs.getInt("MB_num"));
				dto.setCt_num(rs.getInt("CT_num"));
				dto.setNickName(rs.getString("nickName"));

				list.add(dto);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public List<MarketDTO> listBoard(int offset, int size) throws SQLException {
		List<MarketDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT marketNum, title, content, CA_date, ");
			sb.append("    filename, mb.nickName, ");
			sb.append("    ROW_NUMBER() OVER(ORDER BY marketNum DESC) rankNum ");
			sb.append(" FROM marketplace m ");
			sb.append(" JOIN member mb ON m.MB_num = mb.MB_num ");
			sb.append(" ORDER BY marketNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MarketDTO dto = new MarketDTO();

				dto.setMarketNum(rs.getInt("marketNum"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setCa_date(rs.getString("CA_date"));
				dto.setFileName(rs.getString("filename"));
				dto.setNickName(rs.getString("nickName"));

				list.add(dto);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 전체 데이터 개수
	public int dataCount(String schType, String ksd) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM marketplace";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
		}

		return result;
	}

	// 검색X
	public int dataCount() throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM marketplace";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

}