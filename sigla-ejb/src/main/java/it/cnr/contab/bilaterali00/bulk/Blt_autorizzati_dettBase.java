/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Blt_autorizzati_dettBase extends Blt_autorizzati_dettKey implements Keyed {
//    ANNO_VISITA DECIMAL(4,0) NOT NULL
	private java.lang.Integer annoVisita;
	
//    NUM_MAX_GG_VISITA DECIMAL(3,0) NOT NULL
	private java.lang.Integer numMaxGgVisita;

 	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_autorizzati_dettBase() {
		super();
	}
	public Blt_autorizzati_dettBase(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo, java.lang.Long pgAutorizzazione) {
		super(cdAccordo, cdProgetto, cdTerzo, pgAutorizzazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [annoVisita]
	 **/
	public java.lang.Integer getAnnoVisita() {
		return annoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [annoVisita]
	 **/
	public void setAnnoVisita(java.lang.Integer annoVisita)  {
		this.annoVisita=annoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numMaxGgVisita]
	 **/
	public java.lang.Integer getNumMaxGgVisita() {
		return numMaxGgVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numMaxGgVisita]
	 **/
	public void setNumMaxGgVisita(java.lang.Integer numMaxGgVisita)  {
		this.numMaxGgVisita=numMaxGgVisita;
	}
}