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

package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Pdg_preventivo_spe_det
 */

public class Pdg_preventivo_spe_detBulk extends Pdg_preventivo_spe_detBase implements Pdg_preventivo_detBulk {

	protected CdrBulk            altro_cdr;
	protected CdrBulk            centro_responsabilita;
	protected CdrBulk            centro_responsabilita_clgs;
	protected CdrBulk            centro_responsabilita_clge;
	protected WorkpackageBulk linea_attivita;
	protected WorkpackageBulk linea_attivita_clgs;
	protected WorkpackageBulk linea_attivita_clge;
	protected Elemento_voceBulk  elemento_voce;
	protected Capoconto_finBulk categoria_economica_finanziaria;
	protected Classificazione_speseBulk classificazione_spese;
	protected Pdg_variazioneBulk pdg_variazione;
	
	public final static java.util.Dictionary STATO;
	static {
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(ST_NESSUNA_AZIONE,"Nessuna azione");
		STATO.put(ST_CONFERMA,"Conferma");
		STATO.put(ST_ANNULLA,"Annulla");
	}

	protected it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs;
	protected it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clge;
	private it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura;
	private it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione;

	protected java.util.Collection nature;
	protected java.util.Collection funzioni;

	public Pdg_preventivo_spe_detBulk() {
		super();
	}

public Pdg_preventivo_spe_detBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_spesa,ti_appartenenza,ti_gestione);
	setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
