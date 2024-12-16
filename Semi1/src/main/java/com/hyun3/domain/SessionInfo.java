package com.hyun3.domain;

public class SessionInfo {
	private Long mb_Num; // 회원번호
	private String userId;
	private String name;
	private String role;
	private String nickName;
	private int lessonNum;
	

	public int getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(int lessonNum) {
		this.lessonNum = lessonNum;
	}

	public Long getMb_Num() {
		return mb_Num;
	}

	public void setMb_Num(Long mb_Num) {
		this.mb_Num = mb_Num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
