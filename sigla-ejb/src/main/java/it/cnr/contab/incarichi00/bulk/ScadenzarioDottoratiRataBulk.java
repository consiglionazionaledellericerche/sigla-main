/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

import java.util.Optional;

public class ScadenzarioDottoratiRataBulk extends ScadenzarioDottoratiRataBase {
	private CdsBulk cds;
	private Unita_organizzativaBulk uo;
	/**
	 * [SCADENZARIO_DOTTORATI ]
	 **/
	private ScadenzarioDottoratiBulk scadenzarioDottorati =  new ScadenzarioDottoratiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public ScadenzarioDottoratiRataBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public ScadenzarioDottoratiRataBulk(Long id) {
		super(id);
	}
	public ScadenzarioDottoratiBulk getScadenzarioDottorati() {
		return scadenzarioDottorati;
	}
	public void setScadenzarioDottorati(ScadenzarioDottoratiBulk scadenzarioDottorati)  {
		this.scadenzarioDottorati=scadenzarioDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public Long getIdScadenzarioDottorati() {
		ScadenzarioDottoratiBulk scadenzarioDottorati = this.getScadenzarioDottorati();
		if (scadenzarioDottorati == null)
			return null;
		return getScadenzarioDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public void setIdScadenzarioDottorati(Long idScadenzarioDottorati)  {
		this.getScadenzarioDottorati().setId(idScadenzarioDottorati);
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
}