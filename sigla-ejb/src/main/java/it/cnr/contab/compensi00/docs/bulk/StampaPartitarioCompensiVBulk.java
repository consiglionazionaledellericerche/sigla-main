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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;

/**
 * Insert the type's description here.
 * Creation date: (20/03/2003 15.04.27)
 * @author: Gennaro Borriello
 */
public class StampaPartitarioCompensiVBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA 
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) 
	private java.lang.Integer esercizio;
	
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;

	private AnagraficoBulk anagraficoForPrint;
/**
 * StampaPartitarioCompensiVBulk constructor comment.
 */
public StampaPartitarioCompensiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.40.36)
 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoForPrint() {
	return anagraficoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @return java.lang.String
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}

public boolean isROAnagraficoForPrint(){
	return anagraficoForPrint == null || anagraficoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.40.36)
 * @param newAnagraficoForPrint it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public void setAnagraficoForPrint(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagraficoForPrint) {
	anagraficoForPrint = newAnagraficoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @param newCd_unita_organizzativa java.lang.String
 */
public void setCd_unita_organizzativa(java.lang.String newCd_unita_organizzativa) {
	cd_unita_organizzativa = newCd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2003 15.21.10)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
}
