/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class NotaPrecodificataBase extends NotaPrecodificataKey implements Keyed {
//    NOTA VARCHAR(1000) NOT NULL
	private java.lang.String nota;
 
//    NOTA2 VARCHAR(4000)
	private java.lang.String nota2;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NOTA_PRECODIFICATA
	 **/
	public NotaPrecodificataBase() {
		super();
	}
	public NotaPrecodificataBase(java.lang.String cdCds, java.lang.String cdNotaPrecodificata) {
		super(cdCds, cdNotaPrecodificata);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nota]
	 **/
	public java.lang.String getNota() {
		return nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nota]
	 **/
	public void setNota(java.lang.String nota)  {
		this.nota=nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nota2]
	 **/
	public java.lang.String getNota2() {
		return nota2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nota2]
	 **/
	public void setNota2(java.lang.String nota2)  {
		this.nota2=nota2;
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