/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class CausaleSpesaOrdBase extends CausaleSpesaOrdKey implements Keyed {
//    DS_CAUSALE_SPESA VARCHAR(100) NOT NULL
	private String dsCausaleSpesa;
 
//    CD_VOCE_IVA VARCHAR(10)
	private String cdVoceIva;
 
//    FLAG_SPESE VARCHAR(1) NOT NULL
	private Boolean flagSpese;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CAUSALE_SPESA_ORD
	 **/
	public CausaleSpesaOrdBase() {
		super();
	}
	public CausaleSpesaOrdBase(String cdCds, Integer esercizio, String cdCausaleSpesa) {
		super(cdCds, esercizio, cdCausaleSpesa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCausaleSpesa]
	 **/
	public String getDsCausaleSpesa() {
		return dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCausaleSpesa]
	 **/
	public void setDsCausaleSpesa(String dsCausaleSpesa)  {
		this.dsCausaleSpesa=dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagSpese]
	 **/
	public Boolean getFlagSpese() {
		return flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagSpese]
	 **/
	public void setFlagSpese(Boolean flagSpese)  {
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