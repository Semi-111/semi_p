package com.hyun3.domain.academy;

public class LectureReviewDTO {
    private long mb_Num;        // 회원 번호
    private String userId;      // 아이디
    private String nickName;    // 닉네임
    
    private long sb_Num;        // 과목 번호
    private String sb_Name;     // 과목명
    private String pf_Name;     // 교수 이름
    
    private String semester;    // 학기
    private String content;		// 리뷰 내용 
    private String reg_date;	// 작성일
    private int rating;			// 평가 점수
    
    
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
	public long getSb_Num() {
		return sb_Num;
	}
	public void setSb_Num(long sb_Num) {
		this.sb_Num = sb_Num;
	}
	public String getSb_Name() {
		return sb_Name;
	}
	public void setSb_Name(String sb_Name) {
		this.sb_Name = sb_Name;
	}
	public String getPf_Name() {
		return pf_Name;
	}
	public void setPf_Name(String pf_Name) {
		this.pf_Name = pf_Name;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
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
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
    
    
    
}
