package com.hyun3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.domain.ReportDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

public class ReportDAO {
    private Connection conn = DBConn.getConnection();
    
    // 신고 등록
    public void insertReport(ReportDTO dto) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;
        
        try {
            sql = "INSERT INTO Report(RP_num, RP_title, RP_content, RP_reason, "
                + " RP_table, RP_url, MB_num) "
                + " VALUES (seq_report.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, dto.getRP_title());
            pstmt.setString(2, dto.getRP_content());
            pstmt.setString(3, dto.getRP_reason());
            pstmt.setString(4, dto.getRP_table());
            pstmt.setString(5, dto.getRP_url());
            pstmt.setLong(6, dto.getMb_num());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }
    
    // 신고 목록
    public List<ReportDTO> listReport() {
        List<ReportDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
            sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
            sb.append("    r.MB_num, m.nickName ");
            sb.append(" FROM Report r ");
            sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
            sb.append(" ORDER BY r.RP_num DESC ");
            
            pstmt = conn.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                ReportDTO dto = new ReportDTO();
                
                dto.setRP_num(rs.getLong("RP_num"));
                dto.setRP_title(rs.getString("RP_title"));
                dto.setRP_content(rs.getString("RP_content"));
                dto.setRP_reason(rs.getString("RP_reason"));
                dto.setRP_table(rs.getString("RP_table"));
                dto.setRP_url(rs.getString("RP_url"));
                dto.setMb_num(rs.getLong("MB_num"));
                dto.setMemberName(rs.getString("nickName"));
                
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
    
    // 신고글 상세 정보
    public ReportDTO findById(long rpNum) {
        ReportDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            sb.append("SELECT r.RP_num, r.RP_title, r.RP_content, ");
            sb.append("    r.RP_reason, r.RP_table, r.RP_url, ");
            sb.append("    r.MB_num, m.nickName ");
            sb.append(" FROM Report r ");
            sb.append(" JOIN Member m ON r.MB_num = m.MB_num ");
            sb.append(" WHERE r.RP_num = ? ");
            
            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setLong(1, rpNum);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                dto = new ReportDTO();
                
                dto.setRP_num(rs.getLong("RP_num"));
                dto.setRP_title(rs.getString("RP_title"));
                dto.setRP_content(rs.getString("RP_content"));
                dto.setRP_reason(rs.getString("RP_reason"));
                dto.setRP_table(rs.getString("RP_table"));
                dto.setRP_url(rs.getString("RP_url"));
                dto.setMb_num(rs.getLong("MB_num"));
                dto.setMemberName(rs.getString("nickName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
        
        return dto;
    }
    
    // 신고글 삭제
    public void deleteReport(long rpNum) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;
        
        try {
            sql = "DELETE FROM Report WHERE RP_num = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setLong(1, rpNum);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }
}