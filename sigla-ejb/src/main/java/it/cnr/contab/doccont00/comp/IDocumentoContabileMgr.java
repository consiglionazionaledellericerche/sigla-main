package it.cnr.contab.doccont00.comp;

import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
/**
 * Insert the type's description here.
 * Creation date: (2/8/2002 12:45:17 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoContabileMgr {
public void aggiornaCogeCoanInDifferita(
	UserContext userContext,
	IDocumentoContabileBulk docContabile,
	Map values)
	throws ComponentException;
public void aggiornaSaldiInDifferita(
	UserContext userContext,
	IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param )
	throws ComponentException;
public void lockScadenza(
	UserContext userContext,
	IScadenzaDocumentoContabileBulk scadenza)
	throws ComponentException;
public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(
	UserContext userContext,
	IScadenzaDocumentoContabileBulk scadenza,
	java.math.BigDecimal nuovoImporto,
	boolean aggiornaScadenzaSuccessiva )
	throws ComponentException;
}
