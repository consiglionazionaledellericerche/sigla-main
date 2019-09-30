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
import it.cnr.contab.docamm00.tabrif.bulk.CambioHome;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;


/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 14.29.38)
 * @author: Paola sala
 */
public class CRUDTipoContributoRitenutaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final it.cnr.jada.util.action.SimpleDetailCRUDController intervalliCRUDController = new it.cnr.jada.util.action.SimpleDetailCRUDController("intervalli",Tipo_contributo_ritenutaBulk.class,"intervalli",this);

/**
 * CRUDTipoContributoRitenutaBP constructor comment.
 */
public CRUDTipoContributoRitenutaBP() {
	super();
	intervalliCRUDController.setEnabled(false);
}
/**
 * CRUDTipoContributoRitenutaBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTipoContributoRitenutaBP(String function) {
	super(function);
	intervalliCRUDController.setEnabled(false);
}
//
//	Non devo poter modificare un record che non sia l'ultimo in termini
//  di validita' di periodo (cioe' se il record ha data fine validita' 
//	diversa da 31/12/2200 non lo posso modificare)
//
public void basicEdit(ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws BusinessProcessException {

	super.basicEdit(context, bulk, doInitializeForEdit);

	if (!isViewing()){

		Tipo_contributo_ritenutaBulk tipoCori = (Tipo_contributo_ritenutaBulk)bulk;
		java.sql.Timestamp dataOdierna = CompensoBulk.getDataOdierna();

		if (tipoCori.getDt_fin_validita().before(dataOdierna)){
			setStatus(VIEW);		
			setMessage("E' possibile modificare solo il record attivo!");
		}else{
			if (tipoCori.getDt_fin_validita().equals(dataOdierna) && !isUltimoIntervallo(context, tipoCori)){
				setStatus(VIEW);		
				setMessage("E' possibile modificare solo l'ultimo record!");
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
private boolean isUltimoIntervallo(ActionContext context,Tipo_contributo_ritenutaBulk tipoCori) throws BusinessProcessException {

	try {
		
		TipoContributoRitenutaComponentSession session = (TipoContributoRitenutaComponentSession)createComponentSession();
		return session.isUltimoIntervallo(context.getUserContext(), tipoCori);
		
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
}
