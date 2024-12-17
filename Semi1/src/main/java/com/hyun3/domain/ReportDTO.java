package com.hyun3.domain;

public class ReportDTO {
	private long RP_num; // 신고 번호 => 시퀀스
	private String RP_title; // 신고 제목
	private String RP_content; // 신고내용
	private String RP_reason; // 신고사유(음란물/욕설 등등..)
	private String RP_table; // 게시판 분류(어디 게시판인지. 학과게시판/장터게시판 등등
	private String RP_url; // 신고 게시판 번호 
	private long mb_num; // 회원번호
	
	private String memberName; // 신고자 이름 (조인용)
    private String reportDate; // 신고일자 
    private String status;     // 처리상태
	
    private String postTitle; // 신고글의 제목
    private String postContent; //신고글의 내용
    private String postWriter; // 신고글의 작성자
    
    private long postWriterNum; // 신고당한 사람의 회원번호
    
    private Long targetNum;      // 신고당한 게시글 번호
    private Long targetMbNum;    // 신고당한 회원 번호
    
	public Long getTargetNum() {
		return targetNum;
	}
	public void setTargetNum(Long targetNum) {
		this.targetNum = targetNum;
	}
	public Long getTargetMbNum() {
		return targetMbNum;
	}
	public void setTargetMbNum(Long targetMbNum) {
		this.targetMbNum = targetMbNum;
	}
	public long getPostWriterNum() {
		return postWriterNum;
	}
	public void setPostWriterNum(long postWriterNum) {
		this.postWriterNum = postWriterNum;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public String getPostWriter() {
		return postWriter;
	}
	public void setPostWriter(String postWriter) {
		this.postWriter = postWriter;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getRP_num() {
		return RP_num;
	}
	public void setRP_num(long rP_num) {
		RP_num = rP_num;
	}
	public String getRP_title() {
		return RP_title;
	}
	public void setRP_title(String rP_title) {
		RP_title = rP_title;
	}
	public String getRP_content() {
		return RP_content;
	}
	public void setRP_content(String rP_content) {
		RP_content = rP_content;
	}
	public String getRP_reason() {
		return RP_reason;
	}
	public void setRP_reason(String rP_reason) {
		RP_reason = rP_reason;
	}
	public String getRP_table() {
		return RP_table;
	}
	public void setRP_table(String rP_table) {
		RP_table = rP_table;
	}
	public String getRP_url() {
		return RP_url;
	}
	public void setRP_url(String rP_url) {
		RP_url = rP_url;
	}
	public long getMb_num() {
		return mb_num;
	}
	public void setMb_num(long mb_num) {
		this.mb_num = mb_num;
	}
	
	
}
