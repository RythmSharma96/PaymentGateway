package com.payment.demo.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class RequestModel {
	private int amount;
	@NotNull(message="Currency cannot be null")
	private String Currency;
	@NotNull(message="Card Type cannot be null")
	private String type;
	@NotNull(message="Card Details cannnot be null")
	private CardRequestModel card;
	
	@AssertTrue(message="Card type must be debit or credit")
	private boolean isDebitOrCredit() { //only credit and debit cards are allowed
		if(type == null)
			return false;
	    if (type.toLowerCase().equals("debitcard") || type.toLowerCase().equals("creditcard"))
	    	return true;
	    return false;
	 }
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CardRequestModel getCard() {
		return card;
	}
	public void setCard(CardRequestModel card) {
		this.card = card;
	} 
	
}
