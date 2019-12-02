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

public class InquadramentoKey extends OggettoBulk implements KeyedPersistent {
	// DT_INI_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_ini_validita;

	// PG_RIF_INQUADRAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rif_inquadramento;

	// DT_INI_VALIDITA_RAPPORTO TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_ini_validita_rapporto;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_rapporto;

	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

public InquadramentoKey() {
	super();
}
public InquadramentoKey(java.lang.Integer cd_anag,java.lang.String cd_tipo_rapporto,java.sql.Timestamp dt_ini_validita,java.sql.Timestamp dt_ini_validita_rapporto,java.lang.Long pg_rif_inquadramento) {
	this.cd_anag = cd_anag;
	this.cd_tipo_rapporto = cd_tipo_rapporto;
	this.dt_ini_validita = dt_ini_validita;
	this.dt_ini_validita_rapporto = dt_ini_validita_rapporto;
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof InquadramentoKey)) return false;
	InquadramentoKey k = (InquadramentoKey)o;
	if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
	if(!compareKey(getCd_tipo_rapporto(),k.getCd_tipo_rapporto())) return false;
	if(!compareKey(getDt_ini_validita(),k.getDt_ini_validita())) return false;
	if(!compareKey(getDt_ini_validita_rapporto(),k.getDt_ini_validita_rapporto())) return false;
	if(!compareKey(getPg_rif_inquadramento(),k.getPg_rif_inquadramento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
/* 
 * Getter dell'attributo dt_ini_validita_rapporto
 */
public java.sql.Timestamp getDt_ini_validita_rapporto() {
	return dt_ini_validita_rapporto;
}
/* 
 * Getter dell'attributo pg_rif_inquadramento
 */
public java.lang.Long getPg_rif_inquadramento() {
	return pg_rif_inquadramento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_anag())+
		calculateKeyHashCode(getCd_tipo_rapporto())+
		calculateKeyHashCode(getDt_ini_validita())+
		calculateKeyHashCode(getDt_ini_validita_rapporto())+
		calculateKeyHashCode(getPg_rif_inquadramento());
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
/* 
 * Setter dell'attributo dt_ini_validita_rapporto
 */
public void setDt_ini_validita_rapporto(java.sql.Timestamp dt_ini_validita_rapporto) {
	this.dt_ini_validita_rapporto = dt_ini_validita_rapporto;
}
/* 
 * Setter dell'attributo pg_rif_inquadramento
 */
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
}
