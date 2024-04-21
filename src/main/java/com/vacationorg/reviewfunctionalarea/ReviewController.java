package com.vacationorg.reviewfunctionalarea;

import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations.Property;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Chars;
import org.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ReviewController {

	boolean init = false;

	final static String user = "Timmy"; //hard coded user

	ReviewingUser reviewingUser;

	@Autowired
	private ReviewService service;

	@GetMapping("/reviews/{uid}")
	public String listReviews(@PathVariable("uid") long userid, Model model) {
		
		/* simulating that the user is logged in... */
		if (!init){
			reviewingUser = new ReviewingUser(user, null, null, userid);
			service.saveReviewingUser(reviewingUser);

			init = true;
		}
		else {
			reviewingUser = service.findUser(userid);
		}

		//Get data from microservice data store...
		JSONArray respjson = null;
		String url = "http://localhost:8092/prepareReviewList/" + Long.toString(userid);
		ArrayList<Review> reviews = new ArrayList<Review>(); //This user's reviews
		ArrayList<PropertyUnderReview> properties = new ArrayList<PropertyUnderReview>();
		try{
			respjson = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));

			//Parse json array
			for (int i = 0; i < respjson.length(); i++){
				JSONObject obj = respjson.getJSONObject(i);
				

				//The structure:
				//PropertyID

				JSONObject propertyObject = obj.getJSONObject("property");
				//Property (object)
				//	propertyID
				//	propertyLocation
				//	propertyName
				//	startDate
				//	endDate
				long propertyID = propertyObject.getLong("propertyID");
				String propertyLocation = propertyObject.getString("propertyLocation");
				String propertyName = propertyObject.getString("propertyName");

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String startString = propertyObject.getString("startDate");
				Date start = df.parse(startString);
				String endString = propertyObject.getString("endDate");
				Date end = df.parse(endString);		

				PropertyUnderReview property = new PropertyUnderReview(propertyID, propertyName, propertyLocation, start, end);
				properties.add(property);
				service.savePropertyUnderReview(property);

				JSONArray reviewArray = obj.getJSONArray("reviews");
				//reviews (array)
				//	reviewID
				//	userName
				//	userID
				//	propertyID
				//	comment
				//	date
				//	rating

				for (int j = 0; j < reviewArray.length(); j++){
					long reviewID = reviewArray.getJSONObject(j).getLong("reviewID");
					long propID = reviewArray.getJSONObject(j).getLong("propertyID");
					String comment = reviewArray.getJSONObject(j).getString("comment");
					String dateString = reviewArray.getJSONObject(j).getString("date");
					Date date = df.parse(dateString);
					int rating = reviewArray.getJSONObject(j).getInt("rating");

					//Create review using info
					Review newReview = new Review(propID, userid, date, comment, rating, reviewID);
					reviews.add(newReview);
					service.saveReview(newReview);
				}
			}

		} catch (JSONException e){System.out.println("JSON exception!");}
		catch (IOException e){System.out.println("IO exception!");}
		catch (ParseException e) {System.out.println("Parse exception!");}
		System.out.println(respjson.toString());

		//User is hard-coded as per professor's okay
		reviewingUser = new ReviewingUser("Timmy", null, null, userid);
		reviewingUser.setPropertyList(properties);
		reviewingUser.setReviewList(reviews);
		service.saveReviewingUser(reviewingUser);

		
		model.addAttribute("user", reviewingUser);
		model.addAttribute("properties", properties);
		model.addAttribute("reviews", reviews);

		return "reviews";
	}
	

	@GetMapping("/editreview/{uid}/{reviewID}")
	public String editReviewForm(@PathVariable("uid") long uid, @PathVariable("reviewID") long reviewId, Model model){

		reviewingUser = service.findUser(uid);
		
		//Get review from user fetched on last screen
		Review review = service.findReview(reviewId);

		//Populate model
		model.addAttribute("title", "Edit Review");
		model.addAttribute("user", reviewingUser);
		model.addAttribute("review", review);


		return "reviewform";
	}

	@PostMapping("/submiteditedreview/{uid}/{reviewid}")
	public RedirectView submitEditedReview(@ModelAttribute("review") Review review, @PathVariable("uid") long uid, @PathVariable("reviewid") long reviewid, Model model) {

		ReviewingUser user = service.findUser(uid);
		Review oldReview = service.findReview(reviewid);

		oldReview.setComment(review.getComment());
		if (review.getRating() != 0){
			oldReview.setRating(review.getRating());
		}
		oldReview.setLastEdit(new Date());
		service.saveReview(oldReview);

		//Send change back to microservice
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String stringDate = df.format(oldReview.getLastEdit());
		try{
			IOUtils.toString(new URL("http://localhost:8092/updateReview/" + reviewid + "/" + user.getName() + "/" + user.getUserID() + "/" + review.getComment().replaceAll(" ", "_") + "/" + Integer.toString(review.getRating()) + "/" + review.getScheduledProperty() + "/" + stringDate), Charset.forName("UTF-8"));
		} catch (Exception e){};

		return new RedirectView("/reviews/" + Long.toString(uid));
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
	public RedirectView submitNewReview(@ModelAttribute("review") Review review, @PathVariable("uid") long uid, @PathVariable("propertyId") long propertyId, Model model) {

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

		//Send change to microservice
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String stringDate = df.format(newReview.getLastEdit());
		try{
			IOUtils.toString(new URL("http://localhost:8092/updateReview/" + rID + "/" + user.getName() + "/" + user.getUserID() + "/" + newReview.getComment().replaceAll(" ", "_") + "/" + Integer.toString(newReview.getRating()) + "/" + newReview.getScheduledProperty() + "/" + stringDate), Charset.forName("UTF-8"));
		} catch (Exception e){};

		return new RedirectView("/reviews/" + Long.toString(uid));
	}
	

	/* 
	@PostMapping("/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
		model.addAttribute("greeting", greeting);
		return "result";
	}
	*/

}
