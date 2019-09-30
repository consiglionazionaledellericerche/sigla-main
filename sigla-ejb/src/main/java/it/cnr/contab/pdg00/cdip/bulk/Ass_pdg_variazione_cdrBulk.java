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
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import java.math.BigDecimal;
import java.sql.Timestamp;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.SimpleBulkList;

public class Ass_pdg_variazione_cdrBulk extends Ass_pdg_variazione_cdrBase {
	public static final String ICONA_VISTATO = "<img align=middle class=Button alt=Visto&nbsp;apposto src=img/semaforoverde.gif>";
	public static final String ICONA_DA_VISTARE = "<img align=middle class=Button alt=Visto&nbsp;da&nbsp;apporre src=img/semaforogiallo.gif>";
	public static final String ICONA_DA_NON_VISTARE = "<img align=middle class=Button alt=Visto&nbsp;da&nbsp;non&nbsp;apporre src=img/semafororosso.gif>";

	private Pdg_variazioneBulk pdg_variazione;
	private CdrBulk centro_responsabilita;
	private BigDecimal entrata_ripartita;
	private BigDecimal entrata_diff;
	private BigDecimal spesa_ripartita;
	private BigDecimal spesa_diff;
	private SimpleBulkList righeVariazioneEtrGest = new SimpleBulkList();
	private SimpleBulkList righeVariazioneSpeGest = new SimpleBulkList();
	private Boolean fl_riga_vistabile = Boolean.FALSE;
	private Boolean fl_visto_da_apporre = Boolean.FALSE;
	private BigDecimal totale_quota_spesa;
	
