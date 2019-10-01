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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (12/14/2001 3:01:30 PM)
 * @author: Alfonso Ardire
 */
public interface IDocumentoAmministrativoMgr extends it.cnr.jada.comp.ICRUDMgr {
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato.
  *  Inserimento del mandato.
  *    PreCondition:
  *      Si sta inserendo o modificando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato P o L.
  *  Annullamento del mandato.
  *    PreCondition:
  *      Si sta annullando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato A.
  *  Cancellazione del mandato.
  *    PreCondition:
  *      Si sta cancellando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato Q.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public void aggiornaStatoDocumentiAmministrativi(
	UserContext userContext,
	String cd_cds,
	String cd_unita_organizzativa,
	String tipo_documento,
	Integer esercizio,
	Long progressivo,
	String action) throws ComponentException;
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */
//^^@@

public IDocumentoAmministrativoBulk calcoloConsuntivi(
			it.cnr.jada.UserContext userContext,
			IDocumentoAmministrativoBulk documentoAmministrativo)
			throws ComponentException;
public it.cnr.jada.bulk.OggettoBulk creaConBulk(
	UserContext userContext,
	it.cnr.jada.bulk.OggettoBulk bulk,
	it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
	throws ComponentException;
public it.cnr.jada.bulk.OggettoBulk modificaConBulk(
	UserContext userContext,
	it.cnr.jada.bulk.OggettoBulk bulk,
	it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
	throws ComponentException;
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */
//^^@@

public abstract void protocolla(it.cnr.jada.UserContext param0,java.sql.Timestamp param1, Long param2) throws it.cnr.jada.comp.ComponentException;
public IDocumentoAmministrativoBulk riportaAvanti(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm,
	OptionRequestParameter status) 
	throws ComponentException;
public IDocumentoAmministrativoBulk riportaIndietro(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm) 
	throws ComponentException;
/**
 * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo che ha aperto il compenso
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(it.cnr.jada.UserContext userContext, String savePointName) throws ComponentException;
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void setSavePoint(it.cnr.jada.UserContext userContext, String savePointName) throws ComponentException;
//^^@@
/** 
  *  Aggiornamento di un dettaglio di documento amministrativo
  *    PreCondition:
  *      Richiesto l'aggiornamento di un dettaglio di documento amministrativo
  *    PostCondition:
  *      Il dettaglio viene aggiornato
 */
//^^@@

public IDocumentoAmministrativoRigaBulk update(
	it.cnr.jada.UserContext param0, 
	IDocumentoAmministrativoRigaBulk param1)
	throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PreCondition:
  *      Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PostCondition:
  *      Il dettaglio viene aggiornato
 */
//^^@@

public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
	it.cnr.jada.UserContext userContext, 
	IScadenzaDocumentoContabileBulk scadenza)
	throws it.cnr.jada.comp.ComponentException;
public boolean verificaStatoEsercizio(
	UserContext userContext,
	EsercizioBulk anEsercizio) 
	throws ComponentException;
}
