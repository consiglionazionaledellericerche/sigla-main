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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
/**
 * Insert the type's description here.
 * Creation date: (21/06/2006)
 * @author: FG
 */
public class StampaCompensiBulk extends it.cnr.jada.bulk.OggettoBulk {

	// CD_CDS
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	private boolean cdsForPrintEnabled;
	// CD_UNITA_ORGANIZZATIVA 
	private Unita_organizzativaBulk uoForPrint;
	private boolean uoForPrintEnabled;
	
	// ESERCIZIO
	private java.lang.Integer esercizio;
	// TERZO
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzoForPrint;

	
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	

public StampaCompensiBulk() {
	super();
}

public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
	return cdsForPrint;
}

public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
	cdsForPrint = bulk;
}

public String getCdCdsForPrint() {

	if (getCdsForPrint()==null)
		return "%";
	if (getCdsForPrint().getCd_unita_organizzativa()==null)
		return "%";

	return getCdsForPrint().getCd_unita_organizzativa().toString();
}


public Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}

public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
	this.uoForPrint = uoForPrint;
}


public String getCdUoForPrint() {
	if (getUoForPrint()==null)
	  return "%";
	if(getUoForPrint().getCd_unita_organizzativa()==null)
	  return "%";	
	return getUoForPrint().getCd_unita_organizzativa();
}

public String getCdTerzoForPrint() {
	if (getTerzoForPrint()==null)
	  return "%";
	if(getTerzoForPrint().getCd_terzo()==null)
	  return "%";	
	return getTerzoForPrint().getCd_terzo().toString();
}

public java.lang.Integer getEsercizio() {
	return esercizio;
}

public Integer getTc() {
	return new Integer(0);
}

public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzoForPrint() {
	return terzoForPrint;
}

public boolean isROTerzoForPrint(){
	return terzoForPrint == null || terzoForPrint.getCrudStatus() == NORMAL;
}


public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}

public void setTerzoForPrint(TerzoBulk newTerzoForPrint) {
	terzoForPrint = newTerzoForPrint;
}


public java.lang.Long getPgFine() {
	return pgFine;
}
public void setPgFine(java.lang.Long pgFine) {
	this.pgFine = pgFine;
}
public java.lang.Long getPgInizio() {
	return pgInizio;
}
public void setPgInizio(java.lang.Long pgInizio) {
	this.pgInizio = pgInizio;
}

public boolean isCdsForPrintEnabled() {
	return cdsForPrintEnabled;
}

public void setCdsForPrintEnabled(boolean b) {
	this.cdsForPrintEnabled = b;
}

public boolean isUoForPrintEnabled() {
	return uoForPrintEnabled;
}

public void setUoForPrintEnabled(boolean b) {
	this.uoForPrintEnabled = b;
}



}
