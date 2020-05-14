package com.jzkj.gyxg.exception;

public class AjaxOperationFailException extends Exception {
	private String errorMessage = "";
	
	public AjaxOperationFailException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}