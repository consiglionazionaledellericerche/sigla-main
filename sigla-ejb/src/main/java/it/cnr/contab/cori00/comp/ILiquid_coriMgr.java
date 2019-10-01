/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.cori00.comp;

/**
 * Insert the type's description here.
 * Creation date: (11/06/2002 18.04.27)
 * @author: Gennaro Borriello
 */
public interface ILiquid_coriMgr extends it.cnr.jada.comp.ICRUDMgr {
/**  
  *  Calcola liquidazione
  *    PreCondition:
  *      E' stata generata la richiesta di calcolo dei CORI che devono essere liquidati. 
  *		Nessun errore rilevato.
  *    PostCondition:
  *      Viene richiamata la procedura di calcolo della Liquidazione, (metodo callCalcolaLiquidazione).
  *		Restituisce l'oggetto Liquid_coriBulk aggiornato, (metodo aggiornaLiquidCori).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto che contiene le 
  *	 informazioni relative alla liquidazione.
  *
  * @return <code>Liquid_coriBulk</code> l'oggetto aggiornato.
**/

public abstract it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk calcolaLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException;
/**  
  *  Liquida i contributi selezionati.
  *    PreCondition:
  *      E' stata generata la richiesta di liquidare i CORI.
  *    PostCondition:
  *		 Vengono inseriti nella vista VSX_LIQUIDAZIONE_CORI i dati relativi ai CORI selezionati
  *		dall'utente e che devono essere liquidati.
  *     Viene richiamata la procedura di Liquidazione CORI, (metodo callLiquidaCori).
  *		Restituisce l'oggetto Liquid_coriBulk aggiornato, (metodo aggiornaLiquidCori).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto che contiene le 
  *	 informazioni relative alla liquidazione.
  *
  * @param gruppi_selezionati la <code>List</code> lista di gruppi CORI selezionati.
  *
  * @return <code>Liquid_coriBulk</code> l'oggetto aggiornato.
**/

public abstract it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk eseguiLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione di una istanza di Liquid_coriBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Liquid_coriBulk.
  *    PostCondition:
  *      Vengono impostate le proprità relative al CdS alla UO ed alle date di inizio e fine
  *		periodo di Liquidazione, (metodo impostaDate).
  *		Viene cercato il progressivo da assegnare alla Liquidazione, (metodo getNumLiquidazione);
  *		viene restituito l'oggetto Liquid_coriBulk inizializzato.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> l'oggetto che deve essere istanziato.
  *
  * @return <code>OggettoBulk</code> l'oggetto inizializzato.
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**   
  *  Inizializzazione di una istanza di Liquid_coriBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Liquid_coriBulk per modifica
  *    PostCondition:
  *      Vengono caricati i gruppi CORI legati alla Liquidazione indicata, (metodo findGruppoCori).
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> l'oggetto che deve essere istanziato.
  *
  * @return <code>OggettoBulk</code> l'oggetto inizializzato.
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
