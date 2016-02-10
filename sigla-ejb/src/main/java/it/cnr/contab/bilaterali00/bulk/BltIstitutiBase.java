/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class BltIstitutiBase extends BltIstitutiKey implements Keyed {
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String dsCdr;
 
//    SEDE_CDR VARCHAR(200)
	private java.lang.String sedeCdr;
 
//    INDIRIZZO VARCHAR(200)
	private java.lang.String indirizzo;
 
//    CO_INDIRIZZO VARCHAR(200)
	private java.lang.String coIndirizzo;
 
//    CD_CAP VARCHAR(20)
	private java.lang.String cdCap;
 
//    PG_COMUNE DECIMAL(10,0)
	private java.lang.Long pgComune;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiBase() {
		super();
	}
	public BltIstitutiBase(java.lang.String cdCentroResponsabilita) {
		super(cdCentroResponsabilita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCdr]
	 **/
	public java.lang.String getDsCdr() {
		return dsCdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCdr]
	 **/
	public void setDsCdr(java.lang.String dsCdr)  {
		this.dsCdr=dsCdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sedeCdr]
	 **/
	public java.lang.String getSedeCdr() {
		return sedeCdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sedeCdr]
	 **/
	public void setSedeCdr(java.lang.String sedeCdr)  {
		this.sedeCdr=sedeCdr;
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
	 * Restituisce il valore di: [coIndirizzo]
	 **/
	public java.lang.String getCoIndirizzo() {
		return coIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coIndirizzo]
	 **/
	public void setCoIndirizzo(java.lang.String coIndirizzo)  {
		this.coIndirizzo=coIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCap]
	 **/
	public java.lang.String getCdCap() {
		return cdCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCap]
	 **/
	public void setCdCap(java.lang.String cdCap)  {
		this.cdCap=cdCap;
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
}