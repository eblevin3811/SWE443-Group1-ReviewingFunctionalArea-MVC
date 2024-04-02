package com.example.reviewfunctionalarea;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PropertyUnderReviewRepository extends 
	    CrudRepository<PropertyUnderReview, Long> {

			List<PropertyUnderReview> findByPropertyID(Long propertyID);

			Optional<PropertyUnderReview> findById(Long id);
		}