public void completaImportiNulli() {

	if(getIm_raa_a2_costi_finali() == null)
		setIm_raa_a2_costi_finali(IM_ZERO);

	if(getIm_rab_a2_costi_altro_cdr() == null)
		setIm_rab_a2_costi_altro_cdr(IM_ZERO);

	if(getIm_rac_a2_spese_odc() == null)
		setIm_rac_a2_spese_odc(IM_ZERO);

	if(getIm_rad_a2_spese_odc_altra_uo() == null)
		setIm_rad_a2_spese_odc_altra_uo(IM_ZERO);

	if(getIm_rae_a2_spese_ogc() == null)
		setIm_rae_a2_spese_ogc(IM_ZERO);

	if(getIm_raf_a2_spese_ogc_altra_uo() == null)
		setIm_raf_a2_spese_ogc_altra_uo(IM_ZERO);

	if(getIm_rag_a2_spese_costi_altrui() == null)
		setIm_rag_a2_spese_costi_altrui(IM_ZERO);

	if(getIm_rah_a3_costi_finali() == null)
		setIm_rah_a3_costi_finali(IM_ZERO);

	if(getIm_rai_a3_costi_altro_cdr() == null)
		setIm_rai_a3_costi_altro_cdr(IM_ZERO);

	if(getIm_ral_a3_spese_odc() == null)
		setIm_ral_a3_spese_odc(IM_ZERO);

	if(getIm_ram_a3_spese_odc_altra_uo() == null)
		setIm_ram_a3_spese_odc_altra_uo(IM_ZERO);

	if(getIm_ran_a3_spese_ogc() == null)
		setIm_ran_a3_spese_ogc(IM_ZERO);

	if(getIm_rao_a3_spese_ogc_altra_uo() == null)
		setIm_rao_a3_spese_ogc_altra_uo(IM_ZERO);

	if(getIm_rap_a3_spese_costi_altrui() == null)
		setIm_rap_a3_spese_costi_altrui(IM_ZERO);

	if(getIm_rh_ccs_costi() == null)
		setIm_rh_ccs_costi(IM_ZERO);

	if(getIm_ri_ccs_spese_odc() == null)
		setIm_ri_ccs_spese_odc(IM_ZERO);

	if(getIm_rj_ccs_spese_odc_altra_uo() == null)
		setIm_rj_ccs_spese_odc_altra_uo(IM_ZERO);

	if(getIm_rk_ccs_spese_ogc() == null)
		setIm_rk_ccs_spese_ogc(IM_ZERO);

	if(getIm_rl_ccs_spese_ogc_altra_uo() == null)
		setIm_rl_ccs_spese_ogc_altra_uo(IM_ZERO);

	if(getIm_rm_css_ammortamenti() == null)
		setIm_rm_css_ammortamenti(IM_ZERO);

	if(getIm_rn_css_rimanenze() == null)
		setIm_rn_css_rimanenze(IM_ZERO);

	if(getIm_ro_css_altri_costi() == null)
		setIm_ro_css_altri_costi(IM_ZERO);

	if(getIm_rp_css_verso_altro_cdr() == null)
		setIm_rp_css_verso_altro_cdr(IM_ZERO);

	if(getIm_rq_ssc_costi_odc() == null)
		setIm_rq_ssc_costi_odc(IM_ZERO);

	if(getIm_rr_ssc_costi_odc_altra_uo() == null)
		setIm_rr_ssc_costi_odc_altra_uo(IM_ZERO);

	if(getIm_rs_ssc_costi_ogc() == null)
		setIm_rs_ssc_costi_ogc(IM_ZERO);

	if(getIm_rt_ssc_costi_ogc_altra_uo() == null)
		setIm_rt_ssc_costi_ogc_altra_uo(IM_ZERO);

	if(getIm_ru_spese_costi_altrui() == null)
		setIm_ru_spese_costi_altrui(IM_ZERO);

	if(getIm_rv_pagamenti() == null)
		setIm_rv_pagamenti(IM_ZERO);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'altro_cdr'
 *
 * @return Il valore della proprietà 'altro_cdr'
 */
public CdrBulk getAltro_cdr() {
		if(getCentro_responsabilita_clge() != null)
			return getCentro_responsabilita_clge();
		else if(getCentro_responsabilita_clgs() != null)
			return getCentro_responsabilita_clgs();
		else
			return altro_cdr;
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'categoria_economica_finanziaria'
 *
 * @return Il valore della proprietà 'categoria_economica_finanziaria'
 */
public it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk getCategoria_economica_finanziaria() {
	return (getElemento_voce() != null && getElemento_voce().getCapoconto_fin() != null && getElemento_voce().getCapoconto_fin().getCd_capoconto_fin() != null) ? getElemento_voce().getCapoconto_fin() : categoria_economica_finanziaria;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'classificazione_spese'
 *
 * @return Il valore della proprietà 'classificazione_spese'
 */
public Classificazione_speseBulk getClassificazione_spese() {
	return (getElemento_voce() != null && getElemento_voce().getClassificazione_spese() != null && getElemento_voce().getClassificazione_spese().getEsercizio() != null && getElemento_voce().getClassificazione_spese().getCodice_cla_s() != null) ? getElemento_voce().getClassificazione_spese() : classificazione_spese;
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = this.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_centro_responsabilita_clge() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita_clge = this.getCentro_responsabilita_clge();
	if (centro_responsabilita_clge == null)
		return null;
	return centro_responsabilita_clge.getCd_centro_responsabilita();
}
public java.lang.String getCd_centro_responsabilita_clgs() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita_clgs = this.getCentro_responsabilita_clgs();
	if (centro_responsabilita_clgs == null)
		return null;
	return centro_responsabilita_clgs.getCd_centro_responsabilita();
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public java.lang.String getCd_elemento_voce_clge() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clge = this.getElemento_voce_clge();
	if (elemento_voce_clge == null)
		return null;
	return elemento_voce_clge.getCd_elemento_voce();
}
public java.lang.String getCd_elemento_voce_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getCd_elemento_voce();
}
public java.lang.String getCd_funzione() {
	it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione = this.getFunzione();
	if (funzione == null)
		return null;
	return funzione.getCd_funzione();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
}
public java.lang.String getCd_linea_attivita_clge() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita_clge = this.getLinea_attivita_clge();
	if (linea_attivita_clge == null)
		return null;
	return linea_attivita_clge.getCd_linea_attivita();
}
public java.lang.String getCd_linea_attivita_clgs() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita_clgs = this.getLinea_attivita_clgs();
	if (linea_attivita_clgs == null)
		return null;
	return linea_attivita_clgs.getCd_linea_attivita();
}
public java.lang.String getCd_natura() {
	it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura = this.getNatura();
	if (natura == null)
		return null;
	return natura.getCd_natura();
}
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'centro_responsabilita_clge'
 *
 * @return Il valore della proprietà 'centro_responsabilita_clge'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita_clge() {
		return centro_responsabilita_clge;
	}
	/**
	 * Restituisce il <code>CdrBulk</code> collegato al preventivo.
	 *
	 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
	 */

	public CdrBulk getCentro_responsabilita_clgs() {
		return centro_responsabilita_clgs;
	}

	/* 
	 * Getter dell'attributo ti_appartenenza_clgs
	 */
	public java.lang.String getDs_cdr() {
		if(centro_responsabilita == null) return null;
		return centro_responsabilita.getCd_centro_responsabilita() + " - " + centro_responsabilita.getDs_cdr();
	}

	/**
	 * .
	 *
	 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
	 */

	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elemento_voce_clge'
 *
 * @return Il valore della proprietà 'elemento_voce_clge'
 */
