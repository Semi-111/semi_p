package com.hyun3.domain.map_api;

public class StImgDTO {

    private Long imgNum;
    private String imgUrl;
    private String imgTitle;
    private String thumbnail;

    private MapDTO mapDTO;


    public Long getImgNum() {
        return imgNum;
    }

    public void setImgNum(Long imgNum) {
        this.imgNum = imgNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MapDTO getMapDTO() {
        return mapDTO;
    }

    public void setMapDTO(MapDTO mapDTO) {
        this.mapDTO = mapDTO;
    }
}
