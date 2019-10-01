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

package it.cnr.contab.docamm00.bp;

import java.util.Vector;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:16 PM)
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiRistampabiliBP extends ListaDocumentiAmministrativiBP {
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 */
public DocumentiAmministrativiRistampabiliBP() {
	this("");
}
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 * @param function java.lang.String
 */
public DocumentiAmministrativiRistampabiliBP(String function) {
	super(function+"Tr");
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Filtro_ricerca_doc_ammVBulk aggiornaSezionali(
	ActionContext context,
	Filtro_ricerca_doc_amm_ristampabileVBulk filtro) throws BusinessProcessException {

	try {
		Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk)filtro.getInstance();
		FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession)getBusinessProcessForDocAmm(
																					context,
 																					fatturaAttiva).createComponentSession();
		//Vector options = new Vector();
        //options.add(new String[][] { { "TIPO_SEZIONALE.FL_AUTOFATTURA", "N", "AND" } });
		java.util.List sezionali = h.estraeSezionaliPerRistampa(
														context.getUserContext(), 
														fatturaAttiva,
														null);
		filtro.setSezionali(sezionali);
		return filtro;
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Filtro_ricerca_doc_ammVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		Filtro_ricerca_doc_amm_ristampabileVBulk filtro = (Filtro_ricerca_doc_amm_ristampabileVBulk)super.createEmptyModelForSearch(context);
		return aggiornaSezionali(context, filtro);
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_doc_ammVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_doc_amm_ristampabileVBulk bulk = new Filtro_ricerca_doc_amm_ristampabileVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Ottiene il business process responsabile del documento amministativo docAmm.
 */
public IDocumentoAmministrativoBP getBusinessProcessForDocAmm(
		ActionContext context,
		IDocumentoAmministrativoBulk docAmm) 
		throws BusinessProcessException {

	IDocumentoAmministrativoBP docAmmBP = super.getBusinessProcessForDocAmm(context, docAmm);
	if (docAmm == null) return null;

	addChild((BusinessProcess)docAmmBP);
	
	return docAmmBP;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	resetBpCache();
}
}
