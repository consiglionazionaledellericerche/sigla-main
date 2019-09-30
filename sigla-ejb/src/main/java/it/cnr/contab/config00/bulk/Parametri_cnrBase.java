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

package it.cnr.contab.config00.bulk;

import java.math.BigDecimal;

import it.cnr.jada.persistency.Keyed;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrBase extends Parametri_cnrKey implements Keyed {
	// CD_TIPO_RAPPORTO VARCHAR(10) NULL
	private String cd_tipo_rapporto;
    //	IMPORTO_FRANCHIGIA_OCCA NUMBER(15,2) NOT NULL
	private BigDecimal importo_franchigia_occa;
	private Boolean fl_versamenti_cori;
	private Integer versamenti_cori_giorno;
	private String cd_tipo_rapporto_prof;
	private Integer livello_pdg_decis_spe;
	private Integer livello_pdg_decis_etr;
	private Integer livello_pdg_cofog;
	private Integer livello_contratt_pdg_spe;
	private Boolean fl_regolamento_2006;
	private Boolean fl_diaria_miss_italia;
	private Boolean fl_motivazione_su_imp;
	private Boolean fl_approvato_definitivo;
	private BigDecimal importo_max_imp;
	private Boolean fl_deduzione_irpef;
	private Boolean fl_deduzione_family;
	private Boolean fl_detrazioni_altre;
	private Boolean fl_detrazioni_family;
	private Boolean fl_siope;
	private Boolean fl_obb_intrastat;
	private Integer ricerca_prof_int_giorni_pubbl;
	private Integer ricerca_prof_int_giorni_scad;
	private Boolean fl_incarico;
	private String oggettoEmailTerziCongua;
	private String corpoEmailTerziCongua;
	private String oggettoEmail;
	private String corpoEmail;
	private Boolean fl_cup;
	private Boolean fl_siope_cup;
	private Boolean fl_credito_irpef;
	private String clausolaOrdine;
	private java.sql.Timestamp data_stipula_contratti;
	private Boolean fl_nuovo_pdg;
	private Boolean fl_pdg_codlast;
	private Boolean fl_pdg_contrattazione;
	private Boolean fl_pdg_quadra_fonti_esterne;
	private java.sql.Timestamp data_attivazione_new_voce;
	
	private Boolean fl_tesoreria_unica;
	
	private Boolean fl_pubblica_contratto;
	private Integer livello_eco;
	private Integer livello_pat;
	private Boolean fl_nuova_gestione_pg;
	
public Boolean getFl_incarico() {
		return fl_incarico;
	}
	public void setFl_incarico(Boolean fl_incarico) {
		this.fl_incarico = fl_incarico;
	}

	public Parametri_cnrBase() {
		super();
	}
	public Parametri_cnrBase(java.lang.Integer esercizio) {
		super(esercizio);
	}

	public String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}

	public void setCd_tipo_rapporto(String string) {
		cd_tipo_rapporto = string;
	}

	/**
	 * @return
	 */
	public BigDecimal getImporto_franchigia_occa() {
		return importo_franchigia_occa;
	}

	/**
	 * @param decimal
	 */
	public void setImporto_franchigia_occa(BigDecimal decimal) {
		importo_franchigia_occa = decimal;
	}

	/**
	 * @return
	 */
	public Boolean getFl_versamenti_cori() {
		return fl_versamenti_cori;
	}

	/**
	 * @return
	 */
	public Integer getVersamenti_cori_giorno() {
		return versamenti_cori_giorno;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_versamenti_cori(Boolean boolean1) {
		fl_versamenti_cori = boolean1;
	}

	/**
	 * @param integer
	 */
	public void setVersamenti_cori_giorno(Integer integer) {
		versamenti_cori_giorno = integer;
	}

	public String getCd_tipo_rapporto_prof() {
		return cd_tipo_rapporto_prof;
	}

	public void setCd_tipo_rapporto_prof(String string) {
		cd_tipo_rapporto_prof = string;
	}

	public Integer getLivello_pdg_decis_etr() {
		return livello_pdg_decis_etr;
	}

	public Integer getLivello_pdg_decis_spe() {
		return livello_pdg_decis_spe;
	}

	public void setLivello_pdg_decis_etr(Integer integer) {
		livello_pdg_decis_etr = integer;
	}

	public void setLivello_pdg_decis_spe(Integer integer) {
		livello_pdg_decis_spe = integer;
	}
	/**
	 * @return
	 */
	public Integer getLivello_contratt_pdg_spe() {
		return livello_contratt_pdg_spe;
	}

	/**
	 * @param integer
	 */
	public void setLivello_contratt_pdg_spe(Integer integer) {
		livello_contratt_pdg_spe = integer;
	}

	/**
	 * @return
	 */
	public Boolean getFl_regolamento_2006() {
		return fl_regolamento_2006;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_regolamento_2006(Boolean boolean1) {
		fl_regolamento_2006 = boolean1;
	}

	/**
	 * @return
	 */
	public Boolean getFl_diaria_miss_italia() {
		return fl_diaria_miss_italia;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_diaria_miss_italia(Boolean boolean1) {
		fl_diaria_miss_italia = boolean1;
	}
	public Boolean getFl_motivazione_su_imp() {
		return fl_motivazione_su_imp;
	}
	public void setFl_motivazione_su_imp(Boolean fl_motivazione_su_imp) {
		this.fl_motivazione_su_imp = fl_motivazione_su_imp;
	}
	public BigDecimal getImporto_max_imp() {
		return importo_max_imp;
	}
	public void setImporto_max_imp(BigDecimal importo_max_imp) {
		this.importo_max_imp = importo_max_imp;
	}
	public Boolean getFl_approvato_definitivo() {
		return fl_approvato_definitivo;
	}
	public void setFl_approvato_definitivo(Boolean fl_approvato_definitivo) {
		this.fl_approvato_definitivo = fl_approvato_definitivo;
	}
	public Boolean getFl_deduzione_family() {
		return fl_deduzione_family;
	}
	public void setFl_deduzione_family(Boolean fl_deduzione_family) {
		this.fl_deduzione_family = fl_deduzione_family;
	}
	public Boolean getFl_deduzione_irpef() {
		return fl_deduzione_irpef;
	}
	public void setFl_deduzione_irpef(Boolean fl_deduzione_irpef) {
		this.fl_deduzione_irpef = fl_deduzione_irpef;
	}
	public Boolean getFl_detrazioni_altre() {
		return fl_detrazioni_altre;
	}
	public void setFl_detrazioni_altre(Boolean fl_detrazioni_altre) {
		this.fl_detrazioni_altre = fl_detrazioni_altre;
	}
	public Boolean getFl_detrazioni_family() {
		return fl_detrazioni_family;
	}
	public void setFl_detrazioni_family(Boolean fl_detrazioni_family) {
		this.fl_detrazioni_family = fl_detrazioni_family;
	}
	public Boolean getFl_siope() {
		return fl_siope;
	}
	public void setFl_siope(Boolean fl_siope) {
		this.fl_siope = fl_siope;
	}
	public Integer getRicerca_prof_int_giorni_pubbl() {
		return ricerca_prof_int_giorni_pubbl;
	}
	public void setRicerca_prof_int_giorni_pubbl(Integer ricerca_prof_int_giorni_pubbl) {
		this.ricerca_prof_int_giorni_pubbl = ricerca_prof_int_giorni_pubbl;
	}
	public Integer getRicerca_prof_int_giorni_scad() {
		return ricerca_prof_int_giorni_scad;
	}
	public void setRicerca_prof_int_giorni_scad(Integer ricerca_prof_int_giorni_scad) {
		this.ricerca_prof_int_giorni_scad = ricerca_prof_int_giorni_scad;
	}
	public String getOggettoEmailTerziCongua() {
		return oggettoEmailTerziCongua;
	}
	public void setOggettoEmailTerziCongua(String oggettoEmailTerziCongua) {
		this.oggettoEmailTerziCongua = oggettoEmailTerziCongua;
	}
	public String getCorpoEmailTerziCongua() {
		return corpoEmailTerziCongua;
	}
	public void setCorpoEmailTerziCongua(String corpoEmailTerziCongua) {
		this.corpoEmailTerziCongua = corpoEmailTerziCongua;
	}
	public String getOggettoEmail() {
		return oggettoEmail;
	}
	public void setOggettoEmail(String oggettoEmail) {
		this.oggettoEmail = oggettoEmail;
	}
	public String getCorpoEmail() {
		return corpoEmail;
	}
	public void setCorpoEmail(String corpoEmail) {
		this.corpoEmail = corpoEmail;
	}
	public Boolean getFl_obb_intrastat() {
		return fl_obb_intrastat;
	}
	public void setFl_obb_intrastat(Boolean fl_obb_intrastat) {
		this.fl_obb_intrastat = fl_obb_intrastat;
	}
	public Boolean getFl_cup() {
		return fl_cup;
	}
	public void setFl_cup(Boolean fl_cup) {
		this.fl_cup = fl_cup;
	}
	public String getClausolaOrdine() {
		return clausolaOrdine;
	}
	public void setClausolaOrdine(String clausolaOrdine) {
		this.clausolaOrdine = clausolaOrdine;
	}

	public java.sql.Timestamp getData_stipula_contratti() {
		return data_stipula_contratti;
	}

	public void setData_stipula_contratti(java.sql.Timestamp data_stipula_contratti) {
		this.data_stipula_contratti = data_stipula_contratti;
	}
	public Integer getLivello_pdg_cofog() {
		return livello_pdg_cofog;
	}
	public void setLivello_pdg_cofog(Integer livello_pdg_cofog) {
		this.livello_pdg_cofog = livello_pdg_cofog;
	}
	public Boolean getFl_credito_irpef() {
		return fl_credito_irpef;
	}
	public void setFl_credito_irpef(Boolean fl_credito_irpef) {
		this.fl_credito_irpef = fl_credito_irpef;
	}
	
	public Boolean getFl_siope_cup() {
		return fl_siope_cup;
	}
	public void setFl_siope_cup(Boolean fl_siope_cup) {
		this.fl_siope_cup = fl_siope_cup;
	}
	
	public Boolean getFl_nuovo_pdg() {
		return fl_nuovo_pdg;
	}
	
	public void setFl_nuovo_pdg(Boolean fl_nuovo_pdg) {
		this.fl_nuovo_pdg = fl_nuovo_pdg;
	}
	public java.sql.Timestamp getData_attivazione_new_voce() {
		return data_attivazione_new_voce;
	}
	
	public void setData_attivazione_new_voce(java.sql.Timestamp data_attivazione_new_voce) {
		this.data_attivazione_new_voce = data_attivazione_new_voce;
	}	public Boolean getFl_tesoreria_unica() {
		return fl_tesoreria_unica;

	}
	public void setFl_tesoreria_unica(Boolean fl_tesoreria_unica) {
		this.fl_tesoreria_unica = fl_tesoreria_unica;
	}
	public Boolean getFl_pubblica_contratto() {
		return fl_pubblica_contratto;
	}

	public void setFl_pubblica_contratto(Boolean fl_pubblica_contratto) {
		this.fl_pubblica_contratto = fl_pubblica_contratto;
	}	
	public Integer getLivello_eco() {
		return livello_eco;
	}

	public Integer getLivello_pat() {
		return livello_pat;
	}

	public void setLivello_eco(Integer integer) {
		livello_eco = integer;
	}

	public void setLivello_pat(Integer integer) {
		livello_pat = integer;
	}
	public Boolean getFl_nuova_gestione_pg() {
		return fl_nuova_gestione_pg;
	}
	public void setFl_nuova_gestione_pg(Boolean fl_nuova_gestione_pg) {
		this.fl_nuova_gestione_pg = fl_nuova_gestione_pg;
	}
	
	public Boolean getFl_pdg_codlast() {
		return fl_pdg_codlast;
	}
	
	public void setFl_pdg_codlast(Boolean fl_pdg_codlast) {
		this.fl_pdg_codlast = fl_pdg_codlast;
	}
	
	public Boolean getFl_pdg_contrattazione() {
		return fl_pdg_contrattazione;
	}
	
	public void setFl_pdg_contrattazione(Boolean fl_pdg_contrattazione) {
		this.fl_pdg_contrattazione = fl_pdg_contrattazione;
	}
	
	public Boolean getFl_pdg_quadra_fonti_esterne() {
		return fl_pdg_quadra_fonti_esterne;
	}
	
	public void setFl_pdg_quadra_fonti_esterne(Boolean fl_pdg_quadra_fonti_esterne) {
		this.fl_pdg_quadra_fonti_esterne = fl_pdg_quadra_fonti_esterne;
	}}