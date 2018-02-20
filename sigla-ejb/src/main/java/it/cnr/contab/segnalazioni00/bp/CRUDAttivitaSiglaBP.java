package it.cnr.contab.segnalazioni00.bp;

import it.cnr.contab.segnalazioni00.bulk.Attivita_siglaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDAttivitaSiglaBP extends SimpleCRUDBP{

	public CRUDAttivitaSiglaBP() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CRUDAttivitaSiglaBP(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	public void validaAttivita(ActionContext context, Attivita_siglaBulk bulk) throws ValidationException
	{
		if(bulk.getStato() == null)
			throw new ValidationException("Il campo Stato non può essere vuoto!");
		if(bulk.getTipo_attivita() == null)
			throw new ValidationException("Il campo Tipo non può essere vuoto!");				
	}
}
