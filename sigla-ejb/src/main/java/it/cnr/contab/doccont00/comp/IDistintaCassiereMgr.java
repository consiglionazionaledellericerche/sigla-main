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

import it.cnr.contab.doccont00.intcass.bulk.*;
import java.util.*;
import java.util.Vector;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.util.RemoteIterator;

public interface IDistintaCassiereMgr extends ICRUDMgr {
/** 
 *
 * Nome: Annulla modifiche dettagli
 * Pre:  E' stata generata la richiesta di annullare tutte le modifiche fatte dall'utente ai dettagli della distinta
 * Post: La transazione viene riportata all'ultimo savepoint impostato
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinta la Distinta_cassiereBulk le cui modifiche ai dettagli devono essere annullate
 */

public abstract void annullaModificaDettagliDistinta(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Inserisce in distinta tutti i mandati e tutte le reversali 
 *
 * Pre-post-conditions:
 *
 * Nome: Inserisci tutti i documenti contabili
 * Pre:  Una richiesta di inserire in distinta tutti i mandati e le reversali visualizzati all'utente e' stata generata
 * Post: Per ogni mandato/reversale visualizzato all'utente viene generato un dettaglio 
 *       della distinta e lo stato trasmissione del mandato/reversale viene aggiornato a 'inserito in distinta' (metodo 'inserisciDettaglioDistinta'); 
 *       se tale mandato/reversale ha associati altre reversali/mandati, vengono creati automaticamente dei dettagli di distinta anche per questi 
 *		 ed il loro stato trasmissione viene aggiornato (metodo 'aggiungiMandatiEReversaliCollegati').
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinta	la Distinta_cassiereBulk per cui generare i dettagkli
 * @param	docPassivo	un istanza di V_mandato_reversaleBulk contente i criteri di ricerca specificati dall'utente nella
 *          selezione del mandato/reversale
 */

public abstract void associaTuttiDocContabili(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1,it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Calcola i totali dei mandati/reversali inseriti in distinta
 *
 * Pre-post-conditions:
 *
 * Nome: Calcola totali
 * Pre:  Una richiesta di calcolare i totali dei mandati e delle reversali inseriti in distinta e'
 *		 stata generata
 * Post: I totali, distinti secondo le varie tipologie dei mandati e delle reversali, sono stati calcolati
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinta	la Distinta_cassiereBulk per cui calcolare i totali
 * @return	la Distinta_cassiereBulk con tutti i totali impostati
 */

public abstract it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk calcolaTotali(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *
  * Nome: Inizializzazione per modifica
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Distinta_cassiereBulk per modifica
  * Post: Viene inizializzato la distinta, calcolati i totali dei mandati/reversali presenti in distinta (suddivivisi per
  *       tipologia) (metodo 'calcolaTotali'),  vengono calcolati
  *       gli storici degli importi di mandati/reversali già trasmessi al cassiere (metodo 'calcolaTotaliStorici') e viene recuperato il codice del 
  *       Cds Ente (999)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la distinta da inizializzare per la modifica
  *
  * @return la distinta inizializzata per la modifica
  *
  *
*/

public V_ext_cassiere00Bulk caricaLogs(it.cnr.jada.UserContext context, V_ext_cassiere00Bulk file) throws it.cnr.jada.comp.ComponentException;
/** 
  *
  * Nome: Inizializzazione per modifica
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Distinta_cassiereBulk per modifica
  * Post: Viene inizializzato la distinta, calcolati i totali dei mandati/reversali presenti in distinta (suddivivisi per
  *       tipologia) (metodo 'calcolaTotali'),  vengono calcolati
  *       gli storici degli importi di mandati/reversali già trasmessi al cassiere (metodo 'calcolaTotaliStorici') e viene recuperato il codice del 
  *       Cds Ente (999)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la distinta da inizializzare per la modifica
  *
  * @return la distinta inizializzata per la modifica
  *
  *
*/

public RemoteIterator cercaFile_Cassiere(it.cnr.jada.UserContext context, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException;
/**
 * Esegue una operazione di ricerca di Mandati/Reversali
 *
 * Nome: Cerca mandati e reversali
 * Pre:  E' necessario ricercare i mandati e le reversali da cui selezionare quelli da includere in distinta
 * Post: Viene creato un RemoteIterator passandogli le clausole presenti nel SQLBuilder creato dal metodo 'cercaMandatiEReversaliSQL'
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole	le clausole specificate dall'utente
 * @param	docPassivo	l'istanza di V_mandato_reversaleBulk con le impostazioni specificate dall'utente
 * @param	distinta	l'istanza di Distinta_cassiereBulk per cui ricercare i mandati/reversali
 * @return	RemoteIterator con le istanze di V_mandato_reversaleBulk
 */

public abstract it.cnr.jada.util.RemoteIterator cercaMandatiEReversali(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk param2,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param3) throws it.cnr.jada.comp.ComponentException;
/** 
  * Inizializzazione di una istanza di Distinta_cassiereBulk per inserimento
  *
  * Nome: Inizializzazione per inserimento
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Distinta_cassiereBulk per inserimento
  * Post: Viene inizializzata la distinta, impostando come Cds quello di scrivania e come data di emissione la data odierna;
  *       vengono impostati a 0 tutti i totali dei mandati/reversali presenti in distinta;
  *       viene assegnato il progressivo distinta ( metodo 'assegnaProgressivo'); vengono calcolati
  *       gli storici degli importi di mandati/reversali già trasmessi al cassiere (metodo 'calcolaTotaliStorici'); viene recuperato il codice del 
  *       Cds Ente (999). La distinta viene inserita nel database.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la distinta da inizializzare per inserimento
  *
  * @return la distinta inizializzata per l'inserimento
  *
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  * Inizializzazione di una istanza di Distinta_cassiereBulk per modifica
  *
  * Nome: Inizializzazione per modifica
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Distinta_cassiereBulk per modifica
  * Post: Viene inizializzato la distinta, calcolati i totali dei mandati/reversali presenti in distinta (suddivivisi per
  *       tipologia) (metodo 'calcolaTotali'),  vengono calcolati
  *       gli storici degli importi di mandati/reversali già trasmessi al cassiere (metodo 'calcolaTotaliStorici') e viene recuperato il codice del 
  *       Cds Ente (999)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la distinta da inizializzare per la modifica
  *
  * @return la distinta inizializzata per la modifica
  *
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  * Inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
  *
  * Nome: Inizializzazione per ricerca
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
  * Post: Viene inizializzato il Cds della distinta, viene recuperato il codice del Cds ente (999) e vengono calcolati
  *       gli storici degli importi di mandati/reversali già trasmessi al cassiere (metodo 'calcolaTotaliStorici')
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la distinta da inizializzare per la ricerca
  *
  * @return la distinta inizializzata per la ricerca
  *
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
 *
 * Nome: inizializza dettagli per modfica
 * Pre: E' stata generata la richiesta di modificare i dettagli di una distinta
 * Post: E' stato impostato un savepoint in modo da consentire all'utente l'annullamento delle modifiche dei dettagli
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinta la Distinta_cassiereBulk i cui dettagli devono essere modificati
 */

public abstract void inizializzaDettagliDistintaPerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws it.cnr.jada.comp.ComponentException;
/** Gestisce l'invio a cassiere di un insieme di distinte selezionate dall'utente
 *
 * Nome: gestione dell'invio di distinte al cassiere
 * Pre: L'utente ha selezionato le distinte da inviare al cassiere
 * Post: Tutte le distinte selezionate sono state inviate al cassiere ( metodo 'inviaSingolaDistinta')
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinte	la collezione di oggetti V_distinta_cass_im_man_revBulk da inviare
 */

public abstract void inviaDistinte(it.cnr.jada.UserContext param0,java.util.Collection param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: modifica dettagli distinta
 * Pre: L'utente ha richiesto la modifica dei dettagli di una distinta, aggiungendo nuovi mandati/reversali e/o 
 *      richiedendo la cancellazione dalla distinta di mandati/reversali precedentemente inseriti
 * Post: Per ogni mandato/reversale per cui l'utente ha richiesto l'inserimento in distinta viene generato un dettaglio
 *       della distinta e lo stato trasmissione del mandato/reversale viene aggiornato a 'inserito in distinta' (metodo 'inserisciDettaglioDistinta'); 
 *       se tale mandato/reversale ha associati altre reversali/mandati, vengono creati automaticamente dei dettagli di distinta anche per questi 
 *		 ed il loro stato trasmissione viene aggiornato (metodo 'aggiungiMandatiEReversaliCollegati');
 *  	 Per ogni mandato/reversale per cui l'utente ha richiesto la cancellazione dalla distinta viene eliminato 
 *       il dettaglio distinta ad esso riferito e lo stato trasmissione del mandato/reversale viene aggiornato a 'non inserito in distinta'
 *       (metodo 'eliminaDettaglioDistinta');  
 *       se tale mandato/reversale ha associati reversali/mandati, vengono eliminati automaticamente i loro dettagli di distinta 
 *		 ed il loro stato trasmissione viene aggiornato (metodo 'eliminaMandatiEReversaliCollegati');
 *
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	distinta	la Distinta_cassiereBulk i cui dettagli sono stati modificati
 * @param	docContabili l'array di documenti contabili (V_mandato_reversaleBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */

public abstract void modificaDettagliDistinta(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException;
/**  
  *  Processa File
  *    PreCondition:
  *      E' stata generata la richiesta di processare un file.
  *		Nessun errore rilevato.
  *    PostCondition:
  *      Viene richiamata la procedura che processerà il file selezionato dall'utente, (metodo callProcessaFile).
  *		Restituisce l'oggetto V_ext_cassiere00Bulk aggiornato.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param file <code>V_ext_cassiere00Bulk</code> l'oggetto che contiene le 
  *	 informazioni relative al file da processare.
  *
  * @return <code>V_ext_cassiere00Bulk</code> l'oggetto aggiornato.
**/
public V_ext_cassiere00Bulk processaFile(it.cnr.jada.UserContext userContext, V_ext_cassiere00Bulk file) throws it.cnr.jada.comp.ComponentException;
}
