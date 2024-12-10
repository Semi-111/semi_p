package com.hyun3.domain.board;

import com.hyun3.domain.MemberDTO;

public class InfoBoardDTO {
  private long cmNum;        // 게시글 번호
  private String division;  // 구분
  private String title;     // 제목
  private String content;   // 내용
  private String caDate;      // 생성일
  private String fileName;  // 이미지 파일 이름
  private Integer views;    // 조회수 (null 허용)
  private long mbNum;        // 회원번호
  private MemberDTO member; // 작성자 정보 (MemberDTO)

  public MemberDTO getMember() {
    return member;
  }

  public void setMember(MemberDTO member) {
    this.member = member;
  }

  public InfoBoardDTO() {}

  public long getCmNum() {
    return cmNum;
  }

  public void setCmNum(long cmNum) {
    this.cmNum = cmNum;
  }

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCaDate() {
    return caDate;
  }

  public void setCaDate(String caDate) {
    this.caDate = caDate;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getViews() {
    return views;
  }

  public void setViews(Integer views) {
    this.views = views;
  }

  public long getMbNum() {
    return mbNum;
  }

  public void setMbNum(int mbNum) {
    this.mbNum = mbNum;
  }
}

