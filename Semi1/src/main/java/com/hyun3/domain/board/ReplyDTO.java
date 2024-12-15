package com.hyun3.domain.board;

public class ReplyDTO  {

//  private long mbNum; // 회원번호
//  private long cmNum; // 게시글 번호
  private long coNum; // 댓글 번호
  private long parentNum; // 대댓글에 필요한 용도 db 추가 필요할듯
  private String content; // 내용
  private String caDate; // 등록일
  private int replyLike; // 댓글 좋아요
  private InfoBoardDTO board;
  

  public long getCoNum() {
    return coNum;
  }

  public void setCoNum(long coNum) {
    this.coNum = coNum;
  }

}
