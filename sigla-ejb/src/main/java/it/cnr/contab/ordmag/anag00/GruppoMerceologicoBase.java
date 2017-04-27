/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class GruppoMerceologicoBase extends GruppoMerceologicoKey implements Keyed {
//    DS_GRUPPO_MERCEOLOGICO VARCHAR(100) NOT NULL
	private java.lang.String dsGruppoMerceologico;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: GRUPPO_MERCEOLOGICO
	 **/
	public GruppoMerceologicoBase() {
		super();
	}
	public GruppoMerceologicoBase(java.lang.String cdGruppoMerceologico) {
		super(cdGruppoMerceologico);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsGruppoMerceologico]
	 **/
	public java.lang.String getDsGruppoMerceologico() {
		return dsGruppoMerceologico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsGruppoMerceologico]
	 **/
	public void setDsGruppoMerceologico(java.lang.String dsGruppoMerceologico)  {
		this.dsGruppoMerceologico=dsGruppoMerceologico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}