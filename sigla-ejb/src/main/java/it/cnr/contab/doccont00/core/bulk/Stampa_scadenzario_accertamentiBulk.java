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
 * Creation date: (27/01/2003 17.08.02)
 * @author: Roberto Fantino
 */
public class Stampa_scadenzario_accertamentiBulk extends AccertamentoBulk {
	
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsOrigineForPrint;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	
	private String riporto;

	private boolean isUOForPrintEnabled;
	private boolean isCdsForPrintEnabled;

	// Stati
	public final static String TIPO_TUTTI = "*";
	public final static java.util.Dictionary tipoCompetenzaResiduoKeys;

	static 
	{
		tipoCompetenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR, "Competenza");
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR_RES, "Residuo");
		tipoCompetenzaResiduoKeys.put(TIPO_TUTTI, "Tutti");
	};
/**
 * Stampa_scadenzario_accertamentiBulk constructor comment.
 */
public Stampa_scadenzario_accertamentiBulk() {
	super();
}
/**
 * Stampa_scadenzario_accertamentiBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer 
 * @param pg_accertamento java.lang.Long
 */
public Stampa_scadenzario_accertamentiBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
}

/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsCRForPrint() {

	return getCdsEnte().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsOrigCRForPrint() {
	
	if (getCdsOrigineForPrint()==null)
		return "*";
	if (getCdsOrigineForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getCdsOrigineForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (07/04/2003 13.51.43)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsOrigineForPrint() {
	return cdsOrigineForPrint;
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
public String getCdUoForPrint() {
	//if (getUoForPrint()==null)
		//return null;
	//return getUoForPrint().getCd_unita_organizzativa();

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @return java.lang.String
 */
public java.lang.String getRiporto() {
	return riporto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (07/04/2003 13.51.43)
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
	return getCdsOrigineForPrint()==null || getCdsOrigineForPrint().getCrudStatus()==NORMAL;
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
 * Creation date: (07/04/2003 13.51.43)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (07/04/2003 13.51.43)
 * @param newCdsOrigineForPrint it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCdsOrigineForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk newCdsOrigineForPrint) {
	cdsOrigineForPrint = newCdsOrigineForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (07/04/2003 13.51.43)
 * @param newIsCdsForPrintEnabled boolean
 */
public void setIsCdsForPrintEnabled(boolean newIsCdsForPrintEnabled) {
	isCdsForPrintEnabled = newIsCdsForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (07/04/2003 13.51.43)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
 * @param newRiporto java.lang.String
 */
public void setRiporto(java.lang.String newRiporto) {
	riporto = newRiporto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 17.09.22)
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
