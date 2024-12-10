package com.hyun3.controller;


import com.hyun3.dao.map.MapDAO;
import com.hyun3.domain.map_api.MapDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.mvc.annotation.RequestMethod.POST;
import static com.hyun3.util.ApiKey.*;

@Controller
public class MapController {

    @RequestMapping(value = "/map", method = GET)
    public ModelAndView mapMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/map");

        mav.addObject("NaverClientID", NAVER_MAP_ID);

        return mav;
    }

    @RequestMapping(value = "/map/move", method = GET)
    public ModelAndView apiSend(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/sendAPI");

        int page = Integer.parseInt(req.getParameter("page"));
        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");

        mav.addObject("kakao", KAKAO_CLIENT_KEY);
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


    @RequestMapping(value = "/map/naverBlog", method = GET)
    public void naverBlog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientId = NAVER_CLIENT_ID;
        String clientSecret = NAVER_MAP_SECRET_KEY;
       
        int startNum = 0;

        String text;
        try {
            String searchText = req.getParameter("query");
            text = URLEncoder.encode(searchText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/blog.json?query=" + text + "&display=100";
        // 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL, requestHeaders);

        System.out.println(responseBody);

    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }




    }


    //TODO 카카오 지도 API 데이터 요청
    @RequestMapping(value = "/map/kakaoApi", method = POST)
    public void kakaoApi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String kakao = KAKAO_CLIENT_KEY;

    }


}
