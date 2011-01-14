package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 16.55.25)
 * @author: CNRADM
 */
public class CRUDScaglioneAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDScaglioneAction constructor comment.
 */
public CRUDScaglioneAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.18.12)
 * @param context it.cnr.jada.action.ActionContext
 */
public Forward doAggiungiScaglione(ActionContext context) {

	try{
		fillModel(context);
		CRUDScaglioneBP bp = (CRUDScaglioneBP)getBusinessProcess(context);
		bp.doAggiungiScaglione(context);
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
public Forward doBlankSearchFind_provincia(ActionContext context, ScaglioneBulk scaglione) {

	if(scaglione != null) {
		scaglione.setProvincia(new ProvinciaBulk());
		scaglione.setComune(new ComuneBulk());
	}

	return context.findDefaultForward();
}
public Forward doBlankSearchFind_regione(ActionContext context, ScaglioneBulk scaglione) {

	if(scaglione != null) {
		scaglione.setRegione(new RegioneBulk());
		scaglione.setProvincia(new ProvinciaBulk());
		scaglione.setComune(new ComuneBulk());
	}

	return context.findDefaultForward();
}
public Forward doBringBackSearchFind_provincia(ActionContext context, ScaglioneBulk scaglione, ProvinciaBulk prov) {

	if(scaglione != null) {
		scaglione.setProvincia(prov);
		scaglione.setComune(new ComuneBulk());
	}

	return context.findDefaultForward();
}
public Forward doBringBackSearchFind_regione(ActionContext context, ScaglioneBulk scaglione, RegioneBulk reg) {

	if(scaglione != null) {
		scaglione.setRegione(reg);
		scaglione.setProvincia(new ProvinciaBulk());
		scaglione.setComune(new ComuneBulk());
	}

	return context.findDefaultForward();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDScaglioneBP bp = (CRUDScaglioneBP)getBusinessProcess(context);
		bp.delete(context);
		ScaglioneBulk scaglione = (ScaglioneBulk)bp.getModel();
		if (scaglione.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}else{
			bp.edit(context, scaglione);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.18.12)
 * @param context it.cnr.jada.action.ActionContext
 */
public Forward doEliminaScaglione(ActionContext context) {

	try{
		fillModel(context);
		CRUDScaglioneBP bp = (CRUDScaglioneBP)getBusinessProcess(context);
		bp.doEliminaScaglione(context);
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
//
// Nella ricerca dei Tipi contributo e ritenuta imposto la data di fine
// validita' ad infinito in modo da caricare i record piu' recenti
//

public it.cnr.jada.action.Forward doSearchFind_comune(it.cnr.jada.action.ActionContext context) {

	try {

		CRUDScaglioneBP bp = (CRUDScaglioneBP)context.getBusinessProcess();
		ScaglioneBulk scaglione = (ScaglioneBulk)bp.getModel();

		if (scaglione.getCd_provincia()==null){
			throw new MessageToUser("Selezionare la provincia!");
		}

		it.cnr.jada.util.action.FormField field = bp.getFormField("find_comune");
		return search(context,field,null);

	}catch(Exception ex) {
		return handleException(context,ex);
	}
}
//
// Nella ricerca dei Tipi contributo e ritenuta imposto la data di fine
// validita' ad infinito in modo da caricare i record piu' recenti
//

public it.cnr.jada.action.Forward doSearchFind_provincia(it.cnr.jada.action.ActionContext context) {

	try {

		CRUDScaglioneBP bp = (CRUDScaglioneBP)context.getBusinessProcess();
		ScaglioneBulk scaglione = (ScaglioneBulk)bp.getModel();

		if (scaglione.getCd_regione()==null){
			throw new MessageToUser("Selezionare la regione!");
		}

		it.cnr.jada.util.action.FormField field = bp.getFormField("find_provincia");
		return search(context,field,null);

	}catch(Exception ex) {
		return handleException(context,ex);
	}
}
}