public Elemento_voceBulk getElemento_voce_clge() {
		return elemento_voce_clge;
	}
	/**
	 * .
	 *
	 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
	 */
	public Elemento_voceBulk getElemento_voce_clgs() {
		return elemento_voce_clgs;
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'funzione'
 *
 * @return Il valore della proprietà 'funzione'
 */
public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
	return funzione;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'funzioni'
 *
 * @return Il valore della proprietà 'funzioni'
 */
public java.util.Collection getFunzioni() {
	return funzioni;
}
	/**
	 *  Campo fittizio per visualizzare gli scarichi nelle colum.
	 *  Tra
	 *   Im_rj_ccs_spese_odc_altra_uo
	 *   Im_rl_ccs_spese_ogc_altra_uo
	 *   Im_rr_ssc_costi_odc_altra_uo
	 *   Im_rt_ssc_costi_ogc_altra_uo
	 *   Im_rp_css_verso_altro_cdr
	 *  quello diverso da 0
	 *
	 *  Altimenti ritorna 0
	 */
	public java.math.BigDecimal getIm_altra_cdr_a1() {
		java.math.BigDecimal zero = new java.math.BigDecimal(0);

		if( getIm_rj_ccs_spese_odc_altra_uo().compareTo(zero) != 0 )
			return getIm_rj_ccs_spese_odc_altra_uo();

		if( getIm_rl_ccs_spese_ogc_altra_uo().compareTo(zero) != 0 )
			return getIm_rl_ccs_spese_ogc_altra_uo();

		if( getIm_rr_ssc_costi_odc_altra_uo().compareTo(zero) != 0 )
			return getIm_rr_ssc_costi_odc_altra_uo();

		if( getIm_rt_ssc_costi_ogc_altra_uo().compareTo(zero) != 0 )
			return getIm_rt_ssc_costi_ogc_altra_uo();

		if( getIm_rp_css_verso_altro_cdr().compareTo(zero) != 0 )
			return getIm_rp_css_verso_altro_cdr();

		return zero;
	}

	/**
	 *  Campo fittizio per visualizzare gli scarichi nelle colum
	 *  Tra
	 *   Im_rad_a2_spese_odc_altra_uo
	 *   Im_raf_a2_spese_ogc_altra_uo
	 *   Im_rab_a2_costi_altro_cdr
	 *  quello diverso da 0
	 *
	 *  Altimenti ritorna 0
	 */
	public java.math.BigDecimal getIm_altra_cdr_a2() {
		java.math.BigDecimal zero = new java.math.BigDecimal(0);

		if( getIm_rad_a2_spese_odc_altra_uo().compareTo(zero) != 0 )
			return getIm_rad_a2_spese_odc_altra_uo();

		if( getIm_raf_a2_spese_ogc_altra_uo().compareTo(zero) != 0 )
			return getIm_raf_a2_spese_ogc_altra_uo();

		if( getIm_rab_a2_costi_altro_cdr().compareTo(zero) != 0 )
			return getIm_rab_a2_costi_altro_cdr();

		return zero;
	}

	/**
	 *  Campo fittizio per visualizzare gli scarichi nelle colum
	 *  Tra
	 *   Im_ram_a3_spese_odc_altra_uo
	 *   Im_rao_a3_spese_ogc_altra_uo
	 *   Im_rai_a3_costi_altro_cdr
	 *  quello diverso da 0
	 *
	 *  Altimenti ritorna 0
	 */
	public java.math.BigDecimal getIm_altra_cdr_a3() {
		java.math.BigDecimal zero = new java.math.BigDecimal(0);

		if( getIm_ram_a3_spese_odc_altra_uo().compareTo(zero) != 0 )
			return getIm_ram_a3_spese_odc_altra_uo();

		if( getIm_rao_a3_spese_ogc_altra_uo().compareTo(zero) != 0 )
			return getIm_rao_a3_spese_ogc_altra_uo();

		if( getIm_rai_a3_costi_altro_cdr().compareTo(zero) != 0 )
			return getIm_rai_a3_costi_altro_cdr();

		return zero;
	}

	/**
	 *  Campo Costi(H) ritorna la somma di:
	 *	relativa UO (I)
	 *	altra UO (J)
	 *	relativa UO (K)
	 *	altra UO (L)
	 *
	 *	o zero.
	 */
	public java.math.BigDecimal getIm_rh_ccs_costi() {
		java.math.BigDecimal zero = new java.math.BigDecimal(0);

		if( getIm_ri_ccs_spese_odc() != null )
			zero = zero.add( getIm_ri_ccs_spese_odc() );

		if( getIm_rj_ccs_spese_odc_altra_uo() != null )
			zero = zero.add( getIm_rj_ccs_spese_odc_altra_uo() );

		if( getIm_rk_ccs_spese_ogc() != null )
			zero = zero.add( getIm_rk_ccs_spese_ogc() );

		if( getIm_rl_ccs_spese_ogc_altra_uo() != null )
			zero = zero.add( getIm_rl_ccs_spese_ogc_altra_uo() );

		return zero;
	}

	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'linea_attivita_clge'
 *
 * @return Il valore della proprietà 'linea_attivita_clge'
 */
public WorkpackageBulk getLinea_attivita_clge() {
		return linea_attivita_clge;
	}
	public WorkpackageBulk getLinea_attivita_clgs() {
		return linea_attivita_clgs;
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'natura'
 *
 * @return Il valore della proprietà 'natura'
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nature'
 *
 * @return Il valore della proprietà 'nature'
 */
public java.util.Collection getNature() {
	return nature;
}
public java.util.Dictionary getOrigineKeys() {
	return Pdg_preventivoBulk.origineKeys;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dello Stato.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getStatoKeys() {
		return STATO;
	}

public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
public java.lang.String getTi_appartenenza_clge() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clge = this.getElemento_voce_clge();
	if (elemento_voce_clge == null)
		return null;
	return elemento_voce_clge.getTi_appartenenza();
}
public java.lang.String getTi_appartenenza_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
public java.lang.String getTi_gestione_clge() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clge = this.getElemento_voce_clge();
	if (elemento_voce_clge == null)
		return null;
	return elemento_voce_clge.getTi_gestione();
}
public java.lang.String getTi_gestione_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getTi_gestione();
}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setCentro_responsabilita(
			((it.cnr.contab.pdg00.bp.CRUDSpeDetPdGBP)bp).getCentro_responsabilita()
		);
		setPdg_variazione(((it.cnr.contab.pdg00.bp.CRUDSpeDetPdGBP)bp).getPdg_variazione());		
		return super.initialize(bp, context);
	}

	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		setElemento_voce(new Elemento_voceBulk());
		return super.initializeForFreeSearch(bp,context);
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		return super.initializeForInsert(bp,context);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		return super.initializeForSearch(bp,context);
	}

