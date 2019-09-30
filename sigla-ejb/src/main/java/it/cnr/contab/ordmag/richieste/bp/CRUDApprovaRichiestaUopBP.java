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

package it.cnr.contab.ordmag.richieste.bp;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class CRUDApprovaRichiestaUopBP extends CRUDRichiestaUopBP{
	public CRUDApprovaRichiestaUopBP(String function) throws BusinessProcessException{
		super(function);
	}
	public CRUDApprovaRichiestaUopBP() throws BusinessProcessException{
		super();
	}

	@Override
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[9];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.sblocca");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inviaOrdine");

		return toolbar;
	}
	public boolean isInviaOrdineButtonHidden() {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null || !richiesta.isDefinitiva());
	}

	public boolean isSbloccaButtonHidden() {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null || !richiesta.isDefinitiva());
	}
	@Override
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)oggettobulk;
		richiesta.setIsForApprovazione(true);
		return super.find(actioncontext, compoundfindclause, richiesta);
	}
	@Override
	public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
		completeSearchTools(actioncontext, this);
		validate(actioncontext);
		saveChildren(actioncontext);
		update(actioncontext);
		if(getMessage() == null)
			setMessage("Salvataggio eseguito in modo corretto.");
		commitUserTransaction();
		try
		{
			basicEdit(actioncontext, getModel(), true);
		}
		catch(BusinessProcessException businessprocessexception)
		{
			setModel(actioncontext, null);
			setDirty(false);
			throw businessprocessexception;
		}
	}
	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		// TODO Auto-generated method stub
		RichiestaUopBulk richiesta = (RichiestaUopBulk)oggettobulk;
		richiesta.setIsForApprovazione(true);
		return super.initializeModelForSearch(actioncontext, richiesta);
	}
}
