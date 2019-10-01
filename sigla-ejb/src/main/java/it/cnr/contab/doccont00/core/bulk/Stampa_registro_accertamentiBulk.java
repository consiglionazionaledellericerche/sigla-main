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

import it.cnr.contab.config00.sto.bulk.CdsBulk;



/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 * @author: Roberto Fantino
 */
public class Stampa_registro_accertamentiBulk extends AccertamentoBulk {
	
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	private String riporto;
	
	// Stati
	public final static String TIPO_TUTTI = "*";
	public final static java.util.Dictionary tipoCompetenzaResiduoKeys;

	private it.cnr.contab.config00.sto.bulk.CdsBulk cds_origine;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo_cds_origine;

	private boolean isUOForPrintEnabled;
	private boolean isCdsForPrintEnabled;

	static 
	{
		tipoCompetenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR, "Competenza");
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR_RES, "Residuo");
		tipoCompetenzaResiduoKeys.put(TIPO_TUTTI, "Tutti");
	};
	
/**
 * Stampa_obbligazioniBulk constructor comment.
 */
public Stampa_registro_accertamentiBulk() {
	super();
}
/**
 * Stampa_obbligazioniBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer 
 * @param pg_accertamento java.lang.Long
 */
public Stampa_registro_accertamentiBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsCRForPrint() {
	//if (getCds_origine()==null)
		//return "*";
	//if (getCds_origine().getCd_unita_organizzativa()==null)
		//return "*";

	//return getCds_origine().getCd_unita_organizzativa().toString();

	return getCdsEnte().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsOrigCRForPrint() {
	
	if (getCds_origine()==null)
		return "*";
	if (getCds_origine().getCd_unita_organizzativa()==null)
		return "*";

	return getCds_origine().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds_origine() {
	return cds_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {
	
	if (getUo_cds_origine()==null)
		return "*";
	if (getUo_cds_origine().getCd_unita_organizzativa()==null)
		return "*";

	return getUo_cds_origine().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 10.50.25)
 * @return java.lang.Long
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 10.50.25)
 * @return java.lang.Long
 */
public java.lang.Long getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.03.39)
 * @return java.lang.String
 */
public java.lang.String getRiporto() {
	return riporto;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTipoCompetenzaResiduoKeys() {
	return tipoCompetenzaResiduoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo_cds_origine() {
	return uo_cds_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @return boolean
 */
public boolean isCdsForPrintEnabled() {
	return isCdsForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdsForPrint() {
	return getCds_origine()==null || getCds_origine().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrint() {
	return getUo_cds_origine()==null || getUo_cds_origine().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @param newCds_origine it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds_origine(it.cnr.contab.config00.sto.bulk.CdsBulk newCds_origine) {
	cds_origine = newCds_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @param newIsCdsForPrintEnabled boolean
 */
public void setIsCdsForPrintEnabled(boolean newIsCdsForPrintEnabled) {
	isCdsForPrintEnabled = newIsCdsForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 10.50.25)
 * @param newPgFine java.lang.Long
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 10.50.25)
 * @param newPgInizio java.lang.Long
 */
public void setPgInizio(java.lang.Long newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.03.39)
 * @param newRiporto java.lang.String
 */
public void setRiporto(java.lang.String newRiporto) {
	riporto = newRiporto;
}
/**
 * Insert the method's description here.
 * Creation date: (28/03/2003 15.10.43)
 * @param newUo_cds_origine it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUo_cds_origine(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo_cds_origine) {
	uo_cds_origine = newUo_cds_origine;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
