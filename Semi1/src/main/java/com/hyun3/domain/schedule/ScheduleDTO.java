package com.hyun3.domain.schedule;

public class ScheduleDTO {
    private int stGrade; // 과목의 학년
    private String gradee; // 학생의 학년
    private String stGradee; // 학생의 학기
	private String sbNum; // 과목 번호
    private String sbName; // 과목 이름
    private int hakscore; // 학점
    private String studytime; // 수업 시간 
    private String studyDay; // 수업 요일
    private String color; // 과목 색깔
    private String timetableNum; // 시간표 번호... 이제 필요 없을 듯?
    
    private String score; // 성적(null)
    
    private String memberId; // 회원 번호. 로그인 세션으로 가져와야함.

    public String getGradee() {
		return gradee;
	}

	public void setGradee(String gradee) {
		this.gradee = gradee;
	}

	public String getScore() {
		return score;
	}
    
	public void setScore(String score) {
		this.score = score;
	}
	public String getStGradee() {
    	return stGradee;
    }
    public void setStGradee(String stGradee) {
    	this.stGradee = stGradee;
    }

    public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
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
	public int getStGrade() {
		return stGrade;
	}
	public void setStGrade(int stGrade) {
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
