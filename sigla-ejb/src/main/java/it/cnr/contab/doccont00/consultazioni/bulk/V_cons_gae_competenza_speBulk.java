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
 * Date 12/10/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_gae_competenza_speBulk extends OggettoBulk  implements Persistent {
//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pg_progetto;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    PG_COMMESSA DECIMAL(10,0)
	private java.lang.Long pg_commessa;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String ds_commessa;
 
//    PG_MODULO DECIMAL(10,0)
	private java.lang.Long pg_modulo;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
	
//  CD_CDS VARCHAR(15)
	private java.lang.String cd_cds;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
//    TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    IM_STANZ_INIZIALE_A1 DECIMAL(22,0)
	private java.math.BigDecimal im_stanz_iniziale_a1;
 
//    PG_VARIAZIONE_PDG DECIMAL(22,0)
	private java.lang.Long pg_variazione_pdg;
 
//    DS_VARIAZIONE VARCHAR(300)
	private java.lang.String ds_variazione;
 
//    VARIAZIONI_PIU DECIMAL(22,0)
	private java.math.BigDecimal variazioni_piu;
 
//    VARIAZIONI_MENO DECIMAL(22,0)
	private java.math.BigDecimal variazioni_meno;
 
//    CD_CDS_OBB VARCHAR(30)
	private java.lang.String cd_cds_obb;
 
//    pg_obbligazioni DECIMAL(22,0)
	private java.lang.Long pg_obbligazione;
 
//    pg_obbligazioni_scadenzario DECIMAL(22,0)
	private java.lang.Long pg_obbligazione_scadenzario;
 
//    DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;
 
//    impegni_comp DECIMAL(22,0)
	private java.math.BigDecimal impegni_comp;
 
//    cd_cds_man VARCHAR(30)
	private java.lang.String cd_cds_man;
 
//    pg_mandato DECIMAL(22,0)
	private java.lang.Long pg_mandato;
 
//    ds_mandato VARCHAR(300)
	private java.lang.String ds_mandato;
 
//    mandati_comp DECIMAL(22,0)
	private java.math.BigDecimal mandati_comp;
	
	private java.math.BigDecimal da_pagare;
	private java.math.BigDecimal disponibile;
	private java.math.BigDecimal assestato;
	private java.lang.String ds_natura;
	public V_cons_gae_competenza_speBulk() {
		super();
	}
	
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(
			java.lang.String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}
	public java.lang.String getCd_commessa() {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa) {
		this.cd_commessa = cd_commessa;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento) {
		this.cd_dipartimento = cd_dipartimento;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}
	public java.lang.String getCd_modulo() {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo) {
		this.cd_modulo = cd_modulo;
	}
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}
	public java.lang.String getDs_commessa() {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa) {
		this.ds_commessa = ds_commessa;
	}
	public java.lang.String getDs_elemento_voce() {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce) {
		this.ds_elemento_voce = ds_elemento_voce;
	}
	public java.lang.String getDs_linea_attivita() {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita) {
		this.ds_linea_attivita = ds_linea_attivita;
	}
	public java.lang.String getDs_modulo() {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo) {
		this.ds_modulo = ds_modulo;
	}
	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto) {
		this.ds_progetto = ds_progetto;
	}
	public java.lang.String getDs_scadenza() {
		return ds_scadenza;
	}
	public void setDs_scadenza(java.lang.String ds_scadenza) {
		this.ds_scadenza = ds_scadenza;
	}
	public java.lang.String getDs_variazione() {
		return ds_variazione;
	}
	public void setDs_variazione(java.lang.String ds_variazione) {
		this.ds_variazione = ds_variazione;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.math.BigDecimal getIm_stanz_iniziale_a1() {
		return im_stanz_iniziale_a1;
	}
	public void setIm_stanz_iniziale_a1(java.math.BigDecimal im_stanz_iniziale_a1) {
		this.im_stanz_iniziale_a1 = im_stanz_iniziale_a1;
	}
	public java.lang.Long getPg_commessa() {
		return pg_commessa;
	}
	public void setPg_commessa(java.lang.Long pg_commessa) {
		this.pg_commessa = pg_commessa;
	}
	public java.lang.Long getPg_modulo() {
		return pg_modulo;
	}
	public void setPg_modulo(java.lang.Long pg_modulo) {
		this.pg_modulo = pg_modulo;
	}
	public java.lang.Long getPg_progetto() {
		return pg_progetto;
	}
	public void setPg_progetto(java.lang.Long pg_progetto) {
		this.pg_progetto = pg_progetto;
	}
	public java.lang.Long getPg_variazione_pdg() {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg) {
		this.pg_variazione_pdg = pg_variazione_pdg;
	}
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	public java.math.BigDecimal getVariazioni_meno() {
		return variazioni_meno;
	}
	public void setVariazioni_meno(java.math.BigDecimal variazioni_meno) {
		this.variazioni_meno = variazioni_meno;
	}
	public java.math.BigDecimal getVariazioni_piu() {
		return variazioni_piu;
	}
	public void setVariazioni_piu(java.math.BigDecimal variazioni_piu) {
		this.variazioni_piu = variazioni_piu;
	}
	public java.math.BigDecimal getAssestato() {
		return assestato;
	}
	public void setAssestato(java.math.BigDecimal assestato) {
		this.assestato = assestato;
	}
	public java.math.BigDecimal getDisponibile() {
		return disponibile;
	}
	public void setDisponibile(java.math.BigDecimal disponibile) {
		this.disponibile = disponibile;
	}
	public java.lang.String getCd_cds_man() {
		return cd_cds_man;
	}
	public void setCd_cds_man(java.lang.String cd_cds_man) {
		this.cd_cds_man = cd_cds_man;
	}
	public java.lang.String getCd_cds_obb() {
		return cd_cds_obb;
	}
	public void setCd_cds_obb(java.lang.String cd_cds_obb) {
		this.cd_cds_obb = cd_cds_obb;
	}
	public java.math.BigDecimal getDa_pagare() {
		return da_pagare;
	}
	public void setDa_pagare(java.math.BigDecimal da_pagare) {
		this.da_pagare = da_pagare;
	}
	public java.lang.String getDs_mandato() {
		return ds_mandato;
	}
	public void setDs_mandato(java.lang.String ds_mandato) {
		this.ds_mandato = ds_mandato;
	}
	public java.math.BigDecimal getImpegni_comp() {
		return impegni_comp;
	}
	public void setImpegni_comp(java.math.BigDecimal impegni_comp) {
		this.impegni_comp = impegni_comp;
	}
	public java.math.BigDecimal getMandati_comp() {
		return mandati_comp;
	}
	public void setMandati_comp(java.math.BigDecimal mandati_comp) {
		this.mandati_comp = mandati_comp;
	}
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato) {
		this.pg_mandato = pg_mandato;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.pg_obbligazione = pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(
			java.lang.Long pg_obbligazione_scadenzario) {
		this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
	}

	public java.lang.String getDs_natura() {
		return ds_natura;
	}

	public void setDs_natura(java.lang.String ds_natura) {
		this.ds_natura = ds_natura;
	}

	public java.lang.String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
	

	
}