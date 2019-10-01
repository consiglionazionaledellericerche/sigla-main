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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
/**
 * Insert the type's description here.
 * Creation date: (12/02/2003 12.05.44)
 * @author: CNRADM
 */
public class Stampa_vpg_missioneBulk extends MissioneBulk {
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;
/**
 * Stampa_vpg_missioneBulk constructor comment.
 */
public Stampa_vpg_missioneBulk() {
	super();
}
/**
 * Stampa_vpg_missioneBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_missione java.lang.Long
 */
public Stampa_vpg_missioneBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_missione) {
	super(cd_cds, cd_unita_organizzativa, esercizio, pg_missione);
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
 * Creation date: (12/02/2003 12.06.45)
 * @return java.lang.Long
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2003 12.06.14)
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
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 9.58.17)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
public boolean isROTerzoForPrint(){
	return terzoForPrint == null || terzoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2003 12.06.45)
 * @param newPgFine java.lang.Long
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2003 12.06.14)
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
 * Insert the method's description here.
 * Creation date: (13/02/2003 9.58.17)
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
