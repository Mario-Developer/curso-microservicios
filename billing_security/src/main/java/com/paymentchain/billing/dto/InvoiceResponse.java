/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author sotobotero
 */
@Schema(name = "InvoiceResponse", description = "Model represent a invoice on database")
public class InvoiceResponse {

	@Schema(name = "invoiceId", required = true, example = "2", defaultValue = "1", description = "Unique Id of iinvoice  on database")
	private long invoiceId;
	@Schema(name = "customer", required = true, example = "2", defaultValue = "1", description = "Unique Id of customer that represent the owner of invoice")
	private long customer;
	@Schema(name = "number", required = true, example = "3", defaultValue = "8", description = "Number given on fisical invoice")
	private String number;
	private String detail;
	private double amount;
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public long getCustomer() {
		return customer;
	}
	public void setCustomer(long customer) {
		this.customer = customer;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

}
