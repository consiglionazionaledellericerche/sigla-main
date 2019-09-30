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

package it.cnr.contab.doccont00.comp;

import it.cnr.jada.comp.ComponentException;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (13/06/2002 11.59.19)
 * @author: Simonetta Costa
 */
public interface ISospesoRiscontroMgr extends it.cnr.jada.comp.ICRUDMgr {
/** 
  *  stato IN SOSPESO
  *    PreCondition:
  *      E' stata generata la richiesta di modificare lo stato INIZIALE di un insieme di Sospesi di Entrata CNR nello stato
  *      IN SOSPESO
  *    PostCondition:
  *      Ad ogni Sospeso d'Entrata del CNR viene impostato lo stato a IN SOSPESO
  *  stato ASSOCIATO A CDS
  *    PreCondition:
  *      E' stata generata la richiesta di modificare lo stato INIZIALE di un insieme di Sospesi di Entrata CNR nello stato
  *      ASSOCIATO A CDS ed e' stato specificato il Cds a cui assegnare il sospeso
  *    PostCondition:
  *      Ad ogni Sospeso d'Entrata del CNR viene impostato lo stato a ASSOCIATO A CDS e viene impostato il codice del Cds origine
  *      al valore specificato dall'utente
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param sospesi la collezione di <code>SospesoBulk</code> per cui cambiare lo stato
  * @param nuovoStato il nuovo stato (IN SOSPESO o ASSOCIATO A CDS) da impostare al sospeso
  * @param cd_cds il codice del cds da impostare al cd_cds_origine del sospeso nel caso di nuovoStato = ASSOCIATO A CDS
  *               null nel caso di nuovoStato = IN SOSPESO  
  *
*/

public abstract void cambiaStato(it.cnr.jada.UserContext param0,java.util.Collection param1,java.lang.String param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione Sospeso
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Sospeso e il sospeso supera la validazione
  *      (metodo verificaSospesoRiscontro)
  *    PostCondition:
  *      Per i Sospesi d'Entrata del CNR vengono resettati il cds di origine e l'unità organizzativa di 
  *		 origine e viene impostato lo stato del sospeso a INIZIALE. 
  *		 In tutti gli altri casi viene resettata l'unità organizzativa di origine, viene impostato il cds
  *		 di origine con quello di scrivania e lo stato del sospeso a ASSOCIATO A CDS.
  *  Creazione di un Sospeso - errore
  *    PreCondition:
  *      la richiesta di creazione di un sospeso e' stata generata ed esiste un altro sospeso con la stessa chiave
  *    PostCondition:
  *      una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
  *  creazione Riscontro
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Riscontro e il riscontro supera la validazione
  *      (metodo verificaSospesoRiscontro)
  *    PostCondition:
  *      Viene aggiornato l'importo del riscontro associato al documento contabile (mandato o reversale).
  *		 Viene poi creato un dettaglio di riscontro (metodo creaDettaglioSospeso), di tipo spesa o entrata, a seconda se
  *		 il riscontro è stato associato rispettivamente a un mandato o a una reversale
  *  Creazione di un Riscontro - errore
  *    PreCondition:
  *      la richiesta di creazione di un riscontro e' stata generata ed esiste un altro riscontro con la stessa chiave
  *    PostCondition:
  *      una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da creare
  *
  * @return OggettoBulk il Sospeso o Riscontro creato
*/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  eliminazione Sospeso
  *    PreCondition:
  *      E' stata generata la richiesta di eliminare un sospeso e il sospeso supera la validazione
  *		 (metodo validaEliminaConBulk)
  *    PostCondition:
  *		 Viene impostato il flag stornato del sospeso a TRUE.
  *  eliminazione Riscontro
  *    PreCondition:
  *      E' stata generata la richiesta di eliminare un riscontro e il riscontro supera la validazione
  *		 (metodo validaEliminaConBulk)
  *    PostCondition:
  *		 Viene impostato il flag stornato del riscontro a TRUE.
  *		 Viene annullato l'eventuale dettaglio del riscontro che sta per essere cancellato
  *		 (metodo annullaDettaglioSospeso)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da cancellare
  *
*/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di SospesoBulk
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di SospesoBulk
  *    PostCondition:
  *     Vengono impostati l'esercizio con l'esercizio di scrivania, il flag
  *		stornato a FALSE, il cds e il cds origine rispettivamente con il cds 
  *		e il cds origine di scrivania, l'importo associato a documenti contabili
  *		a 0, la data di registrazione con la data del Server
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per l'inserimento
  *
  * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per l'inserimento
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di SospesoBulk per modifica
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di SospesoBulk
  *    PostCondition:
  *     Viene caricata la collezione dei dettagli d'entrata o di spesa del riscontro
  *		(rispettivamente Sospeso_det_etrBulk e Sospeso_det_uscBulk) e delle 
  *		associazioni mandato-reversale( Ass_mandato_reversaleBulk).
  *  inizializzazione di una istanza di SospesoBulk per modifica - errore dettaglio d'Entrata
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di un riscontro d'entrata
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che non è stata
  *		associata nessuna reversale al riscontro
  *  inizializzazione di una istanza di SospesoBulk per modifica - errore associazione mandato-reversale
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di un riscontro d'entrata
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che non esiste
  *		nessuna associazione mandato-reversale per il riscontro
  *  inizializzazione di una istanza di SospesoBulk per modifica - errore dettaglio di Spesa
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di un riscontro di spesa
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che non è stato
  *		associato nessun mandato al riscontro
  *  inizializzazione di una istanza di SospesoBulk per modifica - errore associazione mandato-reversale
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di un riscontro di spesa
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che non esiste
  *		nessuna associazione mandato-reversale per il riscontro
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la modifica
  *
  * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la modifica
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di SospesoBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di SospesoBulk per ricerca
  *    PostCondition:
  *     Viene inizializzata l'istanza di SospesoBulk, impostando l'esercizio e il cds 
  *		rispettivamente con l'esercizio e il cds di scrivania e flag stornato a FALSE.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la ricerca
  *
  * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la ricerca
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di SospesoBulk per ricerca libera
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di SospesoBulk per ricerca libera
  *    PostCondition:
  *     Viene inizializzata l'istanza di SospesoBulk, impostando l'esercizio e il cds 
  *		rispettivamente con l'esercizio e il cds di scrivania e flag stornato a FALSE.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la ricerca libera
  *
  * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la ricerca libera
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica Sospeso
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di un Sospeso e il sospeso supera la validazione
  *      (metodo verificaSospesoRiscontro)
  *    PostCondition:
  *      Vengono aggiornate le eventuali modifiche alla descrizione o alla causale del sospeso
  *  modifica Riscontro
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di un Riscontro e il riscontro supera la validazione
  *      (metodo verificaSospesoRiscontro)
  *    PostCondition:
  *      Nel caso il documento contabile (mandato o reversale) inserito dall'utente sia diverso da quello 
  *		 precedentemente associato al riscontro, viene annullato il dettaglio di spesa o d'entrata del 
  *		 doc. contabile precedente (metodo annullaDettaglioSospeso) e viene creato un nuovo dettaglio di 
  *		 riscontro (metodo creaDettaglioSospeso).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da modificare
  *
  * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro modificato
*/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
