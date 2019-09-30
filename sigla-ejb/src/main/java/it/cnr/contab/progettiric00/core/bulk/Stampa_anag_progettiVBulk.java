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

package it.cnr.contab.progettiric00.core.bulk;

/**
 * @author marco spasiano
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.*;

import java.util.Dictionary;

public class Stampa_anag_progettiVBulk extends ProgettoBulk {

    private ProgettoBulk progettoForPrint;
    private Unita_organizzativaBulk unita_organizzativaForPrint;
    private boolean isUOForPrintEnabled;
    private java.lang.Integer esercizioForPrint;
    public static final Integer LIVELLO_PROGETTO_ALL 	= new Integer(99);
        
        public final static Dictionary livello_progettoKeys;
        static {
		livello_progettoKeys = new it.cnr.jada.util.OrderedHashtable();
		livello_progettoKeys.put(LIVELLO_PROGETTO_PRIMO,"Progetto");
		livello_progettoKeys.put(LIVELLO_PROGETTO_SECONDO,"Commessa");
		livello_progettoKeys.put(LIVELLO_PROGETTO_TERZO,"Modulo di Attività");
		//livello_progettoKeys.put(LIVELLO_PROGETTO_ALL,"Sottogruppo");
	};		
	public final static Dictionary tipo_faseKeys;
	static {
		tipo_faseKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseKeys.put(TIPO_FASE_PREVISIONE,"Previsione");
		tipo_faseKeys.put(TIPO_FASE_GESTIONE,"Gestione");
		tipo_faseKeys.put("E","Entrambi");
	};
	/**
	 * Constructor for Stampa_progettiVBulk.
	 */
	public Stampa_anag_progettiVBulk() {
		super();
	}
	/**
	 * Returns the progettoForPrint.
	 * @return ProgettoBulk
	 */
	public ProgettoBulk getProgettoForPrint() {
		return progettoForPrint;
	}
	
	/**
	 * Sets the progettoForPrint.
	 * @param progettoForPrint The progettoForPrint to set
	 */
	public void setProgettoForPrint(ProgettoBulk progettoForPrint) {
		this.progettoForPrint = progettoForPrint;
	}
    public boolean isROProgettoForPrint(){
	   return progettoForPrint == null || progettoForPrint.getCrudStatus() == NORMAL;
    }
    public String getCdProgettoCRForPrint() {

      if (getProgettoForPrint() == null)
        return "*";
      if (getProgettoForPrint().getCd_progetto()== null)
          return "*";
      return getProgettoForPrint().getCd_progetto().toString();
    }	
    public String getCdUnita_organizzativaCRForPrint() {

      if (getUnita_organizzativaForPrint() == null)
        return "*";
      if (getUnita_organizzativaForPrint().getCd_unita_organizzativa()== null)
          return "*";
      return getUnita_organizzativaForPrint().getCd_unita_organizzativa().toString();
    }    
	/**
	 * Returns the stato.
	 * @return java.lang.String
	 */
	public java.lang.String getStato() {
		if (super.getStato() == null)
		  return "*";
		return super.getStato();
	}    
    /**
     * Insert the method's description here.
     * Creation date: (13/02/2003 14.11.01)
     * @return java.lang.String
     */
    public Integer getTc() {
	    return new Integer(0);
    }
    
    public boolean isROunita_organizzativaForPrint(){
	   return !isUOForPrintEnabled;
    }    
	/**
	 * Returns the unita_organizzativaForPrint.
	 * @return Unita_organizzativaBulk
	 */
	public Unita_organizzativaBulk getUnita_organizzativaForPrint() {
		return unita_organizzativaForPrint;
	}

	/**
	 * Sets the unita_organizzativaForPrint.
	 * @param unita_organizzativaForPrint The unita_organizzativaForPrint to set
	 */
	public void setUnita_organizzativaForPrint(Unita_organizzativaBulk unita_organizzativaForPrint) {
		this.unita_organizzativaForPrint = unita_organizzativaForPrint;
	}
    /**
     * Insert the method's description here.
     * Creation date: (08/04/2003 15.30.39)
     * @param newIsUOForPrintEnabled boolean
     */
    public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	  isUOForPrintEnabled = newIsUOForPrintEnabled;
    }
    /**
     * Insert the method's description here.
     * Creation date: (08/04/2003 15.30.39)
     * @return boolean
     */
    public boolean isUOForPrintEnabled() {
    	return isUOForPrintEnabled;
    }
	
	public java.lang.Integer getEsercizioForPrint() {
		return esercizioForPrint;
	}
	public void setEsercizioForPrint(java.lang.Integer esercizioForPrint) {
		this.esercizioForPrint = esercizioForPrint;
	}    	
}
