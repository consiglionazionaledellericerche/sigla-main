package it.cnr.contab.config00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDWorkpackageBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController risultati = new SimpleDetailCRUDController("risultati",it.cnr.contab.config00.latt.bulk.RisultatoBulk.class,"risultati",this);
	private boolean flNuovoPdg = false;
	private boolean flTiGestioneES = false;
	
	private Unita_organizzativaBulk uoScrivania;
	private Integer esercizioScrivania;

	private Pdg_modulo_speseBulk pdgModuloSpese;
	private Pdg_Modulo_EntrateBulk pdgModuloEntrate;
	private Pdg_variazione_riga_gestBulk pdgVariazioneRigaSpese;
	private Pdg_variazione_riga_entrata_gestBulk pdgVariazioneRigaEntrate;
	
	private CdrBulk pdgCdrAssegnatario;

	public CRUDWorkpackageBP() {
	super();
}
public CRUDWorkpackageBP(String function) {
	super(function);
}
public CRUDWorkpackageBP(String function, Pdg_modulo_speseBulk pdgModuloSpese, CdrBulk pdgCdrAssegnatario)
		throws BusinessProcessException {
	this(function);
	this.pdgModuloSpese = pdgModuloSpese;
	this.pdgCdrAssegnatario = pdgCdrAssegnatario;
}
public CRUDWorkpackageBP(String function, Pdg_Modulo_EntrateBulk pdgModuloEntrate, CdrBulk pdgCdrAssegnatario)
		throws BusinessProcessException {
	this(function);
	this.pdgModuloEntrate = pdgModuloEntrate;
	this.pdgCdrAssegnatario = pdgCdrAssegnatario;
}
public CRUDWorkpackageBP(String function, Pdg_variazione_riga_gestBulk pdgVariazioneRiga, CdrBulk pdgCdrAssegnatario)
		throws BusinessProcessException {
	this(function);
	if (pdgVariazioneRiga instanceof Pdg_variazione_riga_entrata_gestBulk)
		this.pdgVariazioneRigaEntrate = (Pdg_variazione_riga_entrata_gestBulk)pdgVariazioneRiga;
	else
		this.pdgVariazioneRigaSpese = pdgVariazioneRiga;
	this.pdgCdrAssegnatario = pdgCdrAssegnatario;
}
/**
 * Ritorna i risultati della linea di attività
 * 
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getRisultati() {
	return risultati;
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
		Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
		setFlTiGestioneES(parEnte.getFl_gae_es().booleanValue());
		if (parEnte.getFl_informix())
			aggiornaGECO(actioncontext.getUserContext());
		super.initialize(actioncontext);
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	} 
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
public boolean isFlTiGestioneES() {
	return flTiGestioneES;
}
public void setFlTiGestioneES(boolean flTiGestioneES) {
	this.flTiGestioneES = flTiGestioneES;
}
@Override
public String getSearchResultColumnSet() {
	if (this.isFlNuovoPdg()) return "prg_liv2";
	return super.getSearchResultColumnSet();
}
@Override
public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk)
		throws BusinessProcessException {
	oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
	try {
		if (this.pdgCdrAssegnatario!=null) {
			((WorkpackageBulk)oggettobulk).setCentro_responsabilita(this.pdgCdrAssegnatario);
			if (this.pdgModuloSpese!=null) {
				Progetto_sipBulk progettoSip = this.pdgModuloSpese.getPdg_modulo_costi().getPdg_modulo().getProgetto();
				ProgettoBulk progetto = (ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progettoSip.getEsercizio(), progettoSip.getPg_progetto(), progettoSip.getTipo_fase()));
				progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
				progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
				((WorkpackageBulk)oggettobulk).setTi_gestione(WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
				((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
				((WorkpackageBulk)oggettobulk).setPdgMissione(this.pdgModuloSpese.getPdgMissione());				
				((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
				((WorkpackageBulk)oggettobulk).setCofog(this.pdgModuloSpese.getCofog());			
			} else if (this.pdgModuloEntrate!=null) {
				Progetto_sipBulk progettoSip = this.pdgModuloEntrate.getTestata().getProgetto();
				ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progettoSip.getEsercizio(), progettoSip.getPg_progetto(), progettoSip.getTipo_fase())));
				progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
				progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
				((WorkpackageBulk)oggettobulk).setTi_gestione(WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
				((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
				((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
			} else if (this.pdgVariazioneRigaSpese!=null) {
				if (this.pdgVariazioneRigaSpese.getProgetto()!=null && this.pdgVariazioneRigaSpese.getProgetto().getPg_progetto()!=null) {
					ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(this.pdgVariazioneRigaSpese.getProgetto().getEsercizio(), this.pdgVariazioneRigaSpese.getProgetto().getPg_progetto(), this.pdgVariazioneRigaSpese.getProgetto().getTipo_fase())));
					progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
					progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
					((WorkpackageBulk)oggettobulk).setTi_gestione(WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
					((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
					((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
				}
			} else if (this.pdgVariazioneRigaEntrate!=null) {
				if (this.pdgVariazioneRigaEntrate.getProgetto()!=null && this.pdgVariazioneRigaEntrate.getProgetto().getPg_progetto()!=null) {
					ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(this.pdgVariazioneRigaEntrate.getProgetto().getEsercizio(), this.pdgVariazioneRigaEntrate.getProgetto().getPg_progetto(), this.pdgVariazioneRigaEntrate.getProgetto().getTipo_fase())));
					progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
					progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
					((WorkpackageBulk)oggettobulk).setTi_gestione(WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
					((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
					((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));
				}
			}
		}
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	} 
	return oggettobulk;
}
@Override
public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
		throws BusinessProcessException {
	oggettobulk = super.initializeModelForSearch(actioncontext, oggettobulk);
	try {
		if (this.pdgCdrAssegnatario!=null) {
			((WorkpackageBulk)oggettobulk).setCentro_responsabilita(this.pdgCdrAssegnatario);
			if (this.pdgModuloSpese!=null) {
				Progetto_sipBulk progettoSip = this.pdgModuloSpese.getPdg_modulo_costi().getPdg_modulo().getProgetto();
				ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progettoSip.getEsercizio(), progettoSip.getPg_progetto(), progettoSip.getTipo_fase())));
				progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
				
				((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
				((WorkpackageBulk)oggettobulk).setPdgMissione(this.pdgModuloSpese.getPdgMissione());
				((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
				((WorkpackageBulk)oggettobulk).setCofog(this.pdgModuloSpese.getCofog());			
			} else if (this.pdgModuloEntrate!=null) {
				Progetto_sipBulk progettoSip = this.pdgModuloEntrate.getTestata().getProgetto();
				ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progettoSip.getEsercizio(), progettoSip.getPg_progetto(), progettoSip.getTipo_fase())));
				progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
				
				((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
				((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
			} else if (this.pdgVariazioneRigaSpese!=null) {
				if (this.pdgVariazioneRigaSpese.getProgetto()!=null && this.pdgVariazioneRigaSpese.getProgetto().getPg_progetto()!=null) {
					ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(this.pdgVariazioneRigaSpese.getProgetto().getEsercizio(), this.pdgVariazioneRigaSpese.getProgetto().getPg_progetto(), this.pdgVariazioneRigaSpese.getProgetto().getTipo_fase())));
					progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
					progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
					((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
					((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));			
				}
			} else if (this.pdgVariazioneRigaEntrate!=null) {
				if (this.pdgVariazioneRigaEntrate.getProgetto()!=null && this.pdgVariazioneRigaEntrate.getProgetto().getPg_progetto()!=null) {
					ProgettoBulk progetto = ((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(this.pdgVariazioneRigaEntrate.getProgetto().getEsercizio(), this.pdgVariazioneRigaEntrate.getProgetto().getPg_progetto(), this.pdgVariazioneRigaEntrate.getProgetto().getTipo_fase())));
					progetto.setProgettopadre((ProgettoBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
					progetto.setOtherField((Progetto_other_fieldBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
							new Progetto_other_fieldBulk(progetto.getPg_progetto())));
				
					((WorkpackageBulk)oggettobulk).setProgetto2016(progetto);
					((WorkpackageBulk)oggettobulk).setPdgProgramma((Pdg_programmaBulk)this.createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));
				}
			}
		}
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	} 
	return oggettobulk;
}
public boolean isMapFromPianoGestioneSpese(){
	return this.pdgModuloSpese!=null;
}
public boolean isMapFromPianoGestioneEntrate(){
	return this.pdgModuloEntrate!=null;
}
public boolean isMapFromVariazioneGestioneSpese(){
	return this.pdgVariazioneRigaSpese!=null;
}
public boolean isMapFromVariazioneGestioneEntrate(){
	return this.pdgVariazioneRigaEntrate!=null;
}
public boolean isFieldProgettoDisable(){
	return this.isMapFromPianoGestioneSpese()||this.isMapFromPianoGestioneEntrate()||
			(this.isMapFromVariazioneGestioneSpese() && this.pdgVariazioneRigaSpese.getProgetto()!=null && this.pdgVariazioneRigaSpese.getProgetto().getPg_progetto()!=null)||
			(this.isMapFromVariazioneGestioneEntrate() && this.pdgVariazioneRigaEntrate.getProgetto()!=null && this.pdgVariazioneRigaEntrate.getProgetto().getPg_progetto()!=null);
}
@Override
public void validate(ActionContext actioncontext) throws ValidationException {
	if ((isMapFromPianoGestioneSpese() || isMapFromVariazioneGestioneSpese()) && WorkpackageBulk.TI_GESTIONE_ENTRATE.equals(((WorkpackageBulk)getModel()).getTi_gestione()))
		throw new ValidationException("Il tipo di gestione non può essere di tipo entrata.");
	if ((isMapFromPianoGestioneEntrate() || isMapFromVariazioneGestioneEntrate()) && WorkpackageBulk.TI_GESTIONE_SPESE.equals(((WorkpackageBulk)getModel()).getTi_gestione()))
		throw new ValidationException("Il tipo di gestione non può essere di tipo spesa.");
	super.validate(actioncontext);
}
@Override
public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
	if (compoundfindclause==null)
		compoundfindclause = new CompoundFindClause();

	if (isMapFromPianoGestioneSpese() || isMapFromVariazioneGestioneSpese())
		compoundfindclause.addClause(FindClause.AND, "ti_gestione", SQLBuilder.NOT_EQUALS, WorkpackageBulk.TI_GESTIONE_ENTRATE);
	else if (isMapFromPianoGestioneEntrate() || isMapFromVariazioneGestioneEntrate())
		compoundfindclause.addClause(FindClause.AND, "ti_gestione", SQLBuilder.NOT_EQUALS, WorkpackageBulk.TI_GESTIONE_SPESE);
	return super.find(actioncontext, compoundfindclause, oggettobulk);
}
}