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
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (21/03/2005 12.32.44)
 * @author: Tilde
 */
public class EstrazioneINPSMensileBP extends AbstractEstrazioneFiscaleBP {
/**
 * EstrazioneINPSMensileBP constructor comment.
 */
public EstrazioneINPSMensileBP() {
	super();
}
/**
 * EstrazioneINPSMensileBP constructor comment.
 * @param function java.lang.String
 */
public EstrazioneINPSMensileBP(String function) {
	super(function);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.extractINPSMensile");

	return toolbar;
}
public void doElaboraINPSMensile(ActionContext context) throws BusinessProcessException {

	try{
		EstrazioneINPSMensileBulk inps = (EstrazioneINPSMensileBulk)getModel();

		CompensoComponentSession sess = (CompensoComponentSession)createComponentSession();
		sess.doElaboraINPSMensile(context.getUserContext(), inps);


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
		EstrazioneINPSMensileBulk inps = new EstrazioneINPSMensileBulk();
		/*  da verificare se aggiungerlo o meno anche nel Bulk
		inps.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		*/
		inps.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		
		setModel(context, inps);
	} catch(Throwable e) {
		throw handleException(e);
	}
	
	super.init(config,context);
}
}
