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

package it.cnr.contab.config00.pdcfin.bulk;

import java.math.BigDecimal;

import it.cnr.jada.persistency.*;

public class Elemento_voceBase extends Elemento_voceKey implements Keyed {
	// CD_CAPOCONTO_FIN VARCHAR(10) 
	private java.lang.String cd_capoconto_fin;

	// ESERCIZIO_ELEMENTO_PADRE DECIMAL(4,0) 
	private java.lang.Integer esercizio_elemento_padre;

	// TI_GESTIONE_ELEMENTO_PADRE CHAR(1)
	private java.lang.String ti_gestione_elemento_padre;

	// TI_APPARTENENZA_ELEMENTO_PADRE CHAR(1)
	private java.lang.String ti_appartenenza_elemento_padre;

	// CD_ELEMENTO_PADRE VARCHAR(20)
	private java.lang.String cd_elemento_padre;

	// CD_PARTE VARCHAR(20)
	private java.lang.String cd_parte;

	// CD_PROPRIO_ELEMENTO VARCHAR(20)
	private java.lang.String cd_proprio_elemento;

	// DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;

	// FL_LIMITE_ASS_OBBLIG CHAR(1)
	private java.lang.Boolean fl_limite_ass_obblig;

	// FL_PARTITA_GIRO CHAR(1)
	private java.lang.Boolean fl_partita_giro;

	// FL_MISSIONI CHAR(1)
	private java.lang.Boolean fl_missioni;

	private java.lang.Boolean flComunicaPagamenti;

	// FL_VOCE_NON_SOGG_IMP_AUT CHAR(1) NOT NULL
	private java.lang.Boolean fl_voce_non_sogg_imp_aut;

	// FL_VOCE_PERSONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_voce_personale;

	// FL_VOCE_SAC CHAR(1) NOT NULL
	private java.lang.Boolean fl_voce_sac;

	// TI_ELEMENTO_VOCE CHAR(1) NOT NULL
	private java.lang.String ti_elemento_voce;

	// ESERCIZIO_CLA_E DECIMAL(4,0)
	private java.lang.Integer esercizio_cla_e;	

	// COD_CLA_E VARCHAR(20)
	private java.lang.String cod_cla_e;	
	
	// ESERCIZIO_CLA_S DECIMAL(4,0)
	private java.lang.Integer esercizio_cla_s;	

	// COD_CLA_S VARCHAR(20)
	private java.lang.String cod_cla_s;	
	
	// FL_RECON CHAR(1) NOT NULL
	private java.lang.Boolean fl_recon;

	// FL_INV_BENI_PATR BOOLEAN NOT NULL
	private java.lang.Boolean fl_inv_beni_patr;
	
	// ID_CLASSIFICAZIONE NUMBER NOT NULL
	private java.lang.Integer id_classificazione;

	// FL_VOCE_FONDO CHAR(1) NOT NULL
	private java.lang.Boolean fl_voce_fondo;

	// FL_CHECK_TERZO_SIOPE CHAR(1) NOT NULL
	private java.lang.Boolean fl_check_terzo_siope;

	private java.lang.Boolean fl_inv_beni_comp;
	
	private java.lang.Boolean fl_limite_spesa;
	
	private java.lang.Boolean fl_prelievo;
	private java.lang.Boolean fl_soggetto_prelievo;
	private java.lang.Boolean fl_solo_residuo;
	private java.lang.Boolean fl_solo_competenza;
	private java.lang.Boolean fl_azzera_residui;
	
	private BigDecimal perc_prelievo_pdgp_entrate;
	
	// FL_TROVATO CHAR(1) NOT NULL
	private String fl_trovato;

	// CD_UNITA_PIANO VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_piano;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_piano;

	// GG_DEROGA_OBBL_COMP_PRG_SCAD INTEGER
	private java.lang.Integer gg_deroga_obbl_comp_prg_scad;
	
	// GG_DEROGA_OBBL_RES_PRG_SCAD INTEGER
	private java.lang.Integer gg_deroga_obbl_res_prg_scad;

	private java.lang.Boolean fl_limite_competenza;

	private java.lang.String blocco_impegni_natfin;

