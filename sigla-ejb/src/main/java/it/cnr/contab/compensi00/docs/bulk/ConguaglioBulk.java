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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneItalianoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.Timestamp;

public class ConguaglioBulk extends ConguaglioBase {

	private BancaBulk banca;
	private Rif_termini_pagamentoBulk terminiPagamento;
	private Rif_modalita_pagamentoBulk modalitaPagamento;
	private java.util.Collection modalita;
	private java.util.Collection termini;
	private Tipo_rapportoBulk tipoRapporto;
	private Tipo_trattamentoBulk tipoTrattamento = new Tipo_trattamentoBulk();
	private V_terzo_per_conguaglioBulk v_terzo = new V_terzo_per_conguaglioBulk ();
	private java.util.Collection tipiRapporto;
	private java.util.Collection tipiTrattamento;
	private CompensoBulk compenso;
	private java.lang.Long pgTempCompenso;
	private int statoConguaglio = ABILITA_CONGUAGLIO;
	public final static int STATO_NORMALE = 2;
	public final static int CREA_COMPENSO_CONGUAGLIO = 1;
	public final static int ABILITA_CONGUAGLIO = 0;

	public final static int CANCELLAZIONE_FISICA = 2;
	public final static int CANCELLAZIONE_LOGICA = 1;	
	private ComuneItalianoBulk  comune_esterno;
public ConguaglioBulk() {
	super();
}
public ConguaglioBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_conguaglio) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_conguaglio);
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.39.19)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
	return banca;
}
public java.lang.String getCd_cds_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getCd_cds();
}
public java.lang.String getCd_modalita_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
	if (modalitaPagamento == null)
		return null;
	return modalitaPagamento.getCd_modalita_pag();
}
public java.lang.String getCd_termini_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk terminiPagamento = this.getTerminiPagamento();
	if (terminiPagamento == null)
		return null;
	return terminiPagamento.getCd_termini_pag();
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
	if (tipoRapporto == null)
		return null;
	return tipoRapporto.getCd_tipo_rapporto();
}
public java.lang.String getCd_trattamento() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento = this.getTipoTrattamento();
	if (tipoTrattamento == null)
		return null;
	return tipoTrattamento.getCd_trattamento();
}
public java.lang.String getCd_uo_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.43.18)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public CompensoBulk getCompenso() {
	return compenso;
}
public java.util.Calendar getDateCalendar(java.sql.Timestamp date) {

	if (date == null)
		date = new java.sql.Timestamp(System.currentTimeMillis());
		
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(java.util.Calendar.HOUR, 0);
	calendar.set(java.util.Calendar.MINUTE, 0);
	calendar.set(java.util.Calendar.SECOND, 0);
	calendar.set(java.util.Calendar.MILLISECOND, 0);
	calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	return calendar;
}
public java.math.BigDecimal getDetrazioni_al() {

	if (getDetrazioni_al_dovuto()==null || getDetrazioni_al_goduto()==null)
		return null;
	return getDetrazioni_al_dovuto().add(getDetrazioni_al_goduto().negate());
}
public java.math.BigDecimal getDetrazioni_co() {

	if (getDetrazioni_co_dovuto()==null || getDetrazioni_co_goduto()==null)
		return null;
	return getDetrazioni_co_dovuto().add(getDetrazioni_co_goduto().negate());
}
public java.math.BigDecimal getDetrazioni_fi() {

	if (getDetrazioni_fi_dovuto()==null || getDetrazioni_fi_goduto()==null)
		return null;
	return getDetrazioni_fi_dovuto().add(getDetrazioni_fi_goduto().negate());
}
public java.math.BigDecimal getDetrazioni_la() {

	if (getDetrazioni_la_dovuto()==null || getDetrazioni_la_goduto()==null)
		return null;
	return getDetrazioni_la_dovuto().add(getDetrazioni_la_goduto().negate());
}
public java.math.BigDecimal getDetrazioni_pe() {

	if (getDetrazioni_pe_dovuto()==null || getDetrazioni_pe_goduto()==null)
		return null;
	return getDetrazioni_pe_dovuto().add(getDetrazioni_pe_goduto().negate());
}
	public java.math.BigDecimal getDetrazioni_rid_cuneo() {

		if (getDetrazioni_rid_cuneo_dovuto()==null || getDetrazioni_rid_cuneo_goduto()==null)
			return null;
		return getDetrazioni_rid_cuneo_dovuto().add(getDetrazioni_rid_cuneo_goduto().negate());
	}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.34.06)
 * @return java.lang.String
 */
