package com.hyun3.domain.map_api;

import java.util.List;

public class MapDTO {

    private Long stId;
    private String stName;
    private String address;
    private String tel;
    private String lat;
    private String lon;
    private String ctGroup;
    private String ctName;

    private List<StImgDTO> stImgDTO;
    private List<BlogDTO> blogDTO;

    public Long getStId() {
        return stId;
    }

    public void setStId(Long stId) {
        this.stId = stId;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCtGroup() {
        return ctGroup;
    }

    public void setCtGroup(String ctGroup) {
        this.ctGroup = ctGroup;
    }

    public String getCtName() {
        return ctName;
    }

    public void setCtName(String ctName) {
        this.ctName = ctName;
    }

    public List<StImgDTO> getStImgDTO() {
        return stImgDTO;
    }

    public void setStImgDTO(List<StImgDTO> stImgDTO) {
        this.stImgDTO = stImgDTO;
    }

    public List<BlogDTO> getBlogDTO() {
        return blogDTO;
    }

    public void setBlogDTO(List<BlogDTO> blogDTO) {
        this.blogDTO = blogDTO;
    }
}
