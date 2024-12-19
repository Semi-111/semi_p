package com.hyun3.domain.map;

public class BlogDTO implements Comparable<BlogDTO>  {

    private Long blogNum;
    private String blogTitle;
    private String blogContent;
    private String blogName;
    private String blogUrl;
    private String blogDate;

    private MapDTO mapDTO;

    public Long getBlogNum() {
        return blogNum;
    }

    public void setBlogNum(Long blogNum) {
        this.blogNum = blogNum;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(String blogDate) {
        this.blogDate = blogDate;
    }

    public MapDTO getMapDTO() {
        return mapDTO;
    }

    public void setMapDTO(MapDTO mapDTO) {
        this.mapDTO = mapDTO;
    }


    @Override
    public int compareTo(BlogDTO o) {
        return this.blogName == null ? -1 : (o.blogName == null ? 1 : this.blogName.compareTo(o.blogName));
    }
}
