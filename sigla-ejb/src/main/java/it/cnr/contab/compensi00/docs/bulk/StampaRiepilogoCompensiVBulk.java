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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
/**
 * Insert the type's description here.
 * Creation date: (14/11/2003 13.58.33)
 * @author: CNRADM
 */
public class StampaRiepilogoCompensiVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// CD_CDS
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA 
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO
	private java.lang.Integer esercizio;

	// TERZO
	//private V_anagrafico_terzoBulk terzoForPrint;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;

	private boolean uoEnte;
/**
 * StampaRiepilogoCompensiVBulk constructor comment.
 */
public StampaRiepilogoCompensiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {

	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return java.lang.String
 */
public java.lang.String getCd_cdsForPrint() {

	if (isUoEnte())
		return "%";
		
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return java.lang.String
 */
public java.lang.String getCd_unita_organizzativa() {

    return cd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return java.lang.String
 */
public java.lang.String getCd_UoForPrint() {

	if (isUoEnte())
		return "%";
		
	return cd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 14.11.01)
 * @return java.lang.String
 */
public Integer getTc() {
	return new Integer(0);
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @return it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzoForPrint() {
	return terzoForPrint;
}

public boolean isROTerzoForPrint(){
	return terzoForPrint == null || terzoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (18/11/2003 9.54.30)
 * @return boolean
 */
public boolean isUoEnte() {
	return uoEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @param newCd_unita_organizzativa java.lang.String
 */
public void setCd_unita_organizzativa(java.lang.String newCd_unita_organizzativa) {
	cd_unita_organizzativa = newCd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2003 14.02.12)
 * @param newTerzoForPrint it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk
 */
public void setTerzoForPrint(TerzoBulk newTerzoForPrint) {
	terzoForPrint = newTerzoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (18/11/2003 9.54.30)
 * @param newUoEnte boolean
 */
public void setUoEnte(boolean newUoEnte) {
	uoEnte = newUoEnte;
}
}
