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

public interface IObbligazionePGiroMgr extends IDocumentoContabileMgr, ICRUDMgr
{

/**
 * Aggiornamento in differita dei saldi dell'obbligazione su partita di giro.
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un impegno pgiro; i saldi di tale impegno non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro creata 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'obbligazione su capitoli di
 *       partita di giro che e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro < 0)
 * Post: I saldi dell'obbligazione pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'obbligazione su capitoli di
 *       partita di giro che non e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro > 0)
 * Post: I saldi dell'obbligazione pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ImpegnoPGiroBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'impegno 
 * @param	param parametro non utilizzato per le partite di giro
 * 
*/

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un impegno su partita di giro
  *    PostCondition:
  *     Alla component che gestisce l'accertamento su pgiro viene inoltrata la richiesta di cancellazione (logica) 
  *		dell'accertamento associato all'accertamento pgiro (metodo eliminaAccertamento), l'obbligazione (con la sua 
  *		scadenza e il suo dettaglio scadenza) viene cancellata (metodo eliminaObbligazione)
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  * @return imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro annullato
  *
 */
public abstract it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk annullaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  creazione
  *    PreCondition:
  *      L'utente richiede la creazione di un nuovo impegno su partita di giro
  *    PostCondition:
  *      L'impegno, dopo essere stato validato (metodo verificaObbligazione), viene creato e in automatico viene creata una 
  *		 scadenza (metodo creaObbligazione_scadenzario) e un dettaglio di scadenza (metodo creaObbligazione_scad_voce). 
  *		 I saldi relativi alla voce del piano dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione). 
  *		 Alla componente che gestisce gli AccertamentiPGiro viene chiesta la creazione di un Accertamento (metodo creaAccertamento). 
  *		 Viene creata l'associazione (Ass_obb_acr_pgiroBulk) fra l'impegno e l'accertamento su partita di giro (metodo creaAss_obb_acr_pgiro)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da creare
  *
  * @return imp_pgiro L'impegno su partita di giro creato
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione per il Cds
  *    PreCondition:
  *      Un Accertamento su partita di giro per il Cds e' stato creato ed e' necessario creare il corrispondente Impegno
  *    PostCondition:
  *      L'impegno (ImpegnoPGiroBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, cds e unità
  *		 organizzativa di appartenenza e di origine uguali a quelli dell'accertamento, data di scadenza uguale a 
  *		 quella della scadenza dell'accertamento su partita di giro.
  *      Viene inoltre creata una scadenza (metodo creaObbligazione_scadenzario) e
  *      un dettaglio di scadenza (metodo creaObbligazione_scad_voce). I saldi relativi alla voce del piano
  *      dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *		 Viene infine validata l'Obbligazione prima della sua creazione (metodo verificaObbligazione)
  *  creazione per il CNR
  *    PreCondition:
  *      Un Accertamento su partita di giro per il CNR e' stato creato ed e' necessario creare il corrispondente Impegno
  *    PostCondition:
  *      L'impegno (ImpegnoPGiroBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, cds e unità
  *		 organizzativa di appartenenza uguali a quelli dell'accertamento e cds e unità organizzativa di origine
  *		 uguali a quelli dell'ente.
  *      Viene inoltre creata una scadenza (metodo creaObbligazione_scadenzario) e
  *      un dettaglio di scadenza (metodo creaObbligazione_scad_voce). I saldi relativi alla voce del piano
  *      dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *		 Viene infine validata l'Obbligazione prima della sua creazione (metodo verificaObbligazione)
  *  errore - Configurazione CNR per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Configurazione CNR la definizione del CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR del CODICE DIVERSI per PGIRO
  *  errore - Anagrafica per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Anagrafica il codice terzo presente in Configurazione CNR come CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Anagrafica
  *  errore - Associazione capitoli entrata/spese
  *    PreCondition:
  *      non e' presente (Ass_partita_giroBulk) l'associazione fra il capitolo di entrata dell'accertamento e un capitolo di spesa
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'assenza dell'associazione
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro creato
  *
  * @return imp_pgiro L'impegno su partita di giro creato in corrispondenza dell'accertamento
*/

