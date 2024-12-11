package com.hyun3.controller;


import com.hyun3.dao.map.MapDAO;
import com.hyun3.domain.map_api.BlogDTO;
import com.hyun3.domain.map_api.MapDTO;
import com.hyun3.domain.map_api.StImgDTO;
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
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.mvc.annotation.RequestMethod.POST;
import static com.hyun3.util.ApiKey.*;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

@Controller
public class MapController {

    private MapDAO dao = null;

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

        dao = new MapDAO();
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

        dao.inputStoreData(list);
    }


    @RequestMapping(value = "/map/naverBlog", method = GET)
    public void naverBlog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        dao = new MapDAO();
        String clientId = NAVER_CLIENT_ID;
        String clientSecret = NAVER_SECRET_KEY;

        List<MapDTO> mapDao = dao.testData();

        for (MapDTO mapDTO : mapDao) {
            String text = URLEncoder.encode(mapDTO.getStName(), StandardCharsets.UTF_8);
            int startNum = 1;



            for (int i = 0; i <= 5; i++) {
                String apiURL = "https://openapi.naver.com/v1/search/blog.json?query=" + text + "&display=100&start=" + startNum;

                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("X-Naver-Client-Id", clientId);
                requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                String responseBody = get(apiURL, requestHeaders);



                JSONObject jsonObject = new JSONObject(responseBody);

                JSONArray items = jsonObject.getJSONArray("items");

                List<BlogDTO> list = dao.selectBlogData(mapDTO.getStId());

                Set<BlogDTO> setDto = new TreeSet<>(list);


                List<BlogDTO> dto = blogMethod(items, setDto);

                MapDAO dao = new MapDAO();
                System.out.println(mapDTO.getStName() + " : " + dto.size());

                if (dto.isEmpty()) {
                    continue;
                }

                dao.inputBlog(dto, mapDTO.getStId());
                startNum += 100;
            }
        }

    }

    private List<BlogDTO> blogMethod(JSONArray items, Set<BlogDTO> setDto) {
        List<BlogDTO> dto = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = unescapeHtml4(item.getString("title").replaceAll("<b>", "").replaceAll("</b>", ""));
            String content = unescapeHtml4(item.getString("description").replaceAll("<b>", "").replaceAll("</b>", ""));
            if (title.contains("홍대") || content.contains("홍대")) {
                BlogDTO blogDTO = new BlogDTO();
                blogDTO.setBlogTitle(title);
                blogDTO.setBlogContent(content);
                blogDTO.setBlogName(item.getString("bloggername"));
                blogDTO.setBlogUrl(item.getString("link"));
                blogDTO.setBlogDate(item.getString("postdate"));
                if (setDto.add(blogDTO)) {
                    dto.add(blogDTO);
                }
            }
        }
        return dto;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
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



    @RequestMapping(value = "/map/naverImg", method = GET)
    public void imgData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        dao = new MapDAO();
        String clientId = NAVER_CLIENT_ID;
        String clientSecret = NAVER_SECRET_KEY;

        List<MapDTO> mapDao = dao.testData();

        for (MapDTO mapDTO : mapDao) {
            String text = URLEncoder.encode(mapDTO.getStName(), StandardCharsets.UTF_8);
            int startNum = 1;

            for (int i = 0; i <= 5; i++) {
                String apiURL = "https://openapi.naver.com/v1/search/image.json?query=" + text + "&display=100&start=" + startNum;

                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("X-Naver-Client-Id", clientId);
                requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                String responseBody = get(apiURL, requestHeaders);

                if (responseBody.isEmpty()) {
                    System.err.println("Response body is null or empty for URL: " + apiURL);
                    continue;
                }

                JSONObject jsonObject = new JSONObject(responseBody);

                if (!jsonObject.has("items")) {
                    System.err.println("No items found in the response for URL: " + apiURL);
                    continue;
                }

                JSONArray items = jsonObject.getJSONArray("items");

                List<StImgDTO> list = dao.selectImgData(mapDTO.getStId());

                Set<StImgDTO> setDto = new TreeSet<>(list);

                List<StImgDTO> dto = imgMethod(items, setDto, mapDTO.getStName());

                System.out.println(mapDTO.getStName() + " : " + dto.size());

                if (dto.isEmpty()) {
                    continue;
                }

                dao.inputImg(dto, mapDTO.getStId());
                startNum += 100;
            }
        }

    }


    private List<StImgDTO> imgMethod(JSONArray items, Set<StImgDTO> setDto, String storeName) {
        List<StImgDTO> dto = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = unescapeHtml4(item.optString("title", "").replaceAll("<b>", "").replaceAll("</b>", ""));
            if (title.isEmpty() || (!title.contains("홍대") && !title.contains(storeName))) {
                continue;
            }
            StImgDTO imgDto = new StImgDTO();
            imgDto.setImgTitle(title);
            imgDto.setImgUrl(item.optString("link", ""));
            imgDto.setThumbnail(item.optString("thumbnail", ""));
            if (imgDto.getImgUrl().isEmpty() || imgDto.getThumbnail().isEmpty()) {
                continue;
            }
            dto.add(imgDto);
        }
        return dto;
    }


}
