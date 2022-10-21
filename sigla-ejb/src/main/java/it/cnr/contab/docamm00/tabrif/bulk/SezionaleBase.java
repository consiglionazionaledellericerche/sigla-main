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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SezionaleBase extends SezionaleKey implements KeyedPersistent {
	// CORRENTE DECIMAL(10,0) NOT NULL
	protected java.lang.Long corrente;

	// PRIMO DECIMAL(10,0) NOT NULL
	protected java.lang.Long primo;

	// ULTIMO DECIMAL(10,0) NOT NULL
	protected java.lang.Long ultimo;

	private java.lang.String cd_voce_ep_iva;

	private java.lang.String cd_voce_ep_iva_split;

	public SezionaleBase() {
		super();
	}
	public SezionaleBase(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.String ti_fattura) {
		super(cd_cds,cd_tipo_sezionale,cd_unita_organizzativa,esercizio,ti_fattura);
	}
	/*
	 * Getter dell'attributo corrente
	 */
	public java.lang.Long getCorrente() {
		return corrente;
	}
	/*
	 * Getter dell'attributo primo
	 */
	public java.lang.Long getPrimo() {
		return primo;
	}
	/*
	 * Getter dell'attributo ultimo
	 */
	public java.lang.Long getUltimo() {
		return ultimo;
	}
	/*
	 * Setter dell'attributo corrente
	 */
	public void setCorrente(java.lang.Long corrente) {
		this.corrente = corrente;
	}
	/*
	 * Setter dell'attributo primo
	 */
	public void setPrimo(java.lang.Long primo) {
		this.primo = primo;
	}
	/*
	 * Setter dell'attributo ultimo
	 */
	public void setUltimo(java.lang.Long ultimo) {
		this.ultimo = ultimo;
	}

	public String getCd_voce_ep_iva() {
		return cd_voce_ep_iva;
	}

	public void setCd_voce_ep_iva(String cd_voce_ep_iva) {
		this.cd_voce_ep_iva = cd_voce_ep_iva;
	}

	public String getCd_voce_ep_iva_split() {
		return cd_voce_ep_iva_split;
	}

	public void setCd_voce_ep_iva_split(String cd_voce_ep_iva_split) {
		this.cd_voce_ep_iva_split = cd_voce_ep_iva_split;
	}
}
