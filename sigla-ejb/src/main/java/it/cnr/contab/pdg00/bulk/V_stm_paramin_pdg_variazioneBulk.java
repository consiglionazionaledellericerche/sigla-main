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

package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_pdg_variazioneBulk extends OggettoBulk  implements Persistent {

	// ID_REPORT DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal id_report;
	
	// CHIAVE VARCHAR(100) NOT NULL
	private java.lang.String chiave;

	// SEQUENZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal sequenza;

	// TIPO CHAR(1) NOT NULL
	private java.lang.String tipo;

	// TI_COMPETENZA_RESIDUO VARCHAR(200)
	private java.lang.String ti_competenza_residuo;

	// PDG_VARIAZIONE NUMBER
	private java.lang.Long pg_variazione;

public V_stm_paramin_pdg_variazioneBulk() {
	super();
	setTipo("A");
}

public java.lang.String getChiave() {
	return chiave;
}

public void setChiave(java.lang.String chiave) {
	this.chiave = chiave;
}

public java.math.BigDecimal getId_report() {
	return id_report;
}

public void setId_report(java.math.BigDecimal id_report) {
	this.id_report = id_report;
}

public java.lang.Long getPg_variazione() {
	return pg_variazione;
}

public void setPg_variazione(java.lang.Long pg_variazione) {
	this.pg_variazione = pg_variazione;
}

public java.math.BigDecimal getSequenza() {
	return sequenza;
}

public void setSequenza(java.math.BigDecimal sequenza) {
	this.sequenza = sequenza;
}

public java.lang.String getTi_competenza_residuo() {
	return ti_competenza_residuo;
}

public void setTi_competenza_residuo(java.lang.String ti_competenza_residuo) {
	this.ti_competenza_residuo = ti_competenza_residuo;
}

public java.lang.String getTipo() {
	return tipo;
}

public void setTipo(java.lang.String tipo) {
	this.tipo = tipo;
}
}
