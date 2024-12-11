package com.hyun3.domain;

public class SessionInfo {
	private Long mb_num; // 회원번호
	private String userId;
	private String name;
	private int role;
	private int nickName;

	public Long getMb_Num() {
		return mb_num;
	}

	public void setMb_Num(Long mb_Num) {
		this.mb_num = mb_Num;
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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getNickName() {
		return nickName;
	}

	public void setNickName(int nickName) {
		this.nickName = nickName;
	}
}
