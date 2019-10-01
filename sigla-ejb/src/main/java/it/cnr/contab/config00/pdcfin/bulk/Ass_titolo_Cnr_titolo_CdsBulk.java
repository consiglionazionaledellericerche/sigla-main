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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Questa classe eredita le caratteristiche della classe <code>Ass_ev_evBulk</code>,
 * che contiene le variabili e i metodi comuni a tutte le sue sottoclassi.
 * In particolare, si tratta di un'associazione tra titoli del Cnr e titoli
 * del Cds.
 */
public class Ass_titolo_Cnr_titolo_CdsBulk extends Ass_ev_evBulk {

public Ass_titolo_Cnr_titolo_CdsBulk() {
	super();

	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	// elemento_voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	// elemento_voce.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	
	setTi_appartenenza_coll(Elemento_voceHome.APPARTENENZA_CDS);
	setTi_gestione_coll(Elemento_voceHome.GESTIONE_SPESE);
	// elemento_voce_coll.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
	// elemento_voce_coll.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);	
	
	setCd_natura("*");
	setCd_cds("*");
}
public Ass_titolo_Cnr_titolo_CdsBulk(java.lang.String cd_cds,java.lang.String cd_elemento_voce,java.lang.String cd_elemento_voce_coll,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_appartenenza_coll,java.lang.String ti_gestione,java.lang.String ti_gestione_coll) {
	super(cd_cds,cd_elemento_voce,cd_elemento_voce_coll,cd_natura,esercizio,ti_appartenenza,ti_appartenenza_coll,ti_gestione,ti_gestione_coll);
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public java.lang.String getCd_elemento_voce_coll() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_coll = this.getElemento_voce_coll();
	if (elemento_voce_coll == null)
		return null;
	return elemento_voce_coll.getCd_elemento_voce();
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
public java.lang.String getTi_appartenenza_coll() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_coll = this.getElemento_voce_coll();
	if (elemento_voce_coll == null)
		return null;
	return elemento_voce_coll.getTi_appartenenza();
}
public java.lang.String getTi_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_elemento_voce();
}
public java.lang.String getTi_elemento_voce_coll() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_coll = this.getElemento_voce_coll();
	if (elemento_voce_coll == null)
		return null;
	return elemento_voce_coll.getTi_elemento_voce();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
public java.lang.String getTi_gestione_coll() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_coll = this.getElemento_voce_coll();
	if (elemento_voce_coll == null)
		return null;
	return elemento_voce_coll.getTi_gestione();
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_elemento_voce_coll(java.lang.String cd_elemento_voce_coll) {
	this.getElemento_voce_coll().setCd_elemento_voce(cd_elemento_voce_coll);
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_appartenenza_coll(java.lang.String ti_appartenenza_coll) {
	this.getElemento_voce_coll().setTi_appartenenza(ti_appartenenza_coll);
}
public void setTi_elemento_voce(java.lang.String ti_elemento_voce) {
	this.getElemento_voce().setTi_elemento_voce(ti_elemento_voce);
}
public void setTi_elemento_voce_coll(java.lang.String ti_elemento_voce_coll) {
	this.getElemento_voce_coll().setTi_elemento_voce(ti_elemento_voce_coll);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
public void setTi_gestione_coll(java.lang.String ti_gestione_coll) {
	this.getElemento_voce_coll().setTi_gestione(ti_gestione_coll);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException 
{
	super.validate();
	
	if ( elemento_voce == null || elemento_voce.getCd_elemento_voce() == null || elemento_voce.getCd_elemento_voce().equals("") )
		throw new ValidationException( "Il codice del TITOLO SPESA CNR è obbligatorio." );
	if ( elemento_voce_coll == null || elemento_voce_coll.getCd_elemento_voce() == null || elemento_voce_coll.getCd_elemento_voce().equals(""))
		throw new ValidationException( "Il codice del TITOLO SPESA CDS è obbligatorio." );
}
}
