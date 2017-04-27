/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class BeneServizioTipoGestKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdMagazzino;
	private java.lang.String cdBeneServizio;
	private java.sql.Timestamp dtIniValidita;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BENE_SERVIZIO_TIPO_GEST
	 **/
	public BeneServizioTipoGestKey() {
		super();
	}
	public BeneServizioTipoGestKey(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdBeneServizio, java.sql.Timestamp dtIniValidita) {
		super();
		this.cdCds=cdCds;
		this.cdMagazzino=cdMagazzino;
		this.cdBeneServizio=cdBeneServizio;
		this.dtIniValidita=dtIniValidita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof BeneServizioTipoGestKey)) return false;
		BeneServizioTipoGestKey k = (BeneServizioTipoGestKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdMagazzino(), k.getCdMagazzino())) return false;
		if (!compareKey(getCdBeneServizio(), k.getCdBeneServizio())) return false;
		if (!compareKey(getDtIniValidita(), k.getDtIniValidita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdMagazzino());
		i = i + calculateKeyHashCode(getCdBeneServizio());
		i = i + calculateKeyHashCode(getDtIniValidita());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtIniValidita]
	 **/
	public void setDtIniValidita(java.sql.Timestamp dtIniValidita)  {
		this.dtIniValidita=dtIniValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtIniValidita]
	 **/
	public java.sql.Timestamp getDtIniValidita() {
		return dtIniValidita;
	}
}