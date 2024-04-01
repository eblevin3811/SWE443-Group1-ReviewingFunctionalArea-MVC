package com.example.handlingformsubmission;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReviewCommentRepository extends 
	    CrudRepository<ReviewComment, Long> {

			List<ReviewComment> findByContent(String content);

			Optional<ReviewComment> findById(Long id);
		}