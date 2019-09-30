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

import java.math.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

import java.util.Vector;

public interface IAccertamentoPGiroMgr extends IDocumentoContabileMgr, ICRUDMgr
{

/**
 * Aggiornamento in differita dei saldi dell'accertamento su partita di giro.
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un accertamento pgiro; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbero l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro creato 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro che e' stato creato nel contesto transazionale del documento amministrativo ( progressivo
 *       accertamento pgiro < 0)
 * Post: I saldi dell'accertamento pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro che non e' stato creato nel contesto transazionale del documento amministrativo ( progressivo
 *       accertamento pgiro > 0)
 * Post: I saldi dell'accertamento pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo AccertamentoPGiroBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti
 *
*/

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un accertamento su partita di giro
  *    PostCondition:
  *     Alla component che gestisce l'obbligazione su pgiro viene inoltrata la richiesta di cancellazione (logica) 
  *		dell'obbligazione associata all'impegno (metodo eliminaObbligazione), l'accertamento (con la sua scadenza 
  *		e il suo dettaglio scadenza) viene cancellato (metodo eliminaAccertamento)
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da cancellare (logicamente)
  * @return accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro annullato
  *
 */
public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk annullaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  creazione
  *    PreCondition:
  *      Un Impegno su partita di giro e' stato creato ed e' necessario creare il corrispondente Accertamento
  *    PostCondition:
  *      L'accertamento (AccertamentoPGiroBulk) viene creato con importo pari a quello dell'impegno, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, data di
  *		 scadenza uguale a quella della scadenza dell'obbligazione su partita di giro.
  *      Viene inoltre creata una scadenza (metodo creaAccertamento_scadenzario) e
  *      un dettaglio di scadenza (metodo creaAccertamento_scad_voce). I saldi relativi alla voce del piano
  *      dell'accertamento vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *		 Viene infine validato l'Accertamento prima della sua creazione (metodo verificaAccertamento)
  *  errore - Configurazione CNR per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Configurazione CNR la definizione del CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR del CODICE DIVERSI 
  *		 per PGIRO
  *  errore - Anagrafica per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Anagrafica il codice terzo presente in Configurazione CNR come CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Anagrafica
  *  errore - Associazione capitoli entrata/spese
  *    PreCondition:
  *      non e' presente (Ass_partita_giroBulk) l'associazione fra il capitolo di spesa dell'impegno e un capitolo 
  *		 di entrata
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'assenza dell'associazione
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro creato
  *
  * @return accert_pgiro L'accertamento su partita di giro creato in corrispondenza dell'impegno
*/

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk creaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
public abstract AccertamentoPGiroBulk creaAccertamentoDiIncassoIVA( UserContext userContext, ReversaleBulk reversale,boolean split ) throws ComponentException;
/** 
  *  creazione
  *    PreCondition:
  *      L'utente richiede la creazione di un nuovo accertamento su partita di giro
  *    PostCondition:
  *      L'accertamento, dopo essere stato validato (metodo verificaAccertamento), viene creato e in automatico 
  *		 viene creata una scadenza (metodo creaAccertamento_scadenzario) e
  *      un dettaglio di scadenza (metodo creaAccertamento_scad_voce). I saldi relativi alla voce del piano
  *      dell'accertamento vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento). Alla componente che 
  *		 gestisce gli ImpegniPGiro viene chiesta la creazione di un Impegno (metodo creaObbligazione). 
  *		 Viene creata l'associazione (Ass_obb_acr_pgiroBulk) fra l'accertamento
  *      e l'impegno su partita di giro (metodo creaAss_obb_acr_pgiro)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da creare
  *
  * @return accert_pgiro L'accertamento su partita di giro creato
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione
  *    PreCondition:
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata
  *    PostCondition:
  *      L'accertamento, la sua scadenza e il suo dettaglio vengono cancellati. I saldi relativi ai documenti
  *      contabili vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da cancellare
  *
 */

public abstract void eliminaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un accertamento su partita di giro
  *    PostCondition:
  *     Viene inoltrata la richiesta di cancellazione (logica) dell'accertamento su partita di giro
  *		(metodo annullaAccertamento)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da cancellare (logicamente)
  *
 */
public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un AccertamentoPGiroBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'accertamento con la data odierna, il codice Cds e il codice Cds di origine 
  *      con il codice Cds di scrivania
  *  inizializzazione per inserimento - errore
  *    PreCondition:
  *      L'unità organizzativa è uguale a quella dell'Ente
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente che l'Ente non è abilitato a creare documenti su partita di giro
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'accertamento su partita di giro inizializzato per l'inserimento
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un AccertamentoPGiroBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'accertamento.
  *      Viene recuperata l'associazione fra l'accertamento e l'impegno
  *		 Viene recuperato l'impegno associato all'accertamento e la relativa scadenza e dettaglio scadenza
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da inizializzare per la modifica
  *
  * @return accert_pgiro l'accertamento su partita di giro inizializzato per la modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un accertamento
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un impegno su partita di giro
  *    PostCondition:
  *      L'importo dell'accertamento associato all'impegno viene modificato e, in cascata, vengono modificati gli importi 
  *      relativi alla scadenza e al dettaglio scadenza dell'accertamento. I saldi relativi ai documenti contabili vengono
  *		 aggiornati (metodo aggiornaCapitoloSaldoAccertamento). Vengono aggiornati gli stati COAN e COGE degli eventuali
  *		 documenti amministrativi associati (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento associato all'impegno
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro
  *    PostCondition:
  *      Il capitolo dell'accertamento associato all'impegno viene aggiornato col valore
  *      presente nell'associazione fra capitolo di spesa e capitolo di entrata su partite di giro
  *  modifica capitolo - errore
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro ma non esiste
  *      l'associazione fra il nuovo capitolo di spesa dell'impegno e un capitolo di entrata
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un'obbligazione su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'accertamento associato all'impegno viene aggiornata.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro a cui è associato l'accertamento pgiro da modificare
  *
  * @return accert_pgiro l'accertamento su partita di giro modificato
 */

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk modificaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un accertamento su partita di giro
  *    PostCondition:
  *      L'importo dell'accertamento viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati.
  *      Alla component che gestisce l'obbligazione su partita di giro viene inoltrata la richiesta di modifica
  *      dell'obbligazione associata all'accertamento (metodo modificaObbligazione)
  *		 I saldi relativi ai documenti contabili vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *		 Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		 (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un accertamento su partita di giro
  *    PostCondition:
  *      La descrizione dell'accertamento e della scadenza di accertamento vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro
  *    PostCondition:
  *      Il capitolo viene aggiornato e viene inoltrata la richiesta di modifica del capitolo
  *      dell'obbligazione associata all'accertamento (metodo modificaObbligazione)
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un accertamento su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'accertamento su partita di giro viene aggiornata.
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da modificare
  *
  * @return accert_pgiro <code>OggettoBulk</code> l'accertamento su partita di giro modificato
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
 * Modifica l'importo di una scadenza e della testata dell'accertamento
 *	
 * Pre-post-conditions:
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica della scadenza successiva
 * Post: Viene generata un'ApplicationException in quanto gli accertamenti su partita di giro hanno un'unica scadenza
 *
 * Nome: Modifica scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza
 * Post: Vengono aggiornati l'importo in testata, in scadenza e in scad_voce e la controparte per l'impegno
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad (con importo originale)
 * @param nuovoImporto che deve assumere la scadenza
 * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'accertamento non deve essere modificata
 *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
 *									 scadenza successiva
 * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
 *                                   dell'accertamento
 * @return la scadenza 
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
}
