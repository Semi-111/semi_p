package com.hyun3.domain.sw;

public class MarketDTO {
	private int marketNum; // 장터번호
	private String title;
	private String content;
	private String ca_date;
	private String fileName;
	private int mb_num;
	private int ct_num; // 카테고리 번호
	private String ct_name; // 카테고리 이름
	public int getMarketNum() {
		return marketNum;
	}
	public void setMarketNum(int marketNum) {
		this.marketNum = marketNum;
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
	public int getMb_num() {
		return mb_num;
	}
	public void setMb_num(int mb_num) {
		this.mb_num = mb_num;
	}
	public int getCt_num() {
		return ct_num;
	}
	public void setCt_num(int ct_num) {
		this.ct_num = ct_num;
	}
	public String getCt_name() {
		return ct_name;
	}
	public void setCt_name(String ct_name) {
		this.ct_name = ct_name;
	}
	
	
}
