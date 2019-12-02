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

/*
 * Created on Sep 16, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.action;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.prevent00.bp.CRUDPdgPianoRipSpeseAccentBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgPianoRipSpeseAccentAction extends it.cnr.jada.util.action.CRUDAction {
	/**
	 * Costruttore standard di CRUDPdgPianoRipSpeseAccentAction.
	 */
	public CRUDPdgPianoRipSpeseAccentAction() {
		super();
	}

	/**
	 * Gestione della richiesta di rendere definitivo il piano 
	 * di riparto delle spese accentrate
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRendiDefinitivo(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)getBusinessProcess(context);
			bp.rendiPdgPianoRipartoDefinitivo(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	/**
	 * Gestione della richiesta di rendere provvisorio il piano 
	 * di riparto delle spese accentrate
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRendiProvvisorio(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)getBusinessProcess(context);
			bp.rendiPdgPianoRipartoProvvisorio(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Gestione della richiesta di rendere provvisorio il piano 
	 * di riparto delle spese accentrate
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doConsultaPianoRiparto(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)getBusinessProcess(context);

			V_classificazione_vociBulk cla = (V_classificazione_vociBulk)bp.getModel();

			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(context.getUserContext()));

			if (cla!=null && cla.getId_classificazione()!=null) {
				clause.addClause("AND","id_classificazione",SQLBuilder.EQUALS,cla.getId_classificazione());
			}

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsPdgPianoRipartoBP");
			
			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Gestione della richiesta di precaricare per la classificazione visualizzata tutti i CDR   
	 * di primo livello con importo nullo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doCaricaStruttura(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)getBusinessProcess(context);
			bp.caricaStruttura(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Gestione della richiesta di aggiornare al valore "Zero" tutti gli importi null associati al CDR 
	 * Tale operazione serve in quanto:
	 *
	 * 1) se l'importo è nullo i CDR possono caricare importi senza controllo
	 * 2) se l'importo è 0 i CDR non possono caricare importi nel Preventivo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doInitImTotSpeseAcc(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)getBusinessProcess(context);
			bp.inizializzaImTotSpeseAcc(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}

