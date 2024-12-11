package com.hyun3.dao.map;

import com.hyun3.domain.map_api.BlogDTO;
import com.hyun3.domain.map_api.MapDTO;
import com.hyun3.domain.map_api.StImgDTO;
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

    public List<MapDTO> testData() {
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


    public List<BlogDTO> selectBlogData(long storeID) {
        List<BlogDTO> resultList = new ArrayList<>();
        try {
            sql = "SELECT BLOG_NAME,BLOG_URL FROM BLOG WHERE ST_ID = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1,storeID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BlogDTO blogDTO = new BlogDTO();

                blogDTO.setBlogName(rs.getString("BLOG_NAME"));
                blogDTO.setBlogUrl(rs.getString("BLOG_URL"));

                resultList.add(blogDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(rs);
        }

        return resultList;
    }

    public void inputBlog(List<BlogDTO> dto,long storeID) throws SQLException {

        try {
            conn.setAutoCommit(false);

            sql="INSERT INTO BLOG(BLOG_NUM, BLOG_TITLE, BLOG_CONTENT, BLOG_NAME, BLOG_URL, REG_DATE, ST_ID) "+
                "VALUES (SEQ_BLOG.nextval, ?, ?, ?, ?, ?, ?)";


            pstmt = conn.prepareStatement(sql);

            for (BlogDTO blogDTO : dto) {
                pstmt.setString(1,blogDTO.getBlogTitle());
                pstmt.setString(2,blogDTO.getBlogContent());
                pstmt.setString(3,blogDTO.getBlogName());
                pstmt.setString(4,blogDTO.getBlogUrl());
                pstmt.setString(5,blogDTO.getBlogDate());
                pstmt.setLong(6,storeID);
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


    public void inputImg(List<StImgDTO> dto, long storeID) throws SQLException {

        try {
            conn.setAutoCommit(false);

            sql="INSERT INTO ST_IMG(IMG_NUM, IMG_URL, IMG_TITLE, THUMBNAIL, ST_ID) "+
                    "VALUES (SEQ_ST_IMG.nextval, ?, ?, ?, ?)";


            pstmt = conn.prepareStatement(sql);

            for (StImgDTO stImgDTO : dto) {
                pstmt.setString(1,stImgDTO.getImgUrl());
                pstmt.setString(2,stImgDTO.getImgTitle());
                pstmt.setString(3,stImgDTO.getThumbnail());
                pstmt.setLong(4,storeID);
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

    public List<StImgDTO> selectImgData(long storeID) {
        List<StImgDTO> resultList = new ArrayList<>();
        try {
            sql = "SELECT IMG_URL,IMG_TITLE FROM ST_IMG WHERE ST_ID = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1,storeID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StImgDTO imgDTO = new StImgDTO();

                imgDTO.setImgUrl("IMG_URL");
                imgDTO.setImgTitle("IMG_TITLE");
                resultList.add(imgDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(rs);
        }

        return resultList;
    }



}
