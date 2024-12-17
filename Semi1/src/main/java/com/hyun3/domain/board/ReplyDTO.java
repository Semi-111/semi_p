package com.hyun3.domain.board;

public class ReplyDTO {
  private long coNum; // 댓글 번호
  private String content;
  private long cmNum; // 게시글 번호 또는 부모 댓글 번호
  private long mbNum; // 회원 번호
  private String nickName; // 댓글 작성자의 닉네임
  private String reg_date; // 등록일
  private int answerCount; // 답글 수
  private int likeCount; // 좋아요 수
  private int disLikeCount; // 싫어요 수 (필요 시)

  // 추가된 속성
  private int liked; // 0: 반응 없음, 1: 좋아요, 2: 싫어요

  // 기존 getters and setters

  public long getCoNum() {
    return coNum;
  }

  public void setCoNum(long coNum) {
    this.coNum = coNum;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getCmNum() {
    return cmNum;
  }

  public void setCmNum(long cmNum) {
    this.cmNum = cmNum;
  }

  public long getMbNum() {
    return mbNum;
  }

  public void setMbNum(long mbNum) {
    this.mbNum = mbNum;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getReg_date() {
    return reg_date;
  }

  public void setReg_date(String reg_date) {
    this.reg_date = reg_date;
  }

  public int getAnswerCount() {
    return answerCount;
  }

  public void setAnswerCount(int answerCount) {
    this.answerCount = answerCount;
  }

  public int getLikeCount() {
    return likeCount;
  }

  public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
  }

  public int getDisLikeCount() {
    return disLikeCount;
  }

  public void setDisLikeCount(int disLikeCount) {
    this.disLikeCount = disLikeCount;
  }

  // 추가된 getter and setter
  public int getLiked() {
    return liked;
  }

  public void setLiked(int liked) {
    this.liked = liked;
  }
}
