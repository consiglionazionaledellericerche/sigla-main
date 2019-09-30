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
 * Created on Jul 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.bp;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.ejb.RicostruzioneResiduiComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsRicostruzioneResiduiBP extends ConsultazioniBP {

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			CdrBulk cdr = ((RicostruzioneResiduiComponentSession)createComponentSession("CNRPDG00_EJB_RicostruzioneResiduiComponentSession", RicostruzioneResiduiComponentSession.class)).findCdr(context.getUserContext());
			boolean isUOScrivaniaEnte = ((RicostruzioneResiduiComponentSession)createComponentSession("CNRPDG00_EJB_RicostruzioneResiduiComponentSession", RicostruzioneResiduiComponentSession.class)).isUOScrivaniaEnte(context.getUserContext());

			if (!isUOScrivaniaEnte) {		
				CompoundFindClause clauses = new CompoundFindClause();
				clauses.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, cdr.getCd_centro_responsabilita());
				setBaseclause(clauses);
			}
			super.init(config,context);
								
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
}
