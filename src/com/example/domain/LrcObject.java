package com.example.domain;

public class LrcObject {
	
	public LrcObject() {
	
	}
	
	public LrcObject(Integer startTime,String content) {
		super();
		this.startTime = startTime;
		this.content = content;
	}
	
	public LrcObject(Integer startTime, Integer endTime, String content) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.content = content;
	}
	public Integer getStartTime() {
		return startTime;
	}
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private  Integer startTime;
	private  Integer endTime;
	private String content;
	@Override
	public String toString() {
		return "LrcObject [startTime=" + startTime + ", endTime=" + endTime
				+ ", content=" + content + "]";
	}
	
	

}
