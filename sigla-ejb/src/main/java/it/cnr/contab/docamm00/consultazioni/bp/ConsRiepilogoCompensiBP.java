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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsRiepilogoCompensiBP extends ConsultazioniBP
{
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	
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

	public VConsRiepCompensiBulk createNewBulk(ActionContext context) throws BusinessProcessException {
		try {
			VConsRiepCompensiBulk bulk = new VConsRiepCompensiBulk();
			bulk.setUser(context.getUserInfo().getUserid());
			return bulk;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	


	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		if (oggettobulk instanceof VConsRiepCompensiBulk){
			try {
				setFindclause(compoundfindclause);
				CompoundFindClause clause = new CompoundFindClause(getBaseclause(), compoundfindclause);
				return createComponentSession().findRiepilogoCompensi(context.getUserContext(),(VConsRiepCompensiBulk)getModel());
			}catch(Throwable e) {
				throw new BusinessProcessException(e);
			}
		} else {
			return super.search(context, compoundfindclause, oggettobulk);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createComponentSession().findRiepilogoCompensi(context.getUserContext(), (VConsRiepCompensiBulk)getModel()));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
		if (bulk instanceof VConsRiepCompensiBulk){
			try {
				return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().findRiepilogoCompensi(actionContext.getUserContext(),(VConsRiepCompensiBulk)bulk));
			} catch(Exception e) {
				throw new it.cnr.jada.action.BusinessProcessException(e);
			}
		} else {
			try {
				return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
			} catch(Exception e) {
				throw new it.cnr.jada.action.BusinessProcessException(e);
			}
		}
	}


	public ConsRiepilogoCompensiComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsRiepilogoCompensiComponentSession)createComponentSession("CNRCOMPENSI00_EJB_ConsRiepilogoCompensiComponentSession",ConsRiepilogoCompensiComponentSession.class);
	}

	public void controlloSelezioni(ActionContext context, VConsRiepCompensiBulk bulk) throws ValidationException{
		if (!bulk.almenoUnaDataSelezionata()){
			throw new ValidationException("E' necessario selezionare almeno un periodo di filtro sulle date.");
		}
		if (bulk.getFiltroSoggetto() == null || bulk.getFiltroSoggetto().getCd_terzo() == null ){
			throw new ValidationException("E' necessario selezionare almeno un terzo.");
		}
	}

	
	
	
	@Override
	protected Boolean isAutoQuery(ActionContext context) {
		return false;
	}
	
	
}
