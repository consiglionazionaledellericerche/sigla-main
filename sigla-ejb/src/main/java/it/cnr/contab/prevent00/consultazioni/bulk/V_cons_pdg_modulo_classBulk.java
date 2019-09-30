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
 * Created on Oct 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.consultazioni.bulk;

import java.math.BigDecimal;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.action.CRUDBP;
/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_cons_pdg_modulo_classBulk extends OggettoBulk implements Persistent {

	private java.lang.Integer esercizio;

	private java.lang.String cd_centro_responsabilita;

	private java.lang.String ds_cdr;

	private java.lang.Integer id_classificazione;

	private java.lang.String cd_classificazione;

	private java.lang.String ds_classificazione;

	private java.lang.String cd_progetto;

	private java.lang.String ds_progetto;

	private java.math.BigDecimal importo_ripartito;

	public V_cons_pdg_modulo_classBulk() {
		super();
	}
	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_classificazione() {
		return ds_classificazione;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getId_classificazione() {
		return id_classificazione;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getImporto_ripartito() {
		return importo_ripartito;
	}

	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String string) {
		cd_centro_responsabilita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	/**
	 * @param string
	 */
	public void setDs_cdr(java.lang.String string) {
		ds_cdr = string;
	}

	/**
	 * @param string
	 */
	public void setDs_classificazione(java.lang.String string) {
		ds_classificazione = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @param integer
	 */
	public void setId_classificazione(java.lang.Integer integer) {
		id_classificazione = integer;
	}

	/**
	 * @param decimal
	 */
	public void setImporto_ripartito(java.math.BigDecimal decimal) {
		importo_ripartito = decimal;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}

	/**
	 * @param integer
	 */
	public void setCd_progetto(java.lang.String string) {
		cd_progetto = string;
	}

	/**
	 * @param string
	 */
	public void setDs_progetto(java.lang.String string) {
		ds_progetto = string;
	}

}