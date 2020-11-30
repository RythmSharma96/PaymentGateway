package com.payment.demo;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PaymentUtility {
	
	@Autowired
	private Environment env;

	public String getRandom(){
		//generate a random 8 digit num to add to auth code
		Random r = new Random();
		return String.format("%08d", r.nextInt(100000000));
	}
	
	public boolean validate(String uid){
		//This method will validate the user token and return false if it does not match
		if(uid != null) {
			if(uid.equals(env.getProperty("authtoken")))
				return true;
		}
		return false;
	}
	
}
