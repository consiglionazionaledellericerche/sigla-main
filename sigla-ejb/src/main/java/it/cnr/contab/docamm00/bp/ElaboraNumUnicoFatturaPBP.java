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
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.ElaboraNumUnicoFatturaPBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author Rosangela Pucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ElaboraNumUnicoFatturaPBP extends it.cnr.jada.util.action.BulkBP {

	/**
	 * 
	 */
	public ElaboraNumUnicoFatturaPBP() {
		super();
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.print");
		return toolbar;
	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException, BusinessProcessException {
	
		return (it.cnr.jada.ejb.CRUDComponentSession)createComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession", FatturaPassivaComponentSession.class);
	}
	
	public void inserisci(ActionContext context, ElaboraNumUnicoFatturaPBulk lancio) throws it.cnr.jada.action.BusinessProcessException	{
		try 
		{   
			if(lancio.getDataRegistrazioneA()==null)
				throw new ApplicationException("Inserire la data limite");
			FatturaPassivaComponentSession sessione = (FatturaPassivaComponentSession) createComponentSession();
			sessione.inserisciProgUnivoco(context.getUserContext(), lancio);
		} catch(Exception e) {
				throw handleException(e);
		}
	}

	public boolean isRicercaButtonEnabled()
	{
		// 17/03/20105 disabilitato perchè inserito alla creazione della fattura passiva
		return false;
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.BulkBP#find(it.cnr.jada.action.ActionContext, it.cnr.jada.persistency.sql.CompoundFindClause, it.cnr.jada.bulk.OggettoBulk, it.cnr.jada.bulk.OggettoBulk, java.lang.String)
	 */
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		return null;
	}
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		try {
			ElaboraNumUnicoFatturaPBulk bulk  = new ElaboraNumUnicoFatturaPBulk();
			//bulk.setDataRegistrazioneA( it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			setModel(context,bulk);
		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
	}
}
