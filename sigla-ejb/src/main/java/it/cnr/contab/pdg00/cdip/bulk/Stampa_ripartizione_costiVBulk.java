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

package it.cnr.contab.pdg00.cdip.bulk;

/**
 * Insert the type's description here.
 * Creation date: (08/06/2004 15.09.42)
 * @author: Gennaro Borriello
 */

import it.cnr.contab.progettiric00.core.bulk.*;
public class Stampa_ripartizione_costiVBulk extends it.cnr.jada.bulk.OggettoBulk {

	private Integer esercizio;
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}


	private String cd_cds;
	
	
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private ProgettoBulk commessaForPrint;
	private ProgettoBulk moduloForPrint;
	private V_dipendenteBulk dipendenteForPrint;
	
	private boolean isUOForPrintEnabled;
	private boolean isCommessaForPrintEnabled;
	private boolean isModuloForPrintEnabled;
	private boolean isDipendenteForPrintEnabled;
	
	// MESE int
	private Integer mese;

	public static final Integer MESE_0 = new Integer(0);
	public static final Integer MESE_1 = new Integer(1);
	public static final Integer MESE_2 = new Integer(2);
	public static final Integer MESE_3 = new Integer(3);
	public static final Integer MESE_4 = new Integer(4);
	public static final Integer MESE_5 = new Integer(5);
	public static final Integer MESE_6 = new Integer(6);
	public static final Integer MESE_7 = new Integer(7);
	public static final Integer MESE_8 = new Integer(8);
	public static final Integer MESE_9 = new Integer(9);
	public static final Integer MESE_10= new Integer(10);
	public static final Integer MESE_11= new Integer(11);
	public static final Integer MESE_12= new Integer(12);
	
	public final static java.util.Dictionary meseKeys;
	static 
	{
		meseKeys = new it.cnr.jada.util.OrderedHashtable();				
			
		meseKeys.put(MESE_0,  "Preventivo");
		meseKeys.put(MESE_1,  "Gennaio");	
		meseKeys.put(MESE_2,  "Febbraio");
		meseKeys.put(MESE_3,  "Marzo");
		meseKeys.put(MESE_4,  "Aprile");
		meseKeys.put(MESE_5,  "Maggio");
		meseKeys.put(MESE_6,  "Giugno");
		meseKeys.put(MESE_7,  "Luglio");
		meseKeys.put(MESE_8,  "Agosto");
		meseKeys.put(MESE_9,  "Settembre");
		meseKeys.put(MESE_10, "Ottobre");
		meseKeys.put(MESE_11, "Novembre");
		meseKeys.put(MESE_12, "Dicembre");
	};
/**
 * Stampa_ripartizione_costiVBulk constructor comment.
 */
public Stampa_ripartizione_costiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.23.28)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * 
 * @return String
 */
public String getCdUOCRForPrint() {

	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * 
 * @return String
 */
public String getCdCommessaCRForPrint() {

	if (getCommessaForPrint()==null)
		return "*";
	if (getCommessaForPrint().getCd_progetto()==null)
		return "*";

	return getCommessaForPrint().getCd_progetto();
}
/**
 * 
 * @return String
 */
public String getCdModuloCRForPrint() {

	if (getModuloForPrint()==null)
		return "*";
	if (getModuloForPrint().getCd_progetto()==null)
		return "*";

	return getModuloForPrint().getCd_progetto();
}
/**
 * 
 * @return String
 */
public String getIdDipendenteCRForPrint() {

	if (getDipendenteForPrint()==null)
		return "*";
	if (getDipendenteForPrint().getId_matricola()==null)
		return "*";

	return getDipendenteForPrint().getId_matricola();
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.23.28)
 * @return java.lang.Integer
 */
//public java.lang.Integer getEsercizio() {
//	return esercizio;
//}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.30.08)
 * @return java.lang.Integer
 */
public java.lang.Integer getMese() {
	return mese;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.23.28)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getMeseKeys() {
	return meseKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 16.24.19)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
public boolean isROCdCommessaForPrint() {
	return getCommessaForPrint()==null || getCommessaForPrint().getCrudStatus()==NORMAL;
}
public boolean isROCdModuloForPrint() {
	return getModuloForPrint()==null || getCommessaForPrint()==null ||getCommessaForPrint().getCd_progetto()==null ||getModuloForPrint().getCrudStatus()==NORMAL;
}
public boolean isROFindModuloForPrint() {
	return getModuloForPrint()==null || getCommessaForPrint()==null ||getCommessaForPrint().getCd_progetto()==null;
}
public boolean isROIdDipendenteForPrint() {
	return getDipendenteForPrint()==null || getDipendenteForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 16.24.19)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.23.28)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.23.28)
 * @param newEsercizio java.lang.Integer
 */
//public void setEsercizio(java.lang.Integer newEsercizio) {
//	esercizio = newEsercizio;
//}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 16.24.19)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 15.30.08)
 * @param newMese java.lang.Integer
 */
public void setMese(java.lang.Integer newMese) {
	mese = newMese;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 16.24.19)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
	super.validate();
	
	// controllo su campo MESE
	if ( getMese() == null )
		throw new it.cnr.jada.bulk.ValidationException( "Attenzione: indicare un Mese." );
//	// controllo su campo ESERCIZIO
//	if ( getEsercizio_base() == null )
//		throw new it.cnr.jada.bulk.ValidationException( "Attenzione: indicare un Esercizio." );
}
	/**
	 * @return
	 */
	public ProgettoBulk getCommessaForPrint() {
		return commessaForPrint;
	}

	/**
	 * @return
	 */
	public ProgettoBulk getModuloForPrint() {
		return moduloForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setCommessaForPrint(ProgettoBulk bulk) {
		commessaForPrint = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setModuloForPrint(ProgettoBulk bulk) {
		moduloForPrint = bulk;
	}

	/**
	 * @return
	 */
	public V_dipendenteBulk getDipendenteForPrint() {
		return dipendenteForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setDipendenteForPrint(V_dipendenteBulk bulk) {
		dipendenteForPrint = bulk;
	}
		
	public String getEsercizioForPrint() {
		  	return this.getEsercizio().toString();
	}
	
}