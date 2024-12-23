package com.hyun3.domain.academy;

public class GradePointDTO {
	private long mb_Num; // 회원번호
	private String userId; // 아이디
	private String nickName; // 닉네임
	private String name; // 회원 이름
	
	private long sb_Num; // 과목 번호 
	private String sb_Name; // 과목명
	// private String pf_Name; // 교수 이름
	
	private int semester; // 학기
	private int grade_year; // 학년
	private int hakscore; // 학점(1,2,3학점)
	
	private String grade; // 성적(A+, A, B)
	private double gradePoint; // 평균학점(4.5)
	private int gradeCount; // 성적별 갯수
	
	private long at_Num; // 수강번호
	private int totalHakscore; // 취득학점
	
	
	public int getTotalHakscore() {
		return totalHakscore;
	}
	public void setTotalHakscore(int totalHakscore) {
		this.totalHakscore = totalHakscore;
	}
	public long getAt_Num() {
		return at_Num;
	}
	public void setAt_Num(long at_Num) {
		this.at_Num = at_Num;
	}
	public int getGradeCount() {
		return gradeCount;
	}
	public void setGradeCount(int gradeCount) {
		this.gradeCount = gradeCount;
	}
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
	
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public int getGrade_year() {
		return grade_year;
	}
	public void setGrade_year(int grade_year) {
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
