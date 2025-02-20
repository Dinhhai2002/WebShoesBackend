package com.shoes.webshoes.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;

public class StoreProcedureListResult<T> {
	private int statusCode;
	private String messageError;
	private List<T> result;
	private long totalRecord;
	private BigDecimal amount;
	private BigDecimal discountAmount;
	private BigDecimal vatAmount;
	private BigDecimal totalAmount;

	public StoreProcedureListResult(int statusCode, String messageError) {
		this.statusCode = statusCode;
		this.messageError = messageError;
	}

	public StoreProcedureListResult(int statusCode, String messageError, List<T> result) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.result = result;
	}

	public StoreProcedureListResult(int statusCode, String messageError, long totalRecord, List<T> result) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.totalRecord = totalRecord;
		this.result = result;
	}

	public StoreProcedureListResult(int statusCode, String messageError, BigDecimal totalAmount, List<T> result) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.totalAmount = totalAmount;
		this.result = result;
	}

	public StoreProcedureListResult(int statusCode, String messageError, long totalRecord) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.totalRecord = totalRecord;
	}

	public StoreProcedureListResult(int statusCode, String messageError, long totalRecord, BigDecimal amount,
			BigDecimal discountAmount, BigDecimal vatAmount, BigDecimal totalAmount, List<T> result) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.totalRecord = totalRecord;
		this.amount = amount;
		this.discountAmount = discountAmount;
		this.vatAmount = vatAmount;
		this.totalAmount = totalAmount;
		this.result = result;
	}

	public StoreProcedureListResult(int statusCode, String messageError, long totalRecord, BigDecimal totalAmount,
			List<T> result) {
		this.statusCode = statusCode;
		this.messageError = messageError;
		this.totalRecord = totalRecord;
		this.totalAmount = totalAmount;
		this.result = result;
	}

	public StoreProcedureListResult(List<T> result, long totalRecord) {
		this.setResult(result);
		this.setTotalRecord(totalRecord);
	}

	public StoreProcedureListResult(List<T> result) {
		this.setResult(result);
	}

	public StoreProcedureListResult() {
		this.result = new ArrayList<T>();
		this.totalRecord = 0;
	}

	/**
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * @return the totalRecord
	 */
	public long getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public boolean isSuccess() {
		return this.getStatusCode() == StoreProcedureStatusCodeEnum.SUCCESS.getValue();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
