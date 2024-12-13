package com.hyun3.domain;

public class LessonDTO {
	private long cm_num; // 게시글 번호
	private String division; // 학과 구분
	private String title; // 글 제목
	private String board_content; // 글 내용
	private String ca_date; // 글 생성일자
	private String fileName;
	private long views;
	private long mb_num; 
	private String nickName;
	
	private int lessonNum; // 학과번호
	private String lessonName; // 학과이름
	
	private boolean userLiked; // 현재 사용자가 좋아요 했는지 여부
    private int likeCount; // 총 좋아요 수
	
    
	public boolean isUserLiked() {
		return userLiked;
	}

	public void setUserLiked(boolean userLiked) {
		this.userLiked = userLiked;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public long getCm_num() {
		return cm_num;
	}

	public void setCm_num(long cm_num) {
		this.cm_num = cm_num;
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

	public String getBoard_content() {
		return board_content;
	}

	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}

	public String getCa_date() {
		return ca_date;
	}

	public void setCa_date(String ca_date) {
		this.ca_date = ca_date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public long getMb_num() {
		return mb_num;
	}

	public void setMb_num(long mb_num) {
		this.mb_num = mb_num;
	}

	public int getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(int lessonNum) {
		this.lessonNum = lessonNum;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

}
