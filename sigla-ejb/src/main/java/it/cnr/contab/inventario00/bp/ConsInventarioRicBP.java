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

package it.cnr.contab.inventario00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.inventario00.consultazioni.bulk.*;
import it.cnr.contab.inventario00.ejb.ConsRegistroInventarioComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ConsInventarioRicBP extends BulkBP{	

	
	public ConsRegistroInventarioComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsRegistroInventarioComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_ConsRegistroInventarioComponentSession", ConsRegistroInventarioComponentSession.class);
	}
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
			it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
			int i = 0;
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
			return toolbar;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		VInventarioRicognizioneBulk bulk = new VInventarioRicognizioneBulk();
		try{
			java.sql.Connection conn = EJBCommonServices.getConnection();
			it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)homeCache.getHome(Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(context.getUserContext())));
			if (!uo.isUoCds()){
				bulk.setUoForPrint(uo);
				bulk.setUOForPrintEnabled(false);
			} else {
				bulk.setUoForPrint(uo);
				bulk.setUOForPrintEnabled(true);
			}
		}catch (Exception e) {
			throw handleException( e );
		}
		setModel(context,bulk);
		super.init(config,context);
	}
	public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
		} catch(Exception e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}	
	public void validazione(ActionContext context, VInventarioRicognizioneBulk bulk) throws ValidationException{
		if (bulk.getData()== null)
			throw new ValidationException("Indicare la data a cui limitare la selezione.");
		if(bulk.getUoForPrint()==null|| bulk.getUoForPrint().getCd_unita_organizzativa()== null)
			throw new ValidationException("Indicare l'Unità Organizzativa.");
	}
}
