package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.PersistencyException;

public interface IScadenzaDocumentoContabileHome {
/**
 * @return it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public void aggiornaImportoAssociatoADocAmm(
	UserContext userContext, 	
	IScadenzaDocumentoContabileBulk scadenza)
	throws PersistencyException, BusyResourceException, OutdatedResourceException;
}
