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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.persistency.*;

public class Tipo_carico_scaricoBase extends Tipo_carico_scaricoKey implements Keyed {
	// DS_TIPO_CARICO_SCARICO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_carico_scarico;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// FL_AUMENTO_VALORE CHAR(1) NOT NULL
	private java.lang.Boolean fl_aumento_valore;

	// FL_BUONO_PER_TRASFERIMENTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_buono_per_trasferimento;

	// FL_ELABORA_BUONO_COGE CHAR(1) NOT NULL
	private java.lang.Boolean fl_elabora_buono_coge;

	// FL_FATTURABILE CHAR(1) NOT NULL
	private java.lang.Boolean fl_fatturabile;

	// TI_DOCUMENTO CHAR(1) NOT NULL
	private java.lang.String ti_documento;
	// fl_storno_fondo CHAR(1) NOT NULL
	private java.lang.Boolean fl_storno_fondo;
	// fl_chiude_fondo CHAR(1) NOT NULL
	private java.lang.Boolean fl_chiude_fondo;
//	 fl_vendita CHAR(1) NOT NULL
	private java.lang.Boolean fl_vendita;
	private java.lang.Boolean fl_da_ordini;

public Tipo_carico_scaricoBase() {
	super();
}
public Tipo_carico_scaricoBase(java.lang.String cd_tipo_carico_scarico) {
	super(cd_tipo_carico_scarico);
}

	public Boolean getFl_da_ordini() {
		return fl_da_ordini;
	}

	public void setFl_da_ordini(Boolean fl_da_ordini) {
		this.fl_da_ordini = fl_da_ordini;
	}

	/*
 * Getter dell'attributo ds_tipo_carico_scarico
 */
public java.lang.String getDs_tipo_carico_scarico() {
	return ds_tipo_carico_scarico;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo fl_aumento_valore
 */
public java.lang.Boolean getFl_aumento_valore() {
	return fl_aumento_valore;
}
/* 
 * Getter dell'attributo fl_buono_per_trasferimento
 */
public java.lang.Boolean getFl_buono_per_trasferimento() {
	return fl_buono_per_trasferimento;
}
/* 
 * Getter dell'attributo fl_elabora_buono_coge
 */
public java.lang.Boolean getFl_elabora_buono_coge() {
	return fl_elabora_buono_coge;
}
/* 
 * Getter dell'attributo fl_fatturabile
 */
public java.lang.Boolean getFl_fatturabile() {
	return fl_fatturabile;
}
/* 
 * Getter dell'attributo ti_documento
 */
public java.lang.String getTi_documento() {
	return ti_documento;
}
/* 
 * Setter dell'attributo ds_tipo_carico_scarico
 */
public void setDs_tipo_carico_scarico(java.lang.String ds_tipo_carico_scarico) {
	this.ds_tipo_carico_scarico = ds_tipo_carico_scarico;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo fl_aumento_valore
 */
public void setFl_aumento_valore(java.lang.Boolean fl_aumento_valore) {
	this.fl_aumento_valore = fl_aumento_valore;
}
/* 
 * Setter dell'attributo fl_buono_per_trasferimento
 */
public void setFl_buono_per_trasferimento(java.lang.Boolean fl_buono_per_trasferimento) {
	this.fl_buono_per_trasferimento = fl_buono_per_trasferimento;
}
/* 
 * Setter dell'attributo fl_elabora_buono_coge
 */
public void setFl_elabora_buono_coge(java.lang.Boolean fl_elabora_buono_coge) {
	this.fl_elabora_buono_coge = fl_elabora_buono_coge;
}
/* 
 * Setter dell'attributo fl_fatturabile
 */
public void setFl_fatturabile(java.lang.Boolean fl_fatturabile) {
	this.fl_fatturabile = fl_fatturabile;
}
/* 
 * Setter dell'attributo ti_documento
 */
public void setTi_documento(java.lang.String ti_documento) {
	this.ti_documento = ti_documento;
}
public java.lang.Boolean getFl_storno_fondo() {
	return fl_storno_fondo;
}
public void setFl_storno_fondo(java.lang.Boolean fl_storno_fondo) {
	this.fl_storno_fondo = fl_storno_fondo;
}
public java.lang.Boolean getFl_chiude_fondo() {
	return fl_chiude_fondo;
}
public void setFl_chiude_fondo(java.lang.Boolean fl_chiude_fondo) {
	this.fl_chiude_fondo = fl_chiude_fondo;
}
public java.lang.Boolean getFl_vendita() {
	return fl_vendita;
}
public void setFl_vendita(java.lang.Boolean fl_vendita) {
	this.fl_vendita = fl_vendita;
}
}
