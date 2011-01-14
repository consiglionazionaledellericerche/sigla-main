package it.cnr.contab.cmis.service;

import it.cnr.jada.DetailedRuntimeException;

public class TypeNotFoundException extends DetailedRuntimeException {
	private static final long serialVersionUID = 1L;

	protected TypeNotFoundException() {
		super();
	}

	protected TypeNotFoundException(String s, Throwable throwable) {
		super("Type not found: "+s, throwable);
	}

	protected TypeNotFoundException(String s) {
		super("Type not found: "+s);
	}

	protected TypeNotFoundException(Throwable throwable) {
		super(throwable);
	}

}
