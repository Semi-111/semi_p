package com.hyun3.dao.Membership;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;
import com.hyun3.domain.member.MemberDTO;

public class MembershipDAO {
    private Connection conn = DBConn.getConnection();

    // 전체 데이터 개수
    public int dataCount() throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) FROM Member";
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

    // 검색에서 전체 데이터 개수
    public int dataCount(String schType, String kwd) throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("SELECT COUNT(*) FROM Member m ");
            
            if (schType.equals("all")) {
                sb.append(" WHERE INSTR(userId, ?) >= 1 OR INSTR(nickName, ?) >= 1 ");
            } else if (schType.equals("userId")) {
                sb.append(" WHERE INSTR(userId, ?) >= 1 ");
            } else if (schType.equals("nickName")) {
                sb.append(" WHERE INSTR(nickName, ?) >= 1 ");
            }

            pstmt = conn.prepareStatement(sb.toString());
            
            if (schType.equals("all")) {
                pstmt.setString(1, kwd);
                pstmt.setString(2, kwd);
            } else {
                pstmt.setString(1, kwd);
            }

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

    // 회원 목록
    public List<MemberDTO> listMember(int offset, int size) {
        List<MemberDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("SELECT m.mb_num, m.userId, m.nickName, ");
            sb.append("    m.ca_day, m.role, m.block, m.lessonNum, ");
            sb.append("    l.lessonname ");
            sb.append(" FROM Member m ");
            sb.append(" LEFT OUTER JOIN lesson l ON m.lessonNum = l.lessonNum ");
            sb.append(" ORDER BY m.mb_num DESC ");
            sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            
            pstmt.setInt(1, offset);
            pstmt.setInt(2, size);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                MemberDTO dto = new MemberDTO();

                dto.setMb_Num(rs.getLong("mb_num"));
                dto.setUserId(rs.getString("userId"));
                dto.setNickName(rs.getString("nickName"));
                dto.setCa_Day(rs.getString("ca_day"));
                dto.setRole(rs.getString("role"));
                dto.setBlock(rs.getInt("block"));
                dto.setLessonNum(rs.getInt("lessonNum"));
                dto.setLessonName(rs.getString("lessonname"));

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

    // 검색에서 리스트
    public List<MemberDTO> listMember(int offset, int size, String schType, String kwd) {
        List<MemberDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("SELECT m.mb_num, m.userId, m.nickName, ");
            sb.append("    m.ca_day, m.role, m.block, m.lessonNum, ");
            sb.append("    l.lessonname ");
            sb.append(" FROM Member m ");
            sb.append(" LEFT OUTER JOIN lesson l ON m.lessonNum = l.lessonNum ");
            
            if (schType.equals("all")) {
                sb.append(" WHERE INSTR(userId, ?) >= 1 OR INSTR(nickName, ?) >= 1 ");
            } else if (schType.equals("userId")) {
                sb.append(" WHERE INSTR(userId, ?) >= 1 ");
            } else if (schType.equals("nickName")) {
                sb.append(" WHERE INSTR(nickName, ?) >= 1 ");
            }
            
            sb.append(" ORDER BY m.mb_num DESC ");
            sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            
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
            
            while(rs.next()) {
                MemberDTO dto = new MemberDTO();

                dto.setMb_Num(rs.getLong("mb_num"));
                dto.setUserId(rs.getString("userId"));
                dto.setNickName(rs.getString("nickName"));
                dto.setCa_Day(rs.getString("ca_day"));
                dto.setRole(rs.getString("role"));
                dto.setBlock(rs.getInt("block"));
                dto.setLessonNum(rs.getInt("lessonNum"));
                dto.setLessonName(rs.getString("lessonname"));

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
}