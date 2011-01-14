/*
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.doccont00.bp;


import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author rpucciarelli
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CRUDCupBP extends SimpleCRUDBP {
	
	public CRUDCupBP() {
		super();
	}
	
	public CRUDCupBP(String function)  throws BusinessProcessException{
		super(function);
	}
		
	public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
		CupBulk cup = (CupBulk)super.createNewBulk(context);
		return cup;
	}
	
	public void validainserimento(ActionContext context, CupBulk bulk) throws ValidationException{
		if ( bulk.getCdCup()==null) 
			throw new ValidationException("E' necessario inserire il Codice");
		if ( bulk.getCdCup().length()!=15) 
			throw new ValidationException("La lunghezza del Codice non è valida");
		if ( bulk.getDescrizione()==null) 
			throw new ValidationException("E' necessario inserire la Descrizione");
		
	}

}