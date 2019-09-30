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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
/**
 * Insert the type's description here.
 * Creation date: (26/03/2003 10.24.57)
 * @author: Gennaro Borriello
 */
public class Stampa_avviso_pag_mandBulk extends MandatoBulk {

    private java.lang.Long pgInizioMand;
    private java.lang.Long pgFineMand;

    private java.lang.Long pgInizioDist;
    private java.lang.Long pgFineDist;

    private boolean findUOForPrintEnabled;
/**
 * Stampa_avviso_pag_mandBulk constructor comment.
 */
public Stampa_avviso_pag_mandBulk() {
	super();
}
/**
 * Stampa_avviso_pag_mandBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_mandato java.lang.Long
 */
public Stampa_avviso_pag_mandBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {
	
	if (getUoEmittenteForPrint()==null)
		return "*";
	if (getUoEmittenteForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoEmittenteForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @return java.lang.Long
 */
public java.lang.Long getPgFineDist() {
	return pgFineDist;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @return java.lang.Long
 */
public java.lang.Long getPgFineMand() {
	return pgFineMand;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @return java.lang.Long
 */
public java.lang.Long getPgInizioDist() {
	return pgInizioDist;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @return java.lang.Long
 */
public java.lang.Long getPgInizioMand() {
	return pgInizioMand;
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
public boolean isROCdUOForPrint() {
	
	return getUoEmittenteForPrint()==null || getUoEmittenteForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (26/03/2003 17.28.29)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return findUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (26/03/2003 17.28.29)
 * @param newFindUOForPrintEnabled boolean
 */
public void setFindUOForPrintEnabled(boolean newFindUOForPrintEnabled) {
	findUOForPrintEnabled = newFindUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @param newPgFineDist java.lang.Long
 */
public void setPgFineDist(java.lang.Long newPgFineDist) {
	pgFineDist = newPgFineDist;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @param newPgFineMand java.lang.Long
 */
public void setPgFineMand(java.lang.Long newPgFineMand) {
	pgFineMand = newPgFineMand;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @param newPgInizioDist java.lang.Long
 */
public void setPgInizioDist(java.lang.Long newPgInizioDist) {
	pgInizioDist = newPgInizioDist;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.01.09)
 * @param newPgInizioMand java.lang.Long
 */
public void setPgInizioMand(java.lang.Long newPgInizioMand) {
	pgInizioMand = newPgInizioMand;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public void setUoEmittenteForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUO) {
	super.setUnita_organizzativa(newUO);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
