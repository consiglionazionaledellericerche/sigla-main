/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Iterator;
import java.util.Optional;

public class ScadenzarioDottoratiBulk extends ScadenzarioDottoratiBase {
	public static final String STATO_NON_ASS_COMPENSO = "N";

	/**
	 * [ANAGRAFICA_DOTTORATI ]
	 **/
	private Anagrafica_dottoratiBulk anagraficaDottorati =  new Anagrafica_dottoratiBulk();
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo;

	private CdsBulk cds = new CdsBulk();
	private ScadenzarioDottoratiBulk scadenzarioDottorati_origine = null;

	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = null;
	private java.util.Collection modalita;

	/**
	 * Banca
	 */
	private it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = null;
	private V_terzo_per_compensoBulk percipiente = null;

	public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
		return banca;
	}

	public java.lang.Long getPg_banca() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return banca.getPg_banca();
	}

	public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
		banca = newBanca;
	}

	public void setPg_banca(java.lang.Long pg_banca) {
		this.getBanca().setPg_banca(pg_banca);
	}

	/**
	 * Fine Banca
	 */
	/**
	 * Insert the method's description here.
	 * Creation date: (6/24/2002 3:36:33 PM)
	 * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
	 */
	public ScadenzarioDottoratiBulk getScadenzarioDottorati() {
		return scadenzarioDottorati_origine;
	}

	/**
	 * Termini pagamento
	 */

	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = null;
	private java.util.Collection termini;

	public java.lang.String getCd_termini_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
		if (termini_pagamento == null)
			return null;
		return termini_pagamento.getCd_termini_pag();
	}

	public java.util.Collection getTermini() {
		return termini;
	}

	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
		return termini_pagamento;
	}

	public void setCd_termini_pag(java.lang.String cd_termini_pag) {
		this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
	}

	public void setTermini(java.util.Collection newTermini) {
		termini = newTermini;
	}

	public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
		termini_pagamento = newTermini_pagamento;
	}
	/**private Integer esercizio;

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}*/

	public java.lang.String getCd_modalita_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
		if (modalita_pagamento == null)
			return null;
		return modalita_pagamento.getCd_modalita_pag();
	}

	public java.util.Collection getModalita() {
		return modalita;
	}


	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
		return modalita_pagamento;
	}

	public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
		this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
	}

	public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
		modalita_pagamento = newModalita_pagamento;
	}

	public void setModalita(java.util.Collection newModalita) {
		modalita = newModalita;
	}

	private Unita_organizzativaBulk uo = new Unita_organizzativaBulk();
	/**
	 *
	 */
	private BulkList scadenzarioDottoratiRate = new BulkList();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI
	 **/
	public ScadenzarioDottoratiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI
	 **/
	public ScadenzarioDottoratiBulk(Long id) {
		super(id);
	}
	public Anagrafica_dottoratiBulk getAnagraficaDottorati() {
		return anagraficaDottorati;
	}
	public void setAnagraficaDottorati(Anagrafica_dottoratiBulk anagraficaDottorati)  {
		this.anagraficaDottorati=anagraficaDottorati;
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}

	public CdsBulk getCds() { return cds; }
	public void setCds(CdsBulk cds) { this.cds=cds; }

	public Unita_organizzativaBulk getUo() { return uo; }
	public void setUo(Unita_organizzativaBulk uo) { this.uo=uo; }
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo anagrafica dottorati.]
	 **/
	public Long getIdAnagraficaDottorati() {
		Anagrafica_dottoratiBulk anagraficaDottorati = this.getAnagraficaDottorati();
		if (anagraficaDottorati == null)
			return null;
		return getAnagraficaDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo anagrafica dottorati.]
	 **/
	public void setIdAnagraficaDottorati(Long idAnagraficaDottorati)  {
		this.getAnagraficaDottorati().setId(idAnagraficaDottorati);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}

	/**
	 *
	 * @return CdCds
	 */
	public String getCdCds(){
		CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		//return getCds().getCd_unita_padre();
		return getCds().getCd_ds_cds();
	}
	/**
	 * Setta il valore di: [CdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getCds().setCd_unita_padre(cdCds);
	}

	/**
	 *
	 * @return CdUnitaOrganizzativa
	 */
	public String getCdUnitaOrganizzativa(){
		Unita_organizzativaBulk uo = this.getUo();
		if (uo == null)
			return null;
		return getUo().getCd_unita_organizzativa();
	}
	/**
	 * Setta il valore di: [CdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa)  {
		this.getUo().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}


	public BulkList getScadenzarioDottoratiRate() {
		return scadenzarioDottoratiRate;
	}

	public void setScadenzarioDottoratiRate(BulkList scadenzarioDottoratiRate) {
		this.scadenzarioDottoratiRate = scadenzarioDottoratiRate;
	}

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {
				getScadenzarioDottoratiRate() };
	}
	public int addToScadenzarioDottoratiRate(ScadenzarioDottoratiRataBulk dett) {
		dett.setScadenzarioDottorati(this);
		getScadenzarioDottoratiRate().add(dett);
		return getScadenzarioDottoratiRate().size()-1;
	}
	public ScadenzarioDottoratiRataBulk removeFromScadenzarioDottoratiRate(int index) {
		ScadenzarioDottoratiRataBulk dett = (ScadenzarioDottoratiRataBulk) getScadenzarioDottoratiRate().remove(index);
		return dett;
	}


	/**
	 * ROpercipiente
	 */
	public V_terzo_per_compensoBulk getPercipiente() {
		return percipiente;
	}

	/**
	 * Restituisce un boolean 'true' nel caso in cui NON posso ricercare banche
	 */

	public boolean isAbledToInsertBank() {

		return !(getTerzo()!= null &&
				getModalita_pagamento() != null &&
				!isROPercipiente());
	}

	/**
	 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente
	 * selezionato non sono modificabili
	 */

	public boolean isROFl_tassazione_separata() {

		return	isROPercipiente() ||
				(getScadenzarioDottoratiRate() != null && !getScadenzarioDottoratiRate().isEmpty());
	}

	/**
	 * Restituisce un boolean 'true' se il percipiente non è modificabile
	 */

	public boolean isROPercipiente() {

		return (getScadenzarioDottorati() != null &&
				(getScadenzarioDottorati().getCrudStatus() == OggettoBulk.NORMAL ||
						getScadenzarioDottorati().getCrudStatus() == OggettoBulk.TO_BE_UPDATED)) ||
				(getStatoAssCompenso() != null &&
						!STATO_NON_ASS_COMPENSO.equalsIgnoreCase(getStatoAssCompenso()) &&
						getCrudStatus() != OggettoBulk.UNDEFINED) ||
				!(getStato() == null ||
						getPercipiente() == null);
	}
	/**
	 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente
	 * selezionato non sono modificabili
	 */

	public boolean isROPercipienteAnag() {

		return	getPercipiente() == null ||
				getPercipiente().getCrudStatus() == OggettoBulk.NORMAL ||
				isROPercipiente();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isROTi_istituz_commerc() {
		return isROPercipiente();
	}

	public boolean isROTerzo() {
		return Optional.ofNullable(getTerzo())
				.filter(terzoBulk -> Optional.ofNullable(terzoBulk.getCd_terzo()).isPresent())
				.isPresent();
	}

}