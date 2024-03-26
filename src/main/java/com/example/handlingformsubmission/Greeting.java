package com.example.handlingformsubmission;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Greeting {

	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
		private Long sid;
	private String content;

	  protected Greeting() {}

	  public Greeting(Long sid, String content) {
	    this.sid = sid;
	    this.content = content;
	  }
	  
	  @Override
	  public String toString() {
	    return String.format(
	        "Greeting[id=%d, sid=%d, content='%s']",
	        id, sid, content);
	  }

	public Long getId() {
		return id;
	}

	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
