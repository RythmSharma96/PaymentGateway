package com.payment.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.demo.PaymentUtility;
import com.payment.demo.model.RequestModel;
import com.payment.demo.model.ResponseModel;
import com.payment.demo.repository.PaymentRepository;

@RestController
@Validated
public class PaymentController {
	
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired 
	private PaymentUtility paymentUtility;
	
	@PostMapping(path="/transaction", consumes={"application/json"})
	public ResponseEntity<ResponseModel> addTransaction(@Valid @RequestBody RequestModel reqPayload, @RequestHeader(name = "X-UID", required=false) String uid) {
		System.out.println("New Transaction posted..");
		
		ResponseModel respPayload = new ResponseModel();
		
		if(!paymentUtility.validate(uid)) {
		respPayload.setStatus("Unauthorized User");
		return new ResponseEntity<ResponseModel>(respPayload, HttpStatus.FORBIDDEN);	
		}
		
		//As we are using in memory database, need to check if table is created or not 
		//(Creation of table could also be done in a standalone API to reduce DB calls on backend)
		try {
			paymentRepository.createTableIfNotExists();
			
			String authCode = "SDSD" + paymentUtility.getRandom();
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String currentTime = sdf.format(date);
			//table created; need to save data now
			paymentRepository.saveData(reqPayload, authCode, currentTime);
			
			//successfully executed; now return response
			respPayload = objectMapper.convertValue(reqPayload, ResponseModel.class);
			respPayload.setAuthorization_code(authCode);
			respPayload.setStatus("Success");
			respPayload.setTime(currentTime);
			
		}catch(Exception e){
			//need to send error response in case there is an error saving to DB
			respPayload.setStatus("error in request");
			e.printStackTrace();
			return new ResponseEntity<ResponseModel>(respPayload, HttpStatus.BAD_REQUEST);
		}
		
		//need to return response here
		return new ResponseEntity<ResponseModel>(respPayload, HttpStatus.OK);
	}
	
	@GetMapping(path="/transaction", produces={"application/json"})
	public ResponseEntity<List<ResponseModel>> getTransaction(@RequestHeader(name = "X-UID", required=false) String uid) {
		System.out.println("Getting all Transactions...");
		
		List<ResponseModel> allresp = new ArrayList<ResponseModel>();
		
		if(!paymentUtility.validate(uid)) {
			ResponseModel respPayload = new ResponseModel();
			respPayload.setStatus("Unauthorized User");
			allresp.add(respPayload);
			return new ResponseEntity<List<ResponseModel>>(allresp,HttpStatus.FORBIDDEN);
		}
		
		try {
		allresp.addAll(paymentRepository.getData());
		}
		catch (Exception e) { //in case table is not created yet
		ResponseModel respPayload = new ResponseModel();
		respPayload.setStatus("Table not created yet, cannot fetch values");
		allresp.add(respPayload);
		e.printStackTrace();
		return new ResponseEntity<List<ResponseModel>>(allresp, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<ResponseModel>>(allresp, HttpStatus.OK);
	}
}
