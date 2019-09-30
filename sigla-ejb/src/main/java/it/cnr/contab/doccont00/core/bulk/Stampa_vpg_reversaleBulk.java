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

/**
 * Insert the type's description here.
 * Creation date: (21/02/2003 10.03.41)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_reversaleBulk extends ReversaleBulk {
	
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
/**
 * Stampa_vpg_reversaleBulk constructor comment.
 */
public Stampa_vpg_reversaleBulk() {
	super();
}
/**
 * Stampa_vpg_reversaleBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_reversale java.lang.Long
 */
public Stampa_vpg_reversaleBulk(String cd_cds, Integer esercizio, Long pg_reversale) {
	super(cd_cds, esercizio, pg_reversale);
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
 * Creation date: (26/02/2003 10.24.16)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2003 10.24.16)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.30.21)
 * @return java.lang.Long
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.30.21)
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
/**
 * Insert the method's description here.
 * Creation date: (26/02/2003 10.24.16)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzoForPrint() {
	return terzoForPrint;
}
public boolean isROTerzoForPrint(){
	return terzoForPrint == null || terzoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2003 10.24.16)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2003 10.24.16)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.30.21)
 * @param newPgFine java.lang.Long
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.30.21)
 * @param newPgInizio java.lang.Long
 */
public void setPgInizio(java.lang.Long newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2003 10.24.16)
 * @param newTerzoForPrint it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzoForPrint(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzoForPrint) {
	terzoForPrint = newTerzoForPrint;
}

public String getCd_cds_orig() {

	if (getCd_cds_origine()==null)
		return "%";
	
	return getCd_cds_origine();
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
