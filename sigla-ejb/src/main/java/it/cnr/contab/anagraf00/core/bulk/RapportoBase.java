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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RapportoBase extends RapportoKey implements Keyed {
	// CAUSALE_FINE_RAPPORTO VARCHAR(300)
	private java.lang.String causale_fine_rapporto;

	// DT_FIN_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fin_validita;

	// MATRICOLA_DIPENDENTE DECIMAL(8,0)
	private java.lang.Integer matricola_dipendente;

	// CD_ENTE_PREV_STI VARCHAR(2)
	private java.lang.String cd_ente_prev_sti;

	// CD_RAPP_IMPIEGO_STI VARCHAR(2)
	private java.lang.String cd_rapp_impiego_sti;
public RapportoBase() {
	super();
}
public RapportoBase(java.lang.Integer cd_anag,java.lang.String cd_tipo_rapporto,java.sql.Timestamp dt_ini_validita) {
	super(cd_anag,cd_tipo_rapporto,dt_ini_validita);
}
/* 
 * Getter dell'attributo causale_fine_rapporto
 */
public java.lang.String getCausale_fine_rapporto() {
	return causale_fine_rapporto;
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Getter dell'attributo matricola_dipendente
 */
public java.lang.Integer getMatricola_dipendente() {
	return matricola_dipendente;
}
/* 
 * Setter dell'attributo causale_fine_rapporto
 */
public void setCausale_fine_rapporto(java.lang.String causale_fine_rapporto) {
	this.causale_fine_rapporto = causale_fine_rapporto;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
/* 
 * Setter dell'attributo matricola_dipendente
 */
public void setMatricola_dipendente(java.lang.Integer matricola_dipendente) {
	this.matricola_dipendente = matricola_dipendente;
}
	/**
	 * @return
	 */
	public java.lang.String getCd_ente_prev_sti() {
		return cd_ente_prev_sti;
	}

	/**
	 * @param string
	 */
	public void setCd_ente_prev_sti(java.lang.String string) {
		cd_ente_prev_sti = string;
	}
	public java.lang.String getCd_rapp_impiego_sti() {
		return cd_rapp_impiego_sti;
	}
	public void setCd_rapp_impiego_sti(java.lang.String cd_rapp_impiego_sti) {
		this.cd_rapp_impiego_sti = cd_rapp_impiego_sti;
	}

}
