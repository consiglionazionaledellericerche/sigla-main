/*
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author fgiardina
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CRUDCodiciSiopeBP extends SimpleCRUDBP {
	
	public CRUDCodiciSiopeBP() {
		super();
	}
	
	public CRUDCodiciSiopeBP(String function)  throws BusinessProcessException{
		super(function);
	}
		
	public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
		Codici_siopeBulk siope = (Codici_siopeBulk)super.createNewBulk(context);
		return siope;
	}
	
	public void validainserimento(ActionContext context, Codici_siopeBulk bulk) throws ValidationException{
		if ( bulk.getEsercizio()==null) 
			throw new ValidationException("E' necessario inserire l' Esercizio");
		if ( bulk.getTi_gestione()==null) 
			throw new ValidationException("E' necessario inserire la gestione Entrata/Spesa");
		if ( bulk.getCd_siope()==null) 
			throw new ValidationException("E' necessario inserire il Codice Siope");
		if ( bulk.getDescrizione()==null) 
			throw new ValidationException("E' necessario inserire la Descrizione");
		
	}

}