public java.lang.String getDs_comune() {

	if (getV_terzo()==null)
		return null;

	return getV_terzo().getDs_comune_fiscale();
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.34.06)
 * @return java.lang.String
 */
public java.lang.String getDs_provincia() {

	if (getV_terzo()==null)
		return null;

	return getV_terzo().getDs_provincia_fiscale();
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.34.06)
 * @return java.lang.String
 */
public java.lang.String getDs_regione() {

	if (getV_terzo()==null)
		return null;

	return getV_terzo().getDs_regione_fiscale();
}
public java.lang.Integer getEsercizio_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getEsercizio();
}
public java.math.BigDecimal getIm_addcom() {

	if (getIm_addcom_dovuto()==null || getIm_addcom_goduto()==null)
		return null;
	return getIm_addcom_dovuto().add(getIm_addcom_goduto().negate());
}
public java.math.BigDecimal getIm_addprov() {

	if (getIm_addprov_dovuto()==null || getIm_addprov_goduto()==null)
		return null;
	return getIm_addprov_dovuto().add(getIm_addprov_goduto().negate());
}
public java.math.BigDecimal getIm_addreg() {

	if (getIm_addreg_dovuto()==null || getIm_addreg_goduto()==null)
		return null;
	return getIm_addreg_dovuto().add(getIm_addreg_goduto().negate());
}
public java.math.BigDecimal getIm_deduzione() {

	if (getIm_deduzione_dovuto()==null || getIm_deduzione_goduto()==null)
		return null;
	return getIm_deduzione_dovuto().add(getIm_deduzione_goduto().negate());
}
public java.math.BigDecimal getIm_deduzione_family() {

	if (getIm_deduzione_family_dovuto()==null || getIm_deduzione_family_goduto()==null)
		return null;
	return getIm_deduzione_family_dovuto().add(getIm_deduzione_family_goduto().negate());
}
public java.math.BigDecimal getIm_irpef() {

	if (getIm_irpef_dovuto()==null || getIm_irpef_goduto()==null)
		return null;
	return getIm_irpef_dovuto().add(getIm_irpef_goduto().negate());
}
public java.math.BigDecimal getIm_credito_irpef() {

	if (getIm_credito_irpef_dovuto()==null || getIm_credito_irpef_goduto()==null)
		return null;
	return getIm_credito_irpef_dovuto().add(getIm_credito_irpef_goduto().negate());
}
	public java.math.BigDecimal getIm_bonus_irpef() {

		if (getIm_bonus_irpef_dovuto()==null || getIm_bonus_irpef_goduto()==null)
			return null;
		return getIm_bonus_irpef_dovuto().add(getIm_bonus_irpef_goduto().negate());
	}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.34.06)
 * @return java.lang.String
 */
public java.lang.String getIndirizzoTerzo() {

	if (getTerzo()==null)
		return null;

	String indirizzo = "";
	if (getTerzo().getVia_sede()!=null)
		indirizzo = indirizzo + getTerzo().getVia_sede();
	if (getTerzo().getNumero_civico_sede()!=null)
		indirizzo = indirizzo + " " + getTerzo().getNumero_civico_sede();
		
	return indirizzo;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return java.util.Collection
 */
public java.util.Collection getModalita() {
	return modalita;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalitaPagamento() {
	return modalitaPagamento;
}
public String getMsgAbilitaConguaglio() {

	return "E' necessario abilitare il conguaglio";
}
public String getMsgCreaCompensoConguaglio() {

	return "E' necessario creare il conguaglio";
}
public java.lang.Long getPg_banca() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
	if (banca == null)
		return null;
	return banca.getPg_banca();
}
public java.lang.Long getPg_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getPg_compenso();
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12.40.29)
 * @return java.lang.Long
 */
