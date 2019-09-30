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

import java.math.BigDecimal;

import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.V_liquid_cori_mancantiBulk;
import it.cnr.contab.docamm00.consultazioni.ejb.MonitoCococoComponentSession;
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
public class AccontoAddComBP extends it.cnr.jada.util.action.BulkBP {

	public AccontoAddComBP() {
			super();
		}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.calcola");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Acconto_classific_coriBulk liquid = new Acconto_classific_coriBulk();
			setModel(context,liquid);
		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
	}

	public void validaPercentuale(ActionContext context, Acconto_classific_coriBulk acconto) throws ValidationException{
		if (acconto.getPercentuale() != null && acconto.getPercentuale().compareTo(new BigDecimal(100))>0)
			throw new ValidationException("Percentuale non valida.");
	}
	
	public void calcola(ActionContext context, Acconto_classific_coriBulk acconto) throws it.cnr.jada.action.BusinessProcessException	{
		try 
		{   CompensoComponentSession sessione = (CompensoComponentSession) createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession", CompensoComponentSession.class);
			sessione.doCalcolaAccontoAddCom(context.getUserContext(), acconto);
		} catch(Exception e) {
				throw handleException(e);
		}
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
