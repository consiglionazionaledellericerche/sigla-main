/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class CausaleSpesaOrdBase extends CausaleSpesaOrdKey implements Keyed {
//    DS_CAUSALE_SPESA VARCHAR(100) NOT NULL
	private java.lang.String dsCausaleSpesa;
 
//    CD_VOCE_IVA VARCHAR(10)
	private java.lang.String cdVoceIva;
 
//    FLAG_SPESE VARCHAR(1) NOT NULL
	private java.lang.Boolean flagSpese;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CAUSALE_SPESA_ORD
	 **/
	public CausaleSpesaOrdBase() {
		super();
	}
	public CausaleSpesaOrdBase(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.String cdCausaleSpesa) {
		super(cdCds, esercizio, cdCausaleSpesa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCausaleSpesa]
	 **/
	public java.lang.String getDsCausaleSpesa() {
		return dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCausaleSpesa]
	 **/
	public void setDsCausaleSpesa(java.lang.String dsCausaleSpesa)  {
		this.dsCausaleSpesa=dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public java.lang.String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(java.lang.String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagSpese]
	 **/
	public java.lang.Boolean getFlagSpese() {
		return flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagSpese]
	 **/
	public void setFlagSpese(java.lang.Boolean flagSpese)  {
		this.flagSpese=flagSpese;
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