public abstract it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk creaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un impegno su partita di giro
  *    PostCondition:
  *     Viene inoltrata la richiesta di cancellazione (logica) dell'impegno su partita di giro
  *		(metodo annullaObbligazione)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  *
 */
public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  cancellazione
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata
  *    PostCondition:
  *      L'obbligazione, la sua scadenza e il suo dettaglio vengono cancellati. I saldi relativi ai documenti
  *      contabili vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro da cancellare
  *
 */

public abstract void eliminaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'obbligazione con la data odierna, 
  *		 il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *  inizializzazione per inserimento - errore
  *    PreCondition:
  *      L'unità organizzativa è uguale a quella dell'Ente
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente che l'Ente non è abilitato a creare documenti su partita di giro
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'impegno su partita di giro inizializzato per l'inserimento
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'impegno.
  *      Viene recuperata l'associazione fra l'impegno e l'accertamento
  *		 Viene recuperato l'accertamento associato all'impegno e la relativa scadenza e dettaglio scadenza
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per la modifica
  *
  * @return imp_pgiro l'impegno su partita di giro inizializzato per la modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per ricerca
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per ricerca
  *      e' stata generata
  *    PostCondition:
  *      Vengono impostati il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per la ricerca
  *
  * @return <code>OggettoBulk</code> l'impegno su partita di giro inizializzato per la ricerca
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
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
  *      L'importo dell'impegno viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati
  *		 (metodo aggiornaCapitoloSaldoObbligazione).
  *      Alla component che gestisce l'accertamento su partita di giro viene inoltrata la richiesta di modifica
  *      dell'accertamento associato all'impegno (metodo modificaAccertamento)
  *		 Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		 (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un impegno su partita di giro
  *    PostCondition:
  *      La descrizione dell'obbligazione e della scadenza di obbligazione vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro
  *    PostCondition:
  *      Il capitolo viene aggiornato e viene inoltrata la richiesta di modifica del capitolo
  *      dell'accertamento associato all'impegno (metodo modificaAccertamento)
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un'obbligazione su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'obbligazione su partita di giro viene aggiornata.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da modificare
  *
  * @return imp_pgiro <code>OggettoBulk</code> l'impegno su partita di giro modificato
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un accertamento su partita di giro
  *    PostCondition:
  *      L'importo dell'impegno associato all'accertamento pgiro viene modificato e, in cascata, vengono modificati gli importi 
  *      relativi alla scadenza e al dettaglio scadenza dell'obbligazione. I saldi relativi ai documenti contabili vengono
  *		 aggiornati (metodo aggiornaCapitoloSaldoObbligazione). Vengono aggiornati gli stati COAN e COGE degli eventuali
  *		 documenti amministrativi associati (metodo aggiornaStatoCOAN_COGEDocAmm).
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione associata all'accertamento pgiro
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro
  *    PostCondition:
  *      Il capitolo dell'impegno associato all'accertamento pgiro viene aggiornato col valore
  *      presente nell'associazione fra capitolo di spesa e capitolo di entrata su partite di giro
  *  modifica capitolo - errore
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro ma non esiste
  *      l'associazione fra il nuovo capitolo di entrata dell'accertamento pgiro e un capitolo di spesa
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un accertamento su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'obbligazione associata all'accertamento su partita di giro viene aggiornata.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro a cui è associato l'impegno pgiro da modificare
  *
  * @return imp_pgiro l'impegno su partita di giro modificato
 */

public abstract it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk modificaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
 * Modifica l'importo di una scadenza e della testata dell'obbligazione
 *	
 * Pre-post-conditions:
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica della scadenza successiva
 * Post: Viene generata un'ApplicationException in quanto le obbligazioni su partita di giro hanno un'unica scadenza
 *
 * Nome: Modifica scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza
 * Post: Vengono aggiornati l'importo in testata, in scadenza e in scad_voce e la controparte per l'accertamento su pgiro
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad la scadenza (con importo originale)
 * @param nuovoImporto che deve assumere la scadenza
 * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'obbligazione non deve essere modificata
 *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
 *									 scadenza successiva
 * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
 *                                   dell'obbligazione
 * @return la scadenza 
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
}
