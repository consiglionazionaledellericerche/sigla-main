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
public class Stampa_var_stanz_resBulk extends OggettoBulk {
	// Esercizio di scrivania
	private Integer esercizio;
    // pg_variazione
	private Var_stanz_resBulk variazioneForPrint;
	private boolean variazioneForPrintEnabled;
		
	//private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	//	private boolean cdsForPrintEnabled;	
		
	/**
	 * 
	 */
	public Stampa_var_stanz_resBulk() {
		super();
	}
	
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_variazioneBulk con i flag inizializzati
	 */
	

	public String getPg_variazioneForPrint() {

			if (getVariazioneForPrint()==null)
				return null;
			if (getVariazioneForPrint().getPg_variazione()==null)
				return null;
			return getVariazioneForPrint().getPg_variazione().toString();
		}


	
	
	
	public Var_stanz_resBulk getVariazioneForPrint() {
		return variazioneForPrint;
	}

	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return boolean
	 */
	public boolean isVariazioneForPrintEnabled() {
		return variazioneForPrintEnabled;
	}
	
	public void setVariazioneForPrintEnabled(boolean b) {
		variazioneForPrintEnabled = b;
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
	public void setVariazioneForPrint(Var_stanz_resBulk newVariazioneForPrint) {
			variazioneForPrint = newVariazioneForPrint;
		}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrintEnabled boolean
	 */
	
}
