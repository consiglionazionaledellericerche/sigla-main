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

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import java.util.*;
import java.util.Vector;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IObbligazioneMgr extends IDocumentoContabileMgr, ICRUDMgr
{

/**
 * Aggiornamento in differita dei saldi dell'obbligazione .
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un'obbligazione; i saldi di tale obbligazione non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione creata 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio che e' stata creata nel contesto transazionale del documento ammninistrativo ( progressivo
 *       obbligazione < 0)
 * Post: I saldi dell'obbligazione sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per obbligazione esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio che non e' stata creata nel contesto transazionale del documento ammninistrativo ( progressivo
 *       obbligazione > 0)
 * Post: I saldi dell'obbligazione sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	obbligazione	l'ObbligazioneBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'obbligazione e il "checkDisponibilitaCassaEseguito" che indica se
 *          l'utente ha richiesto la forzatura della disponibilità di cassa (parametro impostato dalla Gestione Obbligazione)
 * @param	param il parametro che indica se il controllo della disp. di cassa e' necessario (parametro impostato dalla Gestione dei doc. amm.)
*/

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      scadenza(n+1) esiste
  *      scadenza(n+1).importo > differenza in scadenza(n).importo
  *      scadenza(n+1) non ha documenti amministrativi associati
  *    PostCondition:
  *      Il sistema eseguirà l'aggiornamento dell'importo della scadenza successiva (n+1) dell'obbligazione aggiungendo la differenza fra il nuovo e vecchio importo della scadenza in aggiornamento. 
  *      La differenza è espressa come (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio)
  *  scadenza(n+1).importo <= differenza in scadenza(n).importo
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma l'aumento dell'importo della scadenza(n) supera il valore dell'importo dell'ultima scadenza dell'obbligazione. Una formula per questa condizione sarebbe (scadenzario(n+1).importo - (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio) > 0)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché l'aumento dell'importo della scadenza(n) è maggiore all'importo dell'ultima scadenza (cercarebbe settare l'importo <= 0). L'attività non è consentita.
  *  scadenza(n+1) non esiste
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma la scadenza in aggiornamento è l'ultima scadenza dell'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché non esiste una scadenza successiva. L'attività non è consentita.
  *  scadenza(n+1) ha doc amministrativi associati
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) 
  *      alla scadenza in elaborazione (scadenza(n)), ma la scadenza (n+1) ha documenti amministrativi associati
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la modifica della scadenza non è valida.
  *
  * @param aUC lo user context 
  * @param os l'istanza di  <code>Obbligazione_scadenzarioBulk</code> della quale deve essere individuata la scadenza successiva per aggiornarne l'importo
  * @return la scadenza successiva con l'importo modificato
 */

public abstract it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk aggiornaScadenzaSuccessivaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  Lo stato dell'obbligazione è Provvisoria e non esistono ordini
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria e non esiste nessun ordine associato all'obbligazione
  *    PostCondition:
  *      Il sistema eseguirà le seguente attività:
  *      1) L'aggiornamento dei saldi 'obbligazioni' dei capitoli di spesa CdS.
  *         (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *      2) L'eliminazione di ogni scadenza nello scadenzario dell'obbligazione,
  *      3) L'eliminazione dell'obbligazione propria.
  *
  *  Esiste un ordine per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione provvisoria e' stato definito un ordine
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per cancellare un'obbligazione provvisoria
  *      per la quale e' già stato emesso un ordine e' necessario prima cancellare l'ordine
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cancellare
 */

