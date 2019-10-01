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
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 14.32.44)
 * @author: Paola sala
 */
public class CRUDScaglioneBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final it.cnr.jada.util.action.SimpleDetailCRUDController scaglioniCRUDController = new it.cnr.jada.util.action.SimpleDetailCRUDController("scaglioni",ScaglioneBulk.class,"scaglioni",this, false);
/**
 * CRUDScaglioneBP constructor comment.
 */
public CRUDScaglioneBP() {
	super();
}
/**
 * CRUDScaglioneBP constructor comment.
 * @param function java.lang.String
 */
public CRUDScaglioneBP(String function) {
	super(function);
}
public void basicEdit(ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws BusinessProcessException {

	try {
		super.basicEdit(context, bulk, doInitializeForEdit);

		if (!isViewing()){
			ScaglioneBulk aScaglione = (ScaglioneBulk) getModel();
			java.sql.Timestamp dataOdierna = it.cnr.contab.compensi00.docs.bulk.CompensoBulk.getDataOdierna();

			if (aScaglione.getContributo_ritenuta().getDt_fin_validita().compareTo(dataOdierna)<0){
				setMessage("Il Tipo Contributa Ritenuta \"" + aScaglione.getCd_contributo_ritenuta() + "\" non è più valido");
				setStatus(VIEW);
			}else if (aScaglione.getDt_fine_validita().compareTo(dataOdierna)<=0){
				setStatus(VIEW);
		 		setMessage("E' possibile modificare solo il record corrente!");
/*
			}else{
				if (aScaglione.getDt_fine_validita().equals(dataOdierna) && !isUltimoIntervallo(context, aScaglione)){
					setStatus(VIEW);		
					setMessage("E' possibile modificare solo l'ultimo record!");
				}
*/
			}
		}
	
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public void doAggiungiScaglione(ActionContext context) throws BusinessProcessException {

	try {
		ScaglioneBulk testata = (ScaglioneBulk)getModel();
		validaScaglione(context);
		
		ScaglioneBulk scaglione = (ScaglioneBulk)testata.clone();
		testata.getScaglioni().add(scaglione);

		java.util.Collections.sort(
			testata.getScaglioni(),
			new java.util.Comparator() {
				public int compare(Object o1, Object o2) {
					ScaglioneBulk s1 = (ScaglioneBulk)o1;
					ScaglioneBulk s2 = (ScaglioneBulk)o2;
					return s1.getIm_inferiore().compareTo(s2.getIm_inferiore());
				}
			});

		testata.setIm_inferiore(new java.math.BigDecimal(0));
		testata.setIm_superiore(null);

		getScaglioniCRUDController().getSelection().clear();
		getScaglioniCRUDController().setModelIndex(context, -1);

	}catch (ValidationException ex){
		throw handleException(ex);
	}
}
public void doEliminaScaglione(ActionContext context) throws BusinessProcessException {

	try{
		
		ScaglioneBulk testata = (ScaglioneBulk)getModel();
		if (!getScaglioniCRUDController().iterator().hasNext())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare almeno uno scaglione.");

		for (java.util.Iterator i = getScaglioniCRUDController().iterator();i.hasNext();)
			testata.getScaglioni().remove((ScaglioneBulk)i.next());

		java.util.Collections.sort(
			testata.getScaglioni(),
			new java.util.Comparator() {
				public int compare(Object o1, Object o2) {
					ScaglioneBulk s1 = (ScaglioneBulk)o1;
					ScaglioneBulk s2 = (ScaglioneBulk)o2;
					return s1.getIm_inferiore().compareTo(s2.getIm_inferiore());
				}
			});

		getScaglioniCRUDController().getSelection().clear();
		getScaglioniCRUDController().setModelIndex(context, -1);

	}catch(it.cnr.jada.comp.ApplicationException ex){
		throw handleException(ex);
	}

}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.27.02)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public it.cnr.jada.util.action.SimpleDetailCRUDController getScaglioniCRUDController() {
	return scaglioniCRUDController;
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.19.09)
 * @return boolean
 */
public boolean isBottoneAggiungiScaglioneEnabled() {
	return !isViewing();
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.19.09)
 * @return boolean
 */
public boolean isBottoneEliminaScaglioneEnabled() {
	return !isViewing();
//	return getScaglioniCRUDController().iterator().hasNext();
}
private boolean isUltimoIntervallo(ActionContext context,ScaglioneBulk scaglione) throws BusinessProcessException {

	try {
		
		ScaglioneComponentSession session = (ScaglioneComponentSession)createComponentSession();
		return session.isUltimoIntervallo(context.getUserContext(), scaglione);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
private void validaScaglione(ActionContext context) throws ValidationException{

	try{
		ScaglioneBulk testata = (ScaglioneBulk)getModel();

		// valido tutti i campi obbligatori e gli importi inseriti
		testata.validate();

		// controllo con scaglioni definiti precedentemente per eventuali sovrapposizioni
		java.util.List l = testata.getScaglioni();
		if (l!=null && !l.isEmpty())
			for(int i = 0;i<l.size();i++){
				ScaglioneBulk el = (ScaglioneBulk)l.get(i);
			
				if (testata.getIm_superiore().compareTo(el.getIm_inferiore())<0)
					return;
				if (testata.getIm_inferiore().compareTo(el.getIm_superiore())<=0)
					throw new ValidationException("Scaglione non valido. Sovrapposizione con scaglioni precedentemente definiti");
			}

		// non posso inserire uno scaglione in un intervallo già creato
		ScaglioneComponentSession session = (ScaglioneComponentSession)createComponentSession();
		session.validaScaglione(context.getUserContext(), testata);

	}catch(Throwable ex){
		throw new ValidationException(ex.getMessage());
	}
	
}
}
