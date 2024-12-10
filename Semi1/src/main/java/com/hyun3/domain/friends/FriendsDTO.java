package com.hyun3.domain.friends;

public class FriendsDTO {

	private int FR_num;
	private String situation;
	private String FR_requestday;
	private int resp_MBnum;
	private int req_MBnum;
	
	public int getFR_num() {
		return FR_num;
	}
	public void setFR_num(int fR_num) {
		FR_num = fR_num;
	}
	public String getSituation() {
		return situation;
	}
	public void setSituation(String situation) {
		this.situation = situation;
	}
	public String getFR_requestday() {
		return FR_requestday;
	}
	public void setFR_requestday(String fR_requestday) {
		FR_requestday = fR_requestday;
	}
	public int getResp_MBnum() {
		return resp_MBnum;
	}
	public void setResp_MBnum(int resp_MBnum) {
		this.resp_MBnum = resp_MBnum;
	}
	public int getReq_MBnum() {
		return req_MBnum;
	}
	public void setReq_MBnum(int req_MBnum) {
		this.req_MBnum = req_MBnum;
	}
	
}
