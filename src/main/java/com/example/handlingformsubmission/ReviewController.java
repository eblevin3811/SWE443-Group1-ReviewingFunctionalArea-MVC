package com.example.handlingformsubmission;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {

	

	@GetMapping("/reviews")
	public String listReviews(Model model) {
		ReviewingUser demoUser = new ReviewingUser("user123", null, null, 123);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2024);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Date start = calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH, 7);
		Date end = calendar.getTime();

		PropertyUnderReview demoProp = new PropertyUnderReview(123, "Demo property", "Demoland", start, end);
		
		ArrayList<ReviewComment> list = new ArrayList<>();
		
		Review demoReview = new Review(demoProp, demoUser.getUserID(), new Date(), list, 4, 123);
		ReviewComment demoComment = new ReviewComment("It was fine.", new Date(), demoReview.getReviewId());
		list.add(demoComment);

		demoReview.setComments(list);

		ArrayList<Review> demoReviewList = new ArrayList<>();
		ArrayList<PropertyUnderReview> demoPropList = new ArrayList<>();
		demoReviewList.add(0, demoReview);
		demoPropList.add(0, demoProp);

		demoUser.setReviewList(demoReviewList);
		demoUser.setPropertyList(demoPropList);
		
		model.addAttribute("username", demoUser.getName());
		model.addAttribute("properties", demoUser.getPropertyList());
		model.addAttribute("reviews", demoUser.getReviewList());

		return "reviews";
	}
	/*

	@GetMapping("/editreview/{reviewID}")
	public String editReviewForm(Model model){
		model.addAttribute("title", "Edit Review");
		return "reviewform";
	}

	@PostMapping("/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
		model.addAttribute("greeting", greeting);
		return "result";
	}*/

}
