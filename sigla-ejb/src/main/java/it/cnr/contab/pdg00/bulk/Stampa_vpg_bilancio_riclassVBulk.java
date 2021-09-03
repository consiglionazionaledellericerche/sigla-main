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

package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.util.enumeration.TipoIVA;

/**
 * Insert the type's description here.
 * Creation date: (27/08/2004 16.29.19)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_bilancio_riclassVBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	// Esercizio di scrivania
	private Integer esercizio;

	// Il Cds di scrivania
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	private boolean cdsForPrintEnabled;
	
	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();
	private boolean uoForPrintEnabled;

	// ISTITUZIONALE_COMMERCIALE
	private String ti_ist_com;
	
	private Boolean dettaglioConti;
	
	public final static String TIPO_IST_COM = "*";

	public final static java.util.Dictionary ti_ist_comKeys;
	
	static {		
		ti_ist_comKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_ist_comKeys.put(TIPO_IST_COM,"Tutto");
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			ti_ist_comKeys.put(tipoIVA.value(), tipoIVA.label());
		}
	};

/**
 * Stampa_vpg_bilancio_riclassVBulk constructor comment.
 */
public Stampa_vpg_bilancio_riclassVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.lang.String
 */
public String getCdUOCRForPrint() {

	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (30/08/2004 10.58.01)
 * @return java.lang.Integer
 */
public Integer getTc() {
	return new Integer(0);
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @return java.lang.String
 */
public java.lang.String getTi_ist_com() {
	return ti_ist_com;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.31.30)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTi_ist_comKeys() {
	return ti_ist_comKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */
public boolean isROCdsForPrint() {
	return getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @return boolean
 */
public boolean isUoForPrintEnabled() {
	return uoForPrintEnabled;
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
 * @param newTi_ist_com java.lang.String
 */
public void setTi_ist_com(java.lang.String newTi_ist_com) {
	ti_ist_com = newTi_ist_com;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.30.55)
 * @param newUoForPrintEnabled boolean
 */
public void setUoForPrintEnabled(boolean newUoForPrintEnabled) {
	uoForPrintEnabled = newUoForPrintEnabled;
}
	/**
	 * @return
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
		cdsForPrint = bulk;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.lang.String
	 */
	public String getCdCDSCRForPrint() {

		if (getCdsForPrint()==null)
			return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getCdsForPrint().getCd_unita_organizzativa().toString();
	}
	/**
	 * @param b
	 */
	public void setCdsForPrintEnabled(boolean b) {
		cdsForPrintEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isCdsForPrintEnabled() {
		return cdsForPrintEnabled;
	}
	public Boolean getDettaglioConti() {
		return dettaglioConti;
	}
	public String getDettaglioContiYN() {
		return dettaglioConti.booleanValue()?"Y":"N";
	}
	public void setDettaglioConti(Boolean dettaglioConti) {
		this.dettaglioConti = dettaglioConti;
	}

}
