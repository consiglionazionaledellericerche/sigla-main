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

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.enumeration.TipoIVA;


/**
 * Insert the type's description here.
 * Creation date: (17/05/2005 16.02.55)
 * @author: Marco Spasiano
 */
public class Stampa_libro_giornaleBulk extends it.cnr.jada.bulk.OggettoBulk {

	private Integer esercizio;
	private String tipologia;
	private String intestazione;
	private java.sql.Timestamp da_contabilizzazione;
	private java.sql.Timestamp a_contabilizzazione;
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private boolean cdsForPrintEnabled;	
	private boolean uoForPrintEnabled;
	private static final java.util.Dictionary ti_tipologiaKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String TIPO_TUTTO = "T";

	static {
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			ti_tipologiaKeys.put(tipoIVA.value(), tipoIVA.label());
		}
		ti_tipologiaKeys.put(TIPO_TUTTO,"Tutto");
	}

	/**
	 * Stampa_obbligazioniBulk constructor comment.
	 */
	public Stampa_libro_giornaleBulk() {
		super();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_tipologiaKeys() {
		return ti_tipologiaKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.sql.Timestamp
	 */
	public String getCdUoForPrint() {
		if (getUoForPrint()==null)
		  return "*";
		if(getUoForPrint().getCd_unita_organizzativa() == null)
		  return "*";	
		return getUoForPrint().getCd_unita_organizzativa();
	}
	public String getCdCdsForPrint() {
		if (getCdsForPrint()==null)
		  return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
		  return "*";
		return getCdsForPrint().getCd_unita_organizzativa();
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (14/05/2003 9.28.52)
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (23/01/2003 12.00.24)
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
	public boolean isROUoForPrint() {
		return !isUoForPrintEnabled();
	}
	public boolean isROCdsForPrint() {
		return !isCdsForPrintEnabled();
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (19/05/2003 15.45.26)
	 * @param newCdsForPrint java.lang.String
	 */
	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk newCdsForPrint) {
		cdsForPrint = newCdsForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (14/05/2003 9.28.52)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (23/01/2003 12.00.24)
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unitaOrganizzativa) {
		this.uoForPrint = unitaOrganizzativa;
	}
	/**
	 * @return
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}
	
	/**
	 * @return
	 */
	public String getTipologia() {
		return tipologia;
	}
	
	/**
	 * @param string
	 */
	public void setTipologia(String string) {
		tipologia = string;
	}
	
	/**
	 * @return
	 */
	public String getIntestazione() {
		return intestazione;
	}
	
	/**
	 * @param string
	 */
	public void setIntestazione(String string) {
		intestazione = string;
	}

	/**
	 * @return
	 */
	public boolean isCdsForPrintEnabled() {
		return cdsForPrintEnabled;
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
	public boolean isUoForPrintEnabled() {
		return uoForPrintEnabled;
	}

	/**
	 * @param b
	 */
	public void setUoForPrintEnabled(boolean b) {
		uoForPrintEnabled = b;
	}
	public java.sql.Timestamp getA_contabilizzazione() {
		return a_contabilizzazione;
	}
	public void setA_contabilizzazione(java.sql.Timestamp a_contabilizzazione) {
		this.a_contabilizzazione = a_contabilizzazione;
	}
	public java.sql.Timestamp getDa_contabilizzazione() {
		return da_contabilizzazione;
	}
	public void setDa_contabilizzazione(java.sql.Timestamp da_contabilizzazione) {
		this.da_contabilizzazione = da_contabilizzazione;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (26/08/2004 15.39.45)
	 * @return java.sql.Timestamp
	 */
	private java.sql.Timestamp getHighDate() {
			
		return java.sql.Timestamp.valueOf(getEsercizio()+"-12-31 00:00:00.0");
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (26/08/2004 15.39.45)
	 * @return java.sql.Timestamp
	 */
	private java.sql.Timestamp getLowDate() {
		
		return java.sql.Timestamp.valueOf(getEsercizio()+"-01-01 00:00:00.0");
	}
	
	public java.sql.Timestamp getDa_contabilizzazioneForPrint() {
		
		if (da_contabilizzazione == null)
			return getLowDate();
			
		return da_contabilizzazione;
	}
	
	
	public java.sql.Timestamp getA_contabilizzazioneForPrint() {
		
		if (a_contabilizzazione == null) 
			return getHighDate();
			
		return a_contabilizzazione;
	}
}
