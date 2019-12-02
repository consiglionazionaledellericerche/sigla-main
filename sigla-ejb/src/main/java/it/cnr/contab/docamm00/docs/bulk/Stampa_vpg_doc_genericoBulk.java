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

package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (19/03/2003 10.52.15)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_doc_genericoBulk extends Documento_genericoBulk {
	
	private java.lang.Integer pgInizio;
	private java.lang.Integer pgFine;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
/**
 * Stampa_vpg_doc_generici constructor comment.
 */
public Stampa_vpg_doc_genericoBulk() {
	super();
}
/**
 * Stampa_vpg_doc_generici constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_tipo_documento_amm java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_documento_generico java.lang.Long
 */
public Stampa_vpg_doc_genericoBulk(String cd_cds, String cd_tipo_documento_amm, String cd_unita_organizzativa, Integer esercizio, Long pg_documento_generico) {
	super(cd_cds, cd_tipo_documento_amm, cd_unita_organizzativa, esercizio, pg_documento_generico);
}


public String getCdTerzoCRParameter() {

	if (getTerzoForPrint()==null)
		return "%";
	if (getTerzoForPrint().getCd_terzo()==null)
		return "%";

	return getTerzoForPrint().getCd_terzo().toString();
}


public String getCdTipoDocAmmCRParameter() {


	if (getTipo_documento()==null)
		return "%";
	if (getTipo_documento().getCd_tipo_documento_amm()==null)
		return "%";

	return getTipo_documento().getCd_tipo_documento_amm();
}

public Integer getIdTipoDocumentoGenericoParameter() {


	if (getTipoDocumentoGenerico()==null)
		return null;
	if (getTipoDocumentoGenerico().getId()==null)
		return null;

	return getTipoDocumentoGenerico().getId();
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgInizio() {
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
 * Creation date: (19/03/2003 11.20.17)
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
 * Creation date: (19/03/2003 11.20.17)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @param newPgFine java.lang.Integer
 */
public void setPgFine(java.lang.Integer newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @param newPgInizio java.lang.Integer
 */
public void setPgInizio(java.lang.Integer newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2003 11.20.17)
 * @param newTerzoForPrint it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzoForPrint(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzoForPrint) {
	terzoForPrint = newTerzoForPrint;
}
public void validate() throws it.cnr.jada.bulk.ValidationException {	
	
}
}
