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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_epBase extends Voce_epKey implements Keyed {
	// CD_PROPRIO_VOCE_EP VARCHAR(5) NOT NULL
	private java.lang.String cd_proprio_voce_ep;

	// CD_VOCE_EP_PADRE VARCHAR(45)
	private java.lang.String cd_voce_ep_padre;

	// CONTO_SPECIALE VARCHAR(5)
	private java.lang.String conto_speciale;

	// DS_VOCE_EP VARCHAR(100)
	private java.lang.String ds_voce_ep;

	// FL_A_PAREGGIO CHAR(1)
	private java.lang.Boolean fl_a_pareggio;

	// FL_MASTRINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_mastrino;

	// LIVELLO DECIMAL(1,0) NOT NULL
	private java.lang.Integer livello;

	// NATURA_VOCE VARCHAR(5)
	private java.lang.String natura_voce;

	// RIAPRE_A_CONTO_ECONOMICO VARCHAR(45)
	private java.lang.String riapre_a_conto_economico;

	// RIEPILOGA_A VARCHAR(5)
	private java.lang.String riepiloga_a;

	// TI_SEZIONE CHAR(1)
	private java.lang.String ti_sezione;

	// TI_VOCE_EP CHAR(1)
	private java.lang.String ti_voce_ep;

	private java.lang.Integer id_classificazione;

	private java.lang.String cd_voce_ep_contr;

	public Voce_epBase() {
	super();
}
	public Voce_epBase(java.lang.String cd_voce_ep,java.lang.Integer esercizio) {
		super(cd_voce_ep,esercizio);
	}
	/*
	 * Getter dell'attributo cd_proprio_voce_ep
	 */
	public java.lang.String getCd_proprio_voce_ep() {
		return cd_proprio_voce_ep;
	}
	/*
	 * Getter dell'attributo cd_voce_ep_padre
	 */
	public java.lang.String getCd_voce_ep_padre() {
		return cd_voce_ep_padre;
	}
	/*
	 * Getter dell'attributo conto_speciale
	 */
	public java.lang.String getConto_speciale() {
		return conto_speciale;
	}
	/*
	 * Getter dell'attributo ds_voce_ep
	 */
	public java.lang.String getDs_voce_ep() {
		return ds_voce_ep;
	}
	/*
	 * Getter dell'attributo fl_a_pareggio
	 */
	public java.lang.Boolean getFl_a_pareggio() {
		return fl_a_pareggio;
	}
	/*
	 * Getter dell'attributo fl_mastrino
	 */
	public java.lang.Boolean getFl_mastrino() {
		return fl_mastrino;
	}
	/*
	 * Getter dell'attributo livello
	 */
	public java.lang.Integer getLivello() {
		return livello;
	}
	/*
	 * Getter dell'attributo natura_voce
	 */
	public java.lang.String getNatura_voce() {
		return natura_voce;
	}
	/*
	 * Getter dell'attributo riapre_a_conto_economico
	 */
	public java.lang.String getRiapre_a_conto_economico() {
		return riapre_a_conto_economico;
	}
	/*
	 * Getter dell'attributo riepiloga_a
	 */
	public java.lang.String getRiepiloga_a() {
		return riepiloga_a;
	}
	/*
	 * Getter dell'attributo ti_sezione
	 */
	public java.lang.String getTi_sezione() {
		return ti_sezione;
	}
	/*
	 * Getter dell'attributo ti_voce_ep
	 */
	public java.lang.String getTi_voce_ep() {
		return ti_voce_ep;
	}
	/*
	 * Setter dell'attributo cd_proprio_voce_ep
	 */
	public void setCd_proprio_voce_ep(java.lang.String cd_proprio_voce_ep) {
		this.cd_proprio_voce_ep = cd_proprio_voce_ep;
	}
	/*
	 * Setter dell'attributo cd_voce_ep_padre
	 */
	public void setCd_voce_ep_padre(java.lang.String cd_voce_ep_padre) {
		this.cd_voce_ep_padre = cd_voce_ep_padre;
	}
	/*
	 * Setter dell'attributo conto_speciale
	 */
	public void setConto_speciale(java.lang.String conto_speciale) {
		this.conto_speciale = conto_speciale;
	}
	/*
	 * Setter dell'attributo ds_voce_ep
	 */
	public void setDs_voce_ep(java.lang.String ds_voce_ep) {
		this.ds_voce_ep = ds_voce_ep;
	}
	/*
	 * Setter dell'attributo fl_a_pareggio
	 */
	public void setFl_a_pareggio(java.lang.Boolean fl_a_pareggio) {
		this.fl_a_pareggio = fl_a_pareggio;
	}
	/*
	 * Setter dell'attributo fl_mastrino
	 */
	public void setFl_mastrino(java.lang.Boolean fl_mastrino) {
		this.fl_mastrino = fl_mastrino;
	}
	/*
	 * Setter dell'attributo livello
	 */
	public void setLivello(java.lang.Integer livello) {
		this.livello = livello;
	}
	/*
	 * Setter dell'attributo natura_voce
	 */
	public void setNatura_voce(java.lang.String natura_voce) {
		this.natura_voce = natura_voce;
	}
	/*
	 * Setter dell'attributo riapre_a_conto_economico
	 */
	public void setRiapre_a_conto_economico(java.lang.String riapre_a_conto_economico) {
		this.riapre_a_conto_economico = riapre_a_conto_economico;
	}
	/*
	 * Setter dell'attributo riepiloga_a
	 */
	public void setRiepiloga_a(java.lang.String riepiloga_a) {
		this.riepiloga_a = riepiloga_a;
	}
	/*
	 * Setter dell'attributo ti_sezione
	 */
	public void setTi_sezione(java.lang.String ti_sezione) {
		this.ti_sezione = ti_sezione;
	}
	/*
	 * Setter dell'attributo ti_voce_ep
	 */
	public void setTi_voce_ep(java.lang.String ti_voce_ep) {
		this.ti_voce_ep = ti_voce_ep;
	}
	public java.lang.Integer getId_classificazione() {
		return id_classificazione;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione) {
		this.id_classificazione = id_classificazione;
	}

	public java.lang.String getCd_voce_ep_contr() {
		return cd_voce_ep_contr;
	}
	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep_contr) {
		this.cd_voce_ep_contr = cd_voce_ep_contr;
	}

}
