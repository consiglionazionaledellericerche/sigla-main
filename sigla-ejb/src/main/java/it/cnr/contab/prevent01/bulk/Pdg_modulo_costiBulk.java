/*
* Created by Generator 1.0
* Date 28/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_modulo_costiBulk extends Pdg_modulo_costiBase {
	private Pdg_moduloBulk pdg_modulo;
	private BigDecimal tot_massa_spendibile_anno_prec;
	private BigDecimal tot_massa_spendibile_anno_in_corso;
	private BigDecimal valore_presunto_anno_in_corso;
	private BigDecimal tot_entr_fonti_est_anno_in_corso;
	private BigDecimal tot_spese_coperte_fonti_esterne_anno_in_corso;
	
	private BigDecimal spese_decentrate_fonti_interne_istituto;
	private BigDecimal spese_decentrate_fonti_interne_aree;
	private BigDecimal spese_accentrate_fonti_interne_istituto;
	private BigDecimal spese_accentrate_fonti_interne_aree;
	private BulkList dettagliSpese = new BulkList();
	private BulkList dettagliContrSpese = new BulkList();
	private BulkList dettagliPianoEconomicoAnnoCorrente = new BulkList();

	public Pdg_modulo_costiBulk() {
		super();
	}
	public Pdg_modulo_costiBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto) {
		super();
		setPdg_modulo(new Pdg_moduloBulk(esercizio, cd_centro_responsabilita, pg_progetto));
	}
	public Pdg_modulo_costiBulk(Pdg_moduloBulk pdg_modulo) {
		super();
		setPdg_modulo(pdg_modulo);
	}	
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		getPdg_modulo().getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public String getCd_centro_responsabilita() {
		return getPdg_modulo().getCdr().getCd_centro_responsabilita();
	}
	public void setPg_progetto(Integer pg_progetto) {
		getPdg_modulo().getProgetto().setPg_progetto(pg_progetto);
	}
	public Integer getPg_progetto() {
		return getPdg_modulo().getProgetto().getPg_progetto();
	}
	public void setEsercizio(Integer esercizio) {
		getPdg_modulo().setEsercizio(esercizio);
	}
	public Integer getEsercizio() {
		return getPdg_modulo().getEsercizio();
	}


	/**
	 * @return
	 */
	public BigDecimal getTot_entr_fonti_est_anno_in_corso() {
		return tot_entr_fonti_est_anno_in_corso;
	}

	/**
	 * @return
	 */
	public BigDecimal getTot_massa_spendibile_anno_in_corso() {
		return tot_massa_spendibile_anno_in_corso;
	}

	/**
	 * @return
	 */
	public BigDecimal getTot_massa_spendibile_anno_prec() {
		return tot_massa_spendibile_anno_prec;
	}

	/**
	 * @return
	 */
	public BigDecimal getTot_spese_coperte_fonti_esterne_anno_in_corso() {
		return tot_spese_coperte_fonti_esterne_anno_in_corso;
	}

	/**
	 * @return
	 */
	public BigDecimal getValore_presunto_anno_in_corso() {
		return valore_presunto_anno_in_corso;
	}

	/**
	 * @param decimal
	 */
	public void setTot_entr_fonti_est_anno_in_corso(BigDecimal decimal) {
		tot_entr_fonti_est_anno_in_corso = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setTot_massa_spendibile_anno_in_corso(BigDecimal decimal) {
		tot_massa_spendibile_anno_in_corso = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setTot_massa_spendibile_anno_prec(BigDecimal decimal) {
		tot_massa_spendibile_anno_prec = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setTot_spese_coperte_fonti_esterne_anno_in_corso(BigDecimal decimal) {
		tot_spese_coperte_fonti_esterne_anno_in_corso = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setValore_presunto_anno_in_corso(BigDecimal decimal) {
		valore_presunto_anno_in_corso = decimal;
	}
	
	public BigDecimal getTot_costi_figurativi() {
		return Utility.nvl(getIm_cf_tfr()).add(
		       Utility.nvl(getIm_cf_tfr_det()).add(
		       Utility.nvl(getIm_cf_amm_immobili()).add(
		       Utility.nvl(getIm_cf_amm_attrezz()).add(
		       Utility.nvl(getIm_cf_amm_altro())))));
	}
	
	public BigDecimal getTot_costi() {
		return Utility.nvl(getTot_costi_figurativi()).add(Utility.nvl(getIm_costi_generali()));
	}
	
	public BigDecimal getTot_risorse_provenienti_es_prec() {
		return Utility.nvl(getRis_es_prec_tit_i()).add(
			   Utility.nvl(getRis_es_prec_tit_ii()));
	}
	
	public BigDecimal getTot_risorse_presunte_es_prec() {
		return Utility.nvl(getRis_pres_es_prec_tit_i()).add(
			   Utility.nvl(getRis_pres_es_prec_tit_ii()));
	}
	    	    
	/**
	 * @return
	 */
	public BulkList getDettagliSpese() {
		return dettagliSpese;
	}

	/**
	 * @param list
	 */
	public void setDettagliSpese(BulkList list) {
		dettagliSpese = list;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {dettagliSpese, dettagliContrSpese};
	}
	
	public int addToDettagliSpese(Pdg_modulo_speseBulk dett) {
		dett.setPdg_modulo_costi( this );
		if (this.getPdg_modulo()!=null &&
			this.getPdg_modulo().getProgetto()!=null &&
			this.getPdg_modulo().getProgetto().getPdgMissione()!=null)
			dett.setPdgMissione(this.getPdg_modulo().getProgetto().getPdgMissione());
		dettagliSpese.add(dett);
		return dettagliSpese.size()-1;
	}
	public Pdg_modulo_speseBulk removeFromDettagliSpese(int index) {
		Pdg_modulo_speseBulk dett = (Pdg_modulo_speseBulk)dettagliSpese.remove(index);
		return dett;
	}

	public int addToDettagliContrSpese(Pdg_contrattazione_speseBulk dett) {
		//dett.setPdg_modulo( this.getPdg_modulo() );
		dettagliContrSpese.add(dett);
		return dettagliContrSpese.size()-1;
	}

	public Pdg_contrattazione_speseBulk removeFromDettagliContrSpese(int index) {
		Pdg_contrattazione_speseBulk dett = (Pdg_contrattazione_speseBulk)dettagliContrSpese.remove(index);
		return dett;
	}

	public OggettoBulk initializeForInsert(CRUDBP crudbp,ActionContext actioncontext) {
		inizializzaImporti();		
		return super.initializeForInsert(crudbp, actioncontext);
	}
    private void inizializzaImporti(){
    	setIm_cf_amm_altro(Utility.ZERO);
    	setIm_cf_amm_attrezz(Utility.ZERO);
    	setIm_cf_amm_immobili(Utility.ZERO);
    	setIm_cf_tfr(Utility.ZERO);
		setIm_cf_tfr_det(Utility.ZERO);
    	setIm_costi_generali(Utility.ZERO);
    	setRis_es_prec_tit_i(Utility.ZERO);
    	setRis_es_prec_tit_ii(Utility.ZERO);
    	setRis_pres_es_prec_tit_i(Utility.ZERO);
    	setRis_pres_es_prec_tit_ii(Utility.ZERO);
    }
	/**
	 * @return
	 */
	public BigDecimal getDifferenza() {
		return Utility.nvl(getTot_entr_fonti_est_anno_in_corso()).subtract(
		       Utility.nvl(getTot_spese_coperte_fonti_esterne_anno_in_corso()));
	}
    
	
	/**
	 * @return
	 */
	public Pdg_moduloBulk getPdg_modulo() {
		return pdg_modulo;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_modulo(Pdg_moduloBulk bulk) {
		pdg_modulo = bulk;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpese_accentrate_fonti_interne_aree() {
		return spese_accentrate_fonti_interne_aree;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpese_accentrate_fonti_interne_istituto() {
		return spese_accentrate_fonti_interne_istituto;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpese_decentrate_fonti_interne_aree() {
		return spese_decentrate_fonti_interne_aree;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpese_decentrate_fonti_interne_istituto() {
		return spese_decentrate_fonti_interne_istituto;
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_accentrate_fonti_interne() {
		return Utility.nvl(getSpese_accentrate_fonti_interne_istituto()).add(
			   Utility.nvl(getSpese_accentrate_fonti_interne_aree()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_da_fonti_interne_aree() {
		return Utility.nvl(getSpese_accentrate_fonti_interne_aree()).add(
			   Utility.nvl(getSpese_decentrate_fonti_interne_aree()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_da_fonti_interne_istituto() {
		return Utility.nvl(getSpese_accentrate_fonti_interne_istituto()).add(
			   Utility.nvl(getSpese_decentrate_fonti_interne_istituto()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_da_fonti_interne_totale() {
		return Utility.nvl(getTotale_spese_da_fonti_interne_aree()).add(
			   Utility.nvl(getTotale_spese_da_fonti_interne_istituto()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_decentrate_fonti_interne() {
		return Utility.nvl(getSpese_decentrate_fonti_interne_istituto()).add(
			   Utility.nvl(getSpese_decentrate_fonti_interne_aree()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_da_fonti_interne_accentrate() {
		return Utility.nvl(getSpese_accentrate_fonti_interne_istituto()).add(
			   Utility.nvl(getSpese_accentrate_fonti_interne_aree()));
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_spese_da_fonti_interne_decentrate() {
		return Utility.nvl(getSpese_decentrate_fonti_interne_istituto()).add(
			   Utility.nvl(getSpese_decentrate_fonti_interne_aree()));
	}

	/**
	 * @param decimal
	 */
	public void setSpese_accentrate_fonti_interne_aree(BigDecimal decimal) {
		spese_accentrate_fonti_interne_aree = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setSpese_accentrate_fonti_interne_istituto(BigDecimal decimal) {
		spese_accentrate_fonti_interne_istituto = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setSpese_decentrate_fonti_interne_aree(BigDecimal decimal) {
		spese_decentrate_fonti_interne_aree = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setSpese_decentrate_fonti_interne_istituto(BigDecimal decimal) {
		spese_decentrate_fonti_interne_istituto = decimal;
	}

	/**
	 * @return
	 */
	public BulkList getDettagliContrSpese() {
		return dettagliContrSpese;
	}

	/**
	 * @param list
	 */
	public void setDettagliContrSpese(BulkList list) {
		dettagliContrSpese = list;
	}
	
	public BulkList getDettagliPianoEconomicoAnnoCorrente() {
		return dettagliPianoEconomicoAnnoCorrente;
	}

	public void setDettagliPianoEconomicoAnnoCorrente(BulkList dettagliPianoEconomicoAnnoCorrente) {
		this.dettagliPianoEconomicoAnnoCorrente = dettagliPianoEconomicoAnnoCorrente;
	}
}