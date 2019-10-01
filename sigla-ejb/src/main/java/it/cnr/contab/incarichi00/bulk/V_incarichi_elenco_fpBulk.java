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
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;

public class V_incarichi_elenco_fpBulk extends V_incarichi_elencoBulk {
	//  CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	//  CD_PROC_AMM VARCHAR(5)
	private java.lang.String cd_proc_amm;

	//  CD_TIPO_INCARICO VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_incarico;
 
	//  DS_TIPO_INCARICO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_incarico;

	//  CD_TIPO_ATTIVITA VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_attivita;
 
	//  DS_TIPO_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_attivita;

	//  TIPO_NATURA VARCHAR(3) NOT NULL
	private java.lang.String tipo_natura;
 
	//  FL_MERAMENTE_OCCASIONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_meramente_occasionale;

	//  FL_PUBBLICA_CONTRATTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pubblica_contratto;

	//  FL_ART51 CHAR(1) NOT NULL
	private java.lang.Boolean fl_art51;

	//  FL_INVIATO_CORTE_CONTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_inviato_corte_conti;

	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}

	public void setCd_terzo(java.lang.Integer cdTerzo) {
		cd_terzo = cdTerzo;
	}

	public java.lang.String getCd_proc_amm() {
		return cd_proc_amm;
	}

	public void setCd_proc_amm(java.lang.String cdProcAmm) {
		cd_proc_amm = cdProcAmm;
	}

	public java.lang.String getCd_tipo_incarico() {
		return cd_tipo_incarico;
	}

	public void setCd_tipo_incarico(java.lang.String cdTipoIncarico) {
		cd_tipo_incarico = cdTipoIncarico;
	}

	public java.lang.String getDs_tipo_incarico() {
		return ds_tipo_incarico;
	}

	public void setDs_tipo_incarico(java.lang.String dsTipoIncarico) {
		ds_tipo_incarico = dsTipoIncarico;
	}

	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}

	public void setCd_tipo_attivita(java.lang.String cdTipoAttivita) {
		cd_tipo_attivita = cdTipoAttivita;
	}

	public java.lang.String getDs_tipo_attivita() {
		return ds_tipo_attivita;
	}

	public void setDs_tipo_attivita(java.lang.String dsTipoAttivita) {
		ds_tipo_attivita = dsTipoAttivita;
	}

	public java.lang.String getTipo_natura() {
		return tipo_natura;
	}

	public void setTipo_natura(java.lang.String tipoNatura) {
		tipo_natura = tipoNatura;
	}

	public java.lang.Boolean getFl_meramente_occasionale() {
		return fl_meramente_occasionale;
	}

	public void setFl_meramente_occasionale(java.lang.Boolean flMeramenteOccasionale) {
		fl_meramente_occasionale = flMeramenteOccasionale;
	}

	public java.lang.Boolean getFl_pubblica_contratto() {
		return fl_pubblica_contratto;
	}

	public void setFl_pubblica_contratto(java.lang.Boolean flPubblicaContratto) {
		fl_pubblica_contratto = flPubblicaContratto;
	}

	public java.lang.Boolean getFl_art51() {
		return fl_art51;
	}

	public void setFl_art51(java.lang.Boolean flArt51) {
		fl_art51 = flArt51;
	}

	public java.lang.Boolean getFl_inviato_corte_conti() {
		return fl_inviato_corte_conti;
	}

	public void setFl_inviato_corte_conti(java.lang.Boolean flInviatoCorteConti) {
		fl_inviato_corte_conti = flInviatoCorteConti;
	}
	
	public java.util.Dictionary getTipo_naturaKeys() {
		return NaturaBulk.tipo_naturaKeys;
	}
}