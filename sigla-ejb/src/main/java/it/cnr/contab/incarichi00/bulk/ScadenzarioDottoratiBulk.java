/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;

public class ScadenzarioDottoratiBulk extends ScadenzarioDottoratiBase {
	/**
	 * [ANAGRAFICA_DOTTORATI ]
	 **/
	private Anagrafica_dottoratiBulk anagraficaDottorati =  new Anagrafica_dottoratiBulk();
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
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
}