public abstract void cancellaObbligazioneProvvisoria(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lo stato dell'obbligazione è Provvisoria - esercizio ok
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria.
  *      L'esercizio di competenza dell'obbligazione e' uguale all'esercizio di creazione
  *    PostCondition:
  *      L'obbligazione viene aggiornata allo stato di 'Definitiva'.
  *
  *  Lo stato dell'obbligazione è Provvisoria - esercizio errore
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria.
  *      L'esercizio di competenza dell'obbligazione e' maggiore all'esercizio di creazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che un'obbligazione con esercizio competenza
  *      maggiore all'esercizio di creazione non può essere resa definitiva
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da confermare
  * @return ObbligazioneBulk l'obbligazione con lo stato modificato
  *  
 */

public abstract it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk confermaObbligazioneProvvisoria(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione non e' stata creata in un contesto transazionale
  *    PostCondition:
  *      L'obbligazione viene creata, i dettagli di tutte le scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione e' stata creata in un contesto transazionale
  *    PostCondition:
  *      L'obbligazione viene creata e i dettagli di tutte le sue scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) 
  *  Errore di verifica obbligazione
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli eseguiti dal metodo 'verificaObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa - forzatura
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'  
  *		 e l'utente ha scelto di forzare l'emissione dell'obbligazione
  *    PostCondition:
  *      L'obbligazione viene creata, i dettagli di tutte le scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da creare
  * @return l'istanza di  <code>ObbligazioneBulk</code> creata
  *   
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Obbligazione provvisoria
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di un' obbligazione che ha lo stato provvisorio
  *    PostCondition:
  *      L'obbligazione viene fisicamente cancellata tramite il metodo cancellaObbligazioneProvvisoria
  *
  *  Obbligazione definitiva
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di un' obbligazione che ha lo stato definitivo
  *    PostCondition:
  *      L'obbligazione viene stornata e cancellata logicamente tramite il metodo annullaObbligazioneDefinitiva
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da eliminare
  *  
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * ricerca ordine
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dell'ordine (se esiste) associato all'obbligazione
  *    PostCondition:
  *      L'ordine associato all'obbligazione viene restituito
  *
  * @param userContext it.cnr.jada.UserContext lo userContext
  * @param obblig ObbligazioneBulk l'oobligazione per la quale e' necessario individuare l'ordine
  * @return it.cnr.contab.doccont00.ordine.bulk.OrdineBulk l'ordine asssociato all'obbligazione oppure null se nessun ordine e'
  *         stato definito per l'obbligazione 
 */

public abstract it.cnr.contab.doccont00.ordine.bulk.OrdineBulk findOrdineFor(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione scadenza/modifica importo - imputazione automatica
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica dell'obbligazione e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per riaprtire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  creazione scadenza/modifica importo - imputazione manuale
  *    PreCondition:
  *      L'utente ha specificato l'imputazione manuale dell'obbligazione e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  conferma imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  conferma imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  modifica imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'obbligazione e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'obbligazione che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  modifica imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'obbligazione e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'obbligazione che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  Errore - imputazione automatica per linea att SINGOLA
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica, ha inoltre selezionato delle linee di attività dal piano di gestione 
  *      con categoria di dettaglio = SINGOLA e per le quali la somma delle colonne I,K,Q,S,U e' nullo
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare all'utente l'impossibilità di effettuare in automatico la
  *      ripartizione dell'importo della scadenza sulle linee di attività scelte
  *  Errore - imputazione automatica per linea att SCARICO
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica, ha inoltre selezionato delle linee di attività dal piano di gestione 
  *      con categoria di dettaglio = SCARICO e per le quali la somma delle colonne J,L,R,T e' nullo
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare all'utente l'impossibilità di effettuare in automatico la
  *      ripartizione dell'importo della scadenza sulle linee di attività scelte
  *  Errore - percentuali per nuove linee att.
  *    PreCondition:
  *      L'utente ha specificato solo delle linee di attività che non sono presenti nel piano di gestione e la somma
  *      delle percentuali inserite dall'utente da utilizzare nella ripartizione dell'importo di ogni scadenza e' diversa
  *      da 100.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare l'errore all'utente
  *  Errore - percentuali per nuove linee att. > 100
  *    PreCondition:
  *      L'utente ha specificato per le linee di attività che non sono presenti nel piano di gestione 
  *      delle percentuali  da utilizzare nella ripartizione dell'importo di ogni scadenza e la loro somma e'
  *      maggiore di 100
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare l'errore all'utente
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui creare i dettagli scadenza
  * @param scadenzario <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione per cui creare i dettagli oppure
  *        <code>null</code> se e' necessario generare i dettagli per tutte le scadenze
  *  
  *      
 */

public abstract it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk generaDettagliScadenzaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione prospetto
  *    PreCondition:
  *      L'utente richiede la visualizzazione del prospetto spese per una obbligazione.
  *    PostCondition:
  *      L'applicazione crea un report contenente la situazione 'spese' per una obbligazione e per i cdr che
  *      l'utente ha seelzionato.
  *      Il prospetto avrà una riga per ogni linea di attività relativa ai piani di gestione dei CdR 
  *      considerati nell'obbligazione. Il formatto sarà:
  *      
  *      Colonna 1: Linea di attività
  *      Colonna 2: Spese previste nel pdg, calcolati per il 1° esercizio = somma degli importi delle colonne (I), (K), (Q), (S) e (U)
  *      Colonna 3: Spese previste nel pdg, calcolati per il 2° esercizio = somma degli importi delle colonne (AC), (AE) e (AG)
  *      Colonna 4: Spese previste nel pdg, calcolati per il 3° esercizio = somma degli importi delle colonne (AC), (AE) e (AG)  
  *      Colonna 5: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 1° esercizio
  *      Colonna 6: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 2° esercizio
  *      Colonna 7: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 3° esercizio    
  *  valutazione prospetto
  *    PreCondition:
  *      I dati necessari per il prospetto sono stati raccolti.
  *    PostCondition:
  *      Il delta risultante dal prospetto (Colonna 5 - Colonna 2, Colonna 6 - Colonna 3, Colonna 7 - Colonna4 ) viene confrontato 
  *      con l'importo del totale delle linee di attività 
  *      appartenenti allo stesso CdR, nel caso che detto importo sia maggiore del delta risultante, 
  *      il sistema restituisce un messaggio di 'segnalazione' (non bloccante) con il quale avverte il responsabile 
  *      della possibilità di  'sfondamento'. Il controllo sarà ripetuto per ogni cdr coinvolto nei dettagli delle obbligazioni.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cdrList la lista di CdrBulk per cui generare il prospetto spese
  * @return la lista di <code>V_obblig_pdg_saldo_laBulk</code> coi dati relativi alle linee di attività dei Cdr selezionati
  *  
 */

public abstract java.util.List generaProspettoSpeseObbligazione(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare obbligazioni.
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      una istanza di ObbligazioneBulk viene restituita con impostata la data del giorno come data di emissione e
  *      il Cds da cui dipende l'UO di scrivania come Cds dell'obbligazione
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
  *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Obbligazione non esiste
  *    PreCondition:
  *      L'obbligazione richiesta non esiste.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'obbligazione non è stata trovata. L'attività non è consentita.
  *  Obbligazione trovata
  *    PreCondition:
  *      L'obbligazione richiesta è stata trovata.
  *    PostCondition:
  *      L'obbligazione viene caricata normalmente. L'imputazione finanziaria è impostata una volta sola al livello di testata, e poi vale per tutte le scadenze nello scadenzario. In questo caso l'applicazione ricava le informazione per l'imputazione finanziaria dalla prima scadenza dello scadenzario.
  *  Scadenzario dell'obbligazione non esiste
  *    PreCondition:
  *      L'obbligazione richiesta esiste, ma lo scadenzario per l'obbligazione non esiste.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che lo scadenzario non è stato trovato. L'attività non è consentita.
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
  *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'obbligazione con la data odierna, 
  *		 il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tipologia CdS è 'SAC'
  *    PreCondition:
  *      L'utente ha specificato una voce del piano in testata di una obbligazione appartenente al cds SAC
  *    PostCondition:
  *      L'elenco degli articoli di spesa CDS presenti nel piano dei conti Parte 1, aventi come titolo-capitolo la voce del piano selezionata dall'utente,
  *      viene presentato all'utente, evidenziandone la funzione
  *  Tipologia CdS è diverso da 'SAC'
  *    PreCondition:
  *      L'utente ha specificato una voce del piano in testata di una obbligazione appartenente ad un cds con tipologia diversa da SAC
  *    PostCondition:
  *      L'elenco dei capitoli di spesa CDS presenti nel piano dei conti Parte 1, aventi come titolo-capitolo la voce del piano selezionata dall'utente,
  *      viene presentato all'utente, evidenziandone la funzione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare i capitoli
  * @return ObbligazioneBulk l'obbligazione con i capitoli impostati
  *
 */

public abstract it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk listaCapitoliPerCdsVoce(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  CdS diverso da 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato dei capitoli di spesa CDS per un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Viene estratto l'elenco dei Cdr appartenenti all'uo di scrivania per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SINGOLA e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS diverso da 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato dei capitoli di spesa CDS per un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Viene estratto l'elenco dei Cdr appartenenti all'uo di scrivania per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SINGOLA e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente; a tale elenco viene aggiunto
  *      quello ottenuto estraendo i Cdr per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata appartiene all'uo di scrivania e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato degli articoli di spesa CDS per un'obbligazione appartenente al cds SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Fra tutti i Cdr selezionati implicitamente dall'utente con la selezione degli articoli viene estratto 
  *      l'elenco di quelli per i quali sono presenti nel piano di gestione delle linee di attività
  *      con categoria dettaglio = SINGOLA e
  *      il cui cdr e funzione sono uguali ad uno di quelli degli articoli selezionati dall'utente
  *  CdS 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato degli articoli di spesa CDS per un'obbligazione appartenente al cds SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Fra tutti i Cdr selezionati implicitamente dall'utente con la selezione degli articoli viene estratto 
  *      l'elenco di quelli per i quali sono presenti nel piano di gestione delle linee di attività
  *      con categoria dettaglio = SINGOLA e
  *      il cui cdr e funzione e' uguale ad una di quelle degli articoli selezionati dall'utente; a tale elenco viene aggiunto
  *      quello ottenuto estraendo i Cdr per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata ha cdr e funzione uguali
  *      ad uno di quelli selezionati dall'utente con la selezione degli articoli di spesa
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare i cdr
  * @return ObbligazioneBulk l'obbligazione con i cdr impostati
  
  */

public abstract java.util.Vector listaCdrPerCapitoli(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  CdS diverso da 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e funzione uguale ad una di quelle selezionate implicitamente dall'utente con la selezione dei capitoli.
  *  CdS diverso da 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e funzione uguale ad una di quelle selezionate implicitamente dall'utente con la selezione dei capitoli;
  *      a tale elenco viene aggiunto quello ottenuto estraendo le linee di attività presenti nel piano di gestione
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata appartiene all'uo di scrivania e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente al cds SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e cdr e funzione uguali ad uno di quelli selezionati implicitamente dall'utente con la selezione degli articoli.
  *  CdS 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente al cds SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e cdr e funzione uguali ad uno di quelli selezionati implicitamente dall'utente con la selezione degli articoli;
  *      a tale elenco viene aggiunto quello ottenuto estraendo le linee di attività presenti nel piano di gestione
  *      con categoria dettaglio = SCARICO il cui cdr e' uno di quelli selezionati dall'utente e la cui linea di attività 
  *      collegata ha cdr e funzione uguale ad uno di quelli selezionati implicitamente dall'utente 
  *      con la selezione degli articoli
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare le linee di attività
  * @return ObbligazioneBulk l'obbligazione con le linee di attività impostate
  *
  */

public abstract java.util.Vector listaLineeAttivitaPerCapitoliCdr(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza <code>Obbligazione_scadenzarioBulk</code> da mettere in lock
  *
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione non e' stata modificata in un contesto transazionale  
  *    PostCondition:
  *      L'obbligazione viene aggiornata
  *		 I dettagli di tutte le scadenze vengono aggiornati (metodo generaDettagliScadenzaObbligazione) 
  *      I saldi dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *      Lo stato COFI/COGE degli eventuali doc. amministrativi associati all'obbligazione e' stato aggiornato
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione e' stata modificata in un contesto transazionale  
  *    PostCondition:
  *      L'obbligazione viene aggiornata e i dettagli di tutte le scadenze vengono aggiornati (metodo generaDettagliScadenzaObbligazione) 
  *  Errore di verifica obbligazione
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli eseguiti dal metodo 'verificaObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa - forzatura
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'  
  *		 e l'utente ha scelto di forzare l'emissione dell'obbligazione
  *    PostCondition:
  *      L'obbligazione viene modificata, i dettagli di tutte le scadenze vengono modificati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da modificare
  * @return l'istanza di  <code>ObbligazioneBulk</code> modificata
  *  
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
 * Modifica l'importo di una scadenza e aggiunge la differenza alla scadenza successiva oppure modifica l'importo di una
 * scadenza e l'importo della testata dell'obbligazione
 *	
 * Pre-post-conditions:
 *
 * Nome: Modifica Scadenza 
 * Pre:  E' stata generata la richiesta di modifica l'importo di una scadenza 
 * Post: L'importo della scadenza e della testata dell'obbligazione sono stati modificati
 *
 * Nome: Modifica Scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la differenza fra il nuovo importo
 *       e l'importo precedente deve essere riportato sulla scadenza successiva
 * Post: L'importo della scadenza e della scadenza successiva sono stati modificati
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e non esiste una scadenza
 *       successiva su cui scaricare la differenza fra l'importo attuale scadenza e il nuovo importo
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva -  Errore importo scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e (im_scadenza_successisva -
 *       nuovo_im_scadenza + im_scadenza) e' minore di 0
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 * 
 * Nome: Scadenza successiva -  Errore doc amministrativi associati
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la scadenza successiva ha 
 *       già dei documenti amministrativi associati
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Errore imputazione manuale
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e l'imputazione finanziaria
 *       dell'obbligazione non e' automatica
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Obbligazione_scadenzarioBulk il cui importo deve essere modificato
 * @param nuovoImporto il valore del nuovo importo che la scadenza di obbligazione dovrà assumere
 * @param modificaScadenzaSuccessiva il flag che indica se modificare la testata dell'obbligazione o modificare la scadenza
 *        successiva dell'obbligazione
 * @return l'istanza di Obbligazione_scadenzarioBulk con l'importo modificato
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Non esistono documenti amministrativi per l'obbligazione.
  *      Non esitono ordini associati all'obbligazione
  *      Lo stato dell'obbligazione è 'DEFINITIVA'
  *    PostCondition:
  *      Il sistema eseguirà le seguente attività:
  *      1) L'aggiornamento dei saldi 'obbligazioni' dei capitoli di spesa CdS 
  *         (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *      2) L'azzeramento dell'importo di ogni dettaglio di ogni scadenza dell'obbligazione,  
  *      3) L'azzeramento dell'importo di ogni scadenza dell'obbligazione,
  *      4) L'azzeramento dell'importo dell'obbligazione propria,
  *      5) L'aggiornamento dello stato dell'obbligazione a 'STORNATA'.
  *      
  *  Esistono documenti amministrativi per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione definitiva ci sono documenti amministrativi già collegati all'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per stornare una obbligazione definitiva, 
  *      qualsiasi documento amministrativo collegato all'obbligazione deve essere sganciato prima di eseguire 
  *      lo storno. L'attività non è consentita.
  *
  *  Esiste un ordine per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione definitiva e' stato definito un ordine
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per stornare una obbligazione definitiva 
  *      per la quale e' già stato emesso un ordine e' necessario prima cancellare l'ordine
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da annullare
  * @return l'istanza di  <code>ObbligazioneBulk</code> annullata
  *  
 */

public abstract it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk stornaObbligazioneDefinitiva(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Linea attività in Pdg
  *    PreCondition:
  *      L'utente ha selezionato una nuova linea di attività e la nuova linea di attività e' nel Piano di Gestione
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di assegnare come nuova linea di attività
  *      una presente nel P.d.G.
  *  Linea attività in Pdg
  *    PreCondition:
  *      L'utente ha selezionato una nuova linea di attività e la nuova linea di attività non e' presente nel Piano di Gestione
  *    PostCondition:
  *      La nuova linea di attività ha superato la validazione
  *
  * @param userContext lo user context 
  * @param latt l'istanza di  <code>Linea_attivitaBulk</code> da verificare
  *  
 */

public abstract void verificaNuovaLineaAttivita(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati - anno di creazione obbligazione < anno competenza dell'obbligazione
  *    PreCondition:
  *      testata dell'obbligazione verificata (controllato nel metodo verificaTestataObbligazione).
  *      sum(scadenzario.importo) = obbligazione.importo
  *      sum(scad_voce.importo)  = scadenzario.importo
  *		 dettagli d'imputazione finanziaria specificati
  *      almeno una scadenza definita
  *      verfiche per spese per costi altrui superate (metodo verificaFl_spese_costi_altrui)
  *      L'anno di competenza dell'obbligazione è superiore all'anno di creazione dell'obbligazione
  *    PostCondition:
  *      Il sistema può proseguire con la creazione/modifica dell'obbligazione, ma non verranno aggiornati i saldi
  *      dei capitoli di spesa CdS.
  *
  *  Tutti controlli superati - anno di creazione obbligazione = anno competenza
  *    PreCondition:
  *      testata dell'obbligazione verificata (controllato nel metodo verificaTestataObbligazione).
  *      sum(scadenzario.import) = obbligazione.import.
  *      sum(scad_voce.importo)  = scadenzario.importo
  *		 dettagli d'imputazione finanziaria specificati
  *      almeno una scadenza definita
  *      verfiche per spese per costi altrui superate (metodo verificaFl_spese_costi_altrui)  
  *      L'anno di competenza dell'obbligazione è uguale all'anno di creazione dell'obbligazione
  *    PostCondition:
  *      Il sistema può proseguire con la creazione/modifica dell'obbligazione e dovrà effettuare l'aggiornamento
  *      dei saldi dei capitoli di spesa CdS. (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *
  *  sum(scadenzario.importo) not = obbligazione.importo
  *    PreCondition:
  *      La somma degli importi delle scadenze dell'obbligazione non è uguale all'importo dell'obbligazione in elaborazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se l'importo non è uguale alla somma degli importi delle scadenze dell'obbligazione.
  *
  *  sum(scad_voce.importo) not = scadenzario.importo
  *    PreCondition:
  *      L'utente ha selezionato l'imputazione manuale degli importi dei dettagli delle scadenze e la somma degli importi 
  *      dei dettagli di una scadenza dell'obbligazione non è uguale all'importo della scadenza
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se l'importo della scadenza non è uguale alla somma degli importi dei dettagli della scadenza dell'obbligazione.
  *
  *  dettagli d'imputazione finanziaria non specificati al livello di obbligazione
  *    PreCondition:
  *      I dettagli d'imputazione finanziaria (capitolo di spesa, linea d'attività) non sono stati specificati 
  *      al livello di obbligazione 
  *    PostCondition:
  *      Il sistema segnala l'impossibilità di craere/aggiornare l'obbligazione fino a quando l'imputazione finanziaria non viene completata
  * 
  *  scadenze non definite
  *    PreCondition:
  *      Non sono state definite scadenze per l'obbligazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se non viene definita almento una scadenza
  *
  *  spese per costi altrui
  *    PreCondition:
  *      L'utente ha specificato di voler emettere un'obbligazione non di tipo spese per costi altrui
  *      ma ha selezionato linee di attività appartenenti a cdr che non sipendono dall'uo di scrivania
  *      (questo controllo viene effettuato dal metodo 'verificaFl_spese_costi_altrui')
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> da verificare
  *  
  *
 */

public abstract void verificaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati - creazione
  *    PreCondition:
  *      Non esiste già una scadenza per la data.
  *      Attività = creazione
  *    PostCondition:
  *      Alla scrittura dell'obbligazione il sistema aggiungerà questo scadenzario e genererà tutti i dettagli della
  *      scadenza (metodo 'generaDettagliScadenzaObbligazione')
  *  Tutti controlli superati - aggiornamento con agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'obbligazione il sistema aggiornerà questo scadenzario. 
  *      In più, il metodo aggiornaScadenzaSuccessivaObbligazione viene utilizzato per aggiornare la scadenza successiva 
  *      a quella in aggiornamento. 
  *  Tutti controlli superati - aggiornamento senza agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente NON ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'obbligazione il sistema aggiornerà questo scadenzario. 
  *      Sarà il compito dell'utente aggiornare una delle scadenze per garantire che la somma degli importi 
  *      delle scadenze sia uguale all'importo dell'obbligazione.
  *  creazione/modifica - esiste già una scadenza per la data
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica la data di una scadenza. 
  *      Per la data scadenza specificata esiste già una scadenza per l'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data della scadenza non è valida.
  *  creazione/modifica - importo negativo
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica l'importo di una scadenza
  *      Il nuovo importo e' negativo.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo della scadenza deve essere > 0
  *  creazione/modifica - importo nullo 
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica l'importo di una scadenza
  *      Il nuovo importo e' nulla e la scadenza non è associata a documenti amministrativi
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo della scadenza deve essere >= 0
  *  modifica - la scadenza ha doc amministrativi associati e non proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati
  *      e la richiesta non proviene dal BusinessProcess che gestisce i documenti amministrativi  
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la modifica della scadenza non è valida.
  *  modifica - la scadenza ha doc amministrativi associati e proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati e la
  *      richiesta proviene dal BusinessProcess che gestisce i documenti amministartivi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza e' consentito
  *  modifica - la scadenza ha mandati associati
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza 
  *      La scadenza ha mandati associati
  *      La richiesta di modifica proviene dal BusinessProcess che gestisce i documenti amministrativi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza non e' consentito
  
  * @param aUC lo user context 
  * @param scadenzario l'istanza di  <code>Obbligazione_scadenzarioBulk</code> da verificare
  * @return l' ObbligazioneBulk a cui appartiene la scadenza
  *
  */

public abstract it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk verificaScadenzarioObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException;
public EsercizioBulk  verificaStatoEsercizio( UserContext userContext, String cd_cds, Integer es ) throws ComponentException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Copertura finanziaria è sufficiente.
  *      Data obbligazione è valida.
  *      Esercizio competenza >= esercizio creazione dell'obbligazione
  *    PostCondition:
  *      La testata dell'obbligazione è valida. E' consentito eseguire l'attività di salvataggio o di passaggio 
  *		alle pagine successive (Configurazione Imputazione Finanziaria o Scadenzario).
  *  Copertura finanziaria insufficiente
  *    PreCondition:
  *      La copertura finanziaria in riferimento al limite di assunzione di obbligazioni risulta insufficiente.
  *      Il controllo della copertura finanziaria dipende dal metodo 'controllaCoperturaFinanziariaObbligazione', 
  *      che calcola un valore per questo limite secondo i dettagli dell'obbligazione in aggiornamento. 
  *      Per l'aggiornamento, il valore del limite viene confrontato con la differenza fra l'importo vecchio e 
  *      l'importo nuovo dell'obbligazione, quando l'importo nuovo supera l'importo vecchio. 
  *      (Se l'importo rimane uguale, o diminuisce, questo controllo della copertura finanziaria non viene eseguito.)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo dell'obbligazione in aggiornamento supera 
  *      il limite di assunzione di obbligazioni. L'attività non è consentita.
  *  Data obbligazione non è valida
  *    PreCondition:
  *      La data dell'obbligazione in inserimento antecede la data dell'ultima obbligazione inserita per questo CdS. 
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data dell'obbligazione non può essere antecedente
  *      la data dell'ultima obbligazione inserita per questo CdS. L'attività non è consentita.
  *  Esercizio competenza non valido
  *    PreCondition:
  *      L'esercizio di competenza dell'obbligazione e' inferiore all'esercizio di scrivania e quindi all'esercizio
  *      di creazione dell'obbligazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'esercizio di competenza non può essere antecedente
  *      all'esercizio di creazione dell'obbligazione
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per cui verificare la testata
  *  
 */

public abstract void verificaTestataObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException;
}
