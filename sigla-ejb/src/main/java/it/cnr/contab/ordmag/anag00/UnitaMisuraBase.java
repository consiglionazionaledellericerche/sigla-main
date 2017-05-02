/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class UnitaMisuraBase extends UnitaMisuraKey implements Keyed {
//    DS_UNITA_MISURA VARCHAR(100) NOT NULL
	private java.lang.String dsUnitaMisura;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_MISURA
	 **/
	public UnitaMisuraBase() {
		super();
	}
	public UnitaMisuraBase(java.lang.String cdUnitaMisura) {
		super(cdUnitaMisura);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsUnitaMisura]
	 **/
	public java.lang.String getDsUnitaMisura() {
		return dsUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsUnitaMisura]
	 **/
	public void setDsUnitaMisura(java.lang.String dsUnitaMisura)  {
		this.dsUnitaMisura=dsUnitaMisura;
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