package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.LessonDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class LessonDAO {
    public Connection conn = DBConn.getConnection();
    
    public void insertLesson(LessonDTO dto) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;
        
        try {
            sql = "INSERT INTO lessonBoard(CM_num, division, title, content, "
                + " CA_date, fileName, views, MB_num, lessonNum) "
                + " VALUES (seq_lesson_board.NEXTVAL, ?, ?, ?, SYSDATE, ?, 0, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, dto.getDivision());
            pstmt.setString(2, dto.getTitle());
            pstmt.setString(3, dto.getBoard_content());
            pstmt.setString(4, dto.getFileName());
            pstmt.setLong(5, dto.getMb_num());
            pstmt.setInt(6, dto.getLessonNum());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }
    
    public int dataCount() throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) FROM lessonBoard";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getInt(1);
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

    public int dataCount(String schType, String kwd) throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) FROM lessonBoard b "
                + " JOIN Member m ON b.MB_num = m.MB_num ";
            if(schType.equals("all")) {
                sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ";  // board_content가 아닌 content
            } else if(schType.equals("title")) {
                sql += " WHERE INSTR(title, ?) >= 1 ";
            } else {
                sql += " WHERE INSTR(content, ?) >= 1 ";  // board_content가 아닌 content
            }

            pstmt = conn.prepareStatement(sql);
            
            if(schType.equals("all")) {
                pstmt.setString(1, kwd);
                pstmt.setString(2, kwd);
            } else {
                pstmt.setString(1, kwd);
            }

            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getInt(1);
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

    public List<LessonDTO> listBoard(int offset, int size) throws SQLException {
        List<LessonDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(" SELECT CM_num, division, title, content, ");
            sb.append("    TO_CHAR(CA_date, 'YYYY-MM-DD') CA_date, fileName, views, ");
            sb.append("    m.nickName, l.lessonName ");
            sb.append(" FROM lessonBoard b ");
            sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
            sb.append(" JOIN lesson l ON b.lessonNum = l.lessonNum ");
            sb.append(" ORDER BY CM_num DESC ");
            sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            
            pstmt.setInt(1, offset);
            pstmt.setInt(2, size);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                LessonDTO dto = new LessonDTO();

                dto.setCm_num(rs.getLong("CM_num"));
                dto.setDivision(rs.getString("division"));
                dto.setTitle(rs.getString("title"));
                dto.setBoard_content(rs.getString("content"));
                dto.setCa_date(rs.getString("CA_date"));
                dto.setFileName(rs.getString("fileName"));
                dto.setViews(rs.getLong("views"));
                dto.setNickName(rs.getString("nickName"));
                dto.setLessonName(rs.getString("lessonName"));

                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return list;
    }
    
    public List<LessonDTO> listBoard(int offset, int size, String schType, String kwd) throws SQLException {
        List<LessonDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(" SELECT CM_num, division, title, content, ");
            sb.append("    TO_CHAR(CA_date, 'YYYY-MM-DD') CA_date, fileName, views, ");
            sb.append("    m.nickName, l.lessonName ");
            sb.append(" FROM lessonBoard b ");
            sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
            sb.append(" JOIN lesson l ON b.lessonNum = l.lessonNum ");
            
            if(schType.equals("all")) {
                sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
            } else if(schType.equals("title")) {
                sb.append(" WHERE INSTR(title, ?) >= 1 ");
            } else if(schType.equals("content")) {
                sb.append(" WHERE INSTR(content, ?) >= 1 ");
            } else if(schType.equals("nickName")) {
                sb.append(" WHERE INSTR(m.nickName, ?) >= 1 ");
            }
            
            sb.append(" ORDER BY CM_num DESC ");
            sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            
            if(schType.equals("all")) {
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
            while(rs.next()) {
                LessonDTO dto = new LessonDTO();
                dto.setCm_num(rs.getLong("CM_num"));
                dto.setDivision(rs.getString("division"));
                dto.setTitle(rs.getString("title"));
                dto.setBoard_content(rs.getString("content"));
                dto.setCa_date(rs.getString("CA_date"));
                dto.setFileName(rs.getString("fileName"));
                dto.setViews(rs.getLong("views"));
                dto.setNickName(rs.getString("nickName"));
                dto.setLessonName(rs.getString("lessonName"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return list;
    }
    
 // 게시글 보기
    public LessonDTO findById(long num) throws SQLException {
        LessonDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT b.CM_num, b.division, b.title, b.content, "
                + " b.views, b.CA_date, b.fileName, "
                + " b.MB_num, m.nickName, "
                + " b.lessonNum, l.lessonName "
                + " FROM lessonBoard b "
                + " JOIN Member m ON b.MB_num = m.MB_num "
                + " JOIN lesson l ON b.lessonNum = l.lessonNum "
                + " WHERE b.CM_num = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, num);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                dto = new LessonDTO();
                
                dto.setCm_num(rs.getLong("CM_num"));
                dto.setDivision(rs.getString("division"));
                dto.setTitle(rs.getString("title"));
                dto.setBoard_content(rs.getString("content"));
                dto.setViews(rs.getLong("views"));
                dto.setCa_date(rs.getString("CA_date"));
                dto.setFileName(rs.getString("fileName"));
                dto.setMb_num(rs.getLong("MB_num"));
                dto.setNickName(rs.getString("nickName"));
                dto.setLessonNum(rs.getInt("lessonNum"));
                dto.setLessonName(rs.getString("lessonName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return dto;
    }

    // 조회수 증가
    public void updateHitCount(long num) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;

        try {
            sql = "UPDATE lessonBoard SET views=views+1 WHERE CM_num=?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, num);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }
    
 // 이전글
    public LessonDTO findByPrev(long num, String schType, String kwd) throws SQLException {
        LessonDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            if (kwd != null && kwd.length() != 0) {
                sb.append(" SELECT CM_num, title ");
                sb.append(" FROM lessonBoard b ");
                sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
                sb.append(" WHERE CM_num > ? ");
                if (schType.equals("all")) {
                    sb.append("   AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
                } else if (schType.equals("title")) {
                    sb.append("   AND INSTR(title, ?) >= 1 ");
                } else if (schType.equals("content")) {
                    sb.append("   AND INSTR(content, ?) >= 1 ");
                } else if (schType.equals("nickName")) {
                    sb.append("   AND INSTR(m.nickName, ?) >= 1 ");
                }
                sb.append(" ORDER BY CM_num ASC ");
                sb.append(" FETCH FIRST 1 ROWS ONLY ");

                pstmt = conn.prepareStatement(sb.toString());
                
                pstmt.setLong(1, num);
                if (schType.equals("all")) {
                    pstmt.setString(2, kwd);
                    pstmt.setString(3, kwd);
                } else {
                    pstmt.setString(2, kwd);
                }
            } else {
                sb.append(" SELECT CM_num, title ");
                sb.append(" FROM lessonBoard ");
                sb.append(" WHERE CM_num > ? ");
                sb.append(" ORDER BY CM_num ASC ");
                sb.append(" FETCH FIRST 1 ROWS ONLY ");

                pstmt = conn.prepareStatement(sb.toString());
                pstmt.setLong(1, num);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new LessonDTO();
                dto.setCm_num(rs.getLong("CM_num"));
                dto.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return dto;
    }

    // 다음글
    public LessonDTO findByNext(long num, String schType, String kwd) throws SQLException {
        LessonDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            if (kwd != null && kwd.length() != 0) {
                sb.append(" SELECT CM_num, title ");
                sb.append(" FROM lessonBoard b ");
                sb.append(" JOIN Member m ON b.MB_num = m.MB_num ");
                sb.append(" WHERE CM_num < ? ");
                if (schType.equals("all")) {
                    sb.append("   AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
                } else if (schType.equals("title")) {
                    sb.append("   AND INSTR(title, ?) >= 1 ");
                } else if (schType.equals("content")) {
                    sb.append("   AND INSTR(content, ?) >= 1 ");
                } else if (schType.equals("nickName")) {
                    sb.append("   AND INSTR(m.nickName, ?) >= 1 ");
                }
                sb.append(" ORDER BY CM_num DESC ");
                sb.append(" FETCH FIRST 1 ROWS ONLY ");

                pstmt = conn.prepareStatement(sb.toString());
                
                pstmt.setLong(1, num);
                if (schType.equals("all")) {
                    pstmt.setString(2, kwd);
                    pstmt.setString(3, kwd);
                } else {
                    pstmt.setString(2, kwd);
                }
            } else {
                sb.append(" SELECT CM_num, title ");
                sb.append(" FROM lessonBoard ");
                sb.append(" WHERE CM_num < ? ");
                sb.append(" ORDER BY CM_num DESC ");
                sb.append(" FETCH FIRST 1 ROWS ONLY ");

                pstmt = conn.prepareStatement(sb.toString());
                pstmt.setLong(1, num);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new LessonDTO();
                dto.setCm_num(rs.getLong("CM_num"));
                dto.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return dto;
    }
}