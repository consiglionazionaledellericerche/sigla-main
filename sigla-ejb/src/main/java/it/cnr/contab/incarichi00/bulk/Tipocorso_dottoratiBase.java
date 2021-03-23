/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipocorso_dottoratiBase extends Tipocorso_dottoratiKey implements Keyed {
//    NAME VARCHAR(250) NOT NULL
	private java.lang.String name;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPOCORSO_DOTTORATI
	 **/
	public Tipocorso_dottoratiBase() {
		super();
	}
	public Tipocorso_dottoratiBase(java.lang.Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [name]
	 **/
	public java.lang.String getName() {
		return name;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [name]
	 **/
	public void setName(java.lang.String name)  {
		this.name=name;
	}
}