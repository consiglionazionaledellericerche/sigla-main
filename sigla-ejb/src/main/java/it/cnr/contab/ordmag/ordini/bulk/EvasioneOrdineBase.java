/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class EvasioneOrdineBase extends EvasioneOrdineKey implements Keyed {
//    DATA_BOLLA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataBolla;
 
//    NUMERO_BOLLA VARCHAR(30) NOT NULL
	private java.lang.String numeroBolla;
 
//    DATA_CONSEGNA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataConsegna;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE
	 **/
	public EvasioneOrdineBase() {
		super();
	}
	public EvasioneOrdineBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Long numero) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataBolla]
	 **/
	public java.sql.Timestamp getDataBolla() {
		return dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataBolla]
	 **/
	public void setDataBolla(java.sql.Timestamp dataBolla)  {
		this.dataBolla=dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroBolla]
	 **/
	public java.lang.String getNumeroBolla() {
		return numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroBolla]
	 **/
	public void setNumeroBolla(java.lang.String numeroBolla)  {
		this.numeroBolla=numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataConsegna]
	 **/
	public java.sql.Timestamp getDataConsegna() {
		return dataConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataConsegna]
	 **/
	public void setDataConsegna(java.sql.Timestamp dataConsegna)  {
		this.dataConsegna=dataConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}