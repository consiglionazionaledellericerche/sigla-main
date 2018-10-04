/*
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.xml.bind.ValidationException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;


/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdg_Modulo_EntrateBP extends it.cnr.jada.util.action.SimpleCRUDBP{
	private Parametri_cnrBulk parametriCnr;
	private Parametri_enteBulk parametriEnte;
	private Parametri_livelliBulk parametriLivelli;
	private Progetto_sipBulk progetto;
	private String descrizioneClassificazione;
	private Integer esercizio;
	private CdrBulk cdr;
	private boolean Utente_Ente;
	private String anno_corrente,anno_successivo,anno_successivo_successivo;
	private boolean isPDGPEsaminatoDalCentroUP = false;
	public CRUDPdg_Modulo_EntrateBP() {
		super();
	}

	public CRUDPdg_Modulo_EntrateBP(String function) {
		super(function);

		//if("V".equals(function)) roStato = true;
		//else roStato = false;
	}
	
	public CRUDPdg_Modulo_EntrateBP(String function,Integer esercizio,CdrBulk cdr,Progetto_sipBulk progetto) {
		super(function);
		setEsercizio(esercizio);
		setCdr(cdr);
		setProgetto(progetto);		
	}
	
	public boolean isNewButtonHidden() {
	
		return true;
	}
	public boolean isFreeSearchButtonHidden() {

		return true;
	}
	public boolean isSearchButtonHidden() {
		return true;
	}
	public boolean isDeleteButtonEnabled()
	{
		return super.isDeleteButtonEnabled() && this.getCrudDettagliEntrate().countDetails()!=0 && !isUtente_Ente();
	}
	
   private CrudDettagliEntrataBP crudDettagliEntrate = new CrudDettagliEntrataBP( "dettagliCRUDController", Pdg_Modulo_EntrateBulk.class, "dettagli_entrata", this) {
	   protected void validate(ActionContext actioncontext, it.cnr.jada.bulk.OggettoBulk oggettobulk) throws it.cnr.jada.bulk.ValidationException {
		   if (getParametriEnte().getFl_prg_pianoeco() && ((Pdg_Modulo_EntrateBulk)oggettobulk).getVoce_piano_economico()==null) {
				Progetto_sipBulk progetto = ((Pdg_Modulo_EntrateBulk )oggettobulk).getTestata().getProgetto();
				if (progetto!=null && progetto.getTipoFinanziamento().getFlPianoEcoFin())
					throw new it.cnr.jada.bulk.ValidationException("Il progetto selezionato richiede l'indicazione della Voce del Piano Economico.");
		   }
	   };
   };

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		setModel(actioncontext,super.initializeModelForEdit(actioncontext,new Pdg_moduloBulk(getEsercizio(),getCdr().getCd_centro_responsabilita(),getProgetto().getPg_progetto())));
		setStatus(EDIT);
		if (!isEditable())
			setStatus(VIEW);
		try {
			setParametriLivelli(((PdgModuloEntrateComponentSession)createComponentSession()).findParametriLivelli(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
		
			setParametriCnr(((PdgModuloEntrateComponentSession)createComponentSession()).parametriCnr(actioncontext.getUserContext()));
			
			setParametriEnte(Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext()));

			setUtente_Ente(((PdgModuloEntrateComponentSession)createComponentSession()).isUtenteEnte(actioncontext.getUserContext()));
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		isPDGPEsaminatoDalCentroUP(actioncontext, getCdr());
		anno_corrente = CNRUserContext.getEsercizio(actioncontext.getUserContext()).toString();
		anno_successivo = new Integer(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue() + 1).toString();
		anno_successivo_successivo = new Integer(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue() + 2).toString();
		
		setDescrizioneClassificazione(getParametriLivelli().getDs_livello_etr(parametriCnr.getLivello_pdg_decis_etr().intValue())); 
	}
	public void isPDGPEsaminatoDalCentroUP(ActionContext actioncontext, CdrBulk cdr) throws BusinessProcessException{
		try {
			Pdg_esercizioBulk pdg_eserizio = (Pdg_esercizioBulk)Utility.createPdgAggregatoModuloComponentSession().getCdrPdGP(actioncontext.getUserContext(),cdr);
			if (pdg_eserizio.getStato().equalsIgnoreCase(Pdg_esercizioBulk.STATO_ESAMINATO_CDR) ||
				pdg_eserizio.getStato().equalsIgnoreCase(Pdg_esercizioBulk.STATO_APPROVAZIONE_CDR)||
				pdg_eserizio.getStato().equalsIgnoreCase(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR)||
				pdg_eserizio.getStato().equalsIgnoreCase(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR))
				setPDGPEsaminatoDalCentroUP(true);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (EJBException e) {
			throw new BusinessProcessException(e);
		}
	}
	public Parametri_livelliBulk getParametriLivelli(ActionContext actioncontext) throws BusinessProcessException {
	try {			
		if (parametriLivelli == null)
			setParametriLivelli(((PdgModuloEntrateComponentSession)createComponentSession()).findParametriLivelli(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
				
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		}
		return getParametriLivelli();
	}

	public Parametri_livelliBulk getParametriLivelli() {
		return parametriLivelli;
	}
	public void setParametriLivelli(Parametri_livelliBulk bulk) {
		parametriLivelli = bulk;
	}
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}

	public void setParametriCnr(Parametri_cnrBulk bulk) {
			parametriCnr = bulk;
	}
	public void setCdr(CdrBulk bulk) {
		cdr = bulk;
	}
	public void setEsercizio(Integer bulk) {
		esercizio = bulk;
	}
	public void setProgetto(Progetto_sipBulk bulk) {
		progetto = bulk;
	}

	public CdrBulk getCdr() {
		return cdr;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public Progetto_sipBulk getProgetto() {
		return progetto;
	}
    public String getDescrizioneClassificazione() {
 	   return descrizioneClassificazione;
    }

    public void setDescrizioneClassificazione(String string) {
	   descrizioneClassificazione = string;
    }
	public CrudDettagliEntrataBP getCrudDettagliEntrate() {
		return crudDettagliEntrate;

	}

	public void setCrudDettagliEntrate(CrudDettagliEntrataBP controller) {
		crudDettagliEntrate = controller;
		
	}
	
	public boolean isUtente_Ente() {
		return Utente_Ente;
	}

	public void setUtente_Ente(boolean b) {
		Utente_Ente = b;
	}
	public String getAnno_corrente() {
		return anno_corrente;
	}

	public String getAnno_successivo() {
		return anno_successivo;
	}

	public String getAnno_successivo_successivo() {
		return anno_successivo_successivo;
	}

	public boolean isPDGPEsaminatoDalCentroUP() {
		return isPDGPEsaminatoDalCentroUP;
	}

	public void setPDGPEsaminatoDalCentroUP(boolean isPDGPEsaminatoDalCentroUP) {
		this.isPDGPEsaminatoDalCentroUP = isPDGPEsaminatoDalCentroUP;
	}

	public boolean isDeleteModuloButtonHidden() {
		return super.isDeleteButtonHidden() || this.getParametriCnr().getFl_nuovo_pdg();
	}

	public boolean isDeleteProgettoButtonHidden() {
		return super.isDeleteButtonHidden() || !this.getParametriCnr().getFl_nuovo_pdg();
	}
	
	public Parametri_enteBulk getParametriEnte() {
		return parametriEnte;
	}
	
	public void setParametriEnte(Parametri_enteBulk parametriEnte) {
		this.parametriEnte = parametriEnte;
	}
}
