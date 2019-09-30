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

import java.util.*;
import java.util.Vector;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IReversaleMgr extends ICRUDMgr
{

/** 
  *  aggiungiDocAttivi
  *    PreCondition:
  *      E' stata generata la richiesta di aggiungere ad una reversale nuovi documenti amministrativi attivi ( fatture
  *      attive o domenti generici attivi). Tali documenti hanno lo stesso terzo e la stessa classe di pagamento.
  *    PostCondition:
  *      Per ogni documento attivo viene creata una o piu' righe di reversale (metodo creaReversaleRiga) 
  *      secondo la seguente regola:
  *      - per ogni documento generico viene creata una sola riga di reversale
  *      - per ogni fattura attiva viene creata una riga di reversale ed eventualmente righe aggiuntive se
  *        tale fattura e' associata a note di debito e/o note di credito
  *     Viene creata una istanza di ReversaleTerzoBulk (metodo creaReversaleTerzo) coi dati del terzo presente 
  *     nei documenti amministrativi
  *  errore - debitori diversi
  *    PreCondition:
  *      Il codice terzo dei documenti amministrativi attivi da aggiungere alla reversale non e'
  *      lo stesso per tutti i documenti
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
  *  errore - classe di pagamento
  *    PreCondition:
  *      La classe di pagamento (Bancario,Postale,etc.) dei documenti amministrativi attivi da aggiungere alla reversale 
  *      non e' lo stesso per tutti i documenti.
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
  *  errore - reversale di regolarizzazione 
  *    PreCondition:
  *      I documenti amministrativi attivi selezionati per essere aggiunti ad una reversale di regolarizzazione sono stati 
  *      contabilizzati in parte su accertamenti relativi a capitoli di bilancio e in parte su accertamenti 
  *      relativi a partite di giro.
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
  *  errore - Tipo competenza/residuo
  *    PreCondition:
  *      Il tipo (Competenza,Residuo) dei documenti amministrativi attivi da aggiungere alla reversale 
  *      non e' lo stesso per tutti i documenti.
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale da aggiornare
  * @param docAttivi <code>List</code> la lista dei documenti attivi
  *
  * @return reversale <code>ReversaleBulk</code> la Reversale aggiornata
*/

public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk aggiungiDocAttivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale
  *    PostCondition:
  *      Viene annullata la Reversale (metodo annullaReversale) specificando che e' necessario procedere 
  *      anche all'annullamento dei mandati/reversali collegate
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale da annullare
  *
  * @return reversale <code>ReversaleBulk</code> la Reversale annullata
*/

public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk annullaReversale(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento reversale e doc. contabili collegati
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale
  *      La reversale non ha riscontri associati
  *      E' stato richiesta di annullare anche i doc. contabili collegati alla reversale
  *    PostCondition:
  *      Viene impostata la data di annullamento della reversale con la data odierna e lo stato della reversale
  *      diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe della reversale.
  *      Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
  *      accertamento legata alla riga (metodo aggiornaImportoAccertamenti), viene aggiornato lo stato
  *      del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i 
  *      saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associazione sospeso-reversale, viene
  *      aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se la reversale ha associati mandati
  *      o altre reversali viene eseguito il loro annullamento (metodo 'annullaDocContabiliCollegati').
  *      Se il valore dello stato della reversale è INCASSATO, vengono aggiornati i saldi pagati ( metodo aggiornaSaldoPagato)
  *
  *  annullamento reversale
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale
  *      La reversale non ha riscontri associati
  *      E' stato richiesta di non annullare anche i doc. contabili collegati alla reversale
  *    PostCondition:
  *      Viene impostata la data di annullamento della reversale con la data odierna e lo stato della reversale
  *      diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe della reversale.
  *      Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
  *      accertamento legata alla riga (metodo aggiornaImportoAccertamenti), viene aggiornato lo stato
  *      del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i 
  *      saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associazione sospeso-reversale, viene
  *      aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se la reversale ha associati mandati
  *      o altre reversali NON viene eseguito il loro annullamento (metodo 'annullaDocContabiliCollegati').
  *      Se il valore dello stato della reversale è INCASSATO, vengono aggiornati i saldi pagati ( metodo aggiornaSaldoPagato)
  *
  *  errore riscontri associati
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale che ha riscontri associati
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di eseguire l'annullamento
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale da annullare
  * @param annullaCollegati valore booleano che indica se procedere o meno all'annullamento dei doc. contabili associati alla reversale
  *
  * @return reversale <code>ReversaleBulk</code> la Reversale annullata
*/

public abstract ReversaleBulk annullaReversale(UserContext userContext, ReversaleBulk reversale, boolean annullaCollegati ) throws ComponentException;
public abstract void annullaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale di Regolarizzazione
  *    PostCondition:
  *      Viene richiesta alla Component che gestisce il Documento Generico l'annullamento della 
  *		 reversale di regolarizzazione (metodo annullaReversale) e di eventuali documenti
  *		 generici associati alla reversale (metodo docGenerico_annullaDocumentoGenerico).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale di regolarizzazione da annullare
  *
*/

public abstract void annullaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare una Reversale di Trasferimento
  *    PostCondition:
  *      Viene richiesta alla Component che gestisce il Documento Generico l'annullamento della 
  *		 reversale di Trasferimento (metodo annullaReversale) e di eventuali documenti
  *		 generici associati alla reversale (metodo docGenerico_annullaDocumentoGenerico).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale di trasferimento da annullare
  *
*/

public abstract void annullaReversaleDiTrasferimento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  ricerca sospesi
  *    PreCondition:
  *     E' stata richiesta la ricerca dei sospesi di entrata da associare ad una reversale
  *    PostCondition:
  *     Vengono ricercati tutti i sospesi di entrata che non sono ancora stati associati alla reversale 
  *     con cds appartenza uguale al cds appartenenza della reversale,
  *     uo origine uguale all'uo di scrivania, importo disponibile (importo disponibile = importo iniziale del sospeso -
  *     importo già associato a reversali) maggiore di zero (metodo findSospesiDiEntrata)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param clausole le clausole specificate dall'utente
  * @param reversale <code>ReversaleBulk</code> la reversale
  *
  * @return il RemoteIterator della lista dei sospesi
  * 
  * 
*/

public abstract it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione reversale
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Reversale e la reversale supera la validazione
  *      (metodo verificaReversale)
  *    PostCondition:
  *      Vengono aggiornati gli importi dei sospesi eventualmente associati alla reversale (metodo aggiornaImportoSospesi), 
  *      vengono aggiornati gli importi associati a documenti contabili di tutte le scadenze di accertamenti specificate 
  *      nelle righe della reversale (metodo aggiornaImportoAccertamenti), vengono aggiornati i saldi relativi ai capitoli di entrata
  *      (metodo aggiornaCapitoloSaldoRiga), vengono aggiornati gli stati delle fatture specificate nelle righe delle reversali
  *      (metodo aggiornaStatoFattura). Se l'importo incassato della reversale è pari all'importo della reversale stessa,
  *		 vengono aggiornati i saldi pagati (metodo aggiornaSaldoPagato)
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da creare
  *
  * @return OggettoBulk la Reversale creata
*//*
REVERSALE
1 - verifica reversale
2 - inserisce record reversale, terzo, riga
3 - aggiorna accertamento_scadenzario per im_associato_doc_contabili
4 - aggiorna stato cofi della fattura
5 - aggiorna saldi per im_incassato

*/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
  *  creazione reversale di Regolarizzazione
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Reversale di regolarizzazione
  *    PostCondition:
  *      Viene creata una reversale di regolarizzazione a partire dal mandato di 
  *		 regolarizzazione associato.
  *      Viene creata una reversaleTerzo (metodo creaReversaleTerzo).
  *      Viene creato un documento generico attivo (metodo docGenerico_creaDocumentoGenerico). Viene creata una 
  *		 riga di documento generico (metodo docGenerico_creaDocumentoGenericoRiga) per ogni scadenza dell'accertamento 
  *		 (specificato dall'utente) non ancora associata a documenti amministrativi.
  *      Vengono create tante righe di reversale quante sono quelle del documento generico (metodo creaReversaleRiga).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato
  *
  * @return reversale <code>ReversaleBulk</code> la reversale di regolarizzazione creata
  */

public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
  *  creazione reversale di Trasferimento
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Reversale di trasferimento
  *    PostCondition:
  *      Viene creata una reversale di trasferimento a partire dal mandato di 
  *		 accreditamento associato.
  *      Viene creata una reversaleTerzo (metodo creaReversaleTerzo).
  *      Viene creato un documento generico con le relative righe (metodo docGenerico_creaDocumentoGenerico).
  *      Vengono create tante righe di reversale quante sono quelle del documento generico (metodo creaReversaleRiga).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di accreditamento
  *
  * @return reversale <code>ReversaleBulk</code> la reversale di trasferimento creata
  */

public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiTrasferimento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  eliminazione reversale provvisoria
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare fisicamente una reversale provvisoria
  *    PostCondition:
  *		 La reversale provvisoria coi suoi dettagli e' stata eliminata.
  *		 I saldi dei capitoli di entrata dell'accertamento di sistema a cui si riferisce sono stati aggiornati
  *		 (metodo aggiornaCapitoloSaldoRiga).
  *		 Vengono aggiornati lo stato della fattura (metodo aggiornaStatoFattura) e l'importo degli 
  *		 accertamenti associati alla reversale (metodo aggiornaImportoAccertamenti).
  *  eliminazione reversale definitiva
  *    PreCondition:
  *      E' stata generata la richiesta di eliminare una reversale definitiva
  *    PostCondition:
  *		 La reversale viene annullata in quanto ha dei sospesi associati (metodo annullaReversale).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da cancellare
  *
*/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di ReversaleBulk
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk
  *    PostCondition:
  *     Viene impostata la data di emissione della reversale con la data del Server
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da inizializzare per l'inserimento
  *
  * @return bulk <code>OggettoBulk</code> la Reversale inizializzata per l'inserimento
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di ReversaleBulk per modifica
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk
  *    PostCondition:
  *     Viene caricata la collezione delle righe di reversale (Reversale_rigaBulk), dei sospesi associati alla reversale (Sospeso_det_etrBulk),
  *     delle associazioni mandato-reversale( Ass_mandato_reversaleBulk). Vengono caricati i dati del terzo della reversale (Reversale_terzoBulk)
  *		e viene verificato il tipo bollo (metodo verificaTipoBollo)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da inizializzare per la modifica
  *
  * @return reversale la Reversale inizializzata per la modifica
*//*
1 - carica le reversali riga
2 - carica la reversale terzo
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di ReversaleBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk per ricerca
  *    PostCondition:
  *     Viene inizializzata l'istanza di ReversaleBulk
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da inizializzare per la ricerca
  *
  * @return reversale la Reversale inizializzata per la ricerca
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  ricerca documenti attivi
  *    PreCondition:
  *     E' stata richiesta la ricerca dei documenti attivi per cui e' possibile emettere una reversale
  *    PostCondition:
  *     Vengono ricercati tutti i documenti attivi che verificano le seguenti condizioni:
  *     - cds e uo origine uguali a cds e uo di scrivania
  *     - cds di appartenenza uguale al cds per cui si vuole emettere la reversale
  *     - (im_scadenza-im_associato_doc_contabile) della scadenza di accertamento su cui il documento amm.
  *       e' stato contabilizzato maggiore di zero
  *     Fra tutti i documenti individuati vengono esclusi quelli che eventualmente sono già stati selezionati
  *     per questa reversale
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param reversale <code>ReversaleBulk</code> la reversale
  *
  * @return reversale la Reversale aggiornata dopo la ricerca dei documenti attivi
  * 
*/

public abstract it.cnr.contab.doccont00.core.bulk.ReversaleBulk listaDocAttivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica reversale
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di una Reversale (TIPO_REV) e la reversale supera la validazione
  *      (metodo verificaReversale)
  *    PostCondition:
  *      Vengono aggiornati gli importi dei sospesi eventualmente associati alla reversale (metodo aggiornaImportoSospesi) 
  *      e vengono aggiornate le eventuali modifiche alle modalità di pagamento e al tipo bollo della reversale
  *  modifica reversale provvisoria
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di una Reversale provvisoria (TIPO_REV_PROVV) e la reversale supera la 
  *		 validazione (metodo verificaReversale)
  *    PostCondition:
  *		 Viene eliminata la precedente associazione tra reversale e mandati (metodo eliminaAss_mandato_reversale),
  *		 viene eliminata la reversale provvisoria. Vengono quindi create una nuova reversale definitiva coi relativi
  *		 sospesi associati (metodo creaReversaleDefinitiva), e la nuova associazione reversale/mandato 
  *		 (metodo creaAss_mandato_reversale).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> la reversale da modificare
  *
  * @return bulk <code>OggettoBulk</code> la Reversale modificata
*//*
1 - verifica reversale
2 - inserisce record reversale, terzo, riga
3 - per le righe aggiunte o cancellate aggiorna accertamento_scadenzario per im_associato_doc_contabili
4 - per le righe aggiunte o cancellate aggiorna stato cofi della fattura
5 - aggiorna saldi per delta im_incassato
*/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
