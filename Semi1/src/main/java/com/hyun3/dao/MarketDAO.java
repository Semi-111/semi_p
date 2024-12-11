package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.sw.MarketDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;


public class MarketDAO {
	public Connection conn = DBConn.getConnection();

	
	public void insertMarket(MarketDTO dto) throws SQLException {
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

	// 검색이 없는 경우 리스트
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
	public int dataCount(String schType, String kwd) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) cnt " + " FROM marketplace m " + " JOIN member mb ON m.MB_num = mb.MB_num ";

			if (schType.equals("all")) {
				sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else {
				sql += " WHERE INSTR(" + schType + ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
			} else {
				pstmt.setString(1, kwd);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
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

	// 조회수 증가
	public void updateHitCount(long marketNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE marketplace SET views = views + 1 WHERE marketNum = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, marketNum);
			pstmt.executeUpdate();

		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 게시글 가져오기 - 게시글 번호로 조회 
	public MarketDTO findById(long marketNum) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MarketDTO dto = null;
		String sql;

		try {
			sql = "SELECT m.marketNum, m.title, m.content, m.views, "
					+ " m.CA_date, m.fileName, m.MB_num, m.CT_num, mb.nickName " + " FROM marketplace m "
					+ " JOIN member mb ON m.MB_num = mb.MB_num " + " WHERE marketNum = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, marketNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MarketDTO();

				dto.setMarketNum(rs.getInt("marketNum"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setViews(rs.getInt("views"));
				dto.setCa_date(rs.getString("ca_date"));
				dto.setFileName(rs.getString("fileName"));
				dto.setMb_num(rs.getInt("mb_num"));
				dto.setCt_num(rs.getInt("ct_num"));
				dto.setNickName(rs.getString("nickName"));
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 이전글
	public MarketDTO findByPrev(long marketNum, String schType, String kwd) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MarketDTO dto = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append("SELECT marketNum, title ");
				sb.append(" FROM marketplace m ");
				sb.append(" JOIN member mb ON m.MB_num = mb.MB_num ");
				sb.append(" WHERE marketNum > ? ");
				if (schType.equals("all")) {
					sb.append(" AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else {
					sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY marketNum ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, marketNum);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append("SELECT marketNum, title FROM marketplace ");
				sb.append(" WHERE marketNum > ? ");
				sb.append(" ORDER BY marketNum ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setLong(1, marketNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MarketDTO();
				dto.setMarketNum(rs.getInt("marketNum"));
				dto.setTitle(rs.getString("title"));
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public MarketDTO findByNext(long marketNum, String schType, String kwd) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MarketDTO dto = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append("SELECT marketNum, title ");
				sb.append(" FROM marketplace m ");
				sb.append(" JOIN member mb ON m.MB_num = mb.MB_num ");
				sb.append(" WHERE marketNum < ? ");
				if (schType.equals("all")) {
					sb.append(" AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else {
					sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY marketNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, marketNum);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append("SELECT marketNum, title FROM marketplace ");
				sb.append(" WHERE marketNum < ? ");
				sb.append(" ORDER BY marketNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setLong(1, marketNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MarketDTO();
				dto.setMarketNum(rs.getInt("marketNum"));
				dto.setTitle(rs.getString("title"));
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	// 게시글 삭제
	public void deleteMarket(long marketNum) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	        sql = "DELETE FROM marketplace WHERE marketNum = ?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setLong(1, marketNum);
	        pstmt.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}
	
	// 좋아요 여부 확인
	public boolean isLikedMarket(long marketNum, long mb_num) throws SQLException {
	    boolean result = false;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT marketNum FROM market_LK WHERE marketNum = ? AND MB_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setLong(1, marketNum);
	        pstmt.setLong(2, mb_num);
	        
	        rs = pstmt.executeQuery();
	        result = rs.next();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return result;
	}

	// 좋아요 추가
	public void insertLike(long marketNum, long memberNum) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;
	    
	    try {
	        sql = "INSERT INTO market_LK(marketNum, MB_num, dateTime) VALUES (?, ?, SYSDATE)";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setLong(1, marketNum);
	        pstmt.setLong(2, memberNum);
	        
	        pstmt.executeUpdate();
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}

	// 좋아요 삭제
	public void deleteLike(long marketNum, long memberNum) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;
	    
	    try {
	        sql = "DELETE FROM market_LK WHERE marketNum = ? AND MB_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setLong(1, marketNum);
	        pstmt.setLong(2, memberNum);
	        
	        pstmt.executeUpdate();
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}

	// 게시글의 좋아요 개수
	public int countLikes(long marketNum) throws SQLException {
	    int result = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    
	    try {
	        sql = "SELECT COUNT(*) FROM market_LK WHERE marketNum = ?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setLong(1, marketNum);
	        
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	            result = rs.getInt(1);
	        }
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }
	    
	    return result;
	}

	// 글수정
	public void updateMarket(MarketDTO dto) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	        sql = "UPDATE marketplace SET title=?, content=?, filename=?, "
	            + " CT_num=? "
	            + " WHERE marketNum=?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, dto.getTitle());
	        pstmt.setString(2, dto.getContent());
	        pstmt.setString(3, dto.getFileName());
	        pstmt.setInt(4, dto.getCt_num());
	        pstmt.setInt(5, dto.getMarketNum());
	        
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}
	
	// 카테고리별 데이터 개수
	public int dataCount(int category) throws SQLException {
	    int result = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT COUNT(*) FROM marketplace WHERE CT_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setInt(1, category);

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

	// 카테고리별 게시글 리스트
	public List<MarketDTO> listBoard(int offset, int size, int category) throws SQLException {
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
	        sb.append(" WHERE CT_num = ? ");
	        sb.append(" ORDER BY marketNum DESC ");
	        sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setInt(1, category);
	        pstmt.setInt(2, offset);
	        pstmt.setInt(3, size);

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
}
