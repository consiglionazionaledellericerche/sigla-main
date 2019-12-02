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
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Sospeso_det_etrBase extends Sospeso_det_etrKey implements Keyed {
	// IM_ASSOCIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;
	
	private java.lang.String cd_cds_reversale;

public Sospeso_det_etrBase() {
	super();
}
public Sospeso_det_etrBase(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.Long pg_reversale,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super(cd_cds,cd_sospeso,esercizio,pg_reversale,ti_entrata_spesa,ti_sospeso_riscontro);
}
/* 
 * Getter dell'attributo im_associato
 */
public java.math.BigDecimal getIm_associato() {
	return im_associato;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo im_associato
 */
public void setIm_associato(java.math.BigDecimal im_associato) {
	this.im_associato = im_associato;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
public java.lang.String getCd_cds_reversale() {
	return cd_cds_reversale;
}
public void setCd_cds_reversale(java.lang.String cd_cds_reversale) {
	this.cd_cds_reversale = cd_cds_reversale;
}
}
