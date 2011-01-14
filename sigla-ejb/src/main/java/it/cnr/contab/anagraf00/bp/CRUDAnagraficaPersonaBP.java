package it.cnr.contab.anagraf00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;

/**
 * Insert the type's description here.
 * Creation date: (23/07/2001 10:40:12)
 * @author: CNRADM
 */
public class CRUDAnagraficaPersonaBP extends CRUDAnagraficaBP {
/**
 * CRUDAnagraficaPersonaBP constructor comment.
 * @param function java.lang.String
 * @exception it.cnr.jada.action.BusinessProcessException The exception description.
 */
public CRUDAnagraficaPersonaBP(String function) throws it.cnr.jada.action.BusinessProcessException {
	super(function);
}
public it.cnr.jada.bulk.OggettoBulk createNewBulk(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)super.createNewBulk(context);
	anagrafico.setTi_entita_persona_struttura(anagrafico.ENTITA_PERSONA);
	if (isUoEnte(context))
		anagrafico.setUo_ente(true);
	else
		anagrafico.setUo_ente(false);
	return anagrafico;
}
public boolean isUoEnte(ActionContext context){	
	Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
		return true;	
	return false; 
}	
}
