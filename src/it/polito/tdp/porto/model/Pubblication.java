package it.polito.tdp.porto.model;

public class Pubblication {
	
	public int authorid;
	public int eprintid;
	
	public Pubblication(int authorid, int eprintid) {
		this.authorid = authorid;
		this.eprintid = eprintid;
	}
	public int getAuthorid() {
		return authorid;
	}
	public int getEprintid() {
		return eprintid;
	}
	
	
	
}
