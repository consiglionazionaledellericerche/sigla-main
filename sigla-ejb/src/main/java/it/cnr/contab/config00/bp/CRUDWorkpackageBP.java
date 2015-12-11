package it.cnr.contab.config00.bp;


import java.rmi.RemoteException;

import it.cnr.contab.config00.blob.bulk.PostItBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDWorkpackageBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController risultati = new SimpleDetailCRUDController("risultati",it.cnr.contab.config00.latt.bulk.RisultatoBulk.class,"risultati",this);
	private SimpleDetailCRUDController crudDettagliPostIt = new SimpleDetailCRUDController( "DettagliPostIt", PostItBulk.class, "dettagliPostIt", this);
	private boolean flNuovoPdg = false;
	
	private Unita_organizzativaBulk uoScrivania;
	private Integer esercizioScrivania;

	public CRUDWorkpackageBP() {
	super();
}
public CRUDWorkpackageBP(String function) {
	super(function);
}
/**
 * Ritorna i risultati della linea di attività
 * 
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getRisultati() {
	return risultati;
}
/*Angelo 18/11/2004 Aggiunta gestione PostIt*/
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagliPostIt() {
	return crudDettagliPostIt;
}
public boolean isDeleteButtonEnabled() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk tipo_la = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk)getModel();
	return
		super.isDeleteButtonEnabled() &&
		tipo_la != null &&
	    (tipo_la.getTipo_linea_attivita() != null) &&
        (tipo_la.getTipo_linea_attivita().getTi_tipo_la().equals(it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk.PROPRIA));
}
public void reset(ActionContext context) throws BusinessProcessException {
	super.reset(context);
	resetTabs();
}
public void resetForSearch(ActionContext context) throws BusinessProcessException {
	super.resetForSearch(context);
	resetTabs();
}
private void resetTabs() {
	setTab("tab","tabTestata");
}

/* 
 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
 * Sovrascrive quello presente nelle superclassi
 * 
*/

public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

		openForm(context,action,target,"multipart/form-data");
	
}
public static ProgettoRicercaPadreComponentSession getProgettoRicercaPadreComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
	return (ProgettoRicercaPadreComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession",ProgettoRicercaPadreComponentSession.class);
}	
private void aggiornaGECO(UserContext userContext) {
	try {
		getProgettoRicercaPadreComponentSession().aggiornaGECO(userContext);
	} catch (Exception e) {
		String text = "Errore interno del Server Utente:"+CNRUserContext.getUser(userContext);
		SendMail.sendErrorMail(text,e.toString());
	}
}

@Override
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	try {
		setUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
		setEsercizioScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
		setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
		aggiornaGECO(actioncontext.getUserContext());
		super.initialize(actioncontext);
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	} 
}
/*
 * Utilizzato per la gestione del bottone di attivazione del PostIt
 * Sovrascrive il metodo di SimpleCRUDBP
 * */
public boolean isActive(OggettoBulk bulk,int sel) {
  if (bulk instanceof WorkpackageBulk)	
	try
	{
		return ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(sel)).isROpostit();
	}
	catch (RuntimeException e)
	{
		if (sel==0)
		return false;
		else
		e.printStackTrace();
	}
	return false;
}
@Override
public boolean isNewButtonEnabled() {
	if (isUoArea() && !this.isFlNuovoPdg())
    	return false;
    else
    	return super.isNewButtonEnabled();
}
public boolean isUoArea(){
	return (getUoScrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0);
}
public Unita_organizzativaBulk getUoScrivania() {
	return uoScrivania;
}
public void setUoScrivania(Unita_organizzativaBulk uoScrivania) {
	this.uoScrivania = uoScrivania;
}
public Integer getEsercizioScrivania() {
	return esercizioScrivania;
}
public void setEsercizioScrivania(Integer esercizioScrivania) {
	this.esercizioScrivania = esercizioScrivania;
}
public boolean isFlNuovoPdg() {
	return flNuovoPdg;
}
private void setFlNuovoPdg(boolean flNuovoPdg) {
	this.flNuovoPdg = flNuovoPdg;
}
@Override
public String getSearchResultColumnSet() {
	if (this.isFlNuovoPdg()) return "prg_liv2";
	return super.getSearchResultColumnSet();
}
}