package com.example.handlingformsubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service("greetingService")
public class GreetingService {
    @Autowired
    GreetingRepository grepository;
    
public void saveGreeting (Greeting newGreeting)
{
	
	grepository.save(newGreeting);
	
}
}