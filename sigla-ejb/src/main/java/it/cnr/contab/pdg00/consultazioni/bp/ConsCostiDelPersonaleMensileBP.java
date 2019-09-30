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

package it.cnr.contab.pdg00.consultazioni.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ConsCostiDelPersonaleMensileBP extends BulkBP
{
		
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
			Param_cons_costi_personaleBulk parametri = new Param_cons_costi_personaleBulk();
//			CompoundFindClause clauses = new CompoundFindClause();
			
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			String cds = CNRUserContext.getCd_cds(context.getUserContext());
			String uo = CNRUserContext.getCd_unita_organizzativa(context.getUserContext());
			
			parametri.setV_uo(new Unita_organizzativaBulk(uo));
				try {
					completeSearchTool(context,parametri,parametri.getBulkInfo().getFieldProperty("find_uo"));
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (parametri.getV_uo().isUoCds())
					parametri.setROFindV_uo(false);
					else 
					parametri.setROFindV_uo(true);
			setModel(context,parametri);
			parametri.setEsercizio(esercizio);
			parametri.setCds(cds);
			
			setModel(context,parametri);
		super.init(config,context);
	}

	public CRUDComponentSession createComponentSession() throws BusinessProcessException{
		return (CRUDComponentSession)createComponentSession("JADAEJB_CRUDComponentSession");
	}
	
	
	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		try{
			return EJBCommonServices.openRemoteIterator(actioncontext, createComponentSession().cerca(actioncontext.getUserContext(), compoundfindclause, oggettobulk, oggettobulk1, s));
		}catch(Exception exception){
			throw new BusinessProcessException(exception);
		}
	}
	
		public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	
		
}
