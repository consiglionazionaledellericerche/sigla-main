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

package it.cnr.contab.anagraf00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (19/06/2003 11.37.04)
 * @author: Gennaro Borriello
 */
public class Stampa_previdenziale_dipendentiVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// Il Cds di scrivania
	private String cd_cds;

	// la Uo di scirvania
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo_scrivania;

	// La Uo utilizzata per la stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	// La matricola usata per la stampa
	private V_prev_dipBulk matricolaForPrint;

	// Il terzo usato per la stampa	
	private TerzoBulk terzoForPrint;
	
	private boolean isUOForPrintEnabled;

	// Indica se il search-tool per la ricerca della matricola è abilitato
	private boolean matricolaForPrintEnabled;


	// Indica se il search-tool per la ricerca del Terzo è abilitato
	private boolean terzoForPrintEnabled;

/**
 * Stampa_previdenziale_dipendentiVBulk constructor comment.
 */
public Stampa_previdenziale_dipendentiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
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
public String getCdTerzoCRForPrint() {
	
	if (getTerzoForPrint()==null)
		return "0";
	if (getTerzoForPrint().getCd_terzo()==null)
		return "0";

	return getTerzoForPrint().getCd_terzo().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
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
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getMatricolaCRForPrint() {
	
	if (getMatricolaForPrint()==null)
		return "0";
	if (getMatricolaForPrint().getMatricola()==null)
		return "0";

	return getMatricolaForPrint().getMatricola().toString();
	
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 15.22.39)
 * @return it.cnr.contab.anagraf00.core.bulk.V_prev_dipBulk
 */
public V_prev_dipBulk getMatricolaForPrint() {
	return matricolaForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.58.53)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public TerzoBulk getTerzoForPrint() {
	return terzoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2003 9.38.27)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo_scrivania() {
	return uo_scrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.55.31)
 * @return boolean
 */
public boolean isMatricolaForPrintEnabled() {
	return matricolaForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdTerzoForPrint() {
	return getTerzoForPrint()==null || getTerzoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROMatricolaForPrint() {
	return getMatricolaForPrint()==null || getMatricolaForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.55.31)
 * @return boolean
 */
public boolean isTerzoForPrintEnabled() {
	return terzoForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 15.22.39)
 * @param newMatricolaForPrint it.cnr.contab.anagraf00.core.bulk.V_prev_dipBulk
 */
public void setMatricolaForPrint(V_prev_dipBulk newMatricolaForPrint) {
	matricolaForPrint = newMatricolaForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.55.31)
 * @param newMatricolaForPrintEnabled boolean
 */
public void setMatricolaForPrintEnabled(boolean newMatricolaForPrintEnabled) {
	matricolaForPrintEnabled = newMatricolaForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.58.53)
 * @param newTerzoForPrint it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzoForPrint(TerzoBulk newTerzoForPrint) {
	terzoForPrint = newTerzoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.55.31)
 * @param newTerzoForPrintEnabled boolean
 */
public void setTerzoForPrintEnabled(boolean newTerzoForPrintEnabled) {
	terzoForPrintEnabled = newTerzoForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2003 9.38.27)
 * @param newUo_scrivania it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUo_scrivania(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo_scrivania) {
	uo_scrivania = newUo_scrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (19/06/2003 11.39.06)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
}
