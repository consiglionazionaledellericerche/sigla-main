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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 12.28.50)
 * @author: Roberto Fantino
 */
public interface ILiquidazioneRateMinicarrieraMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
 * Ricerca il capitolo (VoceF) dato l'elemento voce e la linea di attivita
 *
 * Pre-post-conditions
 *
 * Nome: Esiste un solo capitolo
 * Pre: Viene richiesto il capitolo associato all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito il capitolo corrispondente
 *
 * Nome: Esiste + di un capitolo
 * Pre: Esistono + capitoli associati all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito una eccezione con la descrizione dell'errore
 *
 * Nome: Non esistono capitoli
 * Pre: Non esistono capitoli associati all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito una eccezione con la descrizione dell'errore
 *
 * @param	userContext lo userContext che ha generato la richiesta
 * @param	bulk il filtro che contiene l'elemento voce e la linea di attivita selezionate dall'utente
 * @return	il capitolo (Voce_fBulk) associato
*/
public abstract Voce_fBulk findVoceF(UserContext userContext, Liquidazione_rate_minicarrieraBulk bulk) throws ComponentException;
/**
 * Viene richiesta l'esecuzione della procedura Oracle CNRCTBxxx.xxx
 *
 * Pre-post-conditions:
 *
 * Nome: Liquidazione rate
 * Pre: Viene richiesta la liquidazione massiva delle rate selezionate
 * Post: Viene eseguita la procedura per la liquidazione massiva delle rate.
 *			Per ogni rata selezionata viene generato un compenso e viene automaticamente
 *			contabilizzato, generando mandati e reversali collegati ad esso
 *
 * @param userContext		lo userContext che ha generato la richiesta
 * @param bulk				istanza di Liquidazione_rateBulk che deve essere utilizzata
 * @param rateDaLiquidare	lista delle rate che devono essere liquidate
 *
*/
public abstract void liquidaRate(it.cnr.jada.UserContext userContext, it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk bulk, java.util.List rateDaLiquidare) throws it.cnr.jada.comp.ComponentException;
}
