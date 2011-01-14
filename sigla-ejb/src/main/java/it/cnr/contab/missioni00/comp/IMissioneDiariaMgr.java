package it.cnr.contab.missioni00.comp;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IMissioneDiariaMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Viene richiesto il completamento dell'oggetto bulk passato come parametro
 * Viene cercata la valuta di default associata alla nazione 
 * selezionato dall'utente
 * 
 * Pre-post_conditions
 *
 * Nome: Ricerca valuta corrispondente alla Nazione trovata
 * Pre: Viene richiesta la valuta associata alla nazione trovata
 * Post: Viene caricata la valuta corrispondente e impostata nell'oggetto bulk
 *
 * @param userContext	lo UserContext che genera la richesta
 * @param bulk			oggetto bulk da completare
 * @return Oggetto Bulk completo di nazione e valuta
 *
**/
public Missione_diariaBulk gestioneNazione(UserContext userContext, Missione_diariaBulk bulk) throws ComponentException;
}
