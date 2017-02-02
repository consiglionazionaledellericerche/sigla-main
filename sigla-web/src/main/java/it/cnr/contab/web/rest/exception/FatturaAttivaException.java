package it.cnr.contab.web.rest.exception;

import java.io.Serializable;
import java.util.Map;

import it.cnr.contab.web.rest.config.FatturaAttivaCodiciEnum;

import javax.ws.rs.core.Response.Status;

public class FatturaAttivaException extends RestException {
	private static final long serialVersionUID = 1L;
	private final FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum;
	
	public FatturaAttivaException(Status status, String message,
			FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum) {
		super(status, message);
		this.fatturaAttivaCodiciEnum = fatturaAttivaCodiciEnum;
	}
	
	public static FatturaAttivaException newInstance(Status status, FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum) {
		return new FatturaAttivaException(status, fatturaAttivaCodiciEnum.getMessage(),fatturaAttivaCodiciEnum);
	}
	
	@Override
	public Map<String, Serializable> getErrorMap() {
		return fatturaAttivaCodiciEnum.getErrorMap();
	}
}
