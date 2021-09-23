/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Accertamento_pluriennaleBulk extends Accertamento_pluriennaleBase {
	/**
	 * [ACCERTAMENTO ]
	 **/
	private AccertamentoBulk accertamento =  new AccertamentoBulk();
	private AccertamentoBulk accertamentoRif =  new AccertamentoBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ACCERTAMENTO_PLURIENNALE
	 **/
	public Accertamento_pluriennaleBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ACCERTAMENTO_PLURIENNALE
	 **/
	public Accertamento_pluriennaleBulk(String cdCds, Integer esercizio, Integer esercizioOriginale, Long pgAccertamento, Integer anno) {
		super(cdCds, esercizio, esercizioOriginale, pgAccertamento, anno);
		setAccertamento( new AccertamentoBulk(cdCds,esercizio,esercizioOriginale,pgAccertamento) );
	}
	public AccertamentoBulk getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(AccertamentoBulk accertamento)  {
		this.accertamento=accertamento;
	}

	public AccertamentoBulk getAccertamentoRif() {
		return accertamentoRif;
	}

	public void setAccertamentoRif(AccertamentoBulk accertamentoRif) {
		this.accertamentoRif = accertamentoRif;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Cds dell'accertamento - chiave primaria]
	 **/
	public String getCdCds() {
		AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = accertamento.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();

	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'accertamento - chiave primaria]
	 **/
	public void setCdCds(String cdCds)  {
		this.getAccertamento().getCds().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'accertamento - chiave primaria]
	 **/
	public Integer getEsercizio() {
		AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return getAccertamento().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'accertamento - chiave primaria]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.getAccertamento().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'accertamento - chiave primaria]
	 **/
	public Integer getEsercizioOriginale() {
		AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return getAccertamento().getEsercizio_originale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'accertamento - chiave primaria]
	 **/
	public void setEsercizioOriginale(Integer esercizioOriginale)  {
		this.getAccertamento().setEsercizio_originale(esercizioOriginale);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'accertamento - chiave primaria]
	 **/
	public Long getPgAccertamento() {
		AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return getAccertamento().getPg_accertamento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'accertamento - chiave primaria]
	 **/
	public void setPgAccertamento(Long pgAccertamento)  {
		this.getAccertamento().setPg_accertamento(pgAccertamento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Cds dell'accertamento di riferimento]
	 **/
	public String getCdCdsRif() {
		AccertamentoBulk accertamento = this.getAccertamentoRif();
		if (accertamento == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = accertamento.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'accertamento di riferimento]
	 **/
	public void setCdCdsRif(String cdCdsRif)  {
		this.getAccertamentoRif().getCds().setCd_unita_organizzativa(cdCdsRif);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'accertamento di riferimento]
	 **/
	public Integer getEsercizioRif() {
		AccertamentoBulk accertamento = this.getAccertamentoRif();
		if (accertamento == null)
			return null;
		return getAccertamentoRif().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'accertamento di riferimento]
	 **/
	public void setEsercizioRif(Integer esercizioRif)  {
		this.getAccertamentoRif().setEsercizio(esercizioRif);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'accertamento di riferimento]
	 **/
	public Integer getEsercizioOriginaleRif() {
		AccertamentoBulk accertamento = this.getAccertamentoRif();
		if (accertamento == null)
			return null;
		return getAccertamentoRif().getEsercizio_originale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'accertamento di riferimento]
	 **/
	public void setEsercizioOriginaleRif(Integer esercizioOriginaleRif)  {
		this.getAccertamentoRif().setEsercizio_originale(esercizioOriginaleRif);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'accertamento di riferimento]
	 **/
	public Long getPgAccertamentoRif() {
		AccertamentoBulk accertamento = this.getAccertamentoRif();
		if (accertamento == null)
			return null;
		return getAccertamentoRif().getPg_accertamento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'accertamento di riferimento]
	 **/
	public void setPgAccertamentoRif(Long pgAccertamentoRif)  {
		this.getAccertamentoRif().setPg_accertamento(pgAccertamentoRif);
	}
}