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

package it.cnr.contab.reports.comp;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;

/**
 * Insert the type's description here.
 * Creation date: (09/05/2002 15:39:16)
 * @author: CNRADM
 */
public interface IOfflineReportMgr {
/**
 *  Stampa non configurata
 *    PreCondition:
 *      La stampa specificata non è stata configurata (non esiste un record corrispondente nella tabella PRINT_PRIORITY)
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "La stampa non è stata configurata correttamente. Avvisare il supporto tecnico."
 *  Normale
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Aggiunge la richiesta di stampa alla tabella PRINT_SPOOLER impostando la priorità e la descrizione
 *		configurate in PRINT_PRIORITY
 */

public abstract void addJob(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk param1,it.cnr.jada.bulk.BulkList param2) throws it.cnr.jada.comp.ComponentException;
/**
 *  Una o più stampe già cancellate
 *    PreCondition:
 *      L'utente ha richiesto la cancellazione di una o più stampe dalla coda di stampa e almeno una di esse risulta già cancellata.
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Una o più stampe sono state cancellate da altri utenti."
 *  Una o più stampe in esecuzione
 *    PreCondition:
 *      L'utente ha richiesto la cancellazione di una o più stampe dalla coda di stampa e almeno una di esse risulta in esecuzione.
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Una o più stampe sono attualmente in esecuzione e non possono essere cancellate."
 *  Normale
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Le stampe specificate vengono cancellate dalla coda di stampa.
 */

public abstract void deleteJobs(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException;
/**
 *  Normale
 *    PreCondition:
 *      L'utente ha richiesto la composizione della coda di stampa
 *    PostCondition:
 *		Viene restituito l'elenco delle stampe presenti nella coda di stampa compatibili con i criteri di visibilità specificati (secondo quanto sepcificato dalla vista "V_PRINT_SPOOLER_VISIBILITA")
 */

public abstract Print_spoolerBulk getJobWaitToJsoDS(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;


}