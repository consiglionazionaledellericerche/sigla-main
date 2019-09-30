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

import it.cnr.contab.anagraf00.bp.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Unita' Organizzativa
 */
public class CRUDUnita_organizzativaAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * Costruttore della classe CRUDLinea_attivitaAction
 */
public CRUDUnita_organizzativaAction() {
	super();
}
/**
	 * Metodo utilizzato per gestire l'annullamento del Cds area di ricerca all'annullamento del Cds padre
	 * dell'unita' organizzativa
	 * @param context <code>ActionContext</code> in uso.
	 * @param uo <code>UnitaOrganizzativaBulk</code>
	 *
	 * @return <code>Forward</code>
	 *
	 */

public it.cnr.jada.action.Forward doBlankSearchFind_responsabile(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo) {
	uo.setResponsabile(new V_terzo_persona_fisicaBulk());
	return context.findDefaultForward();
}
/**
	 * Metodo utilizzato per gestire l'annullamento del Cds area di ricerca all'annullamento del Cds padre
	 * dell'unita' organizzativa
	 * @param context <code>ActionContext</code> in uso.
	 * @param uo <code>UnitaOrganizzativaBulk</code>
	 *
	 * @return <code>Forward</code>
	 *
	 */

public it.cnr.jada.action.Forward doBlankSearchFind_responsabile_amm(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo) {
	uo.setResponsabile_amm(new V_terzo_persona_fisicaBulk());
	return context.findDefaultForward();
}
/**
	 * Metodo utilizzato per gestire l'annullamento del Cds area di ricerca all'annullamento del Cds padre
	 * dell'unita' organizzativa
	 * @param context <code>ActionContext</code> in uso.
	 * @param uo <code>UnitaOrganizzativaBulk</code>
	 *
	 * @return <code>Forward</code>
	 *
	 */

public it.cnr.jada.action.Forward doBlankSearchFind_unita_padre(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo) {
    uo.setCds_area_ricerca(new it.cnr.contab.config00.sto.bulk.CdsBulk());
    uo.setUnita_padre(new it.cnr.contab.config00.sto.bulk.CdsBulk());
    uo.setUoDiRiferimento(new Unita_organizzativaBulk());
	return context.findDefaultForward();
}
/**
	 * Metodo utilizzato per gestire l'annullamento del Cds area di ricerca all'annullamento del Cds padre
	 * dell'unita' organizzativa
	 * @param context <code>ActionContext</code> in uso.
	 * @param uo <code>UnitaOrganizzativaBulk</code>
	 *
	 * @return <code>Forward</code>
	 *
	 */

public it.cnr.jada.action.Forward doBringBackCRUDCrea_responsabile(it.cnr.jada.action.ActionContext context,Unita_organizzativaBulk uo,TerzoBulk terzo) {
	if (!terzo.getAnagrafico().isPersonaFisica())
		throw new MessageToUser("Il responsabile deve essere una persona fisica");
	uo.setResponsabile(terzo);
	return context.findDefaultForward();
}
/**
	 * Metodo utilizzato per gestire l'annullamento del Cds area di ricerca all'annullamento del Cds padre
	 * dell'unita' organizzativa
	 * @param context <code>ActionContext</code> in uso.
	 * @param uo <code>UnitaOrganizzativaBulk</code>
	 *
	 * @return <code>Forward</code>
	 *
	 */

public it.cnr.jada.action.Forward doBringBackCRUDCrea_responsabile_amm(it.cnr.jada.action.ActionContext context,Unita_organizzativaBulk uo,TerzoBulk terzo) {
	if (!terzo.getAnagrafico().isPersonaFisica())
		throw new MessageToUser("Il responsabile amministrativo deve essere una persona fisica");
	uo.setResponsabile_amm(terzo);
	return context.findDefaultForward();
}
/**
 * Gestisce gli effetti dovuti alla impostazione del flag rubrica
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCheckFl_rubrica(it.cnr.jada.action.ActionContext context) 
{
	try 
	{
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		bp.fillModel(context);
		if (((Unita_organizzativaBulk) bp.getModel()).getFl_rubrica().booleanValue() )
			((Unita_organizzativaBulk) bp.getModel()).setUoDiRiferimento( new Unita_organizzativaBulk());
		return context.findDefaultForward();
	} catch(Exception ex) 
	{
				return handleException(context,ex);
	}

}
/**
	 * Metodo utilizzato per richiamare un'entità di tipo Terzo associata 
	 * all'unita' organizzativa in questione.
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
	 *
	 */
public it.cnr.jada.action.Forward doRichiamaTerzo(it.cnr.jada.action.ActionContext context) 
{
	try 
	{
		CRUDBP bp = (CRUDBP)getBusinessProcess(context);
		fillModel( context );
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk) bp.getModel();
		CRUDTerzoBP terzoBP = (CRUDTerzoBP) context.createBusinessProcess( "CRUDTerzoBP", new Object[]{ bp.isEditable() ? "M" : "V", uo } );
		return context.addBusinessProcess(terzoBP);		
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}

}
}