public java.lang.Long getPgConguaglioPos() {

	if (getPg_conguaglio()!=null && getPg_conguaglio().intValue()<0)
		return null;

	return getPg_conguaglio();
}
/**
 * Insert the method's description here.
 * Creation date: (15/07/2002 17.55.06)
 * @return java.lang.Long
 */
public java.lang.Long getPgTempCompenso() {
	return pgTempCompenso;
}
/**
 * Insert the method's description here.
 * Creation date: (16/07/2002 16.29.19)
 * @return int
 */
public int getStatoConguaglio() {
	return statoConguaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return java.util.Collection
 */
public java.util.Collection getTermini() {
	return termini;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTerminiPagamento() {
	return terminiPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.25.23)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public TerzoBulk getTerzo() {

	if (getV_terzo()==null)
		return null;
	return getV_terzo().getTerzo();
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return java.util.Collection
 */
public java.util.Collection getTipiRapporto() {
	return tipiRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return java.util.Collection
 */
public java.util.Collection getTipiTrattamento() {
	return tipiTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipoRapporto() {
	return tipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk getTipoTrattamento() {
	return tipoTrattamento;
}
public java.math.BigDecimal getTotale_detrazioni() {

	if (getTotale_detrazioni_dovuto()==null || getTotale_detrazioni_goduto()==null)
		return null;
	return getTotale_detrazioni_dovuto().add(getTotale_detrazioni_goduto().negate());
}
public java.math.BigDecimal getTotale_detrazioni_dovuto() {

	if (getDetrazioni_la_dovuto()==null ||
		getDetrazioni_pe_dovuto()==null ||
		getDetrazioni_co_dovuto()==null ||
		getDetrazioni_fi_dovuto()==null ||
		getDetrazioni_al_dovuto()==null ||
			getDetrazioni_rid_cuneo_dovuto()==null )
			return null;
	java.math.BigDecimal tot = new java.math.BigDecimal(0);
	tot = tot.add(getDetrazioni_la_dovuto());
	tot = tot.add(getDetrazioni_pe_dovuto());
	tot = tot.add(getDetrazioni_co_dovuto());
	tot = tot.add(getDetrazioni_fi_dovuto());
	tot = tot.add(getDetrazioni_al_dovuto());
	tot = tot.add(getDetrazioni_rid_cuneo_dovuto());
	return tot;
}
public java.math.BigDecimal getTotale_detrazioni_goduto() {

	if (getDetrazioni_la_goduto()==null ||
		getDetrazioni_pe_goduto()==null ||
		getDetrazioni_co_goduto()==null ||
		getDetrazioni_fi_goduto()==null ||
		getDetrazioni_al_goduto()==null ||
			getDetrazioni_rid_cuneo_goduto()==null )
			return null;
	java.math.BigDecimal tot = new java.math.BigDecimal(0);
	tot = tot.add(getDetrazioni_la_goduto());
	tot = tot.add(getDetrazioni_pe_goduto());
	tot = tot.add(getDetrazioni_co_goduto());
	tot = tot.add(getDetrazioni_fi_goduto());
	tot = tot.add(getDetrazioni_al_goduto());
	tot = tot.add(getDetrazioni_rid_cuneo_goduto());
	return tot;
}
public java.math.BigDecimal getTotale_irpef() {

	if (getTotale_irpef_dovuto()==null || getTotale_irpef_goduto()==null)
		return null;
	return getTotale_irpef_dovuto().add(getTotale_irpef_goduto().negate());
}
public java.math.BigDecimal getTotale_irpef_dovuto() {

	if (getIm_irpef_dovuto()==null || 
		getIm_addreg_dovuto()==null ||
		getIm_addprov_dovuto()==null ||
		getIm_addcom_dovuto()==null)
			return null;
	java.math.BigDecimal tot = new java.math.BigDecimal(0);
	tot = tot.add(getIm_irpef_dovuto());
	tot = tot.add(getIm_addreg_dovuto());
	tot = tot.add(getIm_addprov_dovuto());
	tot = tot.add(getIm_addcom_dovuto());
	return tot;
}
public java.math.BigDecimal getTotale_irpef_goduto() {

	if (getIm_irpef_goduto()==null || 
		getIm_addreg_goduto()==null ||
		getIm_addprov_goduto()==null ||
		getIm_addcom_goduto()==null)
			return null;
	java.math.BigDecimal tot = new java.math.BigDecimal(0);
	tot = tot.add(getIm_irpef_goduto());
	tot = tot.add(getIm_addreg_goduto());
	tot = tot.add(getIm_addprov_goduto());
	tot = tot.add(getIm_addcom_goduto());
	return tot;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @return it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public V_terzo_per_conguaglioBulk getV_terzo() {
	return v_terzo;
}
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());	
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	setNumero_giorni(new Integer(0));
	
	setTi_anagrafico(Tipo_rapportoBulk.ALTRO);
	resetImporti();
	resetDetrazioni();

	setFl_escludi_qvaria_deduzione(Boolean.FALSE);
	setFl_intera_qfissa_deduzione(Boolean.FALSE);
	setFl_accantona_add_terr(Boolean.FALSE);
	setFl_detrazioni_fam_intero_anno(Boolean.FALSE);
	setIm_detrazione_personale_anag(new java.math.BigDecimal(0));
	setIm_addreg_rate_eseprec(new java.math.BigDecimal(0));
	setIm_addprov_rate_eseprec(new java.math.BigDecimal(0));
	setIm_addcom_rate_eseprec(new java.math.BigDecimal(0));
	setFl_no_detrazioni_altre(Boolean.FALSE);
	setFl_no_detrazioni_family(Boolean.FALSE);
	setFl_detrazioni_altri_tipi(Boolean.FALSE);
	setFl_no_credito_irpef(Boolean.FALSE);
	setFl_no_credito_cuneo_irpef(Boolean.FALSE);
	setFl_no_detr_cuneo_irpef(Boolean.FALSE);


	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 17.01.17)
 * @return boolean
 */
public boolean isAbledToInsertBank() {

	return !(getTerzo()!= null &&
		getModalitaPagamento() != null &&
		!isROModalitaPagamento());
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 11.24.00)
 * @return boolean
 */
public boolean isROCdTerzo() 
{
	return isROTerzo() || (getV_terzo() == null || getV_terzo().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 11.24.00)
 * @return boolean
 */
public boolean isRODatiEsterni() {
	return isStatoNormale();
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 11.24.00)
 * @return boolean
 */
public boolean isROModalitaPagamento() 
{
	return isROTerzo();
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 11.24.00)
 * @return boolean
 */
public boolean isROTerminiPagamento() {

	return isROTerzo();
}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 11.24.00)
 * @return boolean
 */
public boolean isROTerzo() 
{
	//	Se sono in modifica del conguaglio devono essere disabilitati
	//	tutti i dati del terzo
	return getPg_conguaglio()!=null && (getPg_conguaglio().compareTo(new Long(0)) > 0);
}
public boolean isStatoAbilitaConguaglio(){

	return getStatoConguaglio() == ABILITA_CONGUAGLIO;
}
public boolean isStatoCreaCompensoConguaglio(){

	return getStatoConguaglio() == CREA_COMPENSO_CONGUAGLIO;
}
public boolean isStatoNormale(){

	return getStatoConguaglio() == STATO_NORMALE;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetDetrazioni(){

	resetDetrazioniEsterno();
	resetDetrazioniDovuto();
	resetDetrazioniGoduto();
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetDetrazioniDovuto(){
	
	// Detrazioni dovuto
	setDetrazioni_la_dovuto(new java.math.BigDecimal(0));
	setDetrazioni_pe_dovuto(new java.math.BigDecimal(0));
	setDetrazioni_co_dovuto(new java.math.BigDecimal(0));
	setDetrazioni_fi_dovuto(new java.math.BigDecimal(0));
	setDetrazioni_al_dovuto(new java.math.BigDecimal(0));
	setDetrazioni_rid_cuneo_dovuto(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetDetrazioniEsterno(){
	
	// Detrazioni esterno
	setDetrazioni_la_esterno(new java.math.BigDecimal(0));
	setDetrazioni_pe_esterno(new java.math.BigDecimal(0));
	setDetrazioni_co_esterno(new java.math.BigDecimal(0));
	setDetrazioni_fi_esterno(new java.math.BigDecimal(0));
	setDetrazioni_al_esterno(new java.math.BigDecimal(0));
	setDetrazioni_rid_cuneo_esterno(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetDetrazioniGoduto(){
	
	// Detrazioni goduto
	setDetrazioni_la_goduto(new java.math.BigDecimal(0));
	setDetrazioni_pe_goduto(new java.math.BigDecimal(0));
	setDetrazioni_co_goduto(new java.math.BigDecimal(0));
	setDetrazioni_fi_goduto(new java.math.BigDecimal(0));
	setDetrazioni_al_goduto(new java.math.BigDecimal(0));
	setDetrazioni_rid_cuneo_goduto(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetImporti(){

	setIm_lordo_conguaglio(new java.math.BigDecimal(0));
	setImponibile_fiscale_esterno(new java.math.BigDecimal(0));

	setImponibile_irpef_netto(new java.math.BigDecimal(0));
	setImponibile_irpef_lordo(new java.math.BigDecimal(0));

	resetImportiEsterno();
	resetImportiDovuto();
	resetImportiGoduto();
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetImportiDovuto(){

	setIm_irpef_dovuto(new java.math.BigDecimal(0));
	setIm_irpef_dovuto(new java.math.BigDecimal(0));
	setIm_addreg_dovuto(new java.math.BigDecimal(0));
	setIm_addprov_dovuto(new java.math.BigDecimal(0));
	setIm_addcom_dovuto(new java.math.BigDecimal(0));
	setIm_deduzione_dovuto(new java.math.BigDecimal(0));
	setIm_family_dovuto(new java.math.BigDecimal(0));
	setIm_deduzione_family_dovuto(new java.math.BigDecimal(0));
	setIm_credito_irpef_dovuto(new java.math.BigDecimal(0));
	setIm_bonus_irpef_dovuto(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetImportiEsterno(){

	setIm_irpef_esterno(new java.math.BigDecimal(0));
	setIm_addreg_esterno(new java.math.BigDecimal(0));
	setIm_addprov_esterno(new java.math.BigDecimal(0));
	setIm_addcom_esterno(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
private void resetImportiGoduto(){

	setIm_irpef_goduto(new java.math.BigDecimal(0));
	setIm_addreg_goduto(new java.math.BigDecimal(0));
	setIm_addprov_goduto(new java.math.BigDecimal(0));
	setIm_addcom_goduto(new java.math.BigDecimal(0));
	setIm_deduzione_goduto(new java.math.BigDecimal(0));
	setIm_family_goduto(new java.math.BigDecimal(0));
	setIm_deduzione_family_goduto(new java.math.BigDecimal(0));
	setIm_credito_irpef_goduto(new java.math.BigDecimal(0));
	setIm_bonus_irpef_goduto(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.39.19)
 * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
	banca = newBanca;
}
public void setCd_cds_compenso(java.lang.String cd_cds_compenso) {
	this.getCompenso().setCd_cds(cd_cds_compenso);
}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.getModalitaPagamento().setCd_modalita_pag(cd_modalita_pag);
}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.getTerminiPagamento().setCd_termini_pag(cd_termini_pag);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.getTipoTrattamento().setCd_trattamento(cd_trattamento);
}
public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
	this.getCompenso().setCd_unita_organizzativa(cd_uo_compenso);
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.43.18)
 * @param newCompenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public void setCompenso(CompensoBulk newCompenso) {
	compenso = newCompenso;
}
public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
	this.getCompenso().setEsercizio(esercizio_compenso);
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalita java.util.Collection
 */
public void setModalita(java.util.Collection newModalita) {
	modalita = newModalita;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setModalitaPagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalitaPagamento) {
	modalitaPagamento = newModalitaPagamento;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.getBanca().setPg_banca(pg_banca);
}
public void setPg_compenso(java.lang.Long pg_compenso) {
	this.getCompenso().setPg_compenso(pg_compenso);
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12.40.29)
 * @param newPgCompensoPos java.lang.Long
 */
public void setPgConguaglioPos(java.lang.Long newPgConguaglioPos) {
	setPg_conguaglio(newPgConguaglioPos);
}
/**
 * Insert the method's description here.
 * Creation date: (15/07/2002 17.55.06)
 * @param newPgTempCompenso java.lang.Long
 */
public void setPgTempCompenso(java.lang.Long newPgTempCompenso) {
	pgTempCompenso = newPgTempCompenso;
}
/**
 * Insert the method's description here.
 * Creation date: (16/07/2002 16.29.19)
 * @param newStatoConguaglio int
 */
private void setStatoConguaglio(int newStatoConguaglio) {
	statoConguaglio = newStatoConguaglio;
}
public void setStatoToAbilitaConguaglio(){

	setStatoConguaglio(ABILITA_CONGUAGLIO);
}
public void setStatoToCreaCompensoConguaglio(){

	setStatoConguaglio(CREA_COMPENSO_CONGUAGLIO);
}
public void setStatoToNormale(){

	setStatoConguaglio(STATO_NORMALE);
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTermini java.util.Collection
 */
public void setTermini(java.util.Collection newTermini) {
	termini = newTermini;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTerminiPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public void setTerminiPagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTerminiPagamento) {
	terminiPagamento = newTerminiPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTipiRapporto java.util.Collection
 */
public void setTipiRapporto(java.util.Collection newTipiRapporto) {
	tipiRapporto = newTipiRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTipiTrattamento java.util.Collection
 */
public void setTipiTrattamento(java.util.Collection newTipiTrattamento) {
	tipiTrattamento = newTipiTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTipoRapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public void setTipoRapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipoRapporto) {
	tipoRapporto = newTipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newTipoTrattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public void setTipoTrattamento(it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk newTipoTrattamento) {
	tipoTrattamento = newTipoTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 17.42.31)
 * @param newV_terzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public void setV_terzo(V_terzo_per_conguaglioBulk newV_terzo) {
	v_terzo = newV_terzo;
}
/**
  * Se l'esercizio di scrivania e' antecedente a quello solare
  * non posso modificare le date di registrazione, competenza coge
  * da/a. Esse devono rimanere uguali a 31/12/esercizio scrivania
  */

public void validaDataPerChiusura(java.sql.Timestamp ts) throws it.cnr.jada.comp.ApplicationException
{
	try
	{
		java.sql.Timestamp tsOdierno = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

	   	java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
	   	tsOdiernoGregorian.setTime(tsOdierno);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

		if(	tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) > getEsercizio().intValue() &&
			!ts.equals(new java.sql.Timestamp(sdf.parse("31/12/"+getEsercizio().intValue()).getTime())))
		{
			throw new it.cnr.jada.comp.ApplicationException("Data non e' modificabile");
		}	
	}
	catch(java.text.ParseException e)
	{
		throw new ApplicationException("Errore nella validazione della data!");		
	}
	catch(javax.ejb.EJBException e)
	{
		throw new ApplicationException("Errore nella validazione della data!");				
	}
}
public void validaDate() throws ApplicationException, javax.ejb.EJBException, java.text.ParseException
{
	if (getDt_registrazione() == null)
		throw new ApplicationException("Inserire la data registrazione");

	//	Se sono in CREAZIONE di un conguaglio con esercizio di scrivania precedente 
	//	a quello corrente la data di competenza deve per forza essere inizializzata
	//	al 31/12/esercizio scrivania.
	java.sql.Timestamp tsOdierno = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

   	java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
   	tsOdiernoGregorian.setTime(tsOdierno);
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

	if(	tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) > getEsercizio().intValue() &&
		!getDt_registrazione().equals(new java.sql.Timestamp(sdf.parse("31/12/"+getEsercizio().intValue()).getTime())))
	{
		throw new it.cnr.jada.comp.ApplicationException("La data di registrazione non e' modificabile");
	}	

	java.util.Calendar calendar = getDateCalendar(getDt_registrazione());
	int annoDataRegistrazione = calendar.get(java.util.Calendar.YEAR);

	if (annoDataRegistrazione != getEsercizio().intValue())
		throw new it.cnr.jada.comp.ApplicationException("La Data registrazione deve essere nell'esercizio in corso");
	
	validaDateCompetenzaCoge();
	validaDateCompetenzaCogeEsterne();
}
public void validaDateCompetenzaCoge() throws ApplicationException
{
	if (getDt_da_competenza_coge() == null || getDt_a_competenza_coge() == null)
		throw new ApplicationException("Inserire la data competenza COGE.");

	if (getDt_a_competenza_coge().before(getDt_da_competenza_coge()))
		throw new ApplicationException("Data competenza COGE non corretta. Verificare i periodi.");
}
public void validaDateCompetenzaCogeEsterne() throws ApplicationException{

	if ((getDt_da_competenza_esterno()==null && getDt_a_competenza_esterno()!=null) || (getDt_da_competenza_esterno()!=null && getDt_a_competenza_esterno()==null))
		throw new ApplicationException("Le Date Esterne di Competenza Coge non sono corrette.");

	if (getDt_a_competenza_esterno()!=null && getDt_da_competenza_esterno()!=null && getDt_a_competenza_esterno().compareTo(getDt_da_competenza_esterno())<0)
		throw new ApplicationException("Le Date esterne di Competenza Coge non sono corrette. Verificare i periodi.");
}
public void validaDatiEsterni() throws ApplicationException{

	if (getImponibile_fiscale_esterno()==null)
		throw new ApplicationException("Il campo IMPONIBILE FISCALE non può essere vuoto");
	if (getIm_addreg_esterno()==null)
		throw new ApplicationException("Il campo ADDIZIONALE REGIONALE non può essere vuoto");
	if (getIm_addprov_esterno()==null)
		throw new ApplicationException("Il campo ADDIZIONALE PROVINCIALE non può essere vuoto");
	if (getIm_addcom_esterno()==null)
		throw new ApplicationException("Il campo ADDIZIONALE COMUNALE non può essere vuoto");

	if (getDetrazioni_la_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI LAVORO non può essere vuoto");
	if (getDetrazioni_pe_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI PERCIPIENTE non può essere vuoto");
	if (getDetrazioni_co_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI CONIUGE non può essere vuoto");
	if (getDetrazioni_fi_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI FIGLI non può essere vuoto");
	if (getDetrazioni_al_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI ALTRI non può essere vuoto");
	if (getDetrazioni_rid_cuneo_esterno()==null)
		throw new ApplicationException("Il campo DETRAZIONI RIDUZIONE CUNEO non può essere vuoto");
}
public void validaTerzo() throws ApplicationException{

	if (getTerzo()==null)
		throw new ApplicationException("Inserire il terzo");
	if (getTerzo().getDt_fine_rapporto()!=null)
		if (getTerzo().getDt_fine_rapporto().compareTo(getDt_da_competenza_coge())<=0)
			throw new ApplicationException("Il terzo selezionato non è valido");

//	if (getDt_registrazione().compareTo(getV_terzo().getDt_ini_validita())<0 || getDt_registrazione().compareTo(getV_terzo().getDt_fin_validita())>0)
//		throw new ApplicationException("Il terzo selezionato non è valido alla Data Registrazione del compenso");
		
	if (getModalitaPagamento()==null)
		throw new ApplicationException("Inserire le modalità di pagamento");

	if (getTipoRapporto()==null)
		throw new ApplicationException("Inserire il tipo rapporto");

	if (getTipoTrattamento()==null)
		throw new ApplicationException("Inserire il tipo trattamento");
	if (getDt_registrazione().compareTo(getTipoTrattamento().getDt_ini_validita())<0 || getDt_registrazione().compareTo(getTipoTrattamento().getDt_fin_validita())>0)
		throw new ApplicationException("Il tipo trattamento selezionato non è valido alla Data Registrazione del conguaglio");
}
public void validaTestata() throws ApplicationException, java.text.ParseException, javax.ejb.EJBException
{
	// Validazione Date
	validaDate();

	// Validazione Descrizione
	if (getDs_conguaglio()==null)
		throw new ApplicationException("Inserire la Descrizione");
}
public ComuneItalianoBulk getComune_esterno() {
	return comune_esterno;
}
public void setComune_esterno(ComuneItalianoBulk comune_esterno) {
	this.comune_esterno = comune_esterno;
}
}
