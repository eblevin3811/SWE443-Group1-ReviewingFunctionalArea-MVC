package com.example.reviewfunctionalarea;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Review{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long reviewId;

    private Long scheduledProperty;

	private long userID;
    private Date lastEdit;
    private String comment;
    private int rating;

    public Review(){}

	public Review(Long property, long userID, Date lastEdit, String comment, int rating, long reviewId) {
	    this.scheduledProperty = property;
        this.userID = userID;
        this.lastEdit = lastEdit;
        this.comment = comment;
        this.rating = rating;
        this.reviewId = reviewId;
	}

    public Long getId(){
        return this.id;
    }

    public Long getReviewId(){
        return this.reviewId;
    }

    public void setReviewId(long reviewId){
        this.reviewId = reviewId;
    }

    public void setScheduledProperty(Long scheduledProperty){
        this.scheduledProperty = scheduledProperty;
    }

    public Long getScheduledProperty(){
        return this.scheduledProperty;
    }

    public long getUserID(){
        return this.userID;
    }

    public void setUserID(long userID){
        this.userID = userID;
    }

    public void setRating(int rating){
        this.rating = rating;
    }
	
    public int getRating(){
        return this.rating;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public Date getLastEdit(){
        return this.lastEdit;
    }

    public void setLastEdit(Date lastEdit){
        this.lastEdit = lastEdit;
    }
}
