package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.*;
/**
 * Business Process che gestisce le attività di CRUD per le entita' Sospeso o Riscontro
 */
public class CRUDSospesoCNRBP extends it.cnr.jada.util.action.SimpleCRUDBP 
{
	private final SimpleDetailCRUDController sospesiFigli = new SimpleDetailCRUDController("SospesiGigli",SospesoBulk.class,"sospesiFigliColl",this)
	{
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException 
		{
			if (  ((SospesoBulk) detail).getIm_associato().compareTo( new java.math.BigDecimal(0)) > 0 )
				throw new ValidationException("Non è possibile cancellare un dettaglio già utilizzato da qualche reversale.");
		}		
	};	
public CRUDSospesoCNRBP() {
	super();
}
public CRUDSospesoCNRBP(String function) {
	super(function);
}
public void cambiaStatoCNR( ActionContext action )
{
	((SospesoBulk)getSospesiFigli().getModel()).cambiaStato();
	
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	Button[] toolbar = new Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.cancel");
	return toolbar;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2003 11.29.46)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getSospesiFigli() {
	return sospesiFigli;
}
}
