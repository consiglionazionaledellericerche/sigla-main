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

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (08/03/2002 14.14.23)
 * @author: Roberto Fantino
 */
public class CRUDTipoTrattamentoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final it.cnr.jada.util.action.SimpleDetailCRUDController intervalliCRUDController = new it.cnr.jada.util.action.SimpleDetailCRUDController("intervalli",Tipo_trattamentoBulk.class,"intervalli",this);
/**
 * CRUDTipoTrattamentoBP constructor comment.
 */
public CRUDTipoTrattamentoBP() {
	super();
	intervalliCRUDController.setEnabled(false);
}
/**
 * CRUDTipoTrattamentoBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTipoTrattamentoBP(String function) {
	super(function);
	intervalliCRUDController.setEnabled(false);
}
public void basicEdit(ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws BusinessProcessException {

	super.basicEdit(context, bulk, doInitializeForEdit);

	if (!isViewing()){

		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bulk;
		java.sql.Timestamp dataOdierna = CompensoBulk.getDataOdierna();

		if (tratt.getDt_fin_validita().before(dataOdierna)){
			setStatus(VIEW);		
			setMessage("E' possibile modificare solo il record attivo!");
		}else{
			if (tratt.getDt_fin_validita().equals(dataOdierna) && !isUltimoIntervallo(context, tratt)){
				setStatus(VIEW);		
				setMessage("E' possibile modificare solo l'ultimo intervallo!");
			}
		}
	}

}
/**
 * Insert the method's description here.
 * Creation date: (04/06/2002 14.28.08)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public it.cnr.jada.util.action.SimpleDetailCRUDController getIntervalliCRUDController() {
	return intervalliCRUDController;
}
private boolean isUltimoIntervallo(ActionContext context,Tipo_trattamentoBulk tratt) throws BusinessProcessException {

	try {
		
		TipoTrattamentoComponentSession session = (TipoTrattamentoComponentSession)createComponentSession();
		return session.isUltimoIntervallo(context.getUserContext(), tratt);
		
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
}
