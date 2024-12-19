package com.hyun3.domain.map;

import java.util.List;

public class MapDTO  implements Comparable<MapDTO> {

    private Long stId;
    private String stName;
    private String address;
    private String tel;
    private double lat;
    private double lon;
    private String ctGroup;
    private String divisionCode;
    private String ctName;



    private List<StImgDTO> stImgDTO;
    private List<BlogDTO> blogDTO;

    public Long getStId() {
        return stId;
    }

    public String getDivisionCode() {

        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
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

    @Override
    public int compareTo(MapDTO other) {
        return this.stName.compareTo(other.stName);
    }
}
