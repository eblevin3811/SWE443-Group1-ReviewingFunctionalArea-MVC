package com.vacationorg.reviewfunctionalarea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service("reviewService")
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewingUserRepository reviewingUserRepository;

    @Autowired
    PropertyUnderReviewRepository propertyUnderReviewRepository;
    
    //Save
    public void saveReview (Review review){
        reviewRepository.save(review);
    }

    public void saveReviewingUser (ReviewingUser reviewingUser){
        reviewingUserRepository.save(reviewingUser);
    }

    public void savePropertyUnderReview(PropertyUnderReview propertyUnderReview){
        propertyUnderReviewRepository.save(propertyUnderReview);
    }

    //Fetch
    public ArrayList<Review> prepareReviewList(long uid){
        return (ArrayList<Review>)reviewRepository.findByUserID(uid);
    }

    public ReviewingUser findUser(long uid){
        ReviewingUser returnedUser = reviewingUserRepository.findByUserID(uid).get(0);
        return returnedUser;
    }

    public Review findReview(long rid){
        List<Review> list = reviewRepository.findByReviewId(rid);

        if (list.size() > 0){
            return list.get(0);
        }

        return null;
    }
}