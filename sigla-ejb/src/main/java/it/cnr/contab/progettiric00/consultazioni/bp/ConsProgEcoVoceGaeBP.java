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

package it.cnr.contab.progettiric00.consultazioni.bp;

import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSession;
import it.cnr.contab.progettiric00.consultazioni.bulk.ConsProgettiEcoVociGaeBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;


public class ConsProgEcoVoceGaeBP extends BulkBP
{
	public Button[] createToolbar() {
		Button[] toolbar = new Button[1];
		int i = 0;
		toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
			ConsProgettiEcoVociGaeBulk bulk = new ConsProgettiEcoVociGaeBulk();
			
			CompoundFindClause clauses = new CompoundFindClause();
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
            clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
			bulk.setTipoStampa(ConsProgettiEcoVociGaeBulk.DETTAGLIATA);

			setModel(context,bulk);
			bulk.setEsercizio_piano(esercizio);
		super.init(config,context);
	}

	public boolean isRicercaButtonEnabled()
	{
		return true;
	}

	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause clauses, OggettoBulk bulk, OggettoBulk context, String property) throws BusinessProcessException {
		try {
			CRUDComponentSession cs = (CRUDComponentSession) createComponentSession("CNRPDG00_EJB_PdGPreventivoComponentSession");
			if (cs == null) return null;
			return EJBCommonServices.openRemoteIterator(
					actioncontext,
					cs.cerca(
							actioncontext.getUserContext(),
							clauses,
							bulk,
							getModel(),
							property));
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public void valorizzaProgetto(ActionContext context, ConsProgettiEcoVociGaeBulk bulk) throws ValidationException{
		if (bulk.getPg_progetto()== null)
			throw new ValidationException("Valorizzare il progetto");
	}

}
