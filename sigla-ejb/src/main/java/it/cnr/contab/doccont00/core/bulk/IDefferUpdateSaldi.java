package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (5/15/2002 10:24:10 AM)
 * @author: Simonetta Costa
 */
public interface IDefferUpdateSaldi {
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void addToDefferredSaldi(IDocumentoContabileBulk docCont, java.util.Map values);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi();
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void removeFromDefferredSaldi(IDocumentoContabileBulk docCont);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void resetDefferredSaldi();
}
