package com.hyun3.controller;


import com.hyun3.dao.map.MapDAO;
import com.hyun3.domain.map_api.MapDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import com.hyun3.util.ApiKey;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.mvc.annotation.RequestMethod.POST;
import static com.hyun3.util.ApiKey.KAKAO_CLIENT_KEY;
import static com.hyun3.util.ApiKey.NAVER_CLIENT_ID;

@Controller
public class MapController {

    @RequestMapping(value = "/map", method = GET)
    public ModelAndView mapMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/map");

        mav.addObject("NaverClientID", NAVER_CLIENT_ID);

        return mav;
    }

    @RequestMapping(value = "/map/move", method = GET)
    public ModelAndView apiSend(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/sendAPI");

        int page = Integer.parseInt(req.getParameter("page"));
        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");


        mav.addObject("page", page);
        mav.addObject("lat", lat);
        mav.addObject("lon", lon);
        // 37.556601, 126.919494

        return mav;
    }

    @RequestMapping(value = "/map/db", method = POST)
    public void inDB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        MapDAO dao = new MapDAO();
        List<MapDTO> mapDao = dao.selectStoreData();

        StringBuilder jsonBuffer = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        String jsonString = jsonBuffer.toString();
        JSONArray jsonArray = new JSONArray(jsonString);

        Set<MapDTO> setDto = new TreeSet<>(mapDao);
        List<MapDTO> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            MapDTO dto = new MapDTO();
            dto.setDivisionCode(jsonObject.getString("id"));
            dto.setStName(jsonObject.getString("place_name"));
            dto.setTel(jsonObject.getString("phone"));
            dto.setCtGroup(jsonObject.getString("category_group_name"));
            dto.setCtName(jsonObject.getString("category_name"));
            dto.setAddress(jsonObject.getString("address_name"));
            dto.setLat(Double.parseDouble(jsonObject.getString("y")));
            dto.setLon(Double.parseDouble(jsonObject.getString("x")));
            if (setDto.add(dto)) {
                list.add(dto);
                //System.out.println(list.get(list.size() - 1).getStName());
            }
        }


        //TODO DB에 저장
        dao.inputStoreData(list);
        System.out.println("DB 저장 완료");


    }


    //TODO 카카오 지도 API 데이터 요청
    @RequestMapping(value = "/map/kakaoApi", method = POST)
    public void kakaoApi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String kakao = KAKAO_CLIENT_KEY;

    }


}
