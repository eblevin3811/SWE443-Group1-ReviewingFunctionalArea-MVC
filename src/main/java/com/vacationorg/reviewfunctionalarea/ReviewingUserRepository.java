package com.vacationorg.reviewfunctionalarea;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReviewingUserRepository extends 
	    CrudRepository<ReviewingUser, Long> {

			List<ReviewingUser> findByUserID(Long userID);

			Optional<ReviewingUser> findById(Long id);
		}