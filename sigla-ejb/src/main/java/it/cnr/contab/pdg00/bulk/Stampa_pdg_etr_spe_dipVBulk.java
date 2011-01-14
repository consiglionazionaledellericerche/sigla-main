/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdg_etr_spe_dipVBulk extends Stampa_pdg_etr_speVBulk {
	// Il Progetto
	private ProgettoBulk progettoForPrint = new ProgettoBulk();
    //	Il Dipartimento
	private DipartimentoBulk dipartimentoForPrint = new DipartimentoBulk();
	// Check per i raggruppamenti
	private Boolean ragrr_tipo_progetto;
	private Boolean ragrr_area;
	/**
	 * 
	 */
	public Stampa_pdg_etr_spe_dipVBulk() {
		super();
	}

	/**
	 * @return
	 */
	public ProgettoBulk getProgettoForPrint() {
		return progettoForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setProgettoForPrint(ProgettoBulk bulk) {
		progettoForPrint = bulk;
	}
	/**
	 * 
	 * @return
	 */
	public String getCd_progettoCRForPrint() {
		if (getProgettoForPrint()==null)
			return "*";
		if (getProgettoForPrint().getCd_progetto()==null)
			return "*";
		return getProgettoForPrint().getCd_progetto().toString();
	}
	/**
	 * @return
	 */
	public Boolean getRagrr_area() {
		return ragrr_area;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_tipo_progetto() {
		return ragrr_tipo_progetto;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_area(Boolean boolean1) {
		ragrr_area = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_tipo_progetto(Boolean boolean1) {
		ragrr_tipo_progetto = boolean1;
	}

	/**
	 * @return
	 */
	public DipartimentoBulk getDipartimentoForPrint() {
		return dipartimentoForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setDipartimentoForPrint(DipartimentoBulk bulk) {
		dipartimentoForPrint = bulk;
	}
	/**
	 * 
	 * @return
	 */
	public String getCd_dipartimentoCRForPrint() {
		if (getDipartimentoForPrint()==null)
			return "*";
		if (getDipartimentoForPrint().getCd_dipartimento()==null)
			return "*";
		return getDipartimentoForPrint().getCd_dipartimento().toString();
	}
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_etr_speVBulk con i falg inizializzati
	 */
	public void inizializzaRagruppamenti() {
		super.inizializzaRagruppamenti();
		setRagrr_tipo_progetto(new Boolean(false));
		setRagrr_area(new Boolean(false));		
	}	
	public void selezionaRagruppamenti(){
		super.selezionaRagruppamenti();		
		setRagrr_tipo_progetto(new Boolean(!getRagrr_tipo_progetto().booleanValue()));
		setRagrr_area(new Boolean(!getRagrr_area().booleanValue()));	
	}	
}
