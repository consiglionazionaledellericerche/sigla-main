/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2019
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class SIOPEPlusEsitoBulk extends SIOPEPlusEsitoBase {
	/**
	 * [MANDATO ]
	 **/
	private MandatoBulk mandato;
	/**
	 * [REVERSALE ]
	 **/
	private ReversaleBulk reversale;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_ESITO
	 **/
	public SIOPEPlusEsitoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_ESITO
	 **/
	public SIOPEPlusEsitoBulk(java.lang.Long id) {
		super(id);
	}
	public MandatoBulk getMandato() {
		return mandato;
	}
	public void setMandato(MandatoBulk mandato)  {
		this.mandato=mandato;
	}
	public ReversaleBulk getReversale() {
		return reversale;
	}
	public void setReversale(ReversaleBulk reversale)  {
		this.reversale=reversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio mandato]
	 **/
	public java.lang.Integer getEsercizioMandato() {
		MandatoBulk mandato = this.getMandato();
		if (mandato == null)
			return null;
		return getMandato().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio mandato]
	 **/
	public void setEsercizioMandato(java.lang.Integer esercizioMandato)  {
		this.getMandato().setEsercizio(esercizioMandato);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [CdS mandato]
	 **/
	public java.lang.String getCdCdsMandato() {
		MandatoBulk mandato = this.getMandato();
		if (mandato == null)
			return null;
		return getMandato().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [CdS mandato]
	 **/
	public void setCdCdsMandato(java.lang.String cdCdsMandato)  {
		this.getMandato().setCd_cds(cdCdsMandato);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio mandato]
	 **/
	public java.lang.Long getPgMandato() {
		MandatoBulk mandato = this.getMandato();
		if (mandato == null)
			return null;
		return getMandato().getPg_mandato();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio mandato]
	 **/
	public void setPgMandato(java.lang.Long pgMandato)  {
		this.getMandato().setPg_mandato(pgMandato);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio reversale]
	 **/
	public java.lang.Integer getEsercizioReversale() {
		ReversaleBulk reversale = this.getReversale();
		if (reversale == null)
			return null;
		return getReversale().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio reversale]
	 **/
	public void setEsercizioReversale(java.lang.Integer esercizioReversale)  {
		this.getReversale().setEsercizio(esercizioReversale);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [CdS reversale]
	 **/
	public java.lang.String getCdCdsReversale() {
		ReversaleBulk reversale = this.getReversale();
		if (reversale == null)
			return null;
		return getReversale().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [CdS reversale]
	 **/
	public void setCdCdsReversale(java.lang.String cdCdsReversale)  {
		this.getReversale().setCd_cds(cdCdsReversale);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio reversale]
	 **/
	public java.lang.Long getPgReversale() {
		ReversaleBulk reversale = this.getReversale();
		if (reversale == null)
			return null;
		return getReversale().getPg_reversale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio reversale]
	 **/
	public void setPgReversale(java.lang.Long pgReversale)  {
		this.getReversale().setPg_reversale(pgReversale);
	}
}