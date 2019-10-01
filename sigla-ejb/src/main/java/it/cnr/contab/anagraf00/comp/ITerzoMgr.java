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

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
public interface ITerzoMgr extends ICRUDMgr
{


//^^@@
/** 
  *  Restituire un risultato di ricerca
  *    PreCondition:
  *      Restituire un elenco di oggetti con i parametri uguali a quelli impostati
  *    PostCondition:
  *      Prima di avviare la ricerca ripulisce le condizioni da eventuali "dati sporchi".
 */
//^^@@
public abstract RemoteIterator cerca (UserContext userContext,CompoundFindClause clausole,OggettoBulk bulk) throws ComponentException;
//^^@@
/** 
  *  Restituire un risultato di ricerca
  *    PreCondition:
  *      Restituire un elenco di oggetti con i parametri uguali a quelli impostati
  *    PostCondition:
  *      Prima di avviare la ricerca ripulisce le condizioni da eventuali "dati sporchi".
 */
//^^@@
public abstract RemoteIterator cercaBanchePerTerzoCessionario(UserContext userContext,it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk modalita_pagamento) throws ComponentException, it.cnr.jada.persistency.PersistencyException;
RemoteIterator cercaModalita_pagamento_disponibiliByClause(UserContext userContext,TerzoBulk terzo) throws ComponentException, it.cnr.jada.persistency.PersistencyException;
TerzoBulk cercaTerzoPerUnitaOrganizzativa(UserContext userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa) throws ComponentException;
//^^@@
/** 
  *  Salvare un oggetto anagrafico
  *    PreCondition:
  *      Effettuare il salvataggio dell'oggetto anagrafico e di tutti gli oggetti associati
  *    PostCondition:
  *      Prima di effettuare il salvataggio l'anagrafica viene sottoposta a tutti i controlli necessari.
 */
//^^@@
public abstract OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;
//^^@@
/** 
  *  Eliminare un oggetto anagrafico
  *    PreCondition:
  *      Effettuare l'eliminazione dell'oggetto anagrafico
  *    PostCondition:
  *      Se l'anagrafica ha ancora dei riferimenti anziche effettuare una cancellazione fisica si procede a impostare la data di fine rapporto per tutti gli elementi associati e l'anagrafica stessa.
 */
//^^@@
public abstract void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;
//^^@@
/** 
  *  Impostazioni di default proposte dal programma
  *    PreCondition:
  *      Il programma deve proposte come impostazioni di default le opzioni: Italiana,  Persona fisica, Ditta individuale
  *    PostCondition:
  *      Vengono proposte di default le opzioni: "Italiana", "Persona fisica", "Ditta individuale" e sulla selezione di "Persona giuridica" viene proposto "Altro".
  *  Flag IVA
  *    PreCondition:
  *      Flag Soggetto IVA selezionato di default
  *    PostCondition:
  *      Il Flag Soggetto IVA viene selezionato di default. Se li flag è selezionato rende obbligatorio l'inserimento della Partita I.V.A.
 */
//^^@@
public abstract OggettoBulk inizializzaBulkPerInserimento (UserContext userContext,OggettoBulk bulk) throws ComponentException;
//^^@@
/** 
  *  Inizializzazione
  *    PreCondition:
  *      Preparare l'oggetto alle modifiche
  *    PostCondition:
  *      Si procede, oltre che alla normare procedura di inizializzazione di un oggetto bulk, anche al caricamento di tutti gli elementi associati all'anagrafica in modifica.
  *      
 */
//^^@@
public abstract OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException;
TerzoBulk inizializzaTerzoPerUnitaOrganizzativa(UserContext userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa) throws ComponentException;
//^^@@
/** 
  *  Salvare un oggetto anagrafico
  *    PreCondition:
  *      Effettuare il salvataggio dell'oggetto anagrafico e di tutti gli oggetti associati che sono stati modificati
  *      
  *    PostCondition:
  *      Prima di effettuare il salvataggio l'anagrafica viene sottoposta a tutti i controlli necessari.
 */
//^^@@
public abstract OggettoBulk modificaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;
//^^@@
/** 
  *  Gestione comune sede
  *    PreCondition:
  *      Ricerca del comune e acricamenti dei cap relativi
  *    PostCondition:
  *      Viene assegnato il comune e lanciato l'aggornamento dell'elenco dei cap associati.
 */
//^^@@
public abstract TerzoBulk setComune_sede (UserContext userContext,TerzoBulk terzo,ComuneBulk comune) throws ComponentException;
}
