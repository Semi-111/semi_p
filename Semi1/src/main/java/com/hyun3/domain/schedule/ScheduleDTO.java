package com.hyun3.domain.schedule;

public class ScheduleDTO {
    private String stGrade; // 학년
    private String sbNum; // 과목 번호
    private String sbName; // 과목 이름
    private int hakscore; // 학점
    private String studytime; // 수업 시간 
    private String studyDay; // 수업 요일
    private String color;
    private String timetableNum; 
    
	public String getTimetableNum() {
		return timetableNum;
	}
	public void setTimetableNum(String timetableNum) {
		this.timetableNum = timetableNum;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getStudyDay() {
		return studyDay;
	}
	public void setStudyDay(String studyDay) {
		this.studyDay = studyDay;
	}
	public String getStGrade() {
		return stGrade;
	}
	public void setStGrade(String stGrade) {
		this.stGrade = stGrade;
	}
	public String getSbNum() {
		return sbNum;
	}
	public void setSbNum(String sbNum) {
		this.sbNum = sbNum;
	}
	public String getSbName() {
		return sbName;
	}
	public void setSbName(String sbName) {
		this.sbName = sbName;
	}
	public int getHakscore() {
		return hakscore;
	}
	public void setHakscore(int hakscore) {
		this.hakscore = hakscore;
	}
	public String getStudytime() {
		return studytime;
	}
	public void setStudytime(String studytime) {
		this.studytime = studytime != null ? studytime : "시간 없음";
	}
}
