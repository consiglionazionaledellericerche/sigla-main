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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.*;


/**
 * Insert the type's description here.
 * Creation date: (21/02/2003 9.57.40)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_mandatoBulk extends MandatoBulk {
	
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	
	private boolean isUOForPrintEnabled;

/**
 * Stampa_vpg_mandatoBulk constructor comment.
 */
public Stampa_vpg_mandatoBulk() {
	super();
}
/**
 * Stampa_vpg_mandatoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_mandato java.lang.Long
 */
public Stampa_vpg_mandatoBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
public String getCdTerzoCRParameter() {

	if (getTerzoForPrint()==null)
		return "%";
	if (getTerzoForPrint().getCd_terzo()==null)
		return "%";

	return getTerzoForPrint().getCd_terzo().toString();
}

/**
 * Insert the method's description here.
 * Creation date: (21/02/2003 16.10.07)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2003 16.09.38)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.28.57)
 * @return java.lang.Long
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.28.57)
 * @return java.lang.Long
 */
public java.lang.Long getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 14.11.01)
 * @return java.lang.String
 */
public Integer getTc() {
	return new Integer(0);
}
public TerzoBulk getTerzoForPrint() {
	return terzoForPrint;
}
public boolean isROTerzoForPrint(){
	return terzoForPrint == null || terzoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2003 16.10.07)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2003 16.09.38)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.28.57)
 * @param newPgFine java.lang.Long
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.28.57)
 * @param newPgInizio java.lang.Long
 */
public void setPgInizio(java.lang.Long newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 14.36.31)
 * @param newV_terzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public void setTerzoForPrint(TerzoBulk newTerzo) {
	terzoForPrint = newTerzo;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}

/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoEmittenteForPrint() {
	return super.getUnita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdUOEmittenteForPrint() {
	return getUoEmittenteForPrint()==null || getUoEmittenteForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (08/04/2003 15.30.39)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
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
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public void setUoEmittenteForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUO) {
	super.setUnita_organizzativa(newUO);
}
public String getCdUOCRForPrint() {

    if (super.getUnita_organizzativa() == null)
        return "%";
    if (super.getUnita_organizzativa().getCd_unita_organizzativa() == null)
        return "%";

    return super.getUnita_organizzativa().getCd_unita_organizzativa().toString();

}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOEmittenteForPrint() {
	
	return super.getCd_unita_organizzativa();
}
}
