/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

import java.util.Optional;

public class AnagraficaDottoratiRateBulk extends AnagraficaDottoratiRateBase {
	private CdsBulk cds;
	private Unita_organizzativaBulk uo;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = null;
	private java.util.Collection modalita;
	private it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = null;
	private V_terzo_per_compensoBulk percipiente = null;
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
	/**
	 * [SCADENZARIO_DOTTORATI ]
	 **/
	private Anagrafica_dottoratiBulk anagraficaDottorati =  new Anagrafica_dottoratiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public AnagraficaDottoratiRateBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public AnagraficaDottoratiRateBulk(Long id) {
		super(id);
	}
	public Anagrafica_dottoratiBulk getAnagraficaDottorati() {
		return anagraficaDottorati;
	}
	public void setAnagraficaDottorati(Anagrafica_dottoratiBulk anagraficaDottorati)  {
		this.anagraficaDottorati=anagraficaDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public Long getIdAnagraficaDottorati() {
		Anagrafica_dottoratiBulk anagraficaDottorati = this.getAnagraficaDottorati();
		if (anagraficaDottorati == null)
			return null;
		return getAnagraficaDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public void setIdAnagraficaDottorati(Long idAnagraficaDottorati)  {
		this.getAnagraficaDottorati().setId(idAnagraficaDottorati);
	}

	public CdsBulk getCds() {
		return cds;
	}

	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}

	public Unita_organizzativaBulk getUo() {
		return uo;
	}

	public void setUo(Unita_organizzativaBulk uo) {
		this.uo = uo;
	}

	@Override
	public void setCdCds(String cdCds) {
		Optional.ofNullable(cds)
				.orElse(new CdsBulk())
						.setCd_unita_organizzativa(cdCds);
	}

	@Override
	public String getCdCds() {
		return Optional.ofNullable(cds)
				.map(CdsBulk::getCd_unita_organizzativa)
				.orElse(super.getCdCds());
	}

	@Override
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		Optional.ofNullable(uo)
				.orElse(new Unita_organizzativaBulk())
				.setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}

	@Override
	public String getCdUnitaOrganizzativa() {
		return Optional.ofNullable(uo)
				.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
				.orElse(super.getCdUnitaOrganizzativa());
	}

	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 *
	 * @return*/
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
	 * Banca
	 */


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
}