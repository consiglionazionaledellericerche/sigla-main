/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class RaggrMagazzinoBase extends RaggrMagazzinoKey implements Keyed {
//    DS_RAGGR_MAGAZZINO VARCHAR(100) NOT NULL
	private java.lang.String dsRaggrMagazzino;
 
//    TIPO VARCHAR(3) NOT NULL
	private java.lang.String tipo;
 
//    METODO VARCHAR(1) NOT NULL
	private java.lang.String metodo;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RAGGR_MAGAZZINO
	 **/
	public RaggrMagazzinoBase() {
		super();
	}
	public RaggrMagazzinoBase(java.lang.String cdCds, java.lang.String cdRaggrMagazzino) {
		super(cdCds, cdRaggrMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsRaggrMagazzino]
	 **/
	public java.lang.String getDsRaggrMagazzino() {
		return dsRaggrMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsRaggrMagazzino]
	 **/
	public void setDsRaggrMagazzino(java.lang.String dsRaggrMagazzino)  {
		this.dsRaggrMagazzino=dsRaggrMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public java.lang.String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [metodo]
	 **/
	public java.lang.String getMetodo() {
		return metodo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [metodo]
	 **/
	public void setMetodo(java.lang.String metodo)  {
		this.metodo=metodo;
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