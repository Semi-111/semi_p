package com.hyun3.domain.board;

import java.util.Date;

public class InfoLikeDTO {
  private int mbNum;        // 회원번호
  private int cmNum;        // 게시글 번호
  private Date dateTime;    // 날짜와 시간

  public InfoLikeDTO() {}

  public InfoLikeDTO(int mbNum, int cmNum, Date dateTime) {
    this.mbNum = mbNum;
    this.cmNum = cmNum;
    this.dateTime = dateTime;
  }

  public int getMbNum() {
    return mbNum;
  }

  public void setMbNum(int mbNum) {
    this.mbNum = mbNum;
  }

  public int getCmNum() {
    return cmNum;
  }

  public void setCmNum(int cmNum) {
    this.cmNum = cmNum;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }
}
