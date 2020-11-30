package com.payment.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.payment.demo.model.PaymentRowMapper;
import com.payment.demo.model.RequestModel;
import com.payment.demo.model.ResponseModel;

@Component
public class PaymentRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void createTableIfNotExists() {
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS payment(ID INT NOT NULL AUTO_INCREMENT, AMOUNT VARCHAR2(20),"
				+ "CURRENCY VARCHAR2(3), CARDTYPE VARCHAR2(10), CARDNUMBER VARCHAR2(100), EXPIRATIONMONTH VARCHAR2(2),"
				+ "EXPIRATIONYEAR VARCHAR2(4), CVV VARCHAR2(3), STATUS VARCHAR2(10) DEFAULT 'Success', AUTHCODE VARCHAR2(20), "
				+ "TIMING TIMESTAMP)");
	}
	
	public void saveData(RequestModel reqPayload, String authCode, String currentTime) {
		jdbcTemplate.execute("INSERT INTO PAYMENT(AMOUNT, CURRENCY, CARDTYPE, CARDNUMBER, EXPIRATIONMONTH, EXPIRATIONYEAR, CVV, "
				+ "AUTHCODE, TIMING)"
				+ "VALUES ('"+ reqPayload.getAmount() +"','"+ reqPayload.getCurrency() +"','"+ reqPayload.getType() +"',"
						+ "'"+ reqPayload.getCard().getNumber() +"','"+ reqPayload.getCard().getExpirationMonth() +"',"
						+ "'"+ reqPayload.getCard().getExpirationYear() +"','"+ reqPayload.getCard().getCvv() +"', "
								+ "'"+ authCode +"','"+ currentTime +"')");
	}
	
	public List<ResponseModel> getData() {
		return jdbcTemplate.query("SELECT * from PAYMENT", new PaymentRowMapper());
	}
}
