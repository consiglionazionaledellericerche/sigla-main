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

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 * @author: Roberto Fantino
 */
public abstract class Stampa_spese_ldaBulk extends Pdg_preventivo_spe_detBulk {

	private String cd_cds;
	private Integer esercizio;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrUtente;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrForPrint;
	private String cdsForPrint;
	
	//private boolean isUOForPrintEnabled;
		
	private boolean isCdrForPrintEnabled;

	private int livello_Responsabilita;
	
	// Livelli relativi al CdR utente
	public static final int LV_AC   = 0; // Amministazione centrale
	public static final int LV_CDRI = 1; // CDR I 00
	public static final int LV_RUO  = 2; // CDR II responsabile
	public static final int LV_NRUO = 3; // CDR II non responsabile

/**
 * Stampa_obbligazioniBulk constructor comment.
 */
public Stampa_spese_ldaBulk() {
	super();
}
/**
 * Stampa_spese_ldaBulk constructor comment.
 *
 * @param cd_centro_responsabilita
 * @param cd_elemento_voce
 * @param cd_linea_attivita
 * @param esercizio
 * @param pg_spesa
 * @param ti_appartenenza
 * @param ti_gestione
 */
public Stampa_spese_ldaBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_spesa,ti_appartenenza,ti_gestione);
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.28.52)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;	
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdrNullableForPrint() {

	// Se non è stata specificato un  CdR
	if ( (getCdrForPrint()==null) ||
			(getCdrForPrint().getCd_centro_responsabilita()==null) ){


		if (LV_NRUO==getLivello_Responsabilita()){
			// Restituisco il CdR dell'Utente loggato
			return getCdrUtente().getCd_centro_responsabilita().toString();
		} else{
			return "*";
		}
				
	}

	return getCdrForPrint().getCd_centro_responsabilita().toString();

}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.43.40)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrForPrint() {
	return cdrForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 11.38.06)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrUtente() {
	return cdrUtente;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 15.45.26)
 * @return java.lang.String
 */
public java.lang.String getCdsForPrint() {
	//return cdsForPrint;

	if ( (getCdrForPrint()==null) || 
			(getCdrForPrint().getCd_centro_responsabilita()==null)){

		if (getLivello_Responsabilita()==LV_AC){
			return "*";
		}else{
			// Restituisco il CdS relativo al CdR dell'utente loggato
			return getCdrUtente().getUnita_padre().getCd_unita_padre();
		}
	}
	
	return getCdrForPrint().getUnita_padre().getCd_unita_padre();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUoForPrint() {
	if (getUoForPrint()==null)
		return null;
	return getUoForPrint().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUoNullableForPrint() {

	// Se non è stata specificato un  CdR
	if ( (getCdrForPrint()==null) ||
			(getCdrForPrint().getCd_centro_responsabilita()==null) ){


		if (getLivello_Responsabilita()==LV_NRUO || getLivello_Responsabilita() == LV_RUO){
			if (getCdrUtente().getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA))
				return "*";
			else
				return getCdrUtente().getCd_unita_organizzativa().toString();
		} else if (getLivello_Responsabilita()==LV_CDRI){
				if (getCdrUtente().isCdrSAC()){
					return getCdrUtente().getCd_unita_organizzativa().toString();
				} else {
					return "*";
				}
		} else if (getLivello_Responsabilita()==LV_AC){
			return "*";
		}
				
	}

	return getCdrForPrint().getCd_unita_organizzativa().toString();

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
 * Creation date: (19/05/2003 9.57.19)
 * @return int
 */
public int getLivello_Responsabilita() {
	return livello_Responsabilita;
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
 * Creation date: (16/05/2003 10.53.41)
 * @return boolean
 */
public boolean isCdrForPrintEnabled() {
	return isCdrForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdrForPrint() {
	return getCdrForPrint()==null || getCdrForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.28.52)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.43.40)
 * @param newCdrForPrint it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdrForPrint(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrForPrint) {
	cdrForPrint = newCdrForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 11.38.06)
 * @param newCdrUtente it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdrUtente(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrUtente) {
	cdrUtente = newCdrUtente;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 15.45.26)
 * @param newCdsForPrint java.lang.String
 */
public void setCdsForPrint(java.lang.String newCdsForPrint) {
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
 * Creation date: (16/05/2003 10.53.41)
 * @param newIsCdrForPrintEnabled boolean
 */
public void setIsCdrForPrintEnabled(boolean newIsCdrForPrintEnabled) {
	isCdrForPrintEnabled = newIsCdrForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 9.57.19)
 * @param newLivello_Responsabilita int
 */
public void setLivello_Responsabilita(int newLivello_Responsabilita) {
	livello_Responsabilita = newLivello_Responsabilita;
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
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {

	
}
}
