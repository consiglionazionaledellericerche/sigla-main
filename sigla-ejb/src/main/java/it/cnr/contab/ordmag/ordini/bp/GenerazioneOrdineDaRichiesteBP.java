/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.bp;

import java.util.List;

import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.ejb.EvasioneOrdineComponentSession;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.contab.ordmag.richieste.ejb.GenerazioneOrdiniDaRichiesteComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Business process che gestisce alcune informazioni relative alle obbligazioni.
 * E' utilizzato in combinazione con la <code>CRUDListaObbligazioniAction</code>.
 */

public class GenerazioneOrdineDaRichiesteBP extends SimpleCRUDBP {

	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean carryingThrough = false;
	private boolean isDeleting = false;
	private final SimpleDetailCRUDController richieste = new SimpleDetailCRUDController("RichiesteDaTrasformareInOrdine",VRichiestaPerOrdiniBulk.class,"richiesteDaTrasformareInOrdineColl",this){

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int index = super.addDetail(oggettobulk);
			return index;
		}
		
	};

	public boolean isInputReadonly() 
	{
			return false;
	}

	public GenerazioneOrdineDaRichiesteBP() {
		this(OrdineAcqBulk.class);
	}
	
	public GenerazioneOrdineDaRichiesteBP(Class dettObbligazioniControllerClass) {
		super("Tr");

	}

	/**
	 * CRUDAnagraficaBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public GenerazioneOrdineDaRichiesteBP(String function)
			throws BusinessProcessException {
		super(function + "Tr");

	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");

		return toolbar;
	}
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}
	public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm) {
		this.userConfirm = userConfirm;
	}
	public boolean isCarryingThrough() {
		return carryingThrough;
	}
	public void setCarryingThrough(boolean carryingThrough) {
		this.carryingThrough = carryingThrough;
	}

	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}

	@Override
	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	@Override
	public boolean isSearchButtonHidden() {
		return true;
	}

	@Override
	public boolean isStartSearchButtonHidden() {
		return true;
	}

	public OrdineAcqBulk creaOrdineDaRichieste(ActionContext context) throws BusinessProcessException{
		try {
			OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
			
			GenerazioneOrdiniDaRichiesteComponentSession comp = (GenerazioneOrdiniDaRichiesteComponentSession)createComponentSession();
			
			comp.generaOrdine(context.getUserContext(), ordine);
			setModel(context, ordine);
			
			return ordine;
		}catch(Throwable ex){
			throw handleException(ex);
		}

	}

	@Override
	public boolean isNewButtonHidden() {
		return true;
	}

	public SimpleDetailCRUDController getRichieste() {
		return richieste;
	}

	public void cercaRichieste(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		OrdineAcqBulk bulk = (OrdineAcqBulk) getModel();	
		try 
		{
			bulk.setRichiesteDaTrasformareInOrdineColl(new BulkList<>());
			GenerazioneOrdiniDaRichiesteComponentSession comp = (GenerazioneOrdiniDaRichiesteComponentSession)createComponentSession();
			bulk = comp.cercaRichieste(context.getUserContext(), bulk);

			setModel( context, bulk );
			resyncChildren( context );
		} catch(Exception e) 
		{
			throw handleException(e);
		}
	}

}
