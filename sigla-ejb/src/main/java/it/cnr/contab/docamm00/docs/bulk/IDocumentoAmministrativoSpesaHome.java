package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 5:49:25 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoSpesaHome {
/**
 * Insert the method's description here.
 * Creation date: (5/10/2002 3:23:25 PM)
 */
void updateFondoEconomale(Fondo_spesaBulk spesa)
	throws	it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.bulk.OutdatedResourceException,
			it.cnr.jada.bulk.BusyResourceException;
Boolean archiviaStampa(UserContext userContext, MandatoBulk mandato, IDocumentoAmministrativoSpesaBulk docAmm)
	throws IntrospectionException, PersistencyException;
}
