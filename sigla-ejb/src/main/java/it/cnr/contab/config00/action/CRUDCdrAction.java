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

package it.cnr.contab.config00.action;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Unita' Organizzativa
 */
public class CRUDCdrAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDCdrAction() {
	super();
}
/**
 * Gestisce una richiesta di azzeramento del searchtool "find_responsabile"
 *
 * @param context	L'ActionContext della richiesta
 * @param cdr Centro di responsabilità
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBlankSearchFind_responsabile(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	cdr.setResponsabile(new V_terzo_persona_fisicaBulk());
	return context.findDefaultForward();
}
/**
 * Gestisce una richiesta di assegnamento al crudtool "crea_responsabile"
 * Verifica che si tratti di persona fisica
 *
 * @param context	L'ActionContext della richiesta
 * @param cdr	Centro di responsabilità
 * @param terzo	Terzo
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBringBackCRUDCrea_responsabile(it.cnr.jada.action.ActionContext context,CdrBulk cdr,TerzoBulk terzo) {
	if (!terzo.getAnagrafico().isPersonaFisica())
		throw new MessageToUser("Il responsabile deve essere una persona fisica");
	cdr.setResponsabile(terzo);
	return context.findDefaultForward();
}
	public it.cnr.jada.action.Forward doBringBackSearchFind_responsabile(it.cnr.jada.action.ActionContext context,CdrBulk cdr,TerzoBulk terzo) {
		if (!terzo.getAnagrafico().isPersonaFisica())
			throw new MessageToUser("Il responsabile deve essere una persona fisica");
		cdr.setResponsabile(terzo);
		return context.findDefaultForward();
	}
}
