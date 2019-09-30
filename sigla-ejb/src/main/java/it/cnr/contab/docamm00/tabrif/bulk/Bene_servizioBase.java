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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Bene_servizioBase extends Bene_servizioKey implements Keyed {
	// CD_CATEGORIA_GRUPPO VARCHAR(10)
	private java.lang.String cd_categoria_gruppo;

	// CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_iva;

	// DS_BENE_SERVIZIO VARCHAR(300) NOT NULL
	private java.lang.String ds_bene_servizio;

	// FL_GESTIONE_INVENTARIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_inventario;

	// FL_GESTIONE_MAGAZZINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_magazzino;

	// TI_BENE_SERVIZIO CHAR(1) NOT NULL
	private java.lang.String ti_bene_servizio;

	// UNITA_MISURA VARCHAR(10) NOT NULL
	private java.lang.String unita_misura;
	
	// TIPO_GESTIONE VARCHAR(3) NOT NULL
	private java.lang.String tipoGestione;
	
	private java.lang.String cdTipoArticolo;
	
	private java.lang.String cdGruppoMerceologico;
	
	private java.lang.String fl_valido;

	private java.lang.Boolean fl_obb_intrastat_acq;
	
	private java.lang.Boolean fl_obb_intrastat_ven;
	
	private java.lang.Boolean fl_autofattura;

	private java.lang.Boolean fl_bollo;

	private java.lang.Boolean flScadenza;
	
	private java.lang.Boolean scontiApplicInvent;

	private java.lang.String tipoServizio;

	private java.lang.String tipoManutenzione;

	private java.lang.String note;

public Bene_servizioBase() {
	super();
}
public Bene_servizioBase(java.lang.String cd_bene_servizio) {
	super(cd_bene_servizio);
}
/* 
 * Getter dell'attributo cd_categoria_gruppo
 */
public java.lang.String getCd_categoria_gruppo() {
	return cd_categoria_gruppo;
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo ds_bene_servizio
 */
public java.lang.String getDs_bene_servizio() {
	return ds_bene_servizio;
}
/* 
 * Getter dell'attributo fl_gestione_inventario
 */
public java.lang.Boolean getFl_gestione_inventario() {
	return fl_gestione_inventario;
}
/* 
 * Getter dell'attributo fl_gestione_magazzino
 */
public java.lang.Boolean getFl_gestione_magazzino() {
	return fl_gestione_magazzino;
}
/* 
 * Getter dell'attributo ti_bene_servizio
 */
public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}
/* 
 * Getter dell'attributo unita_misura
 */
public java.lang.String getUnita_misura() {
	return unita_misura;
}
/* 
 * Setter dell'attributo cd_categoria_gruppo
 */
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo ds_bene_servizio
 */
public void setDs_bene_servizio(java.lang.String ds_bene_servizio) {
	this.ds_bene_servizio = ds_bene_servizio;
}
/* 
 * Setter dell'attributo fl_gestione_inventario
 */
public void setFl_gestione_inventario(java.lang.Boolean fl_gestione_inventario) {
	this.fl_gestione_inventario = fl_gestione_inventario;
}
/* 
 * Setter dell'attributo fl_gestione_magazzino
 */
public void setFl_gestione_magazzino(java.lang.Boolean fl_gestione_magazzino) {
	this.fl_gestione_magazzino = fl_gestione_magazzino;
}
/* 
 * Setter dell'attributo ti_bene_servizio
 */
public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
	this.ti_bene_servizio = ti_bene_servizio;
}
/* 
 * Setter dell'attributo unita_misura
 */
public void setUnita_misura(java.lang.String unita_misura) {
	this.unita_misura = unita_misura;
}
public java.lang.String getFl_valido() {
	return fl_valido;
}
public void setFl_valido(java.lang.String fl_valido) {
	this.fl_valido = fl_valido;
}
public java.lang.Boolean getFl_obb_intrastat_acq() {
	return fl_obb_intrastat_acq;
}
public void setFl_obb_intrastat_acq(java.lang.Boolean fl_obb_intrastat_acq) {
	this.fl_obb_intrastat_acq = fl_obb_intrastat_acq;
}
public java.lang.Boolean getFl_obb_intrastat_ven() {
	return fl_obb_intrastat_ven;
}
public void setFl_obb_intrastat_ven(java.lang.Boolean fl_obb_intrastat_ven) {
	this.fl_obb_intrastat_ven = fl_obb_intrastat_ven;
}
public java.lang.Boolean getFl_autofattura() {
	return fl_autofattura;
}
public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
	this.fl_autofattura = fl_autofattura;
}
public java.lang.Boolean getFl_bollo() {
	return fl_bollo;
}
public void setFl_bollo(java.lang.Boolean fl_bollo) {
	this.fl_bollo = fl_bollo;
}
public java.lang.String getCdTipoArticolo() {
	return cdTipoArticolo;
}
public void setCdTipoArticolo(java.lang.String cdTipoArticolo) {
	this.cdTipoArticolo = cdTipoArticolo;
}
public java.lang.String getCdGruppoMerceologico() {
	return cdGruppoMerceologico;
}
public void setCdGruppoMerceologico(java.lang.String cdGruppoMerceologico) {
	this.cdGruppoMerceologico = cdGruppoMerceologico;
}
public java.lang.String getTipoGestione() {
	return tipoGestione;
}
public void setTipoGestione(java.lang.String tipoGestione) {
	this.tipoGestione = tipoGestione;
}
public java.lang.Boolean getFlScadenza() {
	return flScadenza;
}
public void setFlScadenza(java.lang.Boolean flScadenza) {
	this.flScadenza = flScadenza;
}
public java.lang.Boolean getScontiApplicInvent() {
	return scontiApplicInvent;
}
public void setScontiApplicInvent(java.lang.Boolean scontiApplicInvent) {
	this.scontiApplicInvent = scontiApplicInvent;
}
public java.lang.String getTipoServizio() {
	return tipoServizio;
}
public void setTipoServizio(java.lang.String tipoServizio) {
	this.tipoServizio = tipoServizio;
}
public java.lang.String getTipoManutenzione() {
	return tipoManutenzione;
}
public void setTipoManutenzione(java.lang.String tipoManutenzione) {
	this.tipoManutenzione = tipoManutenzione;
}
public java.lang.String getNote() {
	return note;
}
public void setNote(java.lang.String note) {
	this.note = note;
}
}
