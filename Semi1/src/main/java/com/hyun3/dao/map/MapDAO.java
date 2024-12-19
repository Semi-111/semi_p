package com.hyun3.dao.map;

import com.hyun3.domain.map.BlogDTO;
import com.hyun3.domain.map.MapDTO;
import com.hyun3.domain.map.StImgDTO;
import com.hyun3.util.DBConn;
import com.hyun3.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapDAO {
    private Connection conn = DBConn.getConnection();
    private String sql;


    public List<MapDTO> selectMapData(double lat1, double lon1, double lat2, double lon2, int start, int size) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MapDTO> resultList = new ArrayList<>();
        try {
            sql = " WITH ThumbnailCTE AS (" +
                    " SELECT ST_ID, THUMBNAIL," +
                    " ROW_NUMBER() OVER (PARTITION BY ST_ID ORDER BY IMG_NUM) AS rn" +
                    " FROM ST_IMG) " +
                    " SELECT m.ST_ID ST_ID, ST_NAME, ADDRESS, TEL ,THUMBNAIL,LAT,LON " +
                    " FROM MAP m " +
                    " JOIN ThumbnailCTE t ON m.ST_ID = t.ST_ID " +
                    " WHERE t.rn = 1 " +
                    " AND LAT BETWEEN ? AND ? " +
                    " AND LON BETWEEN ? AND ?" +
                    " ORDER BY ST_ID" +
                    " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";

            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, lat1);
            pstmt.setDouble(2, lat2);
            pstmt.setDouble(3, lon1);
            pstmt.setDouble(4, lon2);
            pstmt.setInt(5, start);
            pstmt.setInt(6, size);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                MapDTO mapDTO = new MapDTO();
                mapDTO.setStId(rs.getLong("ST_ID"));
                mapDTO.setStName(rs.getString("ST_NAME"));
                mapDTO.setAddress(rs.getString("ADDRESS"));
                mapDTO.setTel(rs.getString("TEL"));
                mapDTO.setLat(rs.getDouble("LAT"));
                mapDTO.setLon(rs.getDouble("LON"));
                mapDTO.setDivisionCode(rs.getString("THUMBNAIL"));

                resultList.add(mapDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return resultList;
    }


    public List<MapDTO> selectMapData(double lat1, double lon1, double lat2, double lon2, int start, int size, String schTerm) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MapDTO> resultList = new ArrayList<>();
        try {
            sql = " WITH ThumbnailCTE AS (" +
                    " SELECT ST_ID, THUMBNAIL," +
                    " ROW_NUMBER() OVER (PARTITION BY ST_ID ORDER BY IMG_NUM) AS rn" +
                    " FROM ST_IMG) " +
                    " SELECT m.ST_ID ST_ID, ST_NAME, ADDRESS, TEL ,THUMBNAIL,LAT,LON " +
                    " FROM MAP m " +
                    " JOIN ThumbnailCTE t ON m.ST_ID = t.ST_ID " +
                    " WHERE t.rn = 1 " +
                    " AND LAT BETWEEN ? AND ? " +
                    " AND LON BETWEEN ? AND ?" +
                    " AND (m.ST_NAME LIKE ? OR m.CT_NAME LIKE ?)" +
                    " ORDER BY ST_ID" +
                    " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";

            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, lat1);
            pstmt.setDouble(2, lat2);
            pstmt.setDouble(3, lon1);
            pstmt.setDouble(4, lon2);
            pstmt.setString(5, "%" + schTerm + "%");
            pstmt.setString(6, "%" + schTerm + "%");
            pstmt.setInt(7, start);
            pstmt.setInt(8, size);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                MapDTO mapDTO = new MapDTO();
                mapDTO.setStId(rs.getLong("ST_ID"));
                mapDTO.setStName(rs.getString("ST_NAME"));
                mapDTO.setAddress(rs.getString("ADDRESS"));
                mapDTO.setTel(rs.getString("TEL"));
                mapDTO.setLat(rs.getDouble("LAT"));
                mapDTO.setLon(rs.getDouble("LON"));
                mapDTO.setDivisionCode(rs.getString("THUMBNAIL"));

                resultList.add(mapDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return resultList;
    }


    public List<MapDTO> selectMainMapData(double lat1, double lon1, double lat2, double lon2, String schTerm) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MapDTO> result = new ArrayList<>();

        try {
            sql = " WITH ThumbnailCTE AS (" +
                    " SELECT ST_ID, THUMBNAIL," +
                    " ROW_NUMBER() OVER (PARTITION BY ST_ID ORDER BY IMG_NUM) AS rn" +
                    " FROM ST_IMG) " +
                    " SELECT m.ST_ID ST_ID, ST_NAME, ADDRESS, LAT, TEL, LON ,THUMBNAIL" +
                    " FROM MAP m " +
                    " JOIN ThumbnailCTE t ON m.ST_ID = t.ST_ID " +
                    " WHERE t.rn = 1 " +
                    " AND LAT BETWEEN ? AND ? " +
                    " AND LON BETWEEN ? AND ? ";
            if (!schTerm.equals("none")) {
                sql += " AND (m.ST_NAME LIKE ? OR m.CT_NAME LIKE ?) ";
            }
            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, lat1);
            pstmt.setDouble(2, lat2);
            pstmt.setDouble(3, lon1);
            pstmt.setDouble(4, lon2);
            if (!schTerm.equals("none")) {
                pstmt.setString(5, "%" + schTerm + "%");
                pstmt.setString(6, "%" + schTerm + "%");
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MapDTO mapDTO = new MapDTO();
                mapDTO.setStId(rs.getLong("ST_ID"));
                mapDTO.setStName(rs.getString("ST_NAME"));
                mapDTO.setAddress(rs.getString("ADDRESS"));
                mapDTO.setTel(rs.getString("TEL"));
                mapDTO.setLat(rs.getDouble("LAT"));
                mapDTO.setLon(rs.getDouble("LON"));
                mapDTO.setDivisionCode(rs.getString("THUMBNAIL"));


                result.add(mapDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return result;

    }


    public int mapCount(double lat1, double lon1, double lat2, double lon2) {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) cnt "
                    + " FROM MAP " +
                    " WHERE LAT BETWEEN ? AND ? " +
                    " AND LON BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, lat1);
            pstmt.setDouble(2, lat2);
            pstmt.setDouble(3, lon1);
            pstmt.setDouble(4, lon2);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return result;
    }


    public int mapCount(double lat1, double lon1, double lat2, double lon2, String schTerm) {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) cnt "
                    + " FROM MAP " +
                    " WHERE LAT BETWEEN ? AND ? " +
                    " AND LON BETWEEN ? AND ? " +
                    " AND (ST_NAME LIKE ? OR CT_NAME LIKE ?)";

            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, lat1);
            pstmt.setDouble(2, lat2);
            pstmt.setDouble(3, lon1);
            pstmt.setDouble(4, lon2);
            pstmt.setString(5, "%" + schTerm + "%");
            pstmt.setString(6, "%" + schTerm + "%");

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return result;
    }


    public MapDTO getDetails(long stId) throws SQLException {
        String sql = "SELECT * FROM MAP WHERE ST_ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, stId);
        ResultSet rs = pstmt.executeQuery();

        MapDTO details = null;
        if (rs.next()) {
            details = new MapDTO();
            details.setStId(rs.getLong("ST_ID"));
            details.setStName(rs.getString("ST_NAME"));
            details.setAddress(rs.getString("ADDRESS"));
            details.setTel(rs.getString("TEL"));
            details.setCtName(rs.getString("CT_NAME"));

        }

        rs.close();
        pstmt.close();
        return details;

    }

