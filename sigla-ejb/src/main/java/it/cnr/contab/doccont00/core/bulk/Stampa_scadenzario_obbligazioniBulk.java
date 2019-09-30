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
 * Creation date: (23/01/2003 17.03.48)
 * @author: Roberto Fantino
 */
public class Stampa_scadenzario_obbligazioniBulk extends Obbligazione_scadenzarioBulk {
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private boolean cdsUOInScrivania = false;
/**
 * Stampa_scadenzario_obbligazioniBulk constructor comment.
 */
public Stampa_scadenzario_obbligazioniBulk() {
	super();
}
/**
 * Stampa_scadenzario_obbligazioniBulk constructor comment.
 * @param obbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 */
public Stampa_scadenzario_obbligazioniBulk(ObbligazioneBulk obbligazione) {
	super(obbligazione);
}
/**
 * Stampa_scadenzario_obbligazioniBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_obbligazione java.lang.Long
 * @param pg_obbligazione_scadenzario java.lang.Long
 */
public Stampa_scadenzario_obbligazioniBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione, Long pg_obbligazione_scadenzario) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione, pg_obbligazione_scadenzario);
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
public String getCdUoNullableForPrint() {

	String cd = getCdUoForPrint();
	return (cd == null) ? "*" : cd;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.27.20)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.27.20)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.48.58)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @return boolean
 */
public boolean isCdsUOInScrivania() {
	return cdsUOInScrivania;
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
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrintSearchTool() {
	return !isCdsUOInScrivania();
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @param newCdsUOInScrivania boolean
 */
public void setCdsUOInScrivania(boolean newCdsUOInScrivania) {
	cdsUOInScrivania = newCdsUOInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.27.20)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.27.20)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 17.48.58)
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
}
}
