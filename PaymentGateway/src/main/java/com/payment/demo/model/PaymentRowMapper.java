package com.payment.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PaymentRowMapper implements RowMapper<ResponseModel> {
	
	@Override
	public ResponseModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResponseModel resp = new ResponseModel();
		CardResponseModel card = new CardResponseModel();
		card.setNumber(rs.getString("CARDNUMBER"));
		
		resp.setAmount(Integer.parseInt(rs.getString("AMOUNT")));
		resp.setCurrency(rs.getString("CURRENCY"));
		resp.setCard(card);
		resp.setType(rs.getString("CARDTYPE"));
		resp.setStatus(rs.getString("STATUS"));
		resp.setAuthorization_code(rs.getString("AUTHCODE"));
		resp.setTime(rs.getTime("TIMING").toString());
		// TODO Auto-generated method stub
		return resp;
	}
	
}
