package com.hyun3.domain;

public class MemberDTO {
  private Long mb_Num; // 회원번호
  private String userId; // 아이디
  private String pwd; // 비밀번호
  private String name; // 이름
  private String nickName; // 닉네임
  private int block; // 로그인가능 여부
  private int role; // 회원권한
  private int hak; // 학번
  private String ca_Day; // 회원 가입일
  private String modifyDay; // 회원 정보 수정날짜
  private String email; // 이메일
  private String tel; // 전화번호
  private String birth; // 생일


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

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public int getBlock() {
    return block;
  }

  public void setBlock(int block) {
    this.block = block;
  }

  public int getHak() {
    return hak;
  }

  public void setHak(int hak) {
    this.hak = hak;
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
}
