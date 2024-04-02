package com.example.handlingformsubmission;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ReviewController {

	boolean init = false;

	@Autowired
	private ReviewService service;

	@GetMapping("/reviews/{uid}")
	public String listReviews(@PathVariable("uid") long userid, Model model) {
		
		/* SAMPLE HARD-CODED FOR SPRINT 4 DEMO */
		if (!init){
			ReviewingUser demoUser = new ReviewingUser("user123", null, null, userid);
		
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, 2024);
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date start = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, 7);
			Date end = calendar.getTime();
			PropertyUnderReview demoProp = new PropertyUnderReview(123, "Demo property", "Demoland", start, end);
			PropertyUnderReview demoProp2 = new PropertyUnderReview(456, "Demo property 2", "Somewhere else", start, end);
			
			Review demoReview = new Review(demoProp.getPropertyID(), demoUser.getUserID(), new Date(), "It was pretty good.", 4, 123);

			ArrayList<Review> demoReviewList = new ArrayList<>();
			ArrayList<PropertyUnderReview> demoPropList = new ArrayList<>();
			demoReviewList.add(0, demoReview);
			demoPropList.add(0, demoProp);
			demoPropList.add(demoProp2);

			demoUser.setReviewList(demoReviewList);
			demoUser.setPropertyList(demoPropList);

			service.savePropertyUnderReview(demoProp);
			service.saveReviewingUser(demoUser);
			service.saveReview(demoReview);

			model.addAttribute("user", demoUser);
			model.addAttribute("properties", demoUser.getPropertyList());
			model.addAttribute("reviews", demoUser.getReviewList());

			init = true;
		}
		else {
			ReviewingUser user = service.findUser(userid);
			List<Review> reviews = service.prepareReviewList(userid);
			List<PropertyUnderReview> properties = user.getPropertyList();

			model.addAttribute("user", user);
			model.addAttribute("properties", properties);
			model.addAttribute("reviews", reviews);
		}

		return "reviews";
	}
	

	@GetMapping("/editreview/{uid}/{reviewID}")
	public String editReviewForm(@PathVariable("uid") long uid, @PathVariable("reviewID") long reviewId, Model model){
		

		//Get user first
		ReviewingUser user = service.findUser(uid);

		//Get review
		Review review = service.findReview(reviewId);

		//Populate model
		model.addAttribute("title", "Edit Review");
		model.addAttribute("user", user);
		model.addAttribute("review", review);


		return "reviewform";
	}

	@PostMapping("/submiteditedreview/{uid}/{reviewid}")
	public String submitEditedReview(@ModelAttribute("review") Review review, @PathVariable("uid") long uid, @PathVariable("reviewid") long reviewid, Model model) {

		ReviewingUser user = service.findUser(uid);
		Review oldReview = service.findReview(reviewid);

		oldReview.setComment(review.getComment());
		if (review.getRating() != 0){
			oldReview.setRating(review.getRating());
		}
		oldReview.setLastEdit(new Date());
		service.saveReview(oldReview);

		List<Review> reviews = service.prepareReviewList(uid);
		
		List<PropertyUnderReview> properties = user.getPropertyList();

		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("properties", properties);

		return "reviews";
	}

	@GetMapping("/leavereview/{uid}/{propertyId}")
	public String leaveReviewForm(@PathVariable("uid") long uid, @PathVariable("propertyId") long propertyId, Model model){
		
		//Get user first
		ReviewingUser user = service.findUser(uid);

		//New review
		Review review = new Review();

		//Populate model
		model.addAttribute("propertyId", propertyId);
		model.addAttribute("title", "Leave a Review");
		model.addAttribute("user", user);
		model.addAttribute("review", review);


		return "reviewform";
	}

	@PostMapping("/submitnewreview/{uid}/{propertyId}")
	public String submitNewReview(@ModelAttribute("review") Review review, @PathVariable("uid") long uid, @PathVariable("propertyId") long propertyId, Model model) {

		ReviewingUser user = service.findUser(uid);

		//Generate appropriate review ID
		long rID = (long)Math.random();
		while (service.findReview(rID) != null){
			rID += 1;
		}
		Review newReview = new Review();
		newReview.setComment(review.getComment());
		newReview.setLastEdit(new Date());
		newReview.setRating(review.getRating());
		newReview.setReviewId(rID);
		newReview.setScheduledProperty(propertyId);
		newReview.setUserID(uid);
		service.saveReview(newReview);

		//Add review to list
		List<Review> userReviewsList = user.getReviewList();
		userReviewsList.add(newReview);
		user.setReviewList(userReviewsList);
		service.saveReviewingUser(user);

		List<Review> reviews = service.prepareReviewList(uid);
		List<PropertyUnderReview> properties = user.getPropertyList();

		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("properties", properties);

		return "reviews";
	}
	

	/* 
	@PostMapping("/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
		model.addAttribute("greeting", greeting);
		return "result";
	}
	*/

}
