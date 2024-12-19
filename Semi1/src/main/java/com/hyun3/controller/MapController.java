package com.hyun3.controller;


import com.hyun3.dao.map.MapDAO;
import com.hyun3.domain.map.BlogDTO;
import com.hyun3.domain.map.MapDTO;
import com.hyun3.domain.map.StImgDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.util.ApiKey.NAVER_MAP_ID;

@Controller
public class MapController {

    private MapDAO dao = null;

    @RequestMapping(value = "/map", method = GET)
    public ModelAndView mapMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/map");

        dao = new MapDAO();
        String q;
        try {
            double req_lat1 = req.getParameter("lat") != null ? Double.parseDouble(req.getParameter("lat")) : 37.5557568;
            double req_lon1 = req.getParameter("lon") != null ? Double.parseDouble(req.getParameter("lon")) : 126.9172611;
            double req_lat2 = req.getParameter("lat2") != null ? Double.parseDouble(req.getParameter("lat2")) : 37.5574451;
            double req_lon2 = req.getParameter("lon2") != null ? Double.parseDouble(req.getParameter("lon2")) : 126.9217269;

            q = req.getParameter("schTerm");

            List<MapDTO> listJS = new ArrayList<>();
            if (q != null) {
                q = URLDecoder.decode(q, StandardCharsets.UTF_8);
            } else {
                q = "none";
            }
            listJS = dao.selectMainMapData(req_lat1, req_lon1, req_lat2, req_lon2, q);


            JSONArray jsonArray = new JSONArray(listJS);
            String jsMarkersData = jsonArray.toString();


            mav.addObject("markersDataJS", jsMarkersData);
            mav.addObject("NaverClientID", NAVER_MAP_ID);
            mav.addObject("page", 1);
            mav.addObject("schTerm", q);
            mav.addObject("lat1", req_lat1);
            mav.addObject("lon1", req_lon1);
            mav.addObject("lat2", req_lat2);
            mav.addObject("lon2", req_lon2);

        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("markersData", "error");
        }


        return mav;
    }


    @RequestMapping(value = "/map/moveP", method = GET)
    public ModelAndView getMapData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        ModelAndView mav = new ModelAndView("map/searchFrame");

        MapDAO dao = new MapDAO();
        MyUtil util = new MyUtilBootstrap();

        try {
            String page = req.getParameter("page");
            double lat1 = Double.parseDouble(req.getParameter("lat1"));
            double lon1 = Double.parseDouble(req.getParameter("lon1"));
            double lat2 = Double.parseDouble(req.getParameter("lat2"));
            double lon2 = Double.parseDouble(req.getParameter("lon2"));
            String query = req.getParameter("schTerm");
            List<MapDTO> list = new ArrayList<>();

            int current = 1;
            if (page != null) {
                current = Integer.parseInt(page);
            }

            int pageSize = 5;
            int start = (current - 1) * pageSize;
            int dataCount;

            if (query == null) {
                dataCount = dao.mapCount(lat1, lon1, lat2, lon2);
                list = dao.selectMapData(lat1, lon1, lat2, lon2, start, pageSize);
            } else {
                query = URLDecoder.decode(query, StandardCharsets.UTF_8);
                dataCount = dao.mapCount(lat1, lon1, lat2, lon2, query);
                list = dao.selectMapData(lat1, lon1, lat2, lon2, start, pageSize, query);
            }
            int totalPage = util.pageCount(dataCount, pageSize);
            if (current > totalPage) {
                current = totalPage;
            }

            String paging = util.pagingMethod(current, totalPage, "mapPage");

            mav.addObject("markersData", list);
            mav.addObject("paging", paging);
            mav.addObject("page", current);
            mav.addObject("totalPage", totalPage);
            mav.addObject("dataCount", dataCount);
            mav.addObject("schTerm", query);

        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("markersData", "error");
        }

        return mav;
    }


    @RequestMapping(value = "/map/details", method = GET)
    public ModelAndView getDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        ModelAndView mav = new ModelAndView("map/detailFrame");

        String stId = req.getParameter("stId");
        MapDAO dao = new MapDAO();

        String page = req.getParameter("page");


        int mainPage = 1;
        int pageSize = 5;
        try {
            if (page != null) {
                mainPage = Integer.parseInt(page);
            }



            MapDTO details = dao.getDetails(Long.parseLong(stId));
            int blog_cnt = dao.blogData(Long.parseLong(stId));
            int img_cnt = dao.imgData(Long.parseLong(stId));

            details.setStImgDTO(dao.getImg(Long.parseLong(stId), 0,4));
            List<BlogDTO> blog = dao.getBlog(Long.parseLong(stId), pageSize, 0);

            mav.addObject("st_id", details.getStId());
            mav.addObject("st_name", details.getStName());
            mav.addObject("address", details.getAddress());
            mav.addObject("tel", details.getTel());
            mav.addObject("category", details.getCtGroup());
            mav.addObject("page", mainPage);

            mav.addObject("blog_cnt", blog_cnt);
            mav.addObject("img_cnt", img_cnt);
            mav.addObject("img", details.getStImgDTO());
            mav.addObject("blog", blog);

        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("details", "error");
        }

        return mav;
    }


    @ResponseBody
    @RequestMapping(value = "/map/blog", method = RequestMethod.GET)
    public Map<String, Object> getBlogReviews(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();

        String stId = req.getParameter("stId");
        String page = req.getParameter("page");

        try {
            int current_page = Integer.parseInt(page);
            int pageSize = 5;

            MapDAO dao = new MapDAO();
            MyUtil util = new MyUtilBootstrap();

            int dataCount = dao.blogData(Long.parseLong(stId));
            int total_page = util.pageCount(dataCount, pageSize);

            if (current_page > total_page) {
                current_page = total_page;
            }

            int offset = (current_page - 1) * pageSize;
            if (offset < 0) offset = 0;

            List<BlogDTO> blogList = dao.getBlog(Long.parseLong(stId), pageSize, offset);

            model.put("list", blogList);
            model.put("pageNo", current_page);
            model.put("total_page", total_page);
            model.put("dataCount", dataCount);
            model.put("state", "true");

        } catch (Exception e) {
            model.put("state", "false");
            e.printStackTrace();
        }

        return model;
    }


@ResponseBody
@RequestMapping(value = "/map/images", method = RequestMethod.GET)
public Map<String, Object> getImages(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Map<String, Object> model = new HashMap<>();

    String stId = req.getParameter("stId");
    String page = req.getParameter("page");


    try {

        int current_page =  Integer.parseInt(page);;
        int pageSize = 6;

        MapDAO dao = new MapDAO();
        MyUtil util = new MyUtilBootstrap();

        int dataCount = dao.imgData(Long.parseLong(stId));
        int total_page = util.pageCount(dataCount, pageSize);

        if (current_page > total_page) {
            current_page = total_page;
        }

        int offset = (current_page - 1) * pageSize;
        if (offset < 0) offset = 0;

        List<StImgDTO> imgList = dao.getImg(Long.parseLong(stId), offset,6);

        model.put("list", imgList);
        model.put("pageNo", current_page);
        model.put("total_page", total_page);
        model.put("dataCount", dataCount);
        model.put("state", "true");

    } catch (Exception e) {
        model.put("state", "false");
        e.printStackTrace();
    }

    return model;
}

}
