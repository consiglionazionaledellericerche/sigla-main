/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

import java.util.Iterator;

public class ScadenzarioDottoratiBulk extends ScadenzarioDottoratiBase {
	/**
	 * [ANAGRAFICA_DOTTORATI ]
	 **/
	private Anagrafica_dottoratiBulk anagraficaDottorati =  new Anagrafica_dottoratiBulk();
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();

	private CdsBulk cds = new CdsBulk();
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

	public String getCdCds(){
		CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		return getCds().getCd_unita_padre();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [CdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getCds().setCd_unita_padre(cdCds);
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

}