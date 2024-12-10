package com.hyun3.domain.academy;

public class BoardRankDTO {
	private long mb_Num;		// 회원번호
	private String userId;		// 아이디
	private String nickName;	// 닉네임
	
	private long cm_Num; 		// 게시글번호
    private String title; 		// 글제목
    private String ca_Date;		// 게시글 생성일
	private String dateTime;	// 좋아요 클릭한 시간
	private int likeCount;      // 좋아요 수
	
	public long getMb_Num() {
		return mb_Num;
	}
	public void setMb_Num(long mb_Num) {
		this.mb_Num = mb_Num;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public long getCm_Num() {
		return cm_Num;
	}
	public void setCm_Num(long cm_Num) {
		this.cm_Num = cm_Num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCa_Date() {
		return ca_Date;
	}
	public void setCa_Date(String ca_Date) {
		this.ca_Date = ca_Date;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	
	
}
