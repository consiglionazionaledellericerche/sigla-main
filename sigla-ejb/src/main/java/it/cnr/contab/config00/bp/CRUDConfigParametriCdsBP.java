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
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.ejb.Parametri_cdsComponentSession;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

/**
 * @author rpagano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CRUDConfigParametriCdsBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDConfigParametriCdsBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDConfigParametriCdsBP(String function) {
		super(function);
	}
		
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		Parametri_cdsComponentSession sessione = (Parametri_cdsComponentSession) createComponentSession();
		try {
			Parametri_cdsBulk parCdsBulk = sessione.getParametriCds(actioncontext.getUserContext(),	CNRUserContext.getCd_cds(actioncontext.getUserContext()), CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			if (parCdsBulk != null)
				this.setModel(actioncontext, parCdsBulk);
				this.setStatus(it.cnr.jada.util.action.FormController.EDIT);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean isSearchButtonHidden() {
		return true;
	}

	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	public boolean isNewButtonHidden() {
		return true;
	}
	
	public void controlloCdrEntrata(ActionContext context, Parametri_cdsBulk bulk) throws ValidationException{
		if ( bulk.getFl_linea_pgiro_e_cds()==true & bulk.getCd_cdr_linea_pgiro_e()==null) 
			throw new ValidationException("E' necessario inserire il CdR della GAE dedicata del CDS per Partite di Giro di Entrata");
	}
	
	public void controlloCdrSpesa(ActionContext context, Parametri_cdsBulk bulk) throws ValidationException{
		if ( bulk.getFl_linea_pgiro_s_cds()==true & bulk.getCd_cdr_linea_pgiro_s()==null) 
			throw new ValidationException("E' necessario inserire il CdR della GAE dedicata del CDS per Partite di Giro di Spesa");
	}
	
	public void controlloLineaEntrata(ActionContext context, Parametri_cdsBulk bulk) throws ValidationException{
		if ( bulk.getFl_linea_pgiro_e_cds()==true & bulk.getCd_linea_pgiro_e()==null) 
			throw new ValidationException("E' necessario inserire il Codice GAE dedicata del CDS per Partite di Giro di Entrata");
	}
	
	public void controlloLineaSpesa(ActionContext context, Parametri_cdsBulk bulk) throws ValidationException{
		if ( bulk.getFl_linea_pgiro_s_cds()==true & bulk.getCd_linea_pgiro_s()==null) 
			throw new ValidationException("E' necessario inserire il Codice GAE dedicata del CDS per Partite di Giro di Spesa");
	}
}