public boolean isOrigineDefinitivo() {
	return OR_UTENTE.equals(getOrigine());
}
public boolean isOriginePropostaVariazione() {
	return OR_PROPOSTA_VARIAZIONE.equals(getOrigine());
}
public boolean isOrigineStipendi() {
	return OR_STIPENDI.equals(getOrigine());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOaltra_uo'
 *
 * @return Il valore della proprietà 'rOaltra_uo'
 */
public boolean isROaltra_uo() {
		if(isRODettaglio()) {
			return true;
		} else {
			return (getCentro_responsabilita_clgs() != null
					|| getCentro_responsabilita_clge() != null);
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOcategoria_economica_finanziaria'
 *
 * @return Il valore della proprietà 'rOcategoria_economica_finanziaria'
 */
public boolean isROcategoria_economica_finanziaria() {
		if(isRODettaglio()) {
			return true;
		} else {
			return false;
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rODettaglio'
 *
 * @return Il valore della proprietà 'rODettaglio'
 */
public boolean isRODettaglio() {
		if(getFl_sola_lettura() == null)
			return false;
		else
			return getFl_sola_lettura().booleanValue();
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOelemento_voce'
 *
 * @return Il valore della proprietà 'rOelemento_voce'
 */
public boolean isROelemento_voce() {
		if(isRODettaglio()) {
			return true;
		} else {
			return (linea_attivita == null
					|| linea_attivita.getCd_linea_attivita() == null
					|| "".equals( linea_attivita.getCd_linea_attivita() )
					);
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOStato'
 *
 * @return Il valore della proprietà 'rOStato'
 */
public boolean isROStato() {
		return CAT_SCARICO.equals( getCategoria_dettaglio() );
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOim_rv_pagamenti'
 *
 * @return Il valore della proprietà 'rOim_rv_pagamenti'
 */
public boolean isROim_rv_pagamenti() {
		if(isSCRCLGE()) {
			return true;
		} else {
			return getPdg_variazione() != null;
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'sCRCLGE'
 *
 * @return Il valore della proprietà 'sCRCLGE'
 */
public boolean isSCRCLGE() {
		if(isRODettaglio()) {
			return true;
		} else {
			return getCd_elemento_voce_clge() != null;
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'sCRCLGS'
 *
 * @return Il valore della proprietà 'sCRCLGS'
 */
public boolean isSCRCLGS() {
	if(isRODettaglio()) {
		return true;
	} else {
		return getCd_elemento_voce_clgs() != null;
	}
}

public boolean isDaVariazione(){
	return getPdg_variazione()!=null && getPdg_variazione().getPg_variazione_pdg()!=null;	
}
public boolean isDaVariazioneSCRCLGS(){
	return isDaVariazione() || isSCRCLGS();	
}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'altro_cdr'
 *
 * @param newAltra_uo	Il valore da assegnare a 'altro_cdr'
 */
public void setAltro_cdr(CdrBulk newAltra_uo) {
		altro_cdr = newAltra_uo;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'categoria_economica_finanziaria'
 *
 * @param newCategoria_economica_finanziaria	Il valore da assegnare a 'categoria_economica_finanziaria'
 */
public void setCategoria_economica_finanziaria(it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk newCategoria_economica_finanziaria) {
	categoria_economica_finanziaria = newCategoria_economica_finanziaria;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'classificazione_spese'
 *
 * @param newClassificazione_spese	Il valore da assegnare a 'classificazione_spese'
 */
public void setClassificazione_spese(Classificazione_speseBulk newClassificazione_spese) {
	classificazione_spese = newClassificazione_spese;
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_centro_responsabilita_clge(java.lang.String cd_centro_responsabilita_clge) {
	this.getCentro_responsabilita_clge().setCd_centro_responsabilita(cd_centro_responsabilita_clge);
}
public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs) {
	this.getCentro_responsabilita_clgs().setCd_centro_responsabilita(cd_centro_responsabilita_clgs);
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_elemento_voce_clge(java.lang.String cd_elemento_voce_clge) {
	this.getElemento_voce_clge().setCd_elemento_voce(cd_elemento_voce_clge);
}
public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs) {
	this.getElemento_voce_clgs().setCd_elemento_voce(cd_elemento_voce_clgs);
}
public void setCd_funzione(java.lang.String cd_funzione) {
	this.getFunzione().setCd_funzione(cd_funzione);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
public void setCd_linea_attivita_clge(java.lang.String cd_linea_attivita_clge) {
	this.getLinea_attivita_clge().setCd_linea_attivita(cd_linea_attivita_clge);
}
public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs) {
	this.getLinea_attivita_clgs().setCd_linea_attivita(cd_linea_attivita_clgs);
}
public void setCd_natura(java.lang.String cd_natura) {
	this.getNatura().setCd_natura(cd_natura);
}
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
		centro_responsabilita = newCentro_responsabilita;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'centro_responsabilita_clge'
 *
 * @param newCentro_responsabilita_clge	Il valore da assegnare a 'centro_responsabilita_clge'
 */
public void setCentro_responsabilita_clge(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita_clge) {
		centro_responsabilita_clge = newCentro_responsabilita_clge;
	}
	public void setCentro_responsabilita_clgs(CdrBulk newCentro_responsabilita_clgs) {
		centro_responsabilita_clgs = newCentro_responsabilita_clgs;
	}

	public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
		elemento_voce = newElemento_voce;
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elemento_voce_clge'
 *
 * @param newElemento_voce_clge	Il valore da assegnare a 'elemento_voce_clge'
 */
public void setElemento_voce_clge(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce_clge) {
		elemento_voce_clge = newElemento_voce_clge;
	}
	public void setElemento_voce_clgs(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce_clgs) {
		elemento_voce_clgs = newElemento_voce_clgs;
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'funzione'
 *
 * @param newFunzione	Il valore da assegnare a 'funzione'
 */
public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
	funzione = newFunzione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'funzioni'
 *
 * @param newFunzioni	Il valore da assegnare a 'funzioni'
 */
public void setFunzioni(java.util.Collection newFunzioni) {
	funzioni = newFunzioni;
}
	public void setLinea_attivita(WorkpackageBulk newLinea_attivita) {
		linea_attivita = newLinea_attivita;
		setNatura(linea_attivita.getNatura());
		setFunzione(linea_attivita.getFunzione());
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'linea_attivita_clge'
 *
 * @param newLinea_attivita_clge	Il valore da assegnare a 'linea_attivita_clge'
 */
public void setLinea_attivita_clge(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita_clge) {
		linea_attivita_clge = newLinea_attivita_clge;
	}
	public void setLinea_attivita_clgs(WorkpackageBulk newLinea_attivita_clgs) {
		linea_attivita_clgs = newLinea_attivita_clgs;
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'natura'
 *
 * @param newNatura	Il valore da assegnare a 'natura'
 */
public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
	natura = newNatura;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nature'
 *
 * @param newNature	Il valore da assegnare a 'nature'
 */
public void setNature(java.util.Collection newNature) {
	nature = newNature;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_appartenenza_clge(java.lang.String ti_appartenenza_clge) {
	this.getElemento_voce_clge().setTi_appartenenza(ti_appartenenza_clge);
}
public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs) {
	this.getElemento_voce_clgs().setTi_appartenenza(ti_appartenenza_clgs);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
public void setTi_gestione_clge(java.lang.String ti_gestione_clge) {
	this.getElemento_voce_clge().setTi_gestione(ti_gestione_clge);
}
public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs) {
	this.getElemento_voce_clgs().setTi_gestione(ti_gestione_clgs);
}
/**
 * @return
 */
public Pdg_variazioneBulk getPdg_variazione() {
	return pdg_variazione;
}

/**
 * @param bulk
 */
public void setPdg_variazione(Pdg_variazioneBulk bulk) {
	pdg_variazione = bulk;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_pdg_variazione() {
	return getPdg_variazione().getEsercizio();
}

/**
 * @return
 */
public java.lang.Long getPg_variazione_pdg() {
	return getPdg_variazione().getPg_variazione_pdg();
}

/**
 * @param integer
 */
public void setEsercizio_pdg_variazione(java.lang.Integer integer) {
	getPdg_variazione().setEsercizio(integer);
}

/**
 * @param long1
 */
public void setPg_variazione_pdg(java.lang.Long long1) {
	getPdg_variazione().setPg_variazione_pdg(long1);
}
}
