package com.hyun3.domain.board;

public class SecretBoardDTO {
	private long cmNum; // 게시글 번호
	private long mbNum; // 회원번호
	private String division; // 구분
	private String title; // 제목
	private String content; // 내용
	private String caDate; // 생성일
	private String fileName; // 이미지 파일 이름
	private int views; // 조회수
	private String formattedCaDate;

	public String getFormattedCaDate() {
		return formattedCaDate;
	}

	public void setFormattedCaDate(String formattedCaDate) {
		this.formattedCaDate = formattedCaDate;
	}

	private int replyCount;
	private int boardLikeCount;

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

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public int getBoardLikeCount() {
		return boardLikeCount;
	}

	public void setBoardLikeCount(int boardLikeCount) {
		this.boardLikeCount = boardLikeCount;
	}
}
