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

package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.compensi00.ejb.ConsRiepilogoCompensiComponentSession;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.ConsultazioniBP;

public class VisConsRiepilogoCompensiBP extends ConsultazioniBP
{
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		try {
		   	super.init(config,context);
			VConsRiepCompensiBulk bulk = new VConsRiepCompensiBulk();
			
			bulk = ((ConsRiepilogoCompensiComponentSession)createComponentSession()).impostaDatiIniziali(context.getUserContext(), bulk);		
			
			setModel(context,bulk);

			setSearchResultColumnSet("CONSULTAZIONE");
			setFreeSearchSet("CONSULTAZIONE");
 		    getBulkInfo().setShortDescription("Consultazione Riepilogo Compensi");
 		    getBulkInfo().setLongDescription("Consultazione Riepilogo Compensi");
			setMultiSelection(false);

		} catch(Throwable e) {
			throw handleException(e);
		}
	}
public ConsRiepilogoCompensiComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsRiepilogoCompensiComponentSession)createComponentSession("CNRCOMPENSI00_EJB_ConsRiepilogoCompensiComponentSession",ConsRiepilogoCompensiComponentSession.class);
	}

}