	public Elemento_voceBase() {
	super();
}
public Elemento_voceBase(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo cd_capoconto_fin
 */
public java.lang.String getCd_capoconto_fin() {
	return cd_capoconto_fin;
}
/* 
 * Getter dell'attributo esercizio_elemento_padre
 */
public java.lang.Integer getEsercizio_elemento_padre() {
	return esercizio_elemento_padre;
}
/* 
 * Getter dell'attributo ti_gestione_elemento_padre
 */
public java.lang.String getTi_gestione_elemento_padre() {
	return ti_gestione_elemento_padre;
}
/* 
 * Getter dell'attributo ti_appartenenza_elemento_padre
 */
public java.lang.String getTi_appartenenza_elemento_padre() {
	return ti_appartenenza_elemento_padre;
}
/* 
 * Getter dell'attributo cd_elemento_padre
 */
public java.lang.String getCd_elemento_padre() {
	return cd_elemento_padre;
}
/* 
 * Getter dell'attributo cd_parte
 */
public java.lang.String getCd_parte() {
	return cd_parte;
}
/* 
 * Getter dell'attributo cd_proprio_elemento
 */
public java.lang.String getCd_proprio_elemento() {
	return cd_proprio_elemento;
}
/* 
 * Getter dell'attributo ds_elemento_voce
 */
public java.lang.String getDs_elemento_voce() {
	return ds_elemento_voce;
}
/* 
 * Getter dell'attributo fl_limite_ass_obblig
 */
public java.lang.Boolean getFl_limite_ass_obblig() {
	return fl_limite_ass_obblig;
}
/* 
 * Getter dell'attributo fl_partita_giro
 */
public java.lang.Boolean getFl_partita_giro() {
	return fl_partita_giro;
}
/* 
 * Getter dell'attributo fl_voce_non_sogg_imp_aut
 */
public java.lang.Boolean getFl_voce_non_sogg_imp_aut() {
	return fl_voce_non_sogg_imp_aut;
}
/* 
 * Getter dell'attributo fl_voce_personale
 */
public java.lang.Boolean getFl_voce_personale() {
	return fl_voce_personale;
}
/* 
 * Getter dell'attributo fl_voce_sac
 */
public java.lang.Boolean getFl_voce_sac() {
	return fl_voce_sac;
}
/* 
 * Getter dell'attributo ti_elemento_voce
 */
public java.lang.String getTi_elemento_voce() {
	return ti_elemento_voce;
}
/* 
 * Setter dell'attributo cd_capoconto_fin
 */
public void setCd_capoconto_fin(java.lang.String cd_capoconto_fin) {
	this.cd_capoconto_fin = cd_capoconto_fin;
}
/* 
 * Setter dell'attributo esercizio_elemento_padre
 */
public void setEsercizio_elemento_padre(java.lang.Integer esercizio_elemento_padre) {
	this.esercizio_elemento_padre = esercizio_elemento_padre;
}
/* 
 * Setter dell'attributo ti_gestione_elemento_padre
 */
public void setTi_gestione_elemento_padre(java.lang.String ti_gestione_elemento_padre) {
	this.ti_gestione_elemento_padre = ti_gestione_elemento_padre;
}
/* 
 * Setter dell'attributo ti_appartenenza_elemento_padre
 */
public void setTi_appartenenza_elemento_padre(java.lang.String ti_appartenenza_elemento_padre) {
	this.ti_appartenenza_elemento_padre = ti_appartenenza_elemento_padre;
}
/* 
 * Setter dell'attributo cd_elemento_padre
 */
public void setCd_elemento_padre(java.lang.String cd_elemento_padre) {
	this.cd_elemento_padre = cd_elemento_padre;
}
/* 
 * Setter dell'attributo cd_parte
 */
public void setCd_parte(java.lang.String cd_parte) {
	this.cd_parte = cd_parte;
}
/* 
 * Setter dell'attributo cd_proprio_elemento
 */
public void setCd_proprio_elemento(java.lang.String cd_proprio_elemento) {
	this.cd_proprio_elemento = cd_proprio_elemento;
}
/* 
 * Setter dell'attributo ds_elemento_voce
 */
public void setDs_elemento_voce(java.lang.String ds_elemento_voce) {
	this.ds_elemento_voce = ds_elemento_voce;
}
/* 
 * Setter dell'attributo fl_limite_ass_obblig
 */
public void setFl_limite_ass_obblig(java.lang.Boolean fl_limite_ass_obblig) {
	this.fl_limite_ass_obblig = fl_limite_ass_obblig;
}
/* 
 * Setter dell'attributo fl_partita_giro
 */
public void setFl_partita_giro(java.lang.Boolean fl_partita_giro) {
	this.fl_partita_giro = fl_partita_giro;
}
/* 
 * Setter dell'attributo fl_voce_non_sogg_imp_aut
 */
public void setFl_voce_non_sogg_imp_aut(java.lang.Boolean fl_voce_non_sogg_imp_aut) {
	this.fl_voce_non_sogg_imp_aut = fl_voce_non_sogg_imp_aut;
}
/* 
 * Setter dell'attributo fl_voce_personale
 */
public void setFl_voce_personale(java.lang.Boolean fl_voce_personale) {
	this.fl_voce_personale = fl_voce_personale;
}
/* 
 * Setter dell'attributo fl_voce_sac
 */
public void setFl_voce_sac(java.lang.Boolean fl_voce_sac) {
	this.fl_voce_sac = fl_voce_sac;
}
/* 
 * Setter dell'attributo ti_elemento_voce
 */
public void setTi_elemento_voce(java.lang.String ti_elemento_voce) {
	this.ti_elemento_voce = ti_elemento_voce;
}
	/**
	 * @return
	 */
	public java.lang.String getCod_cla_e() {
		return cod_cla_e;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_cla_e() {
		return esercizio_cla_e;
	}

	/**
	 * @param v_cod_cla_e
	 */
	public void setCod_cla_e(java.lang.String v_cod_cla_e) {
		this.cod_cla_e = v_cod_cla_e;
	}

	/**
	 * @param v_esercizio_cla_e
	 */
	public void setEsercizio_cla_e(java.lang.Integer v_esercizio_cla_e) {
		this.esercizio_cla_e = v_esercizio_cla_e;
	}

	/**
	 * @return
	 */
	public java.lang.String getCod_cla_s() {
		return cod_cla_s;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_cla_s() {
		return esercizio_cla_s;
	}

	/**
	 * @param newCod_cla_s
	 */
	public void setCod_cla_s(java.lang.String newCod_cla_s) {
		this.cod_cla_s = newCod_cla_s;
	}

	/**
	 * @param newEsercizio_cla_s
	 */
	public void setEsercizio_cla_s(java.lang.Integer newEsercizio_cla_s) {
		this.esercizio_cla_s = newEsercizio_cla_s;
	}

	/**
	 * @return
	 */
	public java.lang.Boolean getFl_recon() {
		return fl_recon;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_recon(java.lang.Boolean boolean1) {
		fl_recon = boolean1;
	}

	public java.lang.Boolean getFl_inv_beni_patr() {
		return fl_inv_beni_patr;
	}

	public void setFl_inv_beni_patr(java.lang.Boolean boolean1) {
		fl_inv_beni_patr = boolean1;
	}
	/**
	 * @return
	 */
	public java.lang.Integer getId_classificazione() {
		return id_classificazione;
	}

	/**
	 * @param integer
	 */
	public void setId_classificazione(java.lang.Integer integer) {
		id_classificazione = integer;
	}

	public Boolean getFl_voce_fondo() {
		return fl_voce_fondo;
	}
	
	public void setFl_voce_fondo(Boolean fl_voce_fondo) {
		this.fl_voce_fondo = fl_voce_fondo;
	}
	
	public java.lang.Boolean getFl_check_terzo_siope() {
		return fl_check_terzo_siope;
	}
	
	public void setFl_check_terzo_siope(java.lang.Boolean fl_check_terzo_siope) {
		this.fl_check_terzo_siope = fl_check_terzo_siope;
	}
	public java.lang.Boolean getFl_inv_beni_comp() {
		return fl_inv_beni_comp;
	}
	public void setFl_inv_beni_comp(java.lang.Boolean fl_inv_beni_comp) {
		this.fl_inv_beni_comp = fl_inv_beni_comp;
	}
	public java.lang.Boolean getFl_limite_spesa() {
		return fl_limite_spesa;
	}
	public void setFl_limite_spesa(java.lang.Boolean fl_limite_spesa) {
		this.fl_limite_spesa = fl_limite_spesa;
	}
	public java.lang.Boolean getFl_prelievo() {
		return fl_prelievo;
	}
	public void setFl_prelievo(java.lang.Boolean fl_prelievo) {
		this.fl_prelievo = fl_prelievo;
	}
	public java.lang.Boolean getFl_soggetto_prelievo() {
		return fl_soggetto_prelievo;
	}
	public void setFl_soggetto_prelievo(java.lang.Boolean fl_soggetto_prelievo) {
		this.fl_soggetto_prelievo = fl_soggetto_prelievo;
	}
	public BigDecimal getPerc_prelievo_pdgp_entrate() {
		return perc_prelievo_pdgp_entrate;
	} 
	public void setPerc_prelievo_pdgp_entrate(BigDecimal perc_prelievo_pdgp_entrate) {
		this.perc_prelievo_pdgp_entrate = perc_prelievo_pdgp_entrate;
	}
	public java.lang.Boolean getFl_solo_residuo() {
		return fl_solo_residuo;
	}
	public void setFl_solo_residuo(java.lang.Boolean fl_solo_residuo) {
		this.fl_solo_residuo = fl_solo_residuo;
	}
	public java.lang.Boolean getFl_solo_competenza() {
		return fl_solo_competenza;
	}
	public void setFl_solo_competenza(java.lang.Boolean fl_solo_competenza) {
		this.fl_solo_competenza = fl_solo_competenza;
	}
	public void setFl_trovato(String fl_trovato) {
		this.fl_trovato = fl_trovato;
	}
	public String getFl_trovato() {
		return fl_trovato;
	}
	public java.lang.Boolean getFl_azzera_residui() {
		return fl_azzera_residui;
	}
	public void setFl_azzera_residui(java.lang.Boolean fl_azzera_residui) {
		this.fl_azzera_residui = fl_azzera_residui;
	}
	public java.lang.Boolean getFl_missioni() {
		return fl_missioni;
	}
	public void setFl_missioni(java.lang.Boolean fl_missioni) {
		this.fl_missioni = fl_missioni;
	}

	public java.lang.String getCd_unita_piano() {
		return cd_unita_piano;
	}
	
	public void setCd_unita_piano(java.lang.String cd_unita_piano) {
		this.cd_unita_piano = cd_unita_piano;
	}
	
	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}
	
	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}
	
	public java.lang.Integer getGg_deroga_obbl_comp_prg_scad() {
		return gg_deroga_obbl_comp_prg_scad;
	}
	
	public void setGg_deroga_obbl_comp_prg_scad(java.lang.Integer gg_deroga_obbl_comp_prg_scad) {
		this.gg_deroga_obbl_comp_prg_scad = gg_deroga_obbl_comp_prg_scad;
	}
	
	public java.lang.Integer getGg_deroga_obbl_res_prg_scad() {
		return gg_deroga_obbl_res_prg_scad;
	}
	
	public void setGg_deroga_obbl_res_prg_scad(java.lang.Integer gg_deroga_obbl_res_prg_scad) {
		this.gg_deroga_obbl_res_prg_scad = gg_deroga_obbl_res_prg_scad;
	}
	public Boolean getFlComunicaPagamenti() {
		return flComunicaPagamenti;
	}

	public void setFlComunicaPagamenti(Boolean flComunicaPagamenti) {
		this.flComunicaPagamenti = flComunicaPagamenti;
	}

	public Boolean getFl_limite_competenza() {
		return fl_limite_competenza;
	}

	public void setFl_limite_competenza(Boolean fl_limite_competenza) {
		this.fl_limite_competenza = fl_limite_competenza;
	}

	public String getBlocco_impegni_natfin() {
		return blocco_impegni_natfin;
	}

	public void setBlocco_impegni_natfin(String blocco_impegni_natfin) {
		this.blocco_impegni_natfin = blocco_impegni_natfin;
	}
}
