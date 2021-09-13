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

package it.cnr.contab.inventario00.actions;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.inventario00.bp.CRUDSelezionatoreBeniInTransitoBP;
import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.inventario01.bp.CRUDCaricoInventarioBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.ejb.NumerazioneTempBuonoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.List;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class ListaBeniInTransitoDaInventariareAction extends SelezionatoreListaAction {
/**
 * DocumentiAmministrativiProtocollabiliAction constructor comment.
 */
public ListaBeniInTransitoDaInventariareAction() {
	super();
}

@Override
public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
	return actioncontext.findDefaultForward();
}
	public Forward doInventaria(ActionContext actioncontext) throws BusinessProcessException,
			ComponentException,
			java.rmi.RemoteException, FillException {
		CRUDSelezionatoreBeniInTransitoBP bp = (CRUDSelezionatoreBeniInTransitoBP) actioncontext.getBusinessProcess();
		fillModel(actioncontext);
		bp.setSelection(actioncontext);
		List<Transito_beni_ordiniBulk> dettagliDaInventariare = bp.getBeniSelezionati(actioncontext);
		if (dettagliDaInventariare.size() > 0){
			CRUDCaricoInventarioBP ibp = (CRUDCaricoInventarioBP) actioncontext.createBusinessProcess("CRUDCaricoInventarioBP", new Object[]{"MRSTh"});
			Buono_carico_scaricoBulk bcs = new Buono_carico_scaricoBulk();
			bcs.setTi_documento(Buono_carico_scaricoBulk.CARICO);
			bcs.initializeForInsert(ibp, actioncontext);
			bcs.setByOrdini(true);
			bcs = (it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk) ibp.createComponentSession().inizializzaBulkPerInserimento(actioncontext.getUserContext(), bcs);
			bcs.caricaDettagliFromTransito(dettagliDaInventariare);
			bcs.setPg_buono_c_s(((NumerazioneTempBuonoComponentSession) EJBCommonServices.createEJB(
					"CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession",
					NumerazioneTempBuonoComponentSession.class)).getNextTempPG(actioncontext.getUserContext(), bcs));

			ibp.setBy_ordini(true);
			ibp.setModel(actioncontext, bcs);
			ibp.setStatus(ibp.INSERT);
			ibp.setDirty(false);
			ibp.resetChildren(actioncontext);

			actioncontext.addHookForward("close",this,"refresh");
			actioncontext.addHookForward("bringback", this, "refresh");
			return actioncontext.addBusinessProcess(ibp);
		} else {
			bp.setMessage(2, "E' necessario selezionare almeno un bene da inventariare");
			return actioncontext.findDefaultForward();
		}
	}
	protected java.util.List getDettagliDaInventariare(
			ActionContext context,
			java.util.Iterator dettagli) {

		java.util.Vector coll = new java.util.Vector();
		if (dettagli != null) {
			while (dettagli.hasNext()) {
				Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) dettagli.next();
				if (riga.getBene_servizio() != null && riga.getBene_servizio().getCd_bene_servizio() != null &&
						riga.getBene_servizio().getFl_gestione_inventario() != null &&
						riga.getBene_servizio().getFl_gestione_inventario().booleanValue() &&
						!riga.isInventariato())
					coll.add(riga);
			}
		}
		return coll;
	}

	public Forward refresh(ActionContext context) {
		CRUDSelezionatoreBeniInTransitoBP bp = (CRUDSelezionatoreBeniInTransitoBP) context.getBusinessProcess();
		try {
			bp.refresh(context);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}

		return context.findDefaultForward();
	}
}
