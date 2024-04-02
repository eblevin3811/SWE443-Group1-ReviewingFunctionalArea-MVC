package com.example.handlingformsubmission;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class ReviewingUser{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
	private String name;
    private Long userID;

    @OneToMany(mappedBy="userID", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<PropertyUnderReview> propertyList;

    public ReviewingUser(){}

	public ReviewingUser(String name, ArrayList<Review> reviewList, List<PropertyUnderReview> propertyList, long userID) {
	    this.name = name;
        this.reviewList = reviewList;
        this.propertyList = propertyList;
        this.userID = userID;
	}

    public long getId(){
        return this.id;
    }

    public long getUserID(){
        return this.userID;
    }

    public void setUserID(long userID){
        this.userID = userID;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
	  
	public List<Review> getReviewList(){
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList){
        this.reviewList = reviewList; 
    }

    public void setPropertyList(ArrayList<PropertyUnderReview> propertyList){
        this.propertyList = propertyList;
    }

    public List<PropertyUnderReview> getPropertyList(){
        return this.propertyList;
    }

	public Review findReviewByID(long rID){
        Iterator<Review> itr = reviewList.iterator();

        while (itr.hasNext()){
            Review review = itr.next();
            if (review.getId() == rID){
                return review;
            }
        }
        return null;
    }

    public void addReview(Review review){
        reviewList.add(review);
    }

    private void updateReviews(){
        //TODO: implement once database added
    }

}
