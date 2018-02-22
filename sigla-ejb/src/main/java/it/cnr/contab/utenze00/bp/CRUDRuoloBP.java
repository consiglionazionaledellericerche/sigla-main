package it.cnr.contab.utenze00.bp;

/**
 * Business Process che gestisce l'attività di Gestione Ruolo: in particolare gestisce 
 * i due dettagli relativi agli Accessi gia' assegnati al Ruolo e agli Accessi ancora disponibili
 *	
 */


import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

public class CRUDRuoloBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController crudAccessi_disponibili = new SimpleDetailCRUDController("Accessi_disponibili",AccessoBulk.class,"accessi_disponibili",this);
	private final SimpleDetailCRUDController crudAccessi = new SimpleDetailCRUDController("Accessi",AccessoBulk.class,"accessi",this);
public CRUDRuoloBP() throws BusinessProcessException {
	super();

}
public CRUDRuoloBP( String function ) throws BusinessProcessException {
	super(function);

}
public void edit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
	super.edit(context,bulk);
	RuoloBulk ruolo = (RuoloBulk)getModel();
	CNRUserInfo userInfo = (CNRUserInfo)context.getUserInfo();
	if (!"*".equals(userInfo.getUtente().getCd_cds_configuratore()) &&
		((RuoloBulk)getModel()).getCds() == null)
		setStatus(VIEW);
}
/**
 * Restituisce il Controller che gestisce il dettaglio degli Accessi già assegnati ad un Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi() {
	return crudAccessi;
}
/**
 * Restituisce il Controller che gestisce il dettaglio degli Accessi ancora disponibile per un Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi_disponibili() {
	return crudAccessi_disponibili;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cdsFieldEnabled'
 *
 * @return Il valore della proprietà 'cdsFieldEnabled'
 */
public boolean isCdsFieldEnabled() 
{
	return isInserting() && isEditable() && ((RuoloBulk)getModel()).getGestore() != null &&
			"*".equals(((RuoloBulk)getModel()).getGestore().getCd_cds_configuratore());
}
}
