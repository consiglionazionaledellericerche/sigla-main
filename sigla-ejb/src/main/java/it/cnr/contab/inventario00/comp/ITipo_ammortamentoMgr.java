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

package it.cnr.contab.inventario00.comp;

import it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface ITipo_ammortamentoMgr extends ICRUDMgr
{


/** 
  *  Annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare tutte le operazioni fatte sul Tipo Ammortamento.
  *    PostCondition:
  *      Viene effettuata un rollback fino al punto (SavePoint) impostato in precedenza, 
  *		(metodo inizializzaGruppiPerModifica)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/

public abstract void annullaModificaGruppi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/** 
  * Associa tutte le Categorie Gruppo disponibili.
  *    PreCondition:
  *      Si sta tentando di creare un nuovo Tipo Ammortamento. E' stata generata la richiesta 
  *		di associare tutte Categorie Gruppo disponibili.
  *    PostCondition:
  *      Vengono riportati sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG tutte le Categorie Gruppo disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
  * @param gruppiSelezionati la <code>List</code> lista dei Gruppi Selezionati.
**/

public abstract void associaTuttiGruppi(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Cerca tutte le Categorie Gruppo disponibili
  *    PreCondition:
  *      E' stata generata la richiesta di cercare tutte le Categorie Gruppo che rispondono alle caratteristiche 
  *		per essere associati al Tipo Ammortamento.
  *		I gruppi disponibli sono rispondono alle seguenti caratteristiche:
  *		  	- siano soggetti a gestione Inventario, (FL_GESTIONE_INVENTARIO = 'Y');
  *			- siano soggetti ad ammortamento, (FL_AMMORTAMENTO = 'Y');
  *			- siano di livello maggiore di 0, (ossia siano Gruppi e non Categorie);
  *			- NON siano già associati ad altri Tipi Ammortamento;
  *			- NON siano stati cacellati logicamente.
  *    PostCondition:
  *     Viene costruito e restituito l'Iteratore sui gruppi disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento <code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui gruppi trovati.
**/

public abstract it.cnr.jada.util.RemoteIterator cercaGruppiAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Cerca tutte le Categorie Gruppo disponibili poer modifica
  *    PreCondition:
  *      E' stata generata la richiesta di cercare tutte le Categorie Gruppo che rispondono alle caratteristiche 
  *		per essere associati per la modifica di Tipo Ammortamento.
  *		I gruppi disponibli sono rispondono alle seguenti caratteristiche:
  *		  	- siano soggetti a gestione Inventario, (FL_GESTIONE_INVENTARIO = 'Y');
  *			- siano soggetti ad ammortamento, (FL_AMMORTAMENTO = 'Y');
  *			- siano di livello maggiore di 0, (ossia siano Gruppi e non Categorie);
  *			- NON siano già associati ad altri Tipi Ammortamento;
  *			- NON siano stati cacellati logicamente.
  *    PostCondition:
  *     Viene costruito e restituito l'Iteratore sui gruppi disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento <code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui gruppi trovati.
**/

public abstract it.cnr.jada.util.RemoteIterator cercaGruppiAssociabiliPerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Validazione del Tipo Ammortamento
  *    PreCondition:
  *      validaTipo_Ammortamento non superato.
  *    PostCondition:
  *      Un messaggio di errore viene mostrato all'utente. Non  viene consentita la registrazione del Tipo Ammortamento.
  *
  *  Validazione del codice del Tipo Ammortamento
  *    PreCondition:
  *      Il codice specificato per il Tipo Ammortamento è già utilizzato.
  *    PostCondition:
  *      Un messaggio di errore viene mostrato all'utente. Non  viene consentita la registrazione del Tipo Ammortamento.
  * 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata richiesta la creazione di un nuovo Tipo Ammortamento.
  *    PostCondition:
  *      Viene consentito il salvataggio del Tipo Ammortamento. Vengono salvate le associazioni 
  *		che l'utente ha specificato tra il Tipo Ammortamento e le Categorie Gruppo Inventario.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return l'oggetto <code>OggettoBulk</code> creato
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Elimina un Tipo Ammortamento
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare un Tipo Ammortamento.
  *    PostCondition:
  *      Vengono eliminati fisicamente i gruppi associati al Tipo Ammortamento specificato, ( eliminaGruppiConBulk(UserContext, Tipo_ammortamentoBulk) ).
  *		Viene cancellato logicamente il Tipo Ammortamento.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
  * @param bulk <code>OggettoBulk</code> il Tipo Ammortamento da eliminare.
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Elimina TUTTI i Gruppi associati al Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare tutte le associazioni fatte durante 
  *		una sessione di lavoro.
  *    PostCondition:
  *      Vengono cancellate dalla tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_APG, i gruppi associati
  *		al Tipo Ammortamento.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
**/

public abstract void eliminaGruppiConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Elimina dei Gruppi associati al Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare alcune associazioni fatte durante 
  *		la sessione di lavoro.
  *    PostCondition:
  *      Vengono cancellate dalla tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_APG, i gruppi 
  *		specificati.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
  * @param gruppi <code>OggettoBulk[]</code> i gruppi da eliminare.
**/

public abstract void eliminaGruppiConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2) throws it.cnr.jada.comp.ComponentException;
/** 
  * Cerca un Tipo Ammortamento per riassocia.
  *    PreCondition:
  *      E' stata generata la richiesta di riassociare i Gruppi di un Tipo Ammortamento ad 
  *		un altro Tipo Ammortamento.
  *    PostCondition:
  *      Viene restituito un Iteratore sui tipi ammortamento disponibili per l'operazione 
  *		di riassocia; i tipi ammortamento disponibili rispondono alle seguenti caratteristiche:
  *			- non sono stati cancellati logicamente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param bulkClass la <code>Class</code> modello per il tipo ammortamento.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/

public abstract it.cnr.jada.util.RemoteIterator getAmmortamentoRemoteIteratorPerRiassocia(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Richiede l'ID univoco di Transazione.
  *    PreCondition:
  *      E' stato richiesto di recuperare/generare l'identificativo di transazione.
  *    PostCondition:
  *      Viene richiesto l'ID e, se questo non esiste, verrà generato, se richiesto.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID.
  *
  * @return local_transaction_id <code>String</code> l'ID di transazione richiesto.
**/

public abstract java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/**  
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Tipo_ammortamentoBulk.
  *    PostCondition:
  *      Viene impostato l'esercizio prendendo come riferimento quello di scrivania.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Tipo_ammortamentoBulk per modifica.
  *    PostCondition:
  *		 Vengono impostate le caratteristiche del Tipo Ammortamento specificato, (metodo findTipoAmmortamentoPerModifica) e
  *		vengono caricati i gruppi associati, (metodo findGruppiAssociati).
  *      Viene restituito il Tipo Ammortamento inizializzato per la modifica.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk per ricerca
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Tipo_ammortamentoBulk per ricerca.
  *    PostCondition:
  *      Trasmette il Tipo Ammortamento con tutti gli oggetti collegati e preparato per una operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializza sessione di lavoro.
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione di un Tipo Ammortamento.
  *    PostCondition:
  *      Viene impostato un SavePoint sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *		In caso di chiusura della sessione da parte dell'utente, tutte le operazione fatte 
  *		sul DB saranno annullate a partire da questo punto.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/

public abstract void inizializzaGruppi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializza sessione di lavoro per modifica.
  *    PreCondition:
  *      Viene richiesta una possibile operazione di modifica di un Tipo Ammortamento.
  *    PostCondition:
  *      Viene impostato un SavePoint sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *		In caso di chiusura della sessione da parte dell'utente, tutte le operazione fatte 
  *		sul DB saranno annullate a partire da questo punto.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/

public abstract void inizializzaGruppiPerModifica(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica non valida - Valida Tipo Ammortamento non superato.
  *    PreCondition:
  *      I controlli sulle modifiche apportate al tipo ammortamento non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Tipo Ammortamento
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Tipo Ammortamento.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inserisce i grupppi temporanei.
  *    PreCondition:
  *      E' stata generata la richiesta di riportare i gruppi selezionati dall'utente nella tabella 
  *		temporanea ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *    PostCondition:
  *      Vengono riportati sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG i dati relativi ai 
  *		gruppi selezionati dall'utente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param gruppi <code>OggettoBulk[]</code> i gruppi selezionati dall'utente.
  * @param old_ass la <code>BitSet</code> selezione precedente.
  * @param ass la <code>BitSet</code> selezione attuale.
**/

public abstract void modificaGruppi(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Seleziona gruppi associati
  *    PreCondition:
  *      E' stata generata la richiesta di cercare i gruppi associati al Tipo Ammortamento.
  *    PostCondition:
  *      Viene restituito un Iteratore sui gruppi presenti sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param bulkClass la <code>Class</code> modello per la categoria gruppo.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/

public abstract it.cnr.jada.util.RemoteIterator selectGruppiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException;
}
