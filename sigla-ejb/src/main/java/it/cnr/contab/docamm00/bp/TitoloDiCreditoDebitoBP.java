package it.cnr.contab.docamm00.bp;

import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;

/**
 * Insert the type's description here.
 * Creation date: (5/2/2002 4:37:26 PM)
 * @author: CNRADM
 */
public interface TitoloDiCreditoDebitoBP {
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void addToDocumentiContabiliModificati(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
it.cnr.jada.bulk.PrimaryKeyHashMap getDocumentiContabiliModificati();
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
boolean isDocumentoContabileModificato(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void removeFromDocumentiContabiliModificati(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void setDocumentiContabiliModificati(it.cnr.jada.bulk.PrimaryKeyHashMap aMap);
}