	public Ass_pdg_variazione_cdrBulk() {
		super();
	}
	public Ass_pdg_variazione_cdrBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.String cd_centro_responsabilita) {
		super(esercizio, pg_variazione_pdg, cd_centro_responsabilita);
		setPdg_variazione(new Pdg_variazioneBulk(esercizio, pg_variazione_pdg));
		setCentro_responsabilita(new CdrBulk(cd_centro_responsabilita));		
	}
	/**
	 * @return
	 */
	public CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
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
	public void setCentro_responsabilita(CdrBulk bulk) {
		centro_responsabilita = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_variazione(Pdg_variazioneBulk bulk) {
		pdg_variazione = bulk;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#setEsercizio(java.lang.Integer)
	 */	
	public void setEsercizio(java.lang.Integer esercizio)  {
		getPdg_variazione().setEsercizio(esercizio);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#getEsercizio()
	 */
	public java.lang.Integer getEsercizio () {
		return getPdg_variazione().getEsercizio();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#setPg_variazione_pdg(java.lang.Long)
	 */
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		getPdg_variazione().setPg_variazione_pdg(pg_variazione_pdg);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#getPg_variazione_pdg()
	 */
	public java.lang.Long getPg_variazione_pdg () {
		return getPdg_variazione().getPg_variazione_pdg();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#setCd_centro_responsabilita(java.lang.String)
	 */
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrKey#getCd_centro_responsabilita()
	 */
	public java.lang.String getCd_centro_responsabilita () {
		if (getCentro_responsabilita()==null) return null;
		return getCentro_responsabilita().getCd_centro_responsabilita();
	}
	/**
	 * @return
	 */
	public BigDecimal getEntrata_diff() {
		return entrata_diff;
	}

	/**
	 * @return
	 */
	public BigDecimal getEntrata_ripartita() {
		return entrata_ripartita;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpesa_diff() {
		return spesa_diff;
	}

	/**
	 * @return
	 */
	public BigDecimal getSpesa_ripartita() {
		return spesa_ripartita;
	}

	/**
	 * @param decimal
	 */
	public void setEntrata_diff(BigDecimal decimal) {
		entrata_diff = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setEntrata_ripartita(BigDecimal decimal) {
		entrata_ripartita = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setSpesa_diff(BigDecimal decimal) {
		spesa_diff = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setSpesa_ripartita(BigDecimal decimal) {
		spesa_ripartita = decimal;
	}


	public boolean isAbledDettagliEntrate() {
		return !(isNotNew() && !isROIm_entrata());
	}

	public boolean isAbledDettagliSpese() {
		return !(isNotNew() && !isROIm_spesa());
	}
	/*
	 * Serve per sapere se è il campo IM_ENTRATA è in sola modalità lettura 
	 *
	 * Ritorna un boolean con valore true se la variazione è:
	 * 1) uno storno di spesa
	 * 2) un prelievo su fondi 
	 * 3) una restituzione ai fondi
	 */
	public boolean isROIm_entrata() {
		return getPdg_variazione()!=null &&
		   	   getPdg_variazione().getTipo_variazione()!=null && 
	           ((getPdg_variazione().getTipo_variazione().isStorno() &&
				 getPdg_variazione().getTipo_variazione().isTipoVariazioneSpesa()) ||
				getPdg_variazione().getTipo_variazione().isPrelievoFondi() ||
				getPdg_variazione().getTipo_variazione().isRestituzioneFondi());

	}
	/*
	 * Serve per sapere se è il campo IM_SPESA è in sola modalità lettura 
	 *
	 * Ritorna un boolean con valore true se la variazione è:
	 * 1) uno storno di entrata 
	 * 2) una variazione sui fondi
	 */
	public boolean isROIm_spesa() {
		return getPdg_variazione()!=null &&
			   getPdg_variazione().getTipo_variazione()!=null && 
		       ((getPdg_variazione().getTipo_variazione().isStorno() &&
			 	 getPdg_variazione().getTipo_variazione().isTipoVariazioneEntrata()) ||
				getPdg_variazione().getTipo_variazione().isVariazioneSuFondi());
	}

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] 
		{getRigheVariazioneEtrGest(), 
		 getRigheVariazioneSpeGest()};
	}

	public SimpleBulkList getRigheVariazioneEtrGest() {
		return righeVariazioneEtrGest;
	}
	
	public void setRigheVariazioneEtrGest(SimpleBulkList righeVariazioneEtrGest) {
		this.righeVariazioneEtrGest = righeVariazioneEtrGest;
	}

	public SimpleBulkList getRigheVariazioneSpeGest() {
		return righeVariazioneSpeGest;
	}
	
	public void setRigheVariazioneSpeGest(SimpleBulkList righeVariazioneSpeGest) {
		this.righeVariazioneSpeGest = righeVariazioneSpeGest;
	}

	public Pdg_variazione_riga_gestBulk removeFromRigheVariazioneEtrGest(int indiceDiLinea) {
		Pdg_variazione_riga_gestBulk element = (Pdg_variazione_riga_gestBulk)righeVariazioneEtrGest.get(indiceDiLinea);
		return (Pdg_variazione_riga_gestBulk)righeVariazioneEtrGest.remove(indiceDiLinea);
	}
	
	public Pdg_variazione_riga_gestBulk removeFromRigheVariazioneSpeGest(int indiceDiLinea) {
		Pdg_variazione_riga_gestBulk element = (Pdg_variazione_riga_gestBulk)righeVariazioneSpeGest.get(indiceDiLinea);
		return (Pdg_variazione_riga_gestBulk)righeVariazioneSpeGest.remove(indiceDiLinea);
	}

	private Pdg_variazione_riga_gestBulk initNuovaRigaVariazione(Pdg_variazione_riga_gestBulk nuovo)
	{	
		nuovo.setPdg_variazione(getPdg_variazione());
		nuovo.setCdr_assegnatario(getCentro_responsabilita());
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		nuovo.setDt_registrazione(today);
		nuovo.setCategoria_dettaglio(Pdg_modulo_gestBulk.CAT_DIRETTA);
		nuovo.setFl_visto_dip_variazioni(new Boolean(false));
		nuovo.setIm_spese_gest_accentrata_est(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_accentrata_int(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_decentrata_est(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_decentrata_int(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_entrata(it.cnr.contab.util.Utility.ZERO);
//		nuovo.setLinea_attivita(new WorkpackageBulk());
//		nuovo.getLinea_attivita().setProgetto(new ProgettoBulk());
		return nuovo;
	}

	public int addToRigheVariazioneEtrGest(Pdg_variazione_riga_gestBulk nuovo)
	{	
		getRigheVariazioneEtrGest().add(initNuovaRigaVariazione(nuovo));
		return getRigheVariazioneEtrGest().size()-1;
	}

	public int addToRigheVariazioneSpeGest(Pdg_variazione_riga_gestBulk nuovo)
	{	
		getRigheVariazioneSpeGest().add(initNuovaRigaVariazione(nuovo));
		return getRigheVariazioneSpeGest().size()-1;
	}
	/**
	 * Flag utilizzato per sapere se, per la variazione e per il cdr in oggetto, esistono dettagli 
	 * PDG_VARIAZIONE_RIGA_GEST su moduli appartenenti al dipartimento collegato per i quali deve essere 
	 * apposto il visto.
     *
	 * @return Boolean
	 */
	public Boolean getFl_riga_vistabile() {
		return fl_riga_vistabile;
	}
	/**
	 * Flag da settare a:
	 * TRUE:  se, per la variazione e per il cdr in oggetto, esistono dettagli PDG_VARIAZIONE_RIGA_GEST su moduli 
	 *        appartenenti al dipartimento collegato per i quali deve essere apposto il visto.
	 * FALSE: in tutti gli altri casi.
     *
	 * @return Boolean
	 */
	public void setFl_riga_vistabile(Boolean fl_riga_vistabile) {
		this.fl_riga_vistabile = fl_riga_vistabile;
	}
	/**
	 * Flag utilizzato per sapere se, per la variazione e per il cdr in oggetto, esistono dettagli 
	 * PDG_VARIAZIONE_RIGA_GEST su moduli appartenenti al dipartimento collegato per i quali deve essere 
	 * ancora apposto il visto.
     *
	 * @return Boolean
	 */
	public Boolean getFl_visto_da_apporre() {
		return fl_visto_da_apporre;
	}
	/**
	 * Flag da settare a:
	 * TRUE:  se, per la variazione e per il cdr in oggetto, esistono dettagli PDG_VARIAZIONE_RIGA_GEST su moduli 
	 *        appartenenti al dipartimento collegato per i quali deve essere ancora apposto il visto.
	 * FALSE: in tutti gli altri casi.
     *
	 * @return Boolean
	 */
	public void setFl_visto_da_apporre(Boolean fl_visto_da_apporre) {
		this.fl_visto_da_apporre = fl_visto_da_apporre;
	}

	public String getIconaVistoDipartimento() {
		if (!getFl_riga_vistabile().booleanValue())
			return ICONA_DA_NON_VISTARE;
		else if (getFl_visto_da_apporre().booleanValue()) 
			return ICONA_DA_VISTARE;
		else
			return ICONA_VISTATO;
	}
	public void setTotale_quota_spesa(
			BigDecimal totale_quota_spesa_ripartita) {
		this.totale_quota_spesa = totale_quota_spesa_ripartita;
	}
	public BigDecimal getTotale_quota_spesa() {
		return totale_quota_spesa;
	}
}