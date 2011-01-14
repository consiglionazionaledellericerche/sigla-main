/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class CupBase extends CupKey implements Keyed {
//    DESCRIZIONE VARCHAR(200) NOT NULL
	private java.lang.String descrizione;
 
	private java.sql.Timestamp dt_canc;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CUP
	 **/
	public CupBase() {
		super();
	}
	public CupBase(java.lang.String cdCup) {
		super(cdCup);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizione]
	 **/
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizione]
	 **/
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.sql.Timestamp getDt_canc() {
		return dt_canc;
	}
	public void setDt_canc(java.sql.Timestamp dt_canc) {
		this.dt_canc = dt_canc;
	}
}