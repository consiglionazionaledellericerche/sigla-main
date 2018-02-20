package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class CRUDTipoRuoloBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController crudPrivilegi_disponibili = new SimpleDetailCRUDController("Privilegi_disponibili",PrivilegioBulk.class,"privilegi_disponibili",this);
	private final SimpleDetailCRUDController crudPrivilegi = new SimpleDetailCRUDController("Privilegi",PrivilegioBulk.class,"privilegi",this);
	
public CRUDTipoRuoloBP() throws BusinessProcessException {
	super();

}
public CRUDTipoRuoloBP( String function ) throws BusinessProcessException {
	super(function);

}

/**
 * Restituisce il Controller che gestisce il dettaglio dei Privilegi già assegnati ad un Tipo Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudPrivilegi() {
	return crudPrivilegi;
}
/**
 * Restituisce il Controller che gestisce il dettaglio dei Privilegi ancora disponibili per un Tipo Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudPrivilegi_disponibili() {
	return crudPrivilegi_disponibili;
}
}
