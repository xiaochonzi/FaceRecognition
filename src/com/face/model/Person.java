package com.face.model;

public class Person {
	private int id;
	private String personNo;
	private String personName;
	private String personImage;
	private String personCardId;
	private String personAddr;
	private String personLevel;
	private String date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPersonNo() {
		return personNo;
	}
	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonImage() {
		return personImage;
	}
	public void setPersonImage(String personImage) {
		this.personImage = personImage;
	}
	public String getPersonCardId() {
		return personCardId;
	}
	public void setPersonCardId(String personCardId) {
		this.personCardId = personCardId;
	}
	public String getPersonAddr() {
		return personAddr;
	}
	public void setPersonAddr(String personAddr) {
		this.personAddr = personAddr;
	}
	public String getPersonLevel() {
		return personLevel;
	}
	public void setPersonLevel(String personLevel) {
		this.personLevel = personLevel;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", personNo=" + personNo + ", personName=" + personName + ", personImage="
				+ personImage + ", personCardId=" + personCardId + ", personAddr=" + personAddr + ", personLevel="
				+ personLevel + ", date=" + date + "]";
	}
	
	
	
}
