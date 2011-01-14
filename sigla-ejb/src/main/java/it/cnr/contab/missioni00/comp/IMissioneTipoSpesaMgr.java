package it.cnr.contab.missioni00.comp;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IMissioneTipoSpesaMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Viene richiesto il completamento dell'oggetto bulk passato come parametro
 * Vengono cercate la nazione e la valuta associate al tipo area geografica
 * selezionato dall'utente
 * 
 * Pre-post_conditions
 *
 * Nome: Ricerca nazione corrispondente al Tipo Area Geografica dell'oggetto
 * Pre: Viene richiesta la nazione associata al tipo area geografica selezionato
 * Post: Viene caricata la Nazione corrispondente e impostata nell'oggetto bulk
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
public abstract Missione_tipo_spesaBulk gestioneNazione(UserContext userContext, Missione_tipo_spesaBulk bulk) throws ComponentException;
}
