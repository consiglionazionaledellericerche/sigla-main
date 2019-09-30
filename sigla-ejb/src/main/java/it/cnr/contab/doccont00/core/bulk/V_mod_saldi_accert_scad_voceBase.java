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
 * Created on Mar 22, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_mod_saldi_accert_scad_voceBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_VOCE VARCHAR(50)
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(22,0)
	private java.lang.Integer esercizio;

	// IM_DELTA_rev_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_rev_voce;

	// IM_DELTA_inc_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_inc_voce;

	// IM_DELTA_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_voce;

	//	ESERCIZIO_ORIGINALE DECIMAL(10,0)
    private java.lang.Integer esercizio_originale;

	// PG_accertamento DECIMAL(22,0)
	private java.lang.Long pg_accertamento;

	// PG_OLD DECIMAL(22,0)
	private java.lang.Long pg_old;

	// TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;

	// PG_accertamento_SCADENZARIO DECIMAL(22,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;

	// CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

public V_mod_saldi_accert_scad_voceBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_voce
 */
public java.lang.String getCd_voce() {
	return cd_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo im_delta_rev_voce
 */
public java.math.BigDecimal getIm_delta_rev_voce() {
	return im_delta_rev_voce;
}
/* 
 * Getter dell'attributo im_delta_inc_voce
 */
public java.math.BigDecimal getIm_delta_inc_voce() {
	return im_delta_inc_voce;
}
/* 
 * Getter dell'attributo im_delta_voce
 */
public java.math.BigDecimal getIm_delta_voce() {
	return im_delta_voce;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_originale() {
	return esercizio_originale;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_old
 */
public java.lang.Long getPg_old() {
	return pg_old;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_voce
 */
public void setCd_voce(java.lang.String cd_voce) {
	this.cd_voce = cd_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/* 
 * Setter dell'attributo im_delta_rev_voce
 */
public void setIm_delta_rev_voce(java.math.BigDecimal im_delta_rev_voce) {
	this.im_delta_rev_voce = im_delta_rev_voce;
}
/* 
 * Setter dell'attributo im_delta_inc_voce
 */
public void setIm_delta_inc_voce(java.math.BigDecimal im_delta_inc_voce) {
	this.im_delta_inc_voce = im_delta_inc_voce;
}
/* 
 * Setter dell'attributo im_delta_voce
 */
public void setIm_delta_voce(java.math.BigDecimal im_delta_voce) {
	this.im_delta_voce = im_delta_voce;
}
/**
 * @param integer
 */
public void setEsercizio_originale(java.lang.Integer integer) {
	esercizio_originale = integer;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long newPg_accertamento) {
	pg_accertamento = newPg_accertamento;
}
/* 
 * Setter dell'attributo pg_old
 */
public void setPg_old(java.lang.Long newPg_old) {
	pg_old = newPg_old;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
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
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	/**
	 * @return
	 */
	public java.lang.Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
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
	public void setCd_linea_attivita(java.lang.String string) {
		cd_linea_attivita = string;
	}

	/**
	 * @param long1
	 */
	public void setPg_accertamento_scadenzario(java.lang.Long long1) {
		pg_accertamento_scadenzario = long1;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_tipo_documento_cont() {
		return cd_tipo_documento_cont;
	}

	/**
	 * @param string
	 */
	public void setCd_tipo_documento_cont(java.lang.String string) {
		cd_tipo_documento_cont = string;
	}

}
