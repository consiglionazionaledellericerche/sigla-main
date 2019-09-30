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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Creation date: (24/09/2004)
 * @author: Aurelio D'Amico
 * @version: 1.0
 */
public class Estrazione770BP extends AbstractEstrazioneFiscaleBP {
/**
 * Estrazione770BP default contructor.
 */
public Estrazione770BP() {
	super();
}
/**
 * Estrazione770BP constructor comment.
 * @param function java.lang.String
 */
public Estrazione770BP(String function) {
	super(function);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.extract770");

	return toolbar;
}
public void doElabora770(ActionContext context) throws BusinessProcessException {

	try{

		Estrazione770Bulk e770 = (Estrazione770Bulk)getModel();
		
		if(e770.getQuadri_770() == null || e770.getQuadri_770().getCd_quadro()==null)
			throw new it.cnr.jada.comp.ApplicationException("Prima di procedere all'estrazione del file è necessario scegliere il Quadro.");
		if(e770.getQuadri_770() == null || e770.getQuadri_770().getTi_modello()==null)
			throw new it.cnr.jada.comp.ApplicationException("Prima di procedere all'estrazione del file è necessario validare il Quadro.");
		
		CompensoComponentSession sess = (CompensoComponentSession)createComponentSession();
		sess.doElabora770(context.getUserContext(), e770);


	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}

	
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {

	try {
		Estrazione770Bulk e770 = new Estrazione770Bulk();		
		e770.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		e770.setQuadri_770(null);//  setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
	
		setModel(context, e770);
	} catch(Throwable e) {
		throw handleException(e);
	}
	
	super.init(config,context);
}
}
