package com.hyun3.domain.sw;

public class ReplyDTO {
	private int co_num; // 댓글번호
	private String content; // 내용
	private String reg_date; // 등록일
	private long mb_num; // 회원번호
	private int cm_num; // 게시글 번호
	private String nickName;
	
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getCo_num() {
		return co_num;
	}
	public void setCo_num(int co_num) {
		this.co_num = co_num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public long getMb_num() {
		return mb_num;
	}
	public void setMb_num(long mb_num) {
		this.mb_num = mb_num;
	}
	public int getCm_num() {
		return cm_num;
	}
	public void setCm_num(int cm_num) {
		this.cm_num = cm_num;
	}
	
	
}
