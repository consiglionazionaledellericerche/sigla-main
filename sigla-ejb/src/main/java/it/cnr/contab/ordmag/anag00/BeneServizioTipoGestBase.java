/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class BeneServizioTipoGestBase extends BeneServizioTipoGestKey implements Keyed {
//    TIPO_GESTIONE VARCHAR(3) NOT NULL
	private java.lang.String tipoGestione;
 
//    DT_FIN_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dtFinValidita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BENE_SERVIZIO_TIPO_GEST
	 **/
	public BeneServizioTipoGestBase() {
		super();
	}
	public BeneServizioTipoGestBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdBeneServizio, java.sql.Timestamp dtIniValidita) {
		super(cdCds, cdMagazzino, cdBeneServizio, dtIniValidita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoGestione]
	 **/
	public java.lang.String getTipoGestione() {
		return tipoGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoGestione]
	 **/
	public void setTipoGestione(java.lang.String tipoGestione)  {
		this.tipoGestione=tipoGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFinValidita]
	 **/
	public java.sql.Timestamp getDtFinValidita() {
		return dtFinValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFinValidita]
	 **/
	public void setDtFinValidita(java.sql.Timestamp dtFinValidita)  {
		this.dtFinValidita=dtFinValidita;
	}
}