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

package it.cnr.contab.fondecon00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (06/03/2003 15.49.12)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_fondo_economaleBulk extends Fondo_economaleBulk {

	private Fondo_economaleBulk fondoForPrint;
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
/**
 * Stampa_vpg_fondo_economaleBulk constructor comment.
 */
public Stampa_vpg_fondo_economaleBulk() {
	super();
}
/**
 * Stampa_vpg_fondo_economaleBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_codice_fondo java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 */
public Stampa_vpg_fondo_economaleBulk(String cd_cds, String cd_codice_fondo, String cd_unita_organizzativa, Integer esercizio) {
	super(cd_cds, cd_codice_fondo, cd_unita_organizzativa, esercizio);
}
public String getCdTerzoCRParameter() {

	return getUser();
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2003 15.51.34)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2003 15.51.34)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (07/03/2003 12.33.36)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public Fondo_economaleBulk getFondoForPrint() {
	return fondoForPrint;
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
 * Creation date: (07/03/2003 12.33.36)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public boolean isROFondoSearchTool() {
	
	return fondoForPrint == null || fondoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2003 15.51.34)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2003 15.51.34)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (07/03/2003 12.33.36)
 * @param newFondoForPrint it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setFondoForPrint(Fondo_economaleBulk newFondoForPrint) {
	fondoForPrint = newFondoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2003 15.51.34)
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
