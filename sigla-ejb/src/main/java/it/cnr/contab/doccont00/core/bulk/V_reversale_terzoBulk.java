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

import it.cnr.jada.bulk.*;
public class V_reversale_terzoBulk extends ReversaleIBulk 
{
	private java.lang.Integer cd_terzo;
	private java.lang.Integer cd_anag;
	private java.lang.String  denominazione_sede;	
	private Boolean fl_associazione_siope_completa;
/**
 * V_reversale_terzoBulk constructor comment.
 */
public V_reversale_terzoBulk() {
	super();
}
/**
 * V_reversale_terzoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_reversale java.lang.Long
 */
public V_reversale_terzoBulk(String cd_cds, Integer esercizio, Long pg_reversale) {
	super(cd_cds, esercizio, pg_reversale);
}
/**
 * @return java.lang.Integer
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/**
 * @return java.lang.Integer
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/**
 * @return java.lang.String
 */
public java.lang.String getDenominazione_sede() {
	return denominazione_sede;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>EDIT</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per la modifica di un OggettoBulk.
 */
public OggettoBulk initializeForEdit(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	return new ReversaleIBulk( getCd_cds(), getEsercizio(), getPg_reversale());
}
/**
 * @param newCd_anag java.lang.Integer
 */
public void setCd_anag(java.lang.Integer newCd_anag) {
	cd_anag = newCd_anag;
}
/**
 * @param newCd_terzo java.lang.Integer
 */
public void setCd_terzo(java.lang.Integer newCd_terzo) {
	cd_terzo = newCd_terzo;
}
/**
 * @param newDenominazione_sede java.lang.String
 */
public void setDenominazione_sede(java.lang.String newDenominazione_sede) {
	denominazione_sede = newDenominazione_sede;
}
public Boolean getFl_associazione_siope_completa() {
	return fl_associazione_siope_completa;
}
public void setFl_associazione_siope_completa(
		Boolean fl_associazione_siope_completa) {
	this.fl_associazione_siope_completa = fl_associazione_siope_completa;
}
}
