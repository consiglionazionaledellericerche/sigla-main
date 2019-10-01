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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_incaricoBase extends Tipo_incaricoKey implements Keyed {
//    DS_TIPO_INCARICO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_incarico;
 
//   PRC_INCREMENTO DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_incremento;

//   PRC_INCREMENTO_VAR DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_incremento_var;

//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
//    CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

//    TIPO_ASSOCIAZIONE VARCHAR(3) NOT NULL
	private java.lang.String tipo_associazione;
	private java.sql.Timestamp dt_fine_validita;
	public Tipo_incaricoBase() {
		super();
	}
	public Tipo_incaricoBase(java.lang.String cd_tipo_incarico) {
		super(cd_tipo_incarico);
	}
	public java.lang.String getDs_tipo_incarico() {
		return ds_tipo_incarico;
	}
	public void setDs_tipo_incarico(java.lang.String ds_tipo_incarico)  {
		this.ds_tipo_incarico=ds_tipo_incarico;
	}
	public java.math.BigDecimal getPrc_incremento() {
		return prc_incremento;
	}
	public void setPrc_incremento(java.math.BigDecimal prc_incremento) {
		this.prc_incremento = prc_incremento;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
	public java.lang.String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}
	public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
		this.cd_tipo_rapporto = cd_tipo_rapporto;
	}
	public java.lang.String getTipo_associazione() {
		return tipo_associazione;
	}
	public void setTipo_associazione(java.lang.String tipo_associazione) {
		this.tipo_associazione = tipo_associazione;
	}
	public java.math.BigDecimal getPrc_incremento_var() {
		return prc_incremento_var;
	}
	public void setPrc_incremento_var(java.math.BigDecimal prc_incremento_var) {
		this.prc_incremento_var = prc_incremento_var;
	}
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
		this.dt_fine_validita = dt_fine_validita;
	}
}