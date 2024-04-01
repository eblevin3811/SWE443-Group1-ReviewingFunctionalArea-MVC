package com.example.handlingformsubmission;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends 
	    CrudRepository<Review, Long> {

			List<Review> findByReviewId(Long reviewId);

			Optional<Review> findById(Long id);
		}