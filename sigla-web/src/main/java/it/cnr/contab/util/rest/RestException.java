package it.cnr.contab.util.rest;

import javax.ws.rs.core.Response.Status;

public class RestException extends RuntimeException{
	private Status status;
	public RestException(Status status, String message) {
		super(message);
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}
}
