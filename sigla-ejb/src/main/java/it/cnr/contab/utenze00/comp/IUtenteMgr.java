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

package it.cnr.contab.utenze00.comp;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import java.util.Collection;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
public interface IUtenteMgr
{


//^^@@
/**
  *  Richiesta di ricerca attributo diverso da CDS
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di un attributo di un UtenteBulk
  *    PostCondition:
  *      Viene restituito il RemoteIterator con la collezione di OggettoBulk che soddisfano i criteri di ricerca
  *  Richiesta di ricerca attributo CDS
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dell'attributo CDS per un UtenteAmministratoreBulk
  *    PostCondition:
  *      Viene restituito il RemoteIterator con la collezione dei CdsBulk che soddisfano i criteri di ricerca oppure una istanza fittizia di CDS per gestire il codice '*' ad indicare 'Tutti i CDS'
 */
//^^@@
        public RemoteIterator cerca (UserContext userContext,CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws ComponentException;

//^^@@
/**
  *  Richiesta di ricerca accessi
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca degli Utente_unita_accessoBulk definiti per un UtenteComuneBulk o per un UtenteTemplateBulk
  *    PostCondition:
  *      Viene restituita l'istanza di UtenteBulk con impostati gli accessi gia' assegnati (Utente_unita_accessoBulk) e gli accessi (AccessoBulk) ancora dipsonibili per l'utente e l'unita' organizzativa specificata
 */
//^^@@
        public UtenteBulk cercaAccessi (UserContext userContext,UtenteBulk user,Unita_organizzativaBulk uo, CompoundFindClause compoundfindclause) throws ComponentException;

//^^@@
/**
  *  Richiesta di ricerca ruoli
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca degli Utente_unita_ruoloBulk definiti per un UtenteComuneBulk o per un UtenteTemplateBulk
  *    PostCondition:
  *      Viene restituita l'istanza di UtenteBulk con impostati i ruoli gia' assegnati (Utente_unita_ruoloBulk) e i ruoli (ruoloBulk) ancora dipsonibili per l'utente e l'unita' organizzativa specificata
 */
//^^@@
        public UtenteBulk cercaRuoli (UserContext userContext,UtenteBulk user,Unita_organizzativaBulk uo) throws ComponentException;

//^^@@
/**
  *  Richiesta di ricerca codici UO
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dei codici di UO su cui l'utente ha accessi propri
  *    PostCondition:
  *      Viene un collezione di stringhe contenente i codici delle UO
 */
//^^@@
        public Collection cercaUOAccessiPropri (UserContext userContext,UtenteBulk user) throws ComponentException;

//^^@@
/**
  *  Richiesta di ricerca codici UO
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dei codici di UO su cui l'utente ha ruoli propri
  *    PostCondition:
  *      Viene un collezione di stringhe contenente i codici delle UO
 */
//^^@@
        public Collection cercaUORuoliPropri (UserContext userContext,UtenteBulk user) throws ComponentException;

//^^@@
/**
  *  Creazione di Utente Comune o Template
  *    PreCondition:
  *      La richiesta di creazione di una utenza e' stata generata
  *    PostCondition:
  *      Un utente e' stato creato
  *  Creazione di Utente Amministratore
  *    PreCondition:
  *      La richiesta di creazione di una utenza Amministratore di Utenze e' stata generata
  *    PostCondition:
  *      Un utente Amministratore e' stato creato e le istanze Utente_unita_accessoBulk per tutti gli AccessiBulk con tipologia = UTENTE_AMMINISTRATORE sono state generate
  *  Errore di chiave duplicata
  *    PreCondition:
  *      Esiste già un UtenteBulk persistente che possiede la stessa chiave primaria di quello specificato.
  *    PostCondition:
  *      Viene generata una CRUDException
 */
//^^@@
        public OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;

//^^@@
/**
  *  Cancellazione logica di un Utente
  *    PreCondition:
  *      La richiesta di cancellazione di una utenza e' stata generata
  *    PostCondition:
  *      La data di fine validità dell'utente e' stata aggiornata alla data odierna
 */
//^^@@
        public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;
//^^@@
/**
  *  Inizializzazione bulk
  *    PreCondition:
  *      L'inizializzazione di un Utentebulk per eventuale inserimentoo e' stata generata
  *    PostCondition:
  *      L'UtenteBulk viene aggiornato con una password di default e con codice gestore uguale al codice dell'utente che ha generato la richiesta
  *  Gestore non trovato
  *    PreCondition:
  *      L'utente che ha generato la richiesta non esiste
  *    PostCondition:
  *      Viene generata una ComponentException con detail l'ApplicationException con il messaggio da visualizzare all'utente
 */
//^^@@
        public OggettoBulk inizializzaBulkPerInserimento (UserContext userContext,OggettoBulk bulk) throws ComponentException;

//^^@@
/**
  *  Inizializzazione UtenteTemplateBulk
  *    PreCondition:
  *      L'inizializzazione di un UtenteTemplateBulk per eventuale modifica e' stata generata
  *    PostCondition:
  *      L'UtenteTemplateBulk viene aggiornato con codice gestore uguale al codice dell'utente che ha generato la richiesta
  *  Inizializzazione UtenteAmministratoreBulk
  *    PreCondition:
  *      L'inizializzazione di un UtenteAmministratoreBulk per eventuale modifica e' stata generata
  *    PostCondition:
  *      L'UtenteAmministratoreBulk viene aggiornato con codice gestore uguale al codice dell'utente che ha generato la richiesta e con i dati relativi al CDS da lui amministrato
  *  Inizializzazione UtenteComuneBulk
  *    PreCondition:
  *      L'inizializzazione di un UtenteComuneBulk per eventuale modifica e' stata generata
  *    PostCondition:
  *      L'UtenteComuneBulk viene aggiornato con codice gestore uguale al codice dell'utente che ha generato la richiesta e con i dati relativi al CDR a cui appartiene
  *  Gestore non trovato
  *    PreCondition:
  *      L'utente che ha generato la richiesta non esiste
  *    PostCondition:
  *      Viene generata una ComponentException con detail l'ApplicationException con il messaggio da visualizzare all'utente
 */
//^^@@
        public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException;

//^^@@
/**
  *  Inizializzazione UtenteBulk
  *    PreCondition:
  *      L'inizializzazione di un UtenteBulk per eventuale ricerca e' stata generata
  *    PostCondition:
  *      L'UtenteBulk viene aggiornato con codice gestore uguale al codice dell'utente che ha generato la richiesta
  *  Gestore non trovato
  *    PreCondition:
  *      L'utente che ha generato la richiesta non esiste
  *    PostCondition:
  *      Viene generata una ComponentException con detail l'ApplicationException con il messaggio da visualizzare all'utente
 */
//^^@@
        public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException;

//^^@@
/**
  *  Inizializzazione UtenteBulk
  *    PreCondition:
  *      L'inizializzazione di un UtenteBulk per eventuale ricerca libera e' stata generata
  *    PostCondition:
  *      L'UtenteBulk viene aggiornato con codice gestore uguale al codice dell'utente che ha generato la richiesta
  *  Gestore non trovato
  *    PreCondition:
  *      L'utente che ha generato la richiesta non esiste
  *    PostCondition:
  *      Viene generata una ComponentException con detail l'ApplicationException con il messaggio da visualizzare all'utente
 */
//^^@@
        public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException;

public abstract UtenteBulk resetPassword (UserContext userContext,UtenteBulk bulk) throws ComponentException;
}
