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

/*
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_contratto_ditteBase extends Ass_contratto_ditteKey implements Keyed {
	java.lang.String denominazione;
	java.lang.String denominazione_rti;
	java.lang.String tipologia;
	java.lang.String codice_fiscale;
	java.lang.String id_fiscale;
	java.lang.String ruolo;
	private Boolean fl_offerta_presentata;

	public Ass_contratto_ditteBase() {
		super();
	}
	public Ass_contratto_ditteBase(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.Integer pg_dettaglio) {
		super(esercizio, stato_contratto, pg_contratto, pg_dettaglio);
	}
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(java.lang.String denominazione) {
		this.denominazione = denominazione;
	}
	public java.lang.String getDenominazione_rti() {
		return denominazione_rti;
	}
	public void setDenominazione_rti(java.lang.String denominazione_rti) {
		this.denominazione_rti = denominazione_rti;
	}
	public java.lang.String getTipologia() {
		return tipologia;
	}
	public void setTipologia(java.lang.String tipologia) {
		this.tipologia = tipologia;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}
	public java.lang.String getId_fiscale() {
		return id_fiscale;
	}
	public void setId_fiscale(java.lang.String id_fiscale) {
		this.id_fiscale = id_fiscale;
	}
	public java.lang.String getRuolo() {
		return ruolo;
	}
	public void setRuolo(java.lang.String ruolo) {
		this.ruolo = ruolo;
	}

	public Boolean getFl_offerta_presentata() {
		return fl_offerta_presentata;
	}

	public void setFl_offerta_presentata(Boolean fl_offerta_presentata) {
		this.fl_offerta_presentata = fl_offerta_presentata;
	}
}