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

public interface IMandatoMgr extends ICRUDMgr
{

/** 
  *  aggiungiDocPassivi
  *    PreCondition:
  *      E' stata generata la richiesta di aggiungere ad un mandato nuovi documenti amministrativi passivi ( fatture
  *      passive o domenti generici passivi). Tali documenti hanno lo stesso terzo e la stessa classe di pagamento.
  *    PostCondition:
  *      Per ogni documento passivo viene creata una o piu' righe di mandato (metodo creaMandatoRiga) 
  *      secondo la seguente regola:
  *      - per ogni documento generico viene creata una sola riga di mandato
  *      - per ogni fattura passiva viene creata una riga di mandato ed eventualmente righe aggiuntive se
  *        tale fattura e' associata a note di debito e/o note di credito
  *     Viene creata una istanza di MandatoTerzoBulk (metodo creaMandatoTerzo) coi dati del terzo presente 
  *     nei documenti amministrativi
  *  errore - beneficiari diversi
  *    PreCondition:
  *      Il codice terzo dei documenti amministrativi passivi da aggiungere al mandato non e'
  *      lo stesso per tutti i documenti
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti al mandato
  *  errore - classe di pagamento
  *    PreCondition:
  *      La classe di pagamento (Bancario,Postale,etc.) dei documenti amministrativi passivi da aggiungere al mandato 
  *      non e' lo stesso per tutti i documenti.
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti al mandato  
  *  errore - mandato di regolarizzazione 
  *    PreCondition:
  *      I documenti amministrativi passivi selezionati per essere aggiunti ad un mandato di regolarizzazione sono stati 
  *      contabilizzati in parte su obbligazioni relative a capitoli di bilancio e in parte su obbligazioni 
  *      relative a partite di giro.
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti al mandato
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato da aggiornare
  * @param docPassivi <code>List</code> la lista dei documenti passivi selezionati dall'utente
  *
  * @return mandato <code>MandatoBulk</code> il Mandato aggiornato
*/

public abstract it.cnr.contab.doccont00.core.bulk.MandatoBulk aggiungiDocPassivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
/* 
---- non usato  ----
Consente di modificare un mandato di accreditamento CNR - Cds consentendo la creazione di nuove righe di mandato
 */

public abstract it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk aggiungiImpegni(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato 
  *    PostCondition:
  *      Viene annullato il Mandato (metodo annullaMandato) specificando che non e' stata effettuata la
  *      verifica sui compensi e che e' necessario procedere anche all'annullamento dei mandati/reversali collegate
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato da annullare
  *
  * @return mandato <code>MandatoBulk</code> il Mandato annullato
*/


public abstract it.cnr.contab.doccont00.core.bulk.MandatoBulk annullaMandato(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato 
  *    PostCondition:
  *      Viene annullato il Mandato (metodo annullaMandato) passando l'informazione se effettuare o meno la
  *      verifica sui compensi e passando l'informazione che e' necessario procedere 
  *      anche all'annullamento dei mandati/reversali collegate
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato da annullare
  * @param param il parametro che indica se il controllo sul compenso e' necessario ( se param = null e' necessario
  *        effettuare il controllo, altrimenti no)
  *
  * @return mandato <code>MandatoBulk</code> il Mandato annullato
*/
public abstract it.cnr.contab.doccont00.core.bulk.MandatoBulk annullaMandato(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1, CompensoOptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  annullamento mandato e collegati
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato
  *      E' stato richiesto l'annullamento dei doc. contabili collegati
  *    PostCondition:
  *      Viene impostata la data di annullamento del mandato con la data odierna e lo stato del mandato
  *      diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe del mandato.
  *      Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
  *      obbligazione legata alla riga (metodo aggiornaImportoObbligazione), viene aggiornato lo stato
  *      del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i 
  *      saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associzione sospeso-mandato, viene
  *      aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se il mandato ha associate reversali
  *      o altri mandati viene eseguito il loro annullamento (metodo 'annullaDocContabiliCollegati')
  *  annullamento mandato
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato
  *      Non e' necessario procedere all'annullamento dei doc. contabili collegati
  *    PostCondition:
  *      Viene impostata la data di annullamento del mandato con la data odierna e lo stato del mandato
  *      diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe del mandato.
  *      Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
  *      obbligazione legata alla riga (metodo aggiornaImportoObbligazione), viene aggiornato lo stato
  *      del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i 
  *      saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associzione sospeso-mandato, viene
  *      aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se il mandato ha associate reversali
  *      o altri mandati NON viene eseguito il loro annullamento
  *  annullamento mandato di regolarizzazione
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato di regolarizzazione
  *    PostCondition:
  *      Oltre alle PostCondition dell'annullamento di un mandato normale, viene anche annullati sia la 
  *      reversale di regolarizzazione associata al mandato  che il
  *      documento amministrativo generico di entrata creato dal sistema (metodo annullaReversaleRegolarizzazione)
  *  annullamento mandato di trasferimento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato di trasferimento
  *    PostCondition:
  *      Oltre alle PostCondition dell'annullamento di un mandato normale, viene anche annullato il documento amm.
  *      generico creato in automatico alla creazione del mandato (metodo annullaDocumentoGenerico) e viene annullata sia la 
  *      reversale di trasferimento associata al mandato che il relativo documento generico di entrata (metodo annullaReversaleTrasferimento)
  *  errore riscontri associati
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato che ha riscontri associati
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di eseguire l'annullamento
  *  annullamento mandato su anticipo associato a missione
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato
  *      Il mandato non supera la validazione effettuata dal metodo 'verificaMandatoSuAnticipo' in quanto include un anticipo
  *      associato a missione
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di eseguire l'annullamento
  *  annullamento mandato di compenso su riscontro
  *    PreCondition:
  *      E' stata generata la richiesta di annullare un Mandato
  *      Il mandato si riferisce a compensi inclusi in conguagli
  *    PostCondition:
  *      Una segnalazione richiede all'utente se intende comunque proseguire all'annullamento del mandato
  *
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato da annullare
  * @param param il parametro che indica se il controllo sul compenso e' necessario
  * @param annullaCollegati valore booleano che indica se procedere o meno con l'annullamento dei doc. contabili collegati al
  *        mandato
  *
  * @return mandato <code>MandatoBulk</code> il Mandato annullato
*/

public abstract MandatoBulk annullaMandato(UserContext userContext, MandatoBulk mandato, CompensoOptionRequestParameter param, boolean annullaCollegati ) throws ComponentException;
public it.cnr.jada.util.RemoteIterator cercaImpegni(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,MandatoBulk mandato) throws it.cnr.jada.comp.ComponentException; 
/** 
  *  cerca sospesi
  *    PreCondition:
  *     E' stata richiesta la ricerca dei sospesi di spesa da associare ad un mandato
  *    PostCondition:
  *     Vengono ricercati tutti i sospesi di spesa non annullati che non sono ancora stati associati al mandato 
  *     con cds appartenza uguale al cds appartenenza del mandato,
  *     esercizio uguale all'esercizio di scrivania, importo disponibile (importo disponibile = importo iniziale del sospeso -
  *     importo già associato a mandati) maggiore di zero, stato uguale a ASSOCIATO A CDS
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param clausole le clausole specificate dall'utente
  * @param mandato <code>MandatoBulk</code> il mandato
  *
  * @return il RemoteIterator della lista dei sospesi di spesa
  * 
*/

public abstract it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.core.bulk.MandatoBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione mandato
  *    PreCondition:
  *      E' stata generata la richiesta di creazione un Mandato e il mandato supera la validazione
  *      (metodo verificaMandato)
  *    PostCondition:
  *      Vengono aggiornati gli importi dei sospesi eventualmente associati al mandato (metodo aggiornaImportoSospesi), 
  *      vengono aggiornati gli importi associati a documenti contabili di tutte le scadenze di obbligazioni specificate 
  *      nelle righe del mandato (metodo aggiornaImportoObbligazione), vengo aggiornati i saldi relativi ai capitoli di spesa
  *      (metodo aggiornaStatoFattura), vengono aggiornati gli stati delle fatture specificate nelle righe dei mandati
  *      (metodo aggiornaCapitoloSaldoRiga)
  *  creazione mandato di regolarizzazione
  *    PreCondition:
  *      E' stata generata la richiesta di creazione un Mandato di regolarizzazione e il mandato supera la validazione
  *      (metodo verificaMandato)
  *    PostCondition:
  *      Oltre alle PostCondition presenti in 'creazione mandato' viene generata in automatico una reversale
  *      di regolarizzazione (metodo creaReversaleDiregolarizzazione)
  *  creazione mandato di trasferimento di competenza
  *    PreCondition:
  *      E' stata generata la richiesta di creazione un Mandato di regolarizzazione e l'utente ha selezionato solo
  *      impegni di competenza
  *    PostCondition:
  *      Viene richiesto alla Component che gestisce i documenti amministrativi generici di creare un documento
  *      generico di spesa (di tipo TRASF_S) con tante righe quanti sono gli impegni selezionati dall'utente,
  *      viene creato un mandato di regolarizzazione di tipo competenza (metodo creaMandatoRegolarizzazione) con tante righe (metodo creaMandatoRiga)
  *      quanti sono gli impegni selezionati dall'utente. Con il metodo 'aggiornaImportoObbligazione'
  *		 vengono incrementati gli importi (im_associato_doc_contabili)
  *      degli impegni selezionati con l'importo trasferito nel mandato. Con il metodo 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
  *      capitoli di competenza degli impegni selezionati. Viene creata una reversale provvisoria di trasferimento per il Cds che
  *      beneficia del trasferimento (metodo 'creaReversaleDiRegolarizzazione')
  *  creazione mandato di trasferimento residuo
  *    PreCondition:
  *      E' stata generata la richiesta di creazione un Mandato di regolarizzazione e l'utente ha selezionato solo
  *      impegni residui
  *    PostCondition:
  *      Viene richiesto alla Component che gestisce i documenti amministrativi generici di creare un documento
  *      generico di spesa (di tipo TRASF_S) con tante righe quanti sono gli impegni selezionati dall'utente,
  *      viene creato un mandato di regolarizzazione di tipo residuo (metodo creaMandatoRegolarizzazione) con tante righe (metodo creaMandatoRiga)
  *      quanti sono gli impegni selezionati dall'utente. Con il metodo 'aggiornaImportoObbligazione'
  *		 vengono incrementati gli importi (im_associato_doc_contabili)
  *      degli impegni selezionati con l'importo trasferito nel mandato. Con il metodo 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
  *      capitoli residui degli impegni selezionati. Viene creata una reversale provvisoria di trasferimento per il Cds che
  *      beneficia del trasferimento (metodo 'creaReversaleDiRegolarizzazione')
  *  creazione di 2 mandati di trasferimento residuo+competenza
  *    PreCondition:
  *      E' stata generata la richiesta di creazione un Mandato di regolarizzazione e l'utente ha selezionato sia 
  *      impegni residui che di competenza
  *    PostCondition:
  *      Vengono creati 2 mandati uno di competenza e uno residuo e sono da considerarsi valide entrambe le
  *      postconditions: 'creazione mandato di trasferimento residuo' e 'creazione mandato di trasferimento competenza'
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da creare
  *
  * @return wizard il Mandato di Accreditamento creato
  * 		bulk il Mandato (ordinario) creato
*/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/* non usato */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  lista le coordinate bancarie 
  *    PreCondition:
  *      E' stato creata una riga di mandato di trasferimento
  *    PostCondition:
  *     La lista delle coordinate bancarie del terzo beneficiario del mandato viene estratta
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di trasferimento
  *
  * @return result la lista delle banche definite per il terzo beneficiario del mandato
  *			null non è stata definita nessuna banca per il terzo beneficiario del mandato
*/

public abstract List findBancaOptions (UserContext userContext,MandatoAccreditamentoBulk mandato) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException;
/** 
  *  find disponibilità di cassa capitolo
  *    PreCondition:
  *     E' stata richiesta la disponibilita di cassa per ogni capitolo di ogni obbligazione pagata dal mandato
  *    PostCondition:
  *     Viene restituita la disponibilità di cassa di ogni capitolo presente nel dettaglio delle scadenze delle obbligazioni
  *     pagate dal mandato
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoIBulk</code> il mandato di cui si verifica disponibilità di cassa sui capitoli
  *
*/

public List findDisponibilitaDiCassaPerCapitolo (UserContext userContext,MandatoBulk mandato) throws ComponentException;
/** 
  *  inizializzazione di una istanza di MandatoBulk
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di MandatoBulk
  *    PostCondition:
  *     Viene impostata la data di emissione del mandato con la data del Server
  *  inizializzazione di una istanza di MandatoAccreditamentoWizardBulk
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di MandatoAccreditamentoWizardBulk, l'oggetto bulk
  *     utilizzato come wizard per la generazione dei mandati di accreditamento
  *    PostCondition:
  *     Viene impostata la data di emissione del wizard con la data del Server, il Cds e l'UO di appartenenza con 
  *     il Cds e l'UO dell'Ente, il mandato terzo con il codice terzo che corrisponde al Cds beneficiario
  *     del mandato di accreditamento (metodo creaMandatoTerzoPerCds), viene impostata la lista degli impegni
  *     (metodo listaImpegniCNR) del CNR
  *  inizializzazione di una istanza di RicercaMandatoAccreditamentoBulk
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di RicercaMandatoAccreditamentoBulk, l'oggetto bulk
  *     utilizzato per visualizzare i dati di tutti i Cds verso cui emettere i mandati di accreditamento
  *    PostCondition:
  *     Viene impostata la disponibilità di cassa del CNR e viene inizializzata la lista dei Cds con la loro
  *     disponibilità di cassa
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per l'inserimento
  *
  * @return bulk <code>OggettoBulk</code> il Mandato inizializzato per l'inserimento
  *     
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di MandatoBulk per modifica
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di MandatoBulk
  *    PostCondition:
  *     Viene caricata la collezione delle righe di mandato (Mandato_rigaBulk), dei sospesi associati al mandato (Sospeso_det_uscBulk),
  *     delle associazioni mandato-reversale( Ass_mandato_reversaleBulk). Viene caricato i dati del beneficiario del mandato (Mandato_terzoBulk)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la modifica
  *
  * @return mandato il Mandato inizializzato per la modifica
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
  *    PostCondition:
  *     Viene inizializzato il Cds e l'UO di appartenenza con il Cds e l'UO del mandato
  *  inizializzazione di una istanza di CdsBilancioBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di CdsBilancioBulk, l'oggetto bulk che consente
  *     la visualizzazione del Bilancio entrate/spese del CdS
  *    PostCondition:
  *     Viene inizializzata la collezione delle voci di bilancio di un Cds
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la ricerca
  *
  * @return bulk il Mandato inizializzato per la ricerca
  *			bilancio istanza di <code>CdsBilancioBulk</code> che ha impostate le voci di bilancio di quel Cds
  *
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
  *    PostCondition:
  *     Viene inizializzato il Cds e l'UO di appartenenza con il Cds e l'UO del mandato
  *  inizializzazione di una istanza di CdsBilancioBulk per ricerca
  *    PreCondition:
  *     E' stata richiesta l'inizializzazione di una istanza di CdsBilancioBulk, l'oggetto bulk che consente
  *     la visualizzazione del Bilancio entrate/spese del CdS
  *    PostCondition:
  *     Viene inizializzata la collezione delle voci di bilancio di un Cds
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la ricerca
  *
  * @return bulk il Mandato inizializzato per la ricerca
  *			bilancio istanza di <code>CdsBilancioBulk</code> che ha impostate le voci di bilancio di quel Cds
  *
  *
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  ricerca documenti attivi per regolarizzazione
  *    PreCondition:
  *     E' stata richiesta la creazione di un mandato di regolarizzazione
  *     L'utente ha selezionato l'accertamento su cui creare in automatico la reversale di regolarizzazione
  *    PostCondition:
  *     Vengono ricercati tutti i documenti attivi che sono stati contabilizzati sulle scadenze di accertamento contabilizzate
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoIBulk</code> il mandato
  *
  * @return mandato il Mandato dopo la ricerca dei documenti attivi
  * 
  * 
*/
public abstract MandatoIBulk listaDocAttiviPerRegolarizzazione(UserContext aUC, MandatoIBulk mandato) throws ComponentException;
/** 
  *  ricerca documenti passivi
  *    PreCondition:
  *     E' stata richiesta la ricerca dei documenti passivi per cui e' possibile emettere un mandato
  *    PostCondition:
  *     Vengono ricercati tutti i documenti passivi che verificano le seguenti condizioni:
  *     - cds e uo origine uguali a cds e uo di scrivania
  *     - cds di appartenenza uguale al cds per cui si vuole emettere il mandato
  *     - (im_scadenza-im_associato_doc_contabile) della scadenza di obbligazione su cui il documento amm.
  *       e' stato contabilizzato maggiore di zero
  *     Fra tutti i documenti individuati vengono esclusi quelli che eventualmente sono già stati selezionati
  *     per questo mandato
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato
  *
  * @return mandato il Mandato emesso dopo la ricerca dei documenti passivi
  * 
  * 
*/

public abstract it.cnr.contab.doccont00.core.bulk.MandatoBulk listaDocPassivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  ricerca impegni CNR
  *    PreCondition:
  *     E' stata richiesta la ricerca degli impegni del CNR per emettere un mandato di accreditamento
  *     verso un Cds
  *    PostCondition:
  *     Vengono ricercati tutti gli impegni che hanno un importo disponibile ( importo disponibile = importo iniziale
  *     dell'impegno - importo già associato ai documenti contabili) e la cui voce del piano abbia come
  *     cd_proprio il codice del cds beneficiario del mandato di accreditamento e appartenga alla parte 1 del
  *     piano dei conti CNR parte spese
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param mandato <code>MandatoBulk</code> il mandato di accreditamento
  *
  * @return mandato il Mandato di accreditamento emesso dopo la ricerca degli impegni del CNR
  * 
*/

public abstract it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk listaImpegniCNR(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *   lista CdS - disp. cassa
  *    PreCondition:
  *     E' stata richiesta la disponibilità di cassa di tutti i Cds
  *    PostCondition:
  *     Vengono estratte le disponibilità di cassa di tutti i Cds per l'esercizio di scrivania
  *     calcolate nella vista V_DISP_CASSA_CDS
  *
  *  lista CdS - obbligazione
  *    PreCondition:
  *     E' stata richiesta la situazione delle obbligazione non pagate per alcuni Cds selezionati dall'utente
  *    PostCondition:
  *     Vengono sommati gli importi relative a scadenza di obbligazioni definitive (sia su partite di giro che non) 
  *     non ancora pagate per una certa data per tutti i cds selezionati dall'utente
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param ricerca <code>RicercaMandatoAccreditamentoBulk</code> il mandato di accreditamento
  *
  * @return ricerca il Mandato di accreditamento aggiornato in base alla situazione cassa del Cds
  * 
*/

public abstract it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk listaSituazioneCassaCds(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica mandato
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di un Mandato e il mandato supera la validazione
  *      (metodo verificaMandato)
  *    PostCondition:
  *      Vengono aggiornati gli importi dei sospesi eventualmente associati al mandato (metodo aggiornaImportoSospesi),
  *      e vengono aggiornate le eventuali modifiche alle modalità di pagamento e al tipo bollo del mandato
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il mandato da modificare
  *
  * @return mandato <code>OggettoBulk</code> il mandato modificato
*/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
public abstract MandatoBulk annullaMandato(UserContext userContext, MandatoBulk mandato, CompensoOptionRequestParameter param, boolean annullaCollegati,boolean riemissione ) throws ComponentException;
}
