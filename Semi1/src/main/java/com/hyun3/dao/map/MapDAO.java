package com.hyun3.dao.map;

import com.hyun3.domain.map_api.MapDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapDAO {
    private Connection conn = DBConn.getConnection();
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private String sql;

    public List<MapDTO> selectStoreData() {
        List<MapDTO> resultList = new ArrayList<>();

        try {

            sql = "SELECT ST_ID,ST_NAME FROM MAP";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MapDTO mapDTO = new MapDTO();
                mapDTO.setStId(rs.getLong("ST_ID"));
                mapDTO.setStName(rs.getString("ST_NAME"));

                resultList.add(mapDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(rs);
        }

        return resultList;
    }

    public void inputStoreData(List<MapDTO> dto) throws SQLException {

        try {
            conn.setAutoCommit(false);

            sql="INSERT INTO MAP(ST_ID, ST_NAME, ADDRESS, TEL, LAT, LON, CT_GROUP, CT_NAME, ST_DIVISION_CODE) "+
                "VALUES (SEQ_MAP.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            for (MapDTO mapDTO : dto) {
                pstmt.setString(1,mapDTO.getStName());
                pstmt.setString(2,mapDTO.getAddress());
                pstmt.setString(3,mapDTO.getTel());
                pstmt.setDouble(4,mapDTO.getLat());
                pstmt.setDouble(5,mapDTO.getLon());
                pstmt.setString(6,mapDTO.getCtGroup());
                pstmt.setString(7,mapDTO.getCtName());
                pstmt.setString(8,mapDTO.getDivisionCode());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();

        }catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        }finally {
            conn.setAutoCommit(true);
            DBUtil.close(pstmt);
        }




    }


}
