package com.example.reviewfunctionalarea;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends 
	    CrudRepository<Review, Long> {

			List<Review> findByReviewId(Long reviewId);

			List<Review>findByUserID(Long userID);

			Optional<Review> findById(Long id);
		}