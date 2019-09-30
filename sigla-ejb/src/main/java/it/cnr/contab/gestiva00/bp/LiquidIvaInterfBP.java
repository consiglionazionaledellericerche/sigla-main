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

package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;

public class LiquidIvaInterfBP extends it.cnr.jada.util.action.SimpleCRUDBP {
public LiquidIvaInterfBP() {
	super();
}

public LiquidIvaInterfBP(String function) {
	super(function);
}

/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		resetForSearch(context);
	}

	public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			setModel(context,createEmptyModelForSearch(context));
			Liquid_iva_interfBulk liquid_iva = (Liquid_iva_interfBulk)getModel();
			liquid_iva.setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
			liquid_iva.setCd_cds(CNRUserContext.getCd_cds(context.getUserContext()));
			setStatus(SEARCH);
			setDirty(false);
			//super.resetForSearch(context);
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}
			
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException{
		super.basicEdit(actioncontext,oggettobulk,flag);
		Liquid_iva_interfBulk liquid_iva = (Liquid_iva_interfBulk)getModel();
		//Per recuperare il mese dalla data
		java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
		cal.setTime(new java.util.Date(liquid_iva.getDt_inizio().getTime()));
		liquid_iva.setMese((String)Liquid_iva_interfBulk.INT_MESI.get(new Integer(cal.get(java.util.Calendar.MONTH)+1)));
		//il metodo basicEdit scatta solo se la query restituisce i dati
		liquid_iva.setVisualizzaDati(true); 
		if (liquid_iva.getFl_gia_eleborata().booleanValue() == true)
			setStatus(VIEW);
	}
	
	public boolean controllaQuery(ActionContext context, Liquid_iva_interfBulk liquid_iva) throws it.cnr.jada.action.BusinessProcessException {
		try 
		{
			LiquidIvaInterfComponentSession sessione = (LiquidIvaInterfComponentSession) createComponentSession();
			//se contaRiga e' false chiamo un metodo del component che mi inserisce i dati
			return sessione.contaRiga(context.getUserContext(), liquid_iva);
			/**
			if (! sessione.contaRiga(context.getUserContext(), liquid_iva)){
				sessione.inserisciRighe(context.getUserContext(), liquid_iva);
				//setMessage("Per il mese in esame non esistono dati. Il sistema li inserirà in automatico.");				
			}**/
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public void inserisci(ActionContext context, Liquid_iva_interfBulk liquid_iva) throws it.cnr.jada.action.BusinessProcessException	{
		try 
		{
			LiquidIvaInterfComponentSession sessione = (LiquidIvaInterfComponentSession) createComponentSession();
				sessione.inserisciRighe(context.getUserContext(), liquid_iva);
		} catch(Exception e) {
				throw handleException(e);
		}
	}
	public boolean isDeleteButtonHidden()
	{
		return true;
	}
	public boolean isNewButtonHidden()
	{
		return true;
	}
	public boolean isFreeSearchButtonHidden()
	{
		return true;
	}	
}
