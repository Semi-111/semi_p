package com.hyun3.domain.academy;

public class GradePointDTO {
	private long mb_Num; // 회원번호
	private String userId; // 아이디
	private String nickName; // 닉네임
	private String name; // 회원 이름
	
	private long sb_Num; // 과목 번호 
	private String sb_Name; // 과목명
	private String pf_Name; // 교수 이름
	
	private String semester; // 학기
	private String grade_year; // 학년
	private int hakscore; // 학점(1,2,3학점)
	
	private String grade; // 성적(A+, A, B)
	private double gradePoint; // 평균학점(4.5)
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getGrade_year() {
		return grade_year;
	}
	public void setGrade_year(String grade_year) {
		this.grade_year = grade_year;
	}
	public int getHakscore() {
		return hakscore;
	}
	public void setHakscore(int hakscore) {
		this.hakscore = hakscore;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public double getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(double gradePoint) {
		this.gradePoint = gradePoint;
	}
	
	

}
