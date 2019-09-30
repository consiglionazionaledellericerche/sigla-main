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
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IRuoloMgr
{


//^^@@
/**
  *  Cancellazione RuoloBulk
  *    PreCondition:
  *      E' stata generata una richiesta di cancellazioe di un RuoloBulk
  *    PostCondition:
  *      Il RuoloBulk specificato e tutte le istanze di Utente_unita_ruoloBulk ad esso associate sono stati eliminati
 */
//^^@@
        public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;

//^^@@
/**
  *  Inizializzazione bulk
  *    PreCondition:
  *      L'inizializzazione di un RuoloBulk per eventuale inserimento e' stata generata
  *    PostCondition:
  *      Il RuoloBulk viene aggiornato con l'elenco delle istanze di AccessoBulk disponibili per l'utente corrente
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
  *  Inizializzazione bulk
  *    PreCondition:
  *      L'inizializzazione di un RuoloBulk per eventuale modifica e' stata generata
  *    PostCondition:
  *      Il RuoloBulk viene aggiornato con l'elenco delle istanze di AccessoBulk ancora disponibili e con l'elenco di istanze di Utente_unita_ruoloBulk gia' assegnate al ruolo
  *  Gestore non trovato
  *    PreCondition:
  *      L'utente che ha generato la richiesta non esiste
  *    PostCondition:
  *      Viene generata una ComponentException con detail l'ApplicationException con il messaggio da visualizzare all'utente
 */
//^^@@
        public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException;


}