public List<BlogDTO> getBlog(long stId, int size, int start) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<BlogDTO> result = new ArrayList<>();
    try {
        sql = "SELECT BLOG_NUM,BLOG_NAME,BLOG_TITLE,BLOG_CONTENT,BLOG_URL,  " +
                " TO_CHAR(REG_DATE, 'YYYY-MM-DD') REG_DATE "+
                " FROM BLOG WHERE ST_ID = ?" +
              " ORDER BY BLOG_NUM" +
              " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, stId);
        pstmt.setInt(2, start);
        pstmt.setInt(3, size);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setBlogNum(rs.getLong("BLOG_NUM"));
            blogDTO.setBlogName(rs.getString("BLOG_NAME"));
            blogDTO.setBlogTitle(rs.getString("BLOG_TITLE"));
            blogDTO.setBlogContent(rs.getString("BLOG_CONTENT"));
            blogDTO.setBlogUrl(rs.getString("BLOG_URL"));
            blogDTO.setBlogDate(rs.getString("REG_DATE"));


            result.add(blogDTO);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBUtil.close(rs);
        DBUtil.close(pstmt);
    }

    return result;
}
    public List<StImgDTO> getImg(long id,int start, int size) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<StImgDTO> result = new ArrayList<>();

        try {
            sql = "SELECT * FROM ST_IMG WHERE ST_ID = ?" +
                    " ORDER BY IMG_NUM " +
                    " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.setInt(2, start);
            pstmt.setInt(3, size);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                StImgDTO stImgDTO = new StImgDTO();
                stImgDTO.setImgNum(rs.getLong("IMG_NUM"));
                stImgDTO.setThumbnail(rs.getString("THUMBNAIL"));
                stImgDTO.setImgUrl(rs.getString("IMG_URL"));
                stImgDTO.setImgTitle(rs.getString("IMG_TITLE"));


                result.add(stImgDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return result;
    }

    public int blogData(long  id) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            sql = "SELECT COUNT(*) cnt FROM BLOG WHERE ST_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (Exception e) {
            e.printStackTrace();

    }

    return result;
    }

    public int imgData(long id){

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            sql = "SELECT COUNT(*) cnt FROM ST_IMG WHERE ST_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return result;
    }


}
