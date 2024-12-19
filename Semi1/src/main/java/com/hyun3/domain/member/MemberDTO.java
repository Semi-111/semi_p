package com.hyun3.domain.member;

public class MemberDTO {
	private Long mb_Num; // 회원번호
	private String userId; // 아이디
	private String pwd; // 비밀번호
	private String name; // 이름
	private String nickName; // 닉네임
	private int block; // 로그인가능 여부
	private String role; // 회원권한
	private String ca_Day; // 회원 가입일
	private String modifyDay; // 회원 정보 수정날짜
	private String email; // 이메일
	private String tel; // 전화번호
	private String birth; // 생일

	private int studentNum; // 학번(수정)

	private int lessonNum; // 이 부분 추가

	private String lessonName;
	
	
	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCa_Day() {
		return ca_Day;
	}

	public void setCa_Day(String ca_Day) {
		this.ca_Day = ca_Day;
	}

	public String getModifyDay() {
		return modifyDay;
	}

	public void setModifyDay(String modifyDay) {
		this.modifyDay = modifyDay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public int getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}

	public int getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(int lessonNum) {
		this.lessonNum = lessonNum;
	}

}