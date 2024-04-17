package com.vacationorg.reviewfunctionalarea;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PropertyUnderReview{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long propertyID;
    private String propertyLocation;
    private String propertyName;
    private Date startDate;
    private Date endDate;

    public PropertyUnderReview(){}

	public PropertyUnderReview (long propertyID, String propertyName, String propertyLocation, Date startDate, Date endDate) {
	    this.propertyID = propertyID;
        this.propertyLocation = propertyLocation;
        this.propertyName = propertyName;
        this.startDate = startDate;
        this.endDate = endDate;
	}

    public Long getId(){
        return id;
    }
	
    public Long getPropertyID(){
        return this.propertyID;
    }

    public void setPropertyID(long id){
        this.propertyID = id;
    }

    public String getPropertyName(){
        return this.propertyName;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

    public String getPropertyLocation(){
        return this.propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation){
        this.propertyLocation = propertyLocation;
    }

    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }

    public Date getStartDate(){
        return this.startDate;
    }

    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    public Date getEndDate(){
        return this.endDate;
    }

}
