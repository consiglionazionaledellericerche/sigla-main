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

package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.contab.fondiric00.tabrif.bulk.*;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;


public class Fondo_attivita_vincolataBulk extends Fondo_attivita_vincolataBase {

	private Tipo_fondoBulk tipo;
	private Unita_organizzativaBulk unita_organizzativa;
	private TerzoBulk responsabile;
	private TerzoBulk ente;
	private DivisaBulk divisa;

	private BulkList dettagli = new BulkList();

public Fondo_attivita_vincolataBulk() {
	super();
}
public Fondo_attivita_vincolataBulk(java.lang.String cd_fondo) {
	super(cd_fondo);
}
	public int addToDettagli(Fondo_assegnatarioBulk dett) {
		dett.setCd_fondo( getCd_fondo() );
		dettagli.add(dett);
		return dettagli.size()-1;
	}

	/**
	 * Restituisce la lista dei dettagli collegati alla testata per il salvataggio.
	 *
	 * @return it.cnr.jada.bulk.BulkCollection[]
	 *
	 * @see setBulkLists
	 */

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { dettagli };
	}

public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
public java.lang.Integer getCd_ente_fin() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk ente = this.getEnte();
	if (ente == null)
		return null;
	return ente.getCd_terzo();
}
public java.lang.Integer getCd_responsabile_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
	if (responsabile == null)
		return null;
	return responsabile.getCd_terzo();
}
public java.lang.String getCd_tipo_fondo() {
	Tipo_fondoBulk tipo = this.getTipo();
	if (tipo == null)
		return null;
	return tipo.getCd_tipo_fondo();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getDettagli() {
	return dettagli;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.53)
 * @return it.cnr.contab.config00.sto.bulk.EnteBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getEnte() {
	return ente;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile() {
	return responsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @return it.cnr.contabres.fondiric00.tabrif.bulk.Tipo_fondoBulk
 */
public Tipo_fondoBulk getTipo() {
	return tipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public boolean isRODt_fine() {
	return getDt_fine() != null;
}
	public Fondo_assegnatarioBulk removeFromDettagli(int index) {
		Fondo_assegnatarioBulk dett = (Fondo_assegnatarioBulk)dettagli.remove(index);
		return dett;
	}

public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
public void setCd_ente_fin(java.lang.Integer cd_ente_fin) {
	this.getEnte().setCd_terzo(cd_ente_fin);
}
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.getResponsabile().setCd_terzo(cd_responsabile_terzo);
}
public void setCd_tipo_fondo(java.lang.String cd_tipo_fondo) {
	this.getTipo().setCd_tipo_fondo(cd_tipo_fondo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @param newDettagli it.cnr.jada.bulk.BulkList
 */
public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
	dettagli = newDettagli;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.53)
 * @param newEnte it.cnr.contab.config00.sto.bulk.EnteBulk
 */
public void setEnte(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newEnte) {
	ente = newEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setResponsabile(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile) {
	responsabile = newResponsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @param newTipo it.cnr.contabres.fondiric00.tabrif.bulk.Tipo_fondoBulk
 */
public void setTipo(Tipo_fondoBulk newTipo) {
	tipo = newTipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
