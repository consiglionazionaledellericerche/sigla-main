package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (13/11/2002 12.35.13)
 * @author: Roberto Fantino
 */
public interface IAbiCabMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
 * Ricerca cap legati al comune selezionato
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @param abiCab		L'abiCab in uso
 * @return La collezione di cap associati al comune selezionato
 */
public AbicabBulk findCaps(UserContext userContext, AbicabBulk abiCab) throws ComponentException;
/**
 * Ricerca TRUE se l'oggetto bulk è cancellato logicamento
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @param abiCab		L'abiCab in uso
 * @return TRUE se l'oggetto byulk è cancellato logicamento, FALSE altrimenti
 */
public boolean isCancellatoLogicamente(UserContext userContext, AbicabBulk abiCab) throws ComponentException;
}
