/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdg_variazioneBulk extends OggettoBulk {
	// Esercizio di scrivania
	private Integer esercizio;
    // pg_variazione
	private Pdg_variazioneBulk pdg_variazioneForPrint;
	private boolean pdg_variazioneForPrintEnabled;
		
	//private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	//	private boolean cdsForPrintEnabled;	
		
	/**
	 * 
	 */
	public Stampa_pdg_variazioneBulk() {
		super();
	}
	
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_variazioneBulk con i flag inizializzati
	 */
	

	public String getPg_variazione_pdgForPrint() {

			if (getPdg_variazioneForPrint()==null)
				return "*";
			if (getPdg_variazioneForPrint().getPg_variazione_pdg()==null)
				return "*";
			return getPdg_variazioneForPrint().getPg_variazione_pdg().toString();
		}


	
	
	
	public Pdg_variazioneBulk getPdg_variazioneForPrint() {
		return pdg_variazioneForPrint;
	}

	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return boolean
	 */
	public boolean isPdg_variazioneForPrintEnabled() {
		return pdg_variazioneForPrintEnabled;
	}
	
	public void setPdg_variazioneForPrintEnabled(boolean b) {
		pdg_variazioneForPrintEnabled = b;
			}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setPdg_variazioneForPrint(Pdg_variazioneBulk newPdg_variazioneForPrint) {
			pdg_variazioneForPrint = newPdg_variazioneForPrint;
		}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrintEnabled boolean
	 */
	
}
