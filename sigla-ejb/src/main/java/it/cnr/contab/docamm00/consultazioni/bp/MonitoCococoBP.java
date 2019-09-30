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
package it.cnr.contab.docamm00.consultazioni.bp;

import java.io.IOException;
import java.util.BitSet;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

import it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoBulk;
import it.cnr.contab.docamm00.consultazioni.ejb.MonitoCococoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author Matilde D'Urso
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MonitoCococoBP extends it.cnr.jada.util.action.BulkBP {

	/**
	 * 
	 */
	public MonitoCococoBP() {
		super();
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException, BusinessProcessException {
	
		return (it.cnr.jada.ejb.CRUDComponentSession)createComponentSession("CNRDOCAMM00_EJB_MonitoCococoComponentSession", MonitoCococoComponentSession.class);
	}
	
	public void inserisci(ActionContext context, Monito_cococoBulk lancio_monito) throws it.cnr.jada.action.BusinessProcessException	{
		try 
		{   MonitoCococoComponentSession sessione = (MonitoCococoComponentSession) createComponentSession();
			sessione.inserisciRighe(context.getUserContext(), lancio_monito);
		} catch(Exception e) {
				throw handleException(e);
		}
	}

	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		try {
			Monito_cococoBulk monito = new Monito_cococoBulk();
			monito.setAttivita(Monito_cococoBulk.ATT_ISTIT);
			setModel(context,monito);
		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.BulkBP#find(it.cnr.jada.action.ActionContext, it.cnr.jada.persistency.sql.CompoundFindClause, it.cnr.jada.bulk.OggettoBulk, it.cnr.jada.bulk.OggettoBulk, java.lang.String)
	 */
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		return null;
	}
	public void validaDate(ActionContext context, Monito_cococoBulk lancio_monito) throws ValidationException{
		if (lancio_monito.getDt_da_competenza_coge() == null || lancio_monito.getDt_a_competenza_coge() == null)
			throw new ValidationException("Valorizzare le Date di inizio e fine selezione.");
		if (lancio_monito.getDt_da_competenza_coge().after(lancio_monito.getDt_a_competenza_coge()))
			throw new ValidationException("La Data di inizio selezione non puo' essere maggiore della Data di fine selezione.");	
	}
}
