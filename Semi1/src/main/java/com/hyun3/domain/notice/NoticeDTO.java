package com.hyun3.domain.notice;

public class NoticeDTO {
	private long cm_num; // 글번호 시퀀스(seq_notice_board) 
	private String division;
	private String title;
	private String content;
	private String ca_date; //SYSDATE
	private String fileName;
	private String nickName;
	
	private int notice; // 중요공지 여부 (0: 일반, 1: 중요)
	
	private long views;
	private long mb_num;
	
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	
	
}
