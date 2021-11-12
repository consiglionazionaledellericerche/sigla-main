/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ciclo_dottoratiBase extends Ciclo_dottoratiKey implements Keyed {
//    NUMERO DECIMAL(38,0) NOT NULL
	private java.lang.Long numero;
 
//    ANNO_INIZIO INT
	private Integer annoInizio;
 
//    ANNO_FINE INT
	private Integer annoFine;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CICLO_DOTTORATI
	 **/
	public Ciclo_dottoratiBase() {
		super();
	}
	public Ciclo_dottoratiBase(java.lang.Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public java.lang.Long getNumero() {
		return numero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public void setNumero(java.lang.Long numero)  {
		this.numero=numero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [annoInizio]
	 **/
	public Integer getAnnoInizio() {
		return annoInizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [annoInizio]
	 **/
	public void setAnnoInizio(Integer annoInizio)  {
		this.annoInizio=annoInizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [annoFine]
	 **/
	public Integer getAnnoFine() {
		return annoFine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [annoFine]
	 **/
	public void setAnnoFine(Integer annoFine)  {
		this.annoFine=annoFine;
	}
}