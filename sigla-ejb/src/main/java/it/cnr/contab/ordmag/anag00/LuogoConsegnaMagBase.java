/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class LuogoConsegnaMagBase extends LuogoConsegnaMagKey implements Keyed {
//    DS_LUOGO_CONSEGNA VARCHAR(100) NOT NULL
	private java.lang.String dsLuogoConsegna;
 
//    INDIRIZZO VARCHAR(100) NOT NULL
	private java.lang.String indirizzo;
 
//    CAP VARCHAR(5)
	private java.lang.String cap;
 
//    PG_COMUNE DECIMAL(10,0)
	private java.lang.Long pgComune;
 
//    PG_NAZIONE DECIMAL(10,0)
	private java.lang.Long pgNazione;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagBase() {
		super();
	}
	public LuogoConsegnaMagBase(java.lang.String cdCds, java.lang.String cdLuogoConsegna) {
		super(cdCds, cdLuogoConsegna);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsLuogoConsegna]
	 **/
	public java.lang.String getDsLuogoConsegna() {
		return dsLuogoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsLuogoConsegna]
	 **/
	public void setDsLuogoConsegna(java.lang.String dsLuogoConsegna)  {
		this.dsLuogoConsegna=dsLuogoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzo]
	 **/
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzo]
	 **/
	public void setIndirizzo(java.lang.String indirizzo)  {
		this.indirizzo=indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cap]
	 **/
	public java.lang.String getCap() {
		return cap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cap]
	 **/
	public void setCap(java.lang.String cap)  {
		this.cap=cap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgComune]
	 **/
	public java.lang.Long getPgComune() {
		return pgComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgComune]
	 **/
	public void setPgComune(java.lang.Long pgComune)  {
		this.pgComune=pgComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgNazione]
	 **/
	public java.lang.Long getPgNazione() {
		return pgNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgNazione]
	 **/
	public void setPgNazione(java.lang.Long pgNazione)  {
		this.pgNazione=pgNazione;
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