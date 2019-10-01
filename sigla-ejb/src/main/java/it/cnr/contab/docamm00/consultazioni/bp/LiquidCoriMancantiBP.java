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
package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.V_liquid_cori_mancantiBulk;
import it.cnr.contab.pdg00.ejb.RicostruzioneResiduiComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.RicercaLiberaBP;

/**
 * @author mincarnato
 *
 * To change the tempchi?late for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LiquidCoriMancantiBP extends it.cnr.jada.util.action.BulkBP {

	public LiquidCoriMancantiBP() {
			super("Tr");
		}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			V_liquid_cori_mancantiBulk liquid = new V_liquid_cori_mancantiBulk();
			setModel(context,liquid);
		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
	}

	public void validaDate(ActionContext context, V_liquid_cori_mancantiBulk liquid) throws ValidationException{
		if (liquid.getDt_da() == null || liquid.getDt_a() == null)
			throw new ValidationException("Valorizzare le Date di inizio e fine selezione.");
		if (liquid.getDt_da().after(liquid.getDt_a()))
			throw new ValidationException("La Data di inizio selezione non puo' essere maggiore della Data di fine selezione.");	
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.BulkBP#find(it.cnr.jada.action.ActionContext, it.cnr.jada.persistency.sql.CompoundFindClause, it.cnr.jada.bulk.OggettoBulk, it.cnr.jada.bulk.OggettoBulk, java.lang.String)
	 */
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		return null;
	}	
	
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
}
