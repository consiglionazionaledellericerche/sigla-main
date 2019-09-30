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

package it.cnr.contab.pdg00.bp;

import java.rmi.RemoteException;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofi_obbBulk;
import it.cnr.contab.pdg00.ejb.CRUDCostoDelDipendenteComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
/**
 * Insert the type's description here.
 * Creation date: (29/09/2006 12.00)
 * @author: Matilde D'urso
 */
public class StipendiCofiObbBP extends SimpleCRUDBP {
	private SimpleDetailCRUDController stipendi_obb = new SimpleDetailCRUDController("stipendi_obbl",Stipendi_cofi_obbBulk.class,"stipendi_obbligazioni",this){
		public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
			super.setFilter(actioncontext, compoundfindclause);
			try {
				Stipendi_cofiVirtualBulk stipendi_cofiVirtual =  ((CRUDCostoDelDipendenteComponentSession)createComponentSession()).caricaDettagliFiltrati(actioncontext.getUserContext(),(Stipendi_cofiVirtualBulk)getParentModel(),compoundfindclause);
				((StipendiCofiObbBP)actioncontext.getBusinessProcess()).setModel(actioncontext, stipendi_cofiVirtual);
			} catch (DetailedRuntimeException e) {
				handleException(e);
			} catch (ComponentException e) {
				handleException(e);
			} catch (RemoteException e) {
				handleException(e);
			} catch (BusinessProcessException e) {
				handleException(e);
			}
		}	
	};
	/**
 * StipendiCofiObbBP constructor comment.
 */
public StipendiCofiObbBP() {
	super();
}

@Override
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	super.initialize(actioncontext);
	setModel(actioncontext,super.initializeModelForEdit(actioncontext,this.getModel()));
	setStatus(EDIT);
}

public void resetDati(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	initialize(context);
}
/**
 * StipendiCofiObbBP constructor comment.
 * @param function java.lang.String
 */
public StipendiCofiObbBP(String function) {
	super(function);
}
public final it.cnr.jada.util.action.SimpleDetailCRUDController getStipendi_obb() {
	return stipendi_obb;
}
public void setStipendi_obb(SimpleDetailCRUDController controller) {
	stipendi_obb = controller;
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
		
	Button[] toolbar = super.createToolbar();
	Button[] newToolbar = new Button[ toolbar.length + 1 ];
	for ( int i = 0; i< toolbar.length; i++ )
		newToolbar[ i ] = toolbar[ i ];
	newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");

	return newToolbar;
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
public boolean isSearchButtonHidden()
{
    return true;
}
}
