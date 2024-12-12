package com.hyun3.domain;

public class LessonReplyDTO {
    private long co_num;     // 댓글 번호
    private long cm_num;        // 게시글 번호
    private long mb_num;        // 회원 번호
    private String co_content;  // 댓글 내용
    private String reg_date;    // 등록일
    private String nickName;    // 작성자 닉네임
    private long parentNum;     // 부모 댓글 번호
    private int answerCount;    // 답글 개수
    
	public long getCo_num() {
		return co_num;
	}
	public void setCo_num(long co_num) {
		this.co_num = co_num;
	}
	public long getCm_num() {
		return cm_num;
	}
	public void setCm_num(long cm_num) {
		this.cm_num = cm_num;
	}
	public long getMb_num() {
		return mb_num;
	}
	public void setMb_num(long mb_num) {
		this.mb_num = mb_num;
	}
	public String getCo_content() {
		return co_content;
	}
	public void setCo_content(String co_content) {
		this.co_content = co_content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public long getParentNum() {
		return parentNum;
	}
	public void setParentNum(long parentNum) {
		this.parentNum = parentNum;
	}
	public int getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}
    
    
}