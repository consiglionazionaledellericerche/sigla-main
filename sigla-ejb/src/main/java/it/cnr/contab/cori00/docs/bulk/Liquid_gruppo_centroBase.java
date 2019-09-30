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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_centroBase extends Liquid_gruppo_centroKey implements Keyed {
	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// CD_CDS_OBB_ACCENTR VARCHAR(30)
	private java.lang.String cd_cds_obb_accentr;

	// ESERCIZIO_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_obb_accentr;

	// ESERCIZIO_ORI_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obb_accentr;

	// PG_OBB_ACCENTR DECIMAL(10,0)
	private java.lang.Long pg_obb_accentr;

	// DA_ESERCIZIO_PRECEDENTE CHAR(1) NOT NULL
	private java.lang.String da_esercizio_precedente;

public Liquid_gruppo_centroBase() {
	super();
}
	/**
	 * @return Returns the cd_cds_obb_accentr.
	 */
	public java.lang.String getCd_cds_obb_accentr() {
		return cd_cds_obb_accentr;
	}
	/**
	 * @param cd_cds_obb_accentr The cd_cds_obb_accentr to set.
	 */
	public void setCd_cds_obb_accentr(java.lang.String cd_cds_obb_accentr) {
		this.cd_cds_obb_accentr = cd_cds_obb_accentr;
	}
	/**
	 * @return Returns the da_esercizio_precedente.
	 */
	public java.lang.String getDa_esercizio_precedente() {
		return da_esercizio_precedente;
	}
	/**
	 * @param da_esercizio_precedente The da_esercizio_precedente to set.
	 */
	public void setDa_esercizio_precedente(
			java.lang.String da_esercizio_precedente) {
		this.da_esercizio_precedente = da_esercizio_precedente;
	}
	/**
	 * @return Returns the esercizio_obb_accentr.
	 */
	public java.lang.Integer getEsercizio_obb_accentr() {
		return esercizio_obb_accentr;
	}
	/**
	 * @param esercizio_obb_accentr The esercizio_obb_accentr to set.
	 */
	public void setEsercizio_obb_accentr(java.lang.Integer esercizio_obb_accentr) {
		this.esercizio_obb_accentr = esercizio_obb_accentr;
	}
	/**
	 * @return Returns the esercizio_ori_obb_accentr.
	 */
	public java.lang.Integer getEsercizio_ori_obb_accentr() {
		return esercizio_ori_obb_accentr;
	}
	/**
	 * @param esercizio_ori_obb_accentr The esercizio_ori_obb_accentr to set.
	 */
	public void setEsercizio_ori_obb_accentr(java.lang.Integer esercizio_ori_obb_accentr) {
		this.esercizio_ori_obb_accentr = esercizio_ori_obb_accentr;
	}
	/**
	 * @return Returns the pg_obb_accentr.
	 */
	public java.lang.Long getPg_obb_accentr() {
		return pg_obb_accentr;
	}
	/**
	 * @param pg_obb_accentr The pg_obb_accentr to set.
	 */
	public void setPg_obb_accentr(java.lang.Long pg_obb_accentr) {
		this.pg_obb_accentr = pg_obb_accentr;
	}
	/**
	 * @return Returns the stato.
	 */
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * @param stato The stato to set.